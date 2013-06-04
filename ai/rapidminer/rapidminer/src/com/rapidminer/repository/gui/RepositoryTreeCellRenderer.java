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

package com.rapidminer.repository.gui;

import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.rapidminer.gui.renderer.RendererService;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.repository.BlobEntry;
import com.rapidminer.repository.DataEntry;
import com.rapidminer.repository.Entry;
import com.rapidminer.repository.Folder;
import com.rapidminer.repository.IOObjectEntry;
import com.rapidminer.repository.ProcessEntry;
import com.rapidminer.repository.Repository;
import com.rapidminer.tools.Tools;

/**
 * @author Simon Fischer
 */
public class RepositoryTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;

	private static final Icon ICON_FOLDER_OPEN = SwingTools.createIcon("16/folder.png");
	private static final Icon ICON_FOLDER_CLOSED = SwingTools.createIcon("16/folder_closed.png");
	private static final Icon ICON_FOLDER_LOCKED = SwingTools.createIcon("16/folder_lock.png");
	private static final Icon ICON_PROCESS = SwingTools.createIcon("16/gear.png");
	private static final Icon ICON_DATA = SwingTools.createIcon("16/data.png");
	private static final Icon ICON_BLOB = SwingTools.createIcon("16/document_plain.png");
	private static final Icon ICON_TEXT = SwingTools.createIcon("16/text.png");
	private static final Icon ICON_TABLE = SwingTools.createIcon("16/table2.png");
	private static final Icon ICON_IMAGE = SwingTools.createIcon("16/photo_landscape2.png");

	/** stores the icons for all repository implementations */
	private static Map<String, Icon> ICON_REPOSITORY_MAP = new HashMap<String, Icon>();

	private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

	private static final Border ENTRY_BORDER = BorderFactory.createEmptyBorder(1, 0, 1, 0);

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value instanceof Entry) {
			Entry entry = (Entry) value;

			StringBuilder labelText = new StringBuilder();
			labelText.append("<html>").append(entry.getName());

			StringBuilder state = new StringBuilder();
			if (entry instanceof Repository) {
				String reposState = ((Repository) entry).getState();
				if (reposState != null) {
					state.append(reposState).append(" &ndash; ");
				}
			}
			if (entry.getOwner() != null) {
				state.append(entry.getOwner());
			}
			if (entry instanceof DataEntry) {
				state.append("  &ndash; v").append(((DataEntry) entry).getRevision());
				long date = ((DataEntry) entry).getDate();
				if (date >= 0) {
					state.append(", ").append(DATE_FORMAT.format(new Date(date)));
				}
				long size = ((DataEntry) entry).getSize();
				if (size > 0) {
					state.append(" &ndash; ").append(Tools.formatBytes(size));
				}
			}
			if (state.length() > 0) {
				labelText.append(" <small style=\"color:gray\">(").append(state).append(")</small>");
			}

			labelText.append("</html>");
			label.setText(labelText.toString());
			if (entry instanceof Repository) {
				Repository repo = (Repository) entry;
				if (ICON_REPOSITORY_MAP.get(repo.getIconName()) == null) {
					ICON_REPOSITORY_MAP.put(repo.getIconName(), SwingTools.createIcon("16/" + repo.getIconName()));
				}
				label.setIcon(ICON_REPOSITORY_MAP.get(repo.getIconName()));
			} else if (entry.getType().equals(Folder.TYPE_NAME)) {
				if (entry.isReadOnly()) {
					label.setIcon(ICON_FOLDER_LOCKED);
				} else if (expanded) {
					label.setIcon(ICON_FOLDER_OPEN);
				} else {
					label.setIcon(ICON_FOLDER_CLOSED);
				}
			} else if (entry.getType().equals(IOObjectEntry.TYPE_NAME)) {
				if (entry instanceof IOObjectEntry) {
					IOObjectEntry dataEntry = (IOObjectEntry) entry;
					label.setIcon(RendererService.getIcon(dataEntry.getObjectClass()));
				} else {
					label.setIcon(ICON_DATA);
				}
			} else if (entry.getType().equals(ProcessEntry.TYPE_NAME)) {
				label.setIcon(ICON_PROCESS);
			} else if (entry.getType().equals(BlobEntry.TYPE_NAME)) {
				String mimeType = ((BlobEntry) entry).getMimeType();
				if (mimeType != null) {
					if (mimeType.startsWith("text/") || "application/pdf".equals(mimeType) || "application/rtf".equals(mimeType)) {
						label.setIcon(ICON_TEXT);
					} else if (mimeType.equals("application/msexcel")) {
						label.setIcon(ICON_TABLE);
					} else if (mimeType.startsWith("image/")) {
						label.setIcon(ICON_IMAGE);
					} else {
						label.setIcon(ICON_BLOB);
					}
				} else {
					label.setIcon(ICON_BLOB);
				}
			} else {
				label.setIcon(null);
			}
		}

		label.setBorder(ENTRY_BORDER);
		return label;
	}
}
