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

import java.util.LinkedList;
import java.util.List;

/** A tour consisting of multiple {@link Step}s explaining the usage of RapidMiner
 *  or an Extension.
 *  
 *   Implementations of tour must implement a default (no-arg) constructor since
 *   they are created reflectively.
 * 
 * 
 * @author Thilo Kamradt
 *
 */
public abstract class IntroductoryTour {
	
	public static interface TourListener {

		/**
		 * will be called by a {@link Step} if the Tour was closed or is finished
		 */
		public void tourClosed();

	}

	private int maxSteps;
	
	/**
	 * This Array has to be filled with Subclasses of {@link Step} which will guide the Tour.
	 */
	protected Step[] step;

	private String tourKey;

	private boolean completeWindow;

	private List<TourListener> listeners;
	
	private Step firstStep;

	/**
	 * This Constructor will initialize the {@link Step} step[] which has to be filled in the buildTour() Method and adds automatically a {@link FinalStep} to the end of your tour.
	 * 
	 * @param max number of steps you want to perform (size of the Array you want to fill)
	 * @param tourName name of the your tour (will be used as key as well)
	 */
	public IntroductoryTour(int steps, String tourName) {
		this(steps, tourName, true);
	}

	/**
	 * This Constructor will initialize the {@link Step} step[] which has to be filled in the buildTour() Method
	 * 
	 * @param steps number of Steps you will do (size of the Array you want to fill)
	 * @param tourName name of the your tour (will be used as key as well)
	 * @param addComppleteWindow indicates whether a {@link FinalStep} with will be added or not.
	 */
	public IntroductoryTour(int steps, String tourName, boolean addComppleteWindow) {
		this.tourKey = tourName;
		this.completeWindow = addComppleteWindow;
//		if (addComppleteWindow) {
//			this.maxSteps = steps + 1;
//		} else {
			this.maxSteps = steps;
//		}
		this.listeners = new LinkedList<TourListener>();
	}

	/**
	 * method to initializes the needed Array of {@link Step} and the FinalStep if wanted
	 */
	private void init() {
//		if (completeWindow) {
//			step = new Step[maxSteps];
//			step[maxSteps - 1] = new FinalStep(tourKey);
//		} else {
			step = new Step[maxSteps];
//		}
	}

	/**
	 * starts the Tour
	 */
	public void startTour() {
		init();
		buildTour();
		placeFollowers();
//		step[0].start();
		firstStep.start();
	}

	/**
	 * This method fills the step[] instances of subclasses of {@link Step} which will guide through the tour
	 */
	protected abstract void buildTour();

	/**
	 * method to get the key and name of the Tour.
	 * @return String with key of the Tour
	 */
	public String getKey() {
		return tourKey;
	}

	/**
	 * This method connects the single steps to a queue and the the needed parameters to the steps.
	 * After calling this method the isFinal-, tourKey-, index- and listeners-parameter of Step is set.
	 */
	private void placeFollowers() {
		if(step[0].getPreconditions().length == 0) {
			firstStep = step[0];
		} else {
			firstStep = step[0].getPreconditions()[0];
		}
		Step[] conditionsCurrent;
		Step[] conditionsNext;
		//iterate over Array and create a queue
		for (int i = 0; i < step.length; i++) {
			//insert Precondtions to queue
			conditionsCurrent = step[i].getPreconditions();
			if(conditionsCurrent.length != 0) {
				if(i > 0)
					step[i-1].setNext(conditionsCurrent[0]);
				for (int j = 0; j < conditionsCurrent.length; j++) {
					conditionsCurrent[j].makeSettings(tourKey, i + 1, this.getSize(), false, listeners);
					if(j == (conditionsCurrent.length -1)) {
						conditionsCurrent[j].setNext(step[i]);
					} else {
						conditionsCurrent[j].setNext(conditionsCurrent[j + 1]);
					}
				}
			}
			// add the current Step to the queue and set the next step
			if(i <= (step.length - 2)) {
				step[i].makeSettings(tourKey, i + 1, this.getSize(), false, listeners);
				conditionsNext = step[i +1].getPreconditions();
				step[i].setNext(conditionsNext.length == 0 ? step[i +1] : conditionsNext[0]);
			} else {
				if(completeWindow){
					step[i].makeSettings(tourKey, i + 1, this.getSize(), false, listeners);
					step[i].setNext(new FinalStep(tourKey));
					step[i].getNext().makeSettings(tourKey, maxSteps, this.getSize(), true, listeners);
				} else {
					step[i].makeSettings(tourKey, maxSteps, this.getSize(), true, listeners);
				}
			}
		}
	}

	/**
	 * Adds a {@link TourListener} to the IntroductoryTour and to all {@link Step}s of the Tour.
	 * @param listener TourListener
	 */
	public void addListener(TourListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * 
	 * @return returns the size of the Tour including the {@link FinalStep} if the flag was set.
	 */
	public int getSize() {
		return this.maxSteps;
	}
}
