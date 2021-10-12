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

import static com.delmesoft.editor3d.level.block.Plot.*;

import com.delmesoft.editor3d.level.Settings;

public class ChunkData {
	
	public static final int MAX_Z = Plot.SIZE - 1;
	public static final int MAX_X = MAX_Z << BIT_OFFSET;
	
	public static final int BITS = 124;
	
	public static final int MAX_VALUE_2_BITS = 0x3;
	public static final int MAX_VALUE_4_BITS = 0xF;
	public static final int MAX_VALUE_8_BITS = 0xFF;
	
	public static final int MAX_GEOMETRY = 91;
	
	public static final long CLEAR_VALUE1 = 65536L;
	public static final long CLEAR_VALUE2 = 0L;
	

	// * Block data -----------------------------
	// type     = 8 bits (max. dec. 255) index 1 user
	// Sidewalk = 4 bits (max. dex. 15 ) index 1 user
	// Road     = 4 bits (max. dex. 15 ) index 1 user
	
	// * Light data -----------------------------
	// opacity     = 4 bits (max. dec. 15 ) index 1 user
	// sky light   = 4 bits (max. dec. 15 ) index 1 auto
	// block light = 4 bits (max. dec. 15 ) index 1 auto
	
	// 3 bits free
	
	// * Face data -----------------------------
	// enabled face    = 6 bits  ( 1 bits x 6) (max. dec. 1 )   index 1 auto/user
	// blending        = 6 bits  ( 1 bits x 6) (max. dec. 1 )   index 1 user
	// horizontal flip = 6 bits  ( 1 bits x 6) (max. dec. 1 )   index 1 user
	// vertical flip   = 6 bits  ( 1 bits x 6) (max. dec. 1 )   index 1 user
	// texture angle   = 12 bits ( 2 bits x 6) (max. dec. 3 )   index 1 user
	// texture index   = 60 bits (10 bits x 6) (max. dec. 1023 ) index 2 user
	// force draw      = 1 bit index 2 user

	private long[] data;

	private Chunk chunk;
	private Settings settings;
	private int refX;
	private int refY;
	private int refZ;

	public ChunkData(Chunk chunk) {
		this.chunk = chunk;
		settings = chunk.getLevel().getSettings();
		data = new long[settings.blockCount << 1];
		clear();
	}

	public int positionToIndex(int x, int y, int z) {
		return (x << BIT_OFFSET) + (y << BIT_OFFSET2) + z;
	}

	public void setHeight(int x, int z, int height) {
		setHeight((x << BIT_OFFSET) + z, height);
	}

	public void setHeight(int index, int height) {
		data[index << 1] = height;
	}

	public int getHeight(int x, int z) {
		return getHeight((x << BIT_OFFSET) + z);
	}

	public int getHeight(int index) {
		return (int) (data[index << 1] & 0xFF);
	}

	public void setGeometryType(int x, int y, int z, int type) {
		setGeometryType(positionToIndex(x, y, z), type);
	}

	public int getGeometryType(int x, int y, int z) {
		return getGeometryType(positionToIndex(x, y, z));
	}

	public void setGeometryType(int index, int type) { // 8 bits
		index <<= 1;
		data[index] &= ~0xFF;
		data[index] |= (type & 0xFF);
	}

	public int getGeometryType(int index) {
		return (int) (data[index << 1] & 0xFF);
	}
	
	public void setSidewalkDirection(int index, int direction, boolean enabled) { // 1 bit x 4
		index <<= 1;
		data[index] &= ~(0x100L << direction);
		data[index] |= (((enabled ? 1L : 0L) << direction)) << 8;
	}

	public boolean isSidewalkDirection(int index, int direction) {
		return (((data[index << 1] & (0x100L << direction)) >>> 8)) > 0;
	}
	
	public boolean isSidewalkDirection(int x, int y, int z, int direction) {
		return isSidewalkDirection(positionToIndex(x, y, z), direction);
	}
	
	public void setRoadDirection(int index, int direction, boolean enabled) { // 1 bit x 4
		index <<= 1;
		data[index] &= ~(0x1000L << direction);
		data[index] |= (((enabled ? 1L : 0L) << direction)) << 12;
	}

	public boolean isRoadDirection(int index, int direction) {
		return (((data[index << 1] & (0x1000L << direction)) >>> 12)) > 0;
	}
	
	public boolean isRoadDirection(int x, int y, int z, int direction) {
		return isRoadDirection(positionToIndex(x, y, z), direction);
	}

	public void setOpacity(int x, int y, int z, int opacity) {
		setOpacity(positionToIndex(x, y, z), opacity);
	}

	public int getOpacity(int x, int y, int z) {
		return getOpacity(positionToIndex(x, y, z));
	}

	public void setOpacity(int index, int opacity) { // 4 bits
		index <<= 1;
		data[index] &= ~0xF0000L;
		data[index] |= (opacity & 0xFL) << 16;
	}

	public int getOpacity(int index) {
		return (int) ((data[index << 1] & 0xF0000L) >>> 16);
	}

	public void setSkyLight(int x, int y, int z, int light) {
		setSkyLight(positionToIndex(x, y, z), light);
	}

	public int getSkyLight(int x, int y, int z) {
		return getSkyLight(positionToIndex(x, y, z));
	}

	public void setSkyLight(int index, int light) { // 4 bits
		index <<= 1;
		data[index] &= ~0xF00000L;
		data[index] |= (light & 0xFL) << 20;
	}

	public int getSkyLight(int index) {
		return (int) ((data[index << 1] & 0xF00000L) >>> 20);
	}

	public void setBlockLight(int x, int y, int z, int light) {
		setBlockLight(positionToIndex(x, y, z), light);
	}

	public int getBlockLight(int x, int y, int z) {
		return getBlockLight(positionToIndex(x, y, z));
	}

	public void setBlockLight(int index, int light) { // 4 bits
		index <<= 1;
		data[index] &= ~0xF000000L;
		data[index] |= (light & 0x7FL) << 24;
	}

	public int getBlockLight(int index) {
		return (int) ((data[index << 1] & 0xF000000L) >>> 24);
	}
	
	public int getLight(int x, int y, int z) {
		return getLight(positionToIndex(x, y, z));
	}
	
	public int getLight(int index) {
		return (int) ((data[index << 1] & 0xFF00000L) >>> 20);
	}
	
	public void setLight(int x, int y, int z, byte light) {
		this.setLight(positionToIndex(x, y, z), light);
	}
	
	public void setLight(int index, int light) {
		index <<= 1;
		data[index] &= ~0xFF00000L;
		data[index] |= (light & 0xFFL) << 20;
	}

	public void setFaceEnabled(int x, int y, int z, int face, boolean enabled) {
		setFaceEnabled(positionToIndex(x, y, z), face, enabled);
	}

	public boolean isFaceEnabled(int x, int y, int z, int face) {
		return isFaceEnabled(positionToIndex(x, y, z), face); 
	}
	// TODO : << sumar 3
	public void setFaceEnabled(int index, int face, boolean enabled) { // 1 bit x 6
		index <<= 1;
		data[index] &= ~(0x80000000L << face);
		data[index] |= (((enabled ? 1L : 0L) << face)) << 31;
	}

	public boolean isFaceEnabled(int index, int face) {
		return (((data[index << 1] & (0x80000000L << face)) >>> 31)) > 0;
	}

	public boolean hasFaces(int x, int y, int z) {
		return hasFaces(positionToIndex(x, y, z));
	}

	public boolean hasFaces(int index) {
		return (((data[index << 1] & 0x1F80000000L) >>> 31) & 0x3FL) > 0;
	}

	public void setFaceBlending(int x, int y, int z, int face, boolean blending) {
		setFaceBlending(positionToIndex(x, y, z), face, blending);
	}

	public boolean isFaceBlending(int x, int y, int z, int face) {
		return isFaceBlending(positionToIndex(x, y, z), face);
	}

	public void setFaceBlending(int index, int face, boolean blending) { // 1 bit x 6
		index <<= 1;
		data[index] &= ~(0x2000000000L << face);
		data[index] |= (((blending ? 1L : 0L) << face)) << 37;
	}

	public boolean isFaceBlending(int index, int face) {
		return (((data[index << 1] & (0x2000000000L << face)) >>> 37)) > 0;
	}

	public void setFaceFlippedHorizontally(int x, int y, int z, int face, boolean flipped) {
		setFaceFlippedHorizontally(positionToIndex(x, y, z), face, flipped);
	}

	public boolean isFaceFlippedHorizontally(int x, int y, int z, int face) {
		return isFaceFlippedHorizontally(positionToIndex(x, y, z), face);
	}

	public void setFaceFlippedHorizontally(int index, int face, boolean fliped) { // 1 bit x 6
		index <<= 1;
		data[index] &= ~(0x80000000000L << face);
		data[index] |= (((fliped ? 1L : 0L) << face)) << 43;
	}

	public boolean isFaceFlippedHorizontally(int index, int face) {
		return (((data[index << 1] & (0x80000000000L << face)) >>> 43)) > 0;
	}

	public void setFaceFlippedVertically(int x, int y, int z, int face, boolean flipped) {
		setFaceFlippedVertically(positionToIndex(x, y, z), face, flipped);
	}

	public boolean isFaceFlippedVertically(int x, int y, int z, int face) {
		return isFaceFlippedVertically(positionToIndex(x, y, z), face);
	}

	public void setFaceFlippedVertically(int index, int face, boolean fliped) { // 1 bit x 6
		index <<= 1;
		data[index] &= ~(0x2000000000000L << face);
		data[index] |= (((fliped ? 1L : 0L) << face)) << 49;
	}

	public boolean isFaceFlippedVertically(int index, int face) {
		return (((data[index << 1] & (0x2000000000000L << face)) >>> 49)) > 0;
	}
	
	public void setTextureAngle(int x, int y, int z, int face, int angle) {
		setTextureAngle(positionToIndex(x, y, z), face, angle);
	}

	public int getTextureAngle(int x, int y, int z, int face) {
		return getTextureAngle(positionToIndex(x, y, z), face);
	}

	public void setTextureAngle(int index, int face, int angle) { // 2 bits * 6		
		index = index << 1;
		int offset = (52 + (face << 1));	
		data[index] &= ~(0x3L << offset);
		data[index] |= (angle & 0x3L) << offset;		
	}

	public int getTextureAngle(int index, int face) {
		int offset = (52 + (face << 1));
		return (int) ((data[index << 1] & (0x3L << offset)) >>> offset);
	}
	
	public void setTextureIndex(int x, int y, int z, int face, int i) {
		setTextureIndex(positionToIndex(x, y, z), face, i);
	}

	public int getTextureIndex(int x, int y, int z, int face) {
		return getTextureIndex(positionToIndex(x, y, z), face);
	}
	
	public void setTextureIndex(int index, int face, int i) { // 10 bits * 6
		index = (index << 1) + 1;
		long offset = face * 10L;
		data[index] &= ~(0x3FFL << offset);
		data[index] |= (i & 0x3FFL) << offset;
	}

	public int getTextureIndex(int index, int face) {
		long offset = face * 10L;
		return (int) ((data[(index << 1) + 1] & (0x3FFL << offset)) >>> offset);
	}
	
	public void setForceDraw(int x, int y, int z, boolean forceDraw) {
		setForceDraw(positionToIndex(x, y, z), forceDraw);
	}

	public boolean isForceDraw(int x, int y, int z) {
		return isForceDraw(positionToIndex(x, y, z));
	}
	
	public boolean isForceDraw(int index) {
		return ((data[(index << 1) + 1] & 0x1000000000000000L) >> 60) == 1; 
	}
	
	public void setForceDraw(int index, boolean forceDraw) {
		index = (index << 1) + 1;
		data[index] &= ~0x1000000000000000L;
		data[index] |= ((forceDraw ? 1 : 0) & 0x1L) << 60;
	}
	
	public long[] getData() {
		return data;
	}

	public void clear() {
		long[] data = this.data;
		int end = data.length;
		for (int i = SIZE2 << 1; i < end; i += 2) {
			data[i] = CLEAR_VALUE1;
			data[i + 1] = CLEAR_VALUE2;
		}
	}

	public Chunk getChunk() {		
		return chunk;
	}
	
	public int getReferenceLight(int x, int y, int z) {
		y += refY;
		if(y > 0 && y < settings.aHeight) {
			x += refX;
			z += refZ;
			Chunk c = chunk.getLevel().getChunkAbsolute(x, z);
			if (c != null) {
				return c.getChunkData().getLight(x - c.getWorldX(), y, z - c.getWorldZ());
			}
		}
		return MAX_VALUE_4_BITS;
	}

	public void setReferencePoint(int x, int y, int z) {
		this.refX = x;
		this.refY = y;
		this.refZ = z;
	}

}
