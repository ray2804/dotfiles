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
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextAreaEditorKit;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.rapidminer.Process;
import com.rapidminer.gui.MainFrame;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.tools.ExtendedJToolBar;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.ResourceActionTransmitter;
import com.rapidminer.gui.tools.ResourceDockKey;
import com.rapidminer.operator.Operator;
import com.rapidminer.tools.XMLException;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;


/**
 * A text area for editing the process as XML. This editor is the second
 * possible way to edit or create RapidMiner processes. All changes are reflected by
 * the process. However, it should be more convenient to use the tree view
 * for process design. <br/>
 * 
 * This XML editor support very simple syntax highlighting based on keyword
 * parsing.
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class XMLEditor extends JPanel implements ProcessEditor, Dockable, FocusListener {

	private static final long serialVersionUID = 4172143138689034659L;
		
	private final RSyntaxTextArea editor;
	
	private final MainFrame mainFrame;
	
	public XMLEditor(MainFrame mainFrame) {
		super(new BorderLayout());
		this.mainFrame = mainFrame;
		
		// create text area
		this.editor = new RSyntaxTextArea(new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_XML));
		this.editor.setAnimateBracketMatching(true);
		this.editor.setAutoIndentEnabled(true);
		this.editor.setBorder(null);
		this.editor.addFocusListener(this);
		
		JToolBar toolBar = new ExtendedJToolBar();
		toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		toolBar.add(new ResourceAction(true, "xml_editor.apply_changes") {
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					validateProcess();					
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (XMLException e1) {
					e1.printStackTrace();
				}
			}			
		});
		toolBar.addSeparator();
		toolBar.add(new ResourceActionTransmitter(true, "editor.copy", new RTextAreaEditorKit.CopyAction(), this));
		toolBar.add(new ResourceActionTransmitter(true, "editor.cut", new RTextAreaEditorKit.CutAction(), this));
		toolBar.add(new ResourceActionTransmitter(true, "editor.paste", new RTextAreaEditorKit.PasteAction(), this));

		toolBar.addSeparator();
		//TODO Readd 
		//toolBar.add(new RTextAreaEditorKit.editor.SEARCH_AND_REPLACE_ACTION);
		
		add(toolBar, BorderLayout.NORTH);
		add(new RTextScrollPane(editor), BorderLayout.CENTER);
	}
	
	public void setText(String text) {
		this.editor.setText(text);		
	}
	
	public void processUpdated(Process process) {
		setText(process.getRootOperator().getXML(true));
	}
	
	public void processChanged(Process process) {
		processUpdated(process);
	}

	/** Just jumps to the position of the currently selected operator. */
    public void setSelection(List<Operator> selection) {
		if (!selection.isEmpty()) {
			Operator currentOperator = selection.get(0);
			this.editor.requestFocus();
			this.editor.setCaretPosition(0);
			String name = currentOperator.getName();
			String text = this.editor.getText();
			int result = text.indexOf("\"" + name + "\"");
			if (result >= 0) {
				this.editor.select(result + 1, result + name.length() + 1);
			}
		}
	}
	
	public synchronized void validateProcess() throws IOException, XMLException {
		Process newExp = new Process(editor.getText());
		if (!newExp.getRootOperator().getXML(true).equals(RapidMinerGUI.getMainFrame().getProcess().getRootOperator().getXML(true))) {
			Process old = RapidMinerGUI.getMainFrame().getProcess();
			newExp.setProcessLocation(old.getProcessLocation());
			mainFrame.setProcess(newExp, false, true);
		}
	}

	public String getXMLFromEditor(){
		return this.editor.getText();
	}
	
	public void focusGained(FocusEvent e) {}

	public void focusLost(FocusEvent e) {
		try {			
			validateProcess();
		} catch (Exception e1) { }
	}

	public static final String XML_EDITOR_DOCK_KEY = "xml_editor";
	private final DockKey DOCK_KEY = new ResourceDockKey(XML_EDITOR_DOCK_KEY);
	{
		DOCK_KEY.setDockGroup(MainFrame.DOCK_GROUP_ROOT);
	}
	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public DockKey getDockKey() {
		return DOCK_KEY;
	}
}
