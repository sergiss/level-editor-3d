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

import static com.delmesoft.editor3d.level.block.ChunkData.MAX_VALUE_4_BITS;
import static com.delmesoft.editor3d.level.block.Plot.BIT_OFFSET;
import static com.delmesoft.editor3d.level.block.Plot.BIT_OFFSET2;
import static com.delmesoft.editor3d.level.block.Plot.SIZE;

import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.level.block.ChunkData;
import com.delmesoft.editor3d.level.block.Side;
import com.delmesoft.editor3d.level.block.utils.DataPoint;
import com.delmesoft.editor3d.level.block.utils.DataPointArray;

public class ChunkLightGeneratorImpl implements ChunkLightGenerator {

	private Level level;
	private Settings settings;

	public ChunkLightGeneratorImpl(Level level) {
		this.level = level;
		this.settings = level.getSettings();
	}

	@Override
	public void generateSkyLight(Chunk chunk) {

		ChunkData chunkData = chunk.getChunkData();
		
		int maxY = settings.aHeight - 1;
		int minY;
		int index;
		for (int x = 0; x < SIZE; ++x) {
			for (int z = 0; z < SIZE; ++z) {
				minY = chunkData.getHeight(x, z);
				for (int y = maxY; y > minY; --y) {
					index = chunkData.positionToIndex(x, y, z);
					chunkData.setSkyLight(index, MAX_VALUE_4_BITS); // set light
					chunkData.setBlockLight(index, 0); // clear
				}
				for (int y = minY; y > 0; --y) {
					index = chunkData.positionToIndex(x, y, z);
					chunkData.setSkyLight(index, 0); // clear
					chunkData.setBlockLight(index, 0); // clear
				}
			}
		}		
		
	}
	
	@Override
	public void spreadSkyLight(Chunk chunk) {

		DataPointArray stack = new DataPointArray();

		Side side;

		Chunk c1, c2;

		int height, maxHeight;

		int minX = Math.max(chunk.getWorldX() - 1, 0);
		int maxX = Math.min(minX + SIZE, settings.aWidth - 1);
		int minZ = Math.max(chunk.getWorldZ() - 1, 0);
		int maxZ = Math.min(minZ + SIZE, settings.aDepth - 1);

		int x1, z1, x2, z2, wx, wz;
		for (wx = minX; wx <= maxX; ++wx) {
			for (wz = minZ; wz <= maxZ; ++wz) {

				c1 = level.getChunkAbsolute(wx, wz);

				x1 = wx - c1.getWorldX();
				z1 = wz - c1.getWorldZ();

				height = c1.getChunkData().getHeight(x1, z1);
				int light = MAX_VALUE_4_BITS - c1.getChunkData().getOpacity(x1, height, z1);
				if (light == 0) {

					maxHeight = height;

					for (int i = 2; i < 6; ++i) {
						side = Side.values()[i];
						x2 = wx + side.x;
						z2 = wz + side.z;
						c2 = level.getChunkAbsolute(x2, z2);
						if (c2 != null) {
							int tmp = c2.getChunkData().getHeight(x2 - c2.getWorldX(), z2 - c2.getWorldZ());
							if (tmp > maxHeight) {
								maxHeight = tmp;
							}
						}
					}

					if (c1 == chunk) {

						for (int y = height + 1; y < maxHeight; ++y) {
							spreadSkyLight(wx, y, wz, MAX_VALUE_4_BITS, stack);
						}

					} else {

						for (int y = maxHeight; y > 1; --y) {
							light = c1.getChunkData().getSkyLight(x1, y, z1);
							if (light > 0) {
								spreadSkyLight(wx, y, wz, light, stack);
							}
						}

					}

				} else {
					c1.getChunkData().setSkyLight(x1, height, z1, (byte) light);
					spreadSkyLight(wx, height, wz, light, stack);
				}

			}

		}

	}

	private void spreadSkyLight(int x, int y, int z, int light, DataPointArray stack) {
    	
		stack.add(new DataPoint(x, y, z, light));

		int newLight, currentLight;
	
		Chunk chunk;
		Chunk nChunk;

		int index;

		int yOffset;
		int cx, cz, ix, iz;
		
		DataPoint lPoint;
		
		do {

			lPoint = stack.pop();

			light = lPoint.data;

			x = lPoint.x;
			y = lPoint.y;
			z = lPoint.z;

			yOffset = y << BIT_OFFSET2;
			cx = x >> BIT_OFFSET;
			cz = z >> BIT_OFFSET;

			chunk = level.getChunkRelative(cx, cz);
			
			ix = (x - chunk.getWorldX()) << BIT_OFFSET;
			iz = z - chunk.getWorldZ();

			nChunk = level.getChunkRelative((x + 1) >> BIT_OFFSET, cz);
			if (nChunk != null) {
				index = (((x + 1) - nChunk.getWorldX()) << BIT_OFFSET) + iz + yOffset;
				currentLight = nChunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setSkyLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x + 1, y, z, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative((x - 1) >> BIT_OFFSET, cz);
			if (nChunk != null) {
				index = (((x - 1) - nChunk.getWorldX()) << BIT_OFFSET) + iz + yOffset;
				currentLight = nChunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setSkyLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x - 1, y, z, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative(cx, (z + 1) >> BIT_OFFSET);
			if (nChunk != null) {
				index = ix + ((z + 1) - nChunk.getWorldZ()) + yOffset;
				currentLight = nChunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setSkyLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x, y, z + 1, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative(cx, (z - 1) >> BIT_OFFSET);
			if (nChunk != null) {
				index = ix + ((z - 1) - nChunk.getWorldZ()) + yOffset;
				currentLight = nChunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setSkyLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x, y, z - 1, newLight));
					}
				}
			}

			if (y < settings.aHeight - 1) {
				index = ix + iz + ((y + 1) << BIT_OFFSET2);
				currentLight = chunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {					
					newLight = light - chunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						chunk.getChunkData().setSkyLight(index, newLight);
						if (newLight > 1)
							stack.add(new DataPoint(x, y + 1, z, newLight));
					}
				}
			}

			if (y > 1) {
				index = ix + iz + ((y - 1) << BIT_OFFSET2);
				currentLight = chunk.getChunkData().getSkyLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {					
					newLight = light - chunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						chunk.getChunkData().setSkyLight(index, newLight);
						if (newLight > 1)
							stack.add(new DataPoint(x, y - 1, z, newLight));
					}
				}
			}
			
		} while (stack.size > 0);

	}

/*	@Override
	public void spreadBlockLight(int x, int y, int z, int light, DataPointArray stack) {
		
		stack.add(new DataPoint(x, y, z, light));

		int newLight, currentLight;
	
		Chunk chunk;
		Chunk nChunk;

		int index;

		int yOffset;
		int cx, cz, ix, iz;
		
		DataPoint lPoint;
		
		do {

			lPoint = stack.pop();

			light = lPoint.data;

			x = lPoint.x;
			y = lPoint.y;
			z = lPoint.z;

			yOffset = y << BIT_OFFSET2;
			cx = x >> BIT_OFFSET;
			cz = z >> BIT_OFFSET;

			chunk = level.getChunkRelative(cx, cz);
			
			ix = (x - chunk.getWorldX()) << BIT_OFFSET;
			iz = z - chunk.getWorldZ();

			nChunk = level.getChunkRelative((x + 1) >> BIT_OFFSET, cz);
			if (nChunk != null) {
				index = (((x + 1) - nChunk.getWorldX()) << BIT_OFFSET) + iz + yOffset;
				currentLight = nChunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setBlockLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x + 1, y, z, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative((x - 1) >> BIT_OFFSET, cz);
			if (nChunk != null) {
				index = (((x - 1) - nChunk.getWorldX()) << BIT_OFFSET) + iz + yOffset;
				currentLight = nChunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setBlockLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x - 1, y, z, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative(cx, (z + 1) >> BIT_OFFSET);
			if (nChunk != null) {
				index = ix + ((z + 1) - nChunk.getWorldZ()) + yOffset;
				currentLight = nChunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setBlockLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x, y, z + 1, newLight));
					}
				}
			}

			nChunk = level.getChunkRelative(cx, (z - 1) >> BIT_OFFSET);
			if (nChunk != null) {
				index = ix + ((z - 1) - nChunk.getWorldZ()) + yOffset;
				currentLight = nChunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - nChunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						nChunk.getChunkData().setBlockLight(index, newLight);						
						if (newLight > 1)
							stack.add(new DataPoint(x, y, z - 1, newLight));
					}
				}
			}

			if (y < settings.aHeight - 1) {
				index = ix + iz + ((y + 1) << BIT_OFFSET2);
				currentLight = chunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {					
					newLight = light - chunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						chunk.getChunkData().setBlockLight(index, newLight);
						if (newLight > 1)
							stack.add(new DataPoint(x, y + 1, z, newLight));
					}
				}
			}

			if (y > 1) {
				index = ix + iz + ((y - 1) << BIT_OFFSET2);
				currentLight = chunk.getChunkData().getBlockLight(index);
				if (currentLight < MAX_VALUE_4_BITS) {
					newLight = light - chunk.getChunkData().getOpacity(index);
					if (currentLight < newLight) {
						chunk.getChunkData().setBlockLight(index, newLight);
						if (newLight > 1)
							stack.add(new DataPoint(x, y - 1, z, newLight));
					}
				}
			}
			
		} while (stack.size > 0);
		
	}*/
	
}
