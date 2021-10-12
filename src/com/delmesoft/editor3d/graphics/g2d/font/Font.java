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
package com.delmesoft.editor3d.graphics.g2d.font;

import com.delmesoft.editor3d.graphics.g2d.Rectangle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class Font implements Disposable {
	
	protected Texture texture;
	protected Rectangle[] regions;
	
	protected int charWidth, charHeight;

	public float scale = 1f;
	
	public Font(TextureRegion textureRegion, int cols, int rows) {
	
		final Texture texture = textureRegion.getTexture();
		
		float invTexWidth  = 1f / texture.getWidth();
		float invTexHeight = 1f / texture.getHeight();

		int x = textureRegion.getRegionX();
		int y = textureRegion.getRegionY();

		int tileWidth  = textureRegion.getRegionWidth() / cols;
		int tileHeight = textureRegion.getRegionHeight() / rows;

		int startX = x;

		Rectangle[] regions = new Rectangle[rows * cols];
		
		int index = 0;
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			float ry = y * invTexHeight;
			float rh = (y + tileHeight) * invTexHeight;
			for (int col = 0; col < cols; col++, x += tileWidth) {
				regions[index++] = new Rectangle(x * invTexWidth, ry, (x + tileWidth) * invTexWidth, rh);
			}
		}
		
		charWidth  = tileWidth;
		charHeight = tileHeight;
		
		this.regions = regions;
		this.texture = texture;
		
	}	
		
	public void render(CharSequence text, float x, float y, Batch batch) {
		
		float width  = getCharWidth();
		float height = getCharHeight();

		float dspX = 0;

		Texture texture = this.texture;
		Rectangle[] regions = this.regions;
		
		Rectangle tc;

		for(int i = 0, n = text.length(); i < n; i++) {
		
			tc = regions[text.charAt(i)];	
		
			batch.draw(texture, x + dspX, y, width, height, tc.x1, tc.y2, tc.x2, tc.y1);
			dspX += width;

		}
		
	}
	
	public float getCharWidth() {
		return charWidth * scale;
	}

	public float getCharHeight() {
		return charHeight * scale;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public Rectangle[] getTextureCoordinates() {
		return regions;
	}	

	@Override
	public void dispose() {
		for(int i = 0, n = regions.length; i < n; i++) {
			regions[i] = null;
		}
		regions = null;
	}

}
