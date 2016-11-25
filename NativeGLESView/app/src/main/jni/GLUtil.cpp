//
// Created by weiersyuan on 2016/11/24.
//

#include "GLUtil.h"
int GLUtil::compileShader(int type, const char * shaderCode) {

    int shader = glCreateShader(type);
    if (shader == 0) {

    }
    glShaderSource(shader, 1, &shaderCode, NULL);
    glCompileShader(shader);
    GLint compileStatus = 0;
        glGetShaderiv(shader, GL_COMPILE_STATUS, &compileStatus);
        if (!compileStatus) {
            glDeleteShader(shader);
            LOGI(1, "compile shader error");
            return 0;
        }
        return shader;
}

int GLUtil::createProgram(const char *vertexShaderCode, const char *fragmentShaderCode) {
    GLint program = glCreateProgram();
    if (0 == program) {
        LOGI(1, "create program error");
        return 0;
    }
    LOGI(1, "create program success");
    int vertexShaderID = compileShader(GL_VERTEX_SHADER, vertexShaderCode);
    int fragmentShaderID = compileShader(GL_FRAGMENT_SHADER, fragmentShaderCode);

    glAttachShader(program, vertexShaderID);
    glAttachShader(program, fragmentShaderID);
    glLinkProgram(program);
    GLint linkStatus;
    glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
    if (0 == linkStatus) {
        glDeleteProgram(program);
        LOGI(1, "link program error");
        return 0;
    }
    return program;

}
