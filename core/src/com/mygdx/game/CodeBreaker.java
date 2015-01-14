package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.handlers.Content;
import com.mygdx.game.states.GSM;
import com.mygdx.game.states.StartState;

public class CodeBreaker extends ApplicationAdapter {

	public static final String TITLE = "CodeBreaker";
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static final int NUMSCORES = 9;
	
	public static Content res;
	
	private GSM gsm;
	private SpriteBatch sb;
	
	public void create() {

		res = new Content();
		res.loadAtlas("buttonpack.pack", "buttonpack");
		res.loadAtlas("pegpack.pack", "pegpack");
		res.loadAtlas("numberpack.pack", "numberpack");
		res.loadAtlas("menupack.pack", "menupack");
		res.loadAtlas("random.pack", "random");
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		gsm = new GSM();
		gsm.push(new StartState(gsm));
		sb = new SpriteBatch();
		
	}

	public void render() {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(sb);
		
	}
}
