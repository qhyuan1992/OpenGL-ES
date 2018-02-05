//
// Created by vivisong on 05/02/2018.
// first opengl 2.0 version
//
#include <jni.h>
#include <android/log.h>
#include "GLUtil.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include "yuv420ptoRGB565.h"

static const char* VERTEX_SHADER =
        "attribute vec4 vPosition;    \n"
                "attribute vec2 a_texCoord;   \n"
                "varying vec2 tc;     \n"
                "void main()                  \n"
                "{                            \n"
                "   gl_Position = vPosition;  \n"
                "   tc = a_texCoord;  \n"
                "}                            \n";
//Shader.frag文件内容
static const char* FRAG_SHADER =
        "varying lowp vec2 tc;\n"
                "uniform sampler2D SamplerY;\n"
                "uniform sampler2D SamplerU;\n"
                "uniform sampler2D SamplerV;\n"
                "void main(void)\n"
                "{\n"
                "mediump vec3 yuv;\n"
                "lowp vec3 rgb;\n"
                "yuv.x = texture2D(SamplerY, tc).r;\n"
                "yuv.y = texture2D(SamplerU, tc).r - 0.5;\n"
                "yuv.z = texture2D(SamplerV, tc).r - 0.5;\n"
                "rgb = mat3( 1,   1,   1,\n"
                "0,       -0.39465,  2.03211,\n"
                "1.13983,   -0.58060,  0) * yuv;\n"
                "gl_FragColor = vec4(rgb, 1);\n"
                "}\n";

/*
static const char* FRAG_YUV420SPSHADER = //yuv420sp
        Shader "Tango/YUV2RGB"
{
Properties
{
_YTex ("Y channel texture", 2D) = "white" {}
_UTex ("U channel texture", 2D) = "white" {}
_VTex ("V channel texture", 2D) = "white" {}
_TexWidth ("texture width", Float) = 1280.0
_TexHeight ("texture height", Float) = 720.0
_Fx      ("Fx", Float) = 1043.130005
_Fy      ("Fy", Float) = 1038.619995
_Cx      ("Cx", Float) = 641.926025
_Cy      ("Cy", Float) = 359.115997
_K0      ("K0", Float) = 0.246231
_K1      ("K1", Float) = -0.727204
_K2      ("K2", Float) = 0.726065
}
SubShader
{
// Setting the z write off to make sure our video overlay is always rendered at back.
ZWrite Off
ZTest Off
Tags { "Queue" = "Background" }
Pass
{
CGPROGRAM
#pragma multi_compile _ DISTORTION_ON

#pragma vertex vert
#pragma fragment frag

struct appdata
{
    float4 vertex : POSITION;
    float2 uv : TEXCOORD0;
};

struct v2f
{
    float4 vertex : SV_POSITION;
    float2 uv : TEXCOORD0;
};

v2f vert (appdata v)
{
    v2f o;
    // We don't apply any projection or view matrix here to make sure that
    // the geometry is rendered in the screen space.
    o.vertex = v.vertex;
    o.uv = v.uv;
    return o;
}

// The Y, U, V texture.
// However, at present U and V textures are interleaved into the same texture,
// so we'll only sample from _YTex and _UTex.
sampler2D _YTex;
sampler2D _UTex;

// Width of the RGBA texture, this is for indexing the channel of color, not
// for scaling.
float _TexWidth;
float _TexHeight;
float _Fx;
float _Fy;
float _Cx;
float _Cy;
float _K0;
float _K1;
float _K2;

// Compute a modulo b.
float custom_mod(float x, float y)
{
    return x - (y * floor(x / y));
}

fixed4 frag (v2f i) : SV_Target
{
float undistored_x = i.uv.x;
float undistored_y = i.uv.y;
float x = i.uv.x;
float y = i.uv.y;

#ifdef DISTORTION_ON
x = (x * _TexWidth - _Cx) / _Fx;
            y = (y * _TexHeight - _Cy) / _Fy;

            float r2 = x * x + y * y;
            float icdist = 1.0 + r2 * (_K0 + r2 * (_K1 + r2 * _K2));
            undistored_x = x * icdist;
            undistored_y = y * icdist;

            undistored_x = (undistored_x * _Fx + _Cx) / _TexWidth;
            undistored_y = (undistored_y * _Fy + _Cy) / _TexHeight;
#endif

// In this example, we are using HAL_PIXEL_FORMAT_YCrCb_420_SP format
// the data packing is: texture_y will pack 1280x720 pixels into
// a 320x720 RGBA8888 texture.
// texture_Cb and texture_Cr will contain copies of the 2x2 downsampled
// interleaved UV planes packed similarly.
float y_value, u_value, v_value;

float texel_x = undistored_x * _TexWidth;

// Compute packed-pixel offset for Y value.
float packed_offset = floor(custom_mod(texel_x, 4.0));

// Avoid floating point precision problems: Make sure we're sampling from the
// same pixel as we've computed packed_offset for.
undistored_x = (floor(texel_x) + 0.5) / _TexWidth;

float4 packed_y = tex2D(_YTex, float2(undistored_x, (1.0 - undistored_y)));
if (packed_offset == 0)
{
y_value = packed_y.r;
}
else if (packed_offset == 1)
{
y_value = packed_y.g;
}
else if (packed_offset == 2)
{
y_value = packed_y.b;
}
else
{
y_value = packed_y.a;
}

float4 packed_uv = tex2D(_UTex, float2(undistored_x, (1.0 - undistored_y)));

if (packed_offset == 0 || packed_offset == 1)
{
v_value = packed_uv.r;
u_value = packed_uv.g;
}
else
{
v_value = packed_uv.b;
u_value = packed_uv.a;
}

// The YUV to RBA conversion, please refer to: http://en.wikipedia.org/wiki/YUV
// Y'UV420sp (NV21) to RGB conversion (Android) section.
float r = y_value + 1.370705 * (v_value - 0.5);
float g = y_value - 0.698001 * (v_value - 0.5) - (0.337633 * (u_value - 0.5));
float b = y_value + 1.732446 * (u_value - 0.5);

return float4(r, g, b, 1.0);
}
ENDCG
}
}
}
*/
enum {
    ATTRIB_VERTEX,
    ATTRIB_TEXTURE,
};


 void yuv420ptoRGB565::checkGlError(const char* op)
{
    GLint error;
    for (error = glGetError(); error; error = glGetError())
    {
        LOGI(1, "error::after %s() glError (0x%x)\n", op, error);
    }
}

 GLuint yuv420ptoRGB565::bindTexture(GLuint texture, const char *buffer, GLuint w , GLuint h)
{
//	GLuint texture;
//	glGenTextures ( 1, &texture );
    checkGlError("glGenTextures");
    glBindTexture ( GL_TEXTURE_2D, texture );
    checkGlError("glBindTexture");
    glTexImage2D ( GL_TEXTURE_2D, 0, GL_LUMINANCE, w, h, 0, GL_LUMINANCE, GL_UNSIGNED_BYTE, buffer);
    checkGlError("glTexImage2D");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE );
    checkGlError("glTexParameteri");
    glTexParameteri ( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE );
    checkGlError("glTexParameteri");
    //glBindTexture(GL_TEXTURE_2D, 0);

    return texture;
}

 void yuv420ptoRGB565::renderFrame()
{
#if 0
    // Galaxy Nexus 4.2.2
    static GLfloat squareVertices[] = {
        -1.0f, -1.0f,
        1.0f, -1.0f,
        -1.0f,  1.0f,
        1.0f,  1.0f,
    };

    static GLfloat coordVertices[] = {
        0.0f, 1.0f,
        1.0f, 1.0f,
        0.0f,  0.0f,
        1.0f,  0.0f,
    };
#else
    // HUAWEIG510-0010 4.1.1
    static GLfloat squareVertices[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,

            0.0f,  0.0f,
            1.0f,  1.0f,
            0.0f, 1.0f
    };

    static GLfloat coordVertices[] = {
            -1.0f, 1.0f, 0.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,
            1.0f,-1.0f, 0.0f, 1.0f,

            -1.0f,  1.0f, 0.0f, 1.0f,
            1.0f,  -1.0f, 0.0f, 1.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,
    };
     static GLfloat coordVertices2[] = {
             -1.0f, 1.0f,
             1.0f, 1.0f,
             -1.0f,  -1.0f,
             1.0f,  -1.0f,
     };
#endif

   // glClearColor(0.5f, 0.5f, 0.5f, 1);
    checkGlError("glClearColor");
    glClear(GL_COLOR_BUFFER_BIT);
    checkGlError("glClear");
    //PRINTF("setsampler %d %d %d", g_texYId, g_texUId, g_texVId);
    GLint tex_y = glGetUniformLocation(simpleProgram, "SamplerY");
    checkGlError("glGetUniformLocation");
    GLint tex_u = glGetUniformLocation(simpleProgram, "SamplerU");
    checkGlError("glGetUniformLocation");
    GLint tex_v = glGetUniformLocation(simpleProgram, "SamplerV");
    checkGlError("glGetUniformLocation");


    glBindAttribLocation(simpleProgram, ATTRIB_VERTEX, "vPosition");
    checkGlError("glBindAttribLocation");
    glBindAttribLocation(simpleProgram, ATTRIB_TEXTURE, "a_texCoord");
    checkGlError("glBindAttribLocation");

    glVertexAttribPointer(ATTRIB_VERTEX, 4, GL_FLOAT, 0, 0,  coordVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(ATTRIB_VERTEX);
    checkGlError("glEnableVertexAttribArray");

    glVertexAttribPointer(ATTRIB_TEXTURE, 2, GL_FLOAT, 0, 0, squareVertices);
    checkGlError("glVertexAttribPointer");
    glEnableVertexAttribArray(ATTRIB_TEXTURE);
    checkGlError("glEnableVertexAttribArray");

    glActiveTexture(GL_TEXTURE0);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texYId);
    checkGlError("glBindTexture");
    glUniform1i(tex_y, 0);
    checkGlError("glUniform1i");

    glActiveTexture(GL_TEXTURE1);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texUId);
    checkGlError("glBindTexture");
    glUniform1i(tex_u, 1);
    checkGlError("glUniform1i");

    glActiveTexture(GL_TEXTURE2);
    checkGlError("glActiveTexture");
    glBindTexture(GL_TEXTURE_2D, g_texVId);
    checkGlError("glBindTexture");
    glUniform1i(tex_v, 2);
    checkGlError("glUniform1i");

    //glEnable(GL_TEXTURE_2D);
    //checkGlError("glEnable");
    //glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
     glDrawArrays(GL_TRIANGLES, 0, 6);
    checkGlError("glDrawArrays");
}

 GLuint yuv420ptoRGB565::buildShader(const char* source, GLenum shaderType)
{
    GLuint shaderHandle = glCreateShader(shaderType);

    if (shaderHandle)
    {
        glShaderSource(shaderHandle, 1, &source, 0);
        glCompileShader(shaderHandle);

        GLint compiled = 0;
        glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, &compiled);
        if (!compiled)
        {
            GLint infoLen = 0;
            glGetShaderiv(shaderHandle, GL_INFO_LOG_LENGTH, &infoLen);
            if (infoLen)
            {
                char* buf = (char*) malloc(infoLen);
                if (buf)
                {
                    glGetShaderInfoLog(shaderHandle, infoLen, NULL, buf);
                    LOGI(1, "error::Could not compile shader %d:\n%s\n", shaderType, buf);
                    free(buf);
                }
                glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

    }

    return shaderHandle;
}

 GLuint yuv420ptoRGB565::buildProgram(const char* vertexShaderSource,
                           const char* fragmentShaderSource)
{
    GLuint vertexShader = buildShader(vertexShaderSource, GL_VERTEX_SHADER);
    GLuint fragmentShader = buildShader(fragmentShaderSource, GL_FRAGMENT_SHADER);
    GLuint programHandle = glCreateProgram();

    if (programHandle)
    {
        glAttachShader(programHandle, vertexShader);
        checkGlError("glAttachShader");
        glAttachShader(programHandle, fragmentShader);
        checkGlError("glAttachShader");
        glLinkProgram(programHandle);

        GLint linkStatus = GL_FALSE;
        glGetProgramiv(programHandle, GL_LINK_STATUS, &linkStatus);
        if (linkStatus != GL_TRUE) {
            GLint bufLength = 0;
            glGetProgramiv(programHandle, GL_INFO_LOG_LENGTH, &bufLength);
            if (bufLength) {
                char* buf = (char*) malloc(bufLength);
                if (buf) {
                    glGetProgramInfoLog(programHandle, bufLength, NULL, buf);
                    LOGI(1, "error::Could not link program:\n%s\n", buf);
                    free(buf);
                }
            }
            glDeleteProgram(programHandle);
            programHandle = 0;
        }

    }

    return programHandle;
}

 unsigned char * yuv420ptoRGB565::readYUV(const char *path)
{

    FILE *fp;
    unsigned char * buffer;
    long size = g_width * g_height* 3 / 2;

    if((fp=fopen(path,"rb"))==NULL)
    {
        LOGI(1, "cant open the file");
        exit(0);
    }

    buffer = (unsigned char *)malloc(size);
    memset(buffer,'\0',size);
    long len = fread(buffer,1,size,fp);
    //PRINTF("read data size:%ld", len);
    fclose(fp);
    return buffer;
}

void yuv420ptoRGB565::gl_initialize()
{
    g_buffer = NULL;

    simpleProgram = buildProgram(VERTEX_SHADER, FRAG_SHADER);
    glUseProgram(simpleProgram);
    glGenTextures(1, &g_texYId);
    glGenTextures(1, &g_texUId);
    glGenTextures(1, &g_texVId);
    const char *path = "/sdcard/fourk.yuv";
    g_buffer = readYUV(path);
}

void yuv420ptoRGB565::gl_uninitialize()
{

    g_width = 0;
    g_height = 0;

    if (g_buffer)
    {
        free(g_buffer);
        g_buffer = NULL;
    }
}
//设置图像数据
void yuv420ptoRGB565::gl_set_framebuffer(const char* buffer, int buffersize, int width, int height)
{

    if (g_width != width || g_height != height)
    {
        if (g_buffer)
            free(g_buffer);

        g_width = width;
        g_height = height;

        g_buffer = (unsigned char *)malloc(buffersize);
    }

    if (g_buffer)
        memcpy(g_buffer, buffer, buffersize);

}
//画屏
void yuv420ptoRGB565::gl_render_frame()
{
    if (0 == g_width || 0 == g_height)
        return;

#if 0
    int width = 448;
    int height = 336;
	static unsigned char *buffer = NULL;

    if (NULL == buffer)
    {
        char filename[128] = {0};
        strcpy(filename, "/sdcard/yuv_448_336.yuv");
	    buffer = readYUV(filename);
    }

#else
    const char *buffer = (const char *)g_buffer;
    int width = g_width;
    int height = g_height;
#endif
  //  glViewport(0, 0, width, height);
    bindTexture(g_texYId, buffer, width, height);
    bindTexture(g_texUId, buffer + width * height, width/2, height/2);
    bindTexture(g_texVId, buffer + width * height * 5 / 4, width/2, height/2);
    renderFrame();
}
//如果设置图像数据和画屏是两个线程的话，记住要加锁。