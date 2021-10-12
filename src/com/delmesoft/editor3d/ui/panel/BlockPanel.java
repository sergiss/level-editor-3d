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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.badlogic.gdx.Gdx;
import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.ui.dialog.TileMapDialog;
import com.delmesoft.editor3d.ui.panel.FacePanel.FaceData;
import com.delmesoft.editor3d.utils.SwingUtils;

public class BlockPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Presenter presenter;

	private FacePanel backPanel;
	private FacePanel bottomPanel;
	private FacePanel leftPanel;
	private FacePanel topPanel;
	private FacePanel rightPanel;
	private FacePanel frontPanel;

	private JLabel lblGeometry;
	private JSpinner spinnerGeometryType;

	private JPanel blockPanel;
	private JCheckBox chckbxBlending;
	private JSpinner spinnerTextureIndex;
	private JSpinner spinnerFaceAngle;
	private JCheckBox chckbxFlipVertical;
	private JCheckBox chckbxFlipHorizontal;

	private JPanel facePanel;

	private JLabel lblIndex;
	private JLabel lblAngle;

	private TileMapDialog tileMapDialog;
	private JLabel lblOpacity;
	private JSpinner spinnerOpacity;

	private Side currentSide;

	private PathPanel sidewalkPanel;
	private PathPanel roadPanel;

	private FacePanel[] facePanels;
	private JButton btnClear;
	private JPanel selectionPanel;
	private JButton btnClone;
	private JPanel panel;
	private JCheckBox chckbxForceDraw;
	private JButton btnMin;
	private JButton btnMax;

	@SuppressWarnings("serial")
	public BlockPanel(Presenter presenter) {

		this.presenter = presenter;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		blockPanel = new JPanel();
		GridBagConstraints gbc_blockPanel = new GridBagConstraints();
		gbc_blockPanel.fill = GridBagConstraints.BOTH;
		gbc_blockPanel.insets = new Insets(0, 0, 5, 0);
		gbc_blockPanel.gridx = 0;
		gbc_blockPanel.gridy = 0;
		add(blockPanel, gbc_blockPanel);
		blockPanel.setBorder(new TitledBorder(null, "Block", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_blockPanel = new GridBagLayout();
		gbl_blockPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_blockPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_blockPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_blockPanel.rowWeights = new double[] { 0.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
		blockPanel.setLayout(gbl_blockPanel);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		blockPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowHeights = new int[] { 15, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0 };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		lblGeometry = new JLabel("Geometry:");
		GridBagConstraints gbc_lblGeometry = new GridBagConstraints();
		gbc_lblGeometry.insets = new Insets(0, 0, 5, 5);
		gbc_lblGeometry.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblGeometry.gridx = 0;
		gbc_lblGeometry.gridy = 0;
		panel.add(lblGeometry, gbc_lblGeometry);

		spinnerGeometryType = new JSpinner();
		GridBagConstraints gbc_spinnerGeometryType = new GridBagConstraints();
		gbc_spinnerGeometryType.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerGeometryType.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerGeometryType.gridx = 1;
		gbc_spinnerGeometryType.gridy = 0;
		panel.add(spinnerGeometryType, gbc_spinnerGeometryType);
		spinnerGeometryType.setModel(new SpinnerNumberModel(0, 0, ChunkData.MAX_GEOMETRY, 1));

		chckbxForceDraw = new JCheckBox("Force Draw");
		chckbxForceDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEnabled() == false || ((Component) e.getSource()).hasFocus() == false)
					return;
				applyChanges(7);
			}
		});
		GridBagConstraints gbc_chckbxForceDraw = new GridBagConstraints();
		gbc_chckbxForceDraw.gridwidth = 2;
		gbc_chckbxForceDraw.anchor = GridBagConstraints.WEST;
		gbc_chckbxForceDraw.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxForceDraw.gridx = 2;
		gbc_chckbxForceDraw.gridy = 0;
		panel.add(chckbxForceDraw, gbc_chckbxForceDraw);

		lblOpacity = new JLabel("Opacity:");
		lblOpacity.setVisible(false);
		GridBagConstraints gbc_lblOpacity = new GridBagConstraints();
		gbc_lblOpacity.anchor = GridBagConstraints.WEST;
		gbc_lblOpacity.insets = new Insets(0, 0, 0, 5);
		gbc_lblOpacity.gridx = 0;
		gbc_lblOpacity.gridy = 1;
		panel.add(lblOpacity, gbc_lblOpacity);

		spinnerOpacity = new JSpinner();
		spinnerOpacity.setVisible(false);
		GridBagConstraints gbc_spinnerOpacity = new GridBagConstraints();
		gbc_spinnerOpacity.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerOpacity.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerOpacity.gridx = 1;
		gbc_spinnerOpacity.gridy = 1;
		panel.add(spinnerOpacity, gbc_spinnerOpacity);
		spinnerOpacity.setModel(new SpinnerNumberModel(1, 1, ChunkData.MAX_VALUE_4_BITS, 1));

		btnMin = new JButton("min");
		btnMin.setVisible(false);
		btnMin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				spinnerOpacity.setValue(1);
				applyChanges(2);

			}
		});
		GridBagConstraints gbc_btnMin = new GridBagConstraints();
		gbc_btnMin.anchor = GridBagConstraints.WEST;
		gbc_btnMin.insets = new Insets(0, 0, 0, 5);
		gbc_btnMin.gridx = 2;
		gbc_btnMin.gridy = 1;
		panel.add(btnMin, gbc_btnMin);

		btnMax = new JButton("max");
		btnMax.setVisible(false);
		btnMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerOpacity.setValue(ChunkData.MAX_VALUE_4_BITS);
				applyChanges(2);
			}
		});
		GridBagConstraints gbc_btnMax = new GridBagConstraints();
		gbc_btnMax.anchor = GridBagConstraints.WEST;
		gbc_btnMax.gridx = 3;
		gbc_btnMax.gridy = 1;
		panel.add(btnMax, gbc_btnMax);
		spinnerOpacity.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isEnabled() == false || SwingUtils.hasFocus(spinnerOpacity) == false)
					return;
				applyChanges(2);
			}
		});
		spinnerGeometryType.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isEnabled() == false || SwingUtils.hasFocus(spinnerGeometryType) == false)
					return;
				applyChanges(1);
			}
		});

		selectionPanel = new JPanel();
		selectionPanel
				.setBorder(new TitledBorder(null, "Selection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_selectionPanel = new GridBagConstraints();
		gbc_selectionPanel.insets = new Insets(0, 0, 5, 5);
		gbc_selectionPanel.fill = GridBagConstraints.BOTH;
		gbc_selectionPanel.gridx = 0;
		gbc_selectionPanel.gridy = 1;
		blockPanel.add(selectionPanel, gbc_selectionPanel);
		GridBagLayout gbl_selectionPanel = new GridBagLayout();
		gbl_selectionPanel.columnWidths = new int[] { 0, 0 };
		gbl_selectionPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_selectionPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_selectionPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		selectionPanel.setLayout(gbl_selectionPanel);

		btnClear = new JButton("Clear");
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 0;
		selectionPanel.add(btnClear, gbc_btnClear);

		btnClone = new JButton("Clone");
		btnClone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				computeClone();
			}

		});
		GridBagConstraints gbc_btnClone = new GridBagConstraints();
		gbc_btnClone.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnClone.gridx = 0;
		gbc_btnClone.gridy = 1;
		selectionPanel.add(btnClone, gbc_btnClone);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				computeClear();
			}
		});

		backPanel = new FacePanel(Side.BACK) {
			@Override
			public void onRemove() {
				computeRemove(backPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(backPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(backPanel);
			}
		};
		GridBagConstraints gbc_backPanel = new GridBagConstraints();
		gbc_backPanel.fill = GridBagConstraints.BOTH;
		gbc_backPanel.insets = new Insets(0, 0, 5, 5);
		gbc_backPanel.gridx = 1;
		gbc_backPanel.gridy = 1;
		blockPanel.add(backPanel, gbc_backPanel);

		bottomPanel = new FacePanel(Side.BOTTOM) {
			@Override
			public void onRemove() {
				computeRemove(bottomPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(bottomPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(bottomPanel);
			}
		};
		GridBagConstraints gbc_bottomPanel = new GridBagConstraints();
		gbc_bottomPanel.fill = GridBagConstraints.BOTH;
		gbc_bottomPanel.insets = new Insets(0, 0, 5, 0);
		gbc_bottomPanel.gridx = 2;
		gbc_bottomPanel.gridy = 1;
		blockPanel.add(bottomPanel, gbc_bottomPanel);

		leftPanel = new FacePanel(Side.LEFT) {
			@Override
			public void onRemove() {
				computeRemove(leftPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(leftPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(leftPanel);
			}
		};
		GridBagConstraints gbc_leftPanel = new GridBagConstraints();
		gbc_leftPanel.fill = GridBagConstraints.BOTH;
		gbc_leftPanel.insets = new Insets(0, 0, 5, 5);
		gbc_leftPanel.gridx = 0;
		gbc_leftPanel.gridy = 2;
		blockPanel.add(leftPanel, gbc_leftPanel);

		topPanel = new FacePanel(Side.TOP) {
			@Override
			public void onRemove() {
				computeRemove(topPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(topPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(topPanel);
			}
		};
		GridBagConstraints gbc_topPanel = new GridBagConstraints();
		gbc_topPanel.fill = GridBagConstraints.BOTH;
		gbc_topPanel.insets = new Insets(0, 0, 5, 5);
		gbc_topPanel.gridx = 1;
		gbc_topPanel.gridy = 2;
		blockPanel.add(topPanel, gbc_topPanel);

		rightPanel = new FacePanel(Side.RIGHT) {
			@Override
			public void onRemove() {
				computeRemove(rightPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(rightPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(rightPanel);
			}
		};
		GridBagConstraints gbc_rightPanel = new GridBagConstraints();
		gbc_rightPanel.fill = GridBagConstraints.BOTH;
		gbc_rightPanel.insets = new Insets(0, 0, 5, 0);
		gbc_rightPanel.gridx = 2;
		gbc_rightPanel.gridy = 2;
		blockPanel.add(rightPanel, gbc_rightPanel);

		sidewalkPanel = new PathPanel("Sidewalk") {
			@Override
			public void onSelection(int direction) {
				applyChanges(4, direction);
			}
		};

		GridBagConstraints gbc_sidewalkPanel = new GridBagConstraints();
		gbc_sidewalkPanel.insets = new Insets(0, 0, 0, 5);
		gbc_sidewalkPanel.fill = GridBagConstraints.BOTH;
		gbc_sidewalkPanel.gridx = 0;
		gbc_sidewalkPanel.gridy = 3;
		blockPanel.add(sidewalkPanel, gbc_sidewalkPanel);

		frontPanel = new FacePanel(Side.FRONT) {
			@Override
			public void onRemove() {
				computeRemove(frontPanel);
			}

			@Override
			public void onSelection() {
				computeSelection(frontPanel);
			}

			@Override
			public void onDoubleClick() {
				computeDoubleClick(frontPanel);
			}
		};
		GridBagConstraints gbc_frontPanel = new GridBagConstraints();
		gbc_frontPanel.fill = GridBagConstraints.BOTH;
		gbc_frontPanel.insets = new Insets(0, 0, 0, 5);
		gbc_frontPanel.gridx = 1;
		gbc_frontPanel.gridy = 3;
		blockPanel.add(frontPanel, gbc_frontPanel);

		facePanel = new JPanel();
		facePanel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Face", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));
		GridBagConstraints gbc_facePanel = new GridBagConstraints();
		gbc_facePanel.fill = GridBagConstraints.BOTH;
		gbc_facePanel.gridx = 0;
		gbc_facePanel.gridy = 1;
		add(facePanel, gbc_facePanel);
		GridBagLayout gbl_facePanel = new GridBagLayout();
		gbl_facePanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_facePanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_facePanel.columnWeights = new double[] { 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gbl_facePanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		facePanel.setLayout(gbl_facePanel);

		lblIndex = new JLabel("Index:");
		GridBagConstraints gbc_lblIndex = new GridBagConstraints();
		gbc_lblIndex.anchor = GridBagConstraints.WEST;
		gbc_lblIndex.insets = new Insets(0, 0, 5, 5);
		gbc_lblIndex.gridx = 0;
		gbc_lblIndex.gridy = 0;
		facePanel.add(lblIndex, gbc_lblIndex);

		spinnerTextureIndex = new JSpinner();
		spinnerTextureIndex.setModel(new SpinnerNumberModel(0, 0, ChunkData.MAX_VALUE_8_BITS, 1));
		spinnerTextureIndex.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isEnabled() == false || currentSide == null || SwingUtils.hasFocus(spinnerTextureIndex) == false)
					return;
				setFaceIndex(currentSide.ordinal(), (int) spinnerTextureIndex.getValue());
				applyChanges(3, 1, currentSide.ordinal());
			}
		});
		GridBagConstraints gbc_spinnerTileIndex = new GridBagConstraints();
		gbc_spinnerTileIndex.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTileIndex.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerTileIndex.gridx = 1;
		gbc_spinnerTileIndex.gridy = 0;
		facePanel.add(spinnerTextureIndex, gbc_spinnerTileIndex);

		chckbxFlipHorizontal = new JCheckBox("Flip Horizontal");
		chckbxFlipHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEnabled() == false || currentSide == null || ((Component) e.getSource()).hasFocus() == false)
					return;
				setFaceFlippedHorizontally(currentSide.ordinal(), chckbxFlipHorizontal.isSelected());
				applyChanges(3, 3, currentSide.ordinal());
			}
		});
		GridBagConstraints gbc_chckbxFlipHorizontal = new GridBagConstraints();
		gbc_chckbxFlipHorizontal.anchor = GridBagConstraints.WEST;
		gbc_chckbxFlipHorizontal.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxFlipHorizontal.gridx = 2;
		gbc_chckbxFlipHorizontal.gridy = 0;
		facePanel.add(chckbxFlipHorizontal, gbc_chckbxFlipHorizontal);

		lblAngle = new JLabel("Angle:");
		GridBagConstraints gbc_lblAngle = new GridBagConstraints();
		gbc_lblAngle.insets = new Insets(0, 0, 5, 5);
		gbc_lblAngle.anchor = GridBagConstraints.WEST;
		gbc_lblAngle.gridx = 0;
		gbc_lblAngle.gridy = 1;
		facePanel.add(lblAngle, gbc_lblAngle);

		spinnerFaceAngle = new JSpinner();
		spinnerFaceAngle.setModel(new SpinnerNumberModel(0, 0, ChunkData.MAX_VALUE_2_BITS, 1));
		spinnerFaceAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (isEnabled() == false || currentSide == null || SwingUtils.hasFocus(spinnerFaceAngle) == false)
					return;
				setFaceAngle(currentSide.ordinal(), (int) spinnerFaceAngle.getValue());
				applyChanges(3, 2, currentSide.ordinal());
			}
		});
		GridBagConstraints gbc_spinnerFaceAngle = new GridBagConstraints();
		gbc_spinnerFaceAngle.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerFaceAngle.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerFaceAngle.gridx = 1;
		gbc_spinnerFaceAngle.gridy = 1;
		facePanel.add(spinnerFaceAngle, gbc_spinnerFaceAngle);

		chckbxFlipVertical = new JCheckBox("Flip Vertical");
		chckbxFlipVertical.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEnabled() == false || currentSide == null || ((Component) e.getSource()).hasFocus() == false)
					return;
				setFaceFlippedVertically(currentSide.ordinal(), chckbxFlipVertical.isSelected());
				applyChanges(3, 4, currentSide.ordinal());
			}
		});
		GridBagConstraints gbc_chckbxFlipVertical = new GridBagConstraints();
		gbc_chckbxFlipVertical.anchor = GridBagConstraints.WEST;
		gbc_chckbxFlipVertical.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxFlipVertical.gridx = 2;
		gbc_chckbxFlipVertical.gridy = 1;
		facePanel.add(chckbxFlipVertical, gbc_chckbxFlipVertical);

		chckbxBlending = new JCheckBox("Blending");
		chckbxBlending.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEnabled() == false || currentSide == null || ((Component) e.getSource()).hasFocus() == false)
					return;
				setFaceBlending(currentSide.ordinal(), chckbxBlending.isSelected());
				applyChanges(3, 5, currentSide.ordinal());
			}
		});
		GridBagConstraints gbc_chckbxBlending = new GridBagConstraints();
		gbc_chckbxBlending.gridwidth = 3;
		gbc_chckbxBlending.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxBlending.anchor = GridBagConstraints.WEST;
		gbc_chckbxBlending.gridx = 0;
		gbc_chckbxBlending.gridy = 2;
		facePanel.add(chckbxBlending, gbc_chckbxBlending);

		roadPanel = new PathPanel("Road") {
			@Override
			public void onSelection(int direction) {
				applyChanges(5, direction);
			}
		};
		GridBagConstraints gbc_roadPanel = new GridBagConstraints();
		gbc_roadPanel.fill = GridBagConstraints.BOTH;
		gbc_roadPanel.gridx = 2;
		gbc_roadPanel.gridy = 3;
		blockPanel.add(roadPanel, gbc_roadPanel);

		facePanels = new FacePanel[] { topPanel, bottomPanel, frontPanel, backPanel, leftPanel, rightPanel };

	}

	private void computeClone() {
		applyChanges(6);
	}

	private void computeClear() {
		setGeometryType(0);
		setOpacity(0);
		for (int i = 0; i < 4; ++i) {
			setSidewalkSelected(i, false);
			setRoadSelected(i, false);
		}
		for (int i = 0; i < 6; ++i) {
			setFaceEnabled(i, false);
			setFaceIndex(i, 0);
			setFaceFlippedHorizontally(i, false);
			setFaceFlippedVertically(i, false);
			setFaceAngle(i, 0);
			setFaceBlending(i, false);

		}
		applyChanges(0);
	}

	private void computeRemove(FacePanel facePanel) {
		setFaceEnabled(facePanel.getSide().ordinal(), false);
		applyChanges(3, 0, facePanel.getSide().ordinal());
	}

	private void applyChanges(int opcode, Object... args) {

		if (currentSide != null) {
			FacePanel facePanel = facePanels[currentSide.ordinal()];
			FaceData faceData = facePanel.getFaceData();
			setFacePanelEnabled(faceData.isEnabled());
			setPathEnabled(faceData.isEnabled());
		}

		presenter.getEditor().updateSelection(opcode, args); // actualiza los bloques seleccionados

	}

	private void computeSelection(FacePanel facePanel) {
		currentSide = facePanel.getSide();
		// Update selection
		for (Side side : Side.values()) {
			facePanels[side.ordinal()].setSelected(side == currentSide);
		}
		presenter.getEditor().updateBlockPanel();

	}

	private void computeDoubleClick(FacePanel facePanel) {

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				tileMapDialog.setLastSelection(facePanel.getFaceData().getIndex());
				tileMapDialog.setVisible(true);
				int result = tileMapDialog.getResult();
				if (result > -1 /* && result != currentIndex */) {
					facePanel.getFaceData().setEnabled(true);
					facePanel.getFaceData().setIndex(result);
					facePanel.setBufferedImage(tileMapDialog.getBufferedImage(result));
					applyChanges(3, 1, currentSide.ordinal());
				}

				Gdx.app.postRunnable(new Runnable() { // sync
					@Override
					public void run() {
						computeSelection(facePanel);
					}
				});
			}
		};
		
		SwingUtilities.invokeLater(runnable);

	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		setBlockPanelEnabled(enabled);
		setFacePanelEnabled(enabled && currentSide != null);
		setPathEnabled(enabled);
	}

	private void setPathEnabled(boolean enabled) {
		enabled = enabled && currentSide == Side.TOP;
		sidewalkPanel.setEnabled(enabled);
		roadPanel.setEnabled(enabled);
	}

	private void setBlockPanelEnabled(boolean enabled) {

		selectionPanel.setEnabled(enabled);

		btnClear.setEnabled(enabled);
		btnClone.setEnabled(enabled);

		blockPanel.setEnabled(enabled);

		for (FacePanel facePanel : facePanels) {
			facePanel.setEnabled(enabled);
		}

		lblGeometry.setEnabled(enabled);
		lblOpacity.setEnabled(enabled);
		spinnerGeometryType.setEnabled(enabled);
		spinnerOpacity.setEnabled(enabled);
		btnMin.setEnabled(enabled);
		btnMax.setEnabled(enabled);
		chckbxForceDraw.setEnabled(enabled);

		sidewalkPanel.setEnabled(enabled);
		roadPanel.setEnabled(enabled);
	}

	public void setTileMap(TiledTexture tiledTexture) {
		tileMapDialog = new TileMapDialog(presenter.getView().getJFrame(), tiledTexture);
	}

	public void setFacePanelEnabled(boolean enabled) {
		facePanel.setEnabled(enabled);
		spinnerTextureIndex.setEnabled(enabled);
		spinnerFaceAngle.setEnabled(enabled);
		chckbxBlending.setEnabled(enabled);
		chckbxFlipHorizontal.setEnabled(enabled);
		chckbxFlipVertical.setEnabled(enabled);
		lblIndex.setEnabled(enabled);
		lblAngle.setEnabled(enabled);
	}

	public void setGeometryType(int type) {
		spinnerGeometryType.setValue(type);
	}

	public int getGeometryType() {
		return (int) spinnerGeometryType.getValue();
	}

	public void setForceDraw(boolean forceDraw) {
		chckbxForceDraw.setSelected(forceDraw);
	}

	public boolean isForceDraw() {
		return chckbxForceDraw.isSelected();
	}

	public void setOpacity(int opacity) {
		spinnerOpacity.setValue(opacity);
	}

	public int getOpacity() {
		return (int) spinnerOpacity.getValue();
	}

	public void setFaceIndex(int face, int index) {
		facePanels[face].setBufferedImage(tileMapDialog.getBufferedImage(index));
		facePanels[face].getFaceData().setIndex(index);
		if (Side.values()[face] == currentSide) {
			spinnerTextureIndex.setValue(index);
		}
	}

	public int getFaceIndex(int face) {
		return facePanels[face].getFaceData().getIndex();
	}

	public void setFaceFlippedHorizontally(int face, boolean flipped) {
		facePanels[face].getFaceData().setFlippedHorizontally(flipped);
		if (Side.values()[face] == currentSide) {
			chckbxFlipHorizontal.setSelected(flipped);
		}
	}

	public boolean isFaceFlippedHorizontally(int face) {
		return facePanels[face].getFaceData().isFlippedHorizontally();
	}

	public void setFaceFlippedVertically(int face, boolean flipped) {
		facePanels[face].getFaceData().setFlippedVertically(flipped);
		if (Side.values()[face] == currentSide) {
			chckbxFlipVertical.setSelected(flipped);
		}
	}

	public boolean isFaceFlippedVertically(int face) {
		return facePanels[face].getFaceData().isFlippedVertically();
	}

	public void setFaceBlending(int face, boolean blending) {
		facePanels[face].getFaceData().setBlending(blending);
		if (Side.values()[face] == currentSide) {
			chckbxBlending.setSelected(blending);
		}
	}

	public boolean isFaceBlending(int face) {
		return facePanels[face].getFaceData().isBlending();
	}

	public void setFaceAngle(int face, int angle) {
		facePanels[face].getFaceData().setAngle(angle);
		if (Side.values()[face] == currentSide) {
			spinnerFaceAngle.setValue(angle);
		}
	}

	public int getFaceAngle(int face) {
		return facePanels[face].getFaceData().getAngle();
	}

	public void setSidewalkSelected(int direction, boolean selected) {
		sidewalkPanel.setDirectionSelected(direction, selected);
	}

	public boolean isSidewalkSelected(int direction) {
		return sidewalkPanel.isDirectionSelected(direction);
	}

	public void setRoadSelected(int direction, boolean selected) {
		roadPanel.setDirectionSelected(direction, selected);
	}

	public boolean isRoadSelected(int direction) {
		return roadPanel.isDirectionSelected(direction);
	}

	public void setFaceEnabled(int face, boolean enabled) {
		facePanels[face].getFaceData().setEnabled(enabled);
		if (Side.values()[face] == currentSide) {
			setPathEnabled(enabled);
			setFacePanelEnabled(enabled);
		}
	}

	public boolean isFaceEnabled(int face) {
		return facePanels[face].getFaceData().isEnabled();
	}

	public boolean hasFaces() {
		for (FacePanel facePanel : facePanels) {
			if (facePanel.getFaceData().isEnabled()) {
				return true;
			}
		}
		return false;
	}

	long lastClick;

	public void setFaceSelection(Side side) {
		panel = facePanels[side.ordinal()];
		if (System.currentTimeMillis() - lastClick < Constants.DOUBLE_CLICK_TIME) {
			facePanels[side.ordinal()].onDoubleClick();
		} else {
			lastClick = System.currentTimeMillis();
			facePanels[side.ordinal()].onSelection();
		}

	}

}
