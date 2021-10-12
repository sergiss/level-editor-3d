/* 
 * Copyright (c) 2021, Sergio S.- sergi.ss4@gmail.com http://sergiosoriano.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.delmesoft.editor3d.ui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import com.delmesoft.editor3d.presenter.Presenter;

public class MapGeneratorPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField textFieldSeed;
	private JLabel lblPlotSize;
	private JSpinner spinnerPlotSize;
	private JLabel lblSeed;
	private JLabel lblPlotTile;
	private JSpinner spinnerPlotTile;
	private JLabel lblStreetTile;
	private JSpinner spinnerPathTile;
	private JButton btnGenerate;
	
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		textFieldSeed.setEnabled(b);
		lblPlotSize.setEnabled(b);
		spinnerPlotSize.setEnabled(b);
		lblSeed.setEnabled(b);
		lblPlotTile.setEnabled(b);
		spinnerPlotTile.setEnabled(b);
		lblStreetTile.setEnabled(b);
		spinnerPathTile.setEnabled(b);
		btnGenerate.setEnabled(b);
	}

	/**
	 * Create the panel.
	 */
	public MapGeneratorPanel(Presenter presenter) {
		
		setBorder(new TitledBorder(null, "Map Generator", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblPlotSize = new JLabel("Plot Size:");
		GridBagConstraints gbc_lblPlotSize = new GridBagConstraints();
		gbc_lblPlotSize.anchor = GridBagConstraints.WEST;
		gbc_lblPlotSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlotSize.gridx = 0;
		gbc_lblPlotSize.gridy = 0;
		panel.add(lblPlotSize, gbc_lblPlotSize);
		
		spinnerPlotSize = new JSpinner();
		spinnerPlotSize.setModel(new SpinnerNumberModel(new Integer(9), new Integer(1), new Integer(100), new Integer(1)));
		GridBagConstraints gbc_spinnerPlotSize = new GridBagConstraints();
		gbc_spinnerPlotSize.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlotSize.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPlotSize.gridx = 1;
		gbc_spinnerPlotSize.gridy = 0;
		panel.add(spinnerPlotSize, gbc_spinnerPlotSize);
		
		lblPlotTile = new JLabel("Plot Tile:");
		GridBagConstraints gbc_lblPlotTile = new GridBagConstraints();
		gbc_lblPlotTile.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlotTile.gridx = 2;
		gbc_lblPlotTile.gridy = 0;
		panel.add(lblPlotTile, gbc_lblPlotTile);
		
		spinnerPlotTile = new JSpinner();
		GridBagConstraints gbc_spinnerPlotTile = new GridBagConstraints();
		gbc_spinnerPlotTile.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlotTile.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPlotTile.gridx = 3;
		gbc_spinnerPlotTile.gridy = 0;
		panel.add(spinnerPlotTile, gbc_spinnerPlotTile);
		spinnerPlotTile.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		
		lblSeed = new JLabel("Seed:");
		GridBagConstraints gbc_lblSeed = new GridBagConstraints();
		gbc_lblSeed.anchor = GridBagConstraints.WEST;
		gbc_lblSeed.insets = new Insets(0, 0, 0, 5);
		gbc_lblSeed.gridx = 0;
		gbc_lblSeed.gridy = 1;
		panel.add(lblSeed, gbc_lblSeed);
		
		textFieldSeed = new JTextField();
		GridBagConstraints gbc_textFieldSeed = new GridBagConstraints();
		gbc_textFieldSeed.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldSeed.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSeed.gridx = 1;
		gbc_textFieldSeed.gridy = 1;
		panel.add(textFieldSeed, gbc_textFieldSeed);
		textFieldSeed.setColumns(10);
		
		lblStreetTile = new JLabel("Path Tile:");
		GridBagConstraints gbc_lblStreetTile = new GridBagConstraints();
		gbc_lblStreetTile.insets = new Insets(0, 0, 0, 5);
		gbc_lblStreetTile.gridx = 2;
		gbc_lblStreetTile.gridy = 1;
		panel.add(lblStreetTile, gbc_lblStreetTile);
		
		spinnerPathTile = new JSpinner();
		GridBagConstraints gbc_spinnerPathTile = new GridBagConstraints();
		gbc_spinnerPathTile.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPathTile.gridx = 3;
		gbc_spinnerPathTile.gridy = 1;
		panel.add(spinnerPathTile, gbc_spinnerPathTile);
		spinnerPathTile.setModel(new SpinnerNumberModel(new Integer(1), new Integer(0), null, new Integer(1)));
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnGenerate = new JButton("Generate");
		btnGenerate.setIcon(new ImageIcon(MapGeneratorPanel.class.getResource("/icons/gear.png")));
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean result = presenter.getView().showQuestion("Confirm", "Do you want to continue?");
				if(result) {
					int plotSize = (int) spinnerPlotSize.getValue();
					int plotTile = (int) spinnerPlotTile.getValue();
					int pathTile = (int) spinnerPathTile.getValue();
					String seed = textFieldSeed.getText();
					presenter.getEditor().generateRandomMap(plotSize, plotTile, pathTile, seed);
				}
			}
		});
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnGenerate.gridx = 0;
		gbc_btnGenerate.gridy = 0;
		panel_1.add(btnGenerate, gbc_btnGenerate);

	}

}
