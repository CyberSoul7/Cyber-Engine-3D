package com.cybersgames.engine3.engine;

import org.joml.Vector3f;

public class Material {
	
	public Vector3f ambientColor;
	public Vector3f diffuseColor;
	public Vector3f specularColor;
	public float shininess;
	
	public int diffuse = 0;
	
	public Material(Vector3f color, float shininess) {
		this(color, color, color, shininess);
	}
	
	public Material(Vector3f ambientColor, Vector3f diffuseColor, Vector3f specularColor, float shininess) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.shininess = shininess;
	}
	
}
