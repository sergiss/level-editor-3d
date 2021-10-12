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
package com.delmesoft.editor3d.graphics.g2d;

import com.badlogic.gdx.graphics.Texture;

public class TiledTexture {
	
	protected Texture texture;
	
	protected Rectangle[] textureUVs;
	
	protected int tileWidth, tileHeight;
	
	protected int cols, rows;
		
	protected TiledTexture() {};
		
	public TiledTexture(Texture texture, int tileWidth, int tileHeight) {
		this(texture, 0, 0, texture.getWidth(), texture.getHeight(), tileWidth, tileHeight);		
	}
	
	public TiledTexture(TextureArea textureRegion, int tileWidth, int tileHeight) {
		this(textureRegion.texture, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.width, textureRegion.height, tileWidth, tileHeight);
	}
	
	public TiledTexture(Texture texture, int regionX, int regionY, int regionWidth, int regionHeight, int tileWidth, int tileHeight) {
						
		final float invWidth  = 1f / texture.getWidth();
		final float invHeight = 1f / texture.getHeight();

		final float tWidth  = tileWidth  * invWidth;
		final float tHeight = tileHeight * invHeight;

		final float xOff = regionX * invWidth;

		final int cols = texture.getWidth()  / tileWidth;
		final int rows = texture.getHeight() / tileHeight;

		final Rectangle[] tCoords = new Rectangle[cols * rows];

		int index;

		float u, v = regionY * invHeight;

		for (int j = 0; j < rows; j++) {

			index = j * cols;

			u = xOff;

			for (int i = 0; i < cols; i++) {

				tCoords[index + i] = new Rectangle(u, v, u + tWidth, v + tHeight);

				u += tWidth;
			}

			v += tHeight;

		}

		textureUVs = tCoords;

		this.texture = texture;

		this.tileWidth  = tileWidth;
		this.tileHeight = tileHeight;
		
		this.cols = cols;
		this.rows = rows;
		
	}
		
	public TiledTexture(Texture texture, int tileWidth, int tileHeight, int margin, int spacing) {
		this(texture, 0, 0, texture.getWidth(), texture.getHeight(), tileWidth, tileHeight, margin, spacing);
	}

	public TiledTexture(TextureArea textureRegion, int tileWidth, int tileHeight, int margin, int spacing) {
		this(textureRegion.texture, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.width, textureRegion.height, tileWidth, tileHeight, margin, spacing);
	}
	
	public TiledTexture(Texture texture, int regionX, int regionY, int regionWidth, int regionHeight, int tileWidth, int tileHeight, int margin, int spacing) {
						
		final float invWidth  = 1f / texture.getWidth();
		final float invHeight = 1f / texture.getHeight();

		final float tWidth  = tileWidth  * invWidth;
		final float tHeight = tileHeight * invHeight;

		final float spacingX = spacing * invWidth;
		final float spacingY = spacing * invHeight;

		final float xOff = regionX * invWidth + margin * invWidth;

		final int cols = (regionWidth  - margin) / (tileWidth  + spacing);
		final int rows = (regionHeight - margin) / (tileHeight + spacing);

		final Rectangle[] tCoords = new Rectangle[cols * rows];

		int index;

		float u, v = regionY * invHeight + margin * invHeight;

		for (int j = 0; j < rows; j++) {

			index = j * cols;

			u = xOff;

			for (int i = 0; i < cols; i++) {

				tCoords[index + i] = new Rectangle(u, v, u + tWidth, v + tHeight);

				u += tWidth + spacingX;
			}

			v += tHeight + spacingY;
		}

		textureUVs = tCoords;

		this.texture = texture;

		this.tileWidth  = tileWidth;
		this.tileHeight = tileHeight;
		
		this.cols = cols;
		this.rows = rows;
		
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Rectangle[] getTextureCoordinates() {
		return textureUVs;
	}
	
	public void set(TiledTexture tiledTexture) {
		this.texture = tiledTexture.texture;
		this.textureUVs = tiledTexture.textureUVs;
		this.tileWidth = tiledTexture.tileWidth;
		this.tileHeight = tiledTexture.tileHeight;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}
			
}
