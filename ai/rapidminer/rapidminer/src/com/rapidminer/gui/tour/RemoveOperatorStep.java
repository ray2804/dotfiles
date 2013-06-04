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
package com.rapidminer.gui.tour;

import java.awt.Component;
import java.awt.Window;

import com.rapidminer.ProcessSetupListener;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.properties.OperatorPropertyPanel;
import com.rapidminer.gui.tools.components.BubbleToButton;
import com.rapidminer.gui.tools.components.BubbleToDockable;
import com.rapidminer.gui.tools.components.BubbleToOperator;
import com.rapidminer.gui.tools.components.BubbleWindow;
import com.rapidminer.gui.tools.components.BubbleWindow.Alignment;
import com.rapidminer.operator.ExecutionUnit;
import com.rapidminer.operator.Operator;

/**
 * This Subclass of {@link Step} will open a {@link BubbleWindow} which closes if an {@link Operator} of the given Class was removed.
 *
 * @author Philipp Kersting
 *
 */
public class RemoveOperatorStep extends Step {

	private String i18nKey;
	private Window owner = RapidMinerGUI.getMainFrame();
	private Class<? extends Operator> operatorClass;
	private Alignment alignment;
	private BubbleTo element;
	private String buttonKey = "delete";
	private String dockableKey = OperatorPropertyPanel.PROPERTY_EDITOR_DOCK_KEY;
	private ProcessSetupListener listener = null;

	
	/**
	 * @param preferredAlignment offer for alignment but the Class will calculate by itself whether the position is usable.
	 * @param owner the {@link Window} on which the {@link BubbleWindow} should be shown.
	 * @param i18nKey of the message which will be shown in the {@link BubbleWindow}.
	 * @param operatorClass Class or SuperClass of the {@link Operator} which should be deleted.
	 * @param componentKey key of the {@link Component} to which the {@link BubbleWindow} should point to. 
	 */
	public RemoveOperatorStep(BubbleTo element, Alignment preferredAlignment, String i18nKey, Class<? extends Operator> operatorClass) {
		this.i18nKey = i18nKey;
		this.alignment = preferredAlignment;
		this.operatorClass = operatorClass;
		this.element = element;
	}
	
	@Override
	boolean createBubble() {
		switch(element) {
			case BUTTON:
				bubble = new BubbleToButton(owner, dockableKey, alignment, i18nKey, buttonKey, false, new Object[] {});
				break;
			case DOCKABLE:
				bubble = new BubbleToDockable(owner, alignment, i18nKey, dockableKey, new Object[] {});
				break;
			case OPERATOR:
				bubble = new BubbleToOperator(owner, alignment, i18nKey, operatorClass, new Object[] {});
				break;
		}
		listener = new ProcessSetupListener() {

			@Override
			public void operatorRemoved(Operator operator, int oldIndex, int oldIndexAmongEnabled) {
				if (RemoveOperatorStep.this.operatorClass.isInstance(operator)) {
					bubble.triggerFire();
					RapidMinerGUI.getMainFrame().getProcess().removeProcessSetupListener(this);
				}

			}

			@Override
			public void operatorChanged(Operator operator) {
				// do not care

			}

			@Override
			public void operatorAdded(Operator operator) {
				// do not care

			}

			@Override
			public void executionOrderChanged(ExecutionUnit unit) {
				// do not care

			}
		};
		RapidMinerGUI.getMainFrame().getProcess().addProcessSetupListener(listener);
		return true;
	}

	@Override
	protected void stepCanceled() {
		if(listener != null)
		RapidMinerGUI.getMainFrame().getProcess().removeProcessSetupListener(listener);
	}

	@Override
	public Step[] getPreconditions() {
		return new Step[] {new PerspectivesStep(1)};
	}

}
