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
package com.delmesoft.editor3d.editor;

import static com.delmesoft.editor3d.level.block.ChunkData.CLEAR_VALUE1;
import static com.delmesoft.editor3d.level.block.ChunkData.CLEAR_VALUE2;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.editor.tool.BlockTool;
import com.delmesoft.editor3d.editor.tool.DecalTool;
import com.delmesoft.editor3d.editor.tool.EntityTool;
import com.delmesoft.editor3d.editor.tool.EntityTool.FileDescriptor;
import com.delmesoft.editor3d.editor.tool.InputProcessorImpl;
import com.delmesoft.editor3d.editor.tool.PointLightTool;
import com.delmesoft.editor3d.editor.tool.Tool;
import com.delmesoft.editor3d.editor.tool.ZoneTool;
import com.delmesoft.editor3d.entity.Entity;
import com.delmesoft.editor3d.entity.EntityAnimation;
import com.delmesoft.editor3d.environment.PointLight;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.graphics.g2d.TextureArea;
import com.delmesoft.editor3d.graphics.g3d.decal.Decal;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.ui.dialog.TextureRegionDialog;
import com.delmesoft.editor3d.ui.panel.BlockPanel;
import com.delmesoft.editor3d.ui.panel.DecalPanel;
import com.delmesoft.editor3d.ui.panel.EntityPanel;
import com.delmesoft.editor3d.ui.panel.EnvironmentPanel;
import com.delmesoft.editor3d.ui.panel.ZonePanel;
import com.delmesoft.editor3d.undoredo.UndoRedo;
import com.delmesoft.editor3d.undoredo.UndoRedoImpl;
import com.delmesoft.editor3d.undoredo.changeable.AmbientLightChange;
import com.delmesoft.editor3d.undoredo.changeable.Changeable;
import com.delmesoft.editor3d.undoredo.changeable.DataChange;
import com.delmesoft.editor3d.undoredo.changeable.PointLightAddRemoveChange;
import com.delmesoft.editor3d.undoredo.changeable.PointLightTransformChange;
import com.delmesoft.editor3d.undoredo.changeable.ZoneAddRemoveChange;
import com.delmesoft.editor3d.undoredo.changeable.ZoneTransformChange;
import com.delmesoft.editor3d.utils.RectangleSplitter;
import com.delmesoft.editor3d.utils.Utils;
import com.delmesoft.editor3d.utils.Vec3i;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Editor extends Grid {
	
	private UndoRedo<Changeable> undoRedo;	
	
	private Array<Tool> tools;
	private Tool tool;

	private EntityTool entityTool;
	private DecalTool decalTool;
	private PointLightTool pointLightTool;
	private ZoneTool zoneTool;

	private TextureRegionDialog textureRegionSelector;
	
	private InputProcessorImpl inputProcessor;
	
	public Editor(Level level) {
		super(level);		
		
		undoRedo = new UndoRedoImpl<>();
		
		tools = new Array<Tool>();
		tools.add(new BlockTool(this));
		entityTool = new EntityTool(this);
		tools.add(entityTool);
		decalTool = new DecalTool(this);
		tools.add(decalTool);
		pointLightTool = new PointLightTool(this);		
		tools.add(pointLightTool);
		zoneTool = new ZoneTool(this);
		tools.add(zoneTool);
		
		textureRegionSelector = new TextureRegionDialog(level.getPresenter().getView());
		
		inputProcessor = new InputProcessorImpl();		
		Gdx.input.setInputProcessor(inputProcessor);
		
	}

	public void update() {

		Input input = Gdx.input;
		if (input.isKeyJustPressed(Settings.KEY_GRID_UP) && posY + 1F < maxY) {
			posY += 1F;
			grid.worldTransform.val[13] = posY + GRID_OFFSET;
		} else if (input.isKeyJustPressed(Settings.KEY_GRID_DOWN) && posY > 1F) {
			posY -= 1F;
			grid.worldTransform.val[13] = posY + GRID_OFFSET;
		}

		tool.update();
		
		if(input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if(input.isKeyJustPressed(Keys.C)) { // Copy
				tool.copy();
			} else if(input.isKeyJustPressed(Keys.V)) { // Paste
				tool.paste();
			} else if(input.isKeyJustPressed(Keys.Z)) { // Undo
				undo();
			} else if(input.isKeyJustPressed(Keys.Y)) { // Redo
				redo();
			}
		}

	}

	public void undo() {
		Gdx.app.postRunnable(new Runnable() {			
			@Override
			public void run() {
				undoRedo.undo();
				level.forceUpdate();
			}
		});
	}
	
	public void redo() {
		Gdx.app.postRunnable(new Runnable() {			
			@Override
			public void run() {
				undoRedo.redo();
				level.forceUpdate();
			}
		});
	}
	
	private final Operation[] operations = { new Operation() { // Clear

		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) {
			index <<= 1;
			chunkData.getData()[index] = CLEAR_VALUE1;
			chunkData.getData()[index + 1] = CLEAR_VALUE2;
		}

		public void initialize() {}

	},
	new Operation() {
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) { // Geometry
			chunkData.setGeometryType(index, blockPanel.getGeometryType());
		}
		
		public void initialize() {}
		
	}, new Operation() {
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) { // Opacity
			chunkData.setOpacity(index, chunkData.hasFaces(index) ? blockPanel.getOpacity() : 1);
		}
		
		public void initialize() {}
		
	}, new Operation() {
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) { // Face
			int type = (int) args[0];
			int side = (int) args[1];
			switch (type) {
			case 1: // texture index				
				chunkData.setTextureIndex(index, side, blockPanel.getFaceIndex(side));		
			case 0: // enabled				
				if(blockPanel.isFaceEnabled(side)) {
					if(chunkData.hasFaces(index) == false) {
						chunkData.setOpacity(index, ChunkData.MAX_VALUE_4_BITS);
					}
					chunkData.setFaceEnabled(index, side, true);
				} else {
					
					chunkData.setFaceEnabled(index, side, false);
					if(chunkData.hasFaces(index) == false) {
						chunkData.setOpacity(index, 1);
					}
				}				
				break;		
			case 2: // texture angle
				int angle = blockPanel.getFaceAngle(side);
				chunkData.setTextureAngle(index, side, angle);
				break;
			case 3: // texture flip x
				chunkData.setFaceFlippedHorizontally(index, side, blockPanel.isFaceFlippedHorizontally(side));
				break;
			case 4: // texture flip y
				chunkData.setFaceFlippedVertically(index, side, blockPanel.isFaceFlippedVertically(side));
				break;
			case 5: // texture blending
				chunkData.setFaceBlending(index, side, blockPanel.isFaceBlending(side));
				break;
			}
		}
		
		public void initialize() {}
		
	}, new Operation() { // Sidewalk Path
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) {
			int side = (int) args[0];
			if(side > 3) {
				for(int i = 0; i < 4; ++i) {
					chunkData.setSidewalkDirection(index, i, blockPanel.isSidewalkSelected(i));
				}
			} else {
				chunkData.setSidewalkDirection(index, side, blockPanel.isSidewalkSelected(side));
			}			
		}
		
		public void initialize() {}
		
	}, new Operation() { // Road Path
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) {
			int side = (int) args[0];
			if(side > 3) {
				for(int i = 0; i < 4; ++i) {
					chunkData.setRoadDirection(index, i, blockPanel.isRoadSelected(i));
				}
			} else {
				chunkData.setRoadDirection(index, side, blockPanel.isRoadSelected(side));
			}
		}
		
		public void initialize() {}
		
	},new Operation() { // Clone
		
		long data1, data2;
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) {
			index <<= 1;
			chunkData.getData()[index]     = data1;
			chunkData.getData()[index + 1] = data2;
		}
		
		public void initialize() {
			
			Vec3i position = positions.get(0);
			int x = position.x;
			int y = position.y;
			int z = position.z;
			Chunk chunk = level.getChunkAbsolute(x, z);
			if (chunk != null) {
				x -= chunk.getWorldX();
				z -= chunk.getWorldZ();
				ChunkData chunkData = chunk.getChunkData();
				int index = chunkData.positionToIndex(x, y, z) << 1;
				data1 = chunkData.getData()[index];
				data2 = chunkData.getData()[index + 1];
			}
			
		}

	}, new Operation() {
		
		@Override
		public void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object... args) {
			chunkData.setForceDraw(index, blockPanel.isForceDraw());
		}
		
		@Override
		public void initialize() {}
	} };
	
	public void updateSelection(int opcode, Object... args) {

		BlockPanel blockPanel = level.getPresenter().getBlockPanel();

		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {

				if (positions.size > 0) {

					undoRedo.newChange();

					operations[opcode].initialize();

					ChunkData chunkData;
					Chunk chunk;
					int index;
					int x, y, z;
					for (Vec3i position : positions) {

						x = position.x;
						y = position.y;
						z = position.z;

						chunk = level.getChunkAbsolute(x, z);

						if (chunk != null) {

							chunkData = chunk.getChunkData();

							x -= chunk.getWorldX();
							z -= chunk.getWorldZ();

							index = chunkData.positionToIndex(x, y, z);

							undoRedo.mem(new DataChange(chunkData, index << 1));

							operations[opcode].perform(chunkData, index, blockPanel, args);
							
							x = chunk.getLocalX(); z = chunk.getLocalZ();
							for(int x1 = x - 1; x1 < x + 2; ++x1) {
								for(int z1 = z - 1; z1 < z + 2; ++z1) {
									chunk = level.getChunkRelative(x1, z1);
									if(chunk != null) {
										chunk.setNeedUpdate(true);
									}
								}
							}							
							
						}

					}

					level.forceUpdate();
					updateBlockPanel();

				}

			}
			
		});

	}
	
	public void updateBlockPanel() {
		
		if(level.getTiledTexture() != null) {
			
			final BlockPanel blockPanel = level.getPresenter().getBlockPanel();
			
			if(positions.size > 0) {

				blockPanel.setEnabled(true);

				Vec3i position = positions.get(0);

				int x = position.x;
				int y = position.y;
				int z = position.z;

				Chunk chunk = level.getChunkAbsolute(x, z);

				if (chunk != null) {
					
					x -= chunk.getWorldX();
					z -= chunk.getWorldZ();

					ChunkData chunkData = chunk.getChunkData();
					int index = chunkData.positionToIndex(x, y, z);
					
					blockPanel.setGeometryType(chunkData.getGeometryType(index));				
					blockPanel.setOpacity(chunkData.getOpacity(index));
					blockPanel.setForceDraw(chunkData.isForceDraw(index));

					for (int i = 0; i < 4; ++i) {
						blockPanel.setSidewalkSelected(i, chunkData.isSidewalkDirection(index, i));
						blockPanel.setRoadSelected(i, chunkData.isRoadDirection(index, i));
					}

					int ordinal;
					for (Side side : Side.values()) {
						ordinal = side.ordinal();
						blockPanel.setFaceEnabled(ordinal, chunkData.isFaceEnabled(index, ordinal));
						blockPanel.setFaceIndex(ordinal, chunkData.getTextureIndex(index, ordinal));
						blockPanel.setFaceAngle(ordinal, chunkData.getTextureAngle(index, ordinal));
						blockPanel.setFaceFlippedHorizontally(ordinal, chunkData.isFaceFlippedHorizontally(index, ordinal));
						blockPanel.setFaceFlippedVertically(ordinal, chunkData.isFaceFlippedVertically(index, ordinal));
						blockPanel.setFaceBlending(ordinal, chunkData.isFaceBlending(index, ordinal));
					}

				}
				
			} else {
				blockPanel.setEnabled(false);
			}
			
		}
		
	}
	
	public void setFaceSelection(Side side) {
		Gdx.app.postRunnable(new Runnable() {			
			@Override
			public void run() {
				final BlockPanel blockPanel = level.getPresenter().getBlockPanel();
				blockPanel.setFaceSelection(side);
			}
		});		
	}
	
	// Entities ***********************************************************
	
	public void setEntityTransform(float positionX, float positionY, float positionZ, float rotationX, float rotationY, float rotationZ, float scaleX, float scaleY, float scaleZ) {
		
		if(entities.size > 0) {
			
			Vector3 tmp1 = new Vector3();
			Vector3 tmp2 = new Vector3();
			Quaternion tmp3 = new Quaternion();
			
			Entity reference = entities.get(0);
			
			Vector3 position = reference.getTransform().getTranslation(new Vector3()).sub(positionX, positionY, positionZ);
			Vector3 scale = reference.getTransform().getScale(new Vector3()).sub(scaleX, scaleY, scaleZ);
			reference.getTransform().getRotation(tmp3, true);
			Vector3 rotation = new Vector3(tmp3.getYawRad(), tmp3.getPitchRad(), tmp3.getRollRad()).sub(rotationX, rotationY, rotationZ);
									
			for (Entity entity : entities) {
				entity.getTransform().getTranslation(tmp1);
				entity.getTransform().getScale(tmp2);
				entity.getTransform().getRotation(tmp3, true);
				entity.getTransform().setToTranslationAndScaling(tmp1.sub(position), tmp2.sub(scale));
				entity.getTransform().rotate(tmp3.setEulerAnglesRad(tmp3.getYawRad() - rotation.x, tmp3.getPitchRad() - rotation.y, tmp3.getRollRad() - rotation.z));
			}
			
		}
		
	}
	
	public boolean setEntityName(String name) {
		if (entities.size > 0) {
			Entity entity = entities.get(0);
			return setEntityName(entity, name);
		}
		return false;
	}
	
	private boolean setEntityName(Entity entity, String name) {
		if(name != null && name.isEmpty() == false && name.matches(".*\\w.*")) {
			for (Entity e : level.getEntities()) {
				if (e != entity) {
					if (name.equals(e.getName())) {
						level.getPresenter().getEntityPanel().setEntityName(entity.getName());
						return false;
					}
				}
			}
		} else {
			return false;
		}
		entity.setName(name);
		return true;
	}
	
	public void updateEntityPanel() {
		
		final EntityPanel entityPanel = level.getPresenter().getEntityPanel();
		entityPanel.setModelList(entityTool.getFileDescriptors());
		
		if(entities.size > 0) {
			Entity entity = entities.get(0);
			Vector3 tmp = new Vector3();
			entityPanel.setPosition(entity.getTransform().getTranslation(tmp));
			entityPanel.setScale(entity.getTransform().getScale(tmp));
			
			Quaternion rotation = new Quaternion();
			entity.getTransform().getRotation(rotation, true);
			tmp.set(rotation.getYawRad(), rotation.getPitchRad(), rotation.getRollRad());
			entityPanel.setRotation(tmp);
			
			List<String> animationList = new ArrayList<>();
			if(entity instanceof EntityAnimation) {
				EntityAnimation entityAnimation = (EntityAnimation) entity;
				Array<Animation> animations = entityAnimation.getAnimations();
				if(animations.size > 0) {
					for(Animation animation : animations) {
						animationList.add(animation.id);
					}
				}
			}			
			entityPanel.setAnimations(animationList);
			entityPanel.setEntityName(entity.getName());
			entityPanel.setEntityEnabled(true);
		} else {
			entityPanel.setEntityEnabled(false);
		}
		
	}
	
	public void loadModel() {		
		if(entityTool.loadModel()) {
			updateEntityPanel();
		}
	}
	
	public void removeModel(FileDescriptor fileDescriptor) {
		if(entityTool.removeModel(fileDescriptor)) {
			Iterator<Entity> it = level.getEntities().iterator();
			Entity entity;
			while(it.hasNext()) {
				entity = it.next();
				if(entity.getProperties().get(Constants.FILE_DESCRIPTOR) == fileDescriptor) {
					entities.removeValue(entity, true);
					it.remove();
				}
			}
			updateEntityPanel();
		}
	}
	
	public void addEntity(Vector3 position) {
		FileDescriptor fileDescriptor = level.getPresenter().getEntityPanel().getFileDescriptor();
		addEntity(position, new Vector3(0.1F, 0.1F, 0.1F), new Quaternion(),fileDescriptor);
		//updateEntityPanel();
	}
	
	public Entity addEntity(Vector3 position, Vector3 scale, Quaternion rotation, FileDescriptor fileDescriptor) {
		Model model = FileManager.getInstance().get(fileDescriptor.path, Model.class);
		EntityAnimation entity = new EntityAnimation(model, level);
		String name = fileDescriptor.name;
		int count = 0;
		while(setEntityName(entity, name) == false) {
			name = fileDescriptor.name + (++count);
		} 
		entity.setName(name);
		entity.getTransform().setToTranslationAndScaling(position, scale);
		entity.getTransform().rotate(rotation);
		entity.getProperties().put(Constants.FILE_DESCRIPTOR, fileDescriptor);
		level.addEntity(entity);
		return entity;
	}
	
	public void removeSelectedEntities() {
		for(Entity entity : entities) {
			level.removeEntity(entity);
		}
		entities.clear();
		updateEntityPanel();
	}
	
	public void setAnimation(String id) {
		if(entities.size > 0) {
			((EntityAnimation) entities.get(0)).getAnimationController().setAnimation(id, -1);
		}
	}
	
	public EntityTool getEntityTool() {
		return entityTool;
	}	
	
	// Decals ***********************************************************
	
	public void setDecalTransform(float positionX, float positionY, float positionZ, float width, float height) {
				
		if(decals.size > 0) {
			
			Decal reference = decals.get(0);
			
			Vector3 position   = new Vector3(reference.position).sub(positionX, positionY, positionZ);
			Vector2 dimensions = new Vector2(reference.halfWidth, reference.halfHeight).sub(width, height);
			
			for (Decal decal : decals) {
				decal.position.sub(position);
				decal.halfWidth  -= dimensions.x;
				decal.halfHeight -= dimensions.y;
			}
			
		}
		
	}
	
	public boolean setDecalName(String name) {
		if (decals.size > 0) {
			Decal decal = decals.get(0);
			return setDecalName(decal, name);
		}
		return false;
	}
	
	private boolean setDecalName(Decal decal, String name) {
		if(name != null && name.isEmpty() == false && name.matches(".*\\w.*")) {
			for (Decal d : level.getDecals()) {
				if (d != decal) {
					if (name.equals(d.getName())) {
						level.getPresenter().getDecalPanel().setDecalName(decal.getName());
						return false;
					}
				}
			}
		} else {
			return false;
		}
		decal.setName(name);
		return true;
	}

	public DecalTool getDecalTool() {
		return decalTool;
	}

	public void removeSelectedDecals() {
		for(Decal decal : decals) {
			level.removeDecal(decal);
		}
		decals.clear();
		updateDecalPanel();
	}

	public void updateDecalPanel() {
		DecalPanel decalPanel = level.getPresenter().getDecalPanel();
		decalPanel.setTextureAtlasList(decalTool.getFileDescriptors());
		if(decals.size > 0) {
			Decal decal = decals.get(0);
			decalPanel.setPosition(decal.position);
			decalPanel.setDimension(decal.halfWidth, decal.halfHeight);
			decalPanel.setDecalName(decal.getName());
			decalPanel.setDecalEnabled(true);
		} else {
			decalPanel.setDecalEnabled(false);
		}
	}

	public void removeTextureAtlas(FileDescriptor fileDescriptor) {
		if(decalTool.removeTextureAtlas(fileDescriptor)) {
			Iterator<Decal> it = level.getDecals().iterator();
			Decal decal;
			while(it.hasNext()) {
				decal = it.next();
				if(decal.getProperties().get(Constants.FILE_DESCRIPTOR) == fileDescriptor) {
					decals.removeValue(decal, true);
					it.remove();
				}
			}
			updateDecalPanel();
		}
	}

	public void loadTextureAtlas() {
		if(decalTool.loadTextureAtlas()) {
			updateDecalPanel();
		}
	}
	
	public void addDecal(Vector3 position) {
		
		FileDescriptor fileDescriptor = level.getPresenter().getDecalPanel().getFileDescriptor();	
		
		TextureAtlas textureAtlas = FileManager.getInstance().get(fileDescriptor.path, TextureAtlas.class);		
		
		String regionName = level.getPresenter().getDecalPanel().getRegionName();

		if(regionName == null || regionName.isEmpty() || regionName.matches(".*\\w.*") == false) {
			regionName = openAtlasRegionSelector(textureAtlas);	
			if(regionName != null) {
				level.getPresenter().getDecalPanel().setRegionName(regionName);
			}
		}
		
		if(regionName != null) {
			addDecal(position, null, regionName, fileDescriptor);		
			//updateDecalPanel();
		}
				
	}
	
	public Decal addDecal(Vector3 position, Vector2 dimensions, String regionName, FileDescriptor fileDescriptor) {		
		
		TextureAtlas textureAtlas = FileManager.getInstance().get(fileDescriptor.path, TextureAtlas.class);		
		AtlasRegion region = textureAtlas.findRegion(regionName);		

		TextureArea textureArea = new TextureArea(region);
		Decal decal = new Decal(textureArea);
		decal.getProperties().put(Constants.REGION_NAME, regionName);
		String name = region.name;
		int count = 0;
		while(setDecalName(decal, name) == false) {
			name = region.name + (++count);
		} 
		decal.setName(name);

		decal.position.set(position);
		if(dimensions != null) {
			decal.halfWidth  = dimensions.x;
			decal.halfHeight = dimensions.y;	
		} else {
			decal.halfWidth  = region.getRegionWidth()  * 0.5F * 0.01f;
			decal.halfHeight = region.getRegionHeight() * 0.5F * 0.01f;	
		}

		decal.getProperties().put(Constants.FILE_DESCRIPTOR, fileDescriptor);
		level.addDecal(decal);
		return decal;
			
	}

	// Environment ***********************************************************
	
	public void setAmbientLight(float r, float g, float b) {
		undoRedo.newChange();
		undoRedo.mem(new AmbientLightChange(level));
		level.getAmbientLightColor().set(r, g, b, 1F);
	}
	
	public void setPoinLightTransform(float intensity, float r, float g, float b, float x, float y, float z) {
				
		if(pointLights.size > 0) {
			
			undoRedo.newChange();
			
			PointLight reference = pointLights.get(0);
			
			Vector3 position = new Vector3(reference.light.position).sub(x, y, z);
			Vector3 color = new Vector3(reference.light.color.r, reference.light.color.g, reference.light.color.b).sub(r, g, b);
			float intensification = reference.light.intensity - intensity;
			
			for (PointLight pointLight : pointLights) {
				undoRedo.mem(new PointLightTransformChange(pointLight));
				pointLight.light.position.sub(position);
				pointLight.light.color.r -= color.x;
				pointLight.light.color.g -= color.y;
				pointLight.light.color.b -= color.z;
				pointLight.light.intensity -= intensification;
			}
			
		}
		
	}
	
	public void updateEnvironmentPanel() {
		final EnvironmentPanel environmentPanel = level.getPresenter().getEnvironmentPanel();
		environmentPanel.setAmbientLightColor(level.getAmbientLightColor());
				
		if(pointLights.size > 0) {
			PointLight pointLight = pointLights.get(0);
		
			environmentPanel.setPointLightName(pointLight.getName());
			environmentPanel.setPointLightPosition(pointLight.light.position);
			environmentPanel.setPointLightColor(pointLight.light.color);
			environmentPanel.setPointLightIntensity(pointLight.light.intensity);
			
			environmentPanel.setPointLigntEnabled(true);
		} else {
			environmentPanel.setPointLigntEnabled(false);
		}
		
		level.getPresenter().getEnvironmentPanel().getSkyboxPanel().setTextureAtlassPath(level.getSkybox().getTextureAtlasPath());	
		if(level.getSkybox().getTextureAtlasPath() != null) {
			for(Side side : Side.values()) {
				level.getPresenter().getEnvironmentPanel().getSkyboxPanel().setSkyboxRegion(side, level.getSkybox().getAtlasRegionBySide(side));
			}
		}
		
	}
	
	public boolean setPointLightName(String name) {
		if (pointLights.size > 0 && Utils.isEmpty(name) == false) {
			PointLight pointLight = pointLights.get(0);
			return setPointLightName(pointLight, name, true);
		}
		return false;
	}
	
	private boolean setPointLightName(PointLight pointLight, String name, boolean userEvent) {
		for (PointLight p : level.getPointLights()) {
			if (p != pointLight) {
				if (name.equals(p.getName())) {
					if(userEvent)
						level.getPresenter().getEnvironmentPanel().setPointLightName(pointLight.getName());
					return false;
				}
			}
		}

		if (userEvent) {
			undoRedo.newChange();
			undoRedo.mem(new PointLightTransformChange(pointLight));
		}
		pointLight.setName(name);
		return true;
	}
	
	public void addPointLight(Vector3 position) {
		undoRedo.newChange();
		addPointLight(position, null, 20);
	}
	
	public PointLight addPointLight(Vector3 position, Color color, float intensity) {
		PointLight pointLight = new PointLight();
		pointLight.setName("PointLight");
		String name = pointLight.getName();		
		int count = 0;
		while(setPointLightName(pointLight, name, false) == false) {
			name = pointLight.getName() + (++count);
		} 
		pointLight.setName(name);
		pointLight.light.position.set(position);
		if(color == null) {
			pointLight.light.setColor(1, 1, 1, 1);
		} else {
			pointLight.light.setColor(color);
		}
		
		pointLight.light.intensity = intensity;
		level.addPointLight(pointLight);
		
		undoRedo.mem(new PointLightAddRemoveChange(pointLight, getLevel()));
		
		//updateEnvironmentPanel();
		return pointLight;
	}

	public void removeSelectedLightPoints() {
		undoRedo.newChange();
		for(PointLight pointLight : pointLights) {
			undoRedo.mem(new PointLightAddRemoveChange(pointLight, getLevel()));
			level.removePointLight(pointLight);
		}
		pointLights.clear();
		updateEnvironmentPanel();
	}
	
	public PointLightTool getPointLightTool() {
		return pointLightTool;
	}
	
	public UndoRedo<Changeable> getUndoRedo() {
		return undoRedo;
	}
	
	// Zone ******************************************
	
	public boolean setZoneName(String name) {
		if (zones.size > 0 && Utils.isEmpty(name) == false) {
			Zone zone = zones.get(0);
			return setZoneName(zone, name, true);
		}
		return false;
	}
	
	private boolean setZoneName(Zone zone, String name, boolean userEvent) {
		for (Zone d : level.getZones()) {
			if (d != zone) {
				if (name.equals(d.getName())) {
					if(userEvent)
						level.getPresenter().getToolPanel().getZonePanel().setZoneName(zone.getName());
					return false;
				}
			}
		}

		if (userEvent) {
			undoRedo.newChange();
			undoRedo.mem(new ZoneTransformChange(zone));
		}
		zone.setName(name);
		return true;
	}
	
	public boolean setZoneParams(String params) {
		if(zones.size == 0 || params.equals(zones.get(0).getProperties().get(Constants.PARAMS))) return false;
		undoRedo.newChange();
		for(Zone zone : zones) {
			undoRedo.mem(new ZoneTransformChange(zone));
			zone.getProperties().put(Constants.PARAMS, params);
		}
		return true;
	}

	public void setZoneTransform(float positionX, float positionY, float positionZ, float width, float height, float depht) {
		if(zones.size > 0) {
			undoRedo.newChange();
			Zone reference = zones.get(0);
			
			Vector3 position   = new Vector3(reference.position).sub(positionX, positionY, positionZ);
			Vector3 dimensions = new Vector3(reference.dimensions).sub(width, height, depht);
			
			for (Zone zone : zones) {
				undoRedo.mem(new ZoneTransformChange(zone));
				zone.position.sub(position);
				zone.dimensions.sub(dimensions);;
			}
			
		}
	}
	
	public void removeSelectedZones() {
		undoRedo.newChange();
		for(Zone zone : zones) {
			undoRedo.mem(new ZoneAddRemoveChange(zone, getLevel()));
			level.removeZone(zone);
		}
		zones.clear();
		updateToolPanel();
	}
	
	public void addZone(Vector3 position) {
		undoRedo.newChange();
		addZone(position, null, "");
	}
		
	public Zone addZone(Vector3 position, Vector3 dimensions, String params) {
		Zone zone = new Zone();
		zone.setName("Zone");
		String name = zone.getName();
		
		int count = 0;
		while(setZoneName(zone, name, false) == false) {
			name = zone.getName() + (++count);
		} 
		zone.setName(name);
		zone.position.set(position);
		if(dimensions == null)
			zone.dimensions.set(0.5F, 0.5F, 0.5F);
		else
			zone.dimensions.set(dimensions);
		
		zone.getProperties().put(Constants.PARAMS, params);
		level.addZone(zone);
		
		undoRedo.mem(new ZoneAddRemoveChange(zone, getLevel()));
		
		return zone;
		
	}

	public void updateToolPanel() {
		ZonePanel zonePanel = level.getPresenter().getToolPanel().getZonePanel();
		if(zones.size > 0) {
			Zone zone = zones.get(0);
		
			zonePanel.setZoneName(zone.getName());
			zonePanel.setZoneParams((String) zone.getProperties().get(Constants.PARAMS));
			zonePanel.setZonePosition(zone.position);
			zonePanel.setZoneDimensions(zone.dimensions);
			
			zonePanel.setZoneEnabled(true);
		} else {
			zonePanel.setZoneEnabled(false);
		}
		
	}
	
	public ZoneTool getZoneTool() {
		return zoneTool;
	}
	
	private interface Operation {
		void initialize();
		void perform(ChunkData chunkData, int index, BlockPanel blockPanel, Object...args);		
	}
		
	public void setCurrentTool(int index) {
		if(tool != null) tool.clear();
		tool = tools.get(index);
	}
			
	@Override
	public void dispose() {
		super.dispose();
	}
		
	public void loadSkyboxTextureAtlas() {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		fileChooser.setFileFilter(new FileNameExtensionFilter("Texture Atlas (*.atlas)", "atlas"));
				
		int result = fileChooser.showOpenDialog(level.getPresenter().getView().getJFrame());
		if(result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			level.getSkybox().loadTextureAtlas(file.getAbsolutePath());			
		} else {
			level.getSkybox().loadTextureAtlas(null);
		}
		
		updateEnvironmentPanel();
		
	}
	
	public void setSkyboxRegion(Side side, String name) {
		level.getSkybox().setRegion(side, name);
		updateEnvironmentPanel();
	}

	public void generateRandomMap(int plotSize, int plotTile, int pathTile, String seed) {

		int side = Side.TOP.ordinal();

		int type;

		Level level = getLevel();
		level.clear();

		Settings settings = level.getSettings();

		int cols = settings.aWidth;
		int rows = settings.aDepth;

		RectangleSplitter rectangleSplitter = new RectangleSplitter();
		rectangleSplitter.setPlotSize(plotSize);		
		if(seed != null && seed.isEmpty() == false) {
			rectangleSplitter.setSeed(seed.hashCode());
		}
		rectangleSplitter.setSize(cols, rows);
		rectangleSplitter.split();

		int[][] data = rectangleSplitter.getData();

		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {

				Chunk chunk = level.getChunkAbsolute(x, y);
				if(chunk != null) {
					type = data[x][y] == 0 ? plotTile : pathTile;
					int index = chunk.getChunkData().positionToIndex(x - chunk.getWorldX(), 1, y - chunk.getWorldZ());
					chunk.getChunkData().setTextureIndex(index, side, type);
					chunk.getChunkData().setFaceEnabled(index, side, true);
				}

			}
		}		

		level.forceUpdate();
		
		entities.clear();
		decals.clear();
		pointLights.clear();
		
		updateEntityPanel();
		updateDecalPanel();
		updateEnvironmentPanel();
		updateBlockPanel();
		
	}
	
	public void clearLevel() {
		
		Level level = getLevel();
		level.clear();
		
		entities.clear();
		decals.clear();
		pointLights.clear();
		zones.clear();
		
		updateEntityPanel();
		updateDecalPanel();
		updateEnvironmentPanel();
		updateBlockPanel();
		updateToolPanel();
	}

	public String openAtlasRegionSelector(TextureAtlas textureAtlas) {
		try {
			textureRegionSelector.setTextureAtlas(textureAtlas);
		} catch (Exception e) {
			return null;
		}
		return textureRegionSelector.open();
	}

	public InputProcessorImpl getInputProcessor() {
		return inputProcessor;
	}
	
}
