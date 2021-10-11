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

import java.util.Comparator;
import java.util.Iterator;

import com.delmesoft.editor3d.editor.Editor;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.undoredo.changeable.DataChange;
import com.delmesoft.editor3d.utils.BlockIntersector;
import com.delmesoft.editor3d.utils.Vec3i;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class BlockTool extends Tool {
	
	// Special keys: C -> clear empty blocks of selection 

	private boolean enabled;
	
	private Vec3i lastSelection;
	private Vec3i lastPosition; // info
	
	private Array<Vec3i> memory = new Array<Vec3i>();

	public BlockTool(Editor editor) {
		super(editor);
		lastPosition = new Vec3i();
	}

	@Override
	public void update() {
		
		Input input = Gdx.input;
		
		boolean leftButton = input.isButtonPressed(Buttons.LEFT);
		
		if (leftButton) {
			
			if(enabled) {
				enabled = false;

				boolean action1 = input.isKeyPressed(Settings.KEY_ACTION1);
				boolean action2 = input.isKeyPressed(Settings.KEY_ACTION2);

				int i = ((action2 ? 1 : 0) << 1) | (action1 ? 1 : 0);

				updateAction(i);
			} else {
				
				if(editor.getInputProcessor().isDragged()) {
					updateAction(3);
				}
				
			}
			
		} else if (leftButton == false) {
			enabled = true;
			
			if(lastSelection != null) {
				if(input.isKeyJustPressed(Keys.C)) { // copy
					Array<Vec3i> positions = editor.getBlockPositions();
					Iterator<Vec3i> it = positions.iterator();
					Chunk chunk;
					Vec3i position;
					while(it.hasNext()) {
						position = it.next();
						chunk = editor.getLevel().getChunkAbsolute(position.x, position.z);
						if(chunk != null) {
							if(chunk.getChunkData().hasFaces(position.x - chunk.getWorldX(), position.y, position.z - chunk.getWorldZ()) == false) {
								it.remove();
							}
						}
					}
				}
			}
			
		}
		
		Vec3i position = ray(input.getX(), input.getY());
		if(position != null && position.equals(lastPosition) == false) {
			lastPosition.set(position);
			Vector3 direction = editor.getLevel().getCamera().direction;
			Vector3 camPos = editor.getLevel().getCamera().position;
			String[] result = getBlockPropertiesAt(position);
			String cameraInfo = String.format("Camera[Position: x=%.2f, y=%.2f, z=%.2f - Direction: x=%.2f, y=%.2f, z=%.2f]", camPos.x, camPos.y, camPos.z, direction.x, direction.y, direction.z);
			String blockProperties = String.format("Block[x=%d, y=%d, z=%d - Geometry:%s - Side:%s, Texture index:%s]", position.x, position.y, position.z, result[0], result[1], result[2]);
			String chunkInfo = String.format("Chunk[x=%d, y=%d, z=%d]", position.x / Plot.SIZE, position.y / Plot.SIZE, position.z / Plot.SIZE, blockProperties);
			editor.getLevel().getPresenter().getInfoPanel().setText(cameraInfo + "   |   " + chunkInfo + "   |   " + blockProperties);
		}
				
	}

	private void updateAction(int action) {

		Array<Vec3i> positions = editor.getBlockPositions();
		Input input = Gdx.input;

		switch (action) {
		case 0: // Select 1 block
			lastSelection = ray(input.getX(), input.getY());
			positions.clear();

			if (lastSelection != null) {
				Side side  = getSide(input.getX(), input.getY(), lastSelection);
				if(side != null) {
					editor.setFaceSelection(side);
				}					
				positions.add(lastSelection);
			}
			
			editor.updateBlockPanel();
			break;
		case 3:
		case 1: { // Update Multiselection

			if (lastSelection != null) {

				Vec3i position = ray(input.getX(), input.getY());
				if (position != null) {

					positions.clear();

					int x1, x2, y1, y2, z1, z2;
					if (lastSelection.x < position.x) {
						x1 = lastSelection.x;
						x2 = position.x;
					} else {
						x1 = position.x;
						x2 = lastSelection.x;
					}

					if (lastSelection.y < position.y) {
						y1 = lastSelection.y;
						y2 = position.y;
					} else {
						y1 = position.y;
						y2 = lastSelection.y;
					}

					if (lastSelection.z < position.z) {
						z1 = lastSelection.z;
						z2 = position.z;
					} else {
						z1 = position.z;
						z2 = lastSelection.z;
					}
					positions.add(lastSelection);
					boolean testX, testY;
					for (int x = x1; x <= x2; ++x) {
						testX = x != lastSelection.x;
						for (int y = y1; y <= y2; ++y) {
							testY = testX || y != lastSelection.y;
							for (int z = z1; z <= z2; ++z) {
								if (testY || z != lastSelection.z) {
									positions.add(new Vec3i(x, y, z));
								}
							}
						}
					}

				}

			} else {
				updateAction(0);
			}

			break;
		} // end case
		case 2: // Add/Remove to selection
			if (positions.size > 0) {
				Vec3i position = ray(input.getX(), input.getY());

				if (position != null) {
					if (positions.contains(position, false)) {
						positions.removeValue(position, false);
						if (positions.size == 0) {
							editor.updateBlockPanel();
						}
					} else {
						positions.add(position);
					}
				}

			} else {
				updateAction(0);
			}
			break;
		}

	}

	private Side getSide(int x, int y, Vec3i position) {
		Level level = editor.getLevel();
		Camera camera = level.getCamera();
		Ray ray = camera.getPickRay(x, y);
		Side side = BlockIntersector.intersectRayBounds(ray.origin, ray.direction, position.x, position.y - 1, position.z);
		return side;
	}

	public Vec3i ray(final float x, final float y) { // WORLD RAY
		
		Level level = editor.getLevel();
		Camera camera = level.getCamera();
		Ray ray = camera.getPickRay(x, y);
		
		Vec3i result = new Vec3i();
		boolean hit = level.ray(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET, result);
		
		BoundingBox gridBounds = editor.getGridBoundingBox();

		if (hit) {

			if (camera.position.y > (int) gridBounds.getCenterY()) {
				if (result.y > gridBounds.getCenterY()) {
					return result;
				}
			} else {
				if (result.y <= (int) gridBounds.getCenterY()) {
					return result;
				}
			}

		}

		if (Intersector.intersectRayBounds(ray, gridBounds, tmp)) {
			result.set((int) tmp.x, (int) tmp.y, (int) tmp.z);
			return result;
		}
		
		return null;
	}
	
	public String[] getBlockPropertiesAt(Vec3i position) {
		
		String[] result = new String[3];
		Level level = editor.getLevel();
		Chunk chunk = level.getChunkAbsolute(position.x, position.z);
		if(chunk != null) {
			int index  = chunk.getChunkData().positionToIndex(position.x - chunk.getWorldX(), position.y, position.z - chunk.getWorldZ());
			result[0] = String.valueOf(chunk.getChunkData().getGeometryType(index));
			//result[1] = String.valueOf(chunk.getChunkData().getOpacity(index));
			//result[2] = String.valueOf(chunk.getChunkData().getSkyLight(index));
			//result[3] = String.valueOf(chunk.getChunkData().getBlockLight(index));
						
			final Ray ray = level.getCamera().getPickRay(Gdx.input.getX(), Gdx.input.getY());			
			Side side = BlockIntersector.intersectRayBounds(ray.origin.x, ray.origin.y, ray.origin.z, ray.direction.x, ray.direction.y, ray.direction.z, position.x, position.y - 1, position.z);
			if(side != null) {
				
				result[1] = side.name();
				result[2] = String.valueOf(chunk.getChunkData().getTextureIndex(index, side.ordinal()));
				
				/*int x = position.x + side.x;
				int y = position.y + side.y;
				int z = position.z + side.z;
				
				chunk = level.getChunkAbsolute(x, z);
				
				if(chunk != null) {
					index  = chunk.getChunkData().positionToIndex(x - chunk.getWorldX(), y, z - chunk.getWorldZ());
					result[6] = String.valueOf(chunk.getChunkData().getSkyLight(index));
					result[7] = String.valueOf(chunk.getChunkData().getBlockLight(index));
				}*/
				
			}
			
		}
		
		return result;
	}

	@Override
	public void copy() {

		memory.clear();
		if (editor.getBlockPositions().size > 0) {
			memory.addAll(editor.getBlockPositions());
			memory.sort(new Comparator<Vec3i>() {
				@Override
				public int compare(Vec3i o1, Vec3i o2) {
					return Integer.compare(o1.len2(), o2.len2());
				}
			});
		}

	}

	@Override
	public void paste() {
		
		if(memory.size > 0 && lastSelection != null) {
			
			editor.getUndoRedo().newChange();
			Vec3i origin = lastSelection;
			Vec3i minPosition = memory.get(0);
			
			Chunk c1, c2;
			ChunkData chunkData1, chunkData2;
			int index1, index2;
			int x, y, z;
			for(Vec3i position : memory) {

				x = origin.x + (position.x - minPosition.x);
				y = origin.y + (position.y - minPosition.y);
				z = origin.z + (position.z - minPosition.z);

				if(y < editor.getLevel().getSettings().aHeight) {

					c1 = editor.getLevel().getChunkAbsolute(x, z);
					c2 = editor.getLevel().getChunkAbsolute(position.x, position.z);			

					if(c1 != null && c2 != null) {

						chunkData1 = c1.getChunkData();

						x -= c1.getWorldX();
						z -= c1.getWorldZ();

						index1 = chunkData1.positionToIndex(x, y, z) << 1;
						editor.getUndoRedo().mem(new DataChange(chunkData1, index1));

						chunkData2 = c2.getChunkData();

						index2 = chunkData2.positionToIndex(position.x - c2.getWorldX(), position.y, position.z - c2.getWorldZ()) << 1;

						chunkData1.getData()[index1    ] = chunkData2.getData()[index2    ];
						chunkData1.getData()[index1 + 1] = chunkData2.getData()[index2 + 1];

						x = c1.getLocalX(); z = c1.getLocalZ();
						for(int x1 = x - 1; x1 < x + 2; ++x1) {
							for(int z1 = z - 1; z1 < z + 2; ++z1) {
								c1 = editor.getLevel().getChunkRelative(x1, z1);
								if(c1 != null) {
									c1.setNeedUpdate(true);
								}
							}
						}	

					}

				}
				
			}

			editor.getLevel().forceUpdate();
			editor.updateBlockPanel();
		}
		
	}
		
	@Override
	public void clear() {
		lastSelection = null;
		enabled = false;
		editor.getBlockPositions().clear();
		editor.updateBlockPanel();		
	}

	@Override
	public void setMode(Mode mode) {
		// TODO Auto-generated method stub		
	}
	
}
