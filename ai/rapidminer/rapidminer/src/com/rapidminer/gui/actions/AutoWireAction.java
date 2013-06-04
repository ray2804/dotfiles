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
package com.rapidminer.gui.actions;

import java.awt.event.ActionEvent;

import com.rapidminer.gui.MainFrame;
import com.rapidminer.gui.tools.EditBlockingProgressThread;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.operator.ExecutionUnit;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorChain;
import com.rapidminer.operator.ports.metadata.CompatibilityLevel;


/**
 * Wires the current process.
 * 
 * @author Simon Fischer, Tobias Malbrecht
 *
 */
public class AutoWireAction extends ResourceAction {

	private static final long serialVersionUID = -4597160351305617508L;

	private final CompatibilityLevel level;

	private final boolean keepConnections;

	private final boolean recursive;
	
	private final MainFrame mainFrame;
	
	public AutoWireAction(MainFrame mainFrame, String key, CompatibilityLevel level, boolean recursive, boolean keepConnections) {
		super(true, key);
		setCondition(OPERATOR_SELECTED, MANDATORY);
		this.mainFrame = mainFrame;
		this.recursive = recursive;
		this.level = level;
		this.keepConnections = keepConnections;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new EditBlockingProgressThread("auto_wiring") {
			@Override
			public void execute() {				
				Operator op = mainFrame.getFirstSelectedOperator();
				OperatorChain chain;
				if (op instanceof OperatorChain) {
					chain = (OperatorChain) op;
				} else {
					chain = op.getParent();
				}
				if (chain != null) {
					getProgressListener().setTotal(chain.getSubprocesses().size() + 1);
					int i = 1;
					for (ExecutionUnit unit : chain.getSubprocesses()) {
						getProgressListener().setCompleted(i++);
						unit.autoWire(level, keepConnections, recursive);		
					}
					getProgressListener().complete();
				}					
			}
		}.start();
	}
}
