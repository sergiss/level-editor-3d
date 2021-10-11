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

import java.util.HashSet;
import java.util.Set;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.editor.Editor;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.undoredo.changeable.ZoneTransformChange;
import com.delmesoft.editor3d.utils.BlockIntersector;
import com.delmesoft.editor3d.utils.Vec3i;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class ZoneTool extends Tool {

	private Mode mode = Mode.SELECT;
	private boolean enabled;
	private Vector3 moveAxis;
	private float refX;
	
	private boolean drag;

	public ZoneTool(Editor editor) {
		super(editor);
		mode = editor.getLevel().getPresenter().getEnvironmentPanel().getSelectedMode();
	}

	@Override
	public void update() {
		
		Input input = Gdx.input;

		InputProcessorImpl inputProcessor = editor.getInputProcessor();
		boolean leftButton = inputProcessor.isLeftButton();

		boolean keyX = input.isKeyJustPressed(Keys.X);
		boolean keyY = input.isKeyJustPressed(Keys.Y);
		boolean keyZ = input.isKeyJustPressed(Keys.Z);

		if(leftButton == false && (keyX || keyY || keyZ) && editor.getZones().size > 0) {			
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
			editor.getUndoRedo().newChange();
			for(Zone zone : editor.getZones()) {
				editor.getUndoRedo().mem(new ZoneTransformChange(zone));
			}
		} else {
			switch(mode) {
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
						Zone zone = level.getZone(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET);

						if (input.isKeyPressed(Keys.CONTROL_LEFT) == false) {
							editor.getZones().clear();
							if (zone != null) {
								editor.getZones().add(zone);
							}
						} else if (zone != null) {
							int index = editor.getZones().indexOf(zone, true);
							if(index < 0) {
								editor.getZones().add(zone);
							} else {
								editor.getZones().removeIndex(index);
							}
						}
						editor.updateToolPanel();
					}					
					
				} else if(leftButton == false){
					if(drag) {
						drag = false;
						editor.getZones().clear();
						Rectangle selectionArea = editor.getSelectionArea();
												
						float x1 = selectionArea.x;
						float x2 = x1 + selectionArea.width;
						float stepSize = 2F;
						
						float y1 = selectionArea.y;
						float y2 = y1 + selectionArea.height;
						
						Level level = editor.getLevel();
						Camera camera = level.getCamera();
						float h = camera.viewportHeight;
						Set<Zone> zoneSet = new HashSet<>();
						Zone zone;
						Ray ray;
						for(float x = x1; x < x2; x += stepSize) {
							for(float y = y1; y < y2; y += stepSize) {
								ray = camera.getPickRay(x, h - y);
								zone = level.getZone(ray, level.getSettings().chunkVisibility << Plot.BIT_OFFSET);
								if(zone != null) {
									if(zoneSet.contains(zone) == false) {
										zoneSet.add(zone);
										editor.getZones().add(zone);
									}
								}
							}
						}
						editor.updateToolPanel();
						editor.hideSelectionArea();
					}
					enabled = true;
				}

				break;
			case MOVE:

				float x = input.getX() * 0.05F;
				float result = x - refX;

				if (result != 0F) {
					Vector3 tmp = Tool.tmp.set(moveAxis).scl(result);
					Array<Zone> zones = editor.getZones();
					for (Zone zone : zones) {
						zone.position.add(tmp);
					}
					refX = x;
					editor.updateEntityPanel();
				}
				if (leftButton && mode != Mode.SELECT) {
					mode = Mode.SELECT;
					enabled = false;
				}
				break;
			case ADD:
				if (leftButton && enabled) {
					Vector3 position = ray(input.getX(), input.getY());
					if (position != null) {
						editor.addZone(position);
					}
					enabled = false;
				} else if (leftButton == false) {
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

	private Array<Zone> memory;
	@Override
	public void copy() {
		memory = new Array<Zone>(editor.getZones());
	}

	@Override
	public void paste() {
		if(memory != null && memory.size > 0) {
			editor.getUndoRedo().newChange();
			Zone reference = memory.get(0);
			Input input = Gdx.input;
			Vector3 translation = ray(input.getX(), input.getY());
			if(translation != null) {
				Vector3 tmp1 = new Vector3();
				translation.sub(reference.position);
				for(Zone zone : memory) {
					editor.addZone(tmp1.set(zone.position).add(translation), zone.dimensions, (String) zone.getProperties().get(Constants.PARAMS));
				}				
			}
		}
	}

	@Override
	public void clear() {
		//mode = Mode.SELECT;
		enabled = false;
		editor.getPointLights().clear();
		editor.updateEnvironmentPanel();
		memory = null;
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
}
