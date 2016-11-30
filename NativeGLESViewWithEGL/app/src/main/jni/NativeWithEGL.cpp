#include <jni.h>
#include "Renderer.h"
#include <android/native_window_jni.h>



ANativeWindow * mWindow;
Renderer * mRenderer;

extern "C" {
JNIEXPORT void JNICALL
Java_com_example_weiersyuan_nativeglesviewwithegl_MySurfaceView_nativeStartRender(JNIEnv *env,
                                                                                  jclass type) {
    mRenderer = new Renderer();
    mRenderer->start();

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