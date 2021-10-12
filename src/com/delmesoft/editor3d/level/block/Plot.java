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

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Plane;

public class Plot {
	
	public static final int BIT_OFFSET = 4;
	
	public static final int BIT_OFFSET2 = BIT_OFFSET << 1;

	public static final int SIZE = 1 << BIT_OFFSET; // (1 << 4) = 16
	
	public static final int SIZE2 = 1 << BIT_OFFSET2; // (1 << 8) = 256;
			
	public static final int HALF_SIZE = SIZE >> 1;

	public static final float RADIUS = (float) (Math.sqrt(3) * HALF_SIZE); // For frustum

	private final int localY;
	private final int worldY;
	
	private final Chunk chunk;

	public Mesh opaqueMesh, blendedMesh;

	public Plot(int localY, Chunk chunk) {
		this.localY = localY;
		this.worldY = localY << BIT_OFFSET;
		this.chunk = chunk;
	}
	
	public int getLocalY() {
		return localY;
	}
	
	public int getWorldY() {
		return worldY;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		if(opaqueMesh != null) {
			opaqueMesh.dispose();
			opaqueMesh = null;
		}
		if(blendedMesh != null) {
			blendedMesh.dispose();
			blendedMesh = null;
		}
	}

	public boolean isVisible(Plane[] planes) {
		
		if(opaqueMesh == null && blendedMesh == null) return false;
		
		final int x = chunk.getWorldX() + HALF_SIZE,
				  y = worldY            + HALF_SIZE,
				  z = chunk.getWorldZ() + HALF_SIZE;

		Plane plane;

		for (int i = 0; i < 6; i++){
			plane = planes[i];
			if ((plane.normal.x * x + plane.normal.y * y + plane.normal.z * z) < (-RADIUS - planes[i].d)) return false;
		}

		return true;
	}

}
