package com.cybersgames.engine3.engine;

public class GameObject {
	
	Mesh mesh;
	
	public GameObject(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void render() {
		mesh.render();
	}
	
}
