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

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.delmesoft.editor3d.presenter.Presenter;

public class SettingsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Presenter presenter;

	private JCheckBox chckbxShowGrid;
	private JCheckBox chckbxShowBlocks;
	private JCheckBox chckbxShowEntities;
	private JCheckBox chckbxShowStatistics;
	
	public SettingsPanel(Presenter presenter) {
		
		setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		this.presenter = presenter;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		chckbxShowGrid = new JCheckBox("Show Grid");
		GridBagConstraints gbc_chckbxShowGrid = new GridBagConstraints();
		gbc_chckbxShowGrid.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxShowGrid.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxShowGrid.gridx = 0;
		gbc_chckbxShowGrid.gridy = 0;
		add(chckbxShowGrid, gbc_chckbxShowGrid);
		
		chckbxShowEntities = new JCheckBox("Show Entities");
		GridBagConstraints gbc_chckbxShowEntities = new GridBagConstraints();
		gbc_chckbxShowEntities.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxShowEntities.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxShowEntities.gridx = 1;
		gbc_chckbxShowEntities.gridy = 0;
		add(chckbxShowEntities, gbc_chckbxShowEntities);
		
		chckbxShowBlocks = new JCheckBox("Show Blocks");
		GridBagConstraints gbc_chckbxShowBlocks = new GridBagConstraints();
		gbc_chckbxShowBlocks.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxShowBlocks.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxShowBlocks.gridx = 0;
		gbc_chckbxShowBlocks.gridy = 1;
		add(chckbxShowBlocks, gbc_chckbxShowBlocks);
		
		chckbxShowStatistics = new JCheckBox("Show Statistics");
		GridBagConstraints gbc_chckbxShowStatistics = new GridBagConstraints();
		gbc_chckbxShowStatistics.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxShowStatistics.gridx = 1;
		gbc_chckbxShowStatistics.gridy = 1;
		add(chckbxShowStatistics, gbc_chckbxShowStatistics);
	}

	public void setShowGridSelected(boolean selected) {
		chckbxShowGrid.setSelected(selected);
	}

	public boolean isShowGridSelected() {
		return chckbxShowGrid.isSelected();
	}

	public void setShowBlocksSelected(boolean selected) {
		chckbxShowBlocks.setSelected(selected);
	}

	public boolean isShowBlocksSelected() {
		return chckbxShowBlocks.isSelected();
	}
	
	public void setShowEntitiesSelected(boolean selected) {
		chckbxShowEntities.setSelected(selected);
	}

	public boolean isShowEntitiesSelected() {
		return chckbxShowEntities.isSelected();
	}
	
	public void setShowStatisticsSelected(boolean selected) {
		chckbxShowStatistics.setSelected(selected);
	}

	public boolean isShowStatisticsSelected() {
		return chckbxShowStatistics.isSelected();
	}
	
}
