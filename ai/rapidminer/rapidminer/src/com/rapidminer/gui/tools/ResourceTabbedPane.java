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
package com.rapidminer.gui.tools;

import java.awt.Component;
import java.text.MessageFormat;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.rapidminer.tools.I18N;
import com.rapidminer.tools.LogService;
/**
 * 
 * @author Simon Fischer
 */
public class ResourceTabbedPane extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private final String i18KeyPrefix;
	private boolean largeIcons = false;
	
	public ResourceTabbedPane(String i18KeyPrefix) {
		super();
		this.i18KeyPrefix = i18KeyPrefix;
	}

	public ResourceTabbedPane(String i18KeyPrefix, int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
		this.i18KeyPrefix = i18KeyPrefix;
	}

	public ResourceTabbedPane(String i18KeyPrefix, int tabPlacement) {
		super(tabPlacement);
		this.i18KeyPrefix = i18KeyPrefix;
	}


	public void addTabI18N(String key, Component component, String ... i18nArgs) {
		String name;
		if ((i18nArgs != null) && (i18nArgs.length > 0)) {
			name = formatMessage(key, "label", i18nArgs);
		} else {
			name = getMessage(key, "label");			
		}
		Icon icon = null;
		String iconName = getMessageOrNull(key, "icon");
		if (iconName != null) {
			icon = SwingTools.createIcon((isLargeIcons() ? "24/" : "16/") + iconName);
		}
		addTab(name, icon, component);
		int index = getTabCount()-1;
		
		String tip;
		if ((i18nArgs != null) && (i18nArgs.length > 0)) {
			tip = formatMessage(key, "tip", i18nArgs);
		} else {
			tip = getMessageOrNull(key, "tip");			
		}
		if (tip != null) {
			setToolTipTextAt(index, tip);
		}
		String mne = getMessageOrNull(key, "mne");
		if (mne != null) {
			mne = mne.toUpperCase(); 
			if (name.indexOf(mne.charAt(0)) == -1) {
				if (name.indexOf(mne.toUpperCase().charAt(0)) != -1) {
					mne = mne.toUpperCase();
					LogService.getRoot().warning("Mnemonic key "+mne+" not found for tab " + i18KeyPrefix+"."+key + " ("+name+"), converting to upper case.");
					//LogService.getRoot().log(Level.WARNING, "com.rapidminer.tools.I18N.resource_tabbed_pane", mne, i18KeyPrefix, key, name);
				} else {
					LogService.getRoot().warning("Mnemonic key "+mne+" not found for action " + i18KeyPrefix+"."+key + " ("+name+")");
				}
			}
			setMnemonicAt(index, mne.charAt(0));
		}
	}
	
	private String getMessage(String tabName, String key) {
		return I18N.getMessage(I18N.getGUIBundle(), "gui.tabs."+this.i18KeyPrefix+"."+tabName+"."+key);
	}

	private String formatMessage(String tabName, String key, Object[] args) {
		String message = getMessageOrNull(tabName, key);
		if (message != null) {
			return MessageFormat.format(message, args);
		} else {
			return null;
		}
	}

	private String getMessageOrNull(String tabName, String key) {
		return I18N.getMessageOrNull(I18N.getGUIBundle(), "gui.tabs."+this.i18KeyPrefix+"."+tabName+"."+key);
	}

	public void setLargeIcons(boolean largeIcons) {
		this.largeIcons = largeIcons;
	}

	public boolean isLargeIcons() {
		return largeIcons;
	}

}
