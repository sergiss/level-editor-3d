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
package com.delmesoft.editor3d.utils;

import static com.delmesoft.editor3d.level.block.Plot.SIZE2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.editor.tool.EntityTool.FileDescriptor;
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
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.zone.Zone;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LevelIO {

	public static final String END_SECTION = String.valueOf((char) 3);
	public static final String SEPARATOR1 = ",";
	public static final String SEPARATOR2 = ";";

	public static void loadLevel(File file, Presenter presenter) throws Exception {

		long start = System.currentTimeMillis();

		try (FileInputStream in = new FileInputStream(file);
			 BufferedReader br = new BufferedReader(new InputStreamReader(new InflaterInputStream(in)))) {

			String line = br.readLine();

			String[] values = Utils.uncapsulateString(SEPARATOR1, line);

			Settings settings = new Settings();

			settings.initialize(values[0], Integer.valueOf(values[1]), Integer.valueOf(values[2]), Integer.valueOf(values[3]));
			settings.texturePath = values[4];
			settings.tileWidth  = Integer.valueOf(values[5]);
			settings.tileHeight = Integer.valueOf(values[6]);
			settings.margin     = Integer.valueOf(values[7]);
			settings.spacing    = Integer.valueOf(values[8]);
			
			// System.out.println("Tile Map Info: " + settings.getTileMapInfo());
			
			presenter.initialize(settings);
			
			// Environment
			Level level = presenter.getLevel();
			Camera camera = level.getCamera();
			
			line = br.readLine();
			String[] split = Utils.uncapsulateString(SEPARATOR1, line);
			camera.position.set (Float.valueOf(split[0]), Float.valueOf(split[1]), Float.valueOf(split[2]));
			camera.direction.set(Float.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
						
			line = br.readLine();
			values = Utils.uncapsulateString(SEPARATOR1, line);
			level.getSkybox().loadTextureAtlas("null".equals(values[0]) ? null : values[0]);
		
			for(Side side : Side.values()) {
				String name = values[side.ordinal() + 1];
				level.getSkybox().setRegion(side, "null".equals(name) ? null : name);
			}
						
			line = br.readLine();			
			values = Utils.uncapsulateString(SEPARATOR1, line);
			level.getAmbientLightColor().set(Float.valueOf(values[0]), Float.valueOf(values[1]), Float.valueOf(values[2]), 1F);
			
			// Point lights
			PointLight pointLight;
			while(END_SECTION.equals(line = br.readLine()) == false) {				
				values = Utils.uncapsulateString(SEPARATOR1, line);
				pointLight = new PointLight();
				pointLight.setName(values[0]);
				pointLight.light.intensity = Float.valueOf(values[1]);
				pointLight.light.color.set(Float.valueOf(values[2]), Float.valueOf(values[3]), Float.valueOf(values[4]), 1F);
				pointLight.light.position.set(Float.valueOf(values[5]), Float.valueOf(values[6]), Float.valueOf(values[7]));
				level.addPointLight(pointLight);
			}
			
			// Chunks
			ChunkData chunkData;
			Chunk chunk;
			int index, x, z;
			while(END_SECTION.equals(line = br.readLine()) == false) {

				values = Utils.uncapsulateString(SEPARATOR1, line);
				// Chunk info
				x = Integer.valueOf(values[0]);
				z = Integer.valueOf(values[1]);

				chunk = level.getChunkRelative(x, z);
				chunkData = chunk.getChunkData();

				// Chunk data
				line = br.readLine();
				values = line.split(SEPARATOR2);
				for(String v : values) {
					String[] tmp = v.split(SEPARATOR1);
					index = Integer.valueOf(tmp[0]);					
					chunkData.getData()[index    ] = Long.valueOf(tmp[1]);
					chunkData.getData()[index + 1] = Long.valueOf(tmp[2]);
				/*	int type = chunkData.getGeometryType(index >> 1);
					if(type > 13) {
						chunkData.setGeometryType(index >> 1, type + 4);
					}*/
				}
				
			}
			
			// Entities
			Vector3 translation = new Vector3();
			Vector3 scale       = new Vector3();
			Quaternion rotation = new Quaternion();
			
			Array<FileDescriptor> fileDescriptors = presenter.getEditor().getEntityTool().getFileDescriptors();
			fileDescriptors.clear();
			FileDescriptor fileDescriptor;
			while(END_SECTION.equals(line = br.readLine()) == false) {
				values = Utils.uncapsulateString(SEPARATOR1, line);
				fileDescriptor = presenter.getEditor().getEntityTool().getFileDescriptorByName(values[1]);
				if(fileDescriptor == null) {
					fileDescriptor = new FileDescriptor();
					fileDescriptor.name = values[1];
					fileDescriptor.path = values[2];
					fileDescriptors.add(fileDescriptor);
				}
				
				Model model = FileManager.getInstance().get(fileDescriptor.path, Model.class);
				EntityAnimation entityAnimation = new EntityAnimation(model, level);
				entityAnimation.setName(values[0]);
				entityAnimation.getProperties().put(Constants.FILE_DESCRIPTOR, fileDescriptor);
				translation.set(Float.valueOf(values[3]), Float.valueOf(values[4]), Float.valueOf(values[5]));
				rotation.setEulerAnglesRad(Float.valueOf(values[6]), Float.valueOf(values[7]), Float.valueOf(values[8]));
				scale.set(Float.valueOf(values[9]), Float.valueOf(values[10]), Float.valueOf(values[11]));
				
				entityAnimation.getTransform().setToTranslationAndScaling(translation, scale);
				
				entityAnimation.getTransform().rotate(rotation);
				level.addEntity(entityAnimation);
			}
			
			fileDescriptors = presenter.getEditor().getDecalTool().getFileDescriptors();
			fileDescriptors.clear();
			String[] tmp;
			// Decals
			while(END_SECTION.equals(line = br.readLine()) == false) {
				values = Utils.uncapsulateString(SEPARATOR1, line);
				tmp = Utils.uncapsulateString(SEPARATOR2, values[0]);
				fileDescriptor = presenter.getEditor().getDecalTool().getFileDescriptorByName(values[1]);
				if(fileDescriptor == null) {
					fileDescriptor = new FileDescriptor();
					fileDescriptor.name = values[1];
					fileDescriptor.path = values[2];
					fileDescriptors.add(fileDescriptor);
				}
				TextureAtlas textureAtlas = FileManager.getInstance().get(fileDescriptor.path, TextureAtlas.class);
				AtlasRegion region = textureAtlas.findRegion(tmp[1]);
				Decal decal = new Decal(new TextureArea(region));
				decal.getProperties().put(Constants.REGION_NAME, region.name);
				decal.setName(tmp[0]);
				decal.position.set(Float.valueOf(values[3]), Float.valueOf(values[4]), Float.valueOf(values[5]));
				decal.getProperties().put(Constants.FILE_DESCRIPTOR, fileDescriptor);
				decal.halfWidth  = Float.valueOf(values[6]);
				decal.halfHeight = Float.valueOf(values[7]);
				level.addDecal(decal);
			}
			
			// Zones
			while(END_SECTION.equals(line = br.readLine()) == false) {
				values = Utils.uncapsulateString(SEPARATOR1, line);
				Zone zone = new Zone();
				zone.setName(values[0]);
				zone.getProperties().put(Constants.PARAMS, values[1]);				
				zone.position.set(Float.valueOf(values[2]), Float.valueOf(values[3]), Float.valueOf(values[4]));
				zone.dimensions.set(Float.valueOf(values[5]), Float.valueOf(values[6]), Float.valueOf(values[7]));	
				level.addZone(zone);
			}
						
		}
			
		System.out.println("Load time: " + (System.currentTimeMillis() - start));			

	}
	
	public static void saveLevel(File file, Presenter presenter, boolean optimize) throws Exception {

		long start = System.currentTimeMillis();

		Level level = presenter.getLevel();

		Settings settings = level.getSettings();

		try(FileOutputStream out = new FileOutputStream(file);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new DeflaterOutputStream(out, true)))) {

			String line = Utils.encapsulateString(SEPARATOR1, settings.levelName, settings.rWidth, settings.rHeight, settings.rDepth, settings.texturePath, settings.tileWidth, settings.tileHeight, settings.margin, settings.spacing);

			// Level settings ************************************************
			writer.write(line);
			writer.newLine();
			
			// Camera ********************************************
			Camera camera = presenter.getLevel().getCamera();
			line = Utils.encapsulateString(SEPARATOR1, camera.position.x, camera.position.y, camera.position.z, camera.direction.x, camera.direction.y, camera.direction.z);
			
			writer.write(line);
			writer.newLine();
						
			// Environment ************************************************
			// Skybox
			String textureAtlasPath = level.getSkybox().getTextureAtlasPath();
			String[] regionNames = level.getSkybox().getAtlasRegionNames(Side.values());			
			line = Utils.encapsulateString(SEPARATOR1, textureAtlasPath, regionNames[0], regionNames[1], regionNames[2], regionNames[3], regionNames[4], regionNames[5]);
			writer.write(line);
			writer.newLine();
			// Ambient Light
			Color ambientLight = level.getAmbientLightColor();
			line = Utils.encapsulateString(SEPARATOR1, ambientLight.r, ambientLight.g, ambientLight.b);
			writer.write(line);
			writer.newLine();
			// Point Lights
			Array<PointLight> pointLights = level.getPointLights();
			for(PointLight pointLight : pointLights) {
				writer.append(Utils.encapsulateString(SEPARATOR1, pointLight.getName(), pointLight.light.intensity, pointLight.light.color.r, pointLight.light.color.g, pointLight.light.color.b, pointLight.light.position.x, pointLight.light.position.y, pointLight.light.position.z));
				writer.newLine();				
			}
								
			writer.append(END_SECTION);
			writer.newLine();	
			
			StringBuilder sb = new StringBuilder();
			int j = SIZE2 << 1;					
			// Chunks ************************************************
			ChunkData chunkData;
			Chunk chunk;
			for (int x = 0, index; x < settings.rWidth; ++x) {
				for (int z = 0; z < settings.rDepth; ++z) {
					chunk = level.getChunkRelative(x, z);
					chunkData = chunk.getChunkData();

					long[] data = chunkData.getData();
					int n = data.length;
					for (int i = j; i < n; i += 2) {
						index = i >> 1;
						if(chunkData.hasFaces(index) /* && chunk.isBlockSurrounded(index) == false*/) {			
							sb.append(i).append(SEPARATOR1).append(data[i]).append(SEPARATOR1).append(data[i + 1]).append(SEPARATOR2);
						}
					}		
					if(sb.length() > 0) {
						// Chunk info
						writer.write(Utils.encapsulateString(SEPARATOR1, x, z));
						writer.newLine();
						// Chunk data
						writer.write(sb.toString());
						writer.newLine();
						sb.setLength(0);	
					}
				}
			}
			
			writer.append(END_SECTION);
			writer.newLine();
			
			// Entities ************************************************
			Array<Entity> entitites = level.getEntities();
			
			Vector3 translation = new Vector3();
			Quaternion rotation = new Quaternion();
			Vector3 scale       = new Vector3();
			
			FileDescriptor fileDescriptor;
			for(Entity entity : entitites) {
				fileDescriptor = (FileDescriptor) entity.getProperties().get(Constants.FILE_DESCRIPTOR);
				entity.getTransform().getTranslation(translation);
				entity.getTransform().getRotation(rotation, true);
				entity.getTransform().getScale(scale);
				writer.append(Utils.encapsulateString(SEPARATOR1, entity.getName(), fileDescriptor.name, fileDescriptor.path, translation.x, translation.y, translation.z, rotation.getYawRad(), rotation.getPitchRad(), rotation.getRollRad(), scale.x, scale.y, scale.z));
				writer.newLine();
			}
						
			writer.append(END_SECTION);
			writer.newLine();
			
			// Decals ************************************************
			Array<Decal> decals = level.getDecals();

			for(Decal decal : decals) {
				fileDescriptor = (FileDescriptor) decal.getProperties().get(Constants.FILE_DESCRIPTOR);
				writer.append(Utils.encapsulateString(SEPARATOR1, Utils.encapsulateString(SEPARATOR2, decal.getName(), decal.textureArea.userObject), fileDescriptor.name, fileDescriptor.path, decal.position.x, decal.position.y, decal.position.z, decal.halfWidth, decal.halfHeight));
				writer.newLine();
			}
			
			writer.append(END_SECTION);
			writer.newLine();
			
			// Zones ************************************************
			Array<Zone> zones = level.getZones();
			
			for(Zone zone : zones) {
				String params = (String) zone.getProperties().get(Constants.PARAMS);
				writer.append(Utils.encapsulateString(SEPARATOR1, zone.getName(), params, zone.position.x, zone.position.y, zone.position.z, zone.dimensions.x, zone.dimensions.y, zone.dimensions.z));
				writer.newLine();
			}
			
			writer.append(END_SECTION);
		}
		
		System.out.println("Save time: " + (System.currentTimeMillis() - start));

	}
	
	/* Testing.... */
	public static void importGTA2Level(File file, Presenter presenter) throws Exception {
		
		long start = System.currentTimeMillis();
				
		Settings settings = new Settings();
		settings.initialize("", 16, 1, 16);
		settings.texturePath = ".\tiles.png";
		settings.tileWidth  = 64;
		settings.tileHeight = 64;
		settings.margin     = 0;
		settings.spacing    = 0;
		
		System.out.println("Tile Map Info: " + settings.getTileMapInfo());
		
		presenter.initialize(settings);
		
		try(FileInputStream in  = new FileInputStream(file);
			LittleEndianInputStream leis = new LittleEndianInputStream(in);) {
					
			String fileType = leis.readString(4);  // Char[4]		
			//System.out.println(fileType);
			
			int versionCode = leis.readUnsignedShort(); // UInt16
			//System.out.println(versionCode);
					
			String chunkType;
			int chunkSize;
			int i, j, d;
			while ((chunkType = leis.readString(4)) != null) { // Char[4]

				System.out.println(chunkType);

				chunkSize = leis.readInt(); // UInt32
				// System.out.println(chunkSize);

				switch(chunkType) {
				case "UMAP": {

					BlockInfo cs = new BlockInfo();

					for(int z = 0; z < 8; ++z) {
						for (int x = 0; x < 256; ++x) {
							for (int y = 255; y > -1; --y) {

								cs.left   = leis.readUnsignedShort();
								cs.right  = leis.readUnsignedShort();
								cs.top    = leis.readUnsignedShort();
								cs.bottom = leis.readUnsignedShort();
								cs.lid    = leis.readUnsignedShort();

								cs.arrows    = leis.readByte() & 0xFF;
								cs.slopeType = leis.readByte() & 0XFF;
															

								if(x > 0 && x < 255 && y > 0 && y < 255 && z > 0 && z < 7) {

									Chunk chunk = presenter.getLevel().getChunkAbsolute(x, y);
									if(chunk != null) {

										int cx = x - chunk.getWorldX();
										int cz = y - chunk.getWorldZ();
										int index = chunk.getChunkData().positionToIndex(cx, z, cz);

										for(i = 0; i < 5; ++i) {

											j = 0; d = 0;

											switch(i) {
											case 0:
												j = Side.FRONT.ordinal();
												d = cs.left;
												break;
											case 1:
												j = Side.BACK.ordinal();
												d = cs.right;
												break;
											case 2:
												j = Side.LEFT.ordinal();
												d = cs.top;
												break;
											case 3:
												j = Side.RIGHT.ordinal();
												d = cs.bottom;
												break;
											case 4:
												j = Side.TOP.ordinal();
												d = cs.lid;
												break;
											}

											int ti = d & 0x3FF;
											
											int ty = ti / 4;
											int tx = (ti % 4) + (ty / 32) * 4;
											ty %= 32;
											
											ti = tx * 32 + ty;

											if (ti > 0 && ti < 992) {
												// System.out.println(ti);
												chunk.getChunkData().setTextureIndex(index, j, ti);
												
												chunk.getChunkData().setFaceEnabled(index, j, true);
												chunk.getChunkData().setFaceBlending(index, j, true);
																			
												chunk.getChunkData().setFaceFlippedVertically(index, j, ((d & 0x1000) >> 12) == 1);
												chunk.getChunkData().setFaceFlippedHorizontally(index, j, false);
												chunk.getChunkData().setTextureAngle(index, j, (((d & 0x6000) >> 13)) % 3);		
																								
											} else {
												chunk.getChunkData().setFaceEnabled(index, j, false);
												/*if (d != 0 && j == 0) {
													System.err.println(d);
													System.out.println(d & 0x3FF);
												}*/
											}

										}

									}		

								}

							}

						}
					}
					
				} // UMAP
					break;
				default:
					for (i = 0; i < chunkSize; ++i) {
						leis.readByte(); // consume
					}
					break;
				}
				
			}
			
		}
		
		System.out.println("Load time: " + (System.currentTimeMillis() - start));
		
	}
	
	private static final class BlockInfo {
		
		public int left, right, top, bottom, lid; // UInt16
		public int arrows;     // UInt8
		public int slopeType;  // UInt8
		
		@Override
		public String toString() {
			return "BlockInfo [left=" + left + ", right=" + right + ", top=" + top + ", bottom=" + bottom + ", lid=" + lid + ", arrows=" + arrows + ", slopeType=" + slopeType + "]";
		}
		
	}
	
}
