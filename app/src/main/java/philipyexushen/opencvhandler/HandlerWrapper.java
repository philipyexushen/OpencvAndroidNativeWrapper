package philipyexushen.opencvhandler;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Philip on 2018/2/24.
 */

public class HandlerWrapper {
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("opencvhandler");
    }

    public static Bitmap houghCircles(Bitmap image, double dp,
                                  double minDist, double cannyThreshold,
                                  double accumulatorThreshold, int minRadius, int maxRadius){
        int h = image.getHeight(), w = image.getWidth();
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);

        int[] rawBitmap = new int[h*w];
        image.getPixels(rawBitmap,0, w, 0,0, w, h);

        int[] rawBitmapWithHoughCircles
                = nativeHoughCircles(rawBitmap, h, w, dp,
                                minDist, cannyThreshold,accumulatorThreshold,minRadius,maxRadius);
        result.setPixels(rawBitmapWithHoughCircles, 0,w,0,0, w, h);

        return result;
    }

    public static float []generalizedHoughBallard(Bitmap image, Bitmap templ,
                                                 int cannyLowThresh, int cannyHighThresh,
                                                 double minDist, double dp, int level, int votesThreshold,
                                                 int maxBufferSize){
        int h = image.getHeight(), w = image.getWidth();
        int tempH = templ.getHeight(), tempW = templ.getWidth();

        int[] rawSrcBitmap = new int[h*w];
        image.getPixels(rawSrcBitmap,0, w, 0,0, w, h);

        int[] rawTemplBitmap = new int[tempH*tempW];
        templ.getPixels(rawTemplBitmap,0, tempW, 0,0, tempW, tempH);

        return nativeGeneralizedHoughBallard(rawSrcBitmap, rawTemplBitmap, h, w, tempH, tempW,
                cannyLowThresh, cannyHighThresh, minDist, dp, level, votesThreshold, maxBufferSize);
    }

    public static Bitmap drawGeneralizedHoughBallard(Bitmap image, float []position, int tempH, int tempW,
                                                     int r, int g, int b,
                                                     int thickness, int lineType, int shift){
        int h = image.getHeight(), w = image.getWidth();
        int[] rawSrcBitmap = new int[h*w];
        image.getPixels(rawSrcBitmap,0, w, 0,0, w, h);
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);

        assert(position.length % 4 == 0);

        int[] rawBitmapWithHoughCircles
                = nativeDrawGeneralizedHoughBallard(rawSrcBitmap, h, w, position, position.length, tempH, tempW,
                        r, g, b, thickness, lineType, shift);

        result.setPixels(rawBitmapWithHoughCircles, 0,w,0,0, w, h);
        return result;
    }

    /*
    The function finds circles in a grayscale image using a modification of the Hough transform.

    @note Usually the function detects the centers of circles well. However, it may fail to find correct
    radii. You can assist to the function by specifying the radius range ( minRadius and maxRadius ) if
    you know it. Or, you may set maxRadius to 0 to return centers only without radius search, and find the correct
    radius using an additional procedure.

    @param image 8-bit, single-channel, grayscale input image.
    @param h image height
    @param w image width
    @param dp Inverse ratio of the accumulator resolution to the image resolution. For example, if
    dp=1 , the accumulator has the same resolution as the input image. If dp=2 , the accumulator has
    half as big width and height.
    @param minDist Minimum distance between the centers of the detected circles. If the parameter is
    too small, multiple neighbor circles may be falsely detected in addition to a true one. If it is
    too large, some circles may be missed.
    @param cannyThreshold First method-specific parameter. In case of CV_HOUGH_GRADIENT , it is the higher
    threshold of the two passed to the Canny edge detector (the lower one is twice smaller).
    @param accumulatorThreshold Second method-specific parameter. In case of CV_HOUGH_GRADIENT , it is the
    accumulator threshold for the circle centers at the detection stage. The smaller it is, the more
    false circles may be detected. Circles, corresponding to the larger accumulator values, will be
    returned first.
    @param minRadius Minimum circle radius.
    @param maxRadius Maximum circle radius.
    @return circles Output vector of found circles. Each vector is encoded as a 3-element
    floating-point vector \f$(x, y, radius)\f$ .

    @sa fitEllipse, minEnclosingCircle
    */
    private static native int[] nativeHoughCircles(int[] image, int h, int w, double dp,
                                            double minDist, double cannyThreshold,
                                            double accumulatorThreshold, int minRadius, int maxRadius);

    //! Ballard, D.H. (1981). Generalizing the Hough transform to detect arbitrary shapes. Pattern Recognition 13 (2): 111-122.
    //! Detects position only without translation and rotation

    private static native float[]  nativeGeneralizedHoughBallard(int[] image, int[] template,
                                                                 int h, int w,
                                                                 int tempH, int tempW,
                                                                 int cannyLowThresh, int cannyHighThresh,
                                                                 double minDist, double dp, int level, int votesThreshold,
                                                                 int maxBufferSize);

    private static native int[] nativeDrawGeneralizedHoughBallard(int[] image, int h, int w,
                                                                  float []position, int posBufLength,int tempH, int tempW,
                                                                  int r, int g, int b,
                                                                  int thickness, int lineType, int shift);
}
