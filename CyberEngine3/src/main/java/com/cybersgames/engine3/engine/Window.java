package com.cybersgames.engine3.engine;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class Window {
	
	private long windowHandle;
	
	private int width, height;
	private String title;
	private boolean vSync;

	public Window(int width, int height, String title, boolean vSync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.vSync = vSync;
	}
	
	public void init() {
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
			glViewport(0, 0, width, height);
		});
		
		if (vSync) {
			glfwSwapInterval(1);
		} else {
			glfwSwapInterval(0);
		}
		
		glfwShowWindow(windowHandle);
		
	}
	
	public boolean isvSyncOn() {
		return vSync;
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
