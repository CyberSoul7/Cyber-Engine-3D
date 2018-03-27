package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

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
