//
// Created by weiersyuan on 2016/11/24.
//

#include "Shape.h"
#include "yuv420ptoRGB565.h"
#include "GLUtil.h"
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <memory.h>
#include <android/bitmap.h>
#include <time.h>

#define  TAG  "TestGL"
void Shape::initVertex(){
#if 0
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
    msquareVertexs = new GLfloat[4*4];
    int j = 0, k = 0;
    mVertexArray[j++] = -1;
    mVertexArray[j++] = -1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] = 1;
    mVertexArray[j++] = -1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] = -1;
    mVertexArray[j++] = 1;
    mVertexArray[j++] = 0;

    mVertexArray[j++] = 1;
    mVertexArray[j++] = 1;
    mVertexArray[j++] = 0;

    mTextureArray[k++] = 0;
    mTextureArray[k++] = 1;
    mTextureArray[k++] = 0;

    mTextureArray[k++] = 0;
    mTextureArray[k++] = 0;
    mTextureArray[k++] = 0;

    mTextureArray[k++] = 1;
    mTextureArray[k++] = 1;
    mTextureArray[k++] = 0;

    mTextureArray[k++] = 1;
    mTextureArray[k++] = 0;
    mTextureArray[k++] = 0;


    // 初始化顶点坐标
    k = 0;
    msquareVertexs[k++] = -1;
    msquareVertexs[k++] = -1;
    msquareVertexs[k++] = 0;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = -1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 0;
    msquareVertexs[k++] = 0;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = -1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 1;
    msquareVertexs[k++] = 0;


/*
    ByteBuffer vbb0 = ByteBuffer.allocateDirect(squareVertexs.length * 4);
    vbb0.order(ByteOrder.nativeOrder());
    mSqureBuffer = vbb0.asFloatBuffer();
    mSqureBuffer.put(squareVertexs);
    mSqureBuffer.position(0);

    mVertexCount = vertexArray.length / 3;
    ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
    vbb.order(ByteOrder.nativeOrder());
    mVertexBuffer = vbb.asFloatBuffer();
    mVertexBuffer.put(vertexArray);
    mVertexBuffer.position(0);

    ByteBuffer vbb2 = ByteBuffer.allocateDirect(texures.length * 4);
    vbb2.order(ByteOrder.nativeOrder());
    mTexureBuffer = vbb2.asFloatBuffer();
    mTexureBuffer.put(texures);
    mTexureBuffer.position(0);
*/
#endif
}


void Shape::initGL(const char *vertexShaderCode, const char *fragmentShaderCode, int * ptr, int w, int h) {
    mProgram = GLUtil::createProgram(vertexShaderCode, fragmentShaderCode);
    initTextureBMP( ptr, w, h );
    mFramebufferName = 0;
    glGenFramebuffers(1, &mFramebufferName);
    mtexture = 0;
    glGenTextures(1, &mtexture);
    mColorTexture = 0;
    glGenTextures(1, &mColorTexture);

    yuv420ptoRGB565.gl_initialize();

}
 void Shape::initTextureBMP(int* ptr,int w, int h) {

     outWidth = w;
     outHeight = h;

     LOGI(TAG, "bmp in w = %d  h = %d",  w, h);
    // bmpPtr = //loadRaw("/data/data/com.example.weiersyuan.nativeglesviewwithegl/fourk.yuv", 3840, 2160);
     bmpPtr = (unsigned char *)ptr;
     readOut = (unsigned char *) malloc (outWidth * outHeight * 2);

             LOGI(TAG, "bmp in = 0x%x  0x%x w = %d  h = %d", *bmpPtr, *(bmpPtr+1), w, h);
     initPBO(w,h, (int * )bmpPtr);
}


void Shape::initPBO(int w, int h, int *ptr )
{
   // PBOsample.Initialize( w,  h, ptr);
   // PBOsample.initYUVTexure();
    yuv420ptoRGB565.gl_initialize();
}

void Shape::drawPBO(float mvpMatrix[])
{
    //PBOsample.UpdateTextureFBOandrender();
   //PBOsample.Render();
  //  PBOsample.renderYUV();
    double  before = now_ms();
    yuv420ptoRGB565.gl_render_frame();
    double  after = now_ms();

    LOGI(1, "change yuv420 to rgb565 by texture = %lf", after - before);
}
void Shape::draw(float mvpMatrix[]) {
#if 1
    if(1){
        //draw1(mvpMatrix);/
        drawPBO( mvpMatrix);
    }

      return ;

    if (bmpPtr == NULL) {
        LOGI(TAG, "bmp draw null return");
    return;
}
#endif
   // draw1(mvpMatrix);
   // return;
    static double before = now_ms();
   // LOGI(1, "4K bmp before process time = %lf", before);

    mLoadedTextureId = mtexture;
    glBindTexture(GL_TEXTURE_2D, mLoadedTextureId);

    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, outWidth, outHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, (unsigned char *)bmpPtr);
    glBindTexture(GL_TEXTURE_2D, 0);
    static double  newTExture = now_ms();

    double delta0 = newTExture - before;
    LOGI(1, "4K bmp Texute time = %lf", delta0);


// The texture we're going to render to

// "Bind" the newly created texture : all future texture functions will modify this texture
     glBindTexture(GL_TEXTURE_2D, mColorTexture);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_MIN_FILTER, GL_NEAREST);
     glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
     glTexParameterf( GL_TEXTURE_2D,  GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, outWidth, outHeight, 0,GL_RGB, GL_UNSIGNED_BYTE, 0);
    glBindFramebuffer(GL_FRAMEBUFFER, mFramebufferName);

    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mColorTexture, 0);

    if( glCheckFramebufferStatus( GL_FRAMEBUFFER) !=  GL_FRAMEBUFFER_COMPLETE) {
        LOGI(TAG, "Framebuffer error");
    }
     glClear( GL_DEPTH_BUFFER_BIT |  GL_COLOR_BUFFER_BIT);

    int fbPositionHandle =  glGetAttribLocation(mProgram, "aPosition");
    int fbTextureCoordHandle =  glGetAttribLocation(mProgram, "aTextureCoord");
    int fbuTextureHandle =  glGetUniformLocation(mProgram, "uTexture");
    //mUMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    glUseProgram(mProgram);

    // 将顶点数据传递到管线
    //glUniformMatrix4fv(mUMVPMatrixHandle, 1, GL_FALSE, mvpMatrix);

    glVertexAttribPointer(fbPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    glEnableVertexAttribArray(fbPositionHandle);
    glVertexAttribPointer(fbTextureCoordHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mTextureArray);
    glEnableVertexAttribArray(fbTextureCoordHandle);

    //glVertexAttribPointer(fbTextureCoordHandle, 2, GL_FLOAT, GL_FALSE, 4 * 4, msquareVertexs);
    //glEnableVertexAttribArray(fbTextureCoordHandle);
    //msquareVertexs

    glBindTexture(GL_TEXTURE_2D, mLoadedTextureId);
    glUniform1i(fbuTextureHandle, 0);
    //glActiveTexture(GL_TEXTURE0);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    static double beforeRead = now_ms();
    double delta1 = beforeRead - before;
    LOGI(1, "4K bmp before read time = %lf", delta1);
    glReadPixels(0,0, outWidth-1, outHeight-1, GL_RGB, GL_UNSIGNED_BYTE, (void *)readOut);
   // LOGI(TAG, "bmp out = 0x%x  0x%x w = %d  h = %d", *readOut, *(readOut+1), outWidth, outHeight);
    static double after = now_ms();
    //LOGI(1, "4K bmp after process time = %lf", after);
    // glDeleteRenderbuffers(1,&renderedTexture);
    double delta = after - before;
    LOGI(1, "4K bmp  all process time = %lf", delta);
     glBindFramebuffer(GL_FRAMEBUFFER, 0);
    draw1(mvpMatrix);

#if 0//render_to_window
    /*================================render2window================================*/
    // 切换到窗口系统的缓冲区
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
#if 0
    glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
    glUseProgram(mProgram);

    glVertexAttribPointer(fbPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    glEnableVertexAttribArray(fbPositionHandle);
   glVertexAttribPointer(fbTextureCoordHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mTextureArray);
   glEnableVertexAttribArray(fbTextureCoordHandle);
#endif
    glBindTexture(GL_TEXTURE_2D, mColorTexture);
    glActiveTexture(GL_TEXTURE0);
    glUniform1i(fbuTextureHandle, 0);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
#endif


}
Shape::Shape() :bmpPtr(NULL), PBOsample("PBO test"), yuv420ptoRGB565(){
    initVertex();
}
Shape::~Shape() {

    glDeleteTextures(1, &mLoadedTextureId);
    glDeleteTextures(1, &mColorTexture);
    glDeleteFramebuffers(1, &mFramebufferName);
    yuv420ptoRGB565.gl_uninitialize();
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

unsigned char * Shape::loadRaw(const char * imagepath, unsigned int outWidth, unsigned int outHeight){
    LOGI(1,"Reading YUV image %s\n", imagepath);
    outWidth = -1;
    outHeight = -1;
    // Data read from the header of the BMP file
 //   unsigned char header[54];
   unsigned int dataPos;
    unsigned int imageSize;
    // Actual RGB data
    unsigned char * data;

    // Open the file
    FILE * file = fopen(imagepath,"rb");
    if (!file)							    {LOGI(1,"Image could not be opened\n"); return NULL;}

    // Read the header, i.e. the 54 first bytes
#if 0
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
#endif

    // Some BMP files are misformatted, guess missing information
    if (imageSize==0)    imageSize=outWidth*outHeight*3/2; // 3 : one byte for each Red, Green and Blue component
    if (dataPos==0)      dataPos=0; // The BMP header is done that way

    // Create a buffer
    data = new unsigned char [imageSize];

    // Read the actual data from the file into the buffer
    int sizeRaw = fread(data,1,imageSize,file);
  if(sizeRaw != imageSize)
  {
      LOGI(1, "read YUV size error");

  }
    // Everything is in memory now, the file wan be closed
    fclose (file);
#if 0
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
#endif
    return data;
}

// from android samples
/* return current time in milliseconds */
 double Shape::now_ms(void) {

    struct timespec res;
    clock_gettime(CLOCK_REALTIME, &res);
    return 1000.0 * res.tv_sec + (double) res.tv_nsec / 1e6;

}
void Shape::draw1(float mvpMatrix[]) {

    int fbPositionHandle =  glGetAttribLocation(mProgram, "aPosition");
    int fbTextureCoordHandle =  glGetAttribLocation(mProgram, "aTextureCoord");
    int fbuTextureHandle =  glGetUniformLocation(mProgram, "uTexture");
    glUseProgram(mProgram);
    // 将顶点数据传递到管线
    //glUniformMatrix4fv(fbPositionHandle, 1, GL_FALSE, mvpMatrix);
    GLuint  tempTeture = 0;
    glGenTextures(1, &tempTeture);
    glBindTexture(GL_TEXTURE_2D, tempTeture);

    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, outWidth, outHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, (unsigned char *)bmpPtr);

    glBindTexture(GL_TEXTURE_2D, 0);

    glVertexAttribPointer(fbPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    glEnableVertexAttribArray(fbPositionHandle);
    glVertexAttribPointer(fbTextureCoordHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mTextureArray);
    glEnableVertexAttribArray(fbTextureCoordHandle);

    //glVertexAttribPointer(fbTextureCoordHandle, 2, GL_FLOAT, GL_FALSE, 4 * 4, msquareVertexs);
    //glEnableVertexAttribArray(fbTextureCoordHandle);
    //msquareVertexs
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, tempTeture);
    glUniform1i(fbuTextureHandle, 0);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

}