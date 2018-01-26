//
// Created by weiersyuan on 2016/11/24.
//

#include "Shape.h"
#include "GLUtil.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <memory.h>

#define  TAG  "TestGL"
void Shape::initVertex(){
#if 1
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
#else
    mVertexArray = new GLfloat[3*4];
    mTextureArray = new GLfloat[3*4];

    int j = 0, k = 0;

    mVertexArray[j++] =  -1;
    mVertexArray[j++] =  -1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] =  1;
    mVertexArray[j++] =  -1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] =  -1;
    mVertexArray[j++] =  1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] =  -1;
    mVertexArray[j++] =  1;
    mVertexArray[j++] = 0;

  //  mColorArray[k++] = 1;
   // mColorArray[k++] = 1;
   // mColorArray[k++] = 1;
  //  mColorArray[k++] = 0;

    mTextureArray[k++] =  0;
    mTextureArray[k++] =  1;
    mTextureArray[k++] = 0;

    mTextureArray[k++] =  1;
    mTextureArray[k++] =  1;
    mTextureArray[k++] = 0;

    mTextureArray[k++] =  1;
    mTextureArray[k++] =  0;
    mTextureArray[k++] = 0;

    mTextureArray[k++] =  0;
    mTextureArray[k++] =  0;
    mTextureArray[k++] = 0;

  //  mCubeBuffer = ByteBuffer.allocateDirect(coord.length * 4)
    //        .order(ByteOrder.nativeOrder())
   //         .asFloatBuffer();
   // mCubeBuffer.put(coord).position(0);

  //  mTextureCubeBuffer = ByteBuffer.allocateDirect(texture_coord.length * 4)
   //         .order(ByteOrder.nativeOrder())
   //         .asFloatBuffer();
   // mTextureCubeBuffer.put(texture_coord).position(0);

#endif
}


void Shape::initGL(const char *vertexShaderCode, const char *fragmentShaderCode) {
    mProgram = GLUtil::createProgram(vertexShaderCode, fragmentShaderCode);
    mUMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    mAPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    mAColorHandle = glGetAttribLocation(mProgram, "aColor");
  //  initTexture( 1 );
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
     unsigned int outWidth;
     unsigned int outHeight;
     //unsigned char * data = loadBMPRaw("/data/old.bmp" , outWidth , outHeight, false);
     glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, outWidth, outHeight, 0, GL_RGBA, GL_UNSIGNED_SHORT_5_6_5, 0);
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
    glBindTexture( GL_TEXTURE_2D, renderedTexture);
    // 绘制图元
    glDrawArrays(GL_TRIANGLES, 0, 8);


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
    glDrawArrays(GL_TRIANGLES, 0, 8);

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


unsigned char * Shape::loadBMPRaw(const char * imagepath, unsigned int& outWidth, unsigned int& outHeight, bool flipY){
    LOGI(1,"Reading image %s\n", imagepath);
    outWidth = -1;
    outHeight = -1;
    // Data read from the header of the BMP file
    unsigned char header[54];
    unsigned int dataPos;
    unsigned int imageSize;
    // Actual RGB data
    unsigned char * data;

    // Open the file
    FILE * file = fopen(imagepath,"rb");
    if (!file)							    {LOGI(1,"Image could not be opened\n"); return NULL;}

    // Read the header, i.e. the 54 first bytes

    // If less than 54 byes are read, problem
    if ( fread(header, 1, 54, file)!=54 ){
        LOGI(1,"Not a correct BMP file\n");
        return NULL;
    }
    // A BMP files always begins with "BM"
    if ( header[0]!='B' || header[1]!='M' ){
        LOGI(1,"Not a correct BMP file\n");
        return NULL;
    }
    // Make sure this is a 24bpp file
    if ( *(int*)&(header[0x1E])!=0  )         {LOGI(1,"Not a correct BMP file\n");    return NULL;}
    if ( *(int*)&(header[0x1C])!=24 )         {LOGI(1,"Not a correct BMP file\n");    return NULL;}

    // Read the information about the image
    dataPos    = *(int*)&(header[0x0A]);
    imageSize  = *(int*)&(header[0x22]);
    outWidth      = *(int*)&(header[0x12]);
    outHeight     = *(int*)&(header[0x16]);

    // Some BMP files are misformatted, guess missing information
    if (imageSize==0)    imageSize=outWidth*outHeight*3; // 3 : one byte for each Red, Green and Blue component
    if (dataPos==0)      dataPos=54; // The BMP header is done that way

    // Create a buffer
    data = new unsigned char [imageSize];

    // Read the actual data from the file into the buffer
    fread(data,1,imageSize,file);

    // Everything is in memory now, the file wan be closed
    fclose (file);

    if (flipY){
        // swap y-axis
        unsigned char * tmpBuffer = new unsigned char[outWidth*3];
        int size = outWidth*3;
        for (int i=0;i<outHeight/2;i++){
            // copy row i to tmp
            memcpy(tmpBuffer,data+outWidth*3*i,size);
            // copy row h-i-1 to i
            memcpy(data+outWidth*3*i, data+outWidth*3*(outHeight-i-1), size);
            // copy tmp to row h-i-1
            memcpy(data+outWidth*3*(outHeight-i-1),tmpBuffer, size);
        }
        delete [] tmpBuffer;
    }

    return data;
}