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
package com.delmesoft.editor3d.editor.tool;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.editor.Editor;
import com.delmesoft.editor3d.entity.Entity;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.utils.BlockIntersector;
import com.delmesoft.editor3d.utils.Vec3i;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class EntityTool extends Tool {
			
	private JFileChooser fileChooser;
	private Array<FileDescriptor> fileDescriptors;
	
	private boolean enabled;
	
	private Mode mode = Mode.SELECT;
	private Vector3 moveAxis;
	private float refX;
	
	private boolean drag;
	
	public EntityTool(Editor editor) {
		super(editor);
		fileDescriptors = new Array<EntityTool.FileDescriptor>();
		
		mode = editor.getLevel().getPresenter().getEntityPanel().getSelectedMode();
		createFileChooser();
	}

	@Override
	public void update() {
	
		Input input = Gdx.input;
		
		InputProcessorImpl inputProcessor = editor.getInputProcessor();
		boolean leftButton = inputProcessor.isLeftButton();
		
		boolean keyX = input.isKeyJustPressed(Keys.X);
		boolean keyY = input.isKeyJustPressed(Keys.Y);
		boolean keyZ = input.isKeyJustPressed(Keys.Z);
		
		if(leftButton == false && (keyX || keyY || keyZ) && editor.getEntities().size > 0) {			
			if (keyX) {
				moveAxis = Vector3.X;
			} else if (keyY) {
				moveAxis = Vector3.Y;
			} else {
				moveAxis = Vector3.Z;
			}
			
			input.setCursorPosition(Gdx.graphics.getWidth() >> 1, Gdx.graphics.getHeight() >> 1);
			refX = (Gdx.graphics.getWidth() >> 1) * 0.05F;
			
			mode = Mode.MOVE;
		} else {
			
			switch (mode) {
			case SELECT :				
				if (leftButton) {
					
					if(editor.getInputProcessor().isDragged()) {
						Vector2 startPoint = inputProcessor.getStartPoint();
						Vector2 currentPoint = inputProcessor.getCurrentPoint();
						editor.showSelectionArea(startPoint.x, editor.getLevel().getCamera().viewportHeight - startPoint.y, 
								                 currentPoint.x, editor.getLevel().getCamera().viewportHeight - currentPoint.y);
						drag = true;						
					} else if(enabled){
						
						enabled = false;
						Level level = editor.getLevel();
						Ray ray = level.getCamera().getPickRay(Gdx.input.getX(), Gdx.input.getY());
						Entity entity = level.getEntity(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET);

						if (input.isKeyPressed(Keys.CONTROL_LEFT) == false) {
							editor.getEntities().clear();
							if (entity != null) {
								editor.getEntities().add(entity);
							}
						} else if (entity != null) {
							int index = editor.getEntities().indexOf(entity, true);
							if(index < 0) {
								editor.getEntities().add(entity);
							} else {
								editor.getEntities().removeIndex(index);
							}
						}
						
						editor.updateEntityPanel();
						
					}
					
				} else if (leftButton == false) {
					if(drag) {
						drag = false;
						editor.getEntities().clear();
						Rectangle selectionArea = editor.getSelectionArea();
												
						float x1 = selectionArea.x;
						float x2 = x1 + selectionArea.width;
						float stepSize = 2F;
						
						float y1 = selectionArea.y;
						float y2 = y1 + selectionArea.height;
						
						Level level = editor.getLevel();
						Camera camera = level.getCamera();
						float h = camera.viewportHeight;
						Set<Entity> entitySet = new HashSet<>();
						Entity entity;
						Ray ray;
						for(float x = x1; x < x2; x += stepSize) {
							for(float y = y1; y < y2; y += stepSize) {
								ray = camera.getPickRay(x, h - y);
								entity = level.getEntity(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET);
								if(entity != null) {
									if(entitySet.contains(entity) == false) {
										entitySet.add(entity);
										editor.getEntities().add(entity);
									}
								}
							}
						}
						editor.updateEntityPanel();
						editor.hideSelectionArea();
					}
					enabled = true;
				}
				
				break;			
			case MOVE :
							
				float x = input.getX() * 0.05F;
				float result = x - refX;

				if (result != 0F) {
					Vector3 tmp = Tool.tmp.set(moveAxis).scl(result);
					Array<Entity> entities = editor.getEntities();
					for (Entity entity : entities) {
						entity.getTransform().translate(tmp);
					}
					refX = x;
					editor.updateEntityPanel();				
				}
				if (leftButton && mode != Mode.SELECT) {
					mode = Mode.SELECT;
					enabled = false;
				}
				break;
			case ADD :
				if (leftButton && enabled) {
					enabled = false;
					Vector3 position = ray(input.getX(), input.getY());
					if(position != null) {
						editor.addEntity(position);
					}					
				} else if(leftButton == false){
					enabled = true;
				}
				
				break;
			default:
				break;
			}
			
		}
						
	}
	
	public Vector3 ray(final float x, final float y) { // WORLD RAY
				
		Level level = editor.getLevel();
		Camera camera = level.getCamera();
		Ray ray = camera.getPickRay(x, y);
		
		Vector3 result = new Vector3();
		Vec3i tmp = new Vec3i();
		boolean hit = level.ray(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET, tmp);
		
		BoundingBox gridBounds = editor.getGridBoundingBox();

		if (hit) {

			if (camera.position.y > (int) gridBounds.getCenterY()) {
				if (tmp.y > gridBounds.getCenterY()) {
					Side side = BlockIntersector.intersectRayBounds(ray.origin, ray.direction, tmp.x, tmp.y - 1, tmp.z);
					if(side != null)
						return result.set(tmp.x + 0.5F, tmp.y - 0.5F, tmp.z + 0.5F).add(side.x, side.y, side.z);
				}
			} else {
				if (tmp.y <= (int) gridBounds.getCenterY()) {
					Side side = BlockIntersector.intersectRayBounds(ray.origin, ray.direction, tmp.x, tmp.y - 1, tmp.z);
					if(side != null)
						return result.set(tmp.x + 0.5F, tmp.y - 0.5F, tmp.z + 0.5F).add(side.x, side.y, side.z);
				}
			}

		}

		if (Intersector.intersectRayBounds(ray, gridBounds, result)) {
			return result;
		}
		
		return null;
	}
	
	Array<Entity> memory;

	@Override
	public void copy() {
		memory = new Array<Entity>(editor.getEntities());
	}

	@Override
	public void paste() {
		if(memory != null && memory.size > 0) {
			Entity reference = memory.get(0);
			Input input = Gdx.input;
			Vector3 translation = ray(input.getX(), input.getY());
			if(translation != null) {
				Vector3 tmp1 = new Vector3();
				Vector3 tmp2 = new Vector3();
				Quaternion tmp3 = new Quaternion();
				translation.sub(reference.getTransform().getTranslation(tmp1));				
				for(Entity entity : memory) {
					editor.addEntity(entity.getTransform().getTranslation(tmp1).add(translation), entity.getTransform().getScale(tmp2), entity.getTransform().getRotation(tmp3, true), (FileDescriptor) entity.getProperties().get(Constants.FILE_DESCRIPTOR));
				}				
			}
		}
	}
	
	private void createFileChooser() {
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		fileChooser.setFileFilter(new FileNameExtensionFilter("LibGDX 3D Binary (*.g3db)", "g3db"));
	}
	
	public boolean removeModel(FileDescriptor fileDescriptor) {
		return fileDescriptors.removeValue(fileDescriptor, true);
	}

	public boolean loadModel() {
		
		int result = fileChooser.showOpenDialog(editor.getLevel().getPresenter().getView().getJFrame());
		
		if(result == JFileChooser.APPROVE_OPTION) {
			
			File file = fileChooser.getSelectedFile();
			
			try {

				for(FileDescriptor descriptor : fileDescriptors) {
					if(descriptor.path.equals(file.getAbsolutePath())) {
						editor.getLevel().getPresenter().getView().showMessageInfo("Info", String.format("The file %s has already been loaded.", file.getAbsolutePath()));
						return false;
					}
				}

				Model model = FileManager.getInstance().get(file.getAbsolutePath(), Model.class);
				if(model != null) {
					FileDescriptor descriptor = new FileDescriptor();
					String name = file.getName();
					int dotIndex = name.lastIndexOf(".");
					if(dotIndex > 0) {
						name = name.substring(0, dotIndex);
					}				
					descriptor.name = getFreeName(name);
					descriptor.path = file.getAbsolutePath();
					fileDescriptors.add(descriptor);
				}
				
				return true;

			} catch (Exception e) {
				editor.getLevel().getPresenter().getView().showMessageError("Error", String.format("Error loading file %s.", file.getAbsolutePath()));
			}
			
		}
		
		return false;
		
	}
	
	private String getFreeName(String name) {
		
		boolean repeat;
		int i = 1;
		do {
			repeat = false;
			for(FileDescriptor descriptor: fileDescriptors) {
				if(descriptor.name.equals(name)) {
					name += (i++);
					repeat = true;
					break;
				}
			}
			
		} while (repeat);
		
		return name;
	}
	
	public Array<FileDescriptor> getFileDescriptors() {
		return fileDescriptors;
	}

	public FileDescriptor getFileDescriptorByName(String name) {
		for(FileDescriptor fileDescriptor : fileDescriptors) {
			if(fileDescriptor.name.equals(name)) {
				 return fileDescriptor;
			}
		}
		return null;
	}
	
	@Override
	public void clear() {		
		//mode = Mode.SELECT;
		enabled = false;
		editor.getEntities().clear();
		editor.updateEntityPanel();
		memory = null;
	}

	public static class FileDescriptor {
				
		public String name;
		public String path;
		
		@Override
		public String toString() {
			return name;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof FileDescriptor) {
				FileDescriptor fileDescriptor = (FileDescriptor) obj;
				return name.equals(fileDescriptor.name) && path.equals(fileDescriptor.path);
			}
			return false;
		}
				
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}
		
}
