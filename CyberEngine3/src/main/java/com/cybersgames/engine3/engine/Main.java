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

import com.cybersgames.engine3.game.TestGame;

public class Main {
	
	private Window window;
	private Renderer renderer;
	
	private TestGame game;
	
	public static float fps;
	public static float deltaTime = 0.0f;
	
	public Main(TestGame game) throws Exception {
		this.game = game;
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
		
		game.init(window);
		
		renderer.init(window, game.getCamera());
		
	}
	
	private void loop() throws Exception {
		
		double lastTime = System.nanoTime();
		double ns = 1_000_000_000;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while(!glfwWindowShouldClose(window.getHandle())) {
			
			double now = System.nanoTime();
			deltaTime = (float) ((now - lastTime) / ns);
			lastTime = now;
			
			input();
			
			tick();
			
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
		game.tick();
	}
	
	private void input() {
		game.input(window);
	}
	
	private void render() throws Exception {
		
		if (window.isResized()) {
			glViewport(0, 0, window.getWidth(), window.getHeight());
			window.setResized(false);
		}
		
		game.render(renderer, window);
		
		renderer.render(window);
		
	}
	
	private void cleanUp() {
		renderer.cleanUp();
		glfwTerminate();
	}
	
	public static void main(String[] args) {
		try {
			new Main(new TestGame());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
