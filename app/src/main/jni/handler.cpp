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
JNIEXPORT jintArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_houghCircles
        (JNIEnv *env, jclass, jintArray jintImgBuf, jint h, jint w, jdouble dp,
         jdouble minDist, jdouble cannyThreshold, jdouble accumulatorThreshold, jint minRadius, jint maxRadius) {

    jint *cbuf = env->GetIntArrayElements(jintImgBuf, JNI_FALSE);
    if (cbuf == NULL)
        return 0;

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

    int size = h*w;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result, 0, size, cbuf);
    env->ReleaseIntArrayElements(jintImgBuf, cbuf, 0);

    return result;
}

JNIEXPORT jfloatArray JNICALL Java_philipyexushen_opencvhandler_HandlerWrapper_generalizedHoughBallard
        (JNIEnv *, jclass, jintArray, jintArray, jint, jint, jdouble, jdouble, jint, jint, jint) {

}
}





