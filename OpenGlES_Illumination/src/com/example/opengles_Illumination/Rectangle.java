package com.example.opengles_Illumination;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Rectangle {
	private FloatBuffer mVertexBuffer;
	private int mProgram;
	private int mPositionHandle;
	private int muMVPMatrixHandle;
	private int mColorHandle;
	private int muMMatrixHandle;
	private int muLightLocationHandle;

	public Rectangle() {
		initVetexData();
	}

	public void initVetexData() {
		float vertices[] = new float[] {
				// 顶点             颜色
	        	//前面
	        	 0, 0, 1,  1,1,1,0,
	        	 1, 1, 1,  1,0,0,0,
	        	-1, 1, 1,  1,0,0,0,
	        	 0, 0, 1,  1,1,1,0,
	        	-1, 1, 1,  1,0,0,0,
	        	-1,-1, 1,  1,0,0,0,
	        	 0, 0, 1,  1,1,1,0,
	        	-1,-1, 1,  1,0,0,0,
	        	 1,-1, 1,  1,0,0,0,
	        	 0, 0, 1,  1,1,1,0,
	        	 1,-1, 1,  1,0,0,0,
	        	 1, 1, 1,  1,0,0,0,
	        	 //后面
	        	 0, 0,-1,  1,1,1,0,
	        	 1, 1,-1,  1,0,0,0,
	        	 1,-1,-1,  1,0,0,0,
	        	 0, 0,-1,  1,1,1,0,
	        	 1,-1,-1,  1,0,0,0,
	        	-1,-1,-1,  1,0,0,0,
	        	 0, 0,-1,  1,1,1,0,
	        	-1,-1,-1,  1,0,0,0,
	        	-1, 1,-1,  1,0,0,0,
	        	 0, 0,-1,  1,1,1,0,
	        	-1, 1,-1,  1,0,0,0,
	        	 1, 1,-1,  1,0,0,0,
	        	//左面
	        	-1, 0, 0,  1,1,1,0,
	        	-1, 1, 1,  1,0,0,0,
	        	-1, 1,-1,  1,0,0,0,
	        	-1, 0, 0,  1,1,1,0,
	        	-1, 1,-1,  1,0,0,0,
	        	-1,-1,-1,  1,0,0,0,
	        	-1, 0, 0,  1,1,1,0,
	        	-1,-1,-1,  1,0,0,0,
	        	-1,-1, 1,  1,0,0,0,
	        	-1, 0, 0,  1,1,1,0,
	        	-1,-1, 1,  1,0,0,0,
	        	-1, 1, 1,  1,0,0,0,
	        	//右面
	        	 1, 0, 0,  1,1,1,0,
	        	 1, 1, 1,  1,0,0,0,
	        	 1,-1, 1,  1,0,0,0,
	        	 1, 0, 0,  1,1,1,0,
	        	 1,-1, 1,  1,0,0,0,
	        	 1,-1,-1,  1,0,0,0,
	        	 1, 0, 0,  1,1,1,0,
	        	 1,-1,-1,  1,0,0,0,
	        	 1, 1,-1,  1,0,0,0,
	        	 1, 0, 0,  1,1,1,0,
	        	 1, 1,-1,  1,0,0,0,
	        	 1, 1, 1,  1,0,0,0,
	        	 //上面
	        	 0, 1, 0,  1,1,1,0,
	        	 1, 1, 1,  1,0,0,0,
	        	 1, 1,-1,  1,0,0,0,
	        	 0, 1, 0,  1,1,1,0,  	
	        	 1, 1,-1,  1,0,0,0,
	        	-1, 1,-1,  1,0,0,0,
	        	 0, 1, 0,  1,1,1,0,
	        	-1, 1,-1,  1,0,0,0,
	        	-1, 1, 1,  1,0,0,0,
	        	 0, 1, 0,  1,1,1,0,
	        	-1, 1, 1,  1,0,0,0,
	        	 1, 1, 1,  1,0,0,0,
	        	//下面
	        	 0,-1, 0,  1,1,1,0,
	        	 1,-1, 1,  1,0,0,0,
	        	-1,-1, 1,  1,0,0,0,
	        	 0,-1, 0,  1,1,1,0,
	        	-1,-1, 1,  1,0,0,0,
	        	-1,-1,-1,  1,0,0,0,
	        	 0,-1, 0,  1,1,1,0,
	        	-1,-1,-1,  1,0,0,0,
	        	 1,-1,-1,  1,0,0,0,
	        	 0,-1, 0,  1,1,1,0,
	        	 1,-1,-1,  1,0,0,0,
	        	 1,-1, 1,  1,0,0,0,
	        };
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer = vbb.asFloatBuffer();
		mVertexBuffer.put(vertices);
		mVertexBuffer.position(0);

		int vertexShader = loaderShader(GLES20.GL_VERTEX_SHADER,
				vertexShaderCode);
		int fragmentShader = loaderShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(mProgram, vertexShader);
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);

		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
		muLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
	}

	public void draw(float[] mvpMatrix, float [] mMatrix) {
		GLES20.glUseProgram(mProgram);
		// 将顶点数据传递到管线，顶点着色器
		// 定位到位置0，读取顶点
		mVertexBuffer.position(0);
		// stride 跨距，读取下一个值跳过的字节数
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, (4+3) * 4, mVertexBuffer);
		// 将顶点颜色传递到管线，顶点着色器
		// 定位到位置3，读取颜色
		mVertexBuffer.position(3);
        GLES20.glVertexAttribPointer(mColorHandle, 4, GLES20.GL_FLOAT, false, (4+3) * 4, mVertexBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mColorHandle);
		
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
		GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, mMatrix, 0);
		GLES20.glUniform3f(muLightLocationHandle, 0, 0, 20);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12*6);
	}

	private int loaderShader(int type, String shaderCode) {
		int shader = GLES20.glCreateShader(type);
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);
		return shader;
	}
	
	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "uniform mat4 uMMatrix;" // 模型变换的矩阵
			+ "uniform vec3 uLightLocation;" // 光源位置
			+ "attribute vec4 aColor;"
			+ "varying  vec4 vColor;"  
			+ "varying vec4 vDiffuse;"  // 传递给片元着色器的散射光强度
			+ "attribute vec3 aPosition;" // 顶点位置
			+ "void main(){"  
			+ "vec3 normalVectorOrigin = aPosition;"  // 原始采用点法向量
			+ "vec3 normalVector = normalize((uMMatrix*vec4(normalVectorOrigin,1)).xyz);" // 归一化的变换后的法向量
			+ "vec3 vectorLight = normalize(uLightLocation - (uMMatrix * vec4(aPosition,1)).xyz);" // 挂一花的光源到点的向量
			+ "float factor = max(0.0, dot(normalVector, vectorLight));"
			+ "vDiffuse = factor*vec4(1,1,1,1.0);" // 散射光强度
			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
			+ "vColor = aColor;"
			+ "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "varying  vec4 vColor;"
			+ "varying vec4 vDiffuse;" // 从顶点着色器传过来的插值散射光的值，散射光的值依赖顶点。
			+ "void main(){"
			+ "gl_FragColor = vColor*vDiffuse;"
			+ "}";
	
	

//	private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
//			+ "uniform mat4 uMMatrix;" // 模型变换的矩阵
//			+ "uniform vec3 uLightLocation;" // 光源位置
//			+ "attribute vec4 aColor;"
//			+ "varying  vec4 vColor;"  
//			+ "varying vec4 vDiffuse;"  // 传递给片元着色器的散射光强度
//			+ "attribute vec3 aPosition;" // 顶点位置
//			+ "void main(){"  
//			+ "vec3 normalVectorOrigin = aPosition;"  // 原始采用点法向量
//			+ "vec3 normalVector = normalize((uMMatrix*vec4(normalVectorOrigin,1)).xyz);" // 归一化的变换后的法向量
//			+ "vec3 vectorLight = normalize(uLightLocation - (uMMatrix * vec4(aPosition,1)).xyz);" // 挂一花的光源到点的向量
//			+ "float factor = max(0.0, dot(normalVector, vectorLight));"
//			+ "vDiffuse = factor*vec4(1,1,1,1.0);" // 散射光强度
//			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
//			+ "vColor = aColor;"
//			+ "}";
//
//	private String fragmentShaderCode = "precision mediump float;"
//			+ "varying  vec4 vColor;"
//			+ "varying vec4 vDiffuse;" // 从顶点着色器传过来的插值散射光的值，散射光的值依赖顶点。
//			+ "void main(){"
//			+ "gl_FragColor = vColor*vDiffuse;"
//			+ "}";
	
	/*private String vertexShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 aColor;"
			+ "varying  vec4 aaColor;"
			+ "attribute vec3 aPosition;"
			+ "void main(){"
			+ "gl_Position = uMVPMatrix * vec4(aPosition,1);"
			+ "aaColor = aColor;"
			+ "}";

	private String fragmentShaderCode = "precision mediump float;"
			+ "varying  vec4 aaColor;"
			+ "void main(){"
			+ "gl_FragColor = aaColor;"
			+ "}";*/
}
