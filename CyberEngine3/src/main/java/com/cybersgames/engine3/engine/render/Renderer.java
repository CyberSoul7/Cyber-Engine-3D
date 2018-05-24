package com.cybersgames.engine3.engine.render;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import org.lwjgl.opengl.*;

import com.cybersgames.engine3.engine.Camera;
import com.cybersgames.engine3.engine.Utils;
import com.cybersgames.engine3.engine.Window;
import com.cybersgames.engine3.engine.objects.GameObject;
import com.cybersgames.engine3.engine.objects.Material;
import com.cybersgames.engine3.engine.objects.OBJLoader;

public class Renderer {
	
	private List<GameObject> objects = new ArrayList<GameObject>();
	private GameObject light;
	
	private ShaderProgram mainShaderProgram;
	private ShaderProgram lightShaderProgram;
	
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Vector3f viewPos;
	
	private Vector3f cubePositions[] = {
		    new Vector3f( 0.0f,  0.0f,  0.0f),
		    new Vector3f( 2.0f,  5.0f, -15.0f),
		    new Vector3f(-1.5f, -2.2f, -2.5f),
		    new Vector3f(-3.8f, -2.0f, -12.3f),
		    new Vector3f( 2.4f, -0.4f, -3.5f),
		    new Vector3f(-1.7f,  3.0f, -7.5f),
		    new Vector3f( 1.3f, -2.0f, -2.5f),
		    new Vector3f( 1.5f,  2.0f, -2.5f),
		    new Vector3f( 1.5f,  0.2f, -1.5f),
		    new Vector3f(-1.3f,  1.0f, -1.5f)
		};
	
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
		
		for (int i = 0; i < 10; i++) {
			objects.add(new GameObject(OBJLoader.loadMesh("/models/cube.obj", new Texture("/textures/container2.png"), new Texture("/textures/container2_specular.png")), cubePositions[i], new Material(new Vector3f(1.0f, 0.5f, 0.31f), 32.0f)));
		}
		
		light = new GameObject(OBJLoader.loadMesh("/models/cube.obj", null, null), new Vector3f(1.2f, 1.0f, 2.0f), new Material(new Vector3f(1.0f, 1.0f, 1.0f), 0.0f));
		
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
		//mainShaderProgram.setVector3f("light.direction", new Vector3f(-0.2f, -1.0f, -0.3f));
		mainShaderProgram.setVector3f("light.ambient", light.getMaterial().ambientColor);
		mainShaderProgram.setVector3f("light.diffuse", light.getMaterial().diffuseColor);
		mainShaderProgram.setVector3f("light.specular", light.getMaterial().specularColor);
		
		mainShaderProgram.setFloat("light.constant", 1.0f);
		mainShaderProgram.setFloat("light.linear", 0.045f);
		mainShaderProgram.setFloat("light.quadratic", 0.0075f);
		
		mainShaderProgram.setVector3f("viewPos", viewPos);
		
		for (int i = 0; i < objects.size(); i++) {
			GameObject object = objects.get(i);
			
			mainShaderProgram.setInt("material.diffuse", 0);
			mainShaderProgram.setInt("material.specular", 1);
			mainShaderProgram.setVector3f("material.specular", object.getMaterial().specularColor);
			mainShaderProgram.setFloat("material.shininess", object.getMaterial().shininess);
			
			modelMatrix.identity();
			modelMatrix.translate(object.getPosition());
			float angle = 20.0f * i;
			modelMatrix.rotate((float) toRadians(angle), new Vector3f(1.0f, 0.3f, 0.5f));
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
