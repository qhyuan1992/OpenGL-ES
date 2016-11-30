//
// Created by weiersyuan on 2016/11/30.
//

#ifndef NATIVEGLESVIEWWITHEGL_RENDERDER_H
#define NATIVEGLESVIEWWITHEGL_RENDERDER_H

#include <pthread.h>
#include <android/native_window.h>
#include <EGL/egl.h>
#include <GLES/gl.h>
class Renderer {
public:
    Renderer();
    virtual ~Renderer();

    void onRenderThreadRun();

    void start();

    void stop();

    void requestInitEGL(ANativeWindow * pWindow);
    void requestRenderFrame();
    void requestDestroy();

     void nativeSurfaceCreated();

     void nativeSurfaceChanged(EGLint width, EGLint height);

     void nativeDraw();


private:
    enum RenderEvent {
        RE_NONE,
        RE_SURFACE_CREATED,
        RE_SURFACE_CHANGED,
        RE_DRAW_FRAME,
        RE_EXIT
    };

    enum RenderEvent mEnumRenderEvent;
    pthread_t  mThread;
    pthread_mutex_t mMutex;
    pthread_cond_t mCondVar;

    ANativeWindow *mWindow;

    EGLDisplay mDisplay;
    EGLSurface mSurface;
    EGLContext mContext;

    static void *startRenderThread(void *);

    void initEGL();

    EGLint mWidth;
    EGLint mHeight;



    void terminateDisplay();

    bool mISRenderering;

};





#endif //NATIVEGLESVIEWWITHEGL_RENDERDER_H
