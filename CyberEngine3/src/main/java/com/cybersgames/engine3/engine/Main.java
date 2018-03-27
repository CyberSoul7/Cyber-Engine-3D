package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;

public class Main {
	
	private Window window;
	private Renderer renderer;
	
	//Git Test 1
	//Git Test 2
	public Main() throws Exception {
		window = new Window(800, 600, "Cyber Engine 3D", false);
		renderer = new Renderer();
		init();
		loop();
		cleanUp();
	}
	
	private void init() {
		
		if (!glfwInit()) {
			throw new RuntimeException("Failed to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		if (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) {
			glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		}
		
		window.init();
		renderer.init();
		
		GL.createCapabilities();
		
	}
	
	private void loop() throws Exception {
		
		while(!glfwWindowShouldClose(window.getHandle())) {
			input();
			
			render();
			
			glfwSwapBuffers(window.getHandle());
			glfwPollEvents();
		}
		
	}
	
	private void input() {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window.getHandle(), true);
	}
	
	private void render() throws Exception {
		
		glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		
		renderer.render();
		
	}
	
	private void cleanUp() {
		glfwTerminate();
	}
	
	public static void main(String[] args) {
		try {
			new Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}