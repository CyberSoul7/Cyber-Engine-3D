package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	private long windowHandle;
	
	private int width, height;
	private String title;
	private boolean vSync;
	private boolean resized;

	public Window(int width, int height, String title, boolean vSync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.vSync = vSync;
		this.resized = false;
	}
	
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if (windowHandle == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
		
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			
			glfwGetWindowSize(windowHandle, pWidth, pHeight);
			
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			glfwSetWindowPos(
					windowHandle,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		
		glfwMakeContextCurrent(windowHandle);
		
		glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
			Window.this.width = width;
			Window.this.height = height;
			Window.this.setResized(true);
		});
		
		if (vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		
		glfwShowWindow(windowHandle);
		
	}
	
	public float getAspectRatio() {
		return (float)width/(float)height;
	}
	
	public boolean isvSync() {
		return vSync;
	}
	
	public boolean isResized() {
		return resized;
	}
	
	public void setResized(boolean resized) {
		this.resized = resized;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setvSync(boolean vSync) {
		this.vSync = vSync;
		if (vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
	}
	
	public long getHandle() {
		return windowHandle;
	}
	
}
