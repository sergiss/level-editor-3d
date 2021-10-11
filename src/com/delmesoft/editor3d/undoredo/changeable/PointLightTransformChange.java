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
package com.delmesoft.editor3d.undoredo.changeable;


import com.delmesoft.editor3d.environment.PointLight;

import com.badlogic.gdx.math.Vector3;

public class PointLightTransformChange implements Changeable {
	
	private static final Vector3 tmp = new Vector3();
	private static Object obj;
	
	private PointLight pointLight;
	
	private String name;
	private Vector3 position;
	private Vector3 color;
	private float intensity;
	
	public PointLightTransformChange(PointLight pointLight) {
		
		this.pointLight = pointLight;
		
		name = pointLight.getName();
		position = new Vector3(pointLight.light.position);
		color = new Vector3(pointLight.light.color.r, pointLight.light.color.g, pointLight.light.color.b);
		intensity = pointLight.light.intensity;
										
	}

	@Override
	public void swapStates() {
		
			obj = pointLight.getName();
			pointLight.setName(name);
			name = (String) obj;
			
			obj = pointLight.light.intensity;
			pointLight.light.intensity = intensity;
			intensity = (float) obj;
			
			tmp.set(pointLight.light.position);
			pointLight.light.position.set(position);
			position.set(tmp);
			
			tmp.set(pointLight.light.color.r, pointLight.light.color.g, pointLight.light.color.b);
			pointLight.light.color.set(color.x, color.y, color.z, 1);
			color.set(tmp);
			
	}

}
