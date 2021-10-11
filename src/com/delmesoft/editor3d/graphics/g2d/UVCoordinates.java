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

public class UVCoordinates {
	
	
	/*********
	 *0     1*
	 *       *
	 *3     4*
	 *********/
	
	public float u0, v0, 
				 u1, v1, 
				 u2, v2, 
				 u3, v3;
	
	public UVCoordinates() {
		u0 = 0F;
		v0 = 1F;
		u1 = 1F;
		v1 = 1F;
		u2 = 1F;
		v2 = 0F;
		u3 = 0F;
		v3 = 0F;
	}
	
	public UVCoordinates set(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean flipX, boolean flipY, int rotation) {
		return this.set(x0, y0, x1, y1, x2, y2, x3, y3, flipX, flipY, 1F, 1F, rotation);
	}
	
	public UVCoordinates set(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean flipX, boolean flipY, float scaleX, float scaleY, int rotation) {
		return this.set(x0, y0, x1, y1, x2, y2, x3, y3, flipX, flipY, scaleX, scaleY, rotation, 0, 0);
	}
	
	public UVCoordinates set(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, boolean flipX, boolean flipY, float scaleX, float scaleY, int rotation, float positionX, float positionY) {
				
		if (flipX) {
			u0 = 0.5F - x0;
			u1 = 0.5F - x1;
			u2 = 0.5F - x2;
			u3 = 0.5F - x3;
		} else {
			u0 = x0 - 0.5F;
			u1 = x1 - 0.5F;
			u2 = x2 - 0.5F;
			u3 = x3 - 0.5F;
		}

		if (flipY) {
			v0 = 0.5F - y0;
			v1 = 0.5F - y1;
			v2 = 0.5F - y2;
			v3 = 0.5F - y3;
		} else {
			v0 = y0 - 0.5F;
			v1 = y1 - 0.5F;
			v2 = y2 - 0.5F;
			v3 = y3 - 0.5F;
		}
		
		if(rotation > 0 && rotation < 4) {
			float radians = rotation * 1.5708F;			
			float cos = (float) Math.cos(radians);
			float sin = (float) Math.sin(radians);
			float x = u0, y = v0;
			u0 = x * cos - y * sin;
			v0 = x * sin + y * cos;
			x = u1; y = v1;
			u1 = x * cos - y * sin;
			v1 = x * sin + y * cos;
			x = u2; y = v2;
			u2 = x * cos - y * sin;
			v2 = x * sin + y * cos;
			x = u3; y = v3;
			u3 = x * cos - y * sin;
			v3 = x * sin + y * cos;
		}
		
		float originX = 0.5F * scaleX;
		float originY = 0.5F * scaleY;

		u0 = u0 * scaleX + originX + positionX;
		v0 = v0 * scaleY + originY + positionY;
		u1 = u1 * scaleX + originX + positionX;
		v1 = v1 * scaleY + originY + positionY;
		u2 = u2 * scaleX + originX + positionX;
		v2 = v2 * scaleY + originY + positionY;
		u3 = u3 * scaleX + originX + positionX;
		v3 = v3 * scaleY + originY + positionY;
						
		return this;
	}

	@Override
	public String toString() {
		return "UVCoordinates [u0=" + u0 + ", v0=" + v0 + ", u1=" + u1 + ", v1=" + v1 + ", u2=" + u2 + ", v2=" + v2 + ", u3=" + u3 + ", v3=" + v3 + "]";
	}
	
	
		
}
