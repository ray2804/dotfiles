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
package com.rapidminer.test.asserter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.ComparisonFailure;

import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.SparseDataRow;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.IOObjectCollection;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.meta.ParameterSet;
import com.rapidminer.operator.meta.ParameterValue;
import com.rapidminer.operator.nio.file.FileObject;
import com.rapidminer.operator.performance.PerformanceCriterion;
import com.rapidminer.operator.performance.PerformanceVector;
import com.rapidminer.operator.visualization.dependencies.NumericalMatrix;
import com.rapidminer.test_utils.Asserter;
import com.rapidminer.test_utils.RapidAssert;
import com.rapidminer.tools.Tools;
import com.rapidminer.tools.math.Averagable;
import com.rapidminer.tools.math.AverageVector;

/**
 * @author Marius Helf
 *
 */
public class AsserterFactoryRapidMiner implements AsserterFactory {

	@Override
	public List<Asserter> createAsserters() {
		List<Asserter> asserters = new LinkedList<Asserter>();
		
		/* asserter for ParameterSet */
		asserters.add(new Asserter() {

			@Override
			public Class<?> getAssertable() {
				return ParameterSet.class;
			}

			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				ParameterSet expected = (ParameterSet) expectedObj;
				ParameterSet actual = (ParameterSet) actualObj;

				RapidAssert.assertEquals(message + " (performance vectors do not match)", expected.getPerformance(), actual.getPerformance());

				Iterator<ParameterValue> expectedIt = expected.getParameterValues();
				Iterator<ParameterValue> actualIt = actual.getParameterValues();

				while (expectedIt.hasNext()) {
					RapidAssert.assertTrue(message + "(expected parameter vector is longer than actual parameter vector)", actualIt.hasNext());
					ParameterValue expectedParValue = expectedIt.next();
					ParameterValue actualParValue = actualIt.next();
					RapidAssert.assertEquals(message + " (parameter values)", expectedParValue, actualParValue);
				}
				RapidAssert.assertFalse(message + "(expected parameter vector is shorter than actual parameter vector)", actualIt.hasNext());
			}
		});

		/* asserter for PerformanceCriterion */
		asserters.add(new Asserter() {

			/**
			 * Tests for equality by testing all averages, standard deviation and variances, as well as the fitness, max fitness 
			 * and example count.
			 *  
			 * @param message		message to display if an error occurs
			 * @param expected		expected criterion
			 * @param actual		actual criterion
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				PerformanceCriterion expected = (PerformanceCriterion) expectedObj;
				PerformanceCriterion actual = (PerformanceCriterion) actualObj;

				List<Asserter> averegableAsserter = RapidAssert.ASSERTER_REGISTRY.getAsserterForClass(Averagable.class);
				if (averegableAsserter != null) {
					for (Asserter asserter : averegableAsserter) {
						asserter.assertEquals(message, (Averagable) expected, (Averagable) actual);
					}
				} else {
					throw new ComparisonFailure("Comparison of " + Averagable.class.toString() + " is not supported. ", expectedObj.toString(), actualObj.toString());
				}
				Assert.assertEquals(message + " (fitness is not equal)", expected.getFitness(), actual.getFitness());
				Assert.assertEquals(message + " (max fitness is not equal)", expected.getMaxFitness(), actual.getMaxFitness());
				Assert.assertEquals(message + " (example count is not equal)", expected.getExampleCount(), actual.getExampleCount());
			}

			@Override
			public Class<?> getAssertable() {
				return PerformanceCriterion.class;
			}
		});

		asserters.add(new Asserter() {

			/**
			 * Tests for equality by testing all averages, standard deviation and variances.
			 * 
			 * @param message		message to display if an error occurs
			 * @param expected		expected averagable
			 * @param actual		actual averagable
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				Averagable expected = (Averagable) expectedObj;
				Averagable actual = (Averagable) actualObj;

				Assert.assertEquals(message + " (average is not equal)", expected.getAverage(), actual.getAverage());
				Assert.assertEquals(message + " (makro average is not equal)", expected.getMakroAverage(), actual.getMakroAverage());
				Assert.assertEquals(message + " (mikro average is not equal)", expected.getMikroAverage(), actual.getMikroAverage());
				Assert.assertEquals(message + " (average count is not equal)", expected.getAverageCount(), actual.getAverageCount());
				Assert.assertEquals(message + " (makro standard deviation is not equal)", expected.getMakroStandardDeviation(), actual.getMakroStandardDeviation());
				Assert.assertEquals(message + " (mikro standard deviation is not equal)", expected.getMikroStandardDeviation(), actual.getMikroStandardDeviation());
				Assert.assertEquals(message + " (standard deviation is not equal)", expected.getStandardDeviation(), actual.getStandardDeviation());
				Assert.assertEquals(message + " (makro variance is not equal)", expected.getMakroVariance(), actual.getMakroVariance());
				Assert.assertEquals(message + " (mikro variance is not equal)", expected.getMikroVariance(), actual.getMikroVariance());
				Assert.assertEquals(message + " (variance is not equal)", expected.getVariance(), actual.getVariance());

			}

			@Override
			public Class<?> getAssertable() {
				return Averagable.class;
			}
		});

		asserters.add(new Asserter() {

			/**
			 * Tests the two average vectors for equality by testing the size and each averagable.
			 * 
			 * @param message		message to display if an error occurs
			 * @param expected		expected vector
			 * @param actual		actual vector
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				AverageVector expected = (AverageVector) expectedObj;
				AverageVector actual = (AverageVector) actualObj;

				message = message + "Average vectors are not equals";

				int expSize = expected.getSize();
				int actSize = actual.getSize();
				Assert.assertEquals(message + " (size of the average vector is not equal)", expSize, actSize);
				int size = expSize;

				for (int i = 0; i < size; i++) {
					RapidAssert.assertEquals(message, expected.getAveragable(i), actual.getAveragable(i));
				}
			}

			@Override
			public Class<?> getAssertable() {
				return AverageVector.class;
			}

		});

		// Asserter for ExampleSet
		asserters.add(new Asserter() {

			/**
			 * Tests two example sets by iterating over all examples.
			 * 
			 * @param message		message to display if an error occurs
			 * @param expected		expected value
			 * @param actual		actual value
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				ExampleSet expected = (ExampleSet) expectedObj;
				ExampleSet actual = (ExampleSet) actualObj;

				message = message + " - ExampleSets are not equal";

				boolean compareAttributeDefaultValues = true;
				if (expected.getExampleTable().size() > 0) {
					compareAttributeDefaultValues = expected.getExampleTable().getDataRow(0) instanceof SparseDataRow;
				}

				// compare attributes
				RapidAssert.assertEquals(message, expected.getAttributes(), actual.getAttributes(), compareAttributeDefaultValues);

				// compare number of examples
				Assert.assertEquals(message + " (number of examples)", expected.size(), actual.size());

				// compare example values
				Iterator<Example> i1 = expected.iterator();
				Iterator<Example> i2 = actual.iterator();
				int row = 1;
				while (i1.hasNext() && i2.hasNext()) {
					RapidAssert.assertEquals(message + "(example number " + row + ", {0} value of {1})", i1.next(), i2.next());
					row++;
				}
			}

			@Override
			public Class<?> getAssertable() {
				return ExampleSet.class;
			}
		});

		asserters.add(new Asserter() {

			/**
			 * Tests the collection of ioobjects
			 * 
			 * @param expected
			 * @param actual
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				@SuppressWarnings("unchecked")
				IOObjectCollection<IOObject> expected = (IOObjectCollection) expectedObj;
				@SuppressWarnings("unchecked")
				IOObjectCollection<IOObject> actual = (IOObjectCollection) actualObj;

				message = message + "Collection of IOObjects are not equal: ";
				Assert.assertEquals(message + " (number of items)", expected.size(), actual.size());
				RapidAssert.assertEquals(message, expected.getObjects(), actual.getObjects());
			}

			@Override
			public Class<?> getAssertable() {
				return IOObjectCollection.class;
			}

		});

		asserters.add(new Asserter() {

			/**
			 * Test two numerical matrices for equality. This contains tests about the number of columns and rows, as well as column&row names and if
			 * the matrices are marked as symmetrical and if every value within the matrix is equal.
			 *  
			 * @param message		message to display if an error occurs
			 * @param expected		expected matrix
			 * @param actual		actual matrix
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				NumericalMatrix expected = (NumericalMatrix) expectedObj;
				NumericalMatrix actual = (NumericalMatrix) actualObj;

				message = message + "Numerical matrices are not equal";

				int expNrOfCols = expected.getNumberOfColumns();
				int actNrOfCols = actual.getNumberOfColumns();
				Assert.assertEquals(message + " (column number is not equal)", expNrOfCols, actNrOfCols);

				int expNrOfRows = expected.getNumberOfRows();
				int actNrOfRows = actual.getNumberOfRows();
				Assert.assertEquals(message + " (row number is not equal)", expNrOfRows, actNrOfRows);

				int cols = expNrOfCols;
				int rows = expNrOfRows;

				for (int col = 0; col < cols; col++) {
					String expectedColName = expected.getColumnName(col);
					String actualColName = actual.getColumnName(col);
					Assert.assertEquals(message + " (column name at index " + col + " is not equal)", expectedColName, actualColName);
				}

				for (int row = 0; row < rows; row++) {
					String expectedRowName = expected.getRowName(row);
					String actualRowName = actual.getRowName(row);
					Assert.assertEquals(message + " (row name at index " + row + " is not equal)", expectedRowName, actualRowName);
				}

				Assert.assertEquals(message + " (matrix symmetry is not equal)", expected.isSymmetrical(), actual.isSymmetrical());

				for (int row = 0; row < rows; row++) {
					for (int col = 0; col < cols; col++) {

						double expectedVal = expected.getValue(row, col);
						double actualVal = actual.getValue(row, col);
						Assert.assertEquals(message + " (value at row " + row + " and column " + col + " is not equal)", expectedVal, actualVal);

					}
				}

			}

			@Override
			public Class<?> getAssertable() {
				return NumericalMatrix.class;
			}

		});

		asserters.add(new Asserter() {

			/**
			 * Tests the two performance vectors for equality by testing the size, the criteria names, the main criterion and each criterion.
			 * 
			 * @param message		message to display if an error occurs
			 * @param expected		expected vector
			 * @param actual		actual vector
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) {
				PerformanceVector expected = (PerformanceVector) expectedObj;
				PerformanceVector actual = (PerformanceVector) actualObj;

				message = message + "Performance vectors are not equal";

				int expSize = expected.getSize();
				int actSize = actual.getSize();
				Assert.assertEquals(message + " (size of the performance vector is not equal)", expSize, actSize);
				int size = expSize;

				RapidAssert.assertArrayEquals(message, expected.getCriteriaNames(), actual.getCriteriaNames());
				RapidAssert.assertEquals(message, expected.getMainCriterion(), actual.getMainCriterion());

				for (int i = 0; i < size; i++) {
					RapidAssert.assertEquals(message, expected.getCriterion(i), actual.getCriterion(i));
				}
			}

			@Override
			public Class<?> getAssertable() {
				return PerformanceVector.class;
			}
		});

		asserters.add(new Asserter() {

			/**
			 * Tests the two file objects for equality by testing the
			 * 
			 * 
			 * @param message
			 *            message to display if an error occurs
			 * @param expected
			 *            expected file object
			 * @param actual
			 *            actual file object
			 */
			@Override
			public void assertEquals(String message, Object expectedObj, Object actualObj) throws RuntimeException {
				FileObject fo1 = (FileObject) expectedObj;
				FileObject fo2 = (FileObject) actualObj;
				InputStream is1 = null;
				InputStream is2 = null;
				ByteArrayOutputStream bs1 = null;
				ByteArrayOutputStream bs2 = null;
				try {
					is1 = fo1.openStream();
					is2 = fo2.openStream();
					bs1 = new ByteArrayOutputStream();
					bs2 = new ByteArrayOutputStream();
					Tools.copyStreamSynchronously(is1, bs1, true);
					Tools.copyStreamSynchronously(is2, bs2, true);
					byte[] fileData1 = bs1.toByteArray();
					byte[] fileData2 = bs2.toByteArray();
					RapidAssert.assertArrayEquals("file object data", fileData1, fileData2);
				} catch (OperatorException e) {
					throw new RuntimeException("Stream Error");
				} catch (IOException e) {
					throw new RuntimeException("Stream Error");
				} finally {
					if (is1 != null) {
						try {
							is1.close();
						} catch (IOException e) {
							// silent
						}
					}
					if (is2 != null) {
						try {
							is2.close();
						} catch (IOException e) {
							// silent
						}
					}
					if (bs1 != null) {
						try {
							bs1.close();
						} catch (IOException e) {
							// silent
						}
					}
					if (bs2 != null) {
						try {
							bs2.close();
						} catch (IOException e) {
							// silent
						}
					}
				}
			}

			@Override
			public Class<?> getAssertable() {
				return FileObject.class;
			}
		});
		
		return asserters;
	}

}
