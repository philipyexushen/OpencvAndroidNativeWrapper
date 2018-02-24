LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
#opencv
include G:/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED

LOCAL_SRC_FILES := handler.cpp
LOCAL_LDLIBS += -llog
LOCAL_MODULE := opencvhandler
include $(BUILD_SHARED_LIBRARY)