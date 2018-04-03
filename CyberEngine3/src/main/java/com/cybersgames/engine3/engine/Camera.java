package com.cybersgames.engine3.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.*;

public class Camera {
	
	public enum CameraMovement {
		FORWARD,
		BACKWARD,
		RIGHT,
		LEFT
	}
	
	private float cameraSpeed;
	private float sensitivity;
	
	private Vector3f cameraPosition;
	private Vector3f cameraFront;
	private Vector3f cameraUp;
	private Vector3f cameraRight;
	
	private float yaw;
	private float pitch;
	
	public Camera() {
		cameraPosition = new Vector3f(0.0f, 0.0f, 3.0f);
		cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		cameraRight = new Vector3f(cameraFront).cross(cameraUp).normalize();
		cameraSpeed = 2.5f;
		sensitivity = 0.05f;
		yaw = -90.0f;
		pitch = 0.0f;
	}
	
	public void tick() {
		
	}
	
	public Matrix4f getViewMatrix() {
		return (new Matrix4f().setLookAt(cameraPosition, new Vector3f(cameraPosition).add(cameraFront), cameraUp));
	}

	public void processKeyboard(CameraMovement direction) {
		float speed = cameraSpeed * Main.deltaTime;
		switch (direction) {
		case FORWARD:
			cameraPosition.add(new Vector3f(cameraFront).mul(speed));
			break;
		case BACKWARD:
			cameraPosition.sub(new Vector3f(cameraFront).mul(speed));
			break;
		case RIGHT:
			cameraPosition.add(new Vector3f(cameraRight).mul(speed));
			break;
		case LEFT:
			cameraPosition.sub(new Vector3f(cameraRight).mul(speed));
			break;
		}
	}

	public void processMouseMovement(float xoffset, float yoffset, boolean restrictPitch) {
		xoffset *= sensitivity;
		yoffset *= sensitivity;
		
		yaw += xoffset;
		pitch += yoffset;
		
		if (restrictPitch) {
			pitch = Utils.clamp(pitch, -89.0f, 89.0f);
		}
		
		Vector3f front = new Vector3f();
		front.x = (float) (cos(toRadians(yaw)) * cos(toRadians(pitch)));
		front.y = (float) (sin(toRadians(pitch)));
		front.z = (float) (sin(toRadians(yaw)) * cos(toRadians(pitch)));
		front.normalize();
		cameraFront = new Vector3f(front);
	}
	
}
