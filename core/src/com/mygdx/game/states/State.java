package com.mygdx.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.CodeBreaker;

public abstract class State {
	
	protected GSM gsm;
	protected OrthographicCamera cam;
	protected Vector3 mouse;
	
	protected State(GSM gsm) {
		this.gsm = gsm;
		cam = new OrthographicCamera();
		cam.setToOrtho(false, CodeBreaker.WIDTH, CodeBreaker.HEIGHT);
		mouse = new Vector3();
	}
	
	public abstract void update(float dt);
	public abstract void render(SpriteBatch sb);
	public abstract void handleInput();
	
}
