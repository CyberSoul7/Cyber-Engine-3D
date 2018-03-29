package com.cybersgames.engine3.engine;

import org.joml.Vector3f;

public class GameObject {
	
	private Mesh mesh;
	private Vector3f position;
	
	public GameObject(Mesh mesh) {
		this(mesh, new Vector3f().zero());
	}
	
	public GameObject(Mesh mesh, Vector3f position) {
		this.mesh = mesh;
		this.position = position;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void render() {
		mesh.render();
	}

	public void cleanUp() {
		mesh.cleanUp();
	}
	
}
