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
	
	private Camera camera;
	
	public Main() throws Exception {
		window = new Window(800, 600, "Cyber Engine 3D", false);
		renderer = new Renderer();
		init();
		loop();
		cleanUp();
	}
	
	private void init() throws Exception {
		
		camera = new Camera(new Vector3f(0.0f, 0.0f, 5.0f));
		
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
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1_000_000_000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(!glfwWindowShouldClose(window.getHandle())) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
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
		
	}
	
	private void input() {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window.getHandle(), true);
		float cameraSpeed = 0.05f;
		if (glfwGetKey(window.getHandle(), GLFW_KEY_W) == GLFW_PRESS) 
			camera.setPosition(camera.getPosition().add(camera.getCameraFront().mul(cameraSpeed)));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_S) == GLFW_PRESS)
			camera.setPosition(camera.getPosition().sub(camera.getCameraFront().mul(cameraSpeed)));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_A) == GLFW_PRESS)
			camera.setPosition(camera.getPosition().sub(camera.getCameraFront().cross(camera.getCameraUp()).normalize().mul(cameraSpeed)));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_D) == GLFW_PRESS)
			camera.setPosition(camera.getPosition().add(camera.getCameraFront().cross(camera.getCameraUp()).normalize().mul(cameraSpeed)));
	}
	
	private void render() throws Exception {
		
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		
		renderer.render(window, camera);
		
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
