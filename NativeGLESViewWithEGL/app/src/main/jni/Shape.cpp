//
// Created by weiersyuan on 2016/11/24.
//

#include "Shape.h"
#include "GLUtil.h"
#include <math.h>
#define  TAG  "TestGL"
void Shape::initVertex(){

    mVertexArray = new GLfloat[3*8];
    mColorArray = new GLfloat[8*4];
     int j = 0, k = 0;
     mVertexArray[j++] = 0;
     mVertexArray[j++] = 0;
     mVertexArray[j++] = 0;

     mColorArray[k++] = 1;
     mColorArray[k++] = 1;
     mColorArray[k++] = 1;
     mColorArray[k++] = 0;
     for (int angle = 0; angle <= 360; angle += 60) {
         mVertexArray[j++] = (GLfloat) (cos(PI * angle / 180));
         mVertexArray[j++] = (GLfloat) (sin(PI * angle / 180));
         mVertexArray[j++] = 0;

         mColorArray[k++] = 1;
         mColorArray[k++] = 0;
         mColorArray[k++] = 0;
         mColorArray[k++] = 0;
     }
}


void Shape::initGL(const char *vertexShaderCode, const char *fragmentShaderCode) {
    mProgram = GLUtil::createProgram(vertexShaderCode, fragmentShaderCode);
    mUMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    mAPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    mAColorHandle = glGetAttribLocation(mProgram, "aColor");
    initTexture( 1 );
}
 void Shape::initTexture(int res) {
     GLuint  textures;
    glGenTextures(1, &textures);
     mLoadedTextureId = textures;
    glBindTexture(GL_TEXTURE_2D, mLoadedTextureId);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);
   // Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);
    //texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
    //bitmap.recycle();
     glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1024, 768, 0, GL_RGBA, GL_UNSIGNED_SHORT_5_6_5, NULL);

     glBindTexture(GL_TEXTURE_2D, 0);
}


void Shape::draw(float mvpMatrix[]) {

    GLuint FramebufferName = 0;
    glGenFramebuffers(1, &FramebufferName);
    glBindFramebuffer(GL_FRAMEBUFFER, FramebufferName);

// The texture we're going to render to
    GLuint renderedTexture;
    glGenTextures(1, &renderedTexture);
// "Bind" the newly created texture : all future texture functions will modify this texture
    glBindTexture(GL_TEXTURE_2D, renderedTexture);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_MIN_FILTER, GL_NEAREST);
     glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
    // glTexImage2D( GL_TEXTURE_2D, 0,  GL_RGB, ShapeView.sScreenWidth, ShapeView.sScreenHeight, 0,  GL_RGB,  GL_UNSIGNED_SHORT_5_6_5, null);

    //mLoadedTextureId = renderedTexture;
// Give an empty image to OpenGL ( the last "0" )
    glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB, 1024, 768, 0,GL_RGB, GL_UNSIGNED_BYTE, 0);
    glBindTexture(GL_TEXTURE_2D, 0);

    // The renderbuffers buffer
    GLuint renderbuffers;
    glGenRenderbuffers(1, &renderbuffers);
    glBindRenderbuffer(GL_RENDERBUFFER, renderbuffers);
    glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, 1024, 768);
    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbuffers);

    /*================================render2texture================================*/

    // 关联FrameBuffer和Texture、RenderBuffer
    glFramebufferTexture2D( GL_FRAMEBUFFER,  GL_COLOR_ATTACHMENT0,  GL_TEXTURE_2D, renderedTexture, 0);
     glFramebufferRenderbuffer( GL_FRAMEBUFFER,  GL_DEPTH_ATTACHMENT,  GL_RENDERBUFFER, renderbuffers);
    if( glCheckFramebufferStatus( GL_FRAMEBUFFER) !=  GL_FRAMEBUFFER_COMPLETE) {
        LOGI(TAG, "Framebuffer error");
    }
     glClear( GL_DEPTH_BUFFER_BIT |  GL_COLOR_BUFFER_BIT);
    //int frameBufferVertexShader = glloaderShader( GL_VERTEX_SHADER, vertexShaderCode);
    //int frameBufferFagmentShader = loaderShader( GL_FRAGMENT_SHADER, fragmentShaderCode);
   // mProgram =  glCreateProgram();
    // glAttachShader(mFrameBufferProgram, frameBufferVertexShader);
    // glAttachShader(mFrameBufferProgram, frameBufferFagmentShader);
    // glLinkProgram(mFrameBufferProgram);
   // int fbPositionHandle =  glGetAttribLocation(mFrameBufferProgram, "aPosition");
   // int fbNormalHandle =  glGetAttribLocation(mFrameBufferProgram, "aNormal");
   // int fbTextureCoordHandle =  glGetAttribLocation(mFrameBufferProgram, "aTextureCoord");
   // int fbuMVPMatrixHandle =  glGetUniformLocation(mFrameBufferProgram, "uMVPMatrix");
   // int fbuMMatrixHandle =  glGetUniformLocation(mFrameBufferProgram, "uMMatrix");
   // int fbuLightLocationHandle =  glGetUniformLocation(mFrameBufferProgram, "uLightLocation");
   // int fbuTextureHandle =  glGetUniformLocation(mFrameBufferProgram, "uTexture");
   //  glUseProgram(mFrameBufferProgram);
   // mVertexBuffer.position(0);
    // glVertexAttribPointer(fbPositionHandle, 3,  GL_FLOAT, false, 3 * 4, mVertexBuffer);
   // mTexureBuffer.position(0);
   //  glVertexAttribPointer(fbTextureCoordHandle, 2,  GL_FLOAT, false, 2 * 4, mTexureBuffer);
  //  mTexureBuffer.position(0);
    // glVertexAttribPointer(fbNormalHandle, 3,  GL_FLOAT, false, 3 * 4, mNormalBuffer);
   //  glEnableVertexAttribArray(fbPositionHandle);
   //  glEnableVertexAttribArray(fbTextureCoordHandle);
   //  glEnableVertexAttribArray(fbNormalHandle);
   //  glUniform3f(fbuLightLocationHandle, 0, 10, 10);
    // glUniformMatrix4fv(fbuMVPMatrixHandle, 1, false, mvpMatrix, 0);
   //  glUniformMatrix4fv(fbuMMatrixHandle, 1, false, mMatrix, 0);
    // glBindTexture( GL_TEXTURE_2D, mLoadedTextureId);
    // glUniform1i(fbuTextureHandle, 0);
   //  glDrawArrays( GL_TRIANGLES, 0, mVertexCount);
    glUseProgram(mProgram);
    // 将顶点数据传递到管线，顶点着色器
    glUniformMatrix4fv(mUMVPMatrixHandle, 1, GL_FALSE, mvpMatrix);
    glVertexAttribPointer(mAPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    // 将顶点颜色传递到管线，顶点着色器
    glVertexAttribPointer(mAColorHandle, 4, GL_FLOAT, GL_FALSE, 4*4, mColorArray);

    glEnableVertexAttribArray(mAPositionHandle);
    glEnableVertexAttribArray(mAColorHandle);
    glBindTexture( GL_TEXTURE_2D, mLoadedTextureId);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_FAN, 0, 8);


    /*================================render2window================================*/
    // 切换到窗口系统的缓冲区
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    //int vertexShader = loaderShader( GL_VERTEX_SHADER, windowVertexShaderCode);
    //int fragmentShader = loaderShader( GL_FRAGMENT_SHADER, windowFragmentShaderCode);
   // mWindowProgram =  glCreateProgram();
   //  glAttachShader(mWindowProgram, vertexShader);
   //  glAttachShader(mWindowProgram, fragmentShader);
   //  glLinkProgram(mWindowProgram);
  //   glUseProgram(mWindowProgram);
    //int positionHandle =  glGetAttribLocation(mWindowProgram, "aPosition");
   // int textureCoordHandle =  glGetAttribLocation(mWindowProgram, "aTextureCoord");
   // int textureHandle =  glGetUniformLocation(mWindowProgram, "uTexture");
   // mSqureBuffer.position(0);
   //  glVertexAttribPointer(positionHandle, 2,  GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
   // mSqureBuffer.position(2);
   //  glVertexAttribPointer(textureCoordHandle, 2,  GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
   //  glEnableVertexAttribArray(positionHandle);
  //   glEnableVertexAttribArray(textureCoordHandle);
    glUseProgram(mProgram);
    // 将顶点数据传递到管线，顶点着色器
    glUniformMatrix4fv(mUMVPMatrixHandle, 1, GL_FALSE, mvpMatrix);
    glVertexAttribPointer(mAPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    // 将顶点颜色传递到管线，顶点着色器
    glVertexAttribPointer(mAColorHandle, 4, GL_FLOAT, GL_FALSE, 4*4, mColorArray);

    glEnableVertexAttribArray(mAPositionHandle);
    glEnableVertexAttribArray(mAColorHandle);
    //glBindTexture( GL_TEXTURE_2D, mLoadedTextureId);
    glBindTexture(GL_TEXTURE_2D, renderedTexture);
    glActiveTexture(GL_TEXTURE0);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_FAN, 0, 8);

     glDeleteTextures(1, &renderedTexture);
     glDeleteFramebuffers(1, &FramebufferName);
     glDeleteRenderbuffers(1,&renderedTexture);
}
Shape::Shape() {
    initVertex();
}
Shape::~Shape() {
    delete [] mVertexArray;
    delete [] mColorArray;
}
