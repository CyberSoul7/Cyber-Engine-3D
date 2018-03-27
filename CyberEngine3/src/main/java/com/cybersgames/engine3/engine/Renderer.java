package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Renderer {
	
	private int vbo;
	
	private int vao;
	
	private int ebo;
	
	private int vertexCount;
	
	private ShaderProgram mainShaderProgram;
	
	public Renderer() {
	}
	
	public void init() throws Exception {
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		float vertices[] = {
			0.5f, 0.5f, 0.0f, //top right
			0.5f, -0.5f, 0.0f, //bottom right
			-0.5f, -0.5f, 0.0f, //bottom left
			-0.5f, 0.5f, 0.0f //top left
		};
		int indices[] = {
			0, 1, 3, //triangle 1
			1, 2, 3 //triangle 2
		};
		
		FloatBuffer verticesBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			vertexCount = indices.length;
			
			vao = glGenVertexArrays();
			glBindVertexArray(vao);
			
			vbo = glGenBuffers();
			verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			ebo = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			//Unbind VAO and VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
		}
	}
	
	public void render() throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		mainShaderProgram.bind();
		
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		
		mainShaderProgram.unbind();
		
	}
	
}
