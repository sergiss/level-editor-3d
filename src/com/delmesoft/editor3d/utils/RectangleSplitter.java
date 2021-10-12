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
package com.delmesoft.editor3d.utils;

import java.util.Random;

public class RectangleSplitter {
	
	private class Plot {

		public int x, y;
		public int width, height;

		public Plot(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

	}
		
	private int width;
	private int height;

	private int plotSize = 3;
	
	private Plot[] plots;
	private int[][] data;

	private long seed = System.currentTimeMillis();
	private Random random;
	
	public void split() {
		
		int plotSize = this.plotSize;
		
		initData();

		random = new Random(seed);

		int x, y;

		plots = new Plot[width * height];

		plots[0] = new Plot(1, 1, width, height);
		plots[1] = new Plot(1, 1, width, height);
		int wallOrientation;
		int i = 0;
		while (i >= 0) {
			wallOrientation = 2;

			if (plots[i].width < plots[i].height) { //check room dimension and set wall orientation accordingly
				if (plots[i].height >= (2 * plotSize) + 2) {
					wallOrientation = 0;
				}
			} else if (plots[i].width > plots[i].height) {
				if (plots[i].width >= (2 * plotSize) + 2) {
					wallOrientation = 1;
				}
			} else {
				if (plots[i].width >= (2 * plotSize) + 2) {
					wallOrientation = random(0, 2);	// wenn room_width = room_height, dann random ausrichtung der zu setzenden Wand
				}
			}

			if (wallOrientation != 2) {
				
				if (wallOrientation == 1) {
					int wx = random(1 + plotSize, plots[i].width - plotSize);

					if (data[plots[i].x + wx - 1][plots[i].y - 1] == 2 || 
						data[plots[i].x + wx - 1][plots[i].y + plots[i].height] == 2) {
						if (random(0, 2) == 1) {
							wx += 1;
						} else {
							wx -= 1;
						}
					}

					for (y = 0; y < plots[i].height; y++) {
						data[plots[i].x + wx - 1][plots[i].y + y] = 1;
					}
					data[plots[i].x + wx - 1][plots[i].y + y / 2] = 2;  // puerta

					plots[i + 1] = new Plot(plots[i].x + wx, plots[i].y, plots[i].width - wx, plots[i].height);
					plots[i    ] = new Plot(plots[i].x, plots[i].y, wx - 1, plots[i].height);
				} else {
					int wy = random(1 + plotSize, plots[i].height - plotSize);

					if( data[ plots[i].x - 1 ][ plots[i].y + wy - 1 ] == 2 || 
						data[ plots[i].x + plots[i].width ][ plots[i].y + wy - 1 ] == 2 ){
						if (random(1, 3) == 1) {
							wy += 1;
						} else {
							wy -= 1;
						}
					}

					for (x = 0; x < plots[i].width; x++) {
						data[plots[i].x + x][plots[i].y + wy - 1] = 1;
					}

					data[plots[i].x + x / 2][plots[i].y + wy - 1] = 2; // puerta

					plots[i + 1] = new Plot(plots[i].x, plots[i].y + wy, plots[i].width, plots[i].height - wy);
					plots[i    ] = new Plot(plots[i].x, plots[i].y, plots[i].width, wy - 1);
				}

				i++;
			} else {
				i--;
			}
		}

	}
	
	private void initData() {
		data = new int[width + 2][height + 2];
	}

	private int random(int min, int max) {
		return random.nextInt(max - min) + min;
	}
	
	public int[][] getData() {
		return data;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getPlotSize() {
		return plotSize;
	}

	public void setPlotSize(int plotSize) {
		this.plotSize = plotSize;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}
			
}
