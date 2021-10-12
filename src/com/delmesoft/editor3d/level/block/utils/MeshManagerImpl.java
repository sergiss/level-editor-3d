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
package com.delmesoft.editor3d.level.block.utils;

import com.delmesoft.editor3d.graphics.g2d.Rectangle;
import com.delmesoft.editor3d.graphics.g2d.TiledTexture;
import com.delmesoft.editor3d.level.block.Side;

public class MeshManagerImpl extends MeshManager {
	
	private final float scaleX, scaleY;
	
	public MeshManagerImpl(TiledTexture tiledTexture) {
		super(tiledTexture);
		
		scaleX = tiledTexture.getTileWidth()  * (1F / tiledTexture.getTexture().getWidth() );
		scaleY = tiledTexture.getTileHeight() * (1F / tiledTexture.getTexture().getHeight());
	}

	@Override
	public void createTop() {
		int face = Side.TOP.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
							
		switch (geometry) {
		case 0:			
			v0.set(v001); v1.set(v101);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 0
		case 1:
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v000)          ; v2.set(v100)          ;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 1
		case 2:
			v0.set(v001).y -= 0.5F; v1.set(v101);
			v3.set(v000).y -= 0.5F; v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 2
		case 3:			
			v0.set(v001)          ; v1.set(v101)          ;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 3
		case 4:
			v0.set(v001); v1.set(v101).y -= 0.5F;
			v3.set(v000); v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 4
		case 5:			
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 5
		case 6:
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v000)          ; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 6
		case 7: // rotated corner
			v1.set(v001).y -= 0.5F; v2.set(v101).y -= 0.5F;
			v0.set(v000).y -= 0.5F; v3.set(v100)          ;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, (1 + rotation) % 4, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 7
		case 8:			
			v0.set(v001).y -= 0.5F; v1.set(v101)          ;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 8
		case 9: // rotated corner
			v1.set(v001)          ; v2.set(v101).y -= 0.5F;
			v0.set(v000).y -= 0.5F; v3.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, (1 + rotation) % 4, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 9
		case 10:
			v0.set(v011)          ; v1.set(v111)          ;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 10
		case 11:
			v0.set(v011); v1.set(v101).y -= 0.5F;
			v3.set(v010); v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 11
		case 12:			
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v010)          ; v2.set(v110)          ;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 12
		case 13:
			v0.set(v001).y -= 0.5F; v1.set(v111);
			v3.set(v000).y -= 0.5F; v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 13
		case 14:			
			v0.set(v011);
			v3.set(v000).y -= 0.5F; v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(1);
			addTriIndices();
			return;  // 14
		case 15:			
			v1.set(v111);
			v3.set(v010); v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(0);
			addTriIndices();
			return;  // 15
		case 16:			
			v0.set(v011); v1.set(v101).y -= 0.5F;
			v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return;  // 16
		case 17:			
			v0.set(v001).y -= 0.5F; v1.set(v111);
			v3.set(v010);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return;  // 17	
		case 18:
			v0.set(v011)          ; v1.set(v111);
			v3.set(v000).y -= 0.5F; v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 18
		case 19: // rotated corner
			v1.set(v011); v2.set(v111)          ;
			v0.set(v010); v3.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, (1 + rotation) % 4, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 19
		case 20:
			v0.set(v011); v1.set(v101).y -= 0.5F;
			v3.set(v010); v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 20
		case 21: // rotated corner			
			v1.set(v001).y -= 0.5F; v2.set(v111);
			v0.set(v010)          ; v3.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, (1 + rotation) % 4, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return; // 21
		case 22:
			v0.set(v011); v1.set(v111);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 22
		case 23:
			v0.set(v011); v1.set(v101);
			v3.set(v010); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 23
		case 24:			
			v0.set(v001); v1.set(v101);
			v3.set(v010); v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 24
		case 25:
			v0.set(v001); v1.set(v111);
			v3.set(v000); v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 25
		case 26:			
			v0.set(v011);
			v3.set(v000); v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(1);
			addTriIndices();
			return;  // 26
		case 27:			
			v1.set(v111);
			v3.set(v010); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(0);
			addTriIndices();
			return;  // 27
		case 28:			
			v0.set(v011); v1.set(v101);
			v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return;  // 28
		case 29:			
			v0.set(v001); v1.set(v111);
			v3.set(v010);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return;  // 29	
		case 30:			
			v0.set(v001); v1.set(v111);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return;  // 30
		case 31:			
			v0.set(v011); v1.set(v101);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesNormalQuad();
			return;  // 31
		case 32:			
			v0.set(v001); v1.set(v101);
			v3.set(v010); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesFlippedQuad();
			return;  // 32
		case 33:			
			v0.set(v001); v1.set(v101);
			v3.set(v000); v2.set(v110);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addQuadVertices();
			addIndicesNormalQuad();
			return;  // 33
		case 34:			
			v0.set(v001);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(1);
			addTriIndices();
			return;  // 34
		case 35:			
						  v1.set(v101);
			v3.set(v000); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(0);
			addTriIndices();
			return;  // 35
		case 36:			
			v0.set(v001); v1.set(v101);
				          v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return;  // 36
		case 37:			
			v0.set(v001); v1.set(v101);
			v3.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return;  // 37
		case 38:			
			v0.set(v001).y -= 0.5F;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(1);
			addTriIndices();
			return;  // 38
		case 39:			
									v1.set(v101).y -= 0.5F;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(0);
			addTriIndices();
			return;  // 39
		case 40:			
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
									v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return;  // 40
		case 41:			
			v0.set(v001).y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return;  // 41
		case 42:			
			v0.set(v001); v1.set(v101).x -= 0.67F;
			v3.set(v000); v2.set(v100).x -= 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 42
		case 43:			
			v0.set(v001).z -= 0.67F; v1.set(v101).z -= 0.67F;
			v3.set(v000)           ; v2.set(v100)           ;
		
			uv.set(0F, 0.33F, 1F, 0.33F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 43
		case 44:			
			v0.set(v001).x += 0.67F; v1.set(v101);
			v3.set(v000).x += 0.67F; v2.set(v100);
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 44
		case 45:
			v0.set(v001)           ; v1.set(v101)           ;
			v3.set(v000).z += 0.67F; v2.set(v100).z += 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 45
		case 46:			
			v0.set(v001).y -= 0.5F; v1.set(v101).x -= 0.67F; v1.y -= 0.5F;
			v3.set(v000).y -= 0.5F; v2.set(v100).x -= 0.67F; v2.y -= 0.5F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 46
		case 47:			
			v0.set(v001).z -= 0.67F; v0.y -= 0.5F; v1.set(v101).z -= 0.67F; v1.y -= 0.5F;
			v3.set(v000).y -= 0.5F               ; v2.set(v100).y -= 0.5F               ;
		
			uv.set(0F, 0.33F, 1F, 0.33F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 47
		case 48:			
			v0.set(v001).x += 0.67F; v0.y -= 0.5F; v1.set(v101).y -= 0.5F;
			v3.set(v000).x += 0.67F; v3.y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 48
		case 49:			
			v0.set(v001).y -= 0.5F               ; v1.set(v101).y -= 0.5F               ;
			v3.set(v000).z += 0.67F; v3.y -= 0.5F; v2.set(v100).z += 0.67F; v2.y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 49
		case 50:
			v0.set(v001)           ; v1.set(v101)           ; v1.x -= 0.67F;
			v3.set(v000).z += 0.67F; v2.set(v100).z += 0.67F; v2.x -= 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 50
		case 51:			
			v0.set(v001).z -= 0.67F; v1.set(v101).x -= 0.67F; v1.z -= 0.67F;
			v3.set(v000)           ; v2.set(v100).x -= 0.67F               ;
		
			uv.set(0F, 0.33F, 0.33F, 0.33F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 51
		case 52:			
			v0.set(v001).z -= 0.67F; v0.x += 0.67F; v1.set(v101).z -= 0.67F;
			v3.set(v000).x += 0.67F; v2.set(v100);
		
			uv.set(0.67F, 0.33F, 1F, 0.33F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 52
		case 53:			
			v0.set(v001).x += 0.67F; v1.set(v101);
			v3.set(v000).x += 0.67F; v3.z += 0.67F; v2.set(v100).z += 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.67F, 0.67F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 53
		case 54:			
			v0.set(v001).z -= 0.33F; v0.x += 0.33F; v1.set(v101).z -= 0.33F; v1.x -= 0.33F;
			v3.set(v000).z += 0.33F; v3.x += 0.33F; v2.set(v100).z += 0.33F; v2.x -= 0.33F;
		
			uv.set(0.33F, 0.67F, 0.67F, 0.67F, 0.67F, 0.33F, 0.33F, 0.33F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
		case 55:
			v0.set(v001).y -= 0.5F               ; v1.set(v101).y -= 0.5F; v1.x -= 0.67F               ;
			v3.set(v000).y -= 0.5F; v3.z += 0.67F; v2.set(v100).y -= 0.5F; v2.x -= 0.67F; v2.z += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 55
		case 56:	
			v0.set(v001).y -= 0.5F; v0.z -= 0.67F; v1.set(v101).y -= 0.5F; v1.x -= 0.67F; v1.z -= 0.67F;
			v3.set(v000).y -= 0.5F; v2.set(v100).y -= 0.5F; v2.x -= 0.67F;           ;
		
			uv.set(0F, 0.33F, 0.33F, 0.33F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 56
		case 57:
			v0.set(v001).y -= 0.5F; v0.z -= 0.67F; v0.x += 0.67F; v1.set(v101).y -= 0.5F; v1.z -= 0.67F;
			v3.set(v000).y -= 0.5F; v3.x += 0.67F;                v2.set(v100).y -= 0.5F;
		
			uv.set(0.67F, 0.33F, 1F, 0.33F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 57
		case 58:
			v0.set(v001).y -= 0.5F; v0.x += 0.67F; v1.set(v101).y -= 0.5F;
			v3.set(v000).y -= 0.5F; v3.x += 0.67F; v3.z += 0.67F; v2.set(v100).y -= 0.5F; v2.z += 0.67F;			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.67F, 0.67F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 58
		case 59:			
			v0.set(v001).add(0.33F, -0.5F, -0.33F); v1.set(v101).add(-0.33F, -0.5F, -0.33F);
			v3.set(v000).add(0.33F, -0.5F,  0.33F); v2.set(v100).add(-0.33F, -0.5F,  0.33F);
		
			uv.set(0.33F, 0.67F, 0.67F, 0.67F, 0.67F, 0.33F, 0.33F, 0.33F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 59
		case 67: case 66: case 65: case 64:	case 63: case 62: case 61: case 60:
			float value = 0.125F * (geometry - 60);
			v0.set(v001).y -= value + 0.125F; v1.set(v101).y -= value + 0.125F;
			v3.set(v000).y -= value; v2.set(v100).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 75: case 74: case 73: case 72:	case 71: case 70: case 69: case 68:
			value = 0.125F * (geometry - 68);
			v0.set(v001).y -= value; v1.set(v101).y -= value + 0.125F;
			v3.set(v000).y -= value; v2.set(v100).y -= value + 0.125F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 68
		case 83: case 82: case 81: case 80:	case 79: case 78: case 77: case 76:
			value = 0.125F * (geometry - 76);
			v0.set(v001).y -= value         ; v1.set(v101).y -= value         ;
			v3.set(v000).y -= value + 0.125F; v2.set(v100).y -= value + 0.125F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 76
		case 91: case 90: case 89: case 88:	case 87: case 86: case 85: case 84:
			value = 0.125F * (geometry - 84);
			v0.set(v001).y -= value + 0.125F; v1.set(v101).y -= value;
			v3.set(v000).y -= value + 0.125F; v2.set(v100).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 84
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}

	@Override
	public void createBottom() {
		int face = Side.BOTTOM.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
							
		switch (geometry) {
		case 91: case 90: case 89: case 88:	case 87: case 86: case 85: case 84: case 83: case 82: case 81: case 80:	case 79: case 78: case 77: case 76: case 75: case 74: case 73: case 72:	case 71: case 70: case 69: case 68: case 67: case 66: case 65: case 64:	case 63: case 62: case 61: case 60: 
		case 33: case 32: case 31: case 30: case 25: case 24: case 23: case 22: case 21: case 20: case 19: case 18: case 13: case 12: case 11: case 10: case 9: case 8: case 7: case 6 : case 5: case 4: case 3: case 2: case 1: case 0:			
			v0.set(v111); v1.set(v011);
			v3.set(v110); v2.set(v010);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 0
		case 38: case 34: case 26: case 14:		
			v0.set(v111); v1.set(v011);
			v3.set(v110); v2.set(v010);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(0);
			addTriIndices();
			return;  // 14
		case 39: case 35: case 27:	case 15:		
			v0.set(v111); v1.set(v011);
			v3.set(v110); v2.set(v010);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(1);
			addTriIndices();
			return;  // 15
		case 40: case 36: case 28: case 16:
			v0.set(v111); v1.set(v011);
			v3.set(v110); v2.set(v010);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return;  // 16
		case 41: case 37: case 29:case 17:		
			v0.set(v111); v1.set(v011);
			v3.set(v110); v2.set(v010);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return;  // 17
		case 46: case 42:			
			v0.set(v111).x -= 0.67F; v1.set(v011);
			v3.set(v110).x -= 0.67F; v2.set(v010);			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 42
		case 47: case 43:			
			v0.set(v111).z -= 0.67F; v1.set(v011).z -= 0.67F;
			v3.set(v110)           ; v2.set(v010)           ;			
		
			uv.set(0F, 0.33F, 1F, 0.33F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 43
		case 48: case 44:			
			v0.set(v111); v1.set(v011).x += 0.67F;
			v3.set(v110); v2.set(v010).x += 0.67F;			
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 44
		case 49: case 45:			
			v0.set(v111)           ; v1.set(v011)           ;
			v3.set(v110).z += 0.67F; v2.set(v010).z += 0.67F;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 45
		case 55: case 50:			
			v0.set(v111).x -= 0.67F               ; v1.set(v011)           ;
			v3.set(v110).z += 0.67F; v3.x -= 0.67F; v2.set(v010).z += 0.67F;			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.67F, 0.67F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 50
		case 56: case 51:			
			v0.set(v111).z -= 0.67F; v0.x -= 0.67F; v1.set(v011).z -= 0.67F;
			v3.set(v110).x -= 0.67F               ; v2.set(v010)           ;			
		
			uv.set(0.67F, 0.33F, 1F, 0.33F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 51
		case 57: case 52:			
			v0.set(v111).z -= 0.67F; v1.set(v011).z -= 0.67F; v1.x += 0.67F;
			v3.set(v110)           ; v2.set(v010).x += 0.67F               ;			
		
			uv.set(0F, 0.33F, 0.33F, 0.33F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 52
		case 58: case 53:			
			v0.set(v111)           ; v1.set(v011).x += 0.67F               ;
			v3.set(v110).z += 0.67F; v2.set(v010).x += 0.67F; v2.z += 0.67F;	 		
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.67F, 0F, 0.67F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 53
		case 59: case 54:
			v0.set(v111).z -= 0.33F; v0.x -= 0.33F; v1.set(v011).z -= 0.33F; v1.x += 0.33F;
			v3.set(v110).z += 0.33F; v3.x -= 0.33F; v2.set(v010).z += 0.33F; v2.x += 0.33F;			
		
			uv.set(0.33F, 0.67F, 0.67F, 0.67F, 0.67F, 0.33F, 0.33F, 0.33F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
			
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}

	@Override
	public void createFront() {
		int face = Side.FRONT.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
				
		switch (geometry) {
		case 76: case 45: case 37: case 36: case 33: case 32: case 24: case 3: case 0:	
			v0.set(v011); v1.set(v111);
			v3.set(v001); v2.set(v101);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 0
		case 49: case 41: case 40: case 12: case 7: case 6: case 5: case 1:
			v0.set(v011)          ; v1.set(v111)          ;		
			v3.set(v001).y -= 0.5F; v2.set(v101).y -= 0.5F;	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 1
		case 8: case 2:	
			v0.set(v011)          ; v1.set(v111);
			v3.set(v001).y -= 0.5F; v2.set(v101);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 2
		case 9: case 4:	
			v0.set(v011); v1.set(v111)          ;
			v3.set(v001); v2.set(v101).y -= 0.5F;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 4
		case 16: case 20: case 11:
			v0.set(v011); v1.set(v111)          ;		
						  v2.set(v101).y -= 0.5F;	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return; // 11
		case 21: case 17: case 13:
			v0.set(v011)          ; v1.set(v111);		
			v3.set(v001).y -= 0.5F;	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return; // 13
		case 28: case 31: case 23:
			v0.set(v011); v1.set(v111);		
						  v2.set(v101);	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(3);
			addTriIndices();
			return; // 23
		case 29: case 30: case 25:
			v0.set(v011); v1.set(v111);		
			v3.set(v001);	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			addTriVertices(2);
			addTriIndices();
			return; // 25
		case 34:
			v0.set(v011); v1.set(v110);
			v3.set(v001); v2.set(v100);			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 34
		case 38:	
			v0.set(v011)          ; v1.set(v110)          ;
			v3.set(v001).y -= 0.5F; v2.set(v100).y -= 0.5F;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 38
		case 50: case 42:	
			v0.set(v011); v1.set(v111).x -= 0.67F;
			v3.set(v001); v2.set(v101).x -= 0.67F;			
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 42
		case 43:	
			v0.set(v011).z -= 0.67F; v1.set(v111).z -= 0.67F;
			v3.set(v001).z -= 0.67F; v2.set(v101).z -= 0.67F;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 43
		case 53: case 44:	
			v0.set(v011).x += 0.67F; v1.set(v111);
			v3.set(v001).x += 0.67F; v2.set(v101);			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 44
		case 55: case 46:	
			v0.set(v011); v1.set(v111).x -= 0.67F;
			v3.set(v001).y -= 0.5F; v2.set(v101).x -= 0.67F; v2.y -= 0.5F;			
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 46
		case 47:
			v0.set(v011).z -= 0.67F              ; v1.set(v111).z -= 0.67F              ;		
			v3.set(v001).y -= 0.5F; v3.z -= 0.67F; v2.set(v101).y -= 0.5F; v2.z -= 0.67F;	
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 47
		case 58: case 48:	
			v0.set(v011).x += 0.67F; v1.set(v111);
			v3.set(v001).x += 0.67F; v3.y -= 0.5F; v2.set(v101).y -= 0.5F;			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 48
		case 51:	
			v0.set(v011).z -= 0.67F; v1.set(v111).x -= 0.67F; v1.z -= 0.67F;
			v3.set(v001).z -= 0.67F; v2.set(v101).x -= 0.67F; v2.z -= 0.67F;			
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 51
		case 52:	
			v0.set(v011).x += 0.67F; v0.z -= 0.67F; v1.set(v111).z -= 0.67F;
			v3.set(v001).x += 0.67F; v3.z -= 0.67F; v2.set(v101).z -= 0.67F;			
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 52
		case 54:	
			v0.set(v011).x += 0.33F; v0.z -= 0.33F; v1.set(v111).x -= 0.33F; v1.z -= 0.33F;
			v3.set(v001).x += 0.33F; v3.z -= 0.33F; v2.set(v101).x -= 0.33F; v2.z -= 0.33F;			
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0F, 0.33F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
		case 56:	
			v0.set(v011).z -= 0.67F;              ; v1.set(v111).x -= 0.67F; v1.z -= 0.67F;
			v3.set(v001).y -= 0.5F ; v3.z -= 0.67F; v2.set(v101).x -= 0.67F; v2.y -= 0.5F; v2.z -= 0.67F;			
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 56
		case 57:	
			v0.set(v011).x += 0.67F; v0.z -= 0.67F;               v1.set(v111).z -= 0.67F;
			v3.set(v001).x += 0.67F; v3.y -= 0.5F; v3.z -= 0.67F; v2.set(v101).y -= 0.5F;v2.z -= 0.67F;		
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 57
		case 59:	
			v0.set(v011).x += 0.33F; v0.z -= 0.33F; v1.set(v111).x -= 0.33F; v1.z -= 0.33F;
			v3.set(v001).x += 0.33F; v3.y -= 0.5F; v3.z -= 0.33F; v2.set(v101).x -= 0.33F; v2.y -= 0.5F; v2.z -= 0.33F;			
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0.5F, 0.33F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 59
		case 66: case 65: case 64: case 63: case 62: case 61: case 60:
			float value = (geometry - 60) * 0.125F + 0.125F;
			v0.set(v011); v1.set(v111);
			v3.set(v001).y -= value; v2.set(v101).y -= value;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 75: case 74: case 73: case 72: case 71: case 70: case 69: case 68:
			value = (geometry - 68) * 0.125F;
			v0.set(v011); v1.set(v111);
			v3.set(v001).y -= value; v2.set(v101).y -= value + 0.125F;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, value + 0.125F, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 68	
		case 83: case 82: case 81: case 80: case 79: case 78: case 77:
			value = (geometry - 77) * 0.125F + 0.125F;
			v0.set(v011); v1.set(v111);
			v3.set(v001).y -= value; v2.set(v101).y -= value;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 77
		case 91: case 90: case 89: case 88: case 87: case 86: case 85: case 84:
			value = (geometry - 84) * 0.125F;
			v0.set(v011); v1.set(v111);
			v3.set(v001).y -= value + 0.125F; v2.set(v101).y -= value;			
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value + 0.125F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 84
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}

	@Override
	public void createBack() {
		int face = Side.BACK.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
		
		switch (geometry) {
		case 60: case 43: case 35: case 34: case 31: case 30: case 22: case 1: case 0:			
			v0.set(v110); v1.set(v010);
			v3.set(v100); v2.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 0
		case 7: case 2:			
			v0.set(v110); v1.set(v010)          ;
			v3.set(v100); v2.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 2
		case 47: case 39: case 38: case 10: case 9: case 8: case 5: case 3:			
			v0.set(v110)          ; v1.set(v010)          ;
			v3.set(v100).y -= 0.5F; v2.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 0
		case 6: case 4:			
			v0.set(v110); v1.set(v010)          ;
			v3.set(v100).y -= 0.5F; v2.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 2
		case 15: case 19: case 11:			
	        v0.set(v110)          ; v1.set(v010);
			v3.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(2);
			addTriIndices();
			return; // 11
		case 14: case 18: case 13:			
			v0.set(v110); v1.set(v010)          ;
						  v2.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(3);
			addTriIndices();
			return; // 13
		case 27: case 32: case 23:			
	        v0.set(v110); v1.set(v010);
			v3.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(2);
			addTriIndices();
			return; // 23
		case 26: case 33: case 25:			
			v0.set(v110); v1.set(v010);
						  v2.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(3);
			addTriIndices();
			return; // 25
		case 36:			
			v0.set(v110); v1.set(v011);
			v3.set(v100); v2.set(v001);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 36
		case 40:			
			v0.set(v110)          ; v1.set(v011)          ;
			v3.set(v100).y -= 0.5F; v2.set(v001).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 40
		case 51: case 42:			
			v0.set(v110).x -= 0.67F; v1.set(v010);
			v3.set(v100).x -= 0.67F; v2.set(v000);
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 42
		case 52: case 44:			
			v0.set(v110); v1.set(v010).x += 0.67F;
			v3.set(v100); v2.set(v000).x += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 44
		case 45:			
			v0.set(v110).z += 0.67F; v1.set(v010).z += 0.67F;
			v3.set(v100).z += 0.67F; v2.set(v000).z += 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 45
		case 56: case 46:			
			v0.set(v110).x -= 0.67F; v1.set(v010);
			v3.set(v100).x -= 0.67F; v3.y -= 0.5F; v2.set(v000).y -= 0.5F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 46
		case 57: case 48:			
			v0.set(v110)          ; v1.set(v010).x += 0.67F             ;
			v3.set(v100).y -= 0.5F; v2.set(v000).x += 0.67F; v2.y -=0.5F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 48
		case 49:			
			v0.set(v110).z += 0.67F              ; v1.set(v010).z += 0.67F              ;
			v3.set(v100).y -= 0.5F; v3.z += 0.67F; v2.set(v000).y -= 0.5F; v2.z += 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 49
		case 50:			
			v0.set(v110).x -= 0.67F; v0.z += 0.67F; v1.set(v010).z += 0.67F;
			v3.set(v100).x -= 0.67F; v3.z += 0.67F; v2.set(v000).z += 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 50
		case 53:			
			v0.set(v110).z += 0.67F; v1.set(v010).x += 0.67F; v1.z += 0.67F;
			v3.set(v100).z += 0.67F; v2.set(v000).x += 0.67F; v2.z += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 53
		case 54:			
			v0.set(v110).x -= 0.33F; v0.z += 0.33F; v1.set(v010).x += 0.33F; v1.z += 0.33F; 
			v3.set(v100).x -= 0.33F; v3.z += 0.33F; v2.set(v000).x += 0.33F; v2.z += 0.33F; 
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0F, 0.33F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
		case 55:			
			v0.set(v110).x -= 0.67F; v0.z += 0.67F;               v1.set(v010); v1.z += 0.67F;
			v3.set(v100).x -= 0.67F; v3.y -= 0.5F; v3.z += 0.67F; v2.set(v000).y -= 0.5F; v2.z += 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 55
		case 58:			
			v0.set(v110).z += 0.67F;               v1.set(v010).x += 0.67F; v1.z += 0.67F;
			v3.set(v100).y -= 0.5F; v3.z += 0.67F; v2.set(v000).x += 0.67F; v2.y -=0.5F; v2.z += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 58
		case 59:			
			v0.set(v110).x -= 0.33F; v0.z += 0.33F; v1.set(v010).x += 0.33F; v1.z += 0.33F; 
			v3.set(v100).x -= 0.33F; v3.y -= 0.5F; v3.z += 0.33F; v2.set(v000).x += 0.33F; v2.y -= 0.5F; v2.z += 0.33F; 
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0.5F, 0.33F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 59
		case 67: case 66: case 65: case 64: case 63: case 62: case 61:
			float value = (geometry - 61) * 0.125F + 0.125F;			
			v0.set(v110); v1.set(v010);
			v3.set(v100).y -= value; v2.set(v000).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 61
		case 75: case 74: case 73: case 72: case 71: case 70: case 69: case 68:
			value = (geometry - 68) * 0.125F;
			v0.set(v110); v1.set(v010);
			v3.set(v100).y -= value + 0.125F; v2.set(v000).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value + 0.125F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 68	
		case 82: case 81: case 80: case 79: case 78: case 77: case 76:
			value = (geometry - 76) * 0.125F + 0.125F;			
			v0.set(v110); v1.set(v010);
			v3.set(v100).y -= value; v2.set(v000).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 76
		case 91: case 90: case 89: case 88: case 87: case 86: case 85: case 84:
			value = (geometry - 84) * 0.125F;
			v0.set(v110); v1.set(v010);
			v3.set(v100).y -= value; v2.set(v000).y -= value + 0.125F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value + 0.125F, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 84	
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}
	
	@Override
	public void createRight() {
		int face = Side.RIGHT.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
		
		switch (geometry) {
		case 84: case 44: case 36: case 35: case 32: case 31: case 23: case 2: case 0:	
			v0.set(v111); v1.set(v110);
			v3.set(v101); v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 0
		case 7: case 1:	
			v0.set(v111)	      ; v1.set(v110);
			v3.set(v101).y -= 0.5F; v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 1
		case 8: case 3:
			v0.set(v111); v1.set(v110)          ;
			v3.set(v101); v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 3
		case 48: case 40: case 39: case 11: case 9: case 6: case 5: case 4:	
			v0.set(v111)          ; v1.set(v110)          ;
			v3.set(v101).y -= 0.5F; v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 4
		case 15: case 19: case 10:	
			v0.set(v111); v1.set(v110)          ;
			              v2.set(v100).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(3);
			addTriIndices();
			return; // 10
		case 16: case 20: case 12:	
			v0.set(v111)          ; v1.set(v110);
			v3.set(v101).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(2);
			addTriIndices();
			return; // 12
		case 27: case 30: case 22:	
			v0.set(v111); v1.set(v110);
			              v2.set(v100);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(3);
			addTriIndices();
			return; // 22
		case 28: case 33: case 24:	
			v0.set(v111); v1.set(v110);
			v3.set(v101);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			addTriVertices(2);
			addTriIndices();
			return; // 24
		case 37:	
			v0.set(v111); v1.set(v010);
			v3.set(v101); v2.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 37
		case 41:	
			v0.set(v111)          ; v1.set(v010)          ;
			v3.set(v101).y -= 0.5F; v2.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 41
		case 42:	
			v0.set(v111).x -= 0.67F; v1.set(v110).x -= 0.67F;
			v3.set(v101).x -= 0.67F; v2.set(v100).x -= 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 42
		case 52: case 43:	
			v0.set(v111).z -= 0.67F; v1.set(v110);
			v3.set(v101).z -= 0.67F; v2.set(v100);
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 43
		case 53: case 45:	
			v0.set(v111); v1.set(v110).z += 0.67F;
			v3.set(v101); v2.set(v100).z += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 45
		case 46:	
			v0.set(v111).x -= 0.67F              ; v1.set(v110).x -= 0.67F              ;
			v3.set(v101).y -= 0.5F; v3.x -= 0.67F; v2.set(v100).y -= 0.5F; v2.x -= 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 46
		case 57: case 47:	
			v0.set(v111).z -= 0.67F              ; v1.set(v110)              ;
			v3.set(v101).z -= 0.67F; v3.y -= 0.5F; v2.set(v100); v2.y -= 0.5F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 47
		case 58: case 49:	
			v0.set(v111)              ; v1.set(v110).z += 0.67F              ;
			v3.set(v101); v3.y -= 0.5F; v2.set(v100).z += 0.67F; v2.y -= 0.5F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 49
		case 50:	
			v0.set(v111).x -= 0.67F; v1.set(v110).z += 0.67F; v1.x -= 0.67F;
			v3.set(v101).x -= 0.67F; v2.set(v100).z += 0.67F; v2.x -= 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 50
		case 51:	
			v0.set(v111).z -= 0.67F; v0.x -= 0.67F; v1.set(v110).x -= 0.67F; 
			v3.set(v101).z -= 0.67F; v3.x -= 0.67F; v2.set(v100).x -= 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 51
		case 54:	
			v0.set(v111).x -= 0.33F; v0.z -= 0.33F; v1.set(v110).x -= 0.33F; v1.z += 0.33F;
			v3.set(v101).x -= 0.33F; v3.z -= 0.33F; v2.set(v100).x -= 0.33F; v2.z += 0.33F;
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0F, 0.33F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
		case 55:	
			v0.set(v111); v0.x -= 0.67F;               v1.set(v110).z += 0.67F; v1.x -= 0.67F;
			v3.set(v101); v3.y -= 0.5F; v3.x -= 0.67F; v2.set(v100).z += 0.67F; v2.y -= 0.5F; v2.x -= 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 55
		case 56:	
			v0.set(v111).z -= 0.67F; v0.x -= 0.67;               v1.set(v110).x -= 0.67;
			v3.set(v101).z -= 0.67F; v3.y -= 0.5F; v3.x -= 0.67; v2.set(v100); v2.y -= 0.5F; v2.x -= 0.67;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 56
		case 59:	
			v0.set(v111).x -= 0.33F; v0.z -= 0.33F; v1.set(v110).x -= 0.33F; v1.z += 0.33F;
			v3.set(v101).x -= 0.33F; v3.y -= 0.5F; v3.z -= 0.33F; v2.set(v100).x -= 0.33F; v2.y -= 0.5F; v2.z += 0.33F;
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0.5F, 0.33F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 59
		case 67: case 66: case 65: case 64: case 63: case 62: case 61: case 60:
			float value = (geometry - 60) * 0.125F;
			v0.set(v111); v1.set(v110);
			v3.set(v101).y -= value + 0.125F; v2.set(v100).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value + 0.125F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 74: case 73: case 72: case 71: case 70: case 69: case 68:
			value = (geometry - 68) * 0.125F + 0.125F;			
			v0.set(v111); v1.set(v110);
			v3.set(v101).y -= value; v2.set(v100).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 68
		case 83: case 82: case 81: case 80: case 79: case 78: case 77: case 76:
			value = (geometry - 76) * 0.125F;
			v0.set(v111); v1.set(v110);
			v3.set(v101).y -= value; v2.set(v100).y -= value + 0.125F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value + 0.125F, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 91: case 90: case 89: case 88: case 87: case 86: case 85:
			value = (geometry - 85) * 0.125F + 0.125F;			
			v0.set(v111); v1.set(v110);
			v3.set(v101).y -= value; v2.set(v100).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 68
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}

	@Override
	public void createLeft() {
		
		int face = Side.LEFT.ordinal();
		if(chunkData.isFaceEnabled(index, face) == false) return;
		
		int textureIndex = chunkData.getTextureIndex(index, face);
		int rotation  = chunkData.getTextureAngle(index, face);
		boolean flipX = chunkData.isFaceFlippedHorizontally(index, face);
		boolean flipY = chunkData.isFaceFlippedVertically(index, face);
		
		Rectangle rectangle = tiledTexture.getTextureCoordinates()[textureIndex];
						
		switch (geometry) {
		case 68: case 42: case 37: case 34: case 33: case 30: case 25: case 4: case 0:	
			v0.set(v010); v1.set(v011);
			v3.set(v000); v2.set(v001);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 0
		case 6: case 1:	
			v0.set(v010); v1.set(v011)          ;
			v3.set(v000); v2.set(v001).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);			
			break; // 1
		case 46: case 41: case 38: case 13: case 8 : case 7 : case 5: case 2:			
			v0.set(v010)          ; v1.set(v011)          ;
			v3.set(v000).y -= 0.5F; v2.set(v001).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			break; // 2
		case 9: case 3:	
			v0.set(v010)          ; v1.set(v011);
			v3.set(v000).y -= 0.5F; v2.set(v001);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);			
			break; // 3
		 case 18: case 14 : case 10:			
			v0.set(v010)          ; v1.set(v011);
			v3.set(v000).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			addTriVertices(2);
			addTriIndices();
			return; // 10
		case 21: case 17: case 12:			
			v0.set(v010); v1.set(v011)          ;
						  v2.set(v001).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			addTriVertices(3);
			addTriIndices();
			return; // 12
		case 26: case 31: case 22:			
			v0.set(v010); v1.set(v011);
			v3.set(v000);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			addTriVertices(2);
			addTriIndices();
			return; // 22
		case 29: case 32: case 24:			
			v0.set(v010); v1.set(v011);
						  v2.set(v001);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			addTriVertices(3);
			addTriIndices();
			return; // 24
		case 35:	
			v0.set(v010); v1.set(v111);
			v3.set(v000); v2.set(v101);
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 35
		case 39:	
			v0.set(v010)          ; v1.set(v111)          ;
			v3.set(v000).y -= 0.5F; v2.set(v101).y -= 0.5F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 39
		case 51: case 43:
			v0.set(v010); v1.set(v011).z -= 0.67F;
			v3.set(v000); v2.set(v001).z -= 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 43
		case 44:	
			v0.set(v010).x += 0.67F; v1.set(v011).x += 0.67F;
			v3.set(v000).x += 0.67F; v2.set(v001).x += 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 44
		case 50: case 45:	
			v0.set(v010).z += 0.67F; v1.set(v011);
			v3.set(v000).z += 0.67F; v2.set(v001);
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 45
		case 56: case 47:
			v0.set(v010)              ; v1.set(v011).z -= 0.67F              ;
			v3.set(v000); v3.y -= 0.5F; v2.set(v001).z -= 0.67F; v2.y -= 0.5F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 47
		case 48:			
			v0.set(v010).x += 0.67F              ; v1.set(v011).x += 0.67F              ;
			v3.set(v000).y -= 0.5F; v3.x += 0.67F; v2.set(v001).y -= 0.5F; v2.x += 0.67F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);		
			break; // 2
		case 55: case 49:	
			v0.set(v010).z += 0.67F              ; v1.set(v011)              ;
			v3.set(v000).z += 0.67F; v3.y -= 0.5F; v2.set(v001); v2.y -= 0.5F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 49
		case 52:
			v0.set(v010).x += 0.67F; v1.set(v011).z -= 0.67F; v1.x += 0.67F;
			v3.set(v000).x += 0.67F; v2.set(v001).z -= 0.67F; v2.x += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0F, 0F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 52
		case 53:	
			v0.set(v010).z += 0.67F; v0.x += 0.67F; v1.set(v011).x += 0.67F;
			v3.set(v000).z += 0.67F; v3.x += 0.67F; v2.set(v001).x += 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0F, 0.67F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 53
		case 54:	
			v0.set(v010).x += 0.33F; v0.z += 0.33F; v1.set(v011).x += 0.33F; v1.z -= 0.33F;
			v3.set(v000).x += 0.33F; v3.z += 0.33F; v2.set(v001).x += 0.33F; v2.z -= 0.33F;
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0F, 0.33F, 0F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 54
		case 57:
			v0.set(v010).x += 0.67F;                   v1.set(v011).z -= 0.67F; v1.x += 0.67F;
			v3.set(v000); v3.y -= 0.5F; v3.x += 0.67F; v2.set(v001).z -= 0.67F; v2.y -= 0.5F; v2.x += 0.67F;
		
			uv.set(0F, 1F, 0.33F, 1F, 0.33F, 0.5F, 0F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 57
		case 58:	
			v0.set(v010).z += 0.67F; v0.x += 0.67F;               v1.set(v011).x += 0.67F;
			v3.set(v000).z += 0.67F; v3.y -= 0.5F; v3.x += 0.67F; v2.set(v001); v2.y -= 0.5F; v2.x += 0.67F;
		
			uv.set(0.67F, 1F, 1F, 1F, 1F, 0.5F, 0.67F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);						
			break; // 58
		case 59:	
			v0.set(v010).x += 0.33F; v0.z += 0.33F; v1.set(v011).x += 0.33F; v1.z -= 0.33F;
			v3.set(v000).x += 0.33F; v3.y -= 0.5F; v3.z += 0.33F; v2.set(v001).x += 0.33F; v2.y -= 0.5F; v2.z -= 0.33F;
		
			uv.set(0.33F, 1F, 0.67F, 1F, 0.67F, 0.5F, 0.33F, 0.5F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 59
		case 67: case 66: case 65: case 64: case 63: case 62: case 61: case 60:
			float value = (geometry - 60) * 0.125F;
			v0.set(v010); v1.set(v011);
			v3.set(v000).y -= value; v2.set(v001).y -= value + 0.125F;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value + 0.125F, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 75: case 74: case 73: case 72: case 71: case 70: case 69:
			value = (geometry - 69) * 0.125F + 0.125F;			
			v0.set(v010); v1.set(v011);
			v3.set(v000).y -= value; v2.set(v001).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 84
		case 83: case 82: case 81: case 80: case 79: case 78: case 77: case 76:
			value = (geometry - 76) * 0.125F;
			v0.set(v010); v1.set(v011);
			v3.set(v000).y -= value + 0.125F; v2.set(v001).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value + 0.125F, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 60
		case 90: case 89: case 88: case 87: case 86: case 85: case 84:
			value = (geometry - 84) * 0.125F + 0.125F;			
			v0.set(v010); v1.set(v011);
			v3.set(v000).y -= value; v2.set(v001).y -= value;
		
			uv.set(0F, 1F, 1F, 1F, 1F, value, 0F, value, flipX, flipY, scaleX, scaleY, rotation, rectangle.x1, rectangle.y1);
			break; // 84
		default:			
			return;
		}
		addQuadVertices();
		addIndicesNormalQuad();
	}

}
