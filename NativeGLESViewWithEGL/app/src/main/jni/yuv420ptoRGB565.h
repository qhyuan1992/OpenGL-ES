//
// Created by vivisong on 05/02/2018.
//
#ifndef NATIVEGLESVIEWWITHEGL_YUV420PTORGB565_H
#define NATIVEGLESVIEWWITHEGL_YUV420PTORGB565_H
#include <stdio.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

class yuv420ptoRGB565 {

public:
    yuv420ptoRGB565():g_buffer(NULL),g_width(3840),g_height(2160)
    {};

     void checkGlError(const char* op);
     GLuint bindTexture(GLuint texture, const char *buffer, GLuint w , GLuint h);
     void renderFrame();
     GLuint buildShader(const char* source, GLenum shaderType);
     GLuint buildProgram(const char* vertexShaderSource,
                               const char* fragmentShaderSource);
     unsigned char * readYUV(const char *path);
    void gl_initialize();
    void gl_uninitialize();
    void gl_set_framebuffer(const char* buffer, int buffersize, int width, int height);
    void gl_render_frame();
private:
    unsigned char *              g_buffer ;
     int                 g_width  ;
     int                 g_height ;

    GLuint g_texYId;
    GLuint g_texUId;
    GLuint g_texVId;
    GLuint simpleProgram;
};

#endif //NATIVEGLESVIEWWITHEGL_YUV420PTORGB565_H

