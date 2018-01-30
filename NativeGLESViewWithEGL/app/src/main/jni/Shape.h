//
// Created by weiersyuan on 2016/11/24.
//

#ifndef NATIVEGLESVIEW_SHAPE_H
#define NATIVEGLESVIEW_SHAPE_H
#include <GLES2/gl2.h>

#define PI 3.1415926
class Shape {
public:
    GLint mProgram;
    GLint mUMVPMatrixHandle;
    GLint mAPositionHandle;
    GLint mAColorHandle;
    GLfloat* mVertexArray;
    GLfloat* mColorArray;
    GLfloat* msquareVertexs;
    GLuint mLoadedTextureId;
    GLfloat* mTextureArray;
    Shape();
    void initVertex();
    void initGL(const char * vertexShaderCode, const char* fragmentShaderCode,  int * ptr, int w, int h);
    void draw(float mvpMatrix[]);
    void draw1(float mvpMatrix[]);
    void initTextureBMP(int* ptr,int w, int h);
    virtual ~Shape();

    unsigned char * loadBMPRaw(const char * imagepath, unsigned int& outWidth, unsigned int& outHeight, bool flipY);
    static double now_ms(void);

    unsigned int outWidth;
    unsigned int outHeight;
    int * bmpPtr;
    GLuint mFramebufferName;
    GLuint mtexture;
    GLuint mColorTexture;
    unsigned char *readOut;
    };


#endif //NATIVEGLESVIEW_SHAPE_H
