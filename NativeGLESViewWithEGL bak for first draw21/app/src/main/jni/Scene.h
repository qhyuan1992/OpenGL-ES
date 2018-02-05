//--------------------------------------------------------------------------------------
// File: Scene.h
// Desc: 
//
// Author:      QUALCOMM, Advanced Content Group - Snapdragon SDK
//
//               Copyright (c) 2013 QUALCOMM Technologies, Inc. 
//                         All Rights Reserved. 
//                      QUALCOMM Proprietary/GTDR
//--------------------------------------------------------------------------------------
#ifndef SCENE_H
#define SCENE_H

struct FrameBufferObject
{
    int m_nWidth;
    int m_nHeight;

    int m_hFrameBuffer;
    int m_hRenderBuffer;
    int m_hTexture;
};

//--------------------------------------------------------------------------------------
// Name: class PBOSample
// Desc: The main application class for this sample
//--------------------------------------------------------------------------------------
class PBOSample 
{
    
public:
    virtual bool Initialize(int w, int h, int * ptr);
    virtual bool Resize();
    virtual void Destroy();
    virtual void Update();
    virtual void Render();
    virtual void RenderWithoutPBO();

	void Read();
    PBOSample( const char* strName );
	void UpdateTextureFBOandrender();
private:    

    bool InitShaders();
    void UpdatePixels(unsigned char *, int);
    void UpdateTexture();
	bool InitializeBuffer(int * ptr);
	void UpdateTextureFBO();

    char*  g_strWindowTitle;
    int g_nWindowWidth;
    int g_nWindowHeight;
    float  g_fAspectRatio;

    const char*  g_strImageFileName;
    int	    g_nImages;
    
    GLuint       g_hTextureHandle;
    GLuint       g_hShaderProgram;
	GLuint       g_hFBOShaderProgram;
    GLuint       g_VertexLoc     ;
    GLuint       g_TexcoordLoc   ;
	GLuint       textureForFBO ;
	GLuint       frameBufferName ;

    GLuint		 g_hPbos[2];
    unsigned char *	 g_imageData	;
    int	 IMAGE_WIDTH	;
    int	 IMAGE_HEIGHT	;
    int	 CHANNEL_COUNT	;
    int	 DATA_SIZE		;
    GLint  INTERNAL_FORMAT;
    GLenum DATA_TYPE		;
    GLenum FORMAT			;
    
    const char* g_strVSProgram;
    const char* g_strFSProgram;

	const char* g_strFBOVSProgram;
	const char* g_strFBOFSProgram;

	GLuint		 g_hPositionVbo;
	GLuint		 g_hTexcoordVbo;
	GLuint		 g_hVao;

    
};

#endif // SCENE_H
