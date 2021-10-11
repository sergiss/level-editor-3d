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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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

import com.delmesoft.editor3d.level.block.Side;

public abstract class FacePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Color SELECTION_COLOR = new Color(127, 127, 255, 127);

	private JButton btnRemove;
	
	private BufferedImage bufferedImage;

	private boolean selected;
	
	private final FaceData faceData;
	
	private final Side side;
	
	public FacePanel(Side side) {
		
		this.side = side;
		
		this.faceData = new FaceData();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isEnabled()) {
					if(e.getClickCount() == 1) {
						onSelection();
					} else {
						onDoubleClick();
					}
				}
			}
		});		
		
		String name = side.name();
		name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

		setBorder(new TitledBorder(null, name, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		Dimension dimension = new Dimension(100, 100);
		setPreferredSize(dimension);
		setPreferredSize(dimension);
		setMinimumSize(dimension);		

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g) {
				
				if (bufferedImage != null && faceData.isEnabled()) {
					int x = (getWidth() - getHeight()) >> 1;
					g.drawImage(bufferedImage, x, 0, getHeight(), getHeight(), null);
				}
				
				if(selected) {
					g.setColor(SELECTION_COLOR);
					g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
					g.setColor(Color.BLACK);
					g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				}
				super.paint(g);
			}
		};
		panel.setOpaque(false);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		Component glue = Box.createGlue();
		GridBagConstraints gbc_glue = new GridBagConstraints();
		gbc_glue.insets = new Insets(0, 0, 5, 5);
		gbc_glue.fill = GridBagConstraints.BOTH;
		gbc_glue.gridx = 0;
		gbc_glue.gridy = 0;
		panel.add(glue, gbc_glue);

		btnRemove = new JButton("");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.fill = GridBagConstraints.BOTH;
		gbc_btnRemove.gridx = 1;
		gbc_btnRemove.gridy = 1;
		panel.add(btnRemove, gbc_btnRemove);
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onRemove();
			}
		});
		btnRemove.setIcon(new ImageIcon(FacePanel.class.getResource("/icons/remove.png")));
		btnRemove.setPreferredSize(new Dimension(16, 16));

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnRemove.setEnabled(enabled);
		setBufferedImage(null);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		repaint();
	}
	
	public Side getSide() {
		return side;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}
	
	public boolean isSelected() {
		return selected;
	}

	public FaceData getFaceData() {
		return faceData;
	}

	public abstract void onRemove();
	
	public abstract void onSelection();
	
	public abstract void onDoubleClick();
	
	public class FaceData {
		
		private int index;
		
		private int angle;	
		
		private boolean flippedHorizontally;
		private boolean flippedVertically;
		
		private boolean blending;
		
		private boolean enabled;

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public int getAngle() {
			return angle;
		}

		public void setAngle(int angle) {
			this.angle = angle;
		}

		public boolean isFlippedHorizontally() {
			return flippedHorizontally;
		}

		public void setFlippedHorizontally(boolean flippedHorizontally) {
			this.flippedHorizontally = flippedHorizontally;
		}

		public boolean isFlippedVertically() {
			return flippedVertically;
		}

		public void setFlippedVertically(boolean flippedVertically) {
			this.flippedVertically = flippedVertically;
		}

		public boolean isBlending() {
			return blending;
		}

		public void setBlending(boolean blending) {
			this.blending = blending;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
			btnRemove.setEnabled(enabled);
			repaint();
		}		
		
	}
	
}
