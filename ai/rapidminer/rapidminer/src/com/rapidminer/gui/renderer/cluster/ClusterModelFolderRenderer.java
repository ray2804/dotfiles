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
package com.rapidminer.gui.renderer.cluster;

import java.awt.Component;

import javax.swing.JLabel;

import com.rapidminer.gui.renderer.AbstractRenderer;
import com.rapidminer.gui.tools.ExtendedJScrollPane;
import com.rapidminer.gui.viewer.ClusterTreeVisualization;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.clustering.CentroidClusterModel;
import com.rapidminer.operator.clustering.ClusterModel;
import com.rapidminer.operator.clustering.HierarchicalClusterModel;
import com.rapidminer.report.Reportable;

/**
 * A renderer for the folder view of cluster models.
 * 
 * @author Ingo Mierswa
 */
public class ClusterModelFolderRenderer extends AbstractRenderer {

	public String getName() {
		return "Folder View";
	}

	public Component getVisualizationComponent(Object renderable, IOContainer ioContainer) {
		if (renderable instanceof HierarchicalClusterModel) {
			return new ExtendedJScrollPane(new ClusterTreeVisualization((HierarchicalClusterModel) renderable));
		} else if (renderable instanceof ClusterModel) {
			return new ExtendedJScrollPane(new ClusterTreeVisualization((ClusterModel) renderable));
		} else {
			return new JLabel("no folder visualization supported for this kind of cluster model " + renderable.getClass());
		}
	}

	public Reportable createReportable(Object renderable, IOContainer ioContainer, int width, int height) {
		ClusterTreeVisualization plotter = null;
		if (renderable instanceof HierarchicalClusterModel) {
			plotter = new ClusterTreeVisualization((HierarchicalClusterModel) renderable);
		} else if (renderable instanceof CentroidClusterModel) {
			plotter = new ClusterTreeVisualization((CentroidClusterModel) renderable);
		}
		if (plotter != null) {
			plotter.setSize(width, height);
		}
		return plotter;
	}
}
