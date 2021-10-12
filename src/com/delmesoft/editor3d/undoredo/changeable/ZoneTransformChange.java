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

import com.delmesoft.editor3d.Constants;
import com.delmesoft.editor3d.zone.Zone;

import com.badlogic.gdx.math.Vector3;

public class ZoneTransformChange implements Changeable {
	
	protected static final Vector3 tmp = new Vector3();
	protected static Object obj;
	
	private String name;
	private String params;
	
	private Vector3 position;
	private Vector3 dimensions;
	
	private Zone zone;
	
	public ZoneTransformChange(Zone zone) {
		this.zone = zone;
		
		name = zone.getName();
		params = (String) zone.getProperties().get(Constants.PARAMS);
		
		position = new Vector3(zone.position);
		dimensions = new Vector3(zone.dimensions);
		
	}

	@Override
	public void swapStates() {
		obj = zone.getName();
		zone.setName(name);
		name = (String) obj;
		
		obj = (String) zone.getProperties().remove(Constants.PARAMS);
		zone.getProperties().put(Constants.PARAMS, params);
		params = (String) obj;
		
		tmp.set(zone.position);
		zone.position.set(position);
		position.set(tmp);
		
		tmp.set(zone.dimensions);
		zone.dimensions.set(dimensions);
		dimensions.set(tmp);
		
	}

}
