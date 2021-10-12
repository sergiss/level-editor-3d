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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.delmesoft.editor3d.editor.tool.EntityTool.FileDescriptor;
import com.delmesoft.editor3d.editor.tool.Tool.Mode;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.SwingUtils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class DecalPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Presenter presenter;
	
	private JList<FileDescriptor> testureAtlasList;
	private DefaultListModel<FileDescriptor> model;	
	private JButton btnAddTextureAtlas;
	private JButton btnRemoveTextureAtlas;
	private JButton btnRemoveDecal;
	private JToggleButton btnAddDecal;
	private JToggleButton btnSelectDecal;
	private JTextField textFieldDecalName;
	private JSpinner spinnerPositionX;
	private JSpinner spinnerPositionY;
	private JSpinner spinnerPositionZ;
	private JSpinner spinnerWidth;
	private JButton buttonS1;
	private JSpinner spinnerHeight;
	private JButton buttonS2;

	private JPanel panelTextureAtlas;
	private JPanel panelDecal;
	private JLabel lblName;
	private JPanel panelTransform;
	private JPanel panelPosition;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZ;
	private JPanel panelDimension;
	private JLabel lblWidth;
	private JLabel lblHeight;
	private JPanel panelDecalTool;
	
	private JLabel lblRegion;
	private JTextField textFieldRegionName;
	private JButton buttonSelectRegion;
	private JPanel panel_1;
	private JPanel panel_2;
	private Component horizontalStrut;
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		panelTextureAtlas.setEnabled(enabled);
		testureAtlasList.setEnabled(enabled);
		btnAddTextureAtlas.setEnabled(enabled);
		
		if(enabled == false) {
			panelDecalTool.setEnabled(false);
			btnRemoveTextureAtlas.setEnabled(false);			
			btnAddDecal.setEnabled(false);
			btnSelectDecal.setEnabled(false);
			lblRegion.setEnabled(false);
			textFieldRegionName.setEnabled(false);
			textFieldRegionName.setText("");
			buttonSelectRegion.setEnabled(false);
			setDecalEnabled(false);
		}
		
	}
	
	public void setDecalEnabled(boolean enabled) {
		enabled &= isEnabled();
		panelDecal.setEnabled(enabled);
		btnRemoveDecal.setEnabled(enabled);
		lblName.setEnabled(enabled);
		textFieldDecalName.setEnabled(enabled);
		panelTransform.setEnabled(enabled);
		panelPosition.setEnabled(enabled);
		lblX.setEnabled(enabled);
		lblY.setEnabled(enabled);
		lblZ.setEnabled(enabled);
		spinnerPositionX.setEnabled(enabled);
		spinnerPositionY.setEnabled(enabled);
		spinnerPositionZ.setEnabled(enabled);
		panelDimension.setEnabled(enabled);
		lblWidth.setEnabled(enabled);
		lblHeight.setEnabled(enabled);
		spinnerWidth.setEnabled(enabled);
		spinnerHeight.setEnabled(enabled);
		
		buttonS1.setEnabled(enabled);
		buttonS2.setEnabled(enabled);
				
	}
	
	public DecalPanel(Presenter presenter) {
		
		this.presenter = presenter;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
				
		panelTextureAtlas = new JPanel();
		panelTextureAtlas.setBorder(new TitledBorder(null, "Texture Atlas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelTextureAtlas = new GridBagConstraints();
		gbc_panelTextureAtlas.insets = new Insets(0, 0, 5, 0);
		gbc_panelTextureAtlas.fill = GridBagConstraints.BOTH;
		gbc_panelTextureAtlas.gridx = 0;
		gbc_panelTextureAtlas.gridy = 0;
		add(panelTextureAtlas, gbc_panelTextureAtlas);
		GridBagLayout gbl_panelTextureAtlas = new GridBagLayout();
		gbl_panelTextureAtlas.columnWidths = new int[]{0, 0, 0};
		gbl_panelTextureAtlas.rowHeights = new int[]{0, 0, 0};
		gbl_panelTextureAtlas.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelTextureAtlas.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panelTextureAtlas.setLayout(gbl_panelTextureAtlas);
		
		testureAtlasList = new JList<>();
		model = new DefaultListModel<>();
		testureAtlasList.setModel(model);
		testureAtlasList.setBorder(new TitledBorder(null, "List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_listTestureAtlas = new GridBagConstraints();
		gbc_listTestureAtlas.gridwidth = 2;
		gbc_listTestureAtlas.insets = new Insets(0, 0, 5, 0);
		gbc_listTestureAtlas.fill = GridBagConstraints.BOTH;
		gbc_listTestureAtlas.gridx = 0;
		gbc_listTestureAtlas.gridy = 0;
		panelTextureAtlas.add(testureAtlasList, gbc_listTestureAtlas);
		
		btnAddTextureAtlas = new JButton("Add");
		btnAddTextureAtlas.setIcon(new ImageIcon(DecalPanel.class.getResource("/icons/add_image.png")));
		btnAddTextureAtlas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().loadTextureAtlas();
			}
		});
		GridBagConstraints gbc_btnAddTextureAtlas = new GridBagConstraints();
		gbc_btnAddTextureAtlas.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddTextureAtlas.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTextureAtlas.gridx = 0;
		gbc_btnAddTextureAtlas.gridy = 1;
		panelTextureAtlas.add(btnAddTextureAtlas, gbc_btnAddTextureAtlas);
		
		btnRemoveTextureAtlas = new JButton("Remove");
		btnRemoveTextureAtlas.setIcon(new ImageIcon(DecalPanel.class.getResource("/icons/remove.png")));
		btnRemoveTextureAtlas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean result = presenter.getView().showQuestion("Confirm", "Do you want to continue?");
				if(result) {
					presenter.getEditor().removeTextureAtlas(testureAtlasList.getSelectedValue());
				}
			}
		});
		GridBagConstraints gbc_btnRemoveTextureAtlas = new GridBagConstraints();
		gbc_btnRemoveTextureAtlas.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveTextureAtlas.gridx = 1;
		gbc_btnRemoveTextureAtlas.gridy = 1;
		panelTextureAtlas.add(btnRemoveTextureAtlas, gbc_btnRemoveTextureAtlas);
		
		panelDecalTool = new JPanel();
		panelDecalTool.setBorder(new TitledBorder(null, "Decal Tool", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelDecalTool = new GridBagConstraints();
		gbc_panelDecalTool.insets = new Insets(0, 0, 5, 0);
		gbc_panelDecalTool.fill = GridBagConstraints.BOTH;
		gbc_panelDecalTool.gridx = 0;
		gbc_panelDecalTool.gridy = 1;
		add(panelDecalTool, gbc_panelDecalTool);
		GridBagLayout gbl_panelDecalTool = new GridBagLayout();
		gbl_panelDecalTool.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelDecalTool.rowHeights = new int[]{0, 0};
		gbl_panelDecalTool.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelDecalTool.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelDecalTool.setLayout(gbl_panelDecalTool);
		
		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panelDecalTool.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		lblRegion = new JLabel("Region:");
		GridBagConstraints gbc_lblRegion = new GridBagConstraints();
		gbc_lblRegion.anchor = GridBagConstraints.WEST;
		gbc_lblRegion.insets = new Insets(0, 0, 0, 5);
		gbc_lblRegion.gridx = 0;
		gbc_lblRegion.gridy = 0;
		panel_2.add(lblRegion, gbc_lblRegion);
		
		textFieldRegionName = new JTextField();
		textFieldRegionName.setEditable(false);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_2.add(textFieldRegionName, gbc_textField);
		textFieldRegionName.setColumns(10);
		
		buttonSelectRegion = new JButton("...");
		buttonSelectRegion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileDescriptor fileDescriptor = getFileDescriptor();		
				TextureAtlas textureAtlas = FileManager.getInstance().get(fileDescriptor.path, TextureAtlas.class);	
				String regionName = presenter.getEditor().openAtlasRegionSelector(textureAtlas);
				if(regionName != null) {
					textFieldRegionName.setText(regionName);
				}
			}
		});
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridx = 2;
		gbc_button.gridy = 0;
		panel_2.add(buttonSelectRegion, gbc_button);
		
		horizontalStrut = Box.createHorizontalStrut(10);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.fill = GridBagConstraints.BOTH;
		gbc_horizontalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_horizontalStrut.gridx = 1;
		gbc_horizontalStrut.gridy = 0;
		panelDecalTool.add(horizontalStrut, gbc_horizontalStrut);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 0;
		panelDecalTool.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnRemoveDecal = new JButton("");
		GridBagConstraints gbc_btnRemoveDecal = new GridBagConstraints();
		gbc_btnRemoveDecal.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveDecal.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemoveDecal.gridx = 0;
		gbc_btnRemoveDecal.gridy = 0;
		panel_1.add(btnRemoveDecal, gbc_btnRemoveDecal);
		btnRemoveDecal.setIcon(new ImageIcon(DecalPanel.class.getResource("/icons/eraser.png")));
		
		ButtonGroup group = new ButtonGroup();
		
		btnSelectDecal = new JToggleButton("");
		btnSelectDecal.setSelected(true);
		btnSelectDecal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getDecalTool().setMode(Mode.SELECT);
			}
		});
		group.add(btnSelectDecal);
		btnSelectDecal.setIcon(new ImageIcon(DecalPanel.class.getResource("/icons/select.png")));
		GridBagConstraints gbc_btnSelection = new GridBagConstraints();
		gbc_btnSelection.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSelection.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelection.gridx = 1;
		gbc_btnSelection.gridy = 0;
		panel_1.add(btnSelectDecal, gbc_btnSelection);
		
		btnAddDecal = new JToggleButton("");
		GridBagConstraints gbc_btnAddDecal = new GridBagConstraints();
		gbc_btnAddDecal.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddDecal.gridx = 2;
		gbc_btnAddDecal.gridy = 0;
		panel_1.add(btnAddDecal, gbc_btnAddDecal);
		btnAddDecal.setIcon(new ImageIcon(DecalPanel.class.getResource("/icons/brush.png")));
		btnAddDecal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getDecalTool().setMode(Mode.ADD);
			}
		});
		group.add(btnAddDecal);
		btnRemoveDecal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().removeSelectedDecals();
			}
		});
		
		panelDecal = new JPanel();
		panelDecal.setBorder(new TitledBorder(null, "Decal", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelDecal = new GridBagConstraints();
		gbc_panelDecal.fill = GridBagConstraints.BOTH;
		gbc_panelDecal.gridx = 0;
		gbc_panelDecal.gridy = 2;
		add(panelDecal, gbc_panelDecal);
		GridBagLayout gbl_panelDecal = new GridBagLayout();
		gbl_panelDecal.columnWidths = new int[]{0, 0, 0};
		gbl_panelDecal.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelDecal.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelDecal.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelDecal.setLayout(gbl_panelDecal);
		
		lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		panelDecal.add(lblName, gbc_lblName);
		
		textFieldDecalName = new JTextField();
		textFieldDecalName.setInputVerifier(new MyInputVerifier());
		GridBagConstraints gbc_textFieldDecalName = new GridBagConstraints();
		gbc_textFieldDecalName.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldDecalName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldDecalName.gridx = 1;
		gbc_textFieldDecalName.gridy = 0;
		panelDecal.add(textFieldDecalName, gbc_textFieldDecalName);
		textFieldDecalName.setColumns(10);
		
		panelTransform = new JPanel();
		panelTransform.setBorder(new TitledBorder(null, "Transform", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelTransform = new GridBagConstraints();
		gbc_panelTransform.insets = new Insets(0, 0, 5, 0);
		gbc_panelTransform.gridwidth = 2;
		gbc_panelTransform.fill = GridBagConstraints.BOTH;
		gbc_panelTransform.gridx = 0;
		gbc_panelTransform.gridy = 1;
		panelDecal.add(panelTransform, gbc_panelTransform);
		GridBagLayout gbl_panelTransform = new GridBagLayout();
		gbl_panelTransform.columnWidths = new int[]{0, 0};
		gbl_panelTransform.rowHeights = new int[]{0, 0, 0};
		gbl_panelTransform.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelTransform.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelTransform.setLayout(gbl_panelTransform);
		
		panelPosition = new JPanel();
		panelPosition.setBorder(new TitledBorder(null, "Position", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelPosition = new GridBagConstraints();
		gbc_panelPosition.insets = new Insets(0, 0, 5, 0);
		gbc_panelPosition.fill = GridBagConstraints.BOTH;
		gbc_panelPosition.gridx = 0;
		gbc_panelPosition.gridy = 0;
		panelTransform.add(panelPosition, gbc_panelPosition);
		GridBagLayout gridBagLayout_1 = new GridBagLayout();
		gridBagLayout_1.columnWidths = new int[]{0, 0, 0};
		gridBagLayout_1.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout_1.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelPosition.setLayout(gridBagLayout_1);
		
		lblX = new JLabel("x:");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.gridx = 0;
		gbc_lblX.gridy = 0;
		panelPosition.add(lblX, gbc_lblX);
		
		spinnerPositionX = new JSpinner();
		spinnerPositionX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerPositionX)) {
					computeTransform();
				}
			}
		});
		spinnerPositionX.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionX = new GridBagConstraints();
		gbc_spinnerPositionX.fill = GridBagConstraints.VERTICAL;
		gbc_spinnerPositionX.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPositionX.gridx = 1;
		gbc_spinnerPositionX.gridy = 0;
		panelPosition.add(spinnerPositionX, gbc_spinnerPositionX);
		
		lblY = new JLabel("y:");
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.insets = new Insets(0, 0, 5, 5);
		gbc_lblY.gridx = 0;
		gbc_lblY.gridy = 1;
		panelPosition.add(lblY, gbc_lblY);
		
		spinnerPositionY = new JSpinner();
		spinnerPositionY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerPositionY)) {
					computeTransform();
				}
			}
		});
		spinnerPositionY.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionY = new GridBagConstraints();
		gbc_spinnerPositionY.fill = GridBagConstraints.VERTICAL;
		gbc_spinnerPositionY.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPositionY.gridx = 1;
		gbc_spinnerPositionY.gridy = 1;
		panelPosition.add(spinnerPositionY, gbc_spinnerPositionY);
		
		lblZ = new JLabel("z:");
		GridBagConstraints gbc_lblZ = new GridBagConstraints();
		gbc_lblZ.insets = new Insets(0, 0, 0, 5);
		gbc_lblZ.gridx = 0;
		gbc_lblZ.gridy = 2;
		panelPosition.add(lblZ, gbc_lblZ);
		
		spinnerPositionZ = new JSpinner();
		spinnerPositionZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerPositionZ)) {
					computeTransform();
				}
			}
		});
		spinnerPositionZ.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionZ = new GridBagConstraints();
		gbc_spinnerPositionZ.fill = GridBagConstraints.VERTICAL;
		gbc_spinnerPositionZ.gridx = 1;
		gbc_spinnerPositionZ.gridy = 2;
		panelPosition.add(spinnerPositionZ, gbc_spinnerPositionZ);
		
		panelDimension = new JPanel();
		GridBagConstraints gbc_panelDimension = new GridBagConstraints();
		gbc_panelDimension.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelDimension.gridx = 0;
		gbc_panelDimension.gridy = 1;
		panelTransform.add(panelDimension, gbc_panelDimension);
		panelDimension.setBorder(new TitledBorder(null, "Dimension", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelDimension = new GridBagLayout();
		gbl_panelDimension.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelDimension.rowHeights = new int[]{0, 0, 0};
		gbl_panelDimension.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelDimension.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelDimension.setLayout(gbl_panelDimension);
		
		lblWidth = new JLabel("w:");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.fill = GridBagConstraints.VERTICAL;
		gbc_lblWidth.anchor = GridBagConstraints.WEST;
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 0;
		gbc_lblWidth.gridy = 0;
		panelDimension.add(lblWidth, gbc_lblWidth);
		
		spinnerWidth = new JSpinner();
		spinnerWidth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerWidth)) {
					computeTransform();
				}
			}
		});
		spinnerWidth.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerWidth = new GridBagConstraints();
		gbc_spinnerWidth.fill = GridBagConstraints.VERTICAL;
		gbc_spinnerWidth.anchor = GridBagConstraints.WEST;
		gbc_spinnerWidth.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerWidth.gridx = 1;
		gbc_spinnerWidth.gridy = 0;
		panelDimension.add(spinnerWidth, gbc_spinnerWidth);
		
		buttonS1 = new JButton("+");
		buttonS1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerWidth.setValue((float) spinnerWidth.getValue() + 0.1F);
				spinnerHeight.setValue((float) spinnerHeight.getValue() + 0.1F);
				computeTransform();
			}
		});
		buttonS1.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_buttonS1 = new GridBagConstraints();
		gbc_buttonS1.anchor = GridBagConstraints.NORTH;
		gbc_buttonS1.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonS1.insets = new Insets(0, 0, 5, 0);
		gbc_buttonS1.gridx = 2;
		gbc_buttonS1.gridy = 0;
		panelDimension.add(buttonS1, gbc_buttonS1);
		
		lblHeight = new JLabel("h:");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.fill = GridBagConstraints.VERTICAL;
		gbc_lblHeight.anchor = GridBagConstraints.WEST;
		gbc_lblHeight.insets = new Insets(0, 0, 0, 5);
		gbc_lblHeight.gridx = 0;
		gbc_lblHeight.gridy = 1;
		panelDimension.add(lblHeight, gbc_lblHeight);
		
		spinnerHeight = new JSpinner();
		spinnerHeight.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerHeight)) {
					computeTransform();
				}
			}
		});
		spinnerHeight.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerHeight = new GridBagConstraints();
		gbc_spinnerHeight.fill = GridBagConstraints.VERTICAL;
		gbc_spinnerHeight.anchor = GridBagConstraints.WEST;
		gbc_spinnerHeight.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerHeight.gridx = 1;
		gbc_spinnerHeight.gridy = 1;
		panelDimension.add(spinnerHeight, gbc_spinnerHeight);
		
		buttonS2 = new JButton("-");
		buttonS2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerWidth.setValue((float) spinnerWidth.getValue() - 0.1F);
				spinnerHeight.setValue((float) spinnerHeight.getValue() - 0.1F);
				computeTransform();
			}
		});
		buttonS2.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_buttonS2 = new GridBagConstraints();
		gbc_buttonS2.anchor = GridBagConstraints.SOUTH;
		gbc_buttonS2.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonS2.gridx = 2;
		gbc_buttonS2.gridy = 1;
		panelDimension.add(buttonS2, gbc_buttonS2);
	}
	
	private void computeTransform() {		
		float positionX = (float) spinnerPositionX.getValue();
		float positionY = (float) spinnerPositionY.getValue();
		float positionZ = (float) spinnerPositionZ.getValue();
		float width = (float) spinnerWidth.getValue();
		float height = (float) spinnerHeight.getValue();
		
		presenter.getEditor().setDecalTransform(positionX, positionY, positionZ, width, height);
	}

	public void setTextureAtlasList(Array<FileDescriptor> fileDescriptors) {	
		
		FileDescriptor value = testureAtlasList.getSelectedValue();		
		model.removeAllElements();
		
		if (fileDescriptors.size > 0) {
			int index = 0;
			FileDescriptor fileDescriptor;
			for (int i = 0; i < fileDescriptors.size; ++i) {
				fileDescriptor = fileDescriptors.get(i);
				if (fileDescriptor == value) {
					index = i;
				}
				model.addElement(fileDescriptor);
			}
			testureAtlasList.setSelectedIndex(index);
			btnRemoveTextureAtlas.setEnabled(true);
			btnAddDecal.setEnabled(true);
			btnSelectDecal.setEnabled(true);
			lblRegion.setEnabled(true);
			textFieldRegionName.setEnabled(true);
			buttonSelectRegion.setEnabled(true);
		} else {
			btnRemoveTextureAtlas.setEnabled(false);
			btnAddDecal.setEnabled(false);
			btnSelectDecal.setEnabled(false);
			lblRegion.setEnabled(false);
			textFieldRegionName.setEnabled(false);
			textFieldRegionName.setText("");
			buttonSelectRegion.setEnabled(false);
		}
		
	}

	public void setPosition(Vector3 position) {
		spinnerPositionX.setValue(position.x);
		spinnerPositionY.setValue(position.y);
		spinnerPositionZ.setValue(position.z);
	}

	public void setDimension(float width, float height) {
		spinnerWidth.setValue(width);
		spinnerHeight.setValue(height);
	}

	public FileDescriptor getFileDescriptor() {
		return testureAtlasList.getSelectedValue();
	}

	public void setDecalName(String name) {
		textFieldDecalName.setText(name);
	}

	public String getRegionName() {
		return textFieldRegionName.getText();
	}

	public void setRegionName(String name) {
		textFieldRegionName.setText(name);
	}

	public Mode getSelectedMode() {
		return btnSelectDecal.isSelected() ? Mode.SELECT : Mode.ADD;
	}
	
	private class MyInputVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			String text = ((JTextField) input).getText();
			return presenter.getEditor().setDecalName(text);
		}
	}
	
}
