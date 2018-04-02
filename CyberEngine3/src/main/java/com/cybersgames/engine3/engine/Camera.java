package com.cybersgames.engine3.engine;

import org.joml.Vector3f;

public class Camera {
	
	private Vector3f cameraPos;
	private Vector3f cameraFront;
	private Vector3f cameraUp;
	
	public Camera(Vector3f position) {
		this.cameraPos = position;
		cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
	}

	public Vector3f getPosition() {
		return cameraPos;
	}

	public Vector3f getCameraFront() {
		return cameraFront;
	}

	public Vector3f getCameraUp() {
		return cameraUp;
	}

	public void setPosition(Vector3f cameraPos) {
		this.cameraPos = cameraPos;
	}

	public void setCameraFront(Vector3f cameraFront) {
		this.cameraFront = cameraFront;
	}

	public void setCameraUp(Vector3f cameraUp) {
		this.cameraUp = cameraUp;
	}
	
}
