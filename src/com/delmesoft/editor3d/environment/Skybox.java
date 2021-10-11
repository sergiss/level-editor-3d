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
package com.delmesoft.editor3d.environment;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.graphics.MeshBuilder;
import com.delmesoft.editor3d.level.block.Side;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Skybox implements Disposable {

	private ShaderProgram program;

	private int u_worldView;
	private int u_texture;

	private Matrix4 transform;
	private Matrix4 worldView;
	
	private String testureAtlasPath;
	private TextureAtlas textureAtlas;
	
	private Map<Integer, String> regionMap;
	
	private Map<Texture, Mesh> meshMap;
	
	private boolean dirty;

	public Skybox() {

		program = new ShaderProgram(vertexShader, fragmentShader);
		if (!program.isCompiled())
			throw new GdxRuntimeException(program.getLog());

		u_worldView = program.getUniformLocation("u_worldView");
		u_texture   = program.getUniformLocation("u_texture");

		transform = new Matrix4();
		worldView = new Matrix4();
		
		regionMap = new HashMap<>();
		meshMap   = new HashMap<>();
	}
	
	public void loadTextureAtlas(String path) {
		
		this.testureAtlasPath = path;
		regionMap.clear();
		
		if(path != null) {
			textureAtlas = FileManager.getInstance().get(path, TextureAtlas.class);
			dirty = true;
		} else {
			textureAtlas = null;
		}
		
	}

	public String getTextureAtlasPath() {
		return testureAtlasPath;
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public void setRegion(Side side, String name) {
		if (name != null) {
			regionMap.put(side.ordinal(), name);
		} else {
			regionMap.remove(side.ordinal());
		}
		this.dirty = true;
	}
	
	public Map<Integer, String> getRegionMap() {
		return regionMap;
	}
	
	public AtlasRegion getAtlasRegionBySide(Side side) {
		return textureAtlas.findRegion(regionMap.get(side.ordinal()));
	}

	public String[] getAtlasRegionNames(Side... sides) {
		String[] names = new String[sides.length];
		for (Side side : sides) {
			names[side.ordinal()] = regionMap.get(side.ordinal());
		}
		return names;
	}

	public void render(RenderContext context, Camera camera){

		if(testureAtlasPath != null && regionMap.size() > 0) {

			if(dirty) {				
				generateMeshes();
				dirty = false;
			}
			
			context.setCullFace(GL20.GL_BACK);
			
			//Gdx.gl.glFrontFace(GL20.GL_CCW); // TODO : 

			context.setBlending(false, 0, 0);
			context.setDepthTest(0);
			
			context.setDepthMask(false);

			System.arraycopy(camera.view.val, 0, transform.val, 0, 12);
			worldView.set(camera.projection);
			worldView.mul(transform);
									
			program.begin();
			for(Entry<Texture, Mesh> entry : meshMap.entrySet()) {
				program.setUniformMatrix(u_worldView, worldView);
				program.setUniformi(u_texture, context.textureBinder.bind(entry.getKey()));
				entry.getValue().render(program, GL20.GL_TRIANGLES);
			}
			program.end();
			
		} else {
			disposeMeshes();
		}

	}

	private void generateMeshes() {		
		
		disposeMeshes();		
		
		Map<Texture, MeshBuilder> builderMap = new HashMap<>();
	
		MeshBuilder meshBuilder;
		short iOff;
		
		Side side = Side.TOP;		
		AtlasRegion region = textureAtlas.findRegion(regionMap.get(side.ordinal()));

		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices(-1, 0.75f,  1, region.getU() , region.getV(),
									 1, 0.75f,  1, region.getU2(), region.getV(), 
									 1, 0.75f, -1, region.getU2(), region.getV2(), 
									-1, 0.75f, -1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		side = Side.BOTTOM;
		region = textureAtlas.findRegion(regionMap.get(side.ordinal()));
		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices(-1, -0.25f, -1, region.getU() , region.getV(),
									 1, -0.25f, -1, region.getU2(), region.getV(), 
									 1, -0.25f,  1, region.getU2(), region.getV2(), 
									-1, -0.25f,  1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		side = Side.FRONT;
		region = textureAtlas.findRegion(regionMap.get(side.ordinal()));
		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices(-1,  0.75f,  -1, region.getU() , region.getV(),
									 1,  0.75f,  -1, region.getU2(), region.getV(), 
									 1, -0.25f,  -1, region.getU2(), region.getV2(), 
								    -1, -0.25f,  -1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		side = Side.LEFT;
		region = textureAtlas.findRegion(regionMap.get(side.ordinal()));
		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices(-1,  0.75f,  1, region.getU() , region.getV(),
									-1,  0.75f, -1, region.getU2(), region.getV(), 
									-1, -0.25f, -1, region.getU2(), region.getV2(), 
									-1, -0.25f,  1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		side = Side.BACK;
		region = textureAtlas.findRegion(regionMap.get(side.ordinal()));
		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices( 1,  0.75f,  1, region.getU() , region.getV(),
									-1,  0.75f,  1, region.getU2(), region.getV(), 
									-1, -0.25f,  1, region.getU2(), region.getV2(), 
									 1, -0.25f,  1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		side = Side.RIGHT;
		region = textureAtlas.findRegion(regionMap.get(side.ordinal()));
		if(region != null) {
			meshBuilder = builderMap.get(region.getTexture());
			if(meshBuilder == null) {
				meshBuilder = new MeshBuilder(Usage.Position | Usage.TextureCoordinates);
				builderMap.put(region.getTexture(), meshBuilder);
			}
			meshBuilder.addVertices( 1,  0.75f, -1, region.getU() , region.getV(),
									 1,  0.75f,  1, region.getU2(), region.getV(), 
									 1, -0.25f,  1, region.getU2(), region.getV2(), 
									 1, -0.25f, -1, region.getU() , region.getV2());
			
			iOff = meshBuilder.iOff;
			meshBuilder.addIndices(iOff, (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), iOff);
			meshBuilder.iOff += 4;
		}
		
		Iterator<Entry<Texture, MeshBuilder>> it = builderMap.entrySet().iterator();		
		Entry<Texture, MeshBuilder> entry;
		while(it.hasNext()) {
			entry = it.next();
			meshMap.put(entry.getKey(), entry.getValue().end(true));			
		}
		
	}
	
	public void clear() {
		regionMap.clear();
	}

	@Override
	public void dispose() {
		program.dispose();
		disposeMeshes(); 
	}

	private void disposeMeshes() {
		if (meshMap.size() > 0) {
			for (Mesh mesh : meshMap.values()) {
				mesh.dispose();
			}
			meshMap.clear();
		}
	}

	private String vertexShader = 
					"attribute vec4 a_position;\n" + 
					"attribute vec2 a_texCoord0;\n" + 
					"uniform mat4 u_worldView;\n" + 
					"varying vec2 v_texCoords;\n" +  
					"void main()\n" + 
					"{\n" + 
					"   v_texCoords = a_texCoord0;\n" + 
					"   gl_Position = u_worldView * a_position;\n" + 
					"}" ;

	private String fragmentShader = 
					"#ifdef GL_ES\n" +
					"precision mediump float;\n" + 
					"#endif\n" + 
					"varying vec2 v_texCoords;\n" + 
					"uniform sampler2D u_texture;\n" + 
					"void main()\n" + 
					"{\n" + 
					"  gl_FragColor = texture2D(u_texture, v_texCoords);\n" +
					"}";

}
