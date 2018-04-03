package com.cybersgames.engine3.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.*;

public class Camera {
	
	public enum CameraMovement {
		FORWARD,
		BACKWARD,
		LEFT,
		RIGHT
	}
	
	private final static float YAW = -90.0f;
	private final static float PITCH = 0.0f;
	private final static float SPEED = 0.01f;
	private final static float SENSITIVITY = 1.0f;
	private final static float ZOOM = 45.0f;
	
	//Camera Attributes
	private Vector3f position;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;
	private Vector3f worldUp;
	
	//Euler Angles
	private float yaw;
	private float pitch;
	
	//Camera Options
	private float movementSpeed;
	private float mouseSensitivity;
	private float zoom;
	
	protected Camera(Builder builder) {
		this.position = builder.position;
		this.worldUp = builder.up;
		this.yaw = builder.yaw;
		this.pitch = builder.pitch;
		movementSpeed = SPEED;
		mouseSensitivity = SENSITIVITY;
		zoom = ZOOM;
		front = new Vector3f(0.0f, 0.0f, -1.0f);
		updateCameraVectors();
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public Matrix4f getViewMatrix() {
		return (new Matrix4f().setLookAt(position, position.add(front), up));
	}
	
	public void processKeyboard(CameraMovement direction) {
		float velocity = movementSpeed * Main.deltaTime;
		switch (direction) {
		case BACKWARD:
			position.sub(front.mul(velocity));
			break;
		case FORWARD:
			position.add(front.mul(velocity));
			break;
		case LEFT:
			position.sub(right.mul(velocity));
			break;
		case RIGHT:
			position.add(right.mul(velocity));
			break;
		}
	}
	
	public void processMouseMovement(float xoffset, float yoffset, boolean constrainPitch) {
		xoffset *= mouseSensitivity;
		yoffset *= mouseSensitivity;
		
		yaw += xoffset;
		pitch += yoffset;
		
		if (constrainPitch) {
			pitch = Utils.clamp(pitch, -89.0f, 89.0f);
		}
		
		updateCameraVectors();
	}
	
	public void processMouseScroll(float yoffset) {
		zoom -= yoffset;
		zoom = Utils.clamp(zoom, 1.0f, 45.0f);
	}
	
	private void updateCameraVectors() {
		Vector3f front = new Vector3f();
		front.x = (float) (cos(toRadians(yaw) * cos(toRadians(pitch))));
		front.y = (float) (sin(toRadians(pitch)));
		front.z = (float) (sin(toRadians(yaw)) * cos(toRadians(pitch)));
		this.front = new Vector3f(front).normalize();
		right = new Vector3f(front).cross(worldUp).normalize();
		up = new Vector3f(right).cross(front).normalize();
	}
	
	public static class Builder {
		
		private Vector3f position = new Vector3f().zero();
		private Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		private float yaw = YAW;
		private float pitch = PITCH;
		
		public Builder position(Vector3f position) {
			this.position = position;
			return this;
		}
		public Builder up(Vector3f up) {
			this.up = up;
			return this;
		}
		public Builder yaw(float yaw) {
			this.yaw = yaw;
			return this;
		}
		public Builder pitch(float pitch) {
			this.pitch = pitch;
			return this;
		}
		public Camera build() {
			return new Camera(this);
		}
		
	}
	
}
