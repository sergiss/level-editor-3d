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

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Utils {
	
	public static final float bitsToMegabytes = 0.000000125F;
	
	public static final float byteToMega = 0.0000009536743164f;
	
	public static float bytesToMagaBytes(long bytes) {
		return bytes * byteToMega;
	}

	public static String encapsulateString(String separator, Object...values) {
		int n = values.length;
		if (n > 0) {
			String c = String.valueOf((char) 7);
			StringBuilder sb = new StringBuilder();			
			sb.append(String.valueOf(values[0]).replace(separator, c));
			for (int i = 1; i < n; ++i) {
				sb.append(separator).append(String.valueOf(values[i]).replace(separator, c));
			}			
			return sb.toString();
		}		
		return "";
	}
	
	public static String[] uncapsulateString(String separator, String value) {
		String[] values = value.split(separator);
		String c = String.valueOf((char) 7);
		for(int i = 0; i < values.length; ++i) {
			values[i] = values[i].replace(c, separator);
		}
		return values;
	}

	public static int max(int...values) {
		int max = values[0];
		for(int i = 1; i < values.length; ++i) {
			if (max < values[i]) {
				max = values[i];
			}
		}
		return max;
	}

	public static int min(int...values) {
		int min = values[0];
		for(int i = 1; i < values.length; ++i) {
			if (min > values[i]) {
				min = values[i];
			}
		}
		return min;
	}
	
	public static int fastFloor(float x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}
	
	public static int fastFloor(double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}

	public static int fastCeil(float x) {
		int xi = (int) x;
		return x > xi ? xi + 1 : xi;
	}

    public static int fastCeil(double x) {
        int xi = (int) x;
        return x > xi ? xi + 1 : xi;
    }
	
    public static int fastAbs(int i) {
        return (i >= 0) ? i : -i;
    }
    
    public static float fastAbs(float d) {
        return (d >= 0) ? d : -d;
    }
    
    public static Vector3 getRotation(Quaternion quaternion, Vector3 results) {
    	if(results == null) {
    		results = new Vector3();
    	}
    	
    	float angle = quaternion.getAxisAngle(results);
    	results.scl(angle);
    	return results;
    }

	public static boolean isEmpty(String... values) {
		for (String s : values) {
			if (s == null || 
				s.isEmpty() || 
				s.matches(".*\\\\w.*")) {
				return true;
			}
		}
		return false;
	}

}
