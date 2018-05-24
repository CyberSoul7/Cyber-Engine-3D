package com.cybersgames.engine3.engine.objects;

import org.joml.Vector3f;

public class GameObject {
	
	private Mesh mesh;
	private Vector3f position;
	private Material material;
	
	public GameObject(Mesh mesh, Material material) {
		this(mesh, new Vector3f().zero(), material);
	}
	
	public GameObject(Mesh mesh, Vector3f position, Material material) {
		this.mesh = mesh;
		this.position = position;
		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
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
