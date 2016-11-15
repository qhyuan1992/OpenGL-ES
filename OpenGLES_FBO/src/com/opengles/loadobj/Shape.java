package com.opengles.loadobj;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.obj2openjl.v3.Obj2OpenJL;
import org.obj2openjl.v3.model.OpenGLModelData;
import org.obj2openjl.v3.model.RawOpenGLModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Shape {
	private static String TAG = "Shape";
	private float vertices[];
	private float texures[];
	private float normals[];
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTexureBuffer;
	private FloatBuffer mNormalBuffer;
	private int mVertexCount;
	
	private FloatBuffer mSqureBuffer;
	private int mFrameBufferProgram;
	private int mWindowProgram;
	private int mLoadedTextureId;
	private Context mContext;
	private static int COLOR_TEXTURE = 0;
	private static int DEPTH_TEXTURE = 1;
	
	public Shape(Context context) {
		this.mContext = context;
		initVetexData();
	}

	public void initVetexData() {
		float [] squareVertexs = new float[] {
				-1,-1,  0,1,
				-1,1,  0,0,
				1,-1,  1,1,
				1,1,  1,0
		};
		ByteBuffer vbb0 = ByteBuffer.allocateDirect(squareVertexs.length * 4);
		vbb0.order(ByteOrder.nativeOrder());
		mSqureBuffer = vbb0.asFloatBuffer();
		mSqureBuffer.put(squareVertexs);
		mSqureBuffer.position(0);
		InputStream is = null;
		try {
			is = mContext.getAssets().open("obj.obj");
		} catch (IOException e) {
			e.printStackTrace();
		}
		RawOpenGLModel openGLModel = new Obj2OpenJL().convert(is);
		OpenGLModelData openGLModelData = openGLModel.normalize().center().getDataForGLDrawArrays();
		vertices = openGLModelData.getVertices();
		mVertexCount = vertices.length / 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);
		texures = openGLModelData.getTextureCoordinates();
		ByteBuffer vbb2 = ByteBuffer.allocateDirect(texures.length * 4);
		vbb2.order(ByteOrder.nativeOrder());
		mTexureBuffer = vbb2.asFloatBuffer();
		mTexureBuffer.put(texures);
		mTexureBuffer.position(0);
		normals = openGLModelData.getNormals();
		ByteBuffer vbb3 = ByteBuffer.allocateDirect(normals.length * 4);
		vbb3.order(ByteOrder.nativeOrder());
		mNormalBuffer = vbb3.asFloatBuffer();
		mNormalBuffer.put(normals);
		mNormalBuffer.position(0);
		initTexture(R.drawable.texture);
	}
	
	public void initTexture(int res) {
		int [] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		mLoadedTextureId = textures[0];
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLoadedTextureId);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), res);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
	}

	public void draw(float[] mvpMatrix, float[] mMatrix) {
	    /*================================render2texture================================*/
	    // 生成FrameBuffer
	    int [] framebuffers = new int[1];
	    GLES20.glGenFramebuffers(1, framebuffers, 0);
	    int framebufferId = framebuffers[0];
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebufferId);
	    // 生成Texture
	    int [] textures = new int[2];
	    GLES20.glGenTextures(2, textures, 0);
	    int colorTxtureId = textures[COLOR_TEXTURE];
	    int depthTxtureId = textures[DEPTH_TEXTURE];
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, colorTxtureId);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, ShapeView.sScreenWidth, ShapeView.sScreenHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, null);
	    
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTxtureId);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, ShapeView.sScreenWidth, ShapeView.sScreenHeight, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_SHORT, null);
	    // 关联FrameBuffer和Texture
	    GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, colorTxtureId, 0);
	    GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, depthTxtureId, 0);
	    if(GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
	        Log.i(TAG, "Framebuffer error");
	    }
	    
	    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	    int frameBufferVertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	    int frameBufferFagmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
	    mFrameBufferProgram = GLES20.glCreateProgram();
	    GLES20.glAttachShader(mFrameBufferProgram, frameBufferVertexShader);
	    GLES20.glAttachShader(mFrameBufferProgram, frameBufferFagmentShader);
	    GLES20.glLinkProgram(mFrameBufferProgram);
	    int fbPositionHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aPosition");
	    int fbNormalHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aNormal");
	    int fbTextureCoordHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aTextureCoord");
	    int fbuMVPMatrixHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uMVPMatrix");
	    int fbuMMatrixHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uMMatrix");
	    int fbuLightLocationHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uLightLocation");
	    int fbuTextureHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uTexture");
	    GLES20.glUseProgram(mFrameBufferProgram);
	    mVertexBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
	    mTexureBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexureBuffer);
	    mTexureBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);
	    GLES20.glEnableVertexAttribArray(fbPositionHandle);
	    GLES20.glEnableVertexAttribArray(fbTextureCoordHandle);
	    GLES20.glEnableVertexAttribArray(fbNormalHandle);
	    GLES20.glUniform3f(fbuLightLocationHandle, 0, 10, 10);
	    GLES20.glUniformMatrix4fv(fbuMVPMatrixHandle, 1, false, mvpMatrix, 0);
	    GLES20.glUniformMatrix4fv(fbuMMatrixHandle, 1, false, mMatrix, 0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLoadedTextureId);
	    GLES20.glUniform1i(fbuTextureHandle, 0);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);

	    /*================================render2window================================*/
	    // 切换到窗口系统的缓冲区
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	    int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, windowVertexShaderCode);
	    int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, windowFragmentShaderCode);
	    mWindowProgram = GLES20.glCreateProgram();
	    GLES20.glAttachShader(mWindowProgram, vertexShader);
	    GLES20.glAttachShader(mWindowProgram, fragmentShader);
	    GLES20.glLinkProgram(mWindowProgram);
	    GLES20.glUseProgram(mWindowProgram);
	    int positionHandle = GLES20.glGetAttribLocation(mWindowProgram, "aPosition");
	    int textureCoordHandle = GLES20.glGetAttribLocation(mWindowProgram, "aTextureCoord");
	    int textureHandle = GLES20.glGetUniformLocation(mWindowProgram, "uTexture");
	    mSqureBuffer.position(0);
	    GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
	    mSqureBuffer.position(2);
	    GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
	    GLES20.glEnableVertexAttribArray(positionHandle);
	    GLES20.glEnableVertexAttribArray(textureCoordHandle);
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, depthTxtureId);
	    GLES20.glUniform1i(textureHandle, 0);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	    GLES20.glDeleteTextures(2, textures, 0);
	    GLES20.glDeleteFramebuffers(1, framebuffers, 0);
	}
	
	public void draw1(float[] mvpMatrix, float[] mMatrix) {
	    /*================================render2texture================================*/
	    // 生成FrameBuffer
	    int [] framebuffers = new int[1];
	    GLES20.glGenFramebuffers(1, framebuffers, 0);
	    int framebufferId = framebuffers[0];
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebufferId);
	    // 生成Texture
	    int [] textures = new int[1];
	    GLES20.glGenTextures(1, textures, 0);
	    int textureId = textures[0];
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_MIRRORED_REPEAT);
	    GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, ShapeView.sScreenWidth, ShapeView.sScreenHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_SHORT_5_6_5, null);
	    // 生成Renderbuffer
	    int [] renderbuffers = new int[1];
	    GLES20.glGenRenderbuffers(1, renderbuffers, 0);
	    int renderId = renderbuffers[0];
	    GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderId);
	    GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, ShapeView.sScreenWidth, ShapeView.sScreenHeight);  
	    // 关联FrameBuffer和Texture、RenderBuffer
	    GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureId, 0);
	    GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderId);
	    if(GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
	        Log.i(TAG, "Framebuffer error");
	    }
	    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	    int frameBufferVertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
	    int frameBufferFagmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
	    mFrameBufferProgram = GLES20.glCreateProgram();
	    GLES20.glAttachShader(mFrameBufferProgram, frameBufferVertexShader);
	    GLES20.glAttachShader(mFrameBufferProgram, frameBufferFagmentShader);
	    GLES20.glLinkProgram(mFrameBufferProgram);
	    int fbPositionHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aPosition");
	    int fbNormalHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aNormal");
	    int fbTextureCoordHandle = GLES20.glGetAttribLocation(mFrameBufferProgram, "aTextureCoord");
	    int fbuMVPMatrixHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uMVPMatrix");
	    int fbuMMatrixHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uMMatrix");
	    int fbuLightLocationHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uLightLocation");
	    int fbuTextureHandle = GLES20.glGetUniformLocation(mFrameBufferProgram, "uTexture");
	    GLES20.glUseProgram(mFrameBufferProgram);
	    mVertexBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
	    mTexureBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexureBuffer);
	    mTexureBuffer.position(0);
	    GLES20.glVertexAttribPointer(fbNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);
	    GLES20.glEnableVertexAttribArray(fbPositionHandle);
	    GLES20.glEnableVertexAttribArray(fbTextureCoordHandle);
	    GLES20.glEnableVertexAttribArray(fbNormalHandle);
	    GLES20.glUniform3f(fbuLightLocationHandle, 0, 10, 10);
	    GLES20.glUniformMatrix4fv(fbuMVPMatrixHandle, 1, false, mvpMatrix, 0);
	    GLES20.glUniformMatrix4fv(fbuMMatrixHandle, 1, false, mMatrix, 0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mLoadedTextureId);
	    GLES20.glUniform1i(fbuTextureHandle, 0);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);

	    /*================================render2window================================*/
	    // 切换到窗口系统的缓冲区
	    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
	    GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
	    int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, windowVertexShaderCode);
	    int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, windowFragmentShaderCode);
	    mWindowProgram = GLES20.glCreateProgram();
	    GLES20.glAttachShader(mWindowProgram, vertexShader);
	    GLES20.glAttachShader(mWindowProgram, fragmentShader);
	    GLES20.glLinkProgram(mWindowProgram);
	    GLES20.glUseProgram(mWindowProgram);
	    int positionHandle = GLES20.glGetAttribLocation(mWindowProgram, "aPosition");
	    int textureCoordHandle = GLES20.glGetAttribLocation(mWindowProgram, "aTextureCoord");
	    int textureHandle = GLES20.glGetUniformLocation(mWindowProgram, "uTexture");
	    mSqureBuffer.position(0);
	    GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
	    mSqureBuffer.position(2);
	    GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, (2+2) * 4, mSqureBuffer);
	    GLES20.glEnableVertexAttribArray(positionHandle);
	    GLES20.glEnableVertexAttribArray(textureCoordHandle);
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	    GLES20.glUniform1i(textureHandle, 0);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	    GLES20.glDeleteTextures(1, textures, 0);
	    GLES20.glDeleteFramebuffers(1, framebuffers, 0);
	    GLES20.glDeleteRenderbuffers(1, renderbuffers, 0);
	}
	
	private int loaderShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
	
	private String windowVertexShaderCode = ""
			+ "attribute vec2 aPosition;"
			+ "attribute vec2 aTextureCoord;"
			+ "varying vec2 vTextureCoord;"
			+ "void main(){"
			+ "gl_Position = vec4(aPosition,0,1);"
			+ "vTextureCoord = aTextureCoord;"
			+ "}";

	private String windowFragmentShaderCode = "precision mediump float;"
			+ "uniform sampler2D uTexture;"
			+ "varying vec2 vTextureCoord;"
			+ "void main(){"
			+ "gl_FragColor = texture2D(uTexture, vTextureCoord);"
			+ "}";
//	vec4(1,1,1,1)- texture2D(uTexture, vTextureCoord)
	
	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec2 aTextureCoord;"
			+ "varying vec2 vTextureCoord;"
			+ "uniform mat4 uMMatrix;"
			+ "uniform vec3 uLightLocation;"
			+ "varying vec4 vDiffuse;"
			+ "attribute vec3 aPosition;"
			+ "attribute vec3 aNormal;"
			+ "void main(){"  
			+ "vec3 normalVectorOrigin = aNormal;"
			+ "vec3 normalVector = normalize((uMMatrix*vec4(normalVectorOrigin,1)).xyz);"
			+ "vec3 vectorLight = normalize(uLightLocation - (uMMatrix * vec4(aPosition,1)).xyz);"
			+ "float factor = max(0.0, dot(normalVector, vectorLight));"
			+ "vDiffuse = factor*vec4(1,1,1,1.0);"
			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
			+ "vTextureCoord = aTextureCoord;"
			+ "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "uniform sampler2D uTexture;"
			+ "varying vec2 vTextureCoord;"
			+ "varying  vec4 vColor;"
			+ "varying vec4 vDiffuse;"
			+ "void main(){"
			+ "gl_FragColor = (vDiffuse + vec4(0.6,0.6,0.6,1))*texture2D(uTexture, vec2(vTextureCoord.s,vTextureCoord.t));"
			+ "}";
}

