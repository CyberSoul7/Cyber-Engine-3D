package com.cybersgames.engine3.engine;

import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShaderProgram {
	
	private final int programId;
	
	private int vertexShaderId;
	
	private int fragmentShaderId;
	
	public ShaderProgram() {
		programId = glCreateProgram();
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
	}
	
	public void createFragmentshader(String shaderCode) throws Exception {
		fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}
	
	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);
		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(vertexShaderId, 1024));
		}
		
		glAttachShader(programId, shaderId);
		
		return shaderId;
	}
	
	public void link() throws Exception {
		glLinkProgram(programId);
		if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
		}
		
		if (vertexShaderId != 0) {
			glDetachShader(programId, vertexShaderId);
		}
		
		if (fragmentShaderId != 0) {
			glDetachShader(programId, fragmentShaderId);
		}
		
		//TODO: Remove validation in final builds
		glValidateProgram(programId);
		if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
		}
		
	}
	
	public void setBoolean(String name, boolean value) {
		glUniform1i(glGetUniformLocation(programId, name), value ? 1 : 0);
	}
	
	public void setInt(String name, int value) {
		glUniform1i(glGetUniformLocation(programId, name), value);
	}
	
	public void setFloat(String name, float value) {
		glUniform1f(glGetUniformLocation(programId, name), value);
	}
	
	public void setMatrix4f(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(glGetUniformLocation(programId, name), false, fb);
		}
	}
	
	public void setVector3f(String name, Vector3f value) {
		glUniform3f(glGetUniformLocation(programId, name), value.x, value.y, value.z);
	}
	
	public void bind() {
		glUseProgram(programId);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public int getId() {
		return programId;
	}
	
}
