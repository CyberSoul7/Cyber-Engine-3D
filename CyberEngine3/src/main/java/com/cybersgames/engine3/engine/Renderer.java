package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.List;

import static org.joml.Math.PI;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class Renderer {
	
	private List<GameObject> objects = new ArrayList<GameObject>();
	
	private ShaderProgram mainShaderProgram;
	
	public Renderer() {
	}
	
	public void init() throws Exception {
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/shaders/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		glEnable(GL_DEPTH_TEST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		float vertices[] = {
			0.5f, 0.5f, 0.5f, //top right front 0
			0.5f, 0.5f, -0.5f, //top right back 1
			0.5f, -0.5f, 0.5f, //bottom right front 2
			0.5f, -0.5f, -0.5f, //bottom right back 3
			-0.5f, -0.5f, 0.5f, //bottom left front 4
			-0.5f, -0.5f, -0.5f, //bottom left back 5
			-0.5f, 0.5f, 0.5f, //top left front 6
			-0.5f, 0.5f, -0.5f //top left back 7
		};
		float colors[] = {
			1.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			1.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f
		};
		float texCoords[] = {
			1.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			0.0f, 1.0f,
			0.0f, 0.0f,
			0.0f, 0.0f
		};
		int indices[] = {
			//Front Face
			0, 2, 4,
			6, 0, 4,
			//Left Face
			6, 4, 5,
			7, 6, 5,
			//Right Face
			0, 1, 2,
			1, 3, 2,
			//Back Face
			1, 7, 5,
			5, 3, 1,
			//Top Face
			0, 6, 1,
			6, 7, 1,
			//Bottom Face
			2, 3, 4,
			4, 5, 3
		};
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(0.0f, 0.0f, 0.0f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(2.0f, 5.0f, -15.0f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(-1.5f, -2.2f, -2.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(-3.8f, -2.0f, -12.3f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(2.4f, -0.4f, -3.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(-1.7f, 3.0f, -7.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(1.3f, -2.0f, -2.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(1.5f, 2.0f, -2.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(1.5f, 0.2f, -1.5f)));
		objects.add(new GameObject(new Mesh(vertices, indices, colors, texCoords, "/textures/container.png", "/textures/awesomeface.png"), new Vector3f(-1.3f, 1.0f, -1.5f)));
		
	}
	
	public void render(Window window) throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		mainShaderProgram.bind();
		
		mainShaderProgram.setInt("texture1", 0);
		mainShaderProgram.setInt("texture2", 1);
		
		Matrix4f modelMatrix = new Matrix4f().identity();
		modelMatrix.rotate((float) (glfwGetTime() * Math.toRadians(50.0f)), new Vector3f(0.5f, 1.0f, 0.0f));
		mainShaderProgram.setMatrix4f("model", modelMatrix);
		
		Matrix4f viewMatrix = new Matrix4f().identity();
		viewMatrix.translate(new Vector3f(0.0f, 0.0f, -5.0f));
		mainShaderProgram.setMatrix4f("view", viewMatrix);
		
		Matrix4f projectionMatrix = new Matrix4f().identity();
		projectionMatrix.perspective((float) Math.toRadians(45.0f), window.getAspectRatio() * 1.0f, 0.1f, 100.0f);
		mainShaderProgram.setMatrix4f("projection", projectionMatrix);
		
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			
			modelMatrix.identity();
			modelMatrix.translate(object.getPosition());
			float angle = 20.0f * i;
			modelMatrix.rotate((float) Math.toRadians(angle), new Vector3f(1.0f, 0.3f, 0.5f));
			mainShaderProgram.setMatrix4f("model", modelMatrix);
			
			object.render();
		}
		
		mainShaderProgram.unbind();
		
	}
	
	public void cleanUp() {
		for (GameObject object : objects) {
			object.cleanUp();
		}
	}
	
}
