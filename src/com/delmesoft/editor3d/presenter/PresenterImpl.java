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
package com.delmesoft.editor3d.presenter;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.editor.Editor;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.graphics.FlyCamera;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.graphics.g2d.font.Font;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.LevelImpl;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.ui.View;
import com.delmesoft.editor3d.ui.ViewImpl;
import com.delmesoft.editor3d.ui.panel.BlockPanel;
import com.delmesoft.editor3d.ui.panel.DecalPanel;
import com.delmesoft.editor3d.ui.panel.EntityPanel;
import com.delmesoft.editor3d.ui.panel.EnvironmentPanel;
import com.delmesoft.editor3d.ui.panel.InfoPanel;
import com.delmesoft.editor3d.ui.panel.SettingsPanel;
import com.delmesoft.editor3d.ui.panel.ToolPanel;
import com.delmesoft.editor3d.undoredo.ChangeListener;
import com.delmesoft.editor3d.undoredo.UndoRedo;
import com.delmesoft.editor3d.undoredo.changeable.Changeable;
import com.delmesoft.editor3d.utils.LevelIO;
import com.delmesoft.editor3d.utils.Utils;

public class PresenterImpl implements Presenter, ApplicationListener, ChangeListener<Changeable> {
	
	private final Logger logger = Logger.getLogger(PresenterImpl.class.getName());

	private View view;
	
	// Tools
	private BlockPanel blockPanel;
	private EntityPanel entityPanel;
	private DecalPanel decalPanel;
	private EnvironmentPanel environmentPanel;
	private ToolPanel toolPanel;
	
	// Main
	private SettingsPanel settingsPanel;
	private InfoPanel infoPanel;

	private LwjglCanvas lwjglCanvas;

	private Level level;

	private Editor editor;
	
	private FlyCamera camera;
	private ModelBatch modelBatch;
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private JFileChooser fileChooser;

	private JButton btnUndo;
	private JButton btnRedo;

	private RenderContext renderContext;

	private Font font;

	public PresenterImpl(View view) {
		this.view = view;
		view.setPresenter(this);
		
		view.setTitle(Constants.getTitle("Delmesoft"));
		
		createToolbarButtons();		
		createMainPanels();
		createToolPanels();
		createFileChooser();
		
		view.setSaveButtonEnabled(false);
		
		lwjglCanvas = new LwjglCanvas(this);
		Canvas canvas = lwjglCanvas.getCanvas();
		view.setCanvas(canvas);
						
	}

	private void createFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		fileChooser.setFileFilter(new FileNameExtensionFilter("Level Files (*.s3l)", "s3l"));
	}

	private void createMainPanels() {
		
		settingsPanel = new SettingsPanel(this);
		view.setSettingsPanel(settingsPanel);		
		settingsPanel.setEnabled(false);

		infoPanel = new InfoPanel(this);
		view.setInfoPanel(infoPanel);
		
		infoPanel.setText("[x, y]");
		
	}

	private void createToolPanels() {
		blockPanel = new BlockPanel(this);
		view.addToolPanel("Blocks", blockPanel);
		blockPanel.setEnabled(false);

		entityPanel = new EntityPanel(this);
		view.addToolPanel("Entities", entityPanel);
		entityPanel.setEnabled(false);
		
		decalPanel = new DecalPanel(this);
		view.addToolPanel("Decals", decalPanel);
		decalPanel.setEnabled(false);

		environmentPanel = new EnvironmentPanel(this);
		view.addToolPanel("Environment", environmentPanel);
		environmentPanel.setEnabled(false);
		
		toolPanel = new ToolPanel(this);
		view.addToolPanel("Tools", toolPanel);
		toolPanel.setEnabled(false);
	}

	private void createToolbarButtons() {
		view.addToolbarSeparator();

		btnUndo = new JButton("");
		btnUndo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.undo();
			}
		});
		btnUndo.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/undo.png")));
		view.addToolbarComponent(btnUndo);

		btnRedo = new JButton("");
		btnRedo.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.redo();
			}
		});
		btnRedo.setIcon(new ImageIcon(ViewImpl.class.getResource("/icons/redo.png")));
		view.addToolbarComponent(btnRedo);
		
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);

		view.addToolbarSeparator();
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public void create() {
		
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 0));
		
		Config config = new DefaultShader.Config();
		config.numPointLights = 100;
		
		ShaderProvider shaderProvider = new DefaultShaderProvider(config);
		modelBatch = new ModelBatch(renderContext, shaderProvider);
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		camera = new FlyCamera(72, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		font = new Font(new TextureRegion(FileManager.getInstance().get("textures/font.png", Texture.class)), 16, 16);
		font.scale = 1.25F;
		
	}

	@Override
	public void render() {

		final Graphics graphics = Gdx.graphics;

		graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update(Gdx.graphics.getDeltaTime());

		renderContext.begin();

		if (level != null) {
			level.render(modelBatch);
		}

		if (editor != null) {
			editor.update();
			if (settingsPanel.isShowGridSelected()) {
				editor.render(modelBatch);
			}
		}

		renderContext.end();

		if(editor != null) {
			editor.renderScreen(shapeRenderer);
			editor.renderArrow(spriteBatch);
		}
		
		if(settingsPanel.isShowStatisticsSelected()) {
			spriteBatch.begin();		
			font.render("FPS: " + graphics.getFramesPerSecond(), 5, graphics.getHeight() - 20, spriteBatch);
			font.render(String.format("Heap Size: %.2f MB", Utils.bytesToMagaBytes(Runtime.getRuntime().totalMemory())), 5, graphics.getHeight() - 35, spriteBatch);
			font.render(String.format("Heap Use: %.2f MB", Utils.bytesToMagaBytes(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())), 5, graphics.getHeight() - 50, spriteBatch);
			spriteBatch.end();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth  = width;
		camera.viewportHeight = height;
		spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}	
	
	@Override
	public void pause() {}

	@Override
	public void resume() {}
	
	@Override
	public void dispose() {		
		FileManager.getInstance().clear();
	}

	@Override
	public void createNewProject() {
		
		boolean retry;
		try {
			Settings settings = view.openNewProjectDialog();
			if (settings != null) {
				updateProjectFile(null);
				initialize(settings);
				
				updatePanels();
			}
			retry = false;
		} catch (OutOfMemoryError e) {
			level = null;
			String message = "There is no memory available for this setting. Try again.";
			view.showMessageError("Error", message);
			logger.log(java.util.logging.Level.SEVERE, message, e);
			retry = true;
			retry = true;
		} catch (Exception e) {
			String message = "Error creating new project. Try again.";
			view.showMessageError("Error", message);
			logger.log(java.util.logging.Level.SEVERE, message, e);
			retry = true;
		}
		
		if(retry) {
			createNewProject();
		}
		
	}
	
	private void updatePanels() {
		editor.updateEntityPanel();
		editor.updateDecalPanel();
		editor.updateEnvironmentPanel();
	}

	@Override
	public void initialize(Settings settings) throws Exception {
		
		level  = new LevelImpl(this, settings);
		editor = new Editor(level);
		
		editor.setCurrentTool(view.getTabbePaneToolSelectedIndex());
		
		// Camera
		camera.far = Math.max(8 << Plot.BIT_OFFSET, settings.chunkVisibility << Plot.BIT_OFFSET);
		camera.position.set(settings.aWidth * 0.5F, settings.aHeight * 0.5F, settings.aDepth * 0.5F);
		camera.direction.set(0, 0, -1);
		
		blockPanel.setEnabled(false);	
		entityPanel.setEnabled(true);		
		decalPanel.setEnabled(true);
		environmentPanel.setEnabled(true);
		toolPanel.setEnabled(true);
		
		// Settings panel
		settingsPanel.setShowGridSelected(true);
		settingsPanel.setShowBlocksSelected(true);
		settingsPanel.setShowEntitiesSelected(true);
		settingsPanel.setShowStatisticsSelected(true);
		
		TiledTexture tiledTexture = level.getTiledTexture();
		blockPanel.setTileMap(tiledTexture);
		
		view.setSaveButtonEnabled(true);
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);
		
		editor.getUndoRedo().setChangeListener(this);
						
	}

	@Override
	public BlockPanel getBlockPanel() {
		return blockPanel;
	}
	
	@Override
	public EntityPanel getEntityPanel() {
		return entityPanel;
	}
	
	@Override
	public DecalPanel getDecalPanel() {
		return decalPanel;
	}
	
	@Override
	public EnvironmentPanel getEnvironmentPanel() {
		return environmentPanel;
	}
	
	@Override
	public SettingsPanel getSettingsPanel() {
		return settingsPanel;
	}
	
	@Override
	public InfoPanel getInfoPanel() {
		return infoPanel;
	}
	
	@Override
	public ToolPanel getToolPanel() {
		return toolPanel;
	}
	
	public Camera getCamera() {
		return camera;
	}

	@Override
	public Editor getEditor() {
		return editor;
	}

	@Override
	public Level getLevel() {
		return level;
	}
	
	public RenderContext getRenderContext() {
		return renderContext;
	}
	
	@Override
	public void save() {
		if(Constants.file == null) {
			saveAs();
		} else {
			try {
				save(Constants.file);
			} catch (Exception e) {
				String message = "Error saving project.";
				view.showMessageError("Error", message);
				logger.log(java.util.logging.Level.SEVERE, message, e);
			}
		}
	}

	@Override
	public void saveAs() {

		int returnVal = fileChooser.showSaveDialog(view.getJFrame());

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			try {
				
				File file = fileChooser.getSelectedFile();
				if (!file.toString().toLowerCase().endsWith(".s3l")) {
					file = new File(file + ".s3l");
				}

				save(file);
				
			} catch (Throwable e) {
				String message = "Error saving project.";
				view.showMessageError("Error", message);
				logger.log(java.util.logging.Level.SEVERE, message, e);
			}

		}

	}
	
	public void save(File file) throws Exception {
		LevelIO.saveLevel(file, PresenterImpl.this, true);
		updateProjectFile(file);
		// clearUndoRedo();
	}

	private void updateProjectFile(File file) {
		Constants.file = file;
		view.setTitle(Constants.getTitle(file == null ? "Delmesoft" : file.getAbsolutePath()));
	}

	@Override
	public void load() {		
		int returnVal = fileChooser.showOpenDialog(view.getJFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			load(fileChooser.getSelectedFile());
		}
	}
	
	/* public void loadGta2(File file) {
		try {
			if (level != null) {
				level.dispose();
			}
			updateProjectFile(file);			
			LevelIO.importGTA2Level(file, PresenterImpl.this);
			updatePanels();
		} catch (Exception e) {
			String message = String.format("Error loading project '%s'.", file);
			view.showMessageError("Error", message);
			logger.log(java.util.logging.Level.SEVERE, message, e);
		}
	} */
	
	@Override
	public void load(File file) {
		try {
			if (level != null) {
				level.dispose();
			}

			updateProjectFile(file);

			LevelIO.loadLevel(file, PresenterImpl.this);
			// LevelIO.importGTA2Level(file, PresenterImpl.this); // .gmp (uncompressed)
			
			updatePanels();
		} catch (Exception e) {
			String message = String.format("Error loading project '%s'.", file);
			view.showMessageError("Error", message);
			logger.log(java.util.logging.Level.SEVERE, message, e);
		}
	}

	@Override
	public void onElementChange(Changeable e) {

	}

	@Override
	public void onChange(UndoRedo<Changeable> undoRedo) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run() {
				btnUndo.setEnabled(undoRedo.canUndo());
				btnRedo.setEnabled(undoRedo.canRedo());
	
				editor.updateBlockPanel();
				editor.updateEntityPanel();
				editor.updateDecalPanel();
				editor.updateEnvironmentPanel();
				editor.updateToolPanel();
			}
		});
	}
	
	/*private void clearUndoRedo() {
		editor.getUndoRedo().clear();
		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);
	}*/

}
