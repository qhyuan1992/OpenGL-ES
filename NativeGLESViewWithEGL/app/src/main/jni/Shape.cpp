//
// Created by weiersyuan on 2016/11/24.
//

#include "Shape.h"
#include "GLUtil.h"
#include <math.h>
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
}

void Shape::draw(float mvpMatrix[]) {
    glUseProgram(mProgram);
    // 将顶点数据传递到管线，顶点着色器
    glUniformMatrix4fv(mUMVPMatrixHandle, 1, GL_FALSE, mvpMatrix);
    glVertexAttribPointer(mAPositionHandle, 3, GL_FLOAT, GL_FALSE, 3 * 4, mVertexArray);
    // 将顶点颜色传递到管线，顶点着色器
    glVertexAttribPointer(mAColorHandle, 4, GL_FLOAT, GL_FALSE, 4*4, mColorArray);

    glEnableVertexAttribArray(mAPositionHandle);
    glEnableVertexAttribArray(mAColorHandle);
    // 绘制图元
    glDrawArrays(GL_TRIANGLE_FAN, 0, 8);

}
Shape::Shape() {
    initVertex();
}
Shape::~Shape() {
    delete [] mVertexArray;
    delete [] mColorArray;
}
