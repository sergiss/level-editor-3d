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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public abstract class SkyboxFacePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int FIXED_HEIGHT = 50;

	private JButton btnRemove;

	private BufferedImage image;

	private JPanel imagePanel;

	@SuppressWarnings("serial")
	public SkyboxFacePanel(String name) {
		
		setBorder(new TitledBorder(null, name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isEnabled()) {
					if(e.getClickCount() > 1) {
						onDoubleClick();
					}
				}
			}
		});		
				
		imagePanel = new JPanel() {
			@Override
			public void paint(java.awt.Graphics g) {		
				if(image != null) {
					int size = (int) (Math.min(getWidth(), getHeight()));
					g.drawImage(image, getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size, null);
				}
				super.paint(g);
			}
		};
		
		imagePanel.setOpaque(false);
		GridBagConstraints gbc_imagePanel = new GridBagConstraints();
		gbc_imagePanel.fill = GridBagConstraints.BOTH;
		gbc_imagePanel.gridx = 0;
		gbc_imagePanel.gridy = 0;
		add(imagePanel, gbc_imagePanel);
		GridBagLayout gbl_imagePanel = new GridBagLayout();
		gbl_imagePanel.columnWidths = new int[]{0, 0, 0};
		gbl_imagePanel.rowHeights = new int[]{0, 0};
		gbl_imagePanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_imagePanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		imagePanel.setLayout(gbl_imagePanel);
		
		Component verticalStrut = Box.createVerticalStrut(FIXED_HEIGHT);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.fill = GridBagConstraints.HORIZONTAL;
		gbc_verticalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 0;
		imagePanel.add(verticalStrut, gbc_verticalStrut);
		
		btnRemove = new JButton("");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 0;
		imagePanel.add(btnRemove, gbc_btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeTextureRegion();
			}
		});
		btnRemove.setIcon(new ImageIcon(SkyboxFacePanel.class.getResource("/icons/remove.png")));
		btnRemove.setPreferredSize(new Dimension(16, 16));

	}
	
	@Override
	public void setEnabled(boolean enabled) {
		btnRemove.setEnabled(enabled);
		if(enabled == false) {
			image = null;
		}
		super.setEnabled(enabled);
	}
		
	public abstract void onDoubleClick();
	
	public abstract void removeTextureRegion();

	public void setBufferedImage(BufferedImage image) {
		this.image = image;
		btnRemove.setEnabled(image != null);
		imagePanel.repaint();
	}

}
