//
// Created by weiersyuan on 2016/11/24.
//

#ifndef NATIVEGLESVIEW_GLUTIL_H
#define NATIVEGLESVIEW_GLUTIL_H

#include <GLES2/gl2.h>
#include <android/log.h>
#define LOGI(level, ...) __android_log_print(ANDROID_LOG_INFO, "NATIVE_LOG", __VA_ARGS__)
class GLUtil {

public:
    static int compileShader(int type, const char* shaderCode);
    static int createProgram(const char * vertexShaderCode, const char * fragmentShaderCode);
};


#endif //NATIVEGLESVIEW_GLUTIL_H
