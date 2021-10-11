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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.ImageUtils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class SkyboxPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextField textFieldTextureAtlasPath;
	
	private SkyboxFacePanel panelTop;
	private SkyboxFacePanel panelBottom;
	private SkyboxFacePanel panelFront;
	private SkyboxFacePanel panelRight;
	private SkyboxFacePanel panelBack;
	private SkyboxFacePanel panelLeft;

	private Presenter presenter;

	private JButton buttonSetTextureAtlas;

	private JLabel lblTextureAtlas;
	
	public SkyboxPanel(Presenter presenter) {
		
		this.presenter = presenter;
		
		setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Sky Box", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panelTextureAtlas = new JPanel();
		GridBagConstraints gbc_panelTextureAtlas = new GridBagConstraints();
		gbc_panelTextureAtlas.insets = new Insets(0, 0, 5, 0);
		gbc_panelTextureAtlas.fill = GridBagConstraints.BOTH;
		gbc_panelTextureAtlas.gridx = 0;
		gbc_panelTextureAtlas.gridy = 0;
		add(panelTextureAtlas, gbc_panelTextureAtlas);
		GridBagLayout gbl_panelTextureAtlas = new GridBagLayout();
		gbl_panelTextureAtlas.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelTextureAtlas.rowHeights = new int[]{0, 0};
		gbl_panelTextureAtlas.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelTextureAtlas.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelTextureAtlas.setLayout(gbl_panelTextureAtlas);
		
		lblTextureAtlas = new JLabel("Texture Atlas:");
		GridBagConstraints gbc_lblTextureAtlas = new GridBagConstraints();
		gbc_lblTextureAtlas.insets = new Insets(0, 0, 0, 5);
		gbc_lblTextureAtlas.anchor = GridBagConstraints.EAST;
		gbc_lblTextureAtlas.gridx = 0;
		gbc_lblTextureAtlas.gridy = 0;
		panelTextureAtlas.add(lblTextureAtlas, gbc_lblTextureAtlas);
		
		textFieldTextureAtlasPath = new JTextField();
		textFieldTextureAtlasPath.setEditable(false);
		GridBagConstraints gbc_textFieldTextureAtlasPath = new GridBagConstraints();
		gbc_textFieldTextureAtlasPath.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldTextureAtlasPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTextureAtlasPath.gridx = 1;
		gbc_textFieldTextureAtlasPath.gridy = 0;
		panelTextureAtlas.add(textFieldTextureAtlasPath, gbc_textFieldTextureAtlasPath);
		textFieldTextureAtlasPath.setColumns(10);
		
		buttonSetTextureAtlas = new JButton("...");
		buttonSetTextureAtlas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().loadSkyboxTextureAtlas();
				
			}
		});
		GridBagConstraints gbc_buttonSetTextureAtlas = new GridBagConstraints();
		gbc_buttonSetTextureAtlas.gridx = 2;
		gbc_buttonSetTextureAtlas.gridy = 0;
		panelTextureAtlas.add(buttonSetTextureAtlas, gbc_buttonSetTextureAtlas);
		
		JPanel panelVertical = new JPanel();
		GridBagConstraints gbc_panelVertical = new GridBagConstraints();
		gbc_panelVertical.insets = new Insets(0, 0, 5, 0);
		gbc_panelVertical.fill = GridBagConstraints.BOTH;
		gbc_panelVertical.gridx = 0;
		gbc_panelVertical.gridy = 1;
		add(panelVertical, gbc_panelVertical);
		GridBagLayout gbl_panelVertical = new GridBagLayout();
		gbl_panelVertical.columnWidths = new int[]{0, 0, 0};
		gbl_panelVertical.rowHeights = new int[]{0, 0};
		gbl_panelVertical.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelVertical.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelVertical.setLayout(gbl_panelVertical);
		
		panelTop = new SkyboxFacePanel("Top") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelTop, Side.TOP);
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelTop, Side.TOP);
				
			}			
		};
		GridBagConstraints gbc_panelTop = new GridBagConstraints();
		gbc_panelTop.fill = GridBagConstraints.BOTH;
		panelVertical.add(panelTop, gbc_panelTop);
		
		panelBottom = new SkyboxFacePanel("Bottom") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelBottom, Side.BOTTOM);			
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelBottom, Side.BOTTOM);		
				
			}			
		};
		GridBagConstraints gbc_panelBottom = new GridBagConstraints();
		gbc_panelBottom.fill = GridBagConstraints.BOTH;
		panelVertical.add(panelBottom, gbc_panelBottom);
		
		JPanel panelHorizontal = new JPanel();
		GridBagConstraints gbc_panelHorizontal = new GridBagConstraints();
		gbc_panelHorizontal.fill = GridBagConstraints.BOTH;
		gbc_panelHorizontal.gridx = 0;
		gbc_panelHorizontal.gridy = 2;
		add(panelHorizontal, gbc_panelHorizontal);
		GridBagLayout gbl_panelHorizontal = new GridBagLayout();
		gbl_panelHorizontal.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panelHorizontal.rowHeights = new int[]{0, 0};
		gbl_panelHorizontal.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panelHorizontal.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelHorizontal.setLayout(gbl_panelHorizontal);
		
		panelFront = new SkyboxFacePanel("Front") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelFront, Side.FRONT);			
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelFront, Side.FRONT);	
				
			}			
		};
		GridBagConstraints gbc_panelFront = new GridBagConstraints();
		gbc_panelFront.fill = GridBagConstraints.BOTH;
		panelHorizontal.add(panelFront, gbc_panelFront);
		
		panelRight = new SkyboxFacePanel("Right") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelRight, Side.RIGHT);			
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelRight, Side.RIGHT);	
				
			}			
		};
		GridBagConstraints gbc_panelRight = new GridBagConstraints();
		gbc_panelRight.fill = GridBagConstraints.BOTH;
		panelHorizontal.add(panelRight, gbc_panelRight);
				
		panelBack = new SkyboxFacePanel("Back") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelBack, Side.BACK);					
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelBack, Side.BACK);
				
			}			
		};
		GridBagConstraints gbc_panelBack = new GridBagConstraints();
		gbc_panelBack.fill = GridBagConstraints.BOTH;
		panelHorizontal.add(panelBack, gbc_panelBack);
		
		panelLeft = new SkyboxFacePanel("Left") {
			@Override
			public void onDoubleClick() {
				setSkyboxRegion(panelLeft, Side.LEFT);	
			}

			@Override
			public void removeTextureRegion() {
				removeSkyboxRegion(panelLeft, Side.LEFT);	
				
			}			
		};
		GridBagConstraints gbc_panelLeft = new GridBagConstraints();
		gbc_panelLeft.fill = GridBagConstraints.BOTH;
		panelHorizontal.add(panelLeft, gbc_panelLeft);
				
	}
		
	@Override
	public void setEnabled(boolean b) {
		lblTextureAtlas.setEnabled(b);
		textFieldTextureAtlasPath.setEnabled(b);
		buttonSetTextureAtlas.setEnabled(b);
		if (b == false) {
			setSkyboxFacePanelsEnabled(false);
		}
		super.setEnabled(b);
	}

	private void setSkyboxFacePanelsEnabled(boolean b) {
		panelBack.setEnabled(b);
		panelBottom.setEnabled(b);
		panelFront.setEnabled(b);
		panelLeft.setEnabled(b);
		panelRight.setEnabled(b);
		panelTop.setEnabled(b);
	}

	public void setTextureAtlassPath(String path) {
		if (path == null) {
			setSkyboxFacePanelsEnabled(false);
			textFieldTextureAtlasPath.setText("");
			textFieldTextureAtlasPath.setEnabled(false);
			return;
		}

		textFieldTextureAtlasPath.setText(path);
		setSkyboxFacePanelsEnabled(true);
	}

	public void setSkyboxRegion(Side side, AtlasRegion region) {
		switch(side) {
		case BACK:
			setSkyboxRegion(panelBack, region);
			break;
		case BOTTOM:
			setSkyboxRegion(panelBottom, region);
			break;
		case FRONT:
			setSkyboxRegion(panelFront, region);
			break;
		case LEFT:
			setSkyboxRegion(panelLeft, region);
			break;
		case RIGHT:
			setSkyboxRegion(panelRight, region);
			break;
		case TOP:
			setSkyboxRegion(panelTop, region);
			break;
		default:
			break;
		
		}
	}
	
	private void setSkyboxRegion(SkyboxFacePanel panel, AtlasRegion region) {
		BufferedImage image = null;
		if(region != null) {
			image = ImageUtils.atlasRegionToBufferedImage(region);
		}
		panel.setBufferedImage(image);
	}

	// user
	private void setSkyboxRegion(SkyboxFacePanel panel, Side side) {
		String regionName = presenter.getEditor().openAtlasRegionSelector(presenter.getLevel().getSkybox().getTextureAtlas());	
		if(regionName != null)
			presenter.getEditor().setSkyboxRegion(side, regionName);	
		
	}
	// user
	private void removeSkyboxRegion(SkyboxFacePanel panel, Side side) {
		panel.setBufferedImage(null);
		presenter.getEditor().setSkyboxRegion(side, null);	
	}


}
