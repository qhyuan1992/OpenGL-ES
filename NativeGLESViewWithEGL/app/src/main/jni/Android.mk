LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := NativeWithEGL

LOCAL_SRC_FILES :=   Renderer.cpp NativeWithEGL.cpp GLUtil.cpp  Shape.cpp  NativeRenderImp.cpp  yuv420ptoRGB565.cpp Scene.cpp ./glm/detail/*.*  ./glm/gtc/*.*  ./glm/gtx/*.*  ./glm/simd/*.*  ./glm/*.*
LOCAL_LDFLAGS += -ljnigraphics

LOCAL_LDLIBS := -lGLESv3 -llog -lEGL -landroid

include $(BUILD_SHARED_LIBRARY)