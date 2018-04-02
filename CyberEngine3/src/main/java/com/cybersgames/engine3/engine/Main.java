package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

public class Main {
	
	private Window window;
	private Renderer renderer;
	
	public static float fps;
	public static float deltaTime = 0.0f;
	
	private Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
	private Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
	private Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
	
	public Main() throws Exception {
		window = new Window(800, 600, "Cyber Engine 3D", false);
		renderer = new Renderer();
		init();
		loop();
		cleanUp();
	}
	
	private void init() throws Exception {
		
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
		
		GL.createCapabilities();
		
		renderer.init();
		
	}
	
	private void loop() throws Exception {
		
		double lastTime = System.nanoTime();
		double delta = 0;
		float target = 60.0f;
		float ns = target / 1_000_000_000;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(!glfwWindowShouldClose(window.getHandle())) {
			
			double now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			deltaTime = (float) delta;
			input();
			
			while (delta >= 1) {
				tick();
				delta--;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 500) {
				timer += 500;
				frames *= 2;
				fps = (fps + frames) / 2;
				frames = 0;
			}
			
			glfwSwapBuffers(window.getHandle());
			glfwPollEvents();
		}
		
	}
	
	private void tick() {
		System.out.println(deltaTime);
	}
	
	private void input() {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window.getHandle(), true);
		float cameraSpeed = 0.15f * deltaTime;
		if (glfwGetKey(window.getHandle(), GLFW_KEY_W) == GLFW_PRESS)
			cameraPos.add(new Vector3f(cameraFront).mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_S) == GLFW_PRESS)
			cameraPos.sub(new Vector3f(cameraFront).mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_A) == GLFW_PRESS)
			cameraPos.sub(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_D) == GLFW_PRESS)
			cameraPos.add(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
	}
	
	private void render() throws Exception {
		
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		
		renderer.render(window, cameraPos, cameraFront, cameraUp);
		
	}
	
	private void cleanUp() {
		renderer.cleanUp();
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
