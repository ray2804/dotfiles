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
package com.rapidminer.gui.processeditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.rapidminer.Process;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.tools.ExtendedJScrollPane;
import com.rapidminer.gui.tools.ExtendedJTable;
import com.rapidminer.gui.tools.ResourceDockKey;
import com.rapidminer.gui.tools.UpdateQueue;
import com.rapidminer.operator.Operator;
import com.rapidminer.tools.container.Pair;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

/**
 * 
 * A Dockable, that can display all defined macros and their values and allows the user to modify the while the process is running.
 * 
 * @author Philipp Kersting
 *
 */

public class MacroViewer extends JPanel implements Dockable, ProcessEditor {

	private static final long serialVersionUID = 1L;

	private static final String MACRO_VIEWER_DOCK_KEY = "macro_viewer";
	private static final DockKey DOCK_KEY = new ResourceDockKey(MACRO_VIEWER_DOCK_KEY);

	private JTable macroTable;
	private ExtendedJScrollPane scrollPane = new ExtendedJScrollPane();

	private List<Pair<String, String>> data = new ArrayList<Pair<String, String>>();
	private String[] names = { "Macro", "Value" };
	private Process currentProcess;

	private UpdateQueue updateQueue;
	private Observer macroObserver;

	private AbstractTableModel dataModel = new AbstractTableModel() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public int getRowCount() {
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col) {

			if (col == 0) {
				return data.get(row).getFirst();
			} else {
				return data.get(row).getSecond();
			}

		}

		public String getColumnName(int column) {
			return names[column];
		};

		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return false;
			} else {
				return true;
			}

		};

		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			String content = (String) aValue;
			data.get(rowIndex).setSecond(content);
			RapidMinerGUI.getMainFrame().getProcess().getMacroHandler().addMacro(data.get(rowIndex).getFirst(), content);
		};

	};

	public MacroViewer() {
		setLayout(new BorderLayout());

		macroTable = new ExtendedJTable(dataModel, true, false, true);
		//macroTable.setModel(dataModel);
		macroTable.setFillsViewportHeight(true);

		scrollPane = new ExtendedJScrollPane(macroTable);
		add(scrollPane, BorderLayout.CENTER);

		updateQueue = new UpdateQueue(MACRO_VIEWER_DOCK_KEY);
		updateQueue.start();
		macroObserver = new Observer() {

			@Override
			public void update(Observable o, Object arg) {
				updateQueue.execute(new Runnable() {

					@Override
					public void run() {
						updateMacros();
						try {
							Thread.sleep(1000);  // Sleep 1sec to avoid update flooding
						} catch (InterruptedException e) {}
					}
				});
			}
		};
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public DockKey getDockKey() {
		return DOCK_KEY;
	}

	@Override
	public void processChanged(Process process) {
		if (currentProcess != null) {
			currentProcess.getMacroHandler().deleteObserver(macroObserver);
		}
		process.getMacroHandler().addObserver(macroObserver);
		currentProcess = process;
	}

	@Override
	public void setSelection(List<Operator> selection) {}

	@Override
	public void processUpdated(Process process) {}

	private void updateMacros() {
		final List<Pair<String, String>> newData = new ArrayList<Pair<String, String>>();
		Iterator<String> macroNames = RapidMinerGUI.getMainFrame().getProcess().getMacroHandler().getDefinedMacroNames();
		while (macroNames.hasNext()) {
			String name = macroNames.next();
			String value = RapidMinerGUI.getMainFrame().getProcess().getMacroHandler().getMacro(name);
			newData.add(new Pair<String, String>(name, value));
		}
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				data = newData;
				dataModel.fireTableDataChanged();
			}
		});
	}

}
