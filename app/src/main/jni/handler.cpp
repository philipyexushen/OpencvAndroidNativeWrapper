//
// Created by Philip on 2018/2/23.
//
#include <opencv2/opencv.hpp>
#include "philipyexushen_opencvhandler_HandlerWrapper.h"
#include <iostream>

extern "C" {
using namespace cv;
using namespace std;

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    houghCircles
 * Signature: ([IIIDDDDII)[I
 */
JNIEXPORT jintArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeHoughCircles
        (JNIEnv *env, jclass, jintArray jintImgBuf, jint h, jint w, jdouble dp,
         jdouble minDist, jdouble cannyThreshold, jdouble accumulatorThreshold, jint minRadius, jint maxRadius) {
    //直接裸指针操作GetPrimitiveArray，减少拷贝
    jint *cbuf = (jint *)env->GetPrimitiveArrayCritical(jintImgBuf, JNI_FALSE);
    if (cbuf == NULL){
        return jintImgBuf;
    }

    Mat imgSrc(h, w, CV_8UC4, (unsigned char *) cbuf);
    Mat imgGary;
    // convert image to gray
    cv::cvtColor(imgSrc, imgGary, cv::COLOR_BGRA2GRAY);

    //reduce_noise
    cv::medianBlur(imgGary, imgGary, 5);

    //find cirles
    std::vector<Vec3f> circles;
    cv::HoughCircles(imgGary, circles, cv::HOUGH_GRADIENT, dp, minDist, cannyThreshold, accumulatorThreshold, minRadius, maxRadius);

    //draw circles
    for( size_t i = 0; i < circles.size(); i++ )
    {
        Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
        int radius = cvRound(circles[i][2]);
        // circle center
        circle( imgSrc, center, 3, Scalar(0,255,0), -1, 8, 0 );
        // circle outline
        circle( imgSrc, center, radius, Scalar(0,0,255), 3, 8, 0 );
    }

    env->ReleasePrimitiveArrayCritical(jintImgBuf, cbuf, 0);
    return jintImgBuf;
}

jfloatArray makeResultBuffer(JNIEnv *env, const std::vector<Vec4f> &position){
    int size = 4*position.size();
    jfloatArray result = env->NewFloatArray(size);
    jfloat *resultBuf = env->GetFloatArrayElements(result, JNI_FALSE);

    for (int i = 0; i < position.size(); i++) {
        resultBuf[4 * i + 0] = position[i].val[0];
        resultBuf[4 * i + 1] = position[i].val[1];
        resultBuf[4 * i + 2] = position[i].val[2];
        resultBuf[4 * i + 3] = position[i].val[3];
    }
    env->ReleaseFloatArrayElements(result, resultBuf, 0);

    return result;
}

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeGeneralizedHoughBallard
 * Signature: ([I[IIIIIIIDDIII)[F
 */
JNIEXPORT jfloatArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeGeneralizedHoughBallard
        (JNIEnv *env, jclass, jintArray jintImgBuf, jintArray jintTempImgBuf,
         jint h, jint w,
         jint tempH, jint tempW,
         jint cannyLowThresh, jint cannyHighThresh,
         jdouble minDist,
         jdouble dp, jint levels, jint votesThreshold, jint maxBufferSize) {

    jint *cbufImgSrc = env->GetIntArrayElements(jintImgBuf, JNI_FALSE);
    jint *cbufImgTemp = env->GetIntArrayElements(jintTempImgBuf, JNI_FALSE);

    if (cbufImgSrc == NULL || cbufImgTemp == NULL){
        return env->NewFloatArray(0);
    }

    Mat imgSrc(h, w, CV_8UC4, (unsigned char *) cbufImgSrc);
    Mat imgTemp(tempH, tempW, CV_8UC4, (unsigned char *) cbufImgTemp);

    cv::cvtColor(imgSrc, imgSrc, cv::COLOR_BGRA2GRAY);
    cv::cvtColor(imgTemp, imgTemp, cv::COLOR_BGRA2GRAY);
    
    cv::Ptr<GeneralizedHough> alg;
    cv::Ptr<GeneralizedHoughBallard> ballard = createGeneralizedHoughBallard();

    ballard->setMinDist(minDist);
    ballard->setLevels(levels);
    ballard->setDp(dp);
    ballard->setMaxBufferSize(maxBufferSize);
    ballard->setVotesThreshold(votesThreshold);

    alg = ballard;
    alg->setTemplate(imgTemp);

    std::vector<Vec4f> position;
    alg->detect(imgSrc, position);
    jfloatArray result = makeResultBuffer(env, position);

    env->ReleaseIntArrayElements(jintImgBuf, cbufImgSrc, 0);
    env->ReleaseIntArrayElements(jintTempImgBuf, cbufImgTemp, 0);

    return result;
}

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeGeneralizedHoughGuil
 * Signature: ([I[IIIIIIIDDIIDDDDDDDDI)[F
 */
JNIEXPORT jfloatArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeGeneralizedHoughGuil
        (JNIEnv *env, jclass, jintArray jintImgBuf, jintArray jintTempImgBuf,
         jint h, jint w,
         jint tempH, jint tempW,
         jint cannyLowThresh, jint cannyHighThresh,
         jdouble minDist, jdouble dp, jint level, jint posThresh,
         jdouble minScale,jdouble maxScale,jdouble scaleStep,jdouble scaleThresh,
         jdouble minAngle,jdouble maxAngle,jdouble angleStep,jdouble angleThresh,
         jint maxBufferSize){

    jint *cbufImgSrc = env->GetIntArrayElements(jintImgBuf, JNI_FALSE);
    jint *cbufImgTemp = env->GetIntArrayElements(jintTempImgBuf, JNI_FALSE);

    if (cbufImgSrc == NULL || cbufImgTemp == NULL){
        return env->NewFloatArray(0);
    }

    Mat imgSrc(h, w, CV_8UC4, (unsigned char *) cbufImgSrc);
    Mat imgTemp(tempH, tempW, CV_8UC4, (unsigned char *) cbufImgTemp);

    cv::cvtColor(imgSrc, imgSrc, cv::COLOR_BGRA2GRAY);
    cv::cvtColor(imgTemp, imgTemp, cv::COLOR_BGRA2GRAY);

    cv::Ptr<GeneralizedHough> alg;
    cv::Ptr<GeneralizedHoughGuil> guil = createGeneralizedHoughGuil();

    guil->setMinDist(minDist);
    guil->setLevels(level);
    guil->setDp(dp);
    guil->setMaxBufferSize(maxBufferSize);

    guil->setMinAngle(minAngle);
    guil->setMaxAngle(maxAngle);
    guil->setAngleStep(angleStep);
    guil->setAngleThresh(angleThresh);

    guil->setMinScale(minScale);
    guil->setMaxScale(maxScale);
    guil->setScaleStep(scaleStep);
    guil->setScaleThresh(scaleThresh);

    guil->setPosThresh(posThresh);

    alg = guil;
    alg->setTemplate(imgTemp);

    std::vector<Vec4f> position;
    alg->detect(imgSrc, position);

    jfloatArray result = makeResultBuffer(env, position);

    env->ReleaseIntArrayElements(jintImgBuf, cbufImgSrc, 0);
    env->ReleaseIntArrayElements(jintTempImgBuf, cbufImgTemp, 0);

    return result;
}

/*
 * Class:     philipyexushen_opencvhandler_HandlerWrapper
 * Method:    nativeDrawGeneralizedHough
 * Signature: ([III[FIIIIIIIII)[I
 */
JNIEXPORT jintArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_nativeDrawGeneralizedHough
        (JNIEnv *env, jclass, jintArray jintImgBuf, jint h, jint w, jfloatArray jfloatPositionBuf, jint posBufLength, jint tempH, jint tempW,
         jint r, jint g, jint b, jint thickness, jint lineType, jint shift)
{
    jint *cbuf = env->GetIntArrayElements(jintImgBuf, JNI_FALSE);
    jfloat *positionBuf = env->GetFloatArrayElements(jfloatPositionBuf, JNI_FALSE);

    if (cbuf == NULL || positionBuf == NULL){
        return jintImgBuf;
    }

    Mat imgSrc(h, w, CV_8UC4, (unsigned char *) cbuf);
    int size = posBufLength / 4;

    for (int i = 0; i < size; i++)
    {
        float x = positionBuf[4*i + 0];
        float y = positionBuf[4*i + 1];
        float scale = positionBuf[4*i + 2];
        float angle = positionBuf[4*i + 3];

        Point2f pos(x, y);

        RotatedRect rect;
        rect.center = pos;
        rect.size = Size2f(tempW * scale, tempH * scale);
        rect.angle = angle;

        Point2f pts[4];
        rect.points(pts);

        line(imgSrc, pts[0], pts[1], Scalar(r, g, b), thickness, lineType, shift);
        line(imgSrc, pts[1], pts[2], Scalar(r, g, b), thickness, lineType, shift);
        line(imgSrc, pts[2], pts[3], Scalar(r, g, b), thickness, lineType, shift);
        line(imgSrc, pts[3], pts[0], Scalar(r, g, b), thickness, lineType, shift);
    }

    env->ReleaseIntArrayElements(jintImgBuf, cbuf, 0);
    env->ReleaseFloatArrayElements(jfloatPositionBuf, positionBuf, 0);
    return jintImgBuf;
}
}





