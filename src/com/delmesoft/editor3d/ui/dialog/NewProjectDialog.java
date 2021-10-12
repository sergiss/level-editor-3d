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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.utils.Utils;

public class NewProjectDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JTextField textFieldName;

	private JLabel lblBlockCount;

	private JSpinner spinnerWidth;
	private JSpinner spinnerHeight;
	private JSpinner spinnerDepth;

	private Settings results;

	private JLabel lblMemory;
	private JTextField textFieldSource;

	private JButton okButton;

	private JSpinner spinnerTileWidth;

	private JSpinner spinnerTileHeight;

	private JSpinner spinnerMargin;

	private JSpinner spinnerSpacing;

	/**
	 * Create the dialog.
	 * @param viewImpl 
	 */
	public NewProjectDialog(JFrame frame) {
		super(frame, true);

		setResizable(false);

		setTitle("New Project");

		setBounds(100, 100, 320, 390);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		getContentPane().setLayout(gridBagLayout);
		contentPanel.setBorder(new TitledBorder(null, "Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_contentPanel = new GridBagConstraints();
		gbc_contentPanel.fill = GridBagConstraints.BOTH;
		gbc_contentPanel.insets = new Insets(0, 0, 5, 0);
		gbc_contentPanel.gridx = 0;
		gbc_contentPanel.gridy = 0;
		getContentPane().add(contentPanel, gbc_contentPanel);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblInfo = new JLabel("Info");
			lblInfo.setFont(new Font("Dialog", Font.BOLD, 13));
			GridBagConstraints gbc_lblInfo = new GridBagConstraints();
			gbc_lblInfo.gridwidth = 3;
			gbc_lblInfo.insets = new Insets(0, 0, 5, 0);
			gbc_lblInfo.anchor = GridBagConstraints.WEST;
			gbc_lblInfo.gridx = 0;
			gbc_lblInfo.gridy = 0;
			contentPanel.add(lblInfo, gbc_lblInfo);
		}
		{
			JLabel lblName = new JLabel("Name:");
			lblName.setFont(new Font("Dialog", Font.PLAIN, 12));
			GridBagConstraints gbc_lblName = new GridBagConstraints();
			gbc_lblName.insets = new Insets(0, 0, 5, 5);
			gbc_lblName.anchor = GridBagConstraints.WEST;
			gbc_lblName.gridx = 0;
			gbc_lblName.gridy = 1;
			contentPanel.add(lblName, gbc_lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setText(Settings.DEFAULT_LEVEL_NAME);
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.gridwidth = 2;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 1;
			contentPanel.add(textFieldName, gbc_textField);
			textFieldName.setColumns(10);
		}
		{
			JLabel lblTileset = new JLabel("Tileset");
			lblTileset.setFont(new Font("Dialog", Font.BOLD, 13));
			GridBagConstraints gbc_lblTileset = new GridBagConstraints();
			gbc_lblTileset.gridwidth = 3;
			gbc_lblTileset.anchor = GridBagConstraints.WEST;
			gbc_lblTileset.insets = new Insets(0, 0, 5, 5);
			gbc_lblTileset.gridx = 0;
			gbc_lblTileset.gridy = 2;
			contentPanel.add(lblTileset, gbc_lblTileset);
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 3;
			gbc_panel.insets = new Insets(0, 0, 5, 0);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0};
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblSource = new JLabel("Source:");
				lblSource.setFont(new Font("Dialog", Font.PLAIN, 12));
				GridBagConstraints gbc_lblSource = new GridBagConstraints();
				gbc_lblSource.insets = new Insets(0, 0, 0, 5);
				gbc_lblSource.anchor = GridBagConstraints.WEST;
				gbc_lblSource.gridx = 0;
				gbc_lblSource.gridy = 0;
				panel.add(lblSource, gbc_lblSource);
			}
			{
				textFieldSource = new JTextField();
				textFieldSource.setEditable(false);
				GridBagConstraints gbc_textFieldSource = new GridBagConstraints();
				gbc_textFieldSource.insets = new Insets(0, 0, 0, 5);
				gbc_textFieldSource.fill = GridBagConstraints.HORIZONTAL;
				gbc_textFieldSource.gridx = 1;
				gbc_textFieldSource.gridy = 0;
				panel.add(textFieldSource, gbc_textFieldSource);
				textFieldSource.setColumns(10);
			}
			{
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				fileChooser.setFileFilter( new FileNameExtensionFilter("Image Files (*.jpg *.png)", "jpg", "png"));
				
				JButton buttonSource = new JButton("...");
				buttonSource.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int returnVal = fileChooser.showOpenDialog(NewProjectDialog.this);

						if(returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fileChooser.getSelectedFile();
							textFieldSource.setText(file.getAbsolutePath());
							okButton.setEnabled(true);
						}

					}
				});
				GridBagConstraints gbc_buttonSource = new GridBagConstraints();
				gbc_buttonSource.gridx = 2;
				gbc_buttonSource.gridy = 0;
				panel.add(buttonSource, gbc_buttonSource);
			}
		}
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridwidth = 3;
			gbc_panel.insets = new Insets(0, 0, 5, 5);
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 4;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0};
			gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblWidth_1 = new JLabel("Width:");
				lblWidth_1.setFont(new Font("Dialog", Font.PLAIN, 12));
				GridBagConstraints gbc_lblWidth_1 = new GridBagConstraints();
				gbc_lblWidth_1.anchor = GridBagConstraints.WEST;
				gbc_lblWidth_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblWidth_1.gridx = 0;
				gbc_lblWidth_1.gridy = 0;
				panel.add(lblWidth_1, gbc_lblWidth_1);
			}
			{
				spinnerTileWidth = new JSpinner();
				spinnerTileWidth.setModel(new SpinnerNumberModel(new Integer(Settings.DEFAULT_TILE_SIZE), new Integer(1), null, new Integer(1)));
				GridBagConstraints gbc_spinnerTileWidth = new GridBagConstraints();
				gbc_spinnerTileWidth.fill = GridBagConstraints.HORIZONTAL;
				gbc_spinnerTileWidth.insets = new Insets(0, 0, 5, 5);
				gbc_spinnerTileWidth.gridx = 1;
				gbc_spinnerTileWidth.gridy = 0;
				panel.add(spinnerTileWidth, gbc_spinnerTileWidth);
			}
			{
				JLabel lblMargin = new JLabel("Margin:");
				lblMargin.setFont(new Font("Dialog", Font.PLAIN, 12));
				GridBagConstraints gbc_lblMargin = new GridBagConstraints();
				gbc_lblMargin.anchor = GridBagConstraints.WEST;
				gbc_lblMargin.insets = new Insets(0, 0, 5, 5);
				gbc_lblMargin.gridx = 2;
				gbc_lblMargin.gridy = 0;
				panel.add(lblMargin, gbc_lblMargin);
			}
			{
				spinnerMargin = new JSpinner();
				spinnerMargin.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
				GridBagConstraints gbc_spinnerMargin = new GridBagConstraints();
				gbc_spinnerMargin.fill = GridBagConstraints.HORIZONTAL;
				gbc_spinnerMargin.insets = new Insets(0, 0, 5, 0);
				gbc_spinnerMargin.gridx = 3;
				gbc_spinnerMargin.gridy = 0;
				panel.add(spinnerMargin, gbc_spinnerMargin);
			}
			{
				JLabel lblHeight_1 = new JLabel("Height:");
				lblHeight_1.setFont(new Font("Dialog", Font.PLAIN, 12));
				GridBagConstraints gbc_lblHeight_1 = new GridBagConstraints();
				gbc_lblHeight_1.anchor = GridBagConstraints.WEST;
				gbc_lblHeight_1.insets = new Insets(0, 0, 0, 5);
				gbc_lblHeight_1.gridx = 0;
				gbc_lblHeight_1.gridy = 1;
				panel.add(lblHeight_1, gbc_lblHeight_1);
			}
			{
				spinnerTileHeight = new JSpinner();
				spinnerTileHeight.setModel(new SpinnerNumberModel(new Integer(Settings.DEFAULT_TILE_SIZE), new Integer(1), null, new Integer(1)));
				GridBagConstraints gbc_spinnerTileHeight = new GridBagConstraints();
				gbc_spinnerTileHeight.fill = GridBagConstraints.HORIZONTAL;
				gbc_spinnerTileHeight.insets = new Insets(0, 0, 0, 5);
				gbc_spinnerTileHeight.gridx = 1;
				gbc_spinnerTileHeight.gridy = 1;
				panel.add(spinnerTileHeight, gbc_spinnerTileHeight);
			}
			{
				JLabel lblSpacing = new JLabel("Spacing:");
				lblSpacing.setFont(new Font("Dialog", Font.PLAIN, 12));
				GridBagConstraints gbc_lblSpacing = new GridBagConstraints();
				gbc_lblSpacing.insets = new Insets(0, 0, 0, 5);
				gbc_lblSpacing.anchor = GridBagConstraints.WEST;
				gbc_lblSpacing.gridx = 2;
				gbc_lblSpacing.gridy = 1;
				panel.add(lblSpacing, gbc_lblSpacing);
			}
			{
				spinnerSpacing = new JSpinner();
				spinnerSpacing.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
				GridBagConstraints gbc_spinnerSpacing = new GridBagConstraints();
				gbc_spinnerSpacing.fill = GridBagConstraints.HORIZONTAL;
				gbc_spinnerSpacing.gridx = 3;
				gbc_spinnerSpacing.gridy = 1;
				panel.add(spinnerSpacing, gbc_spinnerSpacing);
			}
		}
		{
			JLabel lblDimensions = new JLabel("Dimensions");
			lblDimensions.setFont(new Font("Dialog", Font.BOLD, 13));
			GridBagConstraints gbc_lblDimensions = new GridBagConstraints();
			gbc_lblDimensions.insets = new Insets(0, 0, 5, 0);
			gbc_lblDimensions.anchor = GridBagConstraints.WEST;
			gbc_lblDimensions.gridwidth = 3;
			gbc_lblDimensions.gridx = 0;
			gbc_lblDimensions.gridy = 5;
			contentPanel.add(lblDimensions, gbc_lblDimensions);
		}
		{
			JLabel lblWidth = new JLabel("Width:");
			lblWidth.setFont(new Font("Dialog", Font.PLAIN, 12));
			GridBagConstraints gbc_lblWidth = new GridBagConstraints();
			gbc_lblWidth.anchor = GridBagConstraints.WEST;
			gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
			gbc_lblWidth.gridx = 0;
			gbc_lblWidth.gridy = 6;
			contentPanel.add(lblWidth, gbc_lblWidth);
		}
		{
			spinnerWidth = new JSpinner();
			spinnerWidth.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateStats();
				}
			});
			spinnerWidth.setModel(new SpinnerNumberModel(new Integer(Settings.DEFAULT_WIDTH), new Integer(1), new Integer(Settings.MAX_HORIZONTAL_CHUNKS), new Integer(1)));
			GridBagConstraints gbc_spinnerWidth = new GridBagConstraints();
			gbc_spinnerWidth.fill = GridBagConstraints.BOTH;
			gbc_spinnerWidth.insets = new Insets(0, 0, 5, 5);
			gbc_spinnerWidth.gridx = 1;
			gbc_spinnerWidth.gridy = 6;
			contentPanel.add(spinnerWidth, gbc_spinnerWidth);
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Stats", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.gridheight = 3;
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 2;
			gbc_panel.gridy = 6;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.rowWeights = new double[]{1.0, 1.0};
			gbl_panel.columnWidths = new int[]{0, 0, 0};
			gbl_panel.rowHeights = new int[]{0, 0};
			panel.setLayout(gbl_panel);
			{
				JLabel lblStats1 = new JLabel("Block count:");
				GridBagConstraints gbc_lblStats1 = new GridBagConstraints();
				gbc_lblStats1.anchor = GridBagConstraints.WEST;
				gbc_lblStats1.insets = new Insets(0, 0, 5, 5);
				gbc_lblStats1.gridx = 0;
				gbc_lblStats1.gridy = 0;
				panel.add(lblStats1, gbc_lblStats1);
				lblStats1.setFont(new Font("Dialog", Font.PLAIN, 10));
			}
			{
				lblBlockCount = new JLabel("-");
				GridBagConstraints gbc_lblBlockCount = new GridBagConstraints();
				gbc_lblBlockCount.insets = new Insets(0, 0, 5, 5);
				gbc_lblBlockCount.anchor = GridBagConstraints.WEST;
				gbc_lblBlockCount.gridx = 1;
				gbc_lblBlockCount.gridy = 0;
				panel.add(lblBlockCount, gbc_lblBlockCount);
				lblBlockCount.setFont(new Font("Dialog", Font.PLAIN, 10));
			}
			{
				JLabel lblStats2 = new JLabel("Memory:");
				lblStats2.setFont(new Font("Dialog", Font.PLAIN, 10));
				GridBagConstraints gbc_lblStats2 = new GridBagConstraints();
				gbc_lblStats2.anchor = GridBagConstraints.WEST;
				gbc_lblStats2.insets = new Insets(0, 0, 0, 5);
				gbc_lblStats2.gridx = 0;
				gbc_lblStats2.gridy = 1;
				panel.add(lblStats2, gbc_lblStats2);
			}
			{
				lblMemory = new JLabel("-");
				lblMemory.setFont(new Font("Dialog", Font.PLAIN, 10));
				GridBagConstraints gbc_lblMemory = new GridBagConstraints();
				gbc_lblMemory.insets = new Insets(0, 0, 0, 5);
				gbc_lblMemory.anchor = GridBagConstraints.WEST;
				gbc_lblMemory.gridx = 1;
				gbc_lblMemory.gridy = 1;
				panel.add(lblMemory, gbc_lblMemory);
			}
		}
		{
			JLabel lblHeight = new JLabel("Height:");
			lblHeight.setFont(new Font("Dialog", Font.PLAIN, 12));
			GridBagConstraints gbc_lblHeight = new GridBagConstraints();
			gbc_lblHeight.anchor = GridBagConstraints.WEST;
			gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
			gbc_lblHeight.gridx = 0;
			gbc_lblHeight.gridy = 7;
			contentPanel.add(lblHeight, gbc_lblHeight);
		}
		{
			spinnerHeight = new JSpinner();
			spinnerHeight.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateStats();
				}
			});
			spinnerHeight.setModel(new SpinnerNumberModel(new Integer(Settings.DEFAULT_HEIGHT), new Integer(1), new Integer(Settings.MAX_VERTICAL_PLOTS), new Integer(1)));
			GridBagConstraints gbc_spinnerHeight = new GridBagConstraints();
			gbc_spinnerHeight.fill = GridBagConstraints.BOTH;
			gbc_spinnerHeight.insets = new Insets(0, 0, 5, 5);
			gbc_spinnerHeight.gridx = 1;
			gbc_spinnerHeight.gridy = 7;
			contentPanel.add(spinnerHeight, gbc_spinnerHeight);
		}
		{
			JLabel lblDepth = new JLabel("Depth:");
			lblDepth.setFont(new Font("Dialog", Font.PLAIN, 12));
			GridBagConstraints gbc_lblDepth = new GridBagConstraints();
			gbc_lblDepth.anchor = GridBagConstraints.WEST;
			gbc_lblDepth.insets = new Insets(0, 0, 0, 5);
			gbc_lblDepth.gridx = 0;
			gbc_lblDepth.gridy = 8;
			contentPanel.add(lblDepth, gbc_lblDepth);
		}
		{
			spinnerDepth = new JSpinner();
			spinnerDepth.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					updateStats();
				}
			});
			spinnerDepth.setModel(new SpinnerNumberModel(new Integer(Settings.DEFAULT_DEPTH), new Integer(1), new Integer(Settings.MAX_HORIZONTAL_CHUNKS), new Integer(1)));
			GridBagConstraints gbc_spinnerDepth = new GridBagConstraints();
			gbc_spinnerDepth.fill = GridBagConstraints.BOTH;
			gbc_spinnerDepth.insets = new Insets(0, 0, 0, 5);
			gbc_spinnerDepth.gridx = 1;
			gbc_spinnerDepth.gridy = 8;
			contentPanel.add(spinnerDepth, gbc_spinnerDepth);
		}
		{
			JPanel buttonPane = new JPanel();
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.fill = GridBagConstraints.BOTH;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 1;
			getContentPane().add(buttonPane, gbc_buttonPane);
			GridBagLayout gbl_buttonPane = new GridBagLayout();
			gbl_buttonPane.rowWeights = new double[]{1.0};
			gbl_buttonPane.columnWeights = new double[]{1.0, 0.0};
			buttonPane.setLayout(gbl_buttonPane);
			{
				okButton = new JButton("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						results = new Settings();

						int width  = (int) spinnerWidth.getValue();
						int height = (int) spinnerHeight.getValue();
						int depth  = (int) spinnerDepth.getValue();

						results.initialize(textFieldName.getText(), width, height, depth);
						
						results.texturePath = textFieldSource.getText();
						results.tileWidth   = (int) spinnerTileWidth.getValue();
						results.tileHeight  = (int) spinnerTileHeight.getValue();
						results.margin      = (int) spinnerMargin.getValue();
						results.spacing     = (int) spinnerSpacing.getValue();
						
						NewProjectDialog.this.dispose();
					}
				});
				okButton.setActionCommand("OK");
				GridBagConstraints gbc_okButton = new GridBagConstraints();
				gbc_okButton.fill = GridBagConstraints.VERTICAL;
				gbc_okButton.anchor = GridBagConstraints.EAST;
				gbc_okButton.insets = new Insets(0, 0, 0, 5);
				gbc_okButton.gridx = 0;
				gbc_okButton.gridy = 0;
				buttonPane.add(okButton, gbc_okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						results = null;
						NewProjectDialog.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				GridBagConstraints gbc_cancelButton = new GridBagConstraints();
				gbc_cancelButton.fill = GridBagConstraints.VERTICAL;
				gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
				gbc_cancelButton.anchor = GridBagConstraints.WEST;
				gbc_cancelButton.gridx = 1;
				gbc_cancelButton.gridy = 0;
				buttonPane.add(cancelButton, gbc_cancelButton);
			}
		}

		updateStats();

		setLocationRelativeTo(null);

	}

	private void updateStats() {

		int w = ((Integer) spinnerWidth.getValue())  * Plot.SIZE;
		int h = ((Integer) spinnerHeight.getValue()) * Plot.SIZE;
		int d = ((Integer) spinnerDepth.getValue())  * Plot.SIZE;

		int n = w * h * d;

		lblBlockCount.setText(String.valueOf(n));
		lblMemory.setText(String.format("%.02f (MB)", Math.ceil((((long) n * ChunkData.BITS) * Utils.bitsToMegabytes * 100F)) / 100F));
	}

	public Settings getResults() {
		return results;
	}

}
