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
	
	private Vector3f cameraPosition;
	private Vector3f cameraFront;
	private Vector3f cameraUp;
	private Vector3f cameraRight;
	
	public Camera() {
		cameraPosition = new Vector3f(0.0f, 0.0f, 3.0f);
		cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		cameraRight = new Vector3f(cameraFront).cross(cameraUp).normalize();
		cameraSpeed = 2.5f;
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
	
}
