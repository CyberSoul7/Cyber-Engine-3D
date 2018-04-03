package com.cybersgames.engine3.game;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.cybersgames.engine3.engine.Camera;
import com.cybersgames.engine3.engine.Camera.CameraMovement;
import com.cybersgames.engine3.engine.IGame;
import com.cybersgames.engine3.engine.Main;
import com.cybersgames.engine3.engine.Renderer;
import com.cybersgames.engine3.engine.Utils;
import com.cybersgames.engine3.engine.Window;

public class TestGame implements IGame {
	
	Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 3.0f));
	
	float lastX;
	float lastY;
	
	boolean firstMouse = true;
	
	@Override
	public void init(Window window) {
		
		lastX = window.getWidth() / 2;
		lastY = window.getHeight() / 2;
		
		glfwSetCursorPosCallback(window.getHandle(), (otherWindow, xpos, ypos) -> {
			
			if (firstMouse) {
				lastX = (float) xpos;
				lastY = (float) ypos;
				firstMouse = false;
			}
			
			float xoffset = (float) (xpos - lastX);
			float yoffset = (float) (lastY - ypos);
			lastX = (float) xpos;
			lastY = (float) ypos;
			
			camera.processMouseMovement(xoffset, yoffset, true);
		});
		glfwSetScrollCallback(window.getHandle(), (otherWindow, xoffset, yoffset) -> {
			camera.processMouseScroll((float) yoffset); 
		});
	}
	
	boolean disabledCursor = true;
	
	@Override
	public void input(Window window) {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window.getHandle(), true);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_W) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.FORWARD);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_S) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.BACKWARD);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_A) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.LEFT);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_D) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.RIGHT);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_SPACE) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.UP);
		if (glfwGetKey(window.getHandle(), GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
			camera.processKeyboard(CameraMovement.DOWN);
	}
	
	@Override
	public void tick() {
		camera.tick();
	}

	@Override
	public void render(Renderer renderer, Window window) {
		renderer.setProjectionMatrix(camera.getProjectionMatrix(window));
		renderer.setViewMatrix(camera.getViewMatrix());
	}
	
	public Camera getCamera() {
		return camera;
	}
	
}
