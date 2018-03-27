package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer {
	
	ShaderProgram mainShaderProgram;
	
	public Renderer() {
		mainShaderProgram = new ShaderProgram();
	}
	
	public void init() {
		
	}
	
	public void render() throws Exception {
		
		float vertices[] = {
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.0f, 0.5f, 0.0f
		};
		
		int vbo;
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		mainShaderProgram.createVertexShader(Utils.loadResource("vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("fragment.mainFragment.fs"));
		mainShaderProgram.link();
		
		mainShaderProgram.bind();
		
		
		mainShaderProgram.unbind();
		
	}
	
}
