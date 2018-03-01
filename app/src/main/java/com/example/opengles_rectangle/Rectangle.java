package com.example.opengles_rectangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Rectangle {
	private FloatBuffer mVertexBuffer;
	private int mProgram;
	private int mPositionHandle;
	private int muMVPMatrixHandle;
	
	public Rectangle() {
		initVetexData();
	}
	
	public void initVetexData() {
		// 初始化顶点坐标
		float[] vertexArray = new float[] {
			(float) -0.5, (float) -0.5, 0,
			(float) 0.5, (float) -0.5, 0,
			(float) -0.5, (float) 0.5, 0,
			(float) 0.5, (float) 0.5, 0,
		};
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		mVertexBuffer = buffer.asFloatBuffer();
		mVertexBuffer.put(vertexArray);
		mVertexBuffer.position(0);
		
		int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);
		// 将顶点数据传递到管线，顶点着色器
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 3*4, mVertexBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
		// 绘制图元
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
	
	private int loaderShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec3 aPosition;"
			+ "void main(){"
			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
			+ "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "void main(){"
			+ "gl_FragColor = vec4(1,0,0,0);"
			+ "}";
	
}
