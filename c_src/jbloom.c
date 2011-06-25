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

#include <jni.h>
#include "jbloom_JBloom.h"
#include <stdio.h>
#include "bloom_filter.hpp"

bloom_filter* jbloom2filter(JNIEnv *env, jobject obj);

JNIEXPORT jlong JNICALL Java_jbloom_JBloom_newbloom(JNIEnv *env, jobject obj) {
    jclass cls = (env)->GetObjectClass(obj);
    jdouble fpp;
    jlong pec, rnd;
    bloom_filter* filter;

    fpp = (env)->GetDoubleField(obj, 
        (env)->GetFieldID(cls, "fpp", "D"));
    pec = (env)->GetLongField(obj, 
        (env)->GetFieldID(cls, "pec", "J"));
    rnd = (env)->GetLongField(obj, 
        (env)->GetFieldID(cls, "rnd", "J"));
    filter = new bloom_filter(pec, fpp, rnd);
    return (long) *&filter;
}

JNIEXPORT void JNICALL Java_jbloom_JBloom_insert
  (JNIEnv *env, jobject obj, jbyteArray val) {
    bloom_filter* filter = jbloom2filter(env, obj);    
    jsize len = env->GetArrayLength(val); 
	unsigned char* element = 
	   (unsigned char*)malloc(len * sizeof(unsigned char));
	env->GetByteArrayRegion(val,0,len,(jbyte*)element);
    filter->insert(element, len);
}

JNIEXPORT jboolean JNICALL Java_jbloom_JBloom_contains
  (JNIEnv *env, jobject obj, jbyteArray val) {
    bloom_filter* filter = jbloom2filter(env, obj);    
    jsize len = env->GetArrayLength(val); 
	unsigned char* element = 
	   (unsigned char*)malloc(len * sizeof(unsigned char));
	env->GetByteArrayRegion(val,0,len,(jbyte*)element);
    if (filter->contains(element, len)) {
        return (jboolean) true;
    }
    return (jboolean) false;
}

JNIEXPORT void JNICALL Java_jbloom_JBloom_clear
  (JNIEnv *env, jobject obj) {
    bloom_filter* filter = jbloom2filter(env, obj);    
    filter->clear();
    delete filter;
}

JNIEXPORT jdouble JNICALL Java_jbloom_JBloom_effective_1fpp
  (JNIEnv *env, jobject obj) {
    bloom_filter* filter = jbloom2filter(env, obj);
    return filter->effective_fpp();
}

JNIEXPORT jlong JNICALL Java_jbloom_JBloom_size
  (JNIEnv *env, jobject obj) {
    bloom_filter* filter = jbloom2filter(env, obj);
    return filter->size();
}

JNIEXPORT jlong JNICALL Java_jbloom_JBloom_elements
  (JNIEnv *env, jobject obj) {
    bloom_filter* filter = jbloom2filter(env, obj);
    return filter->element_count();
}


JNIEXPORT void JNICALL Java_jbloom_JBloom_filter_1intersect
  (JNIEnv *env, jclass clazz, jobject b1, jobject b2) {
    bloom_filter* filter1 = jbloom2filter(env, b1);
    bloom_filter* filter2 = jbloom2filter(env, b2);
    *(filter1) &= *(filter2);
}

JNIEXPORT void JNICALL Java_jbloom_JBloom_filter_1union
  (JNIEnv *env, jclass clazz, jobject b1, jobject b2) {
    bloom_filter* filter1 = jbloom2filter(env, b1);
    bloom_filter* filter2 = jbloom2filter(env, b2);
    *(filter1) |= *(filter2);
}

JNIEXPORT void JNICALL Java_jbloom_JBloom_filter_1difference
  (JNIEnv *env, jclass clazz, jobject b1, jobject b2) {
    bloom_filter* filter1 = jbloom2filter(env, b1);
    bloom_filter* filter2 = jbloom2filter(env, b2);
    *(filter1) ^= *(filter2);
}

JNIEXPORT jbyteArray JNICALL Java_jbloom_JBloom_serialize
  (JNIEnv *env, jobject obj) {
    bloom_filter* filter = jbloom2filter(env, obj);
    size_t sz = filter->serialized_size();
    unsigned char* bytes = new unsigned char[sz];
    filter->serialize(bytes, (unsigned int*)&sz);
    
    jbyteArray obj_bytes = env->NewByteArray(sz);
    env->SetByteArrayRegion(obj_bytes, 0, sz, (jbyte*)bytes);
    return obj_bytes;
}

JNIEXPORT jlong JNICALL Java_jbloom_JBloom_native_1deserialize
  (JNIEnv *env, jclass clazz, jbyteArray bytes) {
    jsize sz = env->GetArrayLength(bytes); 
	unsigned char* obj_bytes = 
	   (unsigned char*)malloc(sz * sizeof(unsigned char));
	env->GetByteArrayRegion(bytes,0,sz,(jbyte*)obj_bytes);
    bloom_filter* filter = bloom_filter::deserialize(obj_bytes, sz);
    return (long) *&filter;
}

/* misc */

bloom_filter* jbloom2filter(JNIEnv *env, jobject obj) {
    jclass cls = (env)->GetObjectClass(obj);
    jfieldID fid;
    fid = (env)->GetFieldID(cls, "bloom_ptr", "J");
    return (bloom_filter*) (env)->GetLongField(obj, fid);
}

