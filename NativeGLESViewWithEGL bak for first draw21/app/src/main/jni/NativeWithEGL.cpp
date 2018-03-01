#include <jni.h>
#include "Renderer.h"
#include <android/native_window_jni.h>
#include <android/bitmap.h>
#include <android/log.h>


ANativeWindow * mWindow;
Renderer * mRenderer;
#define LOGI(level, ...) __android_log_print(ANDROID_LOG_INFO, "NATIVE_LOG", __VA_ARGS__)
extern "C" {
JNIEXPORT void JNICALL
Java_com_example_weiersyuan_nativeglesviewwithegl_MySurfaceView_nativeStartRender(JNIEnv *env,
                                                                                  jclass type ,
                                                                                  jobject bmpObj) {
    AndroidBitmapInfo bmpInfo = {0};
    if (AndroidBitmap_getInfo(env, bmpObj, &bmpInfo) < 0) {
        LOGI(1," AndroidBitmap_getInfo fail");
        return ;
    }

   //if (bmpInfo.format != ANDROID_BITMAP_FORMAT_RGB_565) {
    LOGI(1," bmpInfo.format =%d width = %d height = %d stride =%d!", bmpInfo.format, bmpInfo.width, bmpInfo.height, bmpInfo.stride);
    //  return -1;
    // }
    int *dataFromBmp = NULL;

    if (AndroidBitmap_lockPixels(env, bmpObj, (void **) &dataFromBmp) < 0) {
        return ;
    }
    LOGI(1,"new Rendererstart()");
    mRenderer = new Renderer();
    mRenderer->setBmpPtr(dataFromBmp, bmpInfo.width, bmpInfo.height );
    mRenderer->start();
    //if (AndroidBitmap_unlockPixels(env, bmpObj) < 0){
    //    return ;
    //}
}

JNIEXPORT void JNICALL
Java_com_example_weiersyuan_nativeglesviewwithegl_MySurfaceView_nativeSurfaceChanged(JNIEnv *env,
                                                                                     jclass type,
                                                                                     jobject surface) {
    mWindow = ANativeWindow_fromSurface(env, surface);
    // surfacechange时 发送SurfaceChanged消息，此时创建egl环境的消息
    mRenderer->requestInitEGL(mWindow);

}

JNIEXPORT void JNICALL
Java_com_example_weiersyuan_nativeglesviewwithegl_MySurfaceView_nativeSurfaceDestroyed(JNIEnv *env,
                                                                                       jclass type) {
    mRenderer->requestDestroy();
    ANativeWindow_release(mWindow);
    delete mRenderer;
}

JNIEXPORT void JNICALL
Java_com_example_weiersyuan_nativeglesviewwithegl_MySurfaceView_nativeRequestRender(JNIEnv *env,
                                                                                    jclass type) {

    mRenderer->requestRenderFrame();

}


}