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
package com.delmesoft.editor3d.graphics.g3d;

import java.util.HashMap;
import java.util.Map;

import com.delmesoft.editor3d.level.block.Plot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ChunkRendererImpl implements ChunkRenderer {
	
	public static final Vector3[] BLOCK_LIGHT_COLORS;
	public static final float[] BLOCK_LIGHT_DATA;
	
	static {
		
		String[] hexColors = { "#ffffff", "#ff0000", "#ffd500", "#55ff00", "#00ff80", "#00aaff", "#ff00ff" };
		
		int n = hexColors.length;
		BLOCK_LIGHT_COLORS = new Vector3[n];

		String hex;
		for(int i = 0; i < n; ++i) {
			hex = hexColors[i];
			int r = Integer.valueOf(hex.substring(1, 3), 16);
			int g = Integer.valueOf(hex.substring(3, 5), 16);
			int b = Integer.valueOf(hex.substring(5, 7), 16);
			BLOCK_LIGHT_COLORS[i] = new Vector3(r / 255F, g / 255F, b / 255F);
		}
		
		n *= 3;
		BLOCK_LIGHT_DATA = new float[n];
		for(int i = 0, j = 0; i < n; i += 3, j++) {
			BLOCK_LIGHT_DATA[i    ] = BLOCK_LIGHT_COLORS[j].x;
			BLOCK_LIGHT_DATA[i + 1] = BLOCK_LIGHT_COLORS[j].y;
			BLOCK_LIGHT_DATA[i + 2] = BLOCK_LIGHT_COLORS[j].z;
		}
		
	}
	
	private RenderContext context;
	
	private Texture texture;
	
	private Camera camera;
	
	// Ambient light
	private Vector3 color;
	private float light;	
	
	private Shader opaqueShader;
	
	private Shader blendShaser;
	
	private Array<Plot> blendPlots;
	
	private Array<PointLight> pointLights;
	
	public ChunkRendererImpl(RenderContext renderContext) {
		
		this.context = renderContext;
		
		String vert = Gdx.files.internal("shaders/opaqueShader.vert").readString();
		String frag = Gdx.files.internal("shaders/opaqueShader.frag").readString();
		
		vert = vert.replaceAll("MAX_POINT_LIGHTS", String.valueOf(Shader.MAX_POINT_LIGHTS));

		opaqueShader = new Shader(vert, frag);
				
		//vert = Gdx.files.internal("shaders/opaqueShader.vert").readString();
		frag = Gdx.files.internal("shaders/blendShader.frag").readString();
		
		blendShaser = new Shader(vert, frag);
		
		blendPlots = new Array<Plot>();
	
	}

	@Override
	public void begin(Vector3 color, float light, Array<PointLight> pointLights, Camera camera) {
	
		this.color = color;
		this.light = light;
		
		this.pointLights = pointLights;
		
		this.camera = camera;
		
		context.setCullFace(GL20.GL_BACK);
		context.setDepthTest(GL20.GL_LESS, 0f, 1f);
		
		context.setDepthMask(true);
		context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		opaqueShader.begin(camera, color, light, pointLights, texture);
		
	}

	@Override
	public void render(Plot plot) {
		if (plot.opaqueMesh != null) {
			opaqueShader.render(plot.opaqueMesh);
		}
		if (plot.blendedMesh != null) {
			blendPlots.add(plot);
		}	
	}

	@Override
	public void end() {
		
		opaqueShader.end();
		
		if(blendPlots.size > 0) {			
			context.setCullFace(GL20.GL_NONE);
			context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			blendShaser.begin(camera, color, light, pointLights, texture);
			for(Plot plot : blendPlots) {
				blendShaser.render(plot.blendedMesh);
			}
			blendShaser.end();
			blendPlots.clear();
		}
		
	}

	@Override
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}

	@Override
	public void dispose() {
		opaqueShader.dispose();
		blendShaser.dispose();
	}
	
	private class Shader {
		
		public static final int MAX_POINT_LIGHTS = 10;
		
		private ShaderProgram program;
		
		private int u_texture;
		private int u_projTrans;
		private int u_color;
		private int u_light;
		private int u_numPointLights;
		
		private Map<String, Integer> locationMap;
		
		public Shader(String vert, String frag) {
			
			program = new ShaderProgram(vert, frag);
			u_projTrans = program.getUniformLocation("u_projTrans");
			u_light 	= program.getUniformLocation("u_light");
			u_color     = program.getUniformLocation("u_color");
			u_texture   = program.getUniformLocation("u_texture");
			
			locationMap = new HashMap<>();
			
			u_numPointLights = program.getUniformLocation("u_numPointLights");
			for(int i = 0; i < MAX_POINT_LIGHTS; ++i) {
				locationMap.put("u_pointLight.color"    + i, program.getUniformLocation("u_pointLights[" + i + "].color"));
				locationMap.put("u_pointLight.position" + i, program.getUniformLocation("u_pointLights[" + i + "].position"));
				locationMap.put("u_pointLight.radius"   + i, program.getUniformLocation("u_pointLights[" + i + "].radius"));
			}
			
			locationMap.put("u_blockLightColors", program.getUniformLocation("u_blockLightColors[0]"));
			
		}
		
		public void begin(Camera camera, Vector3 color, float light, Array<PointLight> pointLights, Texture texture) {
			
			program.begin();
			program.setUniformMatrix(u_projTrans, camera.combined);
			program.setUniformf(u_color, color);
			program.setUniformf(u_light, light);
					
			if(pointLights != null) {
				int n = Math.min(pointLights.size, MAX_POINT_LIGHTS);
				program.setUniformi(u_numPointLights, n);			
				PointLight pointLight;						
				for(int i = 0; i < n; ++i) {
					pointLight = pointLights.get(i);
					program.setUniformf(locationMap.get("u_pointLight.color" + i)   , pointLight.color.r, pointLight.color.g, pointLight.color.b);
					program.setUniformf(locationMap.get("u_pointLight.position" + i), pointLight.position);
					program.setUniformf(locationMap.get("u_pointLight.radius" + i)  , pointLight.intensity);
				}
			}
		
			program.setUniform3fv(locationMap.get("u_blockLightColors"), BLOCK_LIGHT_DATA, 0, BLOCK_LIGHT_DATA.length);						
			program.setUniformi(u_texture, context.textureBinder.bind(texture));
			
		}
		
		public void render(Mesh mesh) {
			mesh.render(program, GL20.GL_TRIANGLES);
		}
		
		public void end() {
			program.end();
		}
		
		public void dispose() {
			program.dispose();
		}
		
	}

}
