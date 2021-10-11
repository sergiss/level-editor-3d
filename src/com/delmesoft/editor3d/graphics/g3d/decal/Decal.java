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
package com.delmesoft.editor3d.graphics.g3d.decal;

import com.delmesoft.editor3d.entity.ObjInfo;
import com.delmesoft.editor3d.graphics.g2d.TextureArea;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public class Decal extends ObjInfo {
		
	private static final Vector3 tmp = new Vector3();
		
	public final Vector3 position;
	
	public float halfWidth, halfHeight;
	
	public TextureArea textureArea;
		
	public Decal() {
		position = new Vector3();
	}
	
	public Decal(TextureArea textureArea) {
		this();
		this.textureArea = textureArea;
	}
	
	public void render(DecalRenderer decalRenderer, Camera camera) {
		tmp.set(position.x, 0, position.z).sub(camera.position.x, 0, camera.position.z).nor();
		decalRenderer.setRotation(tmp, camera.up);
		decalRenderer.render(textureArea.texture, 
				position.x, position.y, position.z, halfWidth, halfHeight, 
				textureArea.x1, textureArea.y1, textureArea.x2, textureArea.y2);
	}

	public boolean hit(Ray ray) {
		return Intersector.intersectRaySphere(ray, position, Math.max(halfWidth, halfHeight), null);
	}

	public boolean isVisible(Camera camera) {
		return camera.frustum.boundsInFrustum(position.x, position.y, position.z, halfWidth, halfHeight, halfWidth);
	}
	
}
