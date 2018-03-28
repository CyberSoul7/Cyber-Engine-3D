package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
	
	private Mesh mesh;
	
	private ShaderProgram mainShaderProgram;
	
	public Renderer() {
	}
	
	public void init() throws Exception {
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/shaders/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_MIRRORED_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_MIRRORED_REPEAT);
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
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 1.0f
		};
		int indices[] = {
			0, 1, 3,
			1, 2, 3
		};
		mesh = new Mesh(vertices, indices, colors, texCoords, "/textures/container.png");
		
	}
	
	public void render() throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		mainShaderProgram.bind();
		mainShaderProgram.setInt("ourTexture", 0);
		
		mesh.render();
		
		mainShaderProgram.unbind();
		
	}
	
	public void cleanUp() {
		mesh.cleanUp();
	}
	
}
