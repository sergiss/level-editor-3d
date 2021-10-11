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
package com.delmesoft.editor3d.level.block.utils;

import com.delmesoft.editor3d.graphics.MeshBuilder;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.graphics.g2d.UVCoordinates;
import com.delmesoft.editor3d.level.block.ChunkData;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public abstract class MeshManager {
	
	public static final float FORCE_DRAW_OFFSET = 0.001F;
	
	public static final VertexAttributes VERTEX_ATTRIBUTES =  new VertexAttributes(new VertexAttribute(Usage.Position, 		     3, ShaderProgram.POSITION_ATTRIBUTE), 	    // 32 bits * 3
			   												  					   new VertexAttribute(Usage.Normal,  		     3, ShaderProgram.NORMAL_ATTRIBUTE),        // 32 bits * 3
			   												  					   new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0")); // 32 bits * 2
	
	private static final Vector3 tmp1 = new Vector3();
	private static final Vector3 tmp2 = new Vector3();
	private static final Vector3 tmp3 = new Vector3();
	private static final Vector3 tmp4 = new Vector3();
		
	// protected static final float[] normals = { 1F, 0.76F, 0.92F, 0.92F, 0.84F, 0.84F };	
	
	protected TiledTexture tiledTexture;
	
	protected final Vector3 v000, v100, v101, v001, v010, v110, v111, v011;
	
	protected final Vector3 v0, v1, v2, v3;
			
	protected final UVCoordinates uv;
	
	protected ChunkData chunkData;
	protected int x, y, z;
	
	protected int index;
	
	protected int geometry;

	private MeshBuilder meshBuilder;

	public MeshManager(TiledTexture tiledTexture) {
		
		this.tiledTexture = tiledTexture;
		
		v000 = new Vector3();
		v100 = new Vector3();
		v101 = new Vector3();
		v001 = new Vector3();
		v010 = new Vector3();
		v110 = new Vector3();
		v111 = new Vector3();
		v011 = new Vector3();
		
		v0 = new Vector3();
		v1 = new Vector3();
		v2 = new Vector3();
		v3 = new Vector3();
				
		uv = new UVCoordinates();
		
	}

	public MeshManager initialize(ChunkData chunkData, int index, int x, int y, int z, boolean forceDraw) {
		this.chunkData = chunkData;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.index = index;
				
		if(forceDraw) {
			float off = FORCE_DRAW_OFFSET;			
			v000.set(x      + off, y      - off, z      + off);
			v100.set(x + 1F - off, y      - off, z      + off);
			v101.set(x + 1F - off, y      - off, z + 1F - off);
			v001.set(x      + off, y      - off, z + 1F - off);
			v010.set(x      + off, y - 1F + off, z      + off);
			v110.set(x + 1F - off, y - 1F + off, z      + off);
			v111.set(x + 1F - off, y - 1F + off, z + 1F - off);
			v011.set(x      + off, y - 1F + off, z + 1F - off);			
		} else {
			v000.set(x     , y     , z     );
			v100.set(x + 1F, y     , z     );
			v101.set(x + 1F, y     , z + 1F);
			v001.set(x     , y     , z + 1F);
			v010.set(x     , y - 1F, z     );
			v110.set(x + 1F, y - 1F, z     );
			v111.set(x + 1F, y - 1F, z + 1F);
			v011.set(x     , y - 1F, z + 1F);
		}
		
		geometry = chunkData.getGeometryType(index);
		
		chunkData.setReferencePoint(x, y, z);		
	
		return this;
	}

	public abstract void createTop();

	public abstract void createBottom();

	public abstract void createFront();

	public abstract void createBack();

	public abstract void createRight();

	public abstract void createLeft();
	
	public void setMeshBuilder(MeshBuilder meshBuilder) {
		this.meshBuilder = meshBuilder;
	}
		
	public MeshBuilder getMeshBuilder() {
		return meshBuilder;
	}
	
	public void addTriIndices() {
		short iOff = meshBuilder.iOff;
		meshBuilder.addIndices(iOff, (short) (1 + iOff), (short) (2 + iOff));
		meshBuilder.iOff += 3;
	}
	
	public void addTriVertices(int discartIndex) {

		Vector3 normal;
		switch (discartIndex) {
		case 0:
			normal = computeNormal(v1, v2, v3, tmp1);
			meshBuilder.addVertices(v1.x, v1.y, v1.z, normal.x, normal.y, normal.z, uv.u1, uv.v1, 
					                v2.x, v2.y, v2.z, normal.x, normal.y, normal.z, uv.u2, uv.v2, 
					                v3.x, v3.y, v3.z, normal.x, normal.y, normal.z, uv.u3, uv.v3);
			break;
		case 1:
			normal = computeNormal(v0, v2, v3, tmp1);
			meshBuilder.addVertices(v0.x, v0.y, v0.z, normal.x, normal.y, normal.z, uv.u0, uv.v0, 
					                v2.x, v2.y, v2.z, normal.x, normal.y, normal.z, uv.u2, uv.v2,
					                v3.x, v3.y, v3.z, normal.x, normal.y, normal.z, uv.u3, uv.v3);
			break;
		case 2:
			normal = computeNormal(v0, v1, v3, tmp1);
			meshBuilder.addVertices(v0.x, v0.y, v0.z, normal.x, normal.y, normal.z, uv.u0, uv.v0, 
									v1.x, v1.y, v1.z, normal.x, normal.y, normal.z, uv.u1, uv.v1, 
									v3.x, v3.y, v3.z, normal.x, normal.y, normal.z, uv.u3, uv.v3);
			break;
		case 3:
			normal = computeNormal(v0, v1, v2, tmp1);
			meshBuilder.addVertices(v0.x, v0.y, v0.z, normal.x, normal.y, normal.z, uv.u0, uv.v0,
					                v1.x, v1.y, v1.z, normal.x, normal.y, normal.z, uv.u1, uv.v1,
					                v2.x, v2.y, v2.z, normal.x, normal.y, normal.z, uv.u2, uv.v2);
			break;
		}
	
	}
	
	public void addIndicesNormalQuad() {
		short iOff = meshBuilder.iOff;
		meshBuilder.addIndices(iOff, (short) (1 + iOff), (short) (2 + iOff), (short) (2 + iOff), (short) (3 + iOff), iOff);	
		meshBuilder.iOff += 4;
	}
	
	public void addIndicesFlippedQuad() {
		short iOff = meshBuilder.iOff;
		meshBuilder.addIndices((short) (3 + iOff), iOff, (short) (1 + iOff), (short) (1 + iOff), (short) (2 + iOff), (short) (3 + iOff));
		meshBuilder.iOff += 4;
	}
	
	public void addQuadVertices() {

		Vector3 n0 = computeNormal(v0, v1, v3, tmp1);
		Vector3 n1 = computeNormal(v1, v2, v0, tmp2);
		Vector3 n2 = computeNormal(v2, v3, v1, tmp3);
		Vector3 n3 = computeNormal(v3, v0, v2, tmp4);
		
		meshBuilder.addVertices(v0.x, v0.y, v0.z, n0.x, n0.y, n0.z, uv.u0, uv.v0,
							    v1.x, v1.y, v1.z, n1.x, n1.y, n1.z, uv.u1, uv.v1, 
							    v2.x, v2.y, v2.z, n2.x, n2.y, n2.z, uv.u2, uv.v2,
							    v3.x, v3.y, v3.z, n3.x, n3.y, n3.z, uv.u3, uv.v3);
	}

	private static final Vector3 u = new Vector3(), v = new Vector3();
	private Vector3 computeNormal(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 store) {
		u.set(v2).sub(v1);
		v.set(v3).sub(v1);		
		return store.set(u).crs(v);
	}
		
}
