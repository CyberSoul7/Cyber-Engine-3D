package com.cybersgames.engine3.engine;

import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
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

	public Mesh(float vertices[], int indices[], float colors[]) throws Exception {
		vertexCount = indices.length;
		
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		vertVbo = createVbo(vertices, 0, 3);
		colorVbo = createVbo(colors, 1, 3);
		ebo = createVbo(indices);
		
		//Unbind VAO and VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}
	
	public void render() {
		
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
		
	}
	
	private int createVbo(float data[], int arrayId, int size) {
		int id = glGenBuffers();
		
		FloatBuffer dataBuffer = null;
		try {
			dataBuffer = MemoryUtil.memAllocFloat(data.length);
			dataBuffer.put(data).flip();
			glBindBuffer(GL_ARRAY_BUFFER, id);
			glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(arrayId, size, GL_FLOAT, false, 0, 0);
		} finally {
			if (dataBuffer != null) {
				MemoryUtil.memFree(dataBuffer);
			}
		}
		return id;
	}
	
	private int createVbo(int data[]) {
		int id = glGenBuffers();
		
		IntBuffer dataBuffer = null;
		
		try {
			dataBuffer = MemoryUtil.memAllocInt(data.length);
			dataBuffer.put(data).flip();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, dataBuffer, GL_STATIC_DRAW);
		} finally {
			if (dataBuffer != null) {
				MemoryUtil.memFree(dataBuffer);
			}
		}
		return id;
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
