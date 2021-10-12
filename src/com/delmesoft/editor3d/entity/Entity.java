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
package com.delmesoft.editor3d.entity;

import com.delmesoft.editor3d.level.Level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

public class Entity extends ObjInfo {
		
	private static final BoundingBox tmp1 = new BoundingBox();
		
	protected Level level;
	
	protected ModelInstance instance;
	
	private BoundingBox boundingBox = new BoundingBox(); // Cache
		
	public Entity(Model model, Level level) {
				
		model.calculateBoundingBox(boundingBox);
		
		instance = new ModelInstance(model);
		
		this.level = level;		
		
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
	}

	public void update(float deltaTime) {
				
	}

	public ModelInstance getModelInstance() {
		return instance;
	}
	
	public boolean isVisible(Camera camera) {
		return camera.frustum.boundsInFrustum(getBoundingBox(tmp1));
	}

	public BoundingBox getBoundingBox(BoundingBox result) {
		result.set(boundingBox);
		result.mul(instance.transform);
		return result;
	}
	
	public Matrix4 getTransform() {
		return instance.transform;
	}

	public boolean hit(Ray ray) {
		return Intersector.intersectRayBoundsFast(ray, getBoundingBox(tmp1));
	}
			
}
