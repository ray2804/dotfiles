/*
 *  RapidMiner
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.rapidminer.operator.tools;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.AttributeRole;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.set.Condition;
import com.rapidminer.example.set.ConditionCreationException;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.UserError;
import com.rapidminer.operator.ProcessSetupError.Severity;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.operator.ports.metadata.AttributeMetaData;
import com.rapidminer.operator.ports.metadata.CompatibilityLevel;
import com.rapidminer.operator.ports.metadata.ExampleSetMetaData;
import com.rapidminer.operator.ports.metadata.MetaData;
import com.rapidminer.operator.ports.metadata.MetaDataInfo;
import com.rapidminer.operator.ports.metadata.Precondition;
import com.rapidminer.operator.ports.metadata.SimpleMetaDataError;
import com.rapidminer.operator.ports.quickfix.AbstractQuickFix;
import com.rapidminer.operator.ports.quickfix.QuickFix;
import com.rapidminer.operator.preprocessing.filter.attributes.AttributeFilterCondition;
import com.rapidminer.operator.preprocessing.filter.attributes.BlockTypeAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.NoMissingValuesAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.NumericValueAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.RegexpAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.SingleAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.SubsetAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.TransparentAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.ValueTypeAttributeFilter;
import com.rapidminer.operator.preprocessing.filter.attributes.AttributeFilterCondition.ScanResult;
import com.rapidminer.parameter.ParameterHandler;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.parameter.conditions.EqualTypeCondition;
import com.rapidminer.tools.Ontology;


/**
 * An AttributeSubsetSelector provides conditions for filtering
 * attributes from example sets. It also provides parameters for
 * the selection of subsets which can be used in operators that
 * should work on a subset of the available attributes. According
 * to the specified parameters, the available methods select the
 * subsets in both meta data and actual data.
 * 
 * @author Sebastian Land, Tobias Malbrecht
 */
public class AttributeSubsetSelector {
	/** The parameter name for &quot;Implementation of the condition.&quot; */
	public static final String PARAMETER_FILTER_TYPE = "attribute_filter_type";

	/** The parameter name for &quot;Indicates if only examples should be accepted which would normally filtered.&quot; */
	public static final String PARAMETER_INVERT_SELECTION = "invert_selection";

	public static final String PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES = "include_special_attributes";

	public static String[] CONDITION_NAMES = new String[] { "all", "single", "subset", "regular_expression", "value_type", "block_type", "no_missing_values", "numeric_value_filter" };

	private static Class[] CONDITION_IMPLEMENTATIONS = { TransparentAttributeFilter.class, SingleAttributeFilter.class, SubsetAttributeFilter.class, RegexpAttributeFilter.class, ValueTypeAttributeFilter.class, BlockTypeAttributeFilter.class , NoMissingValuesAttributeFilter.class, NumericValueAttributeFilter.class };

	public static final int CONDITION_ALL = 0;

	public static final int CONDITION_SINGLE = 1;

	public static final int CONDITION_SUBSET = 2;

	public static final int CONDITION_REGULAR_EXPRESSION = 3;

	public static final int CONDITION_VALUE_TYPE = 5;

	public static final int CONDITION_BLOCK_TYPE = 6;

	public static final int CONDITION_NO_MISSING_VALUES = 7;

	public static final int CONDITION_NUMERIC_VALUE_FILTER = 8;

	private final ParameterHandler operator;

	private final InputPort inPort;

	private int[] valueTypes;


	public AttributeSubsetSelector(ParameterHandler operator, InputPort inPort) {
		this(operator, inPort, Ontology.ATTRIBUTE_VALUE);
	}

	public AttributeSubsetSelector(ParameterHandler operator, InputPort inPort, int...valueTypes) {
		this.operator = operator;
		this.inPort = inPort;
		if (valueTypes.length == 0) {
			this.valueTypes = new int[] { Ontology.ATTRIBUTE_VALUE };
		} else {
			this.valueTypes = valueTypes;
		}
	}

	/**
	 * This method returns the meta data of an exampleset as if it
	 * would have been filtered. If exceptions are thrown because
	 * of wrong or missing parameter settings, null will be returned.
	 * Please keep in mind that the resulting ExampleSetMetaData are only
	 * a clone of the original one. Changes must hence be performed on the
	 * orginial exampleSetMetaData object.
	 */
	public ExampleSetMetaData getMetaDataSubset(ExampleSetMetaData metaData, boolean keepSpecialIfNotIncluded) {
		try {
			Collection<AttributeMetaData> attributes = metaData.getAllAttributes();
			AttributeFilterCondition condition = createCondition(CONDITION_NAMES[operator.getParameterAsInt(PARAMETER_FILTER_TYPE)], operator);
			ExampleSetMetaData resultSet = metaData.clone();

			// init
			boolean invert = operator.getParameterAsBoolean(PARAMETER_INVERT_SELECTION);
			boolean applyOnSpecial = operator.getParameterAsBoolean(PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES);
			boolean foundUnknown = false;

			for (AttributeMetaData attribute : attributes) {
				if (!attribute.isSpecial() || (attribute.isSpecial() && applyOnSpecial)) {
					MetaDataInfo result = condition.isFilteredOutMetaData(attribute, operator);
					if (invert) {
						if (result == MetaDataInfo.NO)
							result = MetaDataInfo.YES;
						else if (result == MetaDataInfo.YES)
							result = MetaDataInfo.NO;
					}
					if (result == MetaDataInfo.YES)
						resultSet.removeAttribute(attribute);
					if (result == MetaDataInfo.UNKNOWN)
						foundUnknown = true;
				} else if (!keepSpecialIfNotIncluded) {					
					resultSet.removeAttribute(attribute);
				}
			}
			if (applyOnSpecial) {
				for (AttributeMetaData attribute : resultSet.getAllAttributes()) {
					attribute.setRegular();
				}
			}

			// now remove non-special attributes of wrong type
			Iterator<AttributeMetaData> iterator = resultSet.getAllAttributes().iterator();
			while (iterator.hasNext()) {
				AttributeMetaData attribute = iterator.next();
				if (!attribute.isSpecial()) {
					boolean keep = isOfAllowedType(attribute.getValueType());
					if (!keep)
						iterator.remove();
				}
			}

			// now check if unknown was found: Set set relation
			if (foundUnknown)
				resultSet.attributesAreSubset();

			switch (metaData.getAttributeSetRelation()) {
			case SUBSET: 
				resultSet.attributesAreSubset();
				break;
			case SUPERSET: 
				resultSet.attributesAreSuperset();
				break;
			}
			return resultSet;
		} catch (ConditionCreationException e) {
			return new ExampleSetMetaData();
		} catch (UndefinedParameterError e) {
			return new ExampleSetMetaData();
		}
	}

	private boolean isOfAllowedType(int attributeValueType) {
		boolean keep = false;
		for (int type: valueTypes) {
			if (Ontology.ATTRIBUTE_VALUE_TYPE.isA(attributeValueType, type)) {
				keep = true;
				break;
			}
		}
		return keep;
	}

	/**
	 * Returns the example subset which has been filtered
	 * according to the parameter settings.
	 * 
	 * @param parent the example set which should be filtered
	 * @param keepSpecialIfNotIncluded keep the special attributes if they are note included
	 * @return the filtered example set error
	 * @throws UndefinedParameterError necessary parameter not defined
	 * @throws UserError condition could not be created
	 */
	public ExampleSet getSubset(ExampleSet parent, boolean keepSpecialIfNotIncluded) throws UndefinedParameterError, UserError {
		boolean includeSpecial = operator.getParameterAsBoolean(PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES);
		ExampleSet clone = (ExampleSet) parent.clone();
		Set<Attribute> attributeSubset = getAttributeSubset(clone, keepSpecialIfNotIncluded);
		Iterator<Attribute> iterator = clone.getAttributes().allAttributes();
		while (iterator.hasNext()) {
			Attribute currentAttribute = iterator.next();
			if (!attributeSubset.contains(currentAttribute))
				iterator.remove();
			else if (includeSpecial) { // if special are included: Set them to regular
				AttributeRole currentRole = clone.getAttributes().getRole(currentAttribute);
				if (currentRole.isSpecial())
					currentRole.changeToRegular();
			}
		}
		return clone;
	}

	/**
	 * Returns a set of attributes containing the original (!) attributes
	 * of the given example set which match the filter
	 * settings of the parameter. Please keep in mind that this method does not clone the
	 * example set like the getSubset method. If you change the attributes in the subset, 
	 * you will alter them in the given example set.
	 * 
	 * @param exampleSet the original example set
	 * @param keepSpecialIfNotIncluded keep the special attributes if they are not included
	 * @return a set of attributes matching the filters
	 * @throws UndefinedParameterError
	 * @throws UserError
	 */
	public Set<Attribute> getAttributeSubset(ExampleSet exampleSet, boolean keepSpecialIfNotIncluded) throws UndefinedParameterError, UserError {
		try {
			Attributes attributes = exampleSet.getAttributes();
			AttributeFilterCondition condition = createCondition(CONDITION_NAMES[operator.getParameterAsInt(PARAMETER_FILTER_TYPE)], operator);

			// adding attributes passing non scan check
			boolean invert = operator.getParameterAsBoolean(PARAMETER_INVERT_SELECTION);
			Set<Attribute> remainingAttributes = new LinkedHashSet<Attribute>();

			// use iterator for including special attributes
			Iterator<Attribute> iterator = null;
			if (operator.getParameterAsBoolean(PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES)) {
				iterator = attributes.allAttributes();
			} else {
				iterator = attributes.iterator();
			}

			while (iterator.hasNext()) {
				Attribute attribute = iterator.next();
				//check if it is allowed anyway
				if (isOfAllowedType(attribute.getValueType())) { 
					ScanResult result = condition.beforeScanCheck(attribute).invert(invert); 
					switch (result) {
					case KEEP:
					case UNCHECKED:
						remainingAttributes.add(attribute);
						break;
					}
				}
			}

			// now checking for every example
			if (condition.isNeedingScan()) {
				Iterator<Attribute> r = remainingAttributes.iterator();
				while (r.hasNext()) {
					Attribute attribute = r.next();
					ScanResult afterScanResult = ScanResult.UNCHECKED;
					// now iterates over all examples as long as unchecked is returned
					for (Example example : exampleSet) {
						ScanResult result = condition.check(attribute, example); 
						if (result != ScanResult.UNCHECKED) {
							afterScanResult = result;
							break;
						}
					}

					// if still is unchecked, then attribute has never been removed or needs full scan 
					if (condition.isNeedingFullScan())
						afterScanResult = condition.checkAfterFullScan();
					else {
						// if not needing full scan and still unchecked then it has never been removed (=keep)
						if (afterScanResult == ScanResult.UNCHECKED)
							afterScanResult = ScanResult.KEEP;
					}

					// now inverting, cannot be unchecked now
					afterScanResult = afterScanResult.invert(invert);
					if (afterScanResult == ScanResult.REMOVE)
						attributes.remove(attribute);
				}
			}

			if (keepSpecialIfNotIncluded && !operator.getParameterAsBoolean(PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES)) {
				Iterator<AttributeRole> roleIterator = attributes.allAttributeRoles();
				while (roleIterator.hasNext()) {
					AttributeRole currentRole = roleIterator.next();
					if (currentRole.isSpecial())
						remainingAttributes.add(currentRole.getAttribute());
				}
			}
			return remainingAttributes;
		} catch (ConditionCreationException e) {
			if (operator instanceof Operator)
				throw new UserError((Operator) operator, e, 904, CONDITION_NAMES[operator.getParameterAsInt(PARAMETER_FILTER_TYPE)], e.getMessage());
			else
				throw new UserError(null, e, 904, CONDITION_NAMES[operator.getParameterAsInt(PARAMETER_FILTER_TYPE)], e.getMessage());
		}
	}

	/**
	 * Checks if the given name is the short name of a known condition
	 * and creates it. If the name is not known, this method creates
	 * a new instance of className which must be an implementation of
	 * {@link Condition} by calling its two argument constructor
	 * passing it the example set and the parameter string.
	 */
	public static AttributeFilterCondition createCondition(String name, ParameterHandler operator) throws ConditionCreationException {
		try {
			for (int i = 0; i < CONDITION_NAMES.length; i++) {
				if (CONDITION_NAMES[i].equals(name)) {
					AttributeFilterCondition condition = (AttributeFilterCondition) CONDITION_IMPLEMENTATIONS[i].newInstance();
					condition.init(operator);
					return condition;
				}
			}
			throw new ConditionCreationException("Cannot find class '" + name + "'. Check your classpath.");
		} catch (IllegalAccessException e) {
			throw new ConditionCreationException("'" + name + "' cannot access two argument constructor " + name + "(ExampleSet, String)!", e);
		} catch (InstantiationException e) {
			throw new ConditionCreationException(name + ": cannot create condition (" + e.getMessage() + ").", e);
		} catch (Throwable e) {
			throw new ConditionCreationException(name + ": cannot create condition (" + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()) + ").", e);
		}
	}

	/**
	 * This method allows registering filter conditions defined
	 * in plugins.
	 */
	public static void registerCondition(String conditionName, Class<? extends AttributeFilterCondition> conditionClass) {
		String[] newConditionNames = new String[CONDITION_NAMES.length + 1];
		System.arraycopy(CONDITION_NAMES, 0, newConditionNames, 0, CONDITION_NAMES.length);
		newConditionNames[newConditionNames.length - 1] = conditionName;
		CONDITION_NAMES = newConditionNames;

		Class[] newConditionClasses = new Class[CONDITION_IMPLEMENTATIONS.length + 1];
		System.arraycopy(CONDITION_IMPLEMENTATIONS, 0, newConditionClasses, 0, CONDITION_IMPLEMENTATIONS.length);
		newConditionClasses[newConditionClasses.length - 1] = conditionClass;
		CONDITION_IMPLEMENTATIONS = newConditionClasses;
	}

	/**
	 * This method creates the parameter types needed to filter
	 * attributes from example sets.
	 */
	public List<ParameterType> getParameterTypes() {
		List<ParameterType> types = new LinkedList<ParameterType>();
		ParameterType type = new ParameterTypeCategory(PARAMETER_FILTER_TYPE, "The condition specifies which attributes are selected or affected by this operator.", CONDITION_NAMES, 0);
		type.setExpert(false);
		types.add(type);

		for (int i = 0; i < CONDITION_IMPLEMENTATIONS.length; i++) {
			Collection<ParameterType> filterConditions;
			try {
				filterConditions = ((AttributeFilterCondition) CONDITION_IMPLEMENTATIONS[i].newInstance()).getParameterTypes(operator, inPort, valueTypes);
				for (ParameterType conditionalType : filterConditions) {
					types.add(conditionalType);
					conditionalType.registerDependencyCondition(new EqualTypeCondition(operator, PARAMETER_FILTER_TYPE, CONDITION_NAMES, !conditionalType.isExpert(), i));
				}
				// can't do anything about it
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {
			} catch (SecurityException e) {
			}
		}

		type = new ParameterTypeBoolean(PARAMETER_INVERT_SELECTION, "Indicates if only attributes should be accepted which would normally filtered.", false);
		type.setExpert(false);
		types.add(type);
		type = new ParameterTypeBoolean(PARAMETER_INCLUDE_SPECIAL_ATTRIBUTES, "Indicate if this operator should also be applied on the special attributes. Otherwise they are always kept.", false);
		type.setExpert(false);
		types.add(type);

		return types;
	}

	public Precondition makePrecondition() {
		return new Precondition() {
			@Override
			public void assumeSatisfied() { }

			@Override
			public void check(MetaData metaData) {
				if (metaData instanceof ExampleSetMetaData) {
					ExampleSetMetaData subsetMetaData = getMetaDataSubset((ExampleSetMetaData) metaData, false);
					if (subsetMetaData.getAllAttributes().isEmpty()) {
						QuickFix selectAllQuickFix = new AbstractQuickFix(4, false, "attributefilter_select_all") {
							@Override
							public void apply() {
								operator.getParameters().setParameter(PARAMETER_FILTER_TYPE, CONDITION_NAMES[CONDITION_ALL]);
							}
						};
						SimpleMetaDataError error = new SimpleMetaDataError(Severity.WARNING, inPort, Collections.<QuickFix>singletonList(selectAllQuickFix ), "attribute_selection_empty");
						inPort.addError(error);
					}
				}
			}

			@Override
			public String getDescription() {
				return "Example set matching at least one selected attribute.";
			}

			@Override
			public MetaData getExpectedMetaData() {
				return new ExampleSetMetaData();
			}

			@Override
			public boolean isCompatible(MetaData input, CompatibilityLevel level) {
				return ExampleSet.class.isAssignableFrom(input.getObjectClass());
			}
			
		};
	}
}
