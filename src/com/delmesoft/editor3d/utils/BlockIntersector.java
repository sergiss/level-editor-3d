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

import com.delmesoft.editor3d.level.block.Side;

import com.badlogic.gdx.math.Vector3;

public class BlockIntersector {

	private static Vector3 v0 = new Vector3();
	
	public static Side intersectRayBounds(float originX, float originY, float originZ, float directionX, float directionY, float directionZ, float x, float y, float z) {
	
		float maxX = x + 1;
		float maxY = y + 1;
		float maxZ = z + 1;
		
		float lowest = 0, t;
		
		Side side = null;
		// min x
		if (originX <= x && directionX > 0) {
			t = (x - originX) / directionX;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.y >= y && v0.y <= maxY && v0.z >= z && v0.z <= maxZ) {
					side = Side.LEFT;
					lowest = t;
				}
			}
		}
		
		// max x
		if (originX >= maxX && directionX < 0) {
			t = (maxX - originX) / directionX;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.y >= y && v0.y <= maxY && v0.z >= z && v0.z <= maxZ) {
					side = Side.RIGHT;
					lowest = t;
				}
			}
		}
		// min y
		if (originY <= y && directionY > 0) {
			t = (y - originY) / directionY;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.x >= x && v0.x <= maxX && v0.z >= z && v0.z <= maxZ) {
					side = Side.BOTTOM;
					lowest = t;
				}
			}
		}
		// max y
		if (originY >= maxY && directionY < 0) {
			t = (maxY - originY) / directionY;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.x >= x && v0.x <= maxX && v0.z >= z && v0.z <= maxZ) {
					side = Side.TOP;
					lowest = t;
				}
			}
		}
		// min z
		if (originZ <= z && directionZ > 0) {
			t = (z - originZ) / directionZ;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.x >= x && v0.x <= maxX && v0.y >= y && v0.y <= maxY) {
					side = Side.BACK;
					lowest = t;
				}
			}
		}
		
		// max z
		if (originZ >= maxZ && directionZ < 0) {
			t = (maxZ - originZ) / directionZ;
			if (t >= 0 && (t < lowest || side == null)) {
				v0.set(directionX, directionY, directionZ).scl(t).add(originX, originY, originZ);
				if (v0.x >= x && v0.x <= maxX && v0.y >= y && v0.y <= maxY) {
					side = Side.FRONT;
					//lowest = t;
				}
			}
		}
			
		return side;
	}

	public static Side intersectRayBounds(Vector3 origin, Vector3 direction, float x, float y, float z) {
		return intersectRayBounds(origin.x, origin.y, origin.z, direction.x, direction.y, direction.z, x, y, z);
	}


}
