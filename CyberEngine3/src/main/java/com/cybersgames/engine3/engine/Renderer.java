package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import static org.joml.Math.PI;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
	
	private Mesh mesh1;
	private Mesh mesh2;
	
	private ShaderProgram mainShaderProgram;
	
	public Renderer() {
	}
	
	public void init() throws Exception {
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/shaders/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		float vertices[] = {
			0.5f, 0.5f, 0.0f, //top right
			0.5f, -0.5f, 0.0f, //bottom right
			-0.5f, -0.5f, 0.0f, //bottom left
			-0.5f, 0.5f, 0.0f //top left
		};
		float colors[] = {
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f
		};
		float texCoords[] = {
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			0.0f, 0.0f
		};
		int indices[] = {
			0, 1, 3,
			1, 2, 3
		};
		mesh1 = new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png");
		mesh2 = new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png");
		
	}
	
	public void render() throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		mainShaderProgram.bind();
		
		Matrix4f trans = new Matrix4f().identity();
		trans.translate(new Vector3f(0.5f, -0.5f, 0.0f));
		trans.rotate((float) glfwGetTime(), new Vector3f(0.0f, 0.0f, 1.0f));
		mainShaderProgram.setMatrix4f("transform", trans);
		
		mesh1.render(mainShaderProgram);
		
		trans.identity();
		trans.translate(new Vector3f(-0.5f, 0.5f, 0.0f));
		trans.scale((float) Math.sin(glfwGetTime()));
		//trans.rotate((float) glfwGetTime(), new Vector3f(0.0f, 0.0f, 1.0f));
		mainShaderProgram.setMatrix4f("transform", trans);
		
		mesh2.render(mainShaderProgram);
		
		mainShaderProgram.unbind();
		
	}
	
	public void cleanUp() {
		mesh1.cleanUp();
	}
	
}
