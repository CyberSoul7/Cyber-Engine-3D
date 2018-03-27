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
	
	private Mesh mesh;
	
	private ShaderProgram mainShaderProgram;
	
	public Renderer() {
	}
	
	public void init() throws Exception {
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		float vertices[] = {
			0.5f, -0.5f, 0.0f, //bottom right
			-0.5f, -0.5f, 0.0f, //bottom left
			0.0f, 0.5f, 0.0f //top
		};
		float colors[] = {
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f
		};
		int indices[] = {
			0, 1, 2 //triangle 
		};
		mesh = new Mesh(vertices, indices, colors);
		
	}
	
	public void render() throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		mainShaderProgram.bind();
		
		glBindVertexArray(mesh.getVao());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
		
		mainShaderProgram.unbind();
		
	}
	
	public void cleanUp() {
		mesh.cleanUp();
	}
	
}
