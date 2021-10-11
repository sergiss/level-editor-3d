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

import com.delmesoft.editor3d.entity.Entity;
import com.delmesoft.editor3d.environment.PointLight;
import com.delmesoft.editor3d.environment.Skybox;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.graphics.g3d.decal.Decal;
import com.delmesoft.editor3d.level.block.Chunk;
import com.delmesoft.editor3d.presenter.Presenter;
import com.delmesoft.editor3d.utils.Vec3i;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public interface Level extends Disposable {
	
	Settings getSettings();
	
	Camera getCamera();
	
	Chunk getChunkAbsolute(int x, int z);
	
	Chunk getChunkRelative(int x, int z);
	
	Chunk getChunk(int index);

	Presenter getPresenter();

	void render(ModelBatch modelBatch);
	
	TiledTexture getTiledTexture();
	
	Array<Chunk> getRenderChunks();

	void forceUpdate();

	boolean ray(Ray ray, float radius, Vec3i result);

	Color getAmbientLightColor();

	void addEntity(Entity entity);

	boolean removeEntity(Entity entity);

	Array<Entity> getEntities();

	Entity getEntity(Ray ray, float radius);
	
	Decal getDecal(Ray ray, float radius);
	
	PointLight getPointLight(Ray ray, float radius);	
	
	Zone getZone(Ray ray, float radius);
	
	void addZone(Zone zone);
	
	boolean removeZone(Zone zone);

	Chunk[] getChunks();

	void addPointLight(PointLight pointLight);
	
	boolean removePointLight(PointLight pointLight);

	Array<PointLight> getPointLights();
	
	void addDecal(Decal decal);

	boolean removeDecal(Decal decal);
	
	Array<Decal> getDecals();
	
	Array<Zone> getZones();

	Skybox getSkybox();

	void clear();

}
