package com.cybersgames.engine3.game;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.cybersgames.engine3.engine.IGame;
import com.cybersgames.engine3.engine.Main;
import com.cybersgames.engine3.engine.Renderer;
import com.cybersgames.engine3.engine.Utils;
import com.cybersgames.engine3.engine.Window;

public class TestGame implements IGame {
	
	//TODO: Implement Camera Class
	public Vector3f cameraPos = new Vector3f(0.0f, 0.0f, 3.0f);
	public Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
	public Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
	
	float yaw;
	float pitch;
	
	float lastX;
	float lastY;
	
	float fov = 45.0f;
	
	boolean firstMouse = true;
	
	@Override
	public void init(Window window) {
		glfwSetCursorPosCallback(window.getHandle(), (otherWindow, xpos, ypos) -> {
			
			if (firstMouse) {
				lastX = (float) xpos;
				lastY = (float) ypos;
				firstMouse = false;
			}
			
			float xoffset = (float) xpos - lastX;
			float yoffset = lastY - (float) ypos;
			lastX = (float) xpos;
			lastY = (float) ypos;
			
			float sensitivity = 0.05f;
			xoffset *= sensitivity;
			yoffset *= sensitivity;
			
			yaw += xoffset;
			pitch += yoffset;
			
			pitch = Utils.clamp(pitch, -89, 89);
			
			Vector3f front = new Vector3f();
			front.x = (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw)));
			front.y = (float) (Math.sin(Math.toRadians(pitch)));
			front.z = (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)));
			cameraFront = new Vector3f(front).normalize();
		});
		glfwSetScrollCallback(window.getHandle(), (otherWindow, xoffset, yoffset) -> {
			fov -= yoffset;
			fov = Utils.clamp(fov, 1.0f, 45.0f);
		});
	}

	@Override
	public void input(Window window) {
		if (glfwGetKey(window.getHandle(), GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window.getHandle(), true);
		float cameraSpeed = 0.15f * Main.deltaTime;
		if (glfwGetKey(window.getHandle(), GLFW_KEY_W) == GLFW_PRESS)
			cameraPos.add(new Vector3f(cameraFront).mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_S) == GLFW_PRESS)
			cameraPos.sub(new Vector3f(cameraFront).mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_A) == GLFW_PRESS)
			cameraPos.sub(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
		if (glfwGetKey(window.getHandle(), GLFW_KEY_D) == GLFW_PRESS)
			cameraPos.add(new Vector3f(cameraFront).cross(cameraUp).normalize().mul(cameraSpeed));
	}
	
	@Override
	public void tick() {
		
	}

	@Override
	public void render(Renderer renderer, Window window) {
		Matrix4f projection = new Matrix4f().identity().perspective((float) Math.toRadians(fov), window.getAspectRatio(), 0.1f, 100.0f);
		renderer.setProjectionMatrix(projection);
	}
	
}
