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
package com.delmesoft.editor3d.level;

import static com.delmesoft.editor3d.utils.Utils.fastCeil;
import static com.delmesoft.editor3d.utils.Utils.fastFloor;

import java.util.Comparator;

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.entity.Entity;
import com.delmesoft.editor3d.environment.PointLight;
import com.delmesoft.editor3d.environment.Skybox;
import com.delmesoft.editor3d.files.FileManager;
import com.delmesoft.editor3d.files.TextureLoader;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.graphics.g3d.decal.Decal;
import com.delmesoft.editor3d.graphics.g3d.decal.DecalRenderer;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.level.block.Plot;
import com.delmesoft.editor3d.level.block.generators.ChunkGenerator;
import com.delmesoft.editor3d.level.block.generators.ChunkGeneratorImpl;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.Vec3i;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class LevelImpl implements Level {
	
	private Presenter presenter;
	private Settings settings;
	
	private Chunk[] chunks;
	
	private Material opaqueMaterial;
	private Material blendMaterial;
	private TiledTexture tiledTexture;
	
	private Environment environment;
	private ColorAttribute ambientLight;
	private DirectionalLight skyLight;
	
	private Array<PointLight> pointLights;
	private Material boxMaterial1;
	private Material boxMaterial2;
		
	private Array<Chunk> renderChunks; // Generated Chunks
	private Array<Entity> entities;
	
	private ChunkGenerator chunkGenerator;
	
	private boolean forceUpdate = true;	
	
	private DecalRenderer decalRenderer;	
	private Array<Decal> decals;
	
	private Array<Zone> zones;
	
	private Skybox skybox;
	
	public LevelImpl(Presenter presenter, Settings settings) {
		
		this.presenter = presenter;
		this.settings = settings;
		
		int n = settings.chunkCount;
		chunks = new Chunk[n];
		for(int i = 0; i < n; ++i) {
			chunks[i] = new Chunk(this).initialize(i / settings.rDepth, i % settings.rDepth);
		}
		
		createTiledTexture(settings);
		
		Texture texture = tiledTexture.getTexture();
		
	    TextureAttribute textureAttribute = TextureAttribute.createDiffuse(texture);
	    
	    opaqueMaterial = new Material();
	    opaqueMaterial.set(textureAttribute);
	    
	    blendMaterial = new Material();	 
	    blendMaterial.set(textureAttribute);
	    blendMaterial.set(FloatAttribute.createAlphaTest(0.1F));
	    blendMaterial.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
	    blendMaterial.set(IntAttribute.createCullFace(GL20.GL_NONE));
				
		environment = new Environment();	
		ambientLight = new ColorAttribute(ColorAttribute.AmbientLight);
		ambientLight.color.set(0.5F, 0.5F, 0.5F, 1);
		
		skyLight = new DirectionalLight().set(0, 0, 0, -0.5F, -1F, -0.25F);
		
		pointLights = new Array<>();		
								
		renderChunks = new Array<>();
		entities = new Array<>();
		
		chunkGenerator = new ChunkGeneratorImpl(this);
		
		decalRenderer = new DecalRenderer();
		decalRenderer.getNormal().set(0F, 0.5F, -0.5F); // Light reflection 
		
		decals = new Array<>();
		zones = new Array<>();
		
		final ColorAttribute colorAttribute1 = new ColorAttribute(ColorAttribute.Diffuse, Color.YELLOW);
		final ColorAttribute colorAttribute2 = new ColorAttribute(ColorAttribute.Diffuse, Color.CORAL);
		final BlendingAttribute blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		blendingAttribute.opacity = 0.5f;
		
		boxMaterial1 = new Material();
		boxMaterial1.set(colorAttribute1);
		boxMaterial1.set(blendingAttribute);
		
		boxMaterial2 = new Material();
		boxMaterial2.set(colorAttribute2);
		boxMaterial2.set(blendingAttribute);
		
		skybox = new Skybox();
				
	}

	private void createTiledTexture(Settings settings) {
		
		TextureLoader.useMipMaps = true;
		Texture texture = FileManager.getInstance().get(settings.texturePath, Texture.class); // Load texture
		tiledTexture = new TiledTexture(texture, settings.tileWidth, settings.tileHeight,  settings.margin,  settings.spacing);
					
		texture.setFilter(TextureFilter.MipMapNearestLinear, TextureFilter.Nearest);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL30.GL_TEXTURE_BASE_LEVEL, 0);
		Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAX_LEVEL , 2);
				
	}

	@Override
	public void render(ModelBatch modelBatch) {
		
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		Camera camera = presenter.getCamera();
		chunkGenerator.update(((int) camera.position.x) >> Plot.BIT_OFFSET, ((int) camera.position.z) >> Plot.BIT_OFFSET, forceUpdate);
		forceUpdate = false;
		
		environment.clear();
		//environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4F, 0.4F, 0.4F, 1.F));
		//environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13F, 0.13F, 0.13F, 1F));
		//environment.add(new DirectionalLight().set(0.8F, 0.8F, 0.8F, -1F, -0.8F, -0.2F));
		environment.set(ambientLight);
		skyLight.color.set(ambientLight.color);
		environment.add(skyLight);
		
		for(PointLight pointLight : pointLights) {
			if (pointLight.isVisible(camera, Constants.maxLightDistance)) {
				environment.add(pointLight.light);
			}
		}
		
		skybox.render(presenter.getRenderContext(), camera);
		
		modelBatch.begin(camera);
				
		RenderableProvider renderableProvider = new RenderableProvider() {
			
			@Override
			public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
				Renderable renderable;
				
				if (presenter.getSettingsPanel().isShowGridSelected()) {

					Vector3 tmp1 = new Vector3();
					Vector3 tmp2 = new Vector3();
					tmp2.set(0.5F, 0.5F, 0.5F);				

					PointLight pointLight;
					for(int i = 0; i < pointLights.size; ++i) {
						pointLight = pointLights.get(i);

						if (pointLight.isVisible(camera, Constants.maxLightDistance)) {
							renderable = pool.obtain();
							setupRenderable(renderable, presenter.getEditor().getBoxMesh(), boxMaterial1, null);
							tmp1.set(pointLight.light.position).sub(0.25F, -0.25F, 0.25F);
							renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2);
							renderables.add(renderable);
						}
					}
					
					tmp2.set(0.5F, 0.5F, 0.5F);				

					Zone zone;
					for(int i = 0; i < zones.size; ++i) {
						zone = zones.get(i);

						if (zone.isVisible(camera, Constants.maxLightDistance)) {
							renderable = pool.obtain();
							setupRenderable(renderable, presenter.getEditor().getBoxMesh(), boxMaterial2, null);
							tmp1.set(zone.position).sub(0.25F, -0.25F, 0.25F);
							renderable.worldTransform.setToTranslationAndScaling(tmp1, tmp2);
							renderables.add(renderable);
						}
					}

				}
				
				if (presenter.getSettingsPanel().isShowBlocksSelected()) {

					Plot[] plots;
					Plot plot;
					Chunk chunk;
					for (int i = 0; i < renderChunks.size; i++) {
						chunk = renderChunks.get(i);
						plots = chunk.getPlots();
						for (int j = chunk.getMaxHeight() >> Plot.BIT_OFFSET; j >= 0; --j) {
							plot = plots[j];
							if (plot.isVisible(camera.frustum.planes)) {
								if (plot.opaqueMesh != null) {
									renderable = pool.obtain();
									setupRenderable(renderable, plot.opaqueMesh, opaqueMaterial, environment);
									renderables.add(renderable);
								}
								if (plot.blendedMesh != null) {
									renderable = pool.obtain();
									setupRenderable(renderable, plot.blendedMesh, blendMaterial, environment);
									renderables.add(renderable);
								}
							}
						}
					}
				}
				
			}
		};
		modelBatch.render(renderableProvider);

		if (presenter.getSettingsPanel().isShowEntitiesSelected()) {

			for(Entity entity : entities) {
				entity.update(deltaTime);
				if (entity.isVisible(camera)) {
					entity.render(modelBatch, environment);
				}			
			}

			decalRenderer.begin(modelBatch, environment);
			
			for(Decal decal : decals) {
				if(decal.isVisible(camera)) {
					decal.render(decalRenderer, camera);
				}
			}

			decalRenderer.end();

		}
		
		modelBatch.end();
		
	}

	private void setupRenderable(Renderable renderable, Mesh mesh, Material material, Environment environment) {
		renderable.worldTransform.idt();
		renderable.meshPart.mesh = mesh;
		renderable.meshPart.size = mesh.getNumIndices();
		renderable.meshPart.offset = 0;		
		renderable.material = material;
		renderable.environment = environment;
		renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
	}

	@Override
	public Chunk[] getChunks() {
		return chunks;
	}
	
	@Override
	public Camera getCamera() {
		return presenter.getCamera();
	}

	@Override
	public Settings getSettings() {
		return settings;
	}
	
	@Override
	public Chunk getChunkAbsolute(int x, int z) {
		return getChunkRelative((x >> Plot.BIT_OFFSET), z >> Plot.BIT_OFFSET);
	}	
	
	@Override
	public Chunk getChunkRelative(int x, int z) {
		if(x > -1 && x < settings.rWidth && z > -1 && z < settings.rDepth) {
			return chunks[x * settings.rDepth + z];
		}
		return null;
	}
	
	@Override
	public Chunk getChunk(int index) {
		return chunks[index];
	}

	@Override
	public Presenter getPresenter() {
		return presenter;
	}
	
	@Override
	public void addEntity(Entity entity) {
		entities.add(entity);
	}
	
	@Override
	public boolean removeEntity(Entity entity) {
		return entities.removeValue(entity, true);
	}
	
	@Override
	public Array<Entity> getEntities() {
		return entities;
	}
 
	@Override
	public TiledTexture getTiledTexture() {
		return tiledTexture;
	}

	@Override
	public Array<Chunk> getRenderChunks() {
		return renderChunks;
	}

	@Override
	public void forceUpdate() {
		forceUpdate = true;
	}
	
	@Override
	public Entity getEntity(Ray ray, float radius) {
		
		Array<Entity> entities = new Array<Entity>(this.entities);
		Vector3 position = new Vector3();
		entities.sort(new Comparator<Entity>() {
			@Override
			public int compare(Entity o1, Entity o2) {
				float dst1 = o1.getTransform().getTranslation(position).dst2(ray.origin);
				float dst2 = o2.getTransform().getTranslation(position).dst2(ray.origin);
				return Float.compare(dst1, dst2);
			}
		});
		
	    for(Entity entity : entities) {
	    	if(entity.hit(ray)) {
	    		return entity;
	    	}
	    }
		
		return null;
	}
	
	@Override
	public PointLight getPointLight(Ray ray, float radius) {
		Array<PointLight> pointLights = new Array<PointLight>(this.pointLights);
		pointLights.sort(new Comparator<PointLight>() {
			@Override
			public int compare(PointLight o1, PointLight o2) {
				float dst1 = o1.light.position.dst2(ray.origin);
				float dst2 = o2.light.position.dst2(ray.origin);
				return Float.compare(dst1, dst2);
			}
		});

		for (PointLight pointLight : pointLights) {
			if (pointLight.hit(ray)) {
				return pointLight;
			}
		}

		return null;
	}

	@Override
	public Decal getDecal(Ray ray, float radius) {
		Array<Decal> decals = new Array<Decal>(this.decals);
		decals.sort(new Comparator<Decal>() {
			@Override
			public int compare(Decal o1, Decal o2) {
				float dst1 = o1.position.dst2(ray.origin);
				float dst2 = o2.position.dst2(ray.origin);
				return Float.compare(dst1, dst2);
			}
		});

		for (Decal decal : decals) {
			if (decal.hit(ray)) {
				return decal;
			}
		}

		return null;
	}

	@Override
	public Zone getZone(Ray ray, float radius) {
		Array<Zone> decals = new Array<Zone>(this.zones);
		decals.sort(new Comparator<Zone>() {
			@Override
			public int compare(Zone o1, Zone o2) {
				float dst1 = o1.position.dst2(ray.origin);
				float dst2 = o2.position.dst2(ray.origin);
				return Float.compare(dst1, dst2);
			}
		});

		for (Zone decal : zones) {
			if (decal.hit(ray)) {
				return decal;
			}
		}

		return null;
	}
	
	@Override
	public void addZone(Zone zone) {
		zones.add(zone);
	}
	
	@Override
	public boolean removeZone(Zone zone) {
		return zones.removeValue(zone, true);
	}
	
	@Override
	public Array<Zone> getZones() {
		return zones;
	}

	@Override
	public boolean ray(Ray ray, float radius, Vec3i result) {
		
        int stepX = 1, x = fastFloor(ray.origin.x);
        float tMaxX, tDeltaX;

        if (ray.direction.x != 0F) {

            float invDirX = 1F / ray.direction.x;
            if (ray.direction.x > 0F) {
                tMaxX = ((x + 1F) - ray.origin.x) * invDirX;
            } else {
                tMaxX = (x - ray.origin.x) * invDirX;
                stepX = -1;
            }

            tDeltaX = stepX * invDirX;

        } else {
            tMaxX = Float.MAX_VALUE;
            tDeltaX = 0F;
        }

        int stepY = 1, y = fastCeil(ray.origin.y);

        float tMaxY, tDeltaY;

        if (ray.direction.y != 0F) {

            float invDirY = 1F / ray.direction.y;
            if (ray.direction.y > 0F) {
                tMaxY = (y - ray.origin.y) * invDirY;
            } else {
                tMaxY = ((y - 1F) - ray.origin.y) * invDirY;
                stepY = -1;
            }
            tDeltaY = stepY * invDirY;

        } else {
            tMaxY = Float.MAX_VALUE;
            tDeltaY = 0F;
        }

        int stepZ = 1, z = fastFloor(ray.origin.z);
        float tMaxZ, tDeltaZ;

        if (ray.direction.z != 0F) {

            float invDirZ = 1F / ray.direction.z;
            if (ray.direction.z > 0F) {
                tMaxZ = ((z + 1F) - ray.origin.z) * invDirZ;
            } else {
                tMaxZ = (z - ray.origin.z) * invDirZ;
                stepZ = -1;
            }
            tDeltaZ = stepZ * invDirZ;

        } else {
            tMaxZ = Float.MAX_VALUE;
            tDeltaZ = 0F;
        }

        int endX = fastFloor(ray.origin.x + ray.direction.x * radius);
        int endY = fastCeil(ray.origin.y  + ray.direction.y * radius);
        int endZ = fastFloor(ray.origin.z + ray.direction.z * radius);

        while (x != endX || y != endY || z != endZ) {

            if (tMaxX < tMaxY) {

                if (tMaxX < tMaxZ) {
                    tMaxX += tDeltaX;
                    x += stepX;
                } else {
                    tMaxZ += tDeltaZ;
                    z += stepZ;
                }

            } else {

                if (tMaxY < tMaxZ) {
                    tMaxY += tDeltaY;
                    y += stepY;
                } else {
                    tMaxZ += tDeltaZ;
                    z += stepZ;
                }

            }

			if (y >= 1 && y < settings.aHeight) {
				final Chunk chunk = getChunkAbsolute(x, z);
				if (chunk != null) {
					if (chunk.getChunkData().hasFaces(x - chunk.getWorldX(), y, z - chunk.getWorldZ())) {
						if (result != null) {
							result.set(x, y, z);
						}
						return true;
					}
				}
			}

        }

		return false;
	}
	
	@Override
	public Color getAmbientLightColor() {
		return ambientLight.color;
	}
	
	@Override
	public void dispose() {
		for(Chunk chunk : chunks) {
			chunk.dispose();
		}
		
		entities.clear();
		
		//chunkRenderer.dispose();
	}

	@Override
	public void addPointLight(PointLight pointLight) {
		pointLights.add(pointLight);
	}
	
	@Override
	public boolean removePointLight(PointLight pointLight) {
		return pointLights.removeValue(pointLight, true);
	}

	@Override
	public Array<PointLight> getPointLights() {
		return pointLights;
	}

	@Override
	public void addDecal(Decal decal) {
		decals.add(decal);
	}

	@Override
	public boolean removeDecal(Decal decal) {
		return decals.removeValue(decal, true);
	}

	@Override
	public Array<Decal> getDecals() {
		return decals;
	}
	
	@Override
	public Skybox getSkybox() {
		return skybox;
	}

	@Override
	public void clear() {
		skybox.clear();
		entities.clear();
		decals.clear();
		zones.clear();
		pointLights.clear();
		renderChunks.clear();
		ambientLight.color.set(0.5F, 0.5F, 0.5F, 1);
		for(Chunk chunk : chunks) {
			chunk.clear();
		}
		forceUpdate = true;
	}

}
