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
	private GameObject light;
	
	private ShaderProgram mainShaderProgram;
	private ShaderProgram lightShaderProgram;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f viewPos;
	
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
		
		lightShaderProgram = new ShaderProgram();
		lightShaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex/lampVertex.vs"));
		lightShaderProgram.createFragmentshader(Utils.loadResource("/shaders/fragment/lampFragment.fs"));
		lightShaderProgram.link();
		
		glEnable(GL_DEPTH_TEST);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		objects.add(new GameObject(OBJLoader.loadMesh("/models/cube.obj"), new Vector3f(0.0f, 0.0f, 0.0f)));
		light = new GameObject(OBJLoader.loadMesh("/models/cube.obj"), new Vector3f(1.2f, 1.0f, 2.0f));
		
	}
	
	public void render(Window window) throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		lightShaderProgram.bind();
		
		Matrix4f modelMatrix = new Matrix4f().identity();
		modelMatrix.translate(light.getPosition());
		modelMatrix.scale(new Vector3f(0.2f, 0.2f, 0.2f));
		lightShaderProgram.setMatrix4f("model", modelMatrix);
		
		lightShaderProgram.setMatrix4f("view", viewMatrix);
		
		lightShaderProgram.setMatrix4f("projection", projectionMatrix);
		
		light.render();
		
		lightShaderProgram.unbind();
		
		
		
		mainShaderProgram.bind();
		
		mainShaderProgram.setMatrix4f("model", modelMatrix);
		
		mainShaderProgram.setMatrix4f("view", viewMatrix);
		
		mainShaderProgram.setMatrix4f("projection", projectionMatrix);
		
		mainShaderProgram.setVector3f("objectColor", new Vector3f(1.0f, 0.5f, 0.31f));
		mainShaderProgram.setVector3f("lightColor", new Vector3f(1.0f, 1.0f, 1.0f));
		mainShaderProgram.setVector3f("lightPos", light.getPosition());
		mainShaderProgram.setVector3f("viewPos", viewPos);
		
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			
			modelMatrix.identity();
			modelMatrix.translate(object.getPosition());
			modelMatrix.scale(0.5f);
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
	
	public void setViewPos(Vector3f viewPos) {
		this.viewPos = new Vector3f(viewPos);
	}
	
	public void cleanUp() {
		for (GameObject object : objects) {
			object.cleanUp();
		}
	}
	
}
