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

public class TestJBloom {

    static {
        System.loadLibrary("JBloom");
    }

    public static void main(String[] args) 
        throws Exception {
        
        /* membership: insert, contains */
        
        /*
         * JBloom(estimated size, false positive probability, random seed)
         *
        */
        
        JBloom b1 = new JBloom(10000, 0.01, 1234);
        b1.insert("JBloom".getBytes("UTF-8"));
        b1.insert("lucky".getBytes("UTF-8"));
        
        System.out.println("b1.contains('JBloom') = " + 
            b1.contains("JBloom".getBytes("UTF-8")));
        
        System.out.println("b1.contains('giraffes') = " + 
            b1.contains("giraffes".getBytes("UTF-8")));
        
        /* effectiveFPP */
        
        System.out.println("JBloom.effectiveFPP() = " + b1.effectiveFPP());
        
        /* filters */
        
        JBloom b2 = new JBloom(10000, 0.01, 1234);
        b2.insert("lucky".getBytes("UTF-8"));
        b2.insert("lobster".getBytes("UTF-8"));

        /* filter: intersect */

        System.out.println("\nJBloom.intersect(b1, b2)");
        JBloom.intersect(b1, b2);
        
        System.out.println("b1.contains('JBloom') = " + 
            b1.contains("JBloom".getBytes("UTF-8")));
        
        System.out.println("b1.contains('lucky') = " + 
            b1.contains("lucky".getBytes("UTF-8")));
            
        System.out.println("b1.contains('lobster') = " + 
            b1.contains("lobster".getBytes("UTF-8")));
        
        /* filter: union */
        
        JBloom b3 = new JBloom(10000, 0.01, 1234);
        b3.insert("lobster");
        b3.insert("lucky");
        b3.insert("bloom filter");
        b1.insert("JBloom");
        
        System.out.println("\nJBloom.union(b1, b3)");
        
        JBloom.union(b1, b3);
        
        System.out.println("b1.contains('lucky') = " + 
            b1.contains("lucky".getBytes("UTF-8")));
            
        System.out.println("b1.contains('lobster') = " + 
            b1.contains("lobster".getBytes("UTF-8")));
            
        System.out.println("b1.contains('JBloom') = " + 
            b1.contains("JBloom".getBytes("UTF-8")));    
        
        /* filter: difference */
        
        b2.insert("giraffes");
        b2.insert("trees");
        b1.insert("trees");
        b1.insert("sharks");
        b3.insert("lucky");
        
        System.out.println("\nJBloom.difference(b1, b3);");
        JBloom.difference(b1, b3);
        
        System.out.println("b1.contains('giraffes') = " + 
            b1.contains("giraffes".getBytes("UTF-8")));
            
        System.out.println("b1.contains('trees') = " + 
            b1.contains("trees".getBytes("UTF-8")));
        
        System.out.println("b1.contains('sharks') = " + 
            b1.contains("sharks".getBytes("UTF-8")));
            
        System.out.println("b1.contains('lucky') = " + 
            b1.contains("lucky".getBytes("UTF-8")));
            
        /* serialize, deserialize */
        
        System.out.println("\nb1.serialize();");
        byte[] bytes = b1.serialize();
        System.out.println("\nbytes.length = " + bytes.length);
        
        JBloom b4 = JBloom.deserialize(bytes);
        
        System.out.println("b4.contains('giraffes') = " + 
            b4.contains("giraffes".getBytes("UTF-8")));
            
        System.out.println("b4.contains('trees') = " + 
            b4.contains("trees".getBytes("UTF-8")));
        
        System.out.println("b4.contains('sharks') = " + 
            b4.contains("sharks".getBytes("UTF-8")));
            
        System.out.println("b4.contains('lucky') = " + 
            b4.contains("lucky".getBytes("UTF-8")));
        
        /* 1m inserts */
        
        System.out.println("\nperforming 1m inserts...");
        
        JBloom b5 = new JBloom(1000000, 0.1, 1234);
        long st = System.currentTimeMillis();
        for(int i=0; i<1000000; i++) {
            b5.insert(""+i);
        }
        
        System.out.println("1m inserts took " +
            (System.currentTimeMillis()-st) + "ms\n");
            
        st = System.currentTimeMillis();
        byte[] bytes2 = b5.serialize();
        
        System.out.println("10% fpp, serialized size is " + 
            bytes2.length + " bytes and took " + 
            (System.currentTimeMillis()-st) + "ms\n");
        
        System.out.println("sanity check\n");
    
        for(int i=0; i<10; i++) {
            System.out.println(i + ": " +
                b5.contains(""+i));
        }
        
        for(int i=2000000; i<2000010; i++) {
            System.out.println(i + ": " +
                b5.contains(""+i));
        }
        
        b1.delete();
        b2.delete();
        b3.delete();
        b4.delete();
        b5.delete();
    }
}
