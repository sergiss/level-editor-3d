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

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class DecalRenderer implements Disposable {

	private final Renderable renderable;
	private final Mesh mesh;

	final float[] vertices;
    int idx = 0;

	private Quaternion rotation = new Quaternion();
	private Quaternion tmp1 = new Quaternion();
	private Vector3 tmp2 = new Vector3();
	
	private final Vector3 normal = new Vector3(0, 0, -1);

	private ModelBatch modelBatch;
	private Environment environment;
	
	private TextureAttribute textureAttribute;

	public DecalRenderer() {
		this(1000);
	}

	public DecalRenderer(int size) {
		
		mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, size * 4, size * 6, new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE),
																					new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE),
																				    new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"));
		
		
		vertices = new float[size * 8 * 4]; // size == n� models, 6 == 3(position) + 3(normal) + 2(texture coordinates), 4 == 4 vertices

		int len = size * 6;
		short[] indices = new short[len];
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i] = j;
			indices[i + 1] = (short)(j + 3);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 1);
			indices[i + 5] = j;
		}

		mesh.setIndices(indices, 0, len);
		
		renderable = new Renderable();
		renderable.meshPart.mesh = mesh;
		renderable.meshPart.offset = 0;
		renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
		renderable.worldTransform.setToTranslation(0, 0, 0);
		Material material = new Material();
		textureAttribute = new TextureAttribute(TextureAttribute.Diffuse);
		material.set(textureAttribute);
		material.set(FloatAttribute.createAlphaTest(0.1F));
		material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
		material.set(IntAttribute.createCullFace(GL20.GL_BACK));
		renderable.material = material;

	}

	public void render(Texture texture, float x, float y, float z, float halfWidth, float halfHeight, float u, float v, float u2, float v2) {

		float[] vertices = this.vertices;

		if (texture != textureAttribute.textureDescription.texture) {
			switchTexture(texture);
		} else if (idx == vertices.length) {
			flush();
		}

		int idx = this.idx;
		
		float x1, x2, x3, x4;
		float y1, y2, y3, y4;
		float z1, z2, z3, z4;
		
		// left bottom		
		tmp1.set(rotation);
		tmp1.conjugate();
		tmp1.mulLeft(-halfWidth, -halfHeight, 0, 0).mulLeft(rotation);
		x1 = tmp1.x;
		y1 = tmp1.y;
		z1 = tmp1.z;
		// right bottom
		tmp1.set(rotation);
		tmp1.conjugate();
		tmp1.mulLeft(halfWidth, -halfHeight, 0, 0).mulLeft(rotation);
		x2 = tmp1.x;
		y2 = tmp1.y;
		z2 = tmp1.z;
		// right top
		tmp1.set(rotation);
		tmp1.conjugate();
		tmp1.mulLeft(halfWidth, halfHeight, 0, 0).mulLeft(rotation);
		x3 = tmp1.x;
		y3 = tmp1.y;
		z3 = tmp1.z;
		// left top
		tmp1.set(rotation);
		tmp1.conjugate();
		tmp1.mulLeft(-halfWidth, halfHeight, 0, 0).mulLeft(rotation);
		x4 = tmp1.x;
		y4 = tmp1.y;
		z4 = tmp1.z;
		
		// normal
		tmp1.set(rotation);
		tmp1.conjugate();
		//tmp1.mulLeft(0,  0.5F, -0.5F, 0).mulLeft(rotation);
		tmp1.mulLeft(normal.x, normal.y, normal.z, 0).mulLeft(rotation);
		
		vertices[idx   ] = x1 + x;
		vertices[idx+1 ] = y1 + y;
		vertices[idx+2 ] = z1 + z;
		vertices[idx+3 ] = tmp1.x;
		vertices[idx+4 ] = tmp1.y;
		vertices[idx+5 ] = tmp1.z;
		vertices[idx+6 ] = u2;
		vertices[idx+7 ] = v2;

		vertices[idx+8 ] = x2 + x;
		vertices[idx+9 ] = y2 + y;
		vertices[idx+10] = z2 + z;
		vertices[idx+11] = tmp1.x;
		vertices[idx+12] = tmp1.y;
		vertices[idx+13] = tmp1.z;
		vertices[idx+14] = u;
		vertices[idx+15] = v2;

		vertices[idx+16] = x3 + x;
		vertices[idx+17] = y3 + y;
		vertices[idx+18] = z3 + z;
		vertices[idx+19] = tmp1.x;
		vertices[idx+20] = tmp1.y;
		vertices[idx+21] = tmp1.z;
		vertices[idx+22] = u;
		vertices[idx+23] = v;

		vertices[idx+24] = x4 + x;
		vertices[idx+25] = y4 + y;
		vertices[idx+26] = z4 + z;
		vertices[idx+27] = tmp1.x;
		vertices[idx+28] = tmp1.y;
		vertices[idx+29] = tmp1.z;
		vertices[idx+30] = u2;
		vertices[idx+31] = v;

		this.idx += 32;
	}

	private void switchTexture(Texture texture) {
		flush();		
		TextureFilter minFilter = texture.getMinFilter();
		TextureFilter magFilter = texture.getMagFilter();		
		TextureWrap uWrap = texture.getUWrap();
		TextureWrap vWrap = texture.getVWrap();		
		textureAttribute.textureDescription.set(texture, minFilter, magFilter, uWrap, vWrap);
	}

	public void begin(ModelBatch modelBatch, Environment environment) {
		this.modelBatch = modelBatch;
		this.environment = environment;
	}

	public void setRotation (Vector3 dir, Vector3 up) {
		tmp2.set(up).crs(dir).nor();
		float x = tmp2.x, y = tmp2.y, z = tmp2.z;
		tmp2.set(dir).crs(x, y, z).nor();
		rotation.setFromAxes(x, tmp2.x, dir.x, y, tmp2.y, dir.y, z, tmp2.z, dir.z);
	}

	public void end() {
		flush();
	}

	public void flush() {

		if (idx == 0) return;

		int spritesInBatch = idx >> 5;
		int count = spritesInBatch * 6;

		Mesh mesh = this.mesh;
		mesh.setVertices(vertices, 0, idx);
		mesh.getIndicesBuffer().position(0).limit(count);
		renderable.meshPart.size = count;
		renderable.environment = environment;
		modelBatch.render(renderable);
		modelBatch.flush();
		idx = 0;

	}
	
	public Vector3 getNormal() {
		return normal;
	}

	@Override
	public void dispose() {
		mesh.dispose();
	}

}
