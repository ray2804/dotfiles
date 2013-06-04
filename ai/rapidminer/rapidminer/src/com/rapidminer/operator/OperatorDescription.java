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
package com.rapidminer.operator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.io.process.XMLTools;
import com.rapidminer.tools.GenericOperatorFactory;
import com.rapidminer.tools.GroupTree;
import com.rapidminer.tools.OperatorService;
import com.rapidminer.tools.XMLException;
import com.rapidminer.tools.documentation.OperatorDocBundle;
import com.rapidminer.tools.documentation.OperatorDocumentation;
import com.rapidminer.tools.plugin.Plugin;

/**
 * Data container for name, class, short name, path and the (very short)
 * description of an operator. If the corresponding operator is not marked
 * as deprecated the deprecation info string should be null. If the icon
 * string is null, the group icon will be used.
 * 
 * @author Ingo Mierswa
 */
public class OperatorDescription implements Comparable<OperatorDescription> {
    private static final ImageIcon[] EMPTY_ICONS = new ImageIcon[3];
    private final String key;
    private final Class<? extends Operator> clazz;
    private List<String> replacesDeprecatedKeys;

    private final OperatorDocumentation documentation;

    private ImageIcon[] icons;

    private String fullyQualifiedGroupKey;

    /**
     * @deprecated Only used for Weka
     */
    @Deprecated
    private final String deprecationInfo = null;

    private String iconName;

    private final Plugin provider;

    private boolean enabled = true;

    /**
     * Parses an operator in the RM 5.0 xml standard for operator definitions.
     * In contrast to earlier versions, the {@link OperatorDescription} does not
     * register themselves on the OperatorTree. This is now handled centralized by the {@link OperatorService}.
     * 
     * @param bundle
     */
    @SuppressWarnings("unchecked")
    public OperatorDescription(String fullyQualifiedGroupKey, Element element, ClassLoader classLoader, Plugin provider, OperatorDocBundle bundle) throws ClassNotFoundException, XMLException {
        this.provider = provider;
        this.fullyQualifiedGroupKey = fullyQualifiedGroupKey;

        key = XMLTools.getTagContents(element, "key", true);
        setIconName(XMLTools.getTagContents(element, "icon"));

        Class<?> generatedClass = Class.forName(XMLTools.getTagContents(element, "class", true).trim(), true, classLoader);
        this.clazz = (Class<? extends Operator>) generatedClass;

        this.documentation = (OperatorDocumentation) bundle.getObject("operator." + key);
        if (documentation.getName().equals("")) {
            documentation.setName(key);
            documentation.setDocumentation("Operator's description is missing in referenced OperatorDoc.");
        }

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element && ((Element) child).getTagName().equals("replaces")) {
                setIsReplacementFor(((Element) child).getTextContent());
            }
        }
    }

    /** Constructor for programmatic (non-parsed) creation of OperatorDescriptions, e.g. by a {@link GenericOperatorFactory}. */
    public OperatorDescription(String fullyQualifiedGroupKey, String key, Class<? extends Operator> clazz, ClassLoader classLoader, String iconName, Plugin provider) {
        this(fullyQualifiedGroupKey, key, clazz, classLoader, iconName, provider, null);
    }

    /**
     * Constructor for programmatic (non-parsed) creation of OperatorDescriptions, e.g. by a {@link GenericOperatorFactory}.
     * Additionally this allows to specify an operator documentation bundle where the docu is retrieved from.
     * */
    public OperatorDescription(String fullyQualifiedGroupKey, String key, Class<? extends Operator> clazz, ClassLoader classLoader, String iconName, Plugin provider, OperatorDocBundle bundle) {
        this.key = key;
        this.clazz = clazz;
        this.fullyQualifiedGroupKey = fullyQualifiedGroupKey;
        this.provider = provider;
        setIconName(iconName);
        if (bundle == null) {
            this.documentation = new OperatorDocumentation(key);
        } else {
            this.documentation = (OperatorDocumentation) bundle.getObject("operator." + key);
            if (documentation.getName().equals("")) {
                documentation.setName(key);
                documentation.setDocumentation("Operator's description is missing in referenced OperatorDoc.");
            }
        }
    }

    /**
     * Creates a new operator description object. If the corresponding operator is not marked as deprecated the
     * deprecation info string should be null. If the icon string is null, the group icon will be used.
     * 
     * @deprecated No I18N support.
     */
    @Deprecated
    public OperatorDescription(ClassLoader classLoader, String key, String name, String className, String group, String iconName, String deprecationInfo, Plugin provider) throws ClassNotFoundException {
        this(classLoader, key, name, className, null, null, group, iconName, deprecationInfo, provider);
    }

    /**
     * Creates an operator description with the given fields.
     * 
     * @deprecated This constructor cannot provide an internationalization mechanism since description
     *             is not taken from operator documentation bundle.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public OperatorDescription(ClassLoader classLoader, String key, String name, String className, String shortDescription, String longDescription, String groupName, String iconName, String deprecationInfo, Plugin provider) throws ClassNotFoundException {
        this.key = key;

        this.clazz = (Class<? extends Operator>) Class.forName(className, true, classLoader);
        this.documentation = new OperatorDocumentation(name);
        this.documentation.setSynopsis(shortDescription);
        this.documentation.setDocumentation(longDescription);
        this.documentation.setDeprecation(deprecationInfo);

        this.fullyQualifiedGroupKey = groupName;

        this.provider = provider;

        setIconName(iconName);
    }

    /**
     * This constructor remains for compatibility reasons. Please use one of the non deprecated alternatives.
     */
    @Deprecated
    public OperatorDescription(String key, Class<? extends Operator> clazz, GroupTree group, ClassLoader classLoader, String iconName, Plugin provider) {
        this(group.getFullyQualifiedKey(), key, clazz, classLoader, iconName, provider);
    }

    /**
     * This constructor remains for compatibility reasons. Please use one of the non deprecated alternatives.
     */
    @Deprecated
    public OperatorDescription(String key, Class<? extends Operator> clazz, GroupTree groupTree, ClassLoader classLoader, String iconName, Plugin provider, OperatorDocBundle bundle) {
        this(groupTree.getFullyQualifiedKey(), key, clazz, classLoader, iconName, provider);
    }

    public String getName() {
        return getOperatorDocumentation().getName();
    }

    public String getShortName() {
        return getOperatorDocumentation().getShortName();
    }

    public Class<? extends Operator> getOperatorClass() {
        return clazz;
    }

    public String getShortDescription() {
        return getOperatorDocumentation().getSynopsis();
    }

    public String getLongDescriptionHTML() {
        OperatorDocumentation operatorDocumentation = getOperatorDocumentation();
        if (operatorDocumentation.getDocumentation() != null)
            return operatorDocumentation.getDocumentation();
        if (operatorDocumentation.getSynopsis() != null)
            return operatorDocumentation.getSynopsis();
        return "";
    }

    public OperatorDocumentation getOperatorDocumentation() {
        return documentation;
    }

    /**
     * This returns the qualified, dot separated key of the containing group.
     */
    public String getGroup() {
        return fullyQualifiedGroupKey;
    }

    /**
     * This returns the actual group name as displayed in RapidMiner.
     */
    public String getGroupName() {
        int pos = fullyQualifiedGroupKey.lastIndexOf(".");
        if (pos == -1)
            return fullyQualifiedGroupKey;
        else
            return fullyQualifiedGroupKey.substring(pos + 1);
    }

    public ImageIcon getIcon() {
        return getIcons()[1];
    }

    public ImageIcon getSmallIcon() {
        ImageIcon[] icons2 = this.getIcons();
        if (icons2[0] != null) {
            return icons2[0];
        } else {
            return icons2[1];
        }
    }

    public ImageIcon getLargeIcon() {
        ImageIcon[] icons2 = this.getIcons();
        if (icons2[2] != null) {
            return icons2[2];
        } else {
            return icons2[1];
        }
    }

    public String getAbbreviatedClassName() {
        return getOperatorClass().getName().replace("com.rapidminer.operator.", "c.r.o.");
    }

    public String getDeprecationInfo() {
        if (deprecationInfo != null) {
            return deprecationInfo;
        } else {
            return getOperatorDocumentation().getDeprecation();
        }
    }

    public boolean isDeprecated() {
        return deprecationInfo != null;
    }

    public String getProviderName() {
        return provider != null ? provider.getName() : OperatorService.RAPID_MINER_CORE_PREFIX;
    }

    /**
     * This defines the namespace of the provider. If is core, OperatorService.RAPID_MINER_CORE_NAMESPACE is returned.
     * Otherwise the namespace of the extension is returned as defined by the manifest.xml
     * 
     * @return
     */
    public String getProviderNamespace() {
        return provider != null ? provider.getExtensionId() : OperatorService.RAPID_MINER_CORE_NAMESPACE;
    }

    public String getKey() {
        if (provider != null) {
            return provider.getPrefix() + ":" + this.key;
        } else {
            return this.key;
        }
    }
    
    /**
     * Returns the key of this operator without any prefix.
     */
    public String getKeyWithoutPrefix() {
    	return this.key;
    }

    public void disable() {
        this.enabled = false;
    }

    /** Some operators may be disabled, e.g. because they cannot be applied inside an application server (file access etc.) */
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String toString() {
        return "key='" + key + "'; name='" + getName() + "'; " + (replacesDeprecatedKeys != null ? "replaces: " + replacesDeprecatedKeys : "") + "; implemented by " + clazz.getName() + "; group: " + fullyQualifiedGroupKey + "; icon: " + iconName;
    }

    @Override
    public int compareTo(OperatorDescription d) {
        String myName = this.getName();
        String otherName = d.getName();
        return myName.compareTo(otherName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OperatorDescription)) {
            return false;
        } else {
            OperatorDescription other = (OperatorDescription) o;
            return this.getKey().equals(other.getKey());
        }
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    /** Creates a new operator based on the description. Subclasses that want to
     * overwrite the creation behavior should override */
    public final Operator createOperatorInstance() throws OperatorCreationException {
        if (!isEnabled()) {
            throw new OperatorCreationException(OperatorCreationException.OPERATOR_DISABLED_ERROR, key + "(" + clazz.getName() + ")", null);
        }
        Operator operator = null;
        try {
            operator = createOperatorInstanceByDescription(this);
        } catch (InstantiationException e) {
            throw new OperatorCreationException(OperatorCreationException.INSTANTIATION_ERROR, key + "(" + clazz.getName() + ")", e);
        } catch (IllegalAccessException e) {
            throw new OperatorCreationException(OperatorCreationException.ILLEGAL_ACCESS_ERROR, key + "(" + clazz.getName() + ")", e);
        } catch (NoSuchMethodException e) {
            throw new OperatorCreationException(OperatorCreationException.NO_CONSTRUCTOR_ERROR, key + "(" + clazz.getName() + ")", e);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw new OperatorCreationException(OperatorCreationException.CONSTRUCTION_ERROR, key + "(" + clazz.getName() + ")", e);
        } catch (Throwable t) {
        	throw new OperatorCreationException(OperatorCreationException.INSTANTIATION_ERROR, "(" + clazz.getName() + ")", t);
        }
        OperatorService.invokeCreationHooks(operator);
        return operator;
    }

    /**
     * This method creates the actual instance of the {@link Operator} defined by the
     * given {@link OperatorDescription}.
     * Subclasses might overwrite this method in order to change the creation behavior or way.
     */
    protected Operator createOperatorInstanceByDescription(OperatorDescription description) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
        java.lang.reflect.Constructor<? extends Operator> constructor = clazz.getConstructor(new Class[] { OperatorDescription.class });
        return constructor.newInstance(new Object[] { description });
    }

    public void setIsReplacementFor(String opName) {
        if (replacesDeprecatedKeys == null) {
            replacesDeprecatedKeys = new LinkedList<String>();
        }
        replacesDeprecatedKeys.add(opName);
    }

    /** Returns keys of deprecated operators replaced by this operator. */
    public List<String> getReplacedKeys() {
        if (replacesDeprecatedKeys != null) {
            return replacesDeprecatedKeys;
        } else {
            return Collections.emptyList();
        }
    }

    public String getIconName() {
        if (iconName != null) {
            return iconName;
        }
        return null;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
        if (iconName != null) {
            icons = new ImageIcon[3];
            icons[0] = SwingTools.createIcon("16/" + iconName);
            icons[1] = SwingTools.createIcon("24/" + iconName);
            icons[2] = SwingTools.createIcon("48/" + iconName);
        } else {
            icons = EMPTY_ICONS;
        }
    }

    /**
     * This returns true if a specific icon already has been defined in
     * this {@link OperatorDescription}. It might be possible, that there's
     * no such icon name defined. Since each operator should have an icon,
     * it will be read from the containing Group when the {@link OperatorDescription} is
     * registered using {@link OperatorService#registerOperator(OperatorDescription)}.
     */
    public boolean isIconDefined() {
        return iconName != null;
    }

    private ImageIcon[] getIcons() {
        if (icons != null) {
            return icons;
            // } else {
            // return groupTree.getIcons();
        }
        return null;
    }

    public Plugin getProvider() {
        return provider;
    }

    protected void setFullyQualifiedGroupKey(String key) {
        this.fullyQualifiedGroupKey = key;
    }
}
