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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

import com.delmesoft.editor3d.editor.tool.Tool.Mode;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.MyChangeListener;

import com.badlogic.gdx.math.Vector3;

public class ZonePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel lblName;
	private JTextField textFieldZoneName;	

	private JPanel panelTransform;

	private JPanel panelPosition;
	private JLabel lblX;
	private JSpinner spinnerPositionX;
	private JLabel lblY;
	private JSpinner spinnerPositionY;
	private JLabel lblZ;
	private JSpinner spinnerPositionZ;

	private JPanel panelDimension;
	private JLabel lblWidth;
	private JSpinner spinnerWidth;
	private JLabel lblHeight;
	private JSpinner spinnerHeight;
	private JLabel lblDepth;
	private JSpinner spinnerDepth;
	private JButton buttonS1;
	private JButton buttonS2;
	private JPanel toolPanel;
	private JButton btnRemove;
	private JToggleButton btnSelect;
	private JToggleButton btnAdd;

	private Presenter presenter;
	private JLabel lblParams;
	private JTextField textFieldParams;

	protected boolean lock;
	
	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		btnAdd.setEnabled(b);
		btnSelect.setEnabled(b);
		if(b == false) {
			setZoneEnabled(b);
		}
	}
	
	public void setZoneEnabled(boolean enabled) {
		enabled &= isEnabled();
		btnRemove.setEnabled(enabled);
		lblName.setEnabled(enabled);
		textFieldZoneName.setEnabled(enabled);
		lblParams.setEnabled(enabled);
		textFieldParams.setEnabled(enabled);
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
		lblDepth.setEnabled(enabled);
		spinnerWidth.setEnabled(enabled);
		spinnerHeight.setEnabled(enabled);
		spinnerDepth.setEnabled(enabled);
		
		buttonS1.setEnabled(enabled);
		buttonS2.setEnabled(enabled);
	}

	/**
	 * Create the panel.
	 * @param presenter 
	 */
	public ZonePanel(Presenter presenter) {
		
		this.presenter = presenter;
		
		setBorder(new TitledBorder(null, "Zone Editor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel panelText = new JPanel();
		GridBagConstraints gbc_panelText = new GridBagConstraints();
		gbc_panelText.insets = new Insets(0, 0, 5, 0);
		gbc_panelText.fill = GridBagConstraints.BOTH;
		gbc_panelText.gridx = 0;
		gbc_panelText.gridy = 0;
		add(panelText, gbc_panelText);
		GridBagLayout gbl_panelText = new GridBagLayout();
		gbl_panelText.columnWidths = new int[] { 0, 0, 0 };
		gbl_panelText.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelText.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panelText.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelText.setLayout(gbl_panelText);

		lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.anchor = GridBagConstraints.WEST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		panelText.add(lblName, gbc_lblName);
		
		textFieldZoneName = new JTextField();
		textFieldZoneName.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				return presenter.getEditor().setZoneName(((JTextField) input).getText());
			}
		});
		GridBagConstraints gbc_textFieldZoneName = new GridBagConstraints();
		gbc_textFieldZoneName.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldZoneName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldZoneName.gridx = 1;
		gbc_textFieldZoneName.gridy = 0;
		panelText.add(textFieldZoneName, gbc_textFieldZoneName);
		textFieldZoneName.setColumns(10);
		
		lblParams = new JLabel("Params:");
		GridBagConstraints gbc_lblParams = new GridBagConstraints();
		gbc_lblParams.insets = new Insets(0, 0, 0, 5);
		gbc_lblParams.gridx = 0;
		gbc_lblParams.gridy = 1;
		panelText.add(lblParams, gbc_lblParams);
		
		textFieldParams = new JTextField();
		textFieldParams.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				return presenter.getEditor().setZoneParams(((JTextField) input).getText());
			}
		});
		GridBagConstraints gbc_textFieldParams = new GridBagConstraints();
		gbc_textFieldParams.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldParams.gridx = 1;
		gbc_textFieldParams.gridy = 1;
		panelText.add(textFieldParams, gbc_textFieldParams);
		textFieldParams.setColumns(10);
		
		panelTransform = new JPanel();
		panelTransform.setBorder(new TitledBorder(null, "Transform", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panelTransform = new GridBagConstraints();
		gbc_panelTransform.insets = new Insets(0, 0, 5, 0);
		gbc_panelTransform.fill = GridBagConstraints.BOTH;
		gbc_panelTransform.gridx = 0;
		gbc_panelTransform.gridy = 1;
		add(panelTransform, gbc_panelTransform);
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
		GridBagLayout gbl_panelPosition = new GridBagLayout();
		gbl_panelPosition.columnWidths = new int[]{0, 0, 0};
		gbl_panelPosition.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelPosition.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panelPosition.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelPosition.setLayout(gbl_panelPosition);
		
		lblX = new JLabel("x:");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.gridx = 0;
		gbc_lblX.gridy = 0;
		panelPosition.add(lblX, gbc_lblX);
		
		spinnerPositionX = new JSpinner();
		spinnerPositionX.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPositionX.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionX = new GridBagConstraints();
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
		spinnerPositionY.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPositionY.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionY = new GridBagConstraints();
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
		spinnerPositionZ.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPositionZ.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPositionZ = new GridBagConstraints();
		gbc_spinnerPositionZ.gridx = 1;
		gbc_spinnerPositionZ.gridy = 2;
		panelPosition.add(spinnerPositionZ, gbc_spinnerPositionZ);
		
		panelDimension = new JPanel();
		panelDimension.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Dimension", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		GridBagConstraints gbc_panelDimension = new GridBagConstraints();
		gbc_panelDimension.fill = GridBagConstraints.BOTH;
		gbc_panelDimension.gridx = 0;
		gbc_panelDimension.gridy = 1;
		panelTransform.add(panelDimension, gbc_panelDimension);
		GridBagLayout gbl_panelDimension = new GridBagLayout();
		gbl_panelDimension.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panelDimension.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panelDimension.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelDimension.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panelDimension.setLayout(gbl_panelDimension);
		
		lblWidth = new JLabel("Width:");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.anchor = GridBagConstraints.WEST;
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 0;
		gbc_lblWidth.gridy = 0;
		panelDimension.add(lblWidth, gbc_lblWidth);
		
		spinnerWidth = new JSpinner();
		spinnerWidth.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerWidth.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerWidth = new GridBagConstraints();
		gbc_spinnerWidth.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerWidth.gridx = 1;
		gbc_spinnerWidth.gridy = 0;
		panelDimension.add(spinnerWidth, gbc_spinnerWidth);
		
		JPanel panelButtons = new JPanel();
		GridBagConstraints gbc_panelButtons = new GridBagConstraints();
		gbc_panelButtons.gridheight = 3;
		gbc_panelButtons.insets = new Insets(0, 0, 5, 0);
		gbc_panelButtons.fill = GridBagConstraints.BOTH;
		gbc_panelButtons.gridx = 2;
		gbc_panelButtons.gridy = 0;
		panelDimension.add(panelButtons, gbc_panelButtons);
		GridBagLayout gbl_panelButtons = new GridBagLayout();
		gbl_panelButtons.columnWidths = new int[]{0, 0};
		gbl_panelButtons.rowHeights = new int[]{0, 0, 0};
		gbl_panelButtons.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelButtons.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panelButtons.setLayout(gbl_panelButtons);
		
		buttonS1 = new JButton("+");
		buttonS1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lock = true;
				spinnerWidth .setValue((float) spinnerWidth.getValue()  + 0.1F);
				spinnerHeight.setValue((float) spinnerHeight.getValue() + 0.1F);
				spinnerDepth .setValue((float) spinnerDepth.getValue()  + 0.1F);
				lock = false;
				computeTransform();
			}
		});
		buttonS1.setFont(new Font("Dialog", Font.BOLD, 16));
		GridBagConstraints gbc_buttonS1 = new GridBagConstraints();
		gbc_buttonS1.fill = GridBagConstraints.BOTH;
		gbc_buttonS1.insets = new Insets(0, 0, 5, 0);
		gbc_buttonS1.gridx = 0;
		gbc_buttonS1.gridy = 0;
		panelButtons.add(buttonS1, gbc_buttonS1);
		
		buttonS2 = new JButton("-");
		buttonS2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float min = Math.min((float) spinnerWidth.getValue(), (float) spinnerHeight.getValue());
				min = Math.min((float) spinnerDepth.getValue(), min);
				if (min - 0.1F > 0) {
					lock = true;
					spinnerWidth .setValue((float) spinnerWidth.getValue()  - 0.1F);
					spinnerHeight.setValue((float) spinnerHeight.getValue() - 0.1F);
					spinnerDepth .setValue((float) spinnerDepth.getValue()  - 0.1F);
					lock = false;
					computeTransform();
				}
			}
		});
		buttonS2.setFont(new Font("Dialog", Font.BOLD, 16));
		GridBagConstraints gbc_buttonS2 = new GridBagConstraints();
		gbc_buttonS2.fill = GridBagConstraints.BOTH;
		gbc_buttonS2.gridx = 0;
		gbc_buttonS2.gridy = 1;
		panelButtons.add(buttonS2, gbc_buttonS2);
		
		lblHeight = new JLabel("Height:");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.anchor = GridBagConstraints.WEST;
		gbc_lblHeight.insets = new Insets(0, 0, 5, 5);
		gbc_lblHeight.gridx = 0;
		gbc_lblHeight.gridy = 1;
		panelDimension.add(lblHeight, gbc_lblHeight);
		
		spinnerHeight = new JSpinner();
		spinnerHeight.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerHeight.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerHeight = new GridBagConstraints();
		gbc_spinnerHeight.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerHeight.gridx = 1;
		gbc_spinnerHeight.gridy = 1;
		panelDimension.add(spinnerHeight, gbc_spinnerHeight);
		
		lblDepth = new JLabel("Depth:");
		GridBagConstraints gbc_lblDepth = new GridBagConstraints();
		gbc_lblDepth.insets = new Insets(0, 0, 0, 5);
		gbc_lblDepth.anchor = GridBagConstraints.WEST;
		gbc_lblDepth.gridx = 0;
		gbc_lblDepth.gridy = 2;
		panelDimension.add(lblDepth, gbc_lblDepth);
		
		spinnerDepth = new JSpinner();
		spinnerDepth.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerDepth.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(Integer.MIN_VALUE), new Float(Integer.MAX_VALUE), new Float(0.1F)));
		GridBagConstraints gbc_spinnerDepth = new GridBagConstraints();
		gbc_spinnerDepth.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerDepth.gridx = 1;
		gbc_spinnerDepth.gridy = 2;
		panelDimension.add(spinnerDepth, gbc_spinnerDepth);
		
		toolPanel = new JPanel();
		GridBagConstraints gbc_toolPanel = new GridBagConstraints();
		gbc_toolPanel.fill = GridBagConstraints.BOTH;
		gbc_toolPanel.gridx = 0;
		gbc_toolPanel.gridy = 2;
		add(toolPanel, gbc_toolPanel);
		GridBagLayout gbl_toolPanel = new GridBagLayout();
		gbl_toolPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_toolPanel.rowHeights = new int[]{0, 0};
		gbl_toolPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_toolPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		toolPanel.setLayout(gbl_toolPanel);
		
		btnRemove = new JButton("");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().removeSelectedZones();
			}
		});
		btnRemove.setIcon(new ImageIcon(ZonePanel.class.getResource("/icons/eraser.png")));
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.EAST;
		gbc_btnRemove.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemove.gridx = 0;
		gbc_btnRemove.gridy = 0;
		toolPanel.add(btnRemove, gbc_btnRemove);
		
		ButtonGroup group = new ButtonGroup();
		
		btnSelect = new JToggleButton("");
		btnSelect.setSelected(true);
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getZoneTool().setMode(Mode.SELECT);
			}
		});
		group.add(btnSelect);
		btnSelect.setIcon(new ImageIcon(ZonePanel.class.getResource("/icons/select.png")));
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 0;
		toolPanel.add(btnSelect, gbc_btnSelect);
		
		btnAdd = new JToggleButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getZoneTool().setMode(Mode.ADD);
			}
		});
		group.add(btnAdd);
		btnAdd.setIcon(new ImageIcon(ZonePanel.class.getResource("/icons/brush.png")));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 0;
		toolPanel.add(btnAdd, gbc_btnAdd);

	}

	private void computeTransform() {		
		float positionX = (float) spinnerPositionX.getValue();
		float positionY = (float) spinnerPositionY.getValue();
		float positionZ = (float) spinnerPositionZ.getValue();
		float width     = (float) spinnerWidth.getValue();
		float height    = (float) spinnerHeight.getValue();
		float depht     = (float) spinnerDepth.getValue();		
		presenter.getEditor().setZoneTransform(positionX, positionY, positionZ, width, height, depht);
	}
	
	public void setZoneName(String name) {
		textFieldZoneName.setText(name);
	}
	
	public void setZoneParams(String params) {
		textFieldParams.setText(params);
	}

	public void setZonePosition(Vector3 position) {
		lock = true;
		spinnerPositionX.setValue(position.x);
		spinnerPositionY.setValue(position.y);
		spinnerPositionZ.setValue(position.z);
		lock = false;
	}

	public void setZoneDimensions(Vector3 dimensions) {
		lock = true;
		spinnerWidth.setValue(dimensions.x);
		spinnerHeight.setValue(dimensions.y);
		spinnerDepth.setValue(dimensions.z);
		lock = false;
	}

}
