/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class philipyexushen_opencvhandler_HandlerWrapper */

#ifndef _Included_philipyexushen_opencvhandler_HandlerWrapper
#define _Included_philipyexushen_opencvhandler_HandlerWrapper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeHoughCircles
 * Signature: ([IIIDDDDII)[I
 */
JNIEXPORT jintArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeHoughCircles
  (JNIEnv *, jclass, jintArray, jint, jint, jdouble, jdouble, jdouble, jdouble, jint, jint);

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeGeneralizedHoughBallard
 * Signature: ([I[IIIIIIIDDIII)[F
 */
JNIEXPORT jfloatArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeGeneralizedHoughBallard
  (JNIEnv *, jclass, jintArray, jintArray, jint, jint, jint, jint, jint, jint, jdouble, jdouble, jint, jint, jint);

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeGeneralizedHoughGuil
 * Signature: ([I[IIIIIIIDDIIDDDDDDDDI)[F
 */
JNIEXPORT jfloatArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeGeneralizedHoughGuil
  (JNIEnv *, jclass, jintArray, jintArray, jint, jint, jint, jint, jint, jint, jdouble, jdouble, jint, jint, jdouble, jdouble, jdouble, jdouble, jdouble, jdouble, jdouble, jdouble, jint);

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeDrawGeneralizedHough
 * Signature: ([III[FIIIIIIIII)[I
 */
JNIEXPORT jintArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeDrawGeneralizedHough
  (JNIEnv *, jclass, jintArray, jint, jint, jfloatArray, jint, jint, jint, jint, jint, jint, jint, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
