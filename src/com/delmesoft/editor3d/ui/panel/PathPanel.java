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

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.Insets;
import javax.swing.ImageIcon;

public abstract class PathPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JCheckBox[] directionCheckBoxs;

	private JButton btnSelectAll;

	/**
	 * Create the panel.
	 */
	public PathPanel(String title) {
		
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JCheckBox chckbxN = new JCheckBox("N");
		chckbxN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isEnabled() == false || ((Component) e.getSource()).hasFocus() == false) return;
				onSelection(0);
			}
		});
		chckbxN.setFont(new Font("Dialog", Font.PLAIN, 8));
		GridBagConstraints gbc_chckbxN = new GridBagConstraints();
		gbc_chckbxN.anchor = GridBagConstraints.SOUTH;
		gbc_chckbxN.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxN.gridx = 1;
		gbc_chckbxN.gridy = 0;
		add(chckbxN, gbc_chckbxN);
		
		JCheckBox chckbxW = new JCheckBox("W");
		chckbxW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isEnabled() == false || ((Component) e.getSource()).hasFocus() == false) return;
				onSelection(3);
			}
		});
		chckbxW.setFont(new Font("Dialog", Font.PLAIN, 8));
		GridBagConstraints gbc_chckbxW = new GridBagConstraints();
		gbc_chckbxW.anchor = GridBagConstraints.EAST;
		gbc_chckbxW.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxW.gridx = 0;
		gbc_chckbxW.gridy = 1;
		add(chckbxW, gbc_chckbxW);
		
		JCheckBox chckbxE = new JCheckBox("E");
		chckbxE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isEnabled() == false || ((Component) e.getSource()).hasFocus() == false) return;
				onSelection(2);
			}
		});
		
		btnSelectAll = new JButton("");
		btnSelectAll.setIcon(new ImageIcon(PathPanel.class.getResource("/icons/arrows.png")));
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean result = false;
				for(JCheckBox checkBox : directionCheckBoxs) {
					if(checkBox.isSelected() == false) {
						result = true;
						break;
					}
				}
				
				for(JCheckBox checkBox : directionCheckBoxs) {
					checkBox.setSelected(result);
				}
				onSelection(4);
			}
		});
		GridBagConstraints gbc_btnSelectAll = new GridBagConstraints();
		gbc_btnSelectAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectAll.gridx = 1;
		gbc_btnSelectAll.gridy = 1;
		add(btnSelectAll, gbc_btnSelectAll);
		chckbxE.setFont(new Font("Dialog", Font.PLAIN, 8));
		GridBagConstraints gbc_chckbxE = new GridBagConstraints();
		gbc_chckbxE.anchor = GridBagConstraints.WEST;
		gbc_chckbxE.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxE.gridx = 2;
		gbc_chckbxE.gridy = 1;
		add(chckbxE, gbc_chckbxE);
		
		JCheckBox chckbxS = new JCheckBox("S");
		chckbxS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isEnabled() == false || ((Component) e.getSource()).hasFocus() == false) return;
				onSelection(1);
			}
		});
		chckbxS.setFont(new Font("Dialog", Font.PLAIN, 8));
		GridBagConstraints gbc_chckbxS = new GridBagConstraints();
		gbc_chckbxS.anchor = GridBagConstraints.NORTH;
		gbc_chckbxS.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxS.gridx = 1;
		gbc_chckbxS.gridy = 2;
		add(chckbxS, gbc_chckbxS);
		
		directionCheckBoxs = new JCheckBox[] { chckbxN, chckbxS, chckbxE, chckbxW };

	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);		
		for(JCheckBox checkBox : directionCheckBoxs) {
			checkBox.setEnabled(enabled);
		}
		btnSelectAll.setEnabled(enabled);
	}
	
	public abstract void onSelection(int direction);
	
	public boolean isDirectionSelected(int direction) {
		return directionCheckBoxs[direction].isSelected();
	}
	
	public void setDirectionSelected(int direction, boolean selected) {
		directionCheckBoxs[direction].setSelected(selected);
	}

}
