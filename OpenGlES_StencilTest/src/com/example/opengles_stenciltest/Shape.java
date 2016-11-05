package com.example.opengles_stenciltest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Shape {
	private FloatBuffer mRectangleBuffer;
	private int mRectangleProgram; 
	private int mPositionHandle;
	private int muMVPMatrixHandle;
	private FloatBuffer mtrangleBuffer;
	private int mtrangleProgram;

	public Shape() {
		initVetexData();
	}

	public void initVetexData() {
		float rectangleVertices[] = new float[] {
				(float) -0.5, (float) -0.5,
				(float) 0.5, (float) -0.5,
				(float) -0.5, (float) 0.5,
				(float) 0.5, (float) 0.5
				};

		float trangleVertices[] = new float[] {
				-0.7f, -0.3f,
				0.7f, -0.3f,
				0, 0.7f
				};
		
		ByteBuffer rvbb = ByteBuffer.allocateDirect(rectangleVertices.length * 4);
		rvbb.order(ByteOrder.nativeOrder());
		mRectangleBuffer = rvbb.asFloatBuffer();
		mRectangleBuffer.put(rectangleVertices);
		mRectangleBuffer.position(0);

		ByteBuffer lvbb = ByteBuffer.allocateDirect(rectangleVertices.length * 4);
		lvbb.order(ByteOrder.nativeOrder());
		mtrangleBuffer = lvbb.asFloatBuffer();
		mtrangleBuffer.put(trangleVertices);
		mtrangleBuffer.position(0);
		
		int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER,
				rectangleFragmentShaderCode);
		int trangleFragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER,
				trangleFragmentShaderCode);

		mRectangleProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mRectangleProgram, vertexShader);
		GLES20.glAttachShader(mRectangleProgram, fragmentShader);
		GLES20.glLinkProgram(mRectangleProgram);
		
		mtrangleProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mtrangleProgram, vertexShader);
		GLES20.glAttachShader(mtrangleProgram, trangleFragmentShader);
		GLES20.glLinkProgram(mtrangleProgram);

		mPositionHandle = GLES20.glGetAttribLocation(mRectangleProgram, "aPosition");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mRectangleProgram, "uMVPMatrix");
	}

	public void drawRanctangle(float[] mvpMatrix) {
		GLES20.glUseProgram(mRectangleProgram);
		// 将顶点数据传递到管线，顶点着色器
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT,
				false, 2 * 4, mRectangleBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
		// 绘制图元
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void drawTrangle(float[] mvpMatrix) {
		GLES20.glUseProgram(mtrangleProgram);
		// 将顶点数据传递到管线，顶点着色器
		GLES20.glVertexAttribPointer(mPositionHandle, 2, GLES20.GL_FLOAT,
				false, 2 * 4, mtrangleBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
		// 绘制图元
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
	}
	
	private int loaderShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec2 aPosition;"
			+ "void main(){"
			+ "gl_Position = uMVPMatrix * vec4(aPosition, 0, 1);" 
			+ "}";

	private String rectangleFragmentShaderCode = "precision mediump float;"
			+ "void main(){"
			+ "gl_FragColor = vec4(1,0,0,0);"
			+ "}";
	
	private String trangleFragmentShaderCode = "precision mediump float;"
			+ "void main(){"
			+ "gl_FragColor = vec4(0,1,0,0);"
			+ "}";

}
