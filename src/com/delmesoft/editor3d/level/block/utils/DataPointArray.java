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

public class DataPointArray {
	
	public DataPoint[] items;
	public int size;
	
	public DataPointArray() {
		this(8);
	}
	
	public DataPointArray(int initialCapacity) {
		items = new DataPoint[initialCapacity];
	}
	
	public void add(DataPoint lightPoint) {
		ensureCapacity();
		items[size++] = lightPoint;
	}

	private void ensureCapacity() {
		if(size == items.length) {
			DataPoint[] tmp = new DataPoint[(int) (size * 1.75f)];
			System.arraycopy(items, 0, tmp, 0, size);
			items = tmp;
		}
	}

	public void addFirst(DataPoint dataPoint) {
		ensureCapacity();
		System.arraycopy(items, 0, items, 1, size++);
		items[0] = dataPoint;
	}
	
	public DataPoint pop() {
		return items[--size];
	}

	public DataPoint poll() {
		DataPoint d = items[0];
		System.arraycopy(items, 1, items, 0, --size);
		return d;
	}

	public void clear() {
		size = 0;
	}

	public DataPoint[] toArray() {
		DataPoint[] tmp = new DataPoint[size];
		System.arraycopy(items, 0, tmp, 0, size);
		return tmp;
	}
}
