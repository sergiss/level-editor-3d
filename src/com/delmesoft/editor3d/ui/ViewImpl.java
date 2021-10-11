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
package com.delmesoft.editor3d.ui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.badlogic.gdx.Gdx;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.ui.dialog.NewProjectDialog;
import com.delmesoft.editor3d.ui.dialog.SplashDialog;

public class ViewImpl extends JFrame implements View {

	private static final long serialVersionUID = 1L;

	private static View instance;

	public static synchronized View getInstance() {
		if(instance == null) {
			instance = new ViewImpl();
		}
		return instance;
	}

	private Presenter presenter;

	// Dynamic panels
	private JTabbedPane tabbedPane;
	private JPanel toolPanel;

	private JPanel renderPanel;

	// Buttons
	private JButton btnSaveAs;

	private JToolBar toolBarMenu;
	private JPanel splitPanel;
	
	private Map<String, JScrollPane> toolPanelMap;
	private JButton btnSave;

	private ViewImpl() {
		super();	
		
		setMinimumSize(new Dimension(640, 480));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					Gdx.app.exit();
				} finally {
					System.exit(1);
				}
			}
		});

		setIconImage(Toolkit.getDefaultToolkit().getImage(ViewImpl.class.getResource("/icons/gear.png")));
		
		toolPanelMap = new HashMap<>();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		toolBarMenu = new JToolBar();
		toolBarMenu.setFloatable(false);
		GridBagConstraints gbc_toolBarMenu = new GridBagConstraints();
		gbc_toolBarMenu.insets = new Insets(0, 0, 5, 0);
		gbc_toolBarMenu.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBarMenu.gridx = 0;
		gbc_toolBarMenu.gridy = 0;
		panel.add(toolBarMenu, gbc_toolBarMenu);

		JButton btnNew = new JButton("New");
		btnNew.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/newFile.png")));
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.createNewProject();
			}
		});
		toolBarMenu.add(btnNew);

		JButton btnLoad = new JButton("Load");
		btnLoad.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/loadFile.png")));
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.load();
			}
		});
		toolBarMenu.add(btnLoad);
		
		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/save.png")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.save();
			}
		});
		toolBarMenu.add(btnSave);
				
		btnSaveAs = new JButton("Save...");
		btnSaveAs.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/saveFile.png")));
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presenter.saveAs();
			}
		});
		toolBarMenu.add(btnSaveAs);

		splitPanel = new JPanel();
		GridBagConstraints gbc_splitPanel = new GridBagConstraints();
		gbc_splitPanel.fill = GridBagConstraints.BOTH;
		gbc_splitPanel.gridx = 0;
		gbc_splitPanel.gridy = 1;
		panel.add(splitPanel, gbc_splitPanel);
		GridBagLayout gbl_splitPanel = new GridBagLayout();
		gbl_splitPanel.columnWidths = new int[]{0, 0, 0};
		gbl_splitPanel.rowHeights = new int[]{0, 0};
		gbl_splitPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_splitPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		splitPanel.setLayout(gbl_splitPanel);

		toolPanel = new JPanel();
		GridBagConstraints gbc_toolPanel = new GridBagConstraints();
		gbc_toolPanel.fill = GridBagConstraints.BOTH;
		gbc_toolPanel.insets = new Insets(0, 0, 0, 5);
		gbc_toolPanel.gridx = 0;
		gbc_toolPanel.gridy = 0;
		splitPanel.add(toolPanel, gbc_toolPanel);
		GridBagLayout gbl_toolPanel = new GridBagLayout();
		gbl_toolPanel.columnWidths = new int[] { 0, 0 };
		gbl_toolPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_toolPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_toolPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		toolPanel.setLayout(gbl_toolPanel);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);		
		tabbedPane.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	if(presenter.getEditor() != null)
	        		presenter.getEditor().setCurrentTool(tabbedPane.getSelectedIndex());
	        }
	    });
		
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 0);
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		toolPanel.add(tabbedPane, gbc_tabbedPane);

		renderPanel = new JPanel();
		GridBagConstraints gbc_renderPanel = new GridBagConstraints();
		gbc_renderPanel.insets = new Insets(0, 0, 0, 5);
		gbc_renderPanel.fill = GridBagConstraints.BOTH;
		gbc_renderPanel.gridx = 1;
		gbc_renderPanel.gridy = 0;
		splitPanel.add(renderPanel, gbc_renderPanel);
		renderPanel.setLayout(new BorderLayout(0, 0));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setSize(new Dimension((int) (screenSize.width * 0.75), (int) (screenSize.height * 0.75)));
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setLocationRelativeTo(null);
		
		new SplashDialog(ViewImpl.this);

	}

	@Override
	public void addToolbarSeparator() {
		toolBarMenu.addSeparator();
	}

	@Override
	public void addToolbarComponent(Component component) {
		toolBarMenu.add(component);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}
	
	@Override
	public void addToolPanel(String name, JPanel panel) {
		if (toolPanelMap.containsKey(name) == false) {
			JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setMinimumSize(panel.getPreferredSize());
			toolPanelMap.put(name, scrollPane);
			tabbedPane.addTab(name, null, scrollPane, null);
		}
	}

	@Override
	public void removeToolPanel(String name) {
		JScrollPane scrollPane = toolPanelMap.remove(name);
		if(scrollPane != null)
			tabbedPane.remove(scrollPane);
	}

	@Override
	public void setSettingsPanel(JPanel panel) {
		GridBagConstraints gbc_generalPanel = new GridBagConstraints();
		gbc_generalPanel.fill = GridBagConstraints.BOTH;
		gbc_generalPanel.gridx = 0;
		gbc_generalPanel.gridy = 1;
		toolPanel.add(panel, gbc_generalPanel);
	}

	@Override
	public void setInfoPanel(JPanel panel) {
		GridBagConstraints gbcInfoPanel = new GridBagConstraints();
		gbcInfoPanel.fill = GridBagConstraints.BOTH;
		gbcInfoPanel.gridx = 0;
		gbcInfoPanel.gridy = 1;
		getContentPane().add(panel, gbcInfoPanel);
	}

	@Override
	public void setCanvas(Canvas canvas) {
		renderPanel.add(canvas, BorderLayout.CENTER);		
	}

	@Override
	public Settings openNewProjectDialog() {
		NewProjectDialog newProjectDialog = new NewProjectDialog(this);
		newProjectDialog.setVisible(true);		
		return newProjectDialog.getResults();
	}

	@Override
	public void showMessageError(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	@Override
	public void showMessageInfo(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	@Override
	public boolean showQuestion(String title, String message) {
		return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
	}
	
	@Override
	public JFrame getJFrame() {
		return this;
	}

	@Override
	public void setSaveButtonEnabled(boolean enabled) {
		btnSave.setEnabled(enabled);
		btnSaveAs.setEnabled(enabled);
	}

	@Override
	public int getTabbePaneToolSelectedIndex() {
		return tabbedPane.getSelectedIndex();
	}

}
