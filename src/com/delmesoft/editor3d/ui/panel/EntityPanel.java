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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.delmesoft.editor3d.editor.tool.EntityTool.FileDescriptor;
import com.delmesoft.editor3d.editor.tool.Tool.Mode;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.SwingUtils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class EntityPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Presenter presenter;

	private JPanel panelModels;
	private JList<FileDescriptor> modelList;
	private DefaultListModel<FileDescriptor> model;	
	private JButton btnAddModel, btnRemoveModel;
	private JToggleButton btnAddEntity, btnSelectEntity; 
	private JButton btnRemoveEntity; // To level

	private JPanel panelEntity;
	private JLabel lblEntityName;
	private JTextField textFieldEntityName;
	
	private JPanel panelTransform;
	
	private JPanel panelPosition;
	private JLabel lblPositionX, lblPositionY, lblPositionZ;
	private JSpinner spinnerPositionX, spinnerPositionY, spinnerPositionZ;

	private JPanel panelRotation;
	private JLabel lblRotationX, lblRotationY, lblRotationZ;
	private JSpinner spinnerRotationX, spinnerRotationY, spinnerRotationZ;

	private JPanel panelScale;
	private JLabel lblScaleX, lblScaleY, lblScaleZ;
	private JSpinner spinnerScaleX, spinnerScaleY, spinnerScaleZ;
	
	private JPanel panelAnimations;
	private JLabel lblAnimationId;
	private JComboBox<String> comboBoxAnimations;
	private JPanel panel;
	private JButton buttonS1;
	private JButton buttonS2;
	private JPanel entityToolPanel;
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		panelModels.setEnabled(enabled);
		modelList.setEnabled(enabled);
		btnAddModel.setEnabled(enabled);
		
		if(enabled == false) {
			entityToolPanel.setEnabled(enabled);
			btnRemoveModel.setEnabled(enabled);			
			btnAddEntity.setEnabled(enabled);
			btnSelectEntity.setEnabled(enabled);
			setEntityEnabled(false);			
		}
		
	}
	
	public void setEntityEnabled(boolean enabled) {
		enabled &= isEnabled();
		
		panelEntity.setEnabled(enabled);
		btnRemoveEntity.setEnabled(enabled);		
		lblEntityName.setEnabled(enabled);
		textFieldEntityName.setEnabled(enabled);
		panelTransform.setEnabled(enabled);
		panelPosition.setEnabled(enabled);
		lblPositionX.setEnabled(enabled);
		lblPositionY.setEnabled(enabled);
		lblPositionZ.setEnabled(enabled);
		spinnerPositionX.setEnabled(enabled);
		spinnerPositionY.setEnabled(enabled);
		spinnerPositionZ.setEnabled(enabled);
		panelScale.setEnabled(enabled);
		lblScaleX.setEnabled(enabled);
		lblScaleY.setEnabled(enabled);
		lblScaleZ.setEnabled(enabled);
		spinnerScaleX.setEnabled(enabled);
		spinnerScaleY.setEnabled(enabled);
		spinnerScaleZ.setEnabled(enabled);
		buttonS1.setEnabled(enabled);
		buttonS2.setEnabled(enabled);
		panelRotation.setEnabled(enabled);
		lblRotationX.setEnabled(enabled);
		lblRotationY.setEnabled(enabled);
		lblRotationZ.setEnabled(enabled);
		spinnerRotationX.setEnabled(enabled);
		spinnerRotationY.setEnabled(enabled);
		spinnerRotationZ.setEnabled(enabled);
		panelAnimations.setEnabled(enabled);
		lblAnimationId.setEnabled(enabled);
		comboBoxAnimations.setEnabled(enabled);
		
	}
		
	public EntityPanel(Presenter presenter) {
		
		this.presenter = presenter;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		panelModels = new JPanel();
		panelModels.setBorder(new TitledBorder(null, "Models", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelModels = new GridBagConstraints();
		gbc_panelModels.insets = new Insets(0, 0, 5, 0);
		gbc_panelModels.fill = GridBagConstraints.BOTH;
		gbc_panelModels.gridx = 0;
		gbc_panelModels.gridy = 0;
		add(panelModels, gbc_panelModels);
		GridBagLayout gbl_panelModels = new GridBagLayout();
		gbl_panelModels.columnWidths = new int[]{0, 0, 0};
		gbl_panelModels.rowHeights = new int[]{0, 0, 0};
		gbl_panelModels.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelModels.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panelModels.setLayout(gbl_panelModels);
		
		modelList = new JList<>();
		model = new DefaultListModel<>();
		modelList.setModel(model);
		modelList.setBorder(new TitledBorder(null, "List", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_modelList = new GridBagConstraints();
		gbc_modelList.gridwidth = 2;
		gbc_modelList.insets = new Insets(0, 0, 5, 0);
		gbc_modelList.fill = GridBagConstraints.BOTH;
		gbc_modelList.gridx = 0;
		gbc_modelList.gridy = 0;
		panelModels.add(modelList, gbc_modelList);
		
		btnAddModel = new JButton("Add");
		btnAddModel.setIcon(new ImageIcon(EntityPanel.class.getResource("/icons/add_image.png")));
		btnAddModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().loadModel();
			}
		});
		GridBagConstraints gbc_btnAddModel = new GridBagConstraints();
		gbc_btnAddModel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddModel.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddModel.gridx = 0;
		gbc_btnAddModel.gridy = 1;
		panelModels.add(btnAddModel, gbc_btnAddModel);
		
		btnRemoveModel = new JButton("Remove");
		btnRemoveModel.setIcon(new ImageIcon(EntityPanel.class.getResource("/icons/remove.png")));
		btnRemoveModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean result = presenter.getView().showQuestion("Confirm", "Do you want to continue?");
				if(result) {
					presenter.getEditor().removeModel(modelList.getSelectedValue());
				}
			}
		});
		GridBagConstraints gbc_btnRemoveModel = new GridBagConstraints();
		gbc_btnRemoveModel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveModel.gridx = 1;
		gbc_btnRemoveModel.gridy = 1;
		panelModels.add(btnRemoveModel, gbc_btnRemoveModel);
		
		entityToolPanel = new JPanel();
		entityToolPanel.setBorder(new TitledBorder(null, "Entity Tool", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_entityToolPanel = new GridBagConstraints();
		gbc_entityToolPanel.insets = new Insets(0, 0, 5, 0);
		gbc_entityToolPanel.fill = GridBagConstraints.BOTH;
		gbc_entityToolPanel.gridx = 0;
		gbc_entityToolPanel.gridy = 1;
		add(entityToolPanel, gbc_entityToolPanel);
		GridBagLayout gbl_entityToolPanel = new GridBagLayout();
		gbl_entityToolPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_entityToolPanel.rowHeights = new int[]{0, 0};
		gbl_entityToolPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_entityToolPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		entityToolPanel.setLayout(gbl_entityToolPanel);
		
		btnRemoveEntity = new JButton("");
		GridBagConstraints gbc_btnRemoveEntity = new GridBagConstraints();
		gbc_btnRemoveEntity.anchor = GridBagConstraints.EAST;
		gbc_btnRemoveEntity.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemoveEntity.gridx = 0;
		gbc_btnRemoveEntity.gridy = 0;
		entityToolPanel.add(btnRemoveEntity, gbc_btnRemoveEntity);
		btnRemoveEntity.setIcon(new ImageIcon(EntityPanel.class.getResource("/icons/eraser.png")));
		
		ButtonGroup group = new ButtonGroup();
		
		btnSelectEntity = new JToggleButton("");
		btnSelectEntity.setSelected(true);
		btnSelectEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getEntityTool().setMode(Mode.SELECT);
			}
		});
		group.add(btnSelectEntity);
		btnSelectEntity.setIcon(new ImageIcon(EntityPanel.class.getResource("/icons/select.png")));
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 0;
		entityToolPanel.add(btnSelectEntity, gbc_btnSelect);
		
		btnAddEntity = new JToggleButton("");
		GridBagConstraints gbc_btnAddEntity = new GridBagConstraints();
		gbc_btnAddEntity.gridx = 2;
		gbc_btnAddEntity.gridy = 0;
		entityToolPanel.add(btnAddEntity, gbc_btnAddEntity);
		btnAddEntity.setIcon(new ImageIcon(EntityPanel.class.getResource("/icons/brush.png")));
		btnAddEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getEntityTool().setMode(Mode.ADD);
			}
		});
		group.add(btnAddEntity);
		btnRemoveEntity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().removeSelectedEntities();
			}
		});
		
		panelEntity = new JPanel();
		panelEntity.setBorder(new TitledBorder(null, "Entity", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelEntity = new GridBagConstraints();
		gbc_panelEntity.fill = GridBagConstraints.BOTH;
		gbc_panelEntity.gridx = 0;
		gbc_panelEntity.gridy = 2;
		add(panelEntity, gbc_panelEntity);
		GridBagLayout gbl_panelEntity = new GridBagLayout();
		gbl_panelEntity.columnWidths = new int[]{0, 0, 0};
		gbl_panelEntity.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelEntity.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelEntity.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelEntity.setLayout(gbl_panelEntity);
		
		lblEntityName = new JLabel("Name:");
		GridBagConstraints gbc_lblEntityName = new GridBagConstraints();
		gbc_lblEntityName.insets = new Insets(0, 0, 5, 5);
		gbc_lblEntityName.gridx = 0;
		gbc_lblEntityName.gridy = 0;
		panelEntity.add(lblEntityName, gbc_lblEntityName);
		
		textFieldEntityName = new JTextField();
		textFieldEntityName.setInputVerifier(new MyInputVerifier());
		GridBagConstraints gbc_textFieldEntityId = new GridBagConstraints();
		gbc_textFieldEntityId.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldEntityId.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldEntityId.gridx = 1;
		gbc_textFieldEntityId.gridy = 0;
		panelEntity.add(textFieldEntityName, gbc_textFieldEntityId);
		textFieldEntityName.setColumns(10);
		
		panelTransform = new JPanel();
		GridBagConstraints gbc_panelTransform = new GridBagConstraints();
		gbc_panelTransform.insets = new Insets(0, 0, 5, 0);
		gbc_panelTransform.fill = GridBagConstraints.BOTH;
		gbc_panelTransform.gridwidth = 2;
		gbc_panelTransform.gridx = 0;
		gbc_panelTransform.gridy = 1;
		panelEntity.add(panelTransform, gbc_panelTransform);
		panelTransform.setBorder(new TitledBorder(null, "Transform", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelTransform = new GridBagLayout();
		gbl_panelTransform.columnWidths = new int[]{0, 0};
		gbl_panelTransform.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelTransform.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelTransform.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelTransform.setLayout(gbl_panelTransform);
		
		panelPosition = new JPanel();
		panelPosition.setBorder(new TitledBorder(null, "Position", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelPosition = new GridBagConstraints();
		gbc_panelPosition.insets = new Insets(0, 0, 5, 0);
		gbc_panelPosition.fill = GridBagConstraints.BOTH;
		gbc_panelPosition.gridx = 0;
		gbc_panelPosition.gridy = 0;
		panelTransform.add(panelPosition, gbc_panelPosition);
		GridBagLayout gbl_panelPosition = new GridBagLayout();
		gbl_panelPosition.columnWidths = new int[]{0, 0, 0};
		gbl_panelPosition.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelPosition.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelPosition.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelPosition.setLayout(gbl_panelPosition);
		
		lblPositionX = new JLabel("x:");
		GridBagConstraints gbc_lblPositionX = new GridBagConstraints();
		gbc_lblPositionX.anchor = GridBagConstraints.WEST;
		gbc_lblPositionX.insets = new Insets(0, 0, 5, 5);
		gbc_lblPositionX.gridx = 0;
		gbc_lblPositionX.gridy = 0;
		panelPosition.add(lblPositionX, gbc_lblPositionX);
		
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
		gbc_spinnerPositionX.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPositionX.gridx = 1;
		gbc_spinnerPositionX.gridy = 0;
		panelPosition.add(spinnerPositionX, gbc_spinnerPositionX);
		
		lblPositionY = new JLabel("y:");
		GridBagConstraints gbc_lblPositionY = new GridBagConstraints();
		gbc_lblPositionY.anchor = GridBagConstraints.WEST;
		gbc_lblPositionY.insets = new Insets(0, 0, 5, 5);
		gbc_lblPositionY.gridx = 0;
		gbc_lblPositionY.gridy = 1;
		panelPosition.add(lblPositionY, gbc_lblPositionY);
		
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
		gbc_spinnerPositionY.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPositionY.gridx = 1;
		gbc_spinnerPositionY.gridy = 1;
		panelPosition.add(spinnerPositionY, gbc_spinnerPositionY);
		
		lblPositionZ = new JLabel("z:");
		GridBagConstraints gbc_lblPositionZ = new GridBagConstraints();
		gbc_lblPositionZ.anchor = GridBagConstraints.WEST;
		gbc_lblPositionZ.insets = new Insets(0, 0, 0, 5);
		gbc_lblPositionZ.gridx = 0;
		gbc_lblPositionZ.gridy = 2;
		panelPosition.add(lblPositionZ, gbc_lblPositionZ);
		
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
		gbc_spinnerPositionZ.gridx = 1;
		gbc_spinnerPositionZ.gridy = 2;
		panelPosition.add(spinnerPositionZ, gbc_spinnerPositionZ);
		
		panelRotation = new JPanel();
		panelRotation.setBorder(new TitledBorder(null, "Rotation", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelRotation = new GridBagConstraints();
		gbc_panelRotation.insets = new Insets(0, 0, 5, 0);
		gbc_panelRotation.fill = GridBagConstraints.BOTH;
		gbc_panelRotation.gridx = 0;
		gbc_panelRotation.gridy = 1;
		panelTransform.add(panelRotation, gbc_panelRotation);
		GridBagLayout gbl_panelRotation = new GridBagLayout();
		gbl_panelRotation.columnWidths = new int[]{0, 0, 0};
		gbl_panelRotation.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelRotation.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelRotation.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelRotation.setLayout(gbl_panelRotation);
		
		lblRotationX = new JLabel("x:");
		GridBagConstraints gbc_lblRotationX = new GridBagConstraints();
		gbc_lblRotationX.anchor = GridBagConstraints.WEST;
		gbc_lblRotationX.insets = new Insets(0, 0, 5, 5);
		gbc_lblRotationX.gridx = 0;
		gbc_lblRotationX.gridy = 0;
		panelRotation.add(lblRotationX, gbc_lblRotationX);
		
		spinnerRotationX = new JSpinner();
		spinnerRotationX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerRotationX)) {
					computeTransform();
				}
			}
		});
		spinnerRotationX.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerRotationX = new GridBagConstraints();
		gbc_spinnerRotationX.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerRotationX.gridx = 1;
		gbc_spinnerRotationX.gridy = 0;
		panelRotation.add(spinnerRotationX, gbc_spinnerRotationX);
		
		lblRotationY = new JLabel("y:");
		GridBagConstraints gbc_lblRotationY = new GridBagConstraints();
		gbc_lblRotationY.anchor = GridBagConstraints.WEST;
		gbc_lblRotationY.insets = new Insets(0, 0, 5, 5);
		gbc_lblRotationY.gridx = 0;
		gbc_lblRotationY.gridy = 1;
		panelRotation.add(lblRotationY, gbc_lblRotationY);
		
		spinnerRotationY = new JSpinner();
		spinnerRotationY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerRotationY)) {
					computeTransform();
				}
			}
		});
		spinnerRotationY.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerRotationY = new GridBagConstraints();
		gbc_spinnerRotationY.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerRotationY.gridx = 1;
		gbc_spinnerRotationY.gridy = 1;
		panelRotation.add(spinnerRotationY, gbc_spinnerRotationY);
		
		lblRotationZ = new JLabel("z:");
		GridBagConstraints gbc_lblRotationZ = new GridBagConstraints();
		gbc_lblRotationZ.anchor = GridBagConstraints.WEST;
		gbc_lblRotationZ.insets = new Insets(0, 0, 0, 5);
		gbc_lblRotationZ.gridx = 0;
		gbc_lblRotationZ.gridy = 2;
		panelRotation.add(lblRotationZ, gbc_lblRotationZ);
		
		spinnerRotationZ = new JSpinner();
		spinnerRotationZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerRotationZ)) {
					computeTransform();
				}
			}
		});
		spinnerRotationZ.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerRotationZ = new GridBagConstraints();
		gbc_spinnerRotationZ.gridx = 1;
		gbc_spinnerRotationZ.gridy = 2;
		panelRotation.add(spinnerRotationZ, gbc_spinnerRotationZ);
		
		panelScale = new JPanel();
		panelScale.setBorder(new TitledBorder(null, "Scale", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelScale = new GridBagConstraints();
		gbc_panelScale.fill = GridBagConstraints.BOTH;
		gbc_panelScale.gridx = 0;
		gbc_panelScale.gridy = 2;
		panelTransform.add(panelScale, gbc_panelScale);
		GridBagLayout gbl_panelScale = new GridBagLayout();
		gbl_panelScale.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelScale.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelScale.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelScale.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panelScale.setLayout(gbl_panelScale);
		
		lblScaleX = new JLabel("x:");
		GridBagConstraints gbc_lblScaleX = new GridBagConstraints();
		gbc_lblScaleX.anchor = GridBagConstraints.WEST;
		gbc_lblScaleX.insets = new Insets(0, 0, 5, 5);
		gbc_lblScaleX.gridx = 0;
		gbc_lblScaleX.gridy = 0;
		panelScale.add(lblScaleX, gbc_lblScaleX);
		
		spinnerScaleX = new JSpinner();
		spinnerScaleX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerScaleX)) {
					computeTransform();
				}
			}
		});
		spinnerScaleX.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerScaleX = new GridBagConstraints();
		gbc_spinnerScaleX.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerScaleX.gridx = 1;
		gbc_spinnerScaleX.gridy = 0;
		panelScale.add(spinnerScaleX, gbc_spinnerScaleX);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 2;
		gbc_panel.gridy = 0;
		panelScale.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		buttonS1 = new JButton("+");
		buttonS1.setFont(new Font("Dialog", Font.BOLD, 16));
		buttonS1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerScaleX.setValue((float) spinnerScaleX.getValue() + 0.1F);
				spinnerScaleY.setValue((float) spinnerScaleY.getValue() + 0.1F);
				spinnerScaleZ.setValue((float) spinnerScaleZ.getValue() + 0.1F);
				computeTransform();
			}
		});
		GridBagConstraints gbc_buttonS1 = new GridBagConstraints();
		gbc_buttonS1.fill = GridBagConstraints.BOTH;
		gbc_buttonS1.insets = new Insets(0, 0, 5, 0);
		gbc_buttonS1.gridx = 0;
		gbc_buttonS1.gridy = 0;
		panel.add(buttonS1, gbc_buttonS1);
		
		buttonS2 = new JButton("-");
		buttonS2.setFont(new Font("Dialog", Font.BOLD, 16));
		buttonS2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spinnerScaleX.setValue((float) spinnerScaleX.getValue() - 0.1F);
				spinnerScaleY.setValue((float) spinnerScaleY.getValue() - 0.1F);
				spinnerScaleZ.setValue((float) spinnerScaleZ.getValue() - 0.1F);
				computeTransform(); 
			}
		});
		GridBagConstraints gbc_buttonS2 = new GridBagConstraints();
		gbc_buttonS2.fill = GridBagConstraints.BOTH;
		gbc_buttonS2.gridx = 0;
		gbc_buttonS2.gridy = 1;
		panel.add(buttonS2, gbc_buttonS2);
		
		lblScaleY = new JLabel("y:");
		GridBagConstraints gbc_lblScaleY = new GridBagConstraints();
		gbc_lblScaleY.anchor = GridBagConstraints.WEST;
		gbc_lblScaleY.insets = new Insets(0, 0, 5, 5);
		gbc_lblScaleY.gridx = 0;
		gbc_lblScaleY.gridy = 1;
		panelScale.add(lblScaleY, gbc_lblScaleY);
		
		spinnerScaleY = new JSpinner();
		spinnerScaleY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerScaleY)) {
					computeTransform();
				}
			}
		});
		spinnerScaleY.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerScaleY = new GridBagConstraints();
		gbc_spinnerScaleY.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerScaleY.gridx = 1;
		gbc_spinnerScaleY.gridy = 1;
		panelScale.add(spinnerScaleY, gbc_spinnerScaleY);
		
		lblScaleZ = new JLabel("z:");
		GridBagConstraints gbc_lblScaleZ = new GridBagConstraints();
		gbc_lblScaleZ.anchor = GridBagConstraints.WEST;
		gbc_lblScaleZ.insets = new Insets(0, 0, 0, 5);
		gbc_lblScaleZ.gridx = 0;
		gbc_lblScaleZ.gridy = 2;
		panelScale.add(lblScaleZ, gbc_lblScaleZ);
		
		spinnerScaleZ = new JSpinner();
		spinnerScaleZ.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(SwingUtils.hasFocus(spinnerScaleZ)) {
					computeTransform();
				}
			}
		});
		spinnerScaleZ.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerScaleZ = new GridBagConstraints();
		gbc_spinnerScaleZ.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerScaleZ.gridx = 1;
		gbc_spinnerScaleZ.gridy = 2;
		panelScale.add(spinnerScaleZ, gbc_spinnerScaleZ);
		
		panelAnimations = new JPanel();
		GridBagConstraints gbc_panelAnimations = new GridBagConstraints();
		gbc_panelAnimations.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelAnimations.gridwidth = 2;
		gbc_panelAnimations.insets = new Insets(0, 0, 0, 5);
		gbc_panelAnimations.gridx = 0;
		gbc_panelAnimations.gridy = 2;
		panelEntity.add(panelAnimations, gbc_panelAnimations);
		panelAnimations.setBorder(new TitledBorder(null, "Animations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelAnimations = new GridBagLayout();
		gbl_panelAnimations.columnWidths = new int[]{0, 0, 0};
		gbl_panelAnimations.rowHeights = new int[]{0, 0};
		gbl_panelAnimations.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panelAnimations.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelAnimations.setLayout(gbl_panelAnimations);
		
		lblAnimationId = new JLabel("Id:");
		GridBagConstraints gbc_lblAnimationId = new GridBagConstraints();
		gbc_lblAnimationId.insets = new Insets(0, 0, 0, 5);
		gbc_lblAnimationId.anchor = GridBagConstraints.EAST;
		gbc_lblAnimationId.gridx = 0;
		gbc_lblAnimationId.gridy = 0;
		panelAnimations.add(lblAnimationId, gbc_lblAnimationId);
		
		comboBoxAnimations = new JComboBox<>();
		comboBoxAnimations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxAnimations.hasFocus()) {
					presenter.getEditor().setAnimation(String.valueOf(comboBoxAnimations.getSelectedItem()));
				}
			}
		});
		GridBagConstraints gbc_comboBoxAnimations = new GridBagConstraints();
		gbc_comboBoxAnimations.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxAnimations.gridx = 1;
		gbc_comboBoxAnimations.gridy = 0;
		panelAnimations.add(comboBoxAnimations, gbc_comboBoxAnimations);
	}

	private void computeTransform() {
		
		float positionX = (float) spinnerPositionX.getValue();
		float positionY = (float) spinnerPositionY.getValue();
		float positionZ = (float) spinnerPositionZ.getValue();
		float rotationX = (float) spinnerRotationX.getValue();
		float rotationY = (float) spinnerRotationY.getValue();
		float rotationZ = (float) spinnerRotationZ.getValue();
		float scaleX = (float) spinnerScaleX.getValue();
		float scaleY = (float) spinnerScaleY.getValue();
		float scaleZ = (float) spinnerScaleZ.getValue();
		
		presenter.getEditor().setEntityTransform(positionX, positionY, positionZ, rotationX, rotationY, rotationZ, scaleX, scaleY, scaleZ);
	}

	public void setModelList(Array<FileDescriptor> fileDescriptors) {
		FileDescriptor value = modelList.getSelectedValue();
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
			modelList.setSelectedIndex(index);
			entityToolPanel.setEnabled(true);
			btnRemoveModel.setEnabled(true);
			btnAddEntity.setEnabled(true);
			btnSelectEntity.setEnabled(true);
		} else {
			entityToolPanel.setEnabled(false);
			btnRemoveModel.setEnabled(false);
			btnAddEntity.setEnabled(false);
			btnSelectEntity.setEnabled(false);
		}

	}

	public void setPosition(Vector3 position) {
		spinnerPositionX.setValue(position.x);
		spinnerPositionY.setValue(position.y);
		spinnerPositionZ.setValue(position.z);
	}

	public void setScale(Vector3 scale) {
		spinnerScaleX.setValue(scale.x);
		spinnerScaleY.setValue(scale.y);
		spinnerScaleZ.setValue(scale.z);
	}

	public void setRotation(Vector3 rotation) {
		spinnerRotationX.setValue(rotation.x);
		spinnerRotationY.setValue(rotation.y);
		spinnerRotationZ.setValue(rotation.z);
	}

	public void setAnimations(List<String> animations) {
		comboBoxAnimations.removeAllItems();
		for (String animation : animations) {
			comboBoxAnimations.addItem(animation);
		}
	}

	public FileDescriptor getFileDescriptor() {
		return modelList.getSelectedValue();
	}

	public void setEntityName(String name) {
		textFieldEntityName.setText(name);
	}

	public Mode getSelectedMode() {
		return btnSelectEntity.isSelected() ? Mode.SELECT : Mode.ADD;
	}
	
	private class MyInputVerifier extends InputVerifier {
		@Override
		public boolean verify(JComponent input) {
			String text = ((JTextField) input).getText();
			return presenter.getEditor().setEntityName(text);
		}
	}
	
}
