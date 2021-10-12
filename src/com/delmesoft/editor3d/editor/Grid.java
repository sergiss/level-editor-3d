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
package com.delmesoft.editor3d.editor;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.entity.Entity;
import com.delmesoft.editor3d.environment.PointLight;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.graphics.FlyCamera;
import com.delmesoft.editor3d.graphics.MeshBuilder;
import com.delmesoft.editor3d.graphics.RenderHelper;
import com.delmesoft.editor3d.graphics.g3d.decal.Decal;
import com.delmesoft.editor3d.level.Level;
import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.utils.Vec3i;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.MeshPart;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

public class Grid implements Disposable {
	
	public final float GRID_OFFSET = 1 / 32F;
	
	protected Level level;
	
	private Settings settings;
	
	protected Renderable grid;
	protected BoundingBox gridBoundingBox;
	private BoundingBox tmp;

	protected Array<Vec3i> positions;
	protected Array<Entity> entities;
	protected Array<Decal> decals;
	protected Array<PointLight> pointLights;
	protected Array<Zone> zones;

	private float boxRadius;

	private Material boxMaterial;
	private Material lightMaterial;
	private Material zoneMaterial;
	
	private Mesh boxMesh;
		
	protected int posY, maxY;
	
	protected Rectangle selectionArea;
	
	// Arrow
	private final Texture arrowTexture;

	public Grid(Level level) {
		this.level = level;
		
		this.settings = level.getSettings();
				
		posY = 1;
		maxY = settings.aHeight;
		
		MeshBuilder meshBuilder = new MeshBuilder();
		
		Texture texture = FileManager.getInstance().get("textures/grid_tile.png", Texture.class);
		//texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		final TextureAttribute textureAttribute = TextureAttribute.createDiffuse(texture);
		
		{

			grid = RenderHelper.newRenderable();
			grid.material.set(textureAttribute);

			final BlendingAttribute blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			blendingAttribute.opacity = 0.55f;
			grid.material.set(blendingAttribute);
			grid.material.set(new IntAttribute(IntAttribute.CullFace, GL20.GL_NONE));
			
			meshBuilder.setAttibutes(Usage.Position | Usage.TextureCoordinates);
			
			meshBuilder.addVertices(0F,              0F, 0F             , 0F             , 0F, 
									settings.aWidth, 0F, 0F             , settings.aWidth, 0F, 
									settings.aWidth, 0F, settings.aDepth, settings.aWidth, settings.aDepth, 
					 				0F,              0F, settings.aDepth, 0F             , settings.aDepth);

			meshBuilder.addIndices((short) 0, (short) 3, (short) 2, (short) 2, (short) 1, (short) 0);

			Mesh mesh = meshBuilder.end(true);;

			grid.meshPart.mesh = mesh;
			grid.meshPart.size = mesh.getNumIndices();

			gridBoundingBox = mesh.calculateBoundingBox();
			tmp = new BoundingBox();

			grid.worldTransform.val[13] = posY + GRID_OFFSET;
			
		}
		
		BlendingAttribute blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		blendingAttribute.opacity = 0.89f;
		
		{// BOXES
			
			positions = new Array<Vec3i>();
			boxRadius = (float) (Math.sqrt(3) * 0.5f);
			
			final ColorAttribute colorAttribute = new ColorAttribute(ColorAttribute.Diffuse, Color.RED);

			boxMaterial = new Material();
			boxMaterial.set(textureAttribute);
			boxMaterial.set(colorAttribute);
			boxMaterial.set(blendingAttribute);

			meshBuilder.setAttibutes(Usage.Position | Usage.TextureCoordinates);

			short iOff = 0;
			for (iOff = 0; iOff < 24; iOff += 4) {
				meshBuilder.addIndices((short) (iOff + 0), (short) (iOff + 3), (short) (iOff + 2), (short) (iOff + 2), (short) (iOff + 1), (short) (iOff + 0));
			}

			// TOP
			meshBuilder.addVertices(0, 0, 0, 0, 0);
			meshBuilder.addVertices(1, 0, 0, 1, 0);
			meshBuilder.addVertices(1, 0, 1, 1, 1);
			meshBuilder.addVertices(0, 0, 1, 0, 1);

			// BOTTOM
			meshBuilder.addVertices(1, -1, 0, 0, 0);
			meshBuilder.addVertices(0, -1, 0, 1, 0);
			meshBuilder.addVertices(0, -1, 1, 1, 1);
			meshBuilder.addVertices(1, -1, 1, 0, 1);

			// FRONT
			meshBuilder.addVertices(0,  0, 1, 0, 0);
			meshBuilder.addVertices(1,  0, 1, 1, 0);
			meshBuilder.addVertices(1, -1, 1, 1, 1);
			meshBuilder.addVertices(0, -1, 1, 0, 1);

			// BACK
			meshBuilder.addVertices(1,  0, 0, 0, 0);
			meshBuilder.addVertices(0,  0, 0, 1, 0);
			meshBuilder.addVertices(0, -1, 0, 1, 1);
			meshBuilder.addVertices(1, -1, 0, 0, 1);

			// RIGHT
			meshBuilder.addVertices(1,  0, 1, 0, 0);
			meshBuilder.addVertices(1,  0, 0, 1, 0);
			meshBuilder.addVertices(1, -1, 0, 1, 1);
			meshBuilder.addVertices(1, -1, 1, 0, 1);

			// LEFT
			meshBuilder.addVertices(0,  0, 0, 0, 0);
			meshBuilder.addVertices(0,  0, 1, 1, 0);
			meshBuilder.addVertices(0, -1, 1, 1, 1);
			meshBuilder.addVertices(0, -1, 0, 0, 1);

			boxMesh = meshBuilder.end(true);
			
		}
		
		{ // PointLights
			
			ColorAttribute colorAttribute = new ColorAttribute(ColorAttribute.Diffuse, Color.ORANGE);
					
			lightMaterial = new Material();
			lightMaterial.set(textureAttribute);
			lightMaterial.set(colorAttribute);
			
		}
		
		
		{ // Zones
			
			ColorAttribute colorAttribute = new ColorAttribute(ColorAttribute.Diffuse, Color.CYAN);
					
			zoneMaterial = new Material();
			zoneMaterial.set(textureAttribute);
			zoneMaterial.set(colorAttribute);
			zoneMaterial.set(blendingAttribute);
			zoneMaterial.set(IntAttribute.createCullFace(GL20.GL_NONE));
			
		}
		
		// Arrow
		arrowTexture = FileManager.getInstance().get("textures/arrow.png", Texture.class);
		
		entities = new Array<Entity>();
		decals = new Array<Decal>();
		pointLights = new Array<PointLight>();
		zones = new Array<Zone>();
				
	}
	
	public void render(ModelBatch modelBatch) {

		final Camera camera = level.getCamera();
		
		modelBatch.begin(camera);
		if (positions.size > 0 || entities.size > 0 || decals.size > 0 || pointLights.size > 0 || zones.size > 0) {
			modelBatch.render(boxRenderer);
			modelBatch.flush();
		}
		modelBatch.render(grid);
		modelBatch.end();
				
	}
	
	public void renderScreen(ShapeRenderer shapeRenderer) {
		
		if(selectionArea != null) {			
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Color color = new Color(Color.PURPLE);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Line);
			color.a = 0.9F;
			shapeRenderer.setColor(color);
			shapeRenderer.rect(selectionArea.x, selectionArea.y, selectionArea.width, selectionArea.height);
			shapeRenderer.end();
			
			shapeRenderer.begin(ShapeType.Filled);
			color.a = 0.4F;
			shapeRenderer.setColor(color);
			shapeRenderer.rect(selectionArea.x, selectionArea.y, selectionArea.width, selectionArea.height);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}
	
	public void renderArrow(SpriteBatch spriteBatch) {
		final Camera camera = level.getCamera();

		if(camera instanceof FlyCamera) {
			
			float scl = 0.5F;		
			float width  = arrowTexture.getWidth()  * scl;
			float height = arrowTexture.getHeight() * scl;
			
			spriteBatch.begin();		
			spriteBatch.draw(arrowTexture, camera.viewportWidth - width * 1.15F, height * 0.15F, width  * 0.5F, height * 0.5F, width, height, 1F, 1F, ((FlyCamera) camera).getRotation().y, 0, 0, arrowTexture.getWidth(), arrowTexture.getHeight(), false, false);
			spriteBatch.end();

		}

	}
	
	private RenderableProvider boxRenderer = new RenderableProvider() {
		
		Vector3 tmp1 = new Vector3();
		Vector3 tmp2 = new Vector3();

		@Override
		public void getRenderables(com.badlogic.gdx.utils.Array<Renderable> renderables, Pool<Renderable> pool) {
			
			tmp2.set(1.01F, 1.01F, 1.01F);
			
			final Camera camera = level.getCamera();
			final Array<Vec3i> positions = Grid.this.positions;
			
			MeshPart meshPart;
			Renderable renderable;
			for(int i = 0, n = positions.size; i < n; ++i) {
				
				final Vec3i position = positions.get(i);
				
				tmp1.set(position.x, position.y, position.z);
				
				if(camera.frustum.sphereInFrustum(tmp1.x, tmp1.y - 0.5F, tmp1.z, boxRadius)){
					
					tmp1.add(-0.005F, 0.005F, -0.005F);
					
					renderable = pool.obtain();			
					
					renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2); // scaling es necesario?
					renderable.material = boxMaterial;					
					meshPart = renderable.meshPart;					
					meshPart.mesh = boxMesh;
					meshPart.size = boxMesh.getNumIndices();
					meshPart.offset = 0;
					meshPart.primitiveType = GL20.GL_TRIANGLES;
					
					renderables.add(renderable);
				}
				
			}
			
			final Array<Entity> entities = Grid.this.entities;
			BoundingBox boundingBox = new BoundingBox();
			Entity entity;
			for(int i = 0, n = entities.size; i < n; ++i) {
				entity = entities.get(i);
				entity.getBoundingBox(boundingBox);
				if(camera.frustum.boundsInFrustum(boundingBox)) {
					boundingBox.getCorner010(tmp1);
					tmp2.set(boundingBox.getWidth(), boundingBox.getHeight(), boundingBox.getDepth());
					renderable = pool.obtain();				
					renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2); // scaling es necesario?
					renderable.material = boxMaterial;					
					meshPart = renderable.meshPart;					
					meshPart.mesh = boxMesh;
					meshPart.size = boxMesh.getNumIndices();
					meshPart.offset = 0;
					meshPart.primitiveType = GL20.GL_TRIANGLES;
					
					renderables.add(renderable);
				}
			}
			
			final Array<Decal> decals = Grid.this.decals;
			Decal decal;
			for(int i = 0, n = decals.size; i < n; ++i) {
				decal = decals.get(i);
				if(decal.isVisible(camera)) {
					tmp2.set(decal.halfWidth * 2F, decal.halfHeight * 2F, decal.halfWidth * 2F);
					tmp1.set(decal.position).sub(decal.halfWidth, -decal.halfHeight, decal.halfWidth);
					renderable = pool.obtain();				
					renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2); // scaling es necesario?
					renderable.material = boxMaterial;					
					meshPart = renderable.meshPart;					
					meshPart.mesh = boxMesh;
					meshPart.size = boxMesh.getNumIndices();
					meshPart.offset = 0;
					meshPart.primitiveType = GL20.GL_TRIANGLES;
					
					renderables.add(renderable);
				}
			}
			
			tmp2.set(0.5F, 0.5F, 0.5F);
			final Array<PointLight> pointLights = Grid.this.pointLights;
			PointLight pointLight;
			for(int i = 0, n = pointLights.size; i < n; ++i) {
				pointLight = pointLights.get(i);
				if(pointLight.isVisible(camera, Constants.maxLightDistance)) {
					renderable = pool.obtain();
					tmp1.set(pointLight.light.position).sub(0.25F, -0.25F, 0.25F);
					renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2);
					renderable.material = lightMaterial;					
					meshPart = renderable.meshPart;					
					meshPart.mesh = boxMesh;
					meshPart.size = boxMesh.getNumIndices();
					meshPart.offset = 0;
					meshPart.primitiveType = GL20.GL_TRIANGLES;
					
					renderables.add(renderable);
				}
				
			}
			
			
			
			final Array<Zone> zones = Grid.this.zones;
			Zone zone;
			for(int i = 0, n = zones.size; i < n; ++i) {
				zone = zones.get(i);
				if(zone.isVisible(camera, Constants.maxLightDistance)) {
					renderable = pool.obtain();
					tmp1.set(zone.position).sub(zone.dimensions.x * 0.5F, -zone.dimensions.y * 0.5F, zone.dimensions.z * 0.5F);
					tmp2.set(zone.dimensions.x, zone.dimensions.y, zone.dimensions.z);
					renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2);
					renderable.material = zoneMaterial;					
					meshPart = renderable.meshPart;					
					meshPart.mesh = boxMesh;
					meshPart.size = boxMesh.getNumIndices();
					meshPart.offset = 0;
					meshPart.primitiveType = GL20.GL_TRIANGLES;
					
					renderables.add(renderable);
				}
				
			}
			
		}
		
	};

	public Array<Vec3i> getBlockPositions() {
		return positions;
	}
		
	public Array<Entity> getEntities() {
		return entities;
	}
	
	public Array<Decal> getDecals() {
		return decals;
	}
	
	public Array<PointLight> getPointLights() {
		return pointLights;
	}
	
	public Array<Zone> getZones() {
		return zones;
	}

	public BoundingBox getGridBoundingBox() {
		return tmp.set(gridBoundingBox).mul(grid.worldTransform);
	}
	
	public Level getLevel() {
		return level;
	}

	@Override
	public void dispose() {
		grid.meshPart.mesh.dispose();
		boxMesh.dispose();
	}
	
	public Mesh getBoxMesh() {
		return boxMesh;
	}
	
	public void showSelectionArea(float minX, float minY, float maxX, float maxY) {
		
		if(selectionArea == null) {
			selectionArea = new Rectangle();
		}
		if(minX > maxX) {
			float tmp = minX;
			minX = maxX;
			maxX = tmp;
		}
		if(minY > maxY) {
			float tmp = minY;
			minY = maxY;
			maxY = tmp;
		}
		
		selectionArea.set(minX, minY, maxX - minX, maxY - minY);
		
	}
	
	public void hideSelectionArea() {
		selectionArea = null;
	}
	
	public Rectangle getSelectionArea() {
		return selectionArea;
	}

}
