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

import com.delmesoft.editor3d.level.Settings;
import com.delmesoft.editor3d.utils.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class FlyCamera extends PerspectiveCamera {
	
	private static final float DEGPIX = 0.5f;

	private final Vector3 tmp;

	private float cameraSpeed = 15f;
	
	private Vector3 rotation;
	private Quaternion quaternion;

	public FlyCamera(float fieldOffView, float viewportWidth, float viewportHeight) {
		super(fieldOffView, viewportWidth, viewportHeight);

		near = 0.1f;

		tmp = new Vector3();
		rotation = new Vector3();
		quaternion = new Quaternion();

	}

	public void update(float delta) {
		
		final Vector3 direction = super.direction;
		final Vector3 position = super.position;
		final Vector3 up = super.up;
		
		Input input = Gdx.input;

		if(input.isButtonPressed(Buttons.MIDDLE)){
			float deltaX = -Gdx.input.getDeltaX() * DEGPIX;
			float deltaY = -Gdx.input.getDeltaY() * DEGPIX;
			direction.rotate(up, deltaX);

			tmp.set(direction).crs(up).nor();
			if(!(direction.y < -0.99f & deltaY < 0) && !(direction.y > 0.99f & deltaY > 0)){
				direction.rotate(tmp, deltaY);
			}	
		}
				
		if(input.isKeyPressed(Settings.KEY_ACTION1)){
			delta *= 3F;
		}
		
		if(input.isKeyPressed(Settings.KEY_UP)){
			tmp.set(direction).nor().scl(cameraSpeed * delta); // forward
			position.add(tmp);
		}

		if(input.isKeyPressed(Settings.KEY_DOWN)){
			tmp.set(direction).nor().scl(cameraSpeed * delta); // forward
			position.sub(tmp);
		}

		if(input.isKeyPressed(Settings.KEY_LEFT)){
			tmp.set(direction).crs(up).nor().scl(cameraSpeed * delta); // sideway
			position.sub(tmp);
		}

		if(input.isKeyPressed(Settings.KEY_RIGHT)){
			tmp.set(direction).crs(up).nor().scl(cameraSpeed * delta); // sideway
			position.add(tmp);
		}
		
		update(true);
	}

	public Vector3 getRotation(){
		quaternion.setFromMatrix(view);
		return Utils.getRotation(quaternion, rotation);
	}
}
