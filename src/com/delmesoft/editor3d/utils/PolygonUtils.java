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

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PolygonUtils {
	
	private static Quaternion tmpQuat = new Quaternion();	

	public static final void project(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 direction) {
		
		Vector3 centroid = new Vector3(v1.x + v2.x + v3.x, v1.y + v2.y + v3.y, v1.z + v2.z + v3.z).scl(0.333333333F);
						
		v1.sub(centroid);
		v2.sub(centroid);
		v3.sub(centroid);
				
		Vector3 surfaceNormal = getSurfaceNormal(v1, v2, v3);
		
		Quaternion quaternion = tmpQuat.setFromCross(surfaceNormal, direction);

		quaternion.transform(centroid);
						
		quaternion.transform(v1).add(centroid);
		quaternion.transform(v2).add(centroid);
		quaternion.transform(v3).add(centroid);
				
	}
	
	public static final Vector3 getSurfaceNormal(Vector3 v1, Vector3 v2, Vector3 v3) {
		return getSurfaceNormal(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z, v3.x, v3.y, v3.z);
	}
	
	public static final Vector3 getSurfaceNormal(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {		
		Vector3 u = new Vector3(x2 - x1, y2 - y1, z2 - z1);		
		return u.crs(x3 - x1, y3 - y1, z3 - z1).nor();
	}

	public static Vector2 getCentroid(float x1, float y1, float x2, float y2, float x3, float y3) {
		return new Vector2(x1 + x2 + x3, y1 + y2 + y3).scl(0.333333333F);
	}

}
