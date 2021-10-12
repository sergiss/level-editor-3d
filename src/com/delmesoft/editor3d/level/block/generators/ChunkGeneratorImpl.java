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
package com.delmesoft.editor3d.level.block.generators;

import static com.delmesoft.editor3d.level.block.Plot.BIT_OFFSET;

import com.delmesoft.editor3d.graphics.MeshBuilder;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.level.block.utils.MeshManager;
import com.delmesoft.editor3d.level.block.utils.MeshManagerImpl;

import com.badlogic.gdx.utils.Array;

public class ChunkGeneratorImpl implements ChunkGenerator {
		
	private Level level;

	private Settings settings;
	
	private int x;
	private int z;
	
	private MeshManager meshManager;
	private MeshBuilder meshBuilder1;
	private MeshBuilder meshBuilder2;

	public ChunkGeneratorImpl(Level level) {
		this.level = level;
		settings = level.getSettings();
		
		meshManager = new MeshManagerImpl(level.getTiledTexture());
		meshBuilder1 = new MeshBuilder(MeshManager.VERTEX_ATTRIBUTES);
		meshBuilder2 = new MeshBuilder(MeshManager.VERTEX_ATTRIBUTES);
	}
	
	@Override
	public void update(int x, int z, boolean forceUpdate) {

		if(this.x != x || this.z != z || forceUpdate) {
			long time = System.currentTimeMillis();
			int count = 0;
			this.x = x;
			this.z = z;
			
			int minX = Math.max(x - settings.chunkVisibility, 0);
			int minZ = Math.max(z - settings.chunkVisibility, 0);
			int maxX = Math.min(x + settings.chunkVisibility, settings.rWidth - 1);
			int maxZ = Math.min(z + settings.chunkVisibility, settings.rDepth - 1);
			
			Chunk chunk;					
			Array<Chunk> chunks = level.getRenderChunks();
			for(int i = 0; i < chunks.size; ++i) {
				chunk = chunks.get(i);
				if(chunk.getLocalX() < minX || chunk.getLocalX() > maxX || chunk.getLocalZ() < minZ || chunk.getLocalZ() > maxZ) {
					chunk.dispose(); // list PostRunnable
					chunk.setNeedUpdate(true);
				}
			}
			chunks.clear(); // Render chunks
									
			for (int cx = minX; cx <= maxX; ++cx) {
				for (int cz = minZ; cz <= maxZ; ++cz) {
					chunk = level.getChunkRelative(cx, cz);
					if(chunk.isNeedUpdate()) {
						chunk.dispose(); // ensure
						chunk.updateHeights();
						for(int j = chunk.getMaxHeight() >> BIT_OFFSET; j > -1; --j) {
							generateMesh(chunk.getPlots()[j]);
						}
						chunk.setNeedUpdate(false);
						count++;
					}
					chunks.add(chunk);
				}
			}
			System.out.printf("Generation time (%d): %d ms.\n", count, (System.currentTimeMillis() - time));
		}
		
	}
		
	private void generateMesh(Plot plot) {
		
		Chunk chunk = plot.getChunk();
				
		ChunkData chunkData = chunk.getChunkData();
		boolean forceDraw;
		int index;
		int minY = Math.max(1, plot.getWorldY());
		int maxY = plot.getWorldY() + Plot.SIZE - 1;
		int height;
		
		for(int x = 0, wx = chunk.getWorldX(); x < Plot.SIZE; ++x, ++wx) {
			for(int z = 0, wz = chunk.getWorldZ(); z < Plot.SIZE; ++z, ++wz) {
				height = chunkData.getHeight(x, z) + 1;
				for(int y = minY; y <= Math.min(maxY, height); ++y) {
					
					index = chunkData.positionToIndex(x, y, z);
					
					if(chunkData.hasFaces(index)) {
						
						forceDraw = chunkData.isForceDraw(index);
						meshManager.initialize(chunkData, index, wx, y, wz, forceDraw);															
						// TOP
						if (forceDraw || chunk.isFaceEnabled(wx, y + 1, wz, Side.BOTTOM.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.TOP.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createTop();
						}

						// BOTTOM
						if (forceDraw || chunk.isFaceEnabled(wx, y - 1, wz, Side.TOP.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.BOTTOM.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createBottom();
						}

						// FRONT
						if (forceDraw || chunk.isFaceEnabled(wx, y, wz + 1, Side.BACK.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.FRONT.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createFront();
						}

						// BACK
						if (forceDraw || chunk.isFaceEnabled(wx, y, wz - 1, Side.FRONT.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.BACK.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createBack();
						}

						// RIGHT
						if (forceDraw || chunk.isFaceEnabled(wx + 1, y, wz, Side.LEFT.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.RIGHT.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createRight();
						}

						// LEFT
						if (forceDraw || chunk.isFaceEnabled(wx - 1, y, wz, Side.RIGHT.ordinal()) == false) {
							if (chunkData.isFaceBlending(index, Side.LEFT.ordinal())) {
								meshManager.setMeshBuilder(meshBuilder2);
							} else {
								meshManager.setMeshBuilder(meshBuilder1);
							}
							meshManager.createLeft();
						}

					}
					
				}
				
			}
		}
		
		if(meshBuilder1.iOff > 0) {
			plot.opaqueMesh = meshBuilder1.end(true);
		}
		
		if(meshBuilder2.iOff > 0) {
			plot.blendedMesh = meshBuilder2.end(true);
		}
				
	}

}
