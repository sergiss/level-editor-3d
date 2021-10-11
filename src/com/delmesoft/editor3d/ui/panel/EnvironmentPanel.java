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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

public class EnvironmentPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Presenter presenter;

	private JPanel ambientLightPanel;

	private JLabel lblR1, lblG1, lblB1;
	
	private JSpinner spinnerAlR, spinnerAlG, spinnerAlB;
	private JToggleButton btnAddPointLight;
	private JToggleButton btnSelectPointLight;
	private JButton btnRemove;
	private JLabel lblR2;
	private JLabel lblG2;
	private JLabel lblB2;
	private JSpinner spinnerPlR;	
	private JSpinner spinnerPlG;	
	private JSpinner spinnerPlB;
	private JLabel lblX;
	private JLabel lblY;
	private JLabel lblZ;
	private JSpinner spinnerPosX;	
	private JSpinner spinnerPosY;	
	private JSpinner spinnerPosZ;
	private JLabel lblIntensity;
	private JSpinner spinnerIntensity;
	private JLabel lblName;
	private JTextField textFieldPointLightName;

	private JPanel pointLightPanel;
	private JPanel colorPanel;
	private JPanel positionPanel;
	private SkyboxPanel skyboxPanel;
	private JPanel panel;
	private JButton btnDecreaseAmbientLight;
	private JButton btnIncreaseAmbientLight;
	private JPanel panel_1;
	private JButton btnDecreasePointLight;
	private JButton btnIncreasePointLight;
	
	private boolean lock;
		
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.btnAddPointLight.setEnabled(enabled);
		this.btnSelectPointLight.setEnabled(enabled);
		this.lblR1.setEnabled(enabled);
		this.lblG1.setEnabled(enabled);
		this.lblB1.setEnabled(enabled);
		this.ambientLightPanel.setEnabled(enabled);
		this.btnDecreaseAmbientLight.setEnabled(enabled);
		this.btnIncreaseAmbientLight.setEnabled(enabled);
		this.spinnerAlR.setEnabled(enabled);
		this.spinnerAlG.setEnabled(enabled);
		this.spinnerAlB.setEnabled(enabled);
				
		skyboxPanel.setEnabled(enabled);
		
		if(enabled == false)
			setPointLigntEnabled(false);
		
	}
	
	public void setPointLigntEnabled(boolean b) {
		b &= isEnabled();
		pointLightPanel.setEnabled(b);
		lblName.setEnabled(b);
		textFieldPointLightName.setEnabled(b);
		lblIntensity.setEnabled(b);
		spinnerIntensity.setEnabled(b);
		colorPanel.setEnabled(b);
		lblR2.setEnabled(b);
		lblG2.setEnabled(b);
		lblB2.setEnabled(b);
		btnDecreasePointLight.setEnabled(b);
		btnIncreasePointLight.setEnabled(b);
		spinnerPlR.setEnabled(b);
		spinnerPlG.setEnabled(b);
		spinnerPlB.setEnabled(b);
		positionPanel.setEnabled(b);
		lblX.setEnabled(b);
		lblY.setEnabled(b);
		lblZ.setEnabled(b);
		spinnerPosX.setEnabled(b);
		spinnerPosY.setEnabled(b);
		spinnerPosZ.setEnabled(b);
		btnRemove.setEnabled(b);
	}
	
	public EnvironmentPanel(Presenter presenter) {
		
		this.presenter = presenter;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
		skyboxPanel = new SkyboxPanel(presenter);
		GridBagConstraints gbc_skyboxPanel = new GridBagConstraints();
		gbc_skyboxPanel.insets = new Insets(0, 0, 5, 0);
		gbc_skyboxPanel.fill = GridBagConstraints.BOTH;
		gbc_skyboxPanel.gridx = 0;
		gbc_skyboxPanel.gridy = 0;
		add(skyboxPanel, gbc_skyboxPanel);
		
		ambientLightPanel = new JPanel();
		ambientLightPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(128, 128, 128), 1, true), "Ambient Light", TitledBorder.LEADING, TitledBorder.TOP, null, new java.awt.Color(51, 51, 51)));
		GridBagConstraints gbc_ambientLightPanel = new GridBagConstraints();
		gbc_ambientLightPanel.insets = new Insets(0, 0, 5, 0);
		gbc_ambientLightPanel.fill = GridBagConstraints.BOTH;
		gbc_ambientLightPanel.gridx = 0;
		gbc_ambientLightPanel.gridy = 1;
		add(ambientLightPanel, gbc_ambientLightPanel);
		GridBagLayout gbl_ambientLightPanel = new GridBagLayout();
		gbl_ambientLightPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_ambientLightPanel.rowHeights = new int[]{0, 0, 0};
		gbl_ambientLightPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_ambientLightPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		ambientLightPanel.setLayout(gbl_ambientLightPanel);
		
		lblR1 = new JLabel("r:");
		GridBagConstraints gbc_lblR1 = new GridBagConstraints();
		gbc_lblR1.insets = new Insets(0, 0, 5, 5);
		gbc_lblR1.gridx = 0;
		gbc_lblR1.gridy = 0;
		ambientLightPanel.add(lblR1, gbc_lblR1);
		
		spinnerAlR = new JSpinner();
		spinnerAlR.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		spinnerAlR.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				applyAmpientLight();	
			}
		});
		GridBagConstraints gbc_spinnerAlR = new GridBagConstraints();
		gbc_spinnerAlR.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerAlR.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerAlR.gridx = 1;
		gbc_spinnerAlR.gridy = 0;
		ambientLightPanel.add(spinnerAlR, gbc_spinnerAlR);
		
		lblG1 = new JLabel("g:");
		GridBagConstraints gbc_lblG1 = new GridBagConstraints();
		gbc_lblG1.insets = new Insets(0, 0, 5, 5);
		gbc_lblG1.gridx = 2;
		gbc_lblG1.gridy = 0;
		ambientLightPanel.add(lblG1, gbc_lblG1);
		
		spinnerAlG = new JSpinner();
		spinnerAlG.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		spinnerAlG.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				applyAmpientLight();	
			}
		});
		GridBagConstraints gbc_spinnerAlG = new GridBagConstraints();
		gbc_spinnerAlG.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerAlG.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerAlG.gridx = 3;
		gbc_spinnerAlG.gridy = 0;
		ambientLightPanel.add(spinnerAlG, gbc_spinnerAlG);
		
		lblB1 = new JLabel("b:");
		GridBagConstraints gbc_lblB1 = new GridBagConstraints();
		gbc_lblB1.insets = new Insets(0, 0, 5, 5);
		gbc_lblB1.gridx = 4;
		gbc_lblB1.gridy = 0;
		ambientLightPanel.add(lblB1, gbc_lblB1);
		
		spinnerAlB = new JSpinner();
		spinnerAlB.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		spinnerAlB.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				applyAmpientLight();
			}
		});
		GridBagConstraints gbc_spinnerAlB = new GridBagConstraints();
		gbc_spinnerAlB.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerAlB.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerAlB.gridx = 5;
		gbc_spinnerAlB.gridy = 0;
		ambientLightPanel.add(spinnerAlB, gbc_spinnerAlB);
		
		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 6;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		ambientLightPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btnDecreaseAmbientLight = new JButton("-");
		btnDecreaseAmbientLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				float minValue = Math.min((float) spinnerAlR.getValue(), (float) spinnerAlG.getValue());
				minValue = Math.min(minValue, (float) spinnerAlB.getValue());
				
				if(minValue - 0.1F >= 0) {
					lock = true;
					spinnerAlR.setValue((float) spinnerAlR.getValue() - 0.1F);
					spinnerAlG.setValue((float) spinnerAlG.getValue() - 0.1F);
					spinnerAlB.setValue((float) spinnerAlB.getValue() - 0.1F);
					lock = false;
					applyAmpientLight();
				}
			}
		});
		GridBagConstraints gbc_btnDecreaseAmbientLight = new GridBagConstraints();
		gbc_btnDecreaseAmbientLight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDecreaseAmbientLight.insets = new Insets(0, 0, 0, 5);
		gbc_btnDecreaseAmbientLight.gridx = 0;
		gbc_btnDecreaseAmbientLight.gridy = 0;
		panel.add(btnDecreaseAmbientLight, gbc_btnDecreaseAmbientLight);
		
		btnIncreaseAmbientLight = new JButton("+");
		btnIncreaseAmbientLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float maxValue = Math.max((float) spinnerAlR.getValue(), (float) spinnerAlG.getValue());
				maxValue = Math.max(maxValue, (float) spinnerAlB.getValue());
				
				if(maxValue + 0.1F <= 1F) {
					lock = true;
					spinnerAlR.setValue((float) spinnerAlR.getValue() + 0.1F);
					spinnerAlG.setValue((float) spinnerAlG.getValue() + 0.1F);
					spinnerAlB.setValue((float) spinnerAlB.getValue() + 0.1F);
					lock = false;
					applyAmpientLight();
				}
			}			
		});
		GridBagConstraints gbc_btnIncreaseAmbientLight = new GridBagConstraints();
		gbc_btnIncreaseAmbientLight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIncreaseAmbientLight.gridx = 1;
		gbc_btnIncreaseAmbientLight.gridy = 0;
		panel.add(btnIncreaseAmbientLight, gbc_btnIncreaseAmbientLight);
		
		pointLightPanel = new JPanel();
		pointLightPanel.setBorder(new TitledBorder(new LineBorder(new java.awt.Color(128, 128, 128), 1, true), "Point Light", TitledBorder.LEADING, TitledBorder.TOP, null, new java.awt.Color(51, 51, 51)));
		GridBagConstraints gbc_pointLightPanel = new GridBagConstraints();
		gbc_pointLightPanel.fill = GridBagConstraints.BOTH;
		gbc_pointLightPanel.gridx = 0;
		gbc_pointLightPanel.gridy = 2;
		add(pointLightPanel, gbc_pointLightPanel);
		GridBagLayout gbl_pointLightPanel = new GridBagLayout();
		gbl_pointLightPanel.columnWidths = new int[]{0, 0};
		gbl_pointLightPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_pointLightPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_pointLightPanel.rowWeights = new double[]{1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		pointLightPanel.setLayout(gbl_pointLightPanel);
		
		JPanel namePanel = new JPanel();
		GridBagConstraints gbc_namePanel = new GridBagConstraints();
		gbc_namePanel.insets = new Insets(0, 0, 5, 0);
		gbc_namePanel.fill = GridBagConstraints.BOTH;
		gbc_namePanel.gridx = 0;
		gbc_namePanel.gridy = 0;
		pointLightPanel.add(namePanel, gbc_namePanel);
		GridBagLayout gbl_namePanel = new GridBagLayout();
		gbl_namePanel.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_namePanel.rowHeights = new int[]{0, 0};
		gbl_namePanel.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_namePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		namePanel.setLayout(gbl_namePanel);
		
		lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 0, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		namePanel.add(lblName, gbc_lblName);
		
		textFieldPointLightName = new JTextField();
		textFieldPointLightName.setInputVerifier(new InputVerifier() {
			@Override
			public boolean verify(JComponent input) {
				return presenter.getEditor().setPointLightName(((JTextField) input).getText());
			}
		});
		GridBagConstraints gbc_textFieldName = new GridBagConstraints();
		gbc_textFieldName.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldName.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldName.gridx = 1;
		gbc_textFieldName.gridy = 0;
		namePanel.add(textFieldPointLightName, gbc_textFieldName);
		textFieldPointLightName.setColumns(10);
		
		lblIntensity = new JLabel("Intensity:");
		GridBagConstraints gbc_lblIntensity = new GridBagConstraints();
		gbc_lblIntensity.insets = new Insets(0, 0, 0, 5);
		gbc_lblIntensity.gridx = 2;
		gbc_lblIntensity.gridy = 0;
		namePanel.add(lblIntensity, gbc_lblIntensity);
		
		spinnerIntensity = new JSpinner();
		spinnerIntensity.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.gridx = 3;
		gbc_spinner.gridy = 0;
		namePanel.add(spinnerIntensity, gbc_spinner);
		spinnerIntensity.setModel(new SpinnerNumberModel(new Float(0), new Float(0), new Float(999), new Float(0.5F)));
		
		colorPanel = new JPanel();
		colorPanel.setBorder(new TitledBorder(null, "Color", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_colorPanel = new GridBagConstraints();
		gbc_colorPanel.insets = new Insets(0, 0, 5, 0);
		gbc_colorPanel.fill = GridBagConstraints.BOTH;
		gbc_colorPanel.gridx = 0;
		gbc_colorPanel.gridy = 1;
		pointLightPanel.add(colorPanel, gbc_colorPanel);
		GridBagLayout gbl_colorPanel = new GridBagLayout();
		gbl_colorPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_colorPanel.rowHeights = new int[]{0, 0, 0};
		gbl_colorPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_colorPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		colorPanel.setLayout(gbl_colorPanel);
		
		lblR2 = new JLabel("r:");
		GridBagConstraints gbc_lblR2 = new GridBagConstraints();
		gbc_lblR2.insets = new Insets(0, 0, 5, 5);
		gbc_lblR2.gridx = 0;
		gbc_lblR2.gridy = 0;
		colorPanel.add(lblR2, gbc_lblR2);
		
		spinnerPlR = new JSpinner();
		spinnerPlR.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPlR.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		GridBagConstraints gbc_spinnerPlR = new GridBagConstraints();
		gbc_spinnerPlR.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlR.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPlR.gridx = 1;
		gbc_spinnerPlR.gridy = 0;
		colorPanel.add(spinnerPlR, gbc_spinnerPlR);
		
		lblG2 = new JLabel("g:");
		GridBagConstraints gbc_lblG2 = new GridBagConstraints();
		gbc_lblG2.insets = new Insets(0, 0, 5, 5);
		gbc_lblG2.gridx = 2;
		gbc_lblG2.gridy = 0;
		colorPanel.add(lblG2, gbc_lblG2);
		
		spinnerPlG = new JSpinner();
		spinnerPlG.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPlG.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		GridBagConstraints gbc_spinnerPlG = new GridBagConstraints();
		gbc_spinnerPlG.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlG.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPlG.gridx = 3;
		gbc_spinnerPlG.gridy = 0;
		colorPanel.add(spinnerPlG, gbc_spinnerPlG);
		
		lblB2 = new JLabel("b:");
		GridBagConstraints gbc_lblB2 = new GridBagConstraints();
		gbc_lblB2.insets = new Insets(0, 0, 5, 5);
		gbc_lblB2.gridx = 4;
		gbc_lblB2.gridy = 0;
		colorPanel.add(lblB2, gbc_lblB2);
		
		spinnerPlB = new JSpinner();
		spinnerPlB.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPlB.setModel(new SpinnerNumberModel(new Float(1), new Float(0), new Float(1), new Float(0.05F)));
		GridBagConstraints gbc_spinnerPlB = new GridBagConstraints();
		gbc_spinnerPlB.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerPlB.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPlB.gridx = 5;
		gbc_spinnerPlB.gridy = 0;
		colorPanel.add(spinnerPlB, gbc_spinnerPlB);
		
		panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 6;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		colorPanel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnDecreasePointLight = new JButton("-");
		btnDecreasePointLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float minValue = Math.min((float) spinnerPlR.getValue(), (float) spinnerPlG.getValue());
				minValue = Math.min(minValue, (float) spinnerPlB.getValue());
				
				if(minValue - 0.1F >= 0F) {
					lock = true;
					spinnerPlR.setValue((float) spinnerPlR.getValue() - 0.1F);
					spinnerPlG.setValue((float) spinnerPlG.getValue() - 0.1F);
					spinnerPlB.setValue((float) spinnerPlB.getValue() - 0.1F);
					lock = false;
					computeTransform();
				}
			}
		});
		GridBagConstraints gbc_btnDecreasePointLight = new GridBagConstraints();
		gbc_btnDecreasePointLight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDecreasePointLight.insets = new Insets(0, 0, 0, 5);
		gbc_btnDecreasePointLight.gridx = 0;
		gbc_btnDecreasePointLight.gridy = 0;
		panel_1.add(btnDecreasePointLight, gbc_btnDecreasePointLight);
		
		btnIncreasePointLight = new JButton("+");
		btnIncreasePointLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float maxValue = Math.max((float) spinnerPlR.getValue(), (float) spinnerPlG.getValue());
				maxValue = Math.max(maxValue, (float) spinnerPlB.getValue());
				
				if(maxValue + 0.1F <= 1F) {
					lock = true;
					spinnerPlR.setValue((float) spinnerPlR.getValue() + 0.1F);
					spinnerPlG.setValue((float) spinnerPlG.getValue() + 0.1F);
					spinnerPlB.setValue((float) spinnerPlB.getValue() + 0.1F);
					lock = false;
					computeTransform();
				}
			}
		});
		GridBagConstraints gbc_btnIncreasePointLight = new GridBagConstraints();
		gbc_btnIncreasePointLight.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnIncreasePointLight.gridx = 1;
		gbc_btnIncreasePointLight.gridy = 0;
		panel_1.add(btnIncreasePointLight, gbc_btnIncreasePointLight);
		
		positionPanel = new JPanel();
		positionPanel.setBorder(new TitledBorder(null, "Position", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_positionPanel = new GridBagConstraints();
		gbc_positionPanel.insets = new Insets(0, 0, 5, 0);
		gbc_positionPanel.fill = GridBagConstraints.BOTH;
		gbc_positionPanel.gridx = 0;
		gbc_positionPanel.gridy = 2;
		pointLightPanel.add(positionPanel, gbc_positionPanel);
		GridBagLayout gbl_positionPanel = new GridBagLayout();
		gbl_positionPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_positionPanel.rowHeights = new int[]{0, 0};
		gbl_positionPanel.columnWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_positionPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		positionPanel.setLayout(gbl_positionPanel);
		
		lblX = new JLabel("x:");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 0, 5);
		gbc_lblX.gridx = 0;
		gbc_lblX.gridy = 0;
		positionPanel.add(lblX, gbc_lblX);
		
		spinnerPosX = new JSpinner();		
		spinnerPosX.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPosX.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(-1000), new Float(1000), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPosX = new GridBagConstraints();
		gbc_spinnerPosX.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPosX.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerPosX.gridx = 1;
		gbc_spinnerPosX.gridy = 0;
		positionPanel.add(spinnerPosX, gbc_spinnerPosX);
		
		lblY = new JLabel("y:");
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.insets = new Insets(0, 0, 0, 5);
		gbc_lblY.gridx = 2;
		gbc_lblY.gridy = 0;
		positionPanel.add(lblY, gbc_lblY);
		
		spinnerPosY = new JSpinner();
		spinnerPosY.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPosY.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(-1000), new Float(1000), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPosY = new GridBagConstraints();
		gbc_spinnerPosY.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPosY.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerPosY.gridx = 3;
		gbc_spinnerPosY.gridy = 0;
		positionPanel.add(spinnerPosY, gbc_spinnerPosY);
		
		lblZ = new JLabel("z:");
		GridBagConstraints gbc_lblZ = new GridBagConstraints();
		gbc_lblZ.insets = new Insets(0, 0, 0, 5);
		gbc_lblZ.gridx = 4;
		gbc_lblZ.gridy = 0;
		positionPanel.add(lblZ, gbc_lblZ);
		
		spinnerPosZ = new JSpinner();
		spinnerPosZ.addChangeListener(new MyChangeListener() {			
			@Override
			public void onChangeEvent(ChangeEvent e) {
				if(isEnabled() == false || lock) return;
				computeTransform();	
			}
		});
		spinnerPosZ.setModel(new SpinnerNumberModel(new Float(0.0F), new Float(-1000), new Float(1000), new Float(0.1F)));
		GridBagConstraints gbc_spinnerPosZ = new GridBagConstraints();
		gbc_spinnerPosZ.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerPosZ.gridx = 5;
		gbc_spinnerPosZ.gridy = 0;
		positionPanel.add(spinnerPosZ, gbc_spinnerPosZ);
		
		JPanel toolPanel = new JPanel();
		GridBagConstraints gbc_toolPanel = new GridBagConstraints();
		gbc_toolPanel.fill = GridBagConstraints.BOTH;
		gbc_toolPanel.gridx = 0;
		gbc_toolPanel.gridy = 3;
		pointLightPanel.add(toolPanel, gbc_toolPanel);
		GridBagLayout gbl_toolPanel = new GridBagLayout();
		gbl_toolPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_toolPanel.rowHeights = new int[]{0, 0};
		gbl_toolPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_toolPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		toolPanel.setLayout(gbl_toolPanel);
		
		btnRemove = new JButton("");
		btnRemove.setIcon(new ImageIcon(EnvironmentPanel.class.getResource("/icons/eraser.png")));
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().removeSelectedLightPoints();
			}
		});
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.anchor = GridBagConstraints.EAST;
		gbc_btnRemove.insets = new Insets(0, 0, 0, 5);
		gbc_btnRemove.gridx = 0;
		gbc_btnRemove.gridy = 0;
		toolPanel.add(btnRemove, gbc_btnRemove);
		
		ButtonGroup group = new ButtonGroup();
		
		btnAddPointLight = new JToggleButton("");
		btnAddPointLight.setIcon(new ImageIcon(EnvironmentPanel.class.getResource("/icons/brush.png")));
		btnAddPointLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getPointLightTool().setMode(Mode.ADD);
			}
		});
		group.add(btnAddPointLight);
		btnSelectPointLight = new JToggleButton("");
		btnSelectPointLight.setSelected(true);
		btnSelectPointLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.getEditor().getPointLightTool().setMode(Mode.SELECT);
			}
		});
		group.add(btnSelectPointLight);
		btnSelectPointLight.setIcon(new ImageIcon(EnvironmentPanel.class.getResource("/icons/select.png")));
		GridBagConstraints gbc_btnSelect = new GridBagConstraints();
		gbc_btnSelect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSelect.insets = new Insets(0, 0, 0, 5);
		gbc_btnSelect.gridx = 1;
		gbc_btnSelect.gridy = 0;
		toolPanel.add(btnSelectPointLight, gbc_btnSelect);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 0;
		toolPanel.add(btnAddPointLight, gbc_btnAdd);
				
	}

	protected void computeTransform() {
		float intensity = (float) spinnerIntensity.getValue();
		float r = (float) spinnerPlR.getValue();
		float g = (float) spinnerPlG.getValue();
		float b = (float) spinnerPlB.getValue();
		float x = (float) spinnerPosX.getValue();
		float y = (float) spinnerPosY.getValue();
		float z = (float) spinnerPosZ.getValue();
		presenter.getEditor().setPoinLightTransform(intensity, r, g, b, x, y, z);
	}

	private void applyAmpientLight() {
		presenter.getEditor().setAmbientLight((float) spinnerAlR.getValue(), (float) spinnerAlG.getValue(), (float) spinnerAlB.getValue());
	}

	public void setAmbientLightColor(Color color) {
		lock = true;
		spinnerAlR.setValue(color.r);
		spinnerAlG.setValue(color.g);
		spinnerAlB.setValue(color.b);
		lock = false;
	}
	
	public void setPointLightName(String name) {
		textFieldPointLightName.setText(name);
	}

	public void setPointLightPosition(Vector3 position) {
		lock = true;
		spinnerPosX.setValue(position.x);
		spinnerPosY.setValue(position.y);
		spinnerPosZ.setValue(position.z);
		lock = false;
	}

	public void setPointLightColor(Color color) {
		lock = true;
		spinnerPlR.setValue(color.r);
		spinnerPlG.setValue(color.g);
		spinnerPlB.setValue(color.b);
		lock = false;
	}
	
	public void setPointLightIntensity(float intensity) {
		lock = true;
		spinnerIntensity.setValue(intensity);
		lock = false;
	}
	
	/*private class MyInputVerifier extends InputVerifier {
	    @Override
	    public boolean verify(JComponent input) {
	    	if(lock) return true;
	        String text = ((JTextField) input).getText();
	        return presenter.getEditor().setPointLightName(text);
	    }
	}*/
	
	public Mode getSelectedMode() {
		return btnSelectPointLight.isSelected() ? Mode.SELECT : Mode.ADD;
	}

	public SkyboxPanel getSkyboxPanel() {
		return skyboxPanel;
	}
	
}
