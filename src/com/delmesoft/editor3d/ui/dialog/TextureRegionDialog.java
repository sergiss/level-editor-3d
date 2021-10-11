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
package com.delmesoft.editor3d.ui.dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.delmesoft.editor3d.ui.View;
import com.delmesoft.editor3d.utils.ImageUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class TextureRegionDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private static final Color RESULTS_COLOR = new Color(255, 127, 255, 127);

	private String result;
	
	private JButton buttonLeft;
	private JButton buttonRight;
	
	private List<Page> pages;
	private int currentPage;

	private JPanel panel;

	private AtlasRegion selectedRegion;

	private TextureAtlas textureAtlas;	

	public TextureRegionDialog(View view) {
		
		super(view.getJFrame(), true);
		
		pages = new ArrayList<>();
		
		setUndecorated(true);
		setResizable(false);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);

		panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Page page = pages.get(currentPage);
				if (page != null) {
					g.drawImage(page.bufferedImage, 0, 0, null);
				}
				
				if(selectedRegion != null) {
					g.setColor(RESULTS_COLOR);
					g.fillRect(selectedRegion.getRegionX(), selectedRegion.getRegionY(), selectedRegion.getRegionWidth(), selectedRegion.getRegionHeight());
					g.setColor(Color.RED);
					g.drawRect(selectedRegion.getRegionX(), selectedRegion.getRegionY(), selectedRegion.getRegionWidth(), selectedRegion.getRegionHeight());
				}
				
			}

		};

		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
				AtlasRegion result = null;
				
				int x = e.getX();
				int y = e.getY();
				
				for(AtlasRegion region : pages.get(currentPage).regions) {
					if(hit(x, y, region)) {						
						result = region;
						break;						
					}
				}
				if(selectedRegion != result) {
					selectedRegion = result;
					repaint();
				}
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					result = selectedRegion != null ? selectedRegion.name : null;
				} else {
					result = null;
				}
				setVisible(false);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(panel);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		getContentPane().add(scrollPane, gbc_scrollPane);

		JPanel toolPanel = new JPanel();
		GridBagConstraints gbc_toolPanel = new GridBagConstraints();
		gbc_toolPanel.fill = GridBagConstraints.BOTH;
		gbc_toolPanel.gridx = 0;
		gbc_toolPanel.gridy = 1;
		getContentPane().add(toolPanel, gbc_toolPanel);
		GridBagLayout gbl_toolPanel = new GridBagLayout();
		gbl_toolPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_toolPanel.rowHeights = new int[]{0, 0};
		gbl_toolPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_toolPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		toolPanel.setLayout(gbl_toolPanel);

		buttonLeft = new JButton("<");
		buttonLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentPage(currentPage - 1);
			}
		});
		GridBagConstraints gbc_buttonLeft = new GridBagConstraints();
		gbc_buttonLeft.insets = new Insets(0, 0, 0, 5);
		gbc_buttonLeft.gridx = 0;
		gbc_buttonLeft.gridy = 0;
		toolPanel.add(buttonLeft, gbc_buttonLeft);

		buttonRight = new JButton(">");
		buttonRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCurrentPage(currentPage + 1);
			}
		});
		GridBagConstraints gbc_buttonRight = new GridBagConstraints();
		gbc_buttonRight.insets = new Insets(0, 0, 0, 5);
		gbc_buttonRight.gridx = 1;
		gbc_buttonRight.gridy = 0;
		toolPanel.add(buttonRight, gbc_buttonRight);

		JButton btnClose = new JButton("close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = null;
				setVisible(false);
			}
		});
		GridBagConstraints gbc_btnClose = new GridBagConstraints();
		gbc_btnClose.anchor = GridBagConstraints.EAST;
		gbc_btnClose.gridx = 2;
		gbc_btnClose.gridy = 0;
		toolPanel.add(btnClose, gbc_btnClose);

		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
				
	}

	protected boolean hit(int x, int y, AtlasRegion region) {
		return x > region.getRegionX() && x < region.getRegionX() + region.getRegionWidth() && 
			   y > region.getRegionY() && y < region.getRegionY() + region.getRegionHeight();
	}

	private void setCurrentPage(int i) {
		
		if(i < 0 || i >= pages.size()) return;
		
		currentPage = i;
		
		Page page = pages.get(i);
						
		Dimension dimension = new Dimension(page.bufferedImage.getWidth(), page.bufferedImage.getHeight());
		panel.setPreferredSize(dimension);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		int scrollBarWidth = ((Integer) UIManager.get("ScrollBar.width")).intValue();
		
		setSize(new Dimension((int) Math.min(dimension.getWidth()  + scrollBarWidth, screenSize.width  * 0.85), 
				              (int) Math.min(dimension.getHeight() + scrollBarWidth, screenSize.height * 0.85)));
		
		setLocationRelativeTo(null);
		
		panel.repaint();
		panel.revalidate();

		buttonLeft.setEnabled(i > 0);
		buttonRight.setEnabled(i < pages.size() - 1);
		
	}
	
	public void setTextureAtlas(TextureAtlas textureAtlas) {
		pages.clear();
		Page page;
		for(Texture texture : textureAtlas.getTextures()) {
			page = new Page();
			page.bufferedImage = ImageUtils.textureToBufferedImage(texture);
			for(AtlasRegion region : textureAtlas.getRegions()) {
				if(region.getTexture() == texture) {
					page.regions.add(region);
				}
			}
			pages.add(page);
		}		
		setCurrentPage(this.textureAtlas != textureAtlas ? 0 : currentPage);
		this.textureAtlas = textureAtlas;
	}

	public String open() {				
		setVisible(true);		
		return result;
	}
	
	private static class Page {		
		BufferedImage bufferedImage;		
		List<AtlasRegion> regions = new ArrayList<>();		
	}

}
