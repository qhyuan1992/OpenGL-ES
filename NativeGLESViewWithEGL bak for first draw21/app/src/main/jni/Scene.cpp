//--------------------------------------------------------------------------------------
// File: Scene.cpp
// Desc: 
//
// Author:      QUALCOMM, Advanced Content Group - Snapdragon SDK
//
//               Copyright (c) 2013 QUALCOMM Technologies, Inc. 
//                         All Rights Reserved. 
//                      QUALCOMM Proprietary/GTDR
//--------------------------------------------------------------------------------------
//#include <FrmPlatform.h>
#define GL_GLEXT_PROTOTYPES
#include <EGL/egl.h>


#include <GLES3/gl3.h>
#include <GLES2/gl2ext.h>
//#include <FrmApplication.h>
//#include <OpenGLES/FrmShader.h>
#include "Scene.h"
#include <stdio.h>
#include <string.h>
#include "GLUtil.h"

#if defined(__linux__)
#include <stdio.h>
#include <unistd.h>
#endif
#include <time.h>

double now_ms(void) {

    struct timespec res;
    clock_gettime(CLOCK_REALTIME, &res);
    return 1000.0 * res.tv_sec + (double) res.tv_nsec / 1e6;

}
//--------------------------------------------------------------------------------------
// Name: FrmCreateApplicationInstance()
// Desc: Global function to create the application instance
//--------------------------------------------------------------------------------------
//CFrmApplication* FrmCreateApplicationInstance()
//{
//    return new PBOSample( "RenderToTexture" );
//}

//--------------------------------------------------------------------------------------
// Name: PBOSample()
// Desc: Constructor
//--------------------------------------------------------------------------------------
PBOSample::PBOSample( const char* strName )
{
    g_strWindowTitle = "Pixel Buffer Objects";
    g_nWindowWidth   = 1184;
    g_nWindowHeight  = 624;
    g_fAspectRatio   = (float)g_nWindowWidth / (float)g_nWindowHeight;
    //g_strImageFileName = "Tutorials/OpenGLES/TreeBark.tga";
    g_nImages = 1;

    g_hTextureHandle = 0;
    g_hShaderProgram = 0;
    g_VertexLoc      = 0;
    g_TexcoordLoc    = 1;
    g_hPbos[0]	 = 0;
    g_hPbos[1]    =0;

    g_imageData		= 0;	// Unused right now, will be used if adding PBO on/off functionality for testing
   // IMAGE_WIDTH		= 3480;
  //  IMAGE_HEIGHT		= 2160;
  //  CHANNEL_COUNT		= 4;
  //  DATA_SIZE			= IMAGE_WIDTH * IMAGE_HEIGHT * CHANNEL_COUNT;
   // INTERNAL_FORMAT  	=  GL_RGBA8;//GL_RGBA8;
  //  DATA_TYPE			= GL_UNSIGNED_BYTE;
   // FORMAT				= GL_RGBA;

}

//--------------------------------------------------------------------------------------
// Name: InitShaders()
// Desc: Initialize the shaders
//--------------------------------------------------------------------------------------
bool PBOSample::InitShaders()
{
    
//--------------------------------------------------------------------------------------
// Name: g_strVSProgram / g_strFSProgram
// Desc: The vertex and fragment shader programs
//--------------------------------------------------------------------------------------
    g_strVSProgram = 
	"#version 300 es     									\n"
    "in        vec4 g_vVertex;								\n"
    "in        vec4 g_vTexcoord;							\n"
    "														\n"
    "out     vec4 g_vVSTexcoord;							\n"
    "														\n"
    "void main()											\n"
    "{														\n"
    "    gl_Position  = vec4( g_vVertex.x, g_vVertex.y,		\n"
    "                         g_vVertex.z, g_vVertex.w );	\n"
    "    g_vVSTexcoord = g_vTexcoord;						\n"
    "}														\n";

    g_strFSProgram = 
	"#version 300 es														\n"
    "#ifdef GL_FRAGMENT_PRECISION_HIGH										\n"
    "   precision highp float;												\n"
    "#else																	\n"
    "   precision mediump float;											\n"
    "#endif																	\n"
    "																		\n"
    "uniform   sampler2D	g_sImageTexture;	                        	\n"
    "in     vec4			g_vVSTexcoord;									\n"
    "out vec4 out_color;													\n"
    "void main()															\n"
    "{																		\n"
		// When the x component of the texcoord transitions past 0.5, the
		//	second texture will be rendered to the triangle in the window.
     "    out_color   = texture(g_sImageTexture, vec2(g_vVSTexcoord.xy));	\n"
           // "    out_color   = vec4(0.0, 1.0, 0.0, 1.0);                    \n"
       //    "gl_FragColor = texture2D(g_sImageTexture, vec2(g_vVSTexcoord.xy));\n"
    "}																		\n";
 g_strFBOVSProgram =
         "#version 300 es														\n"
            "in  vec4 g_vVertex;\n"
            "in vec4 g_vTexcoord;\n"
            "out     vec4 g_vVSTexcoord;\n"
         "void main()											\n"
         "{														\n"
         "    gl_Position  = vec4( g_vVertex.x, g_vVertex.y,		\n"
         "                         g_vVertex.z, g_vVertex.w );	\n"
         "    g_vVSTexcoord = g_vTexcoord;						\n"
         "}														\n";
            "}";

    g_strFBOFSProgram =
            "#version 300 es														\n"
            "uniform   sampler2D	g_sImageTexture;	                        	\n"
            "in     vec4			g_vVSTexcoord;									\n"
            "out vec4 out_color;                                                    \n"
            "void main(){                                     \n"
            "out_color = texture(g_sImageTexture, vec2(g_vVSTexcoord.xy));  \n"
            "}";

    return true;
}


//--------------------------------------------------------------------------------------
// Name: UpdatePixels()
// Desc: Manipulate the pixels at dst
//--------------------------------------------------------------------------------------
void PBOSample::UpdatePixels(unsigned char *dst, int size)
{
    static int color = 0;

    if(!dst)
        return;

   // int* ptr = (int*)dst;


    // copy 4 bytes at once
    /*
    for(int i = 0; i < IMAGE_HEIGHT/2; ++i)
    {
        for(int j = 0; j < IMAGE_WIDTH/2; ++j)
        {
            *ptr = color;
            ++ptr;
        }
        color += 257;   // add an arbitary number (no meaning)
    }
    ++color;            // scroll down
    */
  memcpy((int *)dst,(int *)g_imageData, DATA_SIZE);
}

bool PBOSample::InitializeBuffer(int * ptr)
{
    // Generate the texture that will be used for rendering
    glGenTextures( 1, &g_hTextureHandle );
   // g_hTextureHandle = g_hTextureHandle;
    glBindTexture( GL_TEXTURE_2D, g_hTextureHandle );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );

    // Use glTexImage2D to initialize the 2D Texture
    glTexImage2D( GL_TEXTURE_2D, 0, INTERNAL_FORMAT, IMAGE_WIDTH, IMAGE_HEIGHT,
                  0, FORMAT, DATA_TYPE, (const void *)ptr);

    glBindTexture( GL_TEXTURE_2D, 0 );

    // Create the Pixel Buffers
    glGenBuffers(2, g_hPbos);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, g_hPbos[0]);
    glBufferData(GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, g_hPbos[1]);
    glBufferData(GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);
}


//--------------------------------------------------------------------------------------
// Name: Initialize()
// Desc: 
//--------------------------------------------------------------------------------------
bool PBOSample::Initialize(int w, int h,int * ptr)
{
    IMAGE_WIDTH		= w;
    IMAGE_HEIGHT		= h;
    CHANNEL_COUNT		= 2;
    DATA_SIZE			= IMAGE_WIDTH * IMAGE_HEIGHT * CHANNEL_COUNT;
    INTERNAL_FORMAT  	=  GL_RGB;//GL_RGBA8;
    DATA_TYPE			= GL_UNSIGNED_SHORT_5_6_5;
    FORMAT				= GL_RGB;

    g_imageData = (unsigned char *)ptr;
    LOGI(TAG, " PBOSample bmp in = 0x%x  0x%x ", *g_imageData, *(g_imageData+1));

    InitShaders();

     // Generate the texture that will be used for rendering
	glGenTextures( 1, &textureForFBO );

	glBindTexture( GL_TEXTURE_2D, textureForFBO );
	glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
	glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

	// Use glTexImage2D to initialize the 2D Texture
	glTexImage2D( GL_TEXTURE_2D, 0, INTERNAL_FORMAT, IMAGE_WIDTH ,IMAGE_HEIGHT ,
                  0, FORMAT, DATA_TYPE, (void *)g_imageData );

    // Generate the texture that will be used for rendering
    glGenTextures( 1, &g_hTextureHandle );

    glBindTexture( GL_TEXTURE_2D, g_hTextureHandle );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
    glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
    //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
   // glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

    // Use glTexImage2D to initialize the 2D Texture
    glTexImage2D( GL_TEXTURE_2D, 0, INTERNAL_FORMAT, IMAGE_WIDTH ,IMAGE_HEIGHT ,
                  0, FORMAT, DATA_TYPE, 0 );
    frameBufferName = 0;
    glGenFramebuffers(1, &frameBufferName);

	
	// Create the Pixel Buffers
    glGenBuffers(2, g_hPbos);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, g_hPbos[0]);
    glBufferData(GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, g_hPbos[1]);
    glBufferData(GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW);
    glBindBuffer(GL_PIXEL_UNPACK_BUFFER, 0);

    // Create the shader program needed to render the texture
    {
        // Compile the shaders
        GLuint hVertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( hVertexShader, 1, &g_strVSProgram, NULL );
        glCompileShader( hVertexShader );

        GLuint hFragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( hFragmentShader, 1, &g_strFSProgram, NULL );
        glCompileShader( hFragmentShader );

        // Check for compile success
        GLint nCompileResult = 0;
	    glGetShaderiv(hFragmentShader, GL_COMPILE_STATUS, &nCompileResult);
	    if (!nCompileResult)
	    {
		    char Log[1024];
		    GLint nLength;
		    glGetShaderInfoLog(hFragmentShader, 1024, &nLength, Log);
		    return false;
	    }

        // Attach the individual shaders to the common shader program
        g_hShaderProgram = glCreateProgram();
        glAttachShader( g_hShaderProgram, hVertexShader );
        glAttachShader( g_hShaderProgram, hFragmentShader );

        // Init attributes BEFORE linking
        glBindAttribLocation(g_hShaderProgram, g_VertexLoc,   "g_vVertex");
        glBindAttribLocation(g_hShaderProgram, g_TexcoordLoc, "g_vTexcoord");
        //glBindAttribLocation(g_hShaderProgram,  g_hTextureHandle, "g_sImageTexture");
        //glBindFragDataLocation(g_hShaderProgram, 0, "out_Color");
        // Link the vertex shader and fragment shader together
        glLinkProgram( g_hShaderProgram );

        // Check for link success
        GLint nLinkResult = 0;
	    glGetProgramiv(g_hShaderProgram, GL_LINK_STATUS, &nLinkResult);
	    if (!nLinkResult)
	    {
		    char Log[1024];
		    GLint nLength;
		    glGetProgramInfoLog(g_hShaderProgram, 1024, &nLength, Log);
		    return false;
	    }

        glDeleteShader( hVertexShader );
        glDeleteShader( hFragmentShader );
    }

    if(0){
        // Compile the FBOshaders
        GLuint hVertexShader = glCreateShader( GL_VERTEX_SHADER );
        glShaderSource( hVertexShader, 1, &g_strFBOVSProgram, NULL );
        glCompileShader( hVertexShader );

        GLuint hFragmentShader = glCreateShader( GL_FRAGMENT_SHADER );
        glShaderSource( hFragmentShader, 1, &g_strFBOFSProgram, NULL );
        glCompileShader( hFragmentShader );

        // Check for compile success
        GLint nCompileResult = 0;
        glGetShaderiv(hFragmentShader, GL_COMPILE_STATUS, &nCompileResult);
        if (!nCompileResult)
        {
            char Log[1024];
            GLint nLength;
            glGetShaderInfoLog(hFragmentShader, 1024, &nLength, Log);
            return false;
        }

        // Attach the individual shaders to the common shader program
        g_hFBOShaderProgram = glCreateProgram();
        glAttachShader( g_hFBOShaderProgram, hVertexShader );
        glAttachShader( g_hFBOShaderProgram, hFragmentShader );

        // Init attributes BEFORE linking
        glBindAttribLocation(g_hFBOShaderProgram, g_VertexLoc,   "g_vVertex");
        glBindAttribLocation(g_hFBOShaderProgram, g_TexcoordLoc, "g_vTexcoord");
        //glBindAttribLocation(g_hShaderProgram, g_hTextureHandle, "g_sImageTexture");

        // Link the vertex shader and fragment shader together
        glLinkProgram( g_hFBOShaderProgram );

        // Check for link success
        GLint nLinkResult = 0;
        glGetProgramiv(g_hShaderProgram, GL_LINK_STATUS, &nLinkResult);
        if (!nLinkResult)
        {
            char Log[1024];
            GLint nLength;
            glGetProgramInfoLog(g_hShaderProgram, 1024, &nLength, Log);
            return false;
        }

        glDeleteShader( hVertexShader );
        glDeleteShader( hFragmentShader );
    }

    float fSize = 1.0f;
	float VertexPositionsb[] =
	{
		-fSize,  +fSize*g_fAspectRatio, 0.0f, 1.0f,
		-fSize, -fSize*g_fAspectRatio, 0.0f, 1.0f,
		+fSize, -fSize*g_fAspectRatio, 0.0f, 1.0f,

		-fSize,  +fSize*g_fAspectRatio, 0.0f, 1.0f,
		+fSize, +fSize*g_fAspectRatio, 0.0f, 1.0f,
		+fSize, -fSize*g_fAspectRatio, 0.0f, 1.0f,
	};
    float VertexPositions[]=
            {
                    -fSize,  -fSize, 0.0f, 1.0f,
                    -fSize, fSize, 0.0f, 1.0f,
                    +fSize, fSize, 0.0f, 1.0f,

                    +fSize, fSize,  0.0f, 1.0f,
                    -fSize,  -fSize,0.0f, 1.0f,
                    +fSize, -fSize, 0.0f, 1.0f,

            };
	float VertexTexcoord[] = {
            0.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 0.0f,

            1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f

	};

	// Bind the vertex attributes
	//glGenVertexArrays(1, &g_hVao);
	//glBindVertexArray(g_hVao);

	glGenBuffers(1, &g_hPositionVbo);
	glBindBuffer(GL_ARRAY_BUFFER, g_hPositionVbo);
    //GLfloat
	glBufferData(GL_ARRAY_BUFFER, sizeof(float)* 24, VertexPositions, GL_STATIC_DRAW);//float32
	glVertexAttribPointer( g_VertexLoc, 3, GL_FLOAT, false, 16, 0 );
	glEnableVertexAttribArray( g_VertexLoc );
	glBindBuffer(GL_ARRAY_BUFFER, 0);

	glGenBuffers(1, &g_hTexcoordVbo);
	glBindBuffer(GL_ARRAY_BUFFER, g_hTexcoordVbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(float) * 12, VertexTexcoord, GL_STATIC_DRAW);//float32
	glVertexAttribPointer( g_TexcoordLoc, 3, GL_FLOAT, false, 8, 0 );
	glEnableVertexAttribArray( g_TexcoordLoc );
	glBindBuffer(GL_ARRAY_BUFFER, 0);


    return true;
}


//--------------------------------------------------------------------------------------
// Name: UpdateTexture()
// Desc: Update the texture with the last updated PBO and manipulate the pixels
//		 in the other PBO.
//--------------------------------------------------------------------------------------
void PBOSample::UpdateTexture()
{

	static int index = 0;
	int nextIndex = 0;
	
	// switch which PBO is getting updated vs getting its data transferred to
	//	the texture
	index = (index+1)%2;
	nextIndex = (index+1)%2;
    nextIndex = 0;
    index = 0;

	// write into PBO with UpdatePixels -> bind the other PBO to update its pixelvalues
	glBindBuffer( GL_PIXEL_UNPACK_BUFFER, g_hPbos[nextIndex] );
	
	glBufferData( GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW );
	glMapBufferRange(GL_PIXEL_UNPACK_BUFFER, 0, DATA_SIZE, GL_MAP_WRITE_BIT );
	
	// Use glGetBufferPointerv to get a pointer to the mapped PBO data
	unsigned char *ptr = NULL;
	glGetBufferPointerv( GL_PIXEL_UNPACK_BUFFER, GL_BUFFER_MAP_POINTER, (GLvoid **)&ptr );
	
	if( ptr )
	{
		// update the PBO's pixel data
        UpdatePixels( ptr, DATA_SIZE );
		glUnmapBuffer( GL_PIXEL_UNPACK_BUFFER );
	}
    // write from PBO into texture -> bind both the texture and PBO
    glBindTexture( GL_TEXTURE_2D, g_hTextureHandle );
    glBindBuffer( GL_PIXEL_UNPACK_BUFFER, g_hPbos[index]);

    // copy the contents of the PBO into the texture
    glTexSubImage2D( GL_TEXTURE_2D, 0, 0, 0,
                     IMAGE_WIDTH, IMAGE_HEIGHT, FORMAT, DATA_TYPE, 0 );

    glBindBuffer( GL_PIXEL_UNPACK_BUFFER, 0 );
}

//--------------------------------------------------------------------------------------
// Name: Resize()
// Desc: 
//--------------------------------------------------------------------------------------
bool PBOSample::Resize()
{
    return true;
}


//--------------------------------------------------------------------------------------
// Name: Destroy()
// Desc: 
//--------------------------------------------------------------------------------------
void PBOSample::Destroy()
{
	glDeleteBuffers( 2, g_hPbos );
    glDeleteTextures( 1, &g_hTextureHandle );
    glDeleteProgram( g_hShaderProgram );
}


//--------------------------------------------------------------------------------------
// Name: Update()
// Desc: 
//--------------------------------------------------------------------------------------
void PBOSample::Update()
{
   return;
}

void PBOSample::RenderWithoutPBO()
{

}

//--------------------------------------------------------------------------------------
// Name: Render()
// Desc: 
//--------------------------------------------------------------------------------------
void PBOSample::Render()
{
    double start =now_ms();
	// Update the texture and also update the other PBO
	UpdateTexture();
    //UpdateTextureFBO();
    double up =now_ms();
    double delta = up - start;
    LOGI(""," updateTexture time = %lf", delta);
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
    // Clear the colorbuffer and depth-buffer
    glClearColor( 0.0f, 0.0f, 0.5f, 1.0f );
    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
   //int fbuTextureHandle =  glGetUniformLocation(g_hShaderProgram, "g_sImageTexture");
    // Set the shader program and the texture



    glUseProgram( g_hShaderProgram );
    glBindTexture( GL_TEXTURE_2D, g_hTextureHandle );
  // glUniform1i(fbuTextureHandle, 0);

    glBindBuffer( GL_ARRAY_BUFFER, g_VertexLoc);
    glVertexAttribPointer( g_VertexLoc, 3, GL_FLOAT, false, 16, 0 );
    glEnableVertexAttribArray( g_VertexLoc );

    glBindBuffer( GL_ARRAY_BUFFER, g_TexcoordLoc);
    glVertexAttribPointer( g_TexcoordLoc, 3, GL_FLOAT, false, 8, 0 );
    glEnableVertexAttribArray( g_TexcoordLoc );
   // glBindVertexArray(g_hVao);

    glActiveTexture(GL_TEXTURE0);

	glDrawArrays( GL_TRIANGLES, 0, 6);
    Read();
    double read =now_ms();
    delta = read - up;
    LOGI(""," Read time = %lf", delta);
    glBindVertexArray(0);
    glBindBuffer( GL_ARRAY_BUFFER, 0);
}

void PBOSample::Read(){

        static int index = 0;
        int nextIndex = 0;

        index = (index + 1) % 2;
        nextIndex = (index + 1) % 2;

        // set the target framebuffer to read
        glReadBuffer(GL_FRONT);

        // read pixels from framebuffer to PBO
        // glReadPixels() should return immediately.
        glBindBuffer(GL_PIXEL_PACK_BUFFER, g_hPbos[index]);
        glReadPixels(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, FORMAT, GL_UNSIGNED_BYTE, 0);

        // map the PBO to process its data by CPU
        glBindBuffer(GL_PIXEL_PACK_BUFFER, g_hPbos[nextIndex]);
        GLubyte* ptr = (GLubyte*)glMapBufferRange(GL_PIXEL_PACK_BUFFER,
                      0, DATA_SIZE, GL_MAP_WRITE_BIT );
        if(ptr)
        {
          //processPixels(ptr, ...);
          glUnmapBuffer(GL_PIXEL_PACK_BUFFER);
        }

        // back to conventional pixel operation
        glBindBuffer(GL_PIXEL_PACK_BUFFER, 0);
}
void PBOSample::UpdateTextureFBOandrender()
{
    double start =now_ms();
    GLuint hTextureHandle;
    glGenTextures( 1, &hTextureHandle );

    glBindTexture(GL_TEXTURE_2D, hTextureHandle);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    //glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

    glTexImage2D( GL_TEXTURE_2D, 0, INTERNAL_FORMAT, IMAGE_WIDTH ,IMAGE_HEIGHT ,
                  0, FORMAT, DATA_TYPE, 0 );

    glBindTexture( GL_TEXTURE_2D, 0 );

    glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName);

    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, hTextureHandle, 0);

    if( glCheckFramebufferStatus( GL_FRAMEBUFFER) !=  GL_FRAMEBUFFER_COMPLETE) {
        LOGI(TAG, "Framebuffer error");
    }

    int fbuTextureHandle =  glGetUniformLocation(g_hFBOShaderProgram, "g_sImageTexture");
    // Set the shader program and the texture
    glUseProgram( g_hFBOShaderProgram);
    glBindTexture( GL_TEXTURE_2D, textureForFBO );

    glBindVertexArray(g_hVao);
    glUniform1i(g_hShaderProgram, 0);

    glDrawArrays( GL_TRIANGLES, 0, 6 );

    glBindFramebuffer(GL_FRAMEBUFFER, 0);


    double up =now_ms();
    double delta = up - start;
    LOGI(""," updateTexture time = %lf", delta);

    // Clear the colorbuffer and depth-buffer
    glClearColor( 0.0f, 0.0f, 0.5f, 1.0f );
    glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );

    // Set the shader program and the texture
    glUseProgram( g_hShaderProgram );
    glBindTexture( GL_TEXTURE_2D, hTextureHandle );

    glBindVertexArray(g_hVao);

    glActiveTexture(GL_TEXTURE0);
    glDrawArrays( GL_TRIANGLES, 0, 6 );
    //Read();
    double read =now_ms();
    delta = read - up;
    LOGI(""," Read time = %lf", delta);
    glBindVertexArray(0);
}
void PBOSample::UpdateTextureFBO()
{
    static int index = 0;
    int nextIndex = 0;

    // switch which PBO is getting updated vs getting its data transferred to
    //	the texture
    index = (index+1)%2;
    nextIndex = (index+1)%2;

    glBindTexture(GL_TEXTURE_2D, g_hTextureHandle);
    glBindBuffer( GL_PIXEL_UNPACK_BUFFER, g_hPbos[index]);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,GL_NEAREST);
    glTexParameterf(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,GL_MIRRORED_REPEAT);
    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,GL_MIRRORED_REPEAT);

    glTexSubImage2D( GL_TEXTURE_2D, 0, 0, 0,
                     IMAGE_WIDTH, IMAGE_HEIGHT, FORMAT, DATA_TYPE, 0 );

    // write into PBO with UpdatePixels -> bind the other PBO to update its pixelvalues
    //tmp glBindBuffer( GL_PIXEL_UNPACK_BUFFER, g_hPbos[nextIndex] );

    //tmp glBufferData( GL_PIXEL_UNPACK_BUFFER, DATA_SIZE, 0, GL_STREAM_DRAW );
    glMapBufferRange(GL_PIXEL_UNPACK_BUFFER, 0, DATA_SIZE, GL_MAP_WRITE_BIT );

    glBindTexture( GL_TEXTURE_2D, 0 );

    glBindFramebuffer(GL_FRAMEBUFFER, frameBufferName);

    glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, g_hTextureHandle, 0);

    if( glCheckFramebufferStatus( GL_FRAMEBUFFER) !=  GL_FRAMEBUFFER_COMPLETE) {
        LOGI(TAG, "Framebuffer error");
    }
    // Use glGetBufferPointerv to get a pointer to the mapped PBO data
    unsigned char *ptr = NULL;
    glGetBufferPointerv( GL_PIXEL_UNPACK_BUFFER, GL_BUFFER_MAP_POINTER, (GLvoid **)&ptr );

    if( ptr )
    {
        // update the PBO's pixel data
        //UpdatePixels( ptr, DATA_SIZE );
        glUnmapBuffer( GL_PIXEL_UNPACK_BUFFER );
    }

    glBindBuffer( GL_PIXEL_UNPACK_BUFFER, 0 );
    int fbuTextureHandle =  glGetUniformLocation(g_hFBOShaderProgram, "g_sImageTexture");
    // Set the shader program and the texture
    glUseProgram( g_hFBOShaderProgram);
    glBindTexture( GL_TEXTURE_2D, textureForFBO );

    glBindVertexArray(g_hVao);
    glUniform1i(g_hShaderProgram, 0);

    glDrawArrays( GL_TRIANGLES, 0, 6 );

    glBindFramebuffer(GL_FRAMEBUFFER, 0);

}