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

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.utils.ImageUtils;
import com.delmesoft.editor3d.utils.SwingUtils;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TileMapDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private static final Color RESULTS_COLOR = new Color(255, 127, 255, 127);
	private static final Color CURRENT_COLOR = new Color(127, 127, 255, 127);

	private int results;
	private int currentIndex;

	private int cols;
	private int rows;

	private BufferedImage[][] tiles;

	public TileMapDialog(JFrame frame, TiledTexture tiledTexture) {
		super(frame, true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					results = -1;
					setVisible(false);
				}
			}
		});
		
		Dimension dt = createTiles(tiledTexture);
		
		int tileWidth  = dt.width;
		int tileHeight = dt.height;
		
		setUndecorated(true);
		setResizable(false);
		setModal(true);
		getContentPane().setLayout(new CardLayout(0, 0));

		JPanel panel = new JPanel() {

			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);

				int x, y;
				for (x = 0; x < cols; ++x) {
					for (y = 0; y < rows; ++y) {
						g.drawImage(tiles[x][y], x * tileWidth, y * tileHeight, null);
					}
				}

				x = results % cols;
				y = results / cols;

				g.setColor(RESULTS_COLOR);
				g.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
				g.setColor(Color.GRAY);
				g.drawRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

				x = currentIndex % cols;
				y = currentIndex / cols;

				g.setColor(CURRENT_COLOR);
				g.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
				g.setColor(Color.GRAY);
				g.drawRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

			}

		};
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX() / tileWidth;
				int y = e.getY() / tileHeight;
				int index = y * cols + x;
				if (index != currentIndex) {
					currentIndex = index;
					repaint();
				}
			}
		});

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					int x = e.getX() / tileWidth;
					int y = e.getY() / tileHeight;
					results = y * cols + x;
				} else {
					results = -1;
				}
				setVisible(false);
			}
		});
		Dimension dimension = new Dimension(tileWidth * cols, tileHeight * rows);
		panel.setPreferredSize(dimension);

		JScrollPane scrollPane = new JScrollPane(panel);
		getContentPane().add(scrollPane);
		
		scrollPane.getVerticalScrollBar().setUnitIncrement(tiledTexture.getTileWidth());
		scrollPane.getHorizontalScrollBar().setUnitIncrement(tiledTexture.getTileHeight());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int scrollBarWidth = ((Integer) UIManager.get("ScrollBar.width")).intValue();
		
		setSize(new Dimension((int) Math.min(dimension.getWidth()  + scrollBarWidth, screenSize.width  * 0.75), 
				              (int) Math.min(dimension.getHeight() + scrollBarWidth, screenSize.height * 0.75)));
		setLocationRelativeTo(null);
	}

	private Dimension createTiles(TiledTexture tiledTexture) {
		
		BufferedImage[][] tiles = ImageUtils.tiledTextureToBufferedImageArray(tiledTexture);
		BufferedImage sample = tiles[0][0];
		
		cols = tiles.length;
		rows = tiles[0].length;
		
		int w = cols * sample.getWidth();
		int h = rows * sample.getHeight();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int minSize = (int) (Math.min(screenSize.getWidth(), screenSize.getHeight()) * 0.75F);
		double scale = 1.0;
		if(w > h) {
			if(w < minSize) {
				scale = (double) minSize / w;
			}
		} else {
			if(h < minSize) {
				scale = (double) minSize / h;
			}
		}
		
		Dimension dt = new Dimension();
		if(scale != 1.0) {
			for(int i = 0; i < cols; i++) {
				for(int j = 0; j < rows; j++) {
					tiles[i][j] = SwingUtils.scale(tiles[i][j], scale, scale, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				}
			}
			dt.width  = (int) (sample.getWidth()  * scale);
			dt.height = (int) (sample.getHeight() * scale);
		} else {
			dt.width  = sample.getWidth();
			dt.height = sample.getHeight();
		}
		this.tiles = tiles;
		return dt;
	}

	public void setLastSelection(int index) {
		results = index > -1 ? index : 0;
	}

	public int getResult() {
		return results;
	}

	public BufferedImage getBufferedImage(int index) {
		int x = index % cols;
		int y = index / cols;		
		return tiles[x][y];
	}

}
