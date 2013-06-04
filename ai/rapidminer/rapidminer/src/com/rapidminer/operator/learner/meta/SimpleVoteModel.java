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
package com.rapidminer.operator.learner.meta;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.learner.SimplePredictionModel;
import com.rapidminer.tools.RandomGenerator;
import com.rapidminer.tools.Tools;

/**
 * A simple vote model. For classification problems, the majority class is chosen.
 * For regression problems, the average prediction value is used. This model
 * only supports simple prediction models.
 *
 * @author Ingo Mierswa
 */
public class SimpleVoteModel extends SimplePredictionModel implements MetaModel {

	private static final long serialVersionUID = 1089932073805038503L;

	private List<? extends SimplePredictionModel> baseModels;

	public SimpleVoteModel(ExampleSet exampleSet, List<? extends SimplePredictionModel> baseModels) {
		super(exampleSet);
		this.baseModels = baseModels;
	}

	@Override
	public double predict(Example example) throws OperatorException {
		if (getLabel().isNominal()) {
			Map<Double, AtomicInteger> classVotes = new TreeMap<Double, AtomicInteger>();
			Iterator<? extends SimplePredictionModel> iterator = baseModels.iterator();
			while (iterator.hasNext()) {
				double prediction = iterator.next().predict(example);
				AtomicInteger counter = classVotes.get(prediction);
				if (counter == null) {
					classVotes.put(prediction, new AtomicInteger(1));					
				} else {
					counter.incrementAndGet();
				}
			}

			Iterator<Double> votedClasses = classVotes.keySet().iterator();
			List<Double> bestClasses = new LinkedList<Double>();
			int bestClassesVotes = -1;
			while (votedClasses.hasNext()) {
				double currentClass = votedClasses.next();
				int currentVotes = classVotes.get(currentClass).intValue();
				if (currentVotes > bestClassesVotes) {
					bestClasses.clear();
					bestClasses.add(currentClass);
					bestClassesVotes = currentVotes;
				}
				if (currentVotes == bestClassesVotes) {
					bestClasses.add(currentClass);
				}
				example.setConfidence(getLabel().getMapping().mapIndex((int)currentClass), ((double) currentVotes) / (double)baseModels.size());
			}
			if (bestClasses.size() == 1) {
				return bestClasses.get(0);              
			} else {
				return bestClasses.get(RandomGenerator.getGlobalRandomGenerator().nextInt(bestClasses.size()));
			}
		} else {
			double sum = 0.0d;
			Iterator<? extends SimplePredictionModel> iterator = baseModels.iterator();
			while (iterator.hasNext()) {
				sum += iterator.next().predict(example);
			}
			return sum / baseModels.size();
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (SimplePredictionModel model : baseModels) {
			buffer.append("Model " + i + ":" + Tools.getLineSeparator());
			buffer.append("---" + Tools.getLineSeparator());
			buffer.append(model.toString());
			buffer.append(Tools.getLineSeparators(2));
			i++;
		}
		return buffer.toString();
	}

	@Override
	public List<String> getModelNames() {
		List<String> names = new LinkedList<String>();
		for (int i = 0; i < this.baseModels.size(); i++) {
			names.add("Model " + (i + 1));
		}
		return names;
	}

	@Override
	public List<? extends Model> getModels() {
		return baseModels;
	}
}
