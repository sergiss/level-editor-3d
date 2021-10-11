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
package com.delmesoft.editor3d.level;

import static com.delmesoft.editor3d.level.block.Plot.SIZE;

import com.delmesoft.editor3d.level.block.Plot;

import com.badlogic.gdx.Input.Keys;

public class Settings {
	
	public static final int MAX_HORIZONTAL_CHUNKS = 38;
	public static final int MAX_VERTICAL_PLOTS = 16;
	
	public static final String DEFAULT_LEVEL_NAME = "Unnamed";
	
	public static final int DEFAULT_WIDTH  = 8;
	public static final int DEFAULT_HEIGHT = 4;
	public static final int DEFAULT_DEPTH  = 8;
	
	public static final int DEFAULT_TILE_SIZE = 16;
	
	public static final int KEY_UP      = Keys.W;
	public static final int KEY_DOWN    = Keys.S;
	public static final int KEY_LEFT    = Keys.A;
	public static final int KEY_RIGHT   = Keys.D;
	public static final int KEY_ACTION1 = Keys.SHIFT_LEFT;
	public static final int KEY_ACTION2 = Keys.CONTROL_LEFT;
	public static final int KEY_GRID_UP = Keys.E;
	public static final int KEY_GRID_DOWN = Keys.Q;

	public String levelName = DEFAULT_LEVEL_NAME;
		
	public int rWidth;
	public int rHeight;
	public int rDepth;
	
	public int aWidth;
	public int aHeight;
	public int aDepth;
	
	public int maxZ = Plot.SIZE - 1;
	public int maxX;
	
	public int chunkCount;	
	public int blockCount;
	
	public String texturePath;
	public int tileWidth  = DEFAULT_TILE_SIZE;
	public int tileHeight = DEFAULT_TILE_SIZE;
	public int margin;
	public int spacing;
	
	public int chunkVisibility = 10;
		
	public Settings initialize(String levelName, int relativeWidth, int relativeHeight, int relativeDepth) {
		
		this.levelName = levelName;

		rWidth = relativeWidth;
		aWidth = relativeWidth * SIZE;

		rHeight = relativeHeight;
		aHeight = relativeHeight * SIZE;

		rDepth = relativeDepth;
		aDepth = relativeDepth * SIZE;

		chunkCount = rWidth * rDepth;

		blockCount = SIZE * SIZE * aHeight;
		
		return this;
	}

	public String getTileMapInfo() {
		return String.format("Texture path: %s, Tile Width: %d, Tile Height: %d, Margin: %d, Spacing: %d", texturePath, tileWidth, tileHeight, margin, spacing);
	}

}
