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
package com.rapidminer.gui.plotter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;

import com.rapidminer.datatable.DataTable;
import com.rapidminer.gui.plotter.PlotterConfigurationModel.PlotterChangedListener;
import com.rapidminer.gui.plotter.PlotterConfigurationModel.PlotterSettingsChangedListener;
import com.rapidminer.gui.plotter.charts.RapidBarPainter;
import com.rapidminer.gui.plotter.charts.RapidXYBarPainter;
import com.rapidminer.gui.tools.ExtendedJScrollPane;

/**
 * This is the main component for all data or statistics plotters containing a {@link PlotterControlPanel} and a
 * {@link Plotter}.
 * 
 * @author Ingo Mierswa, Simon Fischer
 */
public class PlotterPanel extends JPanel {

	private static final long serialVersionUID = -8724351470349745191L;

	public static final int DEFAULT_MAX_NUMBER_OF_DATA_POINTS = 5000;

	static {
		BarRenderer.setDefaultBarPainter(new RapidBarPainter());
		XYBarRenderer.setDefaultBarPainter(new RapidXYBarPainter());
	}

	/** The line style rendered for the legend (or key). */
	public static class LineStyleCellRenderer extends JLabel implements ListCellRenderer {
		private static final long serialVersionUID = -7039142638209143602L;

		Plotter plotter;

		public LineStyleCellRenderer(Plotter plotter) {
			this.plotter = plotter;
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String s = value.toString();
			setText(s);
			Icon icon = plotter.getIcon(index);
			if (icon != null)
				setIcon(icon);
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}

	/** The main panel containing the axes selection panel and the actual plotter component. */
	private final JPanel mainPanel = new JPanel(new BorderLayout());

	private final PlotterControlPanel controlPanel;

	private Component oldPlotterComponent = null;

	private PlotterConfigurationModel settings;
	/**
	 * Creates a new plotter panel based on the given {@link DataTable} object.
	 */
	public PlotterPanel(DataTable dataTable) {
		this(dataTable, PlotterConfigurationModel.DATA_SET_PLOTTER_SELECTION);
	}

	/**
	 * Creates a new plotter panel based on the given {@link DataTable} object.
	 */
	public PlotterPanel(DataTable dataTable, LinkedHashMap<String, Class<? extends Plotter>> availablePlotters) {
		this(new PlotterConfigurationModel(availablePlotters, dataTable));
	}

	public PlotterPanel(final PlotterConfigurationModel settings) {
		super(new BorderLayout());
		this.controlPanel = new PlotterControlPanel(settings);
		this.settings = settings;
		settings.registerPlotterListener(new PlotterChangedListener() {
			@Override
			public void plotterChanged(String plotterName) {
				if (oldPlotterComponent != null) {
					mainPanel.remove(oldPlotterComponent);
				}
				JComponent plotterComponent = settings.getPlotter().getPlotter();
				if (plotterComponent != null) {
					mainPanel.add(plotterComponent, BorderLayout.CENTER);
					oldPlotterComponent = plotterComponent;
				}
				repaint();
				revalidate();
			}

			@Override
			public List<PlotterSettingsChangedListener> getListeningObjects() {
				return new ArrayList<PlotterSettingsChangedListener>(0);
			}
		});
		JScrollPane plotterScrollPane = new ExtendedJScrollPane(mainPanel);
		settings.registerPlotterListener(controlPanel);
		mainPanel.add(controlPanel, BorderLayout.WEST);
		plotterScrollPane.setBorder(null);

		add(plotterScrollPane, BorderLayout.CENTER);
		
		JComponent plotterComponent = settings.getPlotter().getPlotter();
		if (plotterComponent != null) {
			mainPanel.add(plotterComponent, BorderLayout.CENTER);
			oldPlotterComponent = plotterComponent;
		}
		repaint();
		revalidate();
	}
	
	@Override
	public void print(Graphics pg) {
		if (oldPlotterComponent != null) {
			oldPlotterComponent.print(pg);
		} else {
			super.print(pg);
		}
	}
	
	/**
	 * This method returns the plotter settings, controlling which plotter is 
	 * displayed and how.
	 */
	public PlotterConfigurationModel getPlotterSettings() {
		return settings;
	}

	/**
	 * Returns the plotter component.
	 * @return
	 */
	public Component getPlotterComponent() {
		return oldPlotterComponent;
	}
}
