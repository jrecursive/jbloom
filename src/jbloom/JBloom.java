// -------------------------------------------------------------------
// 
// Copyright (c) 2010 John Muellerleile  All Rights Reserved.
// 
// This file is provided to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file
// except in compliance with the License.  You may obtain
// a copy of the License at
// 
//    http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
// 
// -------------------------------------------------------------------

package jbloom;

public class JBloom {
    private native long newbloom();
    public native void insert(byte[] element);
    public native boolean contains(byte[] element);
    public native void clear();
    public native long size();
    public native long elements();
    private native double effective_fpp();
    private static native void filter_intersect(JBloom b1, JBloom b2);
    private static native void filter_union(JBloom b1, JBloom b2);
    private static native void filter_difference(JBloom b1, JBloom b2);
    public native byte[] serialize();
    private static native long native_deserialize(byte[] obj);
    private long bloom_ptr, pec, rnd;
    private double fpp;
    
    private JBloom() { }
    private JBloom(long ptr) {
        this.bloom_ptr = ptr;
    }

    public JBloom(long pec, double fpp, long rnd) {
        this.fpp = fpp;
        this.pec = pec;
        this.rnd = rnd;
        this.bloom_ptr = this.newbloom();
    }
    
    public void insert(String element) 
        throws java.io.UnsupportedEncodingException {
        this.insert(element.getBytes("UTF-8"));
    }
    
    public boolean contains(String element)
        throws java.io.UnsupportedEncodingException {
        return this.contains(element.getBytes("UTF-8"));
    }
    
    public double effectiveFPP() {
        return this.effective_fpp();
    }

    public static void intersect(JBloom b1, JBloom b2) {
        filter_intersect(b1, b2);
    }

    public static void union(JBloom b1, JBloom b2) {
        filter_union(b1, b2);
    }

    public static void difference(JBloom b1, JBloom b2) {
        filter_difference(b1, b2);
    }

    public static JBloom deserialize(byte[] obj) {
        return new JBloom(native_deserialize(obj));
    }
    
    public void delete() {
        this.clear();
        bloom_ptr=0;
    }    
}
