package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static java.lang.Math.*;

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
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glEnable(GL_DEPTH_TEST);
		
		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		
		objects.add(new GameObject(OBJLoader.loadMesh("/models/cube.obj", new Texture("/textures/grassblock.png")), new Vector3f(0.0f, 0.0f, 0.0f), new Material(new Vector3f(1.0f, 0.5f, 0.31f), 32.0f)));
		light = new GameObject(OBJLoader.loadMesh("/models/cube.obj", null), new Vector3f(1.2f, 1.0f, 2.0f), new Material(new Vector3f(1.0f, 1.0f, 1.0f), 0.0f));
		
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
		
		Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);
		
		light.getMaterial().diffuseColor = new Vector3f(lightColor).mul(0.5f);
		light.getMaterial().ambientColor = new Vector3f(light.getMaterial().diffuseColor).mul(0.2f);
		
		mainShaderProgram.setVector3f("light.position", light.getPosition());
		mainShaderProgram.setVector3f("light.ambient", light.getMaterial().ambientColor);
		mainShaderProgram.setVector3f("light.diffuse", light.getMaterial().diffuseColor);
		mainShaderProgram.setVector3f("light.specular", light.getMaterial().specularColor);
		
		mainShaderProgram.setVector3f("viewPos", viewPos);
		
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			
			mainShaderProgram.setInt("material.diffuse", 0);
			mainShaderProgram.setVector3f("material.specular", object.getMaterial().specularColor);
			mainShaderProgram.setFloat("material.shininess", object.getMaterial().shininess);
			
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
