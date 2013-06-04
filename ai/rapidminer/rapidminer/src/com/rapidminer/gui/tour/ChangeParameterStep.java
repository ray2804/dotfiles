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

import java.awt.Window;

import com.rapidminer.ProcessSetupListener;
import com.rapidminer.gui.RapidMinerGUI;
import com.rapidminer.gui.properties.OperatorPropertyPanel;
import com.rapidminer.gui.tools.components.BubbleToDockable;
import com.rapidminer.gui.tools.components.BubbleWindow;
import com.rapidminer.gui.tools.components.BubbleWindow.Alignment;
import com.rapidminer.operator.ExecutionUnit;
import com.rapidminer.operator.Operator;
import com.rapidminer.parameter.UndefinedParameterError;

/**
 *This subclass of {@link Step} will open a {@link BubbleWindow} which closes if the user changes a chosen parameter of an {@link Operator}.
 *
 * @author Philipp Kersting and Thilo Kamradt
 *
 */

public class ChangeParameterStep extends Step {

	private String i18nKey;
	private String targetDockKey = OperatorPropertyPanel.PROPERTY_EDITOR_DOCK_KEY;
	private String parameter;
	private String targetValue;
	private Alignment alignment;
	private Window owner = RapidMinerGUI.getMainFrame();
	private Class<? extends Operator> operatorClass;
	private ProcessSetupListener listener = null;
	

	
	/**
	 * @param preferredAlignment offer for alignment but the Class will calculate by itself whether the position is usable.
	 * @param i18nKey of the message which will be shown in the {@link BubbleWindow}.
	 * @param operatorClass the class of Operator of which you want to change the parameter.
	 * @param parameter the key of the parameter which you want to change.
	 * @param targetDockKey the Component to which the {@link BubbleWindow} should point to.
	 * @param targetValue the Value the user should select for the given parameter.
	 */
	public ChangeParameterStep(Alignment preferredAlignment, String i18nKey, Class<? extends Operator>  operatorClass, String parameter, String targetDockKey, String targetValue) {
		this.alignment = preferredAlignment;
		this.i18nKey = i18nKey;
		this.targetDockKey = targetDockKey;
		this.parameter = parameter;
		this.targetValue = targetValue;
		this.operatorClass = operatorClass;
	}

	/**
	 * @param preferredAlignment offer for alignment but the Class will calculate by itself whether the position is usable.
	 * @param i18nKey of the message which will be shown in the {@link BubbleWindow}.
	 * @param operatorClass the class of Operator of which you want to change the parameter.
	 * @param parameter the key of the parameter which you want to change.
	 * @param targetDockKey the Component to which the {@link BubbleWindow} should point to.
	 * @param targetValue the Value the user should select for the given parameter.
	 */
	public ChangeParameterStep(Alignment preferredAlignment, String i18nKey, Class<? extends Operator>  operatorClass, String parameter, String targetDockKey, String targetValue, Window owner) {
		this.alignment = preferredAlignment;
		this.i18nKey = i18nKey;
		this.targetDockKey = targetDockKey;
		this.parameter = parameter;
		this.targetValue = targetValue;
		this.operatorClass = operatorClass;
		this.owner = owner;
	}
	
	@Override
	boolean createBubble() {
		bubble = new BubbleToDockable(owner, alignment, i18nKey, targetDockKey);
		listener = new ProcessSetupListener() {
			
			@Override
			public void operatorRemoved(Operator operator, int oldIndex, int oldIndexAmongEnabled) {
			}
			
			@Override
			public void operatorChanged(Operator operator) {
				if (ChangeParameterStep.this.operatorClass.isInstance(operator)){
					try {
						if (operator.getParameterAsString(parameter).equals(targetValue)){
							bubble.triggerFire();
							RapidMinerGUI.getMainFrame().getProcess().removeProcessSetupListener(this);
						}
					} catch (UndefinedParameterError e) {
						e.printStackTrace();
					}
				} 
			}
			
			@Override
			public void operatorAdded(Operator operator) {
			}
			
			@Override
			public void executionOrderChanged(ExecutionUnit unit) {
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
		return new Step[] {new PerspectivesStep(1), new NotOnScreenStep(targetDockKey)};
		
	}

}

