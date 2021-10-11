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
package com.delmesoft.editor3d.graphics;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

public class MeshBuilder {
	
	private float[] vertices;	
	private short[] indices;
	
	private int vertexCount;
	private int indexCount;
	
	private VertexAttributes attributes;
	private int stride;

	public short iOff;
	
	public MeshBuilder() {
		vertices = new float[16];
		indices  = new short[8 ];
	}
	
	public MeshBuilder(VertexAttributes attributes) {
		this();
		setAttributes(attributes);
	}
	
	public MeshBuilder(long usage) {
		this(getAttributes(usage));
	}
	
	public void setAttributes(VertexAttributes attributes) {
		this.attributes = attributes;
		this.stride = attributes.vertexSize >> 2;
	}
	
	public void setAttibutes(long usage) {
		setAttributes(getAttributes(usage));
	}

	private void ensureVertexCapacity(int capacity) {
		if (capacity >= vertices.length) {
			float[] tmp = new float[(int) (capacity * 1.75F)];
			System.arraycopy(vertices, 0, tmp, 0, vertexCount);
			vertices = tmp;
		}
	}

	private void ensureIndexCapacity(int capacity) {
		if (capacity >= indices.length) {
			short[] tmp = new short[(int) (capacity * 1.75F)];
			System.arraycopy(indices, 0, tmp, 0, indexCount);
			indices = tmp;
		}
	}

	public void addVertices(float... vertices) {
		int n = vertices.length;
		ensureVertexCapacity(vertexCount + n);
		System.arraycopy(vertices, 0, this.vertices, vertexCount, n);
		vertexCount += n;
	}

	public void addIndices(short... indices) {
		int n = indices.length;
		ensureIndexCapacity(indexCount + n);
		System.arraycopy(indices, 0, this.indices, indexCount, n);
		indexCount += n;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public void clear() {
		vertexCount = 0;
		indexCount = 0;
		iOff = 0;
	}

	public Mesh end(boolean clear) {
		
		Mesh mesh = new Mesh(true, vertexCount / stride, indexCount, attributes);
		mesh.setVertices(vertices, 0, vertexCount).setIndices(indices, 0, indexCount);
		
		if(clear) {
			clear();
		}
		
		return mesh;
	}
	
	public static VertexAttributes getAttributes(long usage) {
		
		final Array<VertexAttribute> attrs = new Array<VertexAttribute>();
		if ((usage & Usage.Position) == Usage.Position)
			attrs.add(new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
		if ((usage & Usage.ColorPacked) == Usage.ColorPacked)
			attrs.add(new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
		else if ((usage & Usage.ColorUnpacked) == Usage.ColorUnpacked)
			attrs.add(new VertexAttribute(Usage.ColorUnpacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
		if ((usage & Usage.Normal) == Usage.Normal)
			attrs.add(new VertexAttribute(Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
		if ((usage & Usage.TextureCoordinates) == Usage.TextureCoordinates)
			attrs.add(new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0"));
		final VertexAttribute attributes[] = new VertexAttribute[attrs.size];
		for (int i = 0; i < attributes.length; i++)
			attributes[i] = attrs.get(i);

		return new VertexAttributes(attributes);		
		
	}
	

}
