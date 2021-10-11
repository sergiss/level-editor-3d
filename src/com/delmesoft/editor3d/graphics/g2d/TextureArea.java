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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class TextureArea extends Rectangle {
	
	public Texture texture;
	
	public int width, height;

	public Object userObject;
	
	public TextureArea() {}
	
	public TextureArea(Texture texture, float x1, float y1, float x2, float y2) {
		
		this.texture = texture;
		
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		width  = Math.round((x2 - x1) * texture.getWidth());
		height = Math.round((y2 - y1) * texture.getHeight());
				
	}
	
	public TextureArea(Texture texture) {
		this(texture, 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public TextureArea(Texture texture, int x, int y, int width, int height) {
		
		this.texture = texture;
		
		float invTexWidth  = 1f / texture.getWidth();
		float invTexHeight = 1f / texture.getHeight();
		
		this.x1 = x * invTexWidth;
		this.x2 = (x + width ) * invTexWidth;
		this.y1 = y * invTexHeight;
		this.y2 = (y + height) * invTexHeight;
		
		this.width  = width;
		this.height = height;
		
	}
	
	public TextureArea(AtlasRegion region) {
		this(region.getTexture(), region.getU(), region.getV(), region.getU2(), region.getV2());
		this.userObject = region.name;
	}

	public int getRegionX() {
		return Math.round(x1 * texture.getWidth());
	}

	public int getRegionY() {
		return Math.round(y1 * texture.getHeight());
	}

}
