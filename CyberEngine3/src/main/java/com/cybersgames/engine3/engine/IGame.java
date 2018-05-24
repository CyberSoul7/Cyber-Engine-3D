package com.cybersgames.engine3.engine;

import com.cybersgames.engine3.engine.render.Renderer;

public interface IGame {
	
	public void init(Window window);
	public void input(Window window);
	public void tick();
	public void render(Renderer renderer, Window window);
	
}
