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

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.delmesoft.editor3d.graphics.g2d.Rectangle;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class ImageUtils {

	public static BufferedImage[][] split(BufferedImage image, int tileWidth, int tileHeight, int margin, int spacing) {
		
		int w = image.getWidth();
		int h = image.getHeight();
		
		int cols = (w - margin) / (tileWidth  + spacing);
		int rows = (h - margin) / (tileHeight + spacing);
		
		Graphics g;
		
		BufferedImage[][] images = new BufferedImage[cols][rows];
		BufferedImage img;
		int x1, y1;
		for(int x = 0; x < cols; ++x) {
			images[x] = new BufferedImage[rows];
			x1 = x * (tileWidth + spacing) + margin;
			for(int y = 0; y < rows; ++y) {				
				img = new BufferedImage(tileWidth, tileHeight, image.getType());				
				g = img.getGraphics();
				y1 = y * (tileHeight + spacing) + margin;
				g.drawImage(image, 0, 0, tileWidth, tileHeight, x1, y1, x1 + tileWidth, y1 + tileHeight, null);
				g.dispose();				
				images[x][y] = img;				
			}
		}
		
		return images;
	}
	
	public static BufferedImage pixmapToBufferedImage(Pixmap pixmap, int x1, int y1, int x2, int y2) {				
		BufferedImage bufferedImage = new BufferedImage(x2 - x1, y2 - y1, BufferedImage.TYPE_INT_ARGB);
		int i, j, x, y;
		for(i = 0, x = x1; x < x2; ++x, ++i) {
			for(j = 0, y = y1; y < y2; ++y, ++j) {
				bufferedImage.setRGB(i, j, rgbaToArgb(pixmap.getPixel(x, y)));
			}
		}		
		return bufferedImage;
	}
	
	public static BufferedImage pixmapToBufferedImage(Pixmap pixmap, Rectangle rectangle) {
		int x1 = (int) (pixmap.getWidth()  * rectangle.x1);
		int x2 = (int) (pixmap.getWidth()  * rectangle.x2);
		int y1 = (int) (pixmap.getHeight() * rectangle.y1);
		int y2 = (int) (pixmap.getHeight() * rectangle.y2);
		return pixmapToBufferedImage(pixmap, x1, y1, x2, y2);
	}
	
	public static BufferedImage pixmapToBufferedImage(Pixmap pixmap) {
		return pixmapToBufferedImage(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight());
	}
	
	public static BufferedImage textureToBufferedImage(Texture texture, int x1, int y1, int x2, int y2) {
		texture.getTextureData().prepare();
		return pixmapToBufferedImage(texture.getTextureData().consumePixmap(), x1, y1, x2, y2);
	}
		
	public static BufferedImage textureToBufferedImage(Texture texture, Rectangle rectangle) {
		texture.getTextureData().prepare();
		return pixmapToBufferedImage(texture.getTextureData().consumePixmap(), rectangle);
	}
	
	public static BufferedImage textureToBufferedImage(Texture texture) {
		return textureToBufferedImage(texture, 0, 0, texture.getWidth(), texture.getHeight());
	}
	
	public static BufferedImage[][] tiledTextureToBufferedImageArray(TiledTexture tiledTexture) {
		BufferedImage[][] result = new BufferedImage[tiledTexture.getCols()][tiledTexture.getRows()];
		Rectangle[] tc = tiledTexture.getTextureCoordinates();
		Rectangle rectangle;
		TextureData textureData = tiledTexture.getTexture().getTextureData();
		textureData.prepare();
		Pixmap pixmap = textureData.consumePixmap();
		
		for(int x = 0; x < tiledTexture.getCols(); ++x) {
			for(int y = 0; y < tiledTexture.getRows(); ++y) {
				rectangle = tc[y * tiledTexture.getCols() + x];
				result[x][y] = pixmapToBufferedImage(pixmap, rectangle);
			}
		}		
		return result;
	}
	
	public static BufferedImage atlasRegionToBufferedImage(AtlasRegion region) {
		return textureToBufferedImage(region.getTexture(), region.getRegionX(), region.getRegionY(), region.getRegionX() + region.getRegionWidth(), region.getRegionY() + region.getRegionHeight());
	}
	
	public static int rgbaToArgb(int rgba) { // RGBA8888
		int r = (int) ((rgba & 0XFF000000) >> 24);
		int g = (int) ((rgba & 0X00FF0000) >> 16);
		int b = (int) ((rgba & 0X0000FF00) >> 8);
		int a = (int) ( rgba & 0X000000FF);
		return a << 24 | r << 16 | g << 8 | b; // TYPE_INT_ARGB
	}
	
	public static int argbToRgba(int argb) { // TYPE_INT_ARGB
		int b = (int) (argb  & 0X000000FF);
		int g = (int) ((argb & 0X0000FF00) >> 8);
		int r = (int) ((argb & 0X00FF0000) >> 16);
		int a = (int) ((argb & 0XFF000000) >> 24);
		return r << 24 | g << 16 | b << 8 | a; // RGBA8888
	}

}
