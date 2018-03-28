package com.cybersgames.engine3.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private int vertexCount;
	private int vao;
	private int vertVbo;
	private int colorVbo;
	private int ebo;

	public Mesh(float vertices[], int indices[], float colors[]) {
		
		FloatBuffer verticesBuffer = null;
		FloatBuffer colorsBuffer = null;
		IntBuffer indicesBuffer = null;
		
		try {
			vertexCount = indices.length;
			
			vao = glGenVertexArrays();
			glBindVertexArray(vao);
			
			vertVbo = glGenBuffers();
			verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
			verticesBuffer.put(vertices).flip();
			glBindBuffer(GL_ARRAY_BUFFER, vertVbo);
			glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			
			colorVbo = glGenBuffers();
			colorsBuffer = MemoryUtil.memAllocFloat(colors.length);
			colorsBuffer.put(colors).flip();
			glBindBuffer(GL_ARRAY_BUFFER, colorVbo);
			glBufferData(GL_ARRAY_BUFFER, colorsBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
			
			ebo = glGenBuffers();
			indicesBuffer = MemoryUtil.memAllocInt(indices.length);
			indicesBuffer.put(indices).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
			
			//Unbind VAO and VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		} finally {
			if (verticesBuffer != null) {
				MemoryUtil.memFree(verticesBuffer);
			}
			if (indicesBuffer != null) {
				MemoryUtil.memFree(indicesBuffer);
			}
			if (colorsBuffer != null) {
				MemoryUtil.memFree(colorsBuffer);
			}
		}
	}
	
	public int getVao() {
		return vao;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public void cleanUp() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vertVbo);
		glDeleteBuffers(colorVbo);
		glDeleteBuffers(ebo);
		
		glBindVertexArray(0);
		glDeleteVertexArrays(vao);
	}
	
}
