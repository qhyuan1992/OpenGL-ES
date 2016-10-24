package com.example.opengles_circle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class SixShape {
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mColorBuffer;
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int muMVPMatrixHandle;
	
	public SixShape(float r) {
		initVetexData(r);
	}
	
	public void initVetexData(float r) {
		// 初始化顶点坐标
		float[] vertexArray = new float[8*3];
		// 初始化顶点颜色
		float[] colorArray=new float[8*4];
		int j = 0, k = 0;
		vertexArray[j++] = 0;
		vertexArray[j++] = 0;
		vertexArray[j++] = 0;
		
		colorArray[k++] = 1;
		colorArray[k++] = 1;
		colorArray[k++] = 1;
		colorArray[k++] = 0;
		for (int angle = 0; angle <= 360; angle += 60) {
			vertexArray[j++] = (float) (r*Math.cos(Math.toRadians(angle)));
			vertexArray[j++] = (float) (r*Math.sin(Math.toRadians(angle)));
			vertexArray[j++] = 0;
			
			colorArray[k++] = 1;
			colorArray[k++] = 0;
			colorArray[k++] = 0;
			colorArray[k++] = 0;
		}
		ByteBuffer buffer = ByteBuffer.allocateDirect(vertexArray.length * 4);
		buffer.order(ByteOrder.nativeOrder());
		mVertexBuffer = buffer.asFloatBuffer();
		mVertexBuffer.put(vertexArray);
		mVertexBuffer.position(0);
		
		ByteBuffer cbb=ByteBuffer.allocateDirect(colorArray.length*4);
		cbb.order(ByteOrder.nativeOrder());
		mColorBuffer=cbb.asFloatBuffer();
		mColorBuffer.put(colorArray);
		mColorBuffer.position(0);
		
		
		int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		
		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);
		
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(mProgram);
		// 将顶点数据传递到管线，顶点着色器
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, mVertexBuffer);
		// 将顶点颜色传递到管线，顶点着色器
		GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false,0, mColorBuffer);
		// 将变换矩阵传递到管线
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		// 绘制图元
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 8);
	}
	
	private int loaderShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}

	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec3 aPosition;"
			+ "attribute vec4 aColor;"
			+ "varying  vec4 aaColor;"
			+ "void main(){"
			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
			+ "aaColor = aColor;"
			+ "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "varying  vec4 aaColor;"
			+ "void main(){"
			+ "gl_FragColor = aaColor;"
			+ "}";
	
}
