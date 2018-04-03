package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.opengl.*;

public class Renderer {
	
	private List<GameObject> objects = new ArrayList<GameObject>();
	
	private ShaderProgram mainShaderProgram;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	
	public Renderer() {
	}
	
	public void init(Window window, Camera camera) throws Exception {
		
		projectionMatrix = new Matrix4f().identity();
		projectionMatrix = new Matrix4f().identity();
		projectionMatrix.perspective((float) Math.toRadians(45.0f), window.getAspectRatio(), 0.1f, 100.0f);
		
		viewMatrix = camera.getViewMatrix();
		
		mainShaderProgram = new ShaderProgram();
		mainShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex/mainVertex.vs"));
		mainShaderProgram.createFragmentshader(Utils.loadResource("/shaders/fragment/mainFragment.fs"));
		mainShaderProgram.link();
		
		glEnable(GL_DEPTH_TEST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
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
		
		mainShaderProgram.setMatrix4f("view", viewMatrix);
		
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
	
	public void setProjectionMatrix(Matrix4f projection) {
		projectionMatrix = new Matrix4f(projection);
	}
	
	public void setViewMatrix(Matrix4f view) {
		viewMatrix = new Matrix4f(view);
	}
	
	public void cleanUp() {
		for (GameObject object : objects) {
			object.cleanUp();
		}
	}
	
}
