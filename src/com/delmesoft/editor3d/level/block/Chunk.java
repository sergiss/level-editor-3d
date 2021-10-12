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
package com.delmesoft.editor3d.level.block;

import static com.delmesoft.editor3d.level.block.Plot.BIT_OFFSET;
import static com.delmesoft.editor3d.level.block.Plot.BIT_OFFSET2;
import static com.delmesoft.editor3d.level.block.Plot.SIZE;

import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;

public class Chunk {
	
	private Level level;

	private ChunkData chunkData;
	
	private Plot[] plots;

	private int localX;
	private int localZ;

	private int worldX;
	private int worldZ;

	private int maxHeight = 1;
	
	private boolean needUpdate;

	public Chunk(Level level) {

		this.level = level;

		chunkData = new ChunkData(this);
		
		int n = level.getSettings().rHeight;
		plots = new Plot[n];
		for(int i = 0; i < n; ++i) {
			plots[i] = new Plot(i, this);
		}		
		
		needUpdate = true;
		
	}
	
	public Chunk initialize(int localX, int localZ) {
		this.localX = localX;
		this.localZ = localZ;
		
		this.worldX = localX * SIZE;
		this.worldZ = localZ * SIZE;		
		return this;
	}

	public Level getLevel() {
		return level;
	}

	public ChunkData getChunkData() {
		return chunkData;
	}
	
	public Plot[] getPlots() {
		return plots;
	}

	public int getLocalX() {
		return localX;
	}

	public int getLocalZ() {
		return localZ;
	}

	public int getWorldX() {
		return worldX;
	}

	public int getWorldZ() {
		return worldZ;
	}
	
	public int getMaxHeight() {
		return maxHeight;
	}
	
	public void updateHeights() {
		maxHeight = 1;

		Settings settings = level.getSettings();
		for (int x = 0; x < Plot.SIZE; ++x) {
			for (int z = 0; z < Plot.SIZE; ++z) {
				for (int y = settings.aHeight - 1; y > 0; --y) {
					if (chunkData.hasFaces(x, y, z)) {				
						chunkData.setHeight(x, z, y);
						if (y > maxHeight) {
							maxHeight = y;
						}
						break;
					}
				}
			}
		}
		
	}
	
	public boolean isBlockSurrounded(int index) {
		
		int x = ((index >> BIT_OFFSET) % SIZE) + worldX;
		int y = (index >> BIT_OFFSET2);
		int z = (index % SIZE) + worldZ;
				
		int i;
		for (Side side : Side.values()) {
			i = y + side.y;
			if (isFaceEnabled(x + side.x, i, z + side.z, side.oposite.ordinal()) == false) { // cara
				return false;
			}
		}
		
		return true;
	}

	public boolean isFaceEnabled(int x, int y, int z, int ordinal) {

		if(y > 0 && y < level.getSettings().aHeight) { // limites superior e inferior
			Chunk chunk = level.getChunkAbsolute(x, z);
			if(chunk != null) {
				ChunkData chunkData = chunk.getChunkData();
				int index = chunkData.positionToIndex(x - chunk.getWorldX(), y, z - chunk.getWorldZ());
				return chunkData.isForceDraw(index) == false && chunkData.isFaceEnabled(index, ordinal);
			}
		} 
		
		return false;
	}

	public void clear() {

		chunkData.clear();
		needUpdate = true;
		for (Plot plot : plots) {
			plot.clear();
		}
		
	}

	public void dispose() {
		for (Plot plot : plots) {
			plot.dispose();
		}
	}
	
	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	@Override
	public String toString() {
		return "Chunk [localX=" + localX + ", localZ=" + localZ + ", worldX=" + worldX + ", worldZ=" + worldZ + "]";
	}

}
