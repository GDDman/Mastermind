package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Buttons;
import com.mygdx.game.ui.Other;

public class StartState extends State {

	private Buttons play, high, instructions, title;
	
	public StartState(GSM gsm) {
		
		super(gsm);	
		Gdx.gl.glClearColor(255f/255f, 114f/255f, 113f/255f, 1f);
		title = new Buttons("title.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 250, 420f, 178.4f);
		play = new Buttons("play.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 50, 480f, 84.48f);
		play.setBounds(360, 0);
		high = new Buttons("high.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 100, 480f, 84.48f);
		high.setBounds(360, 0);
		instructions = new Buttons("instructions.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 250, 480f, 84.48f);
		instructions.setBounds(360, 0);
	}
	
	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		title.render(sb);
		play.render(sb);
		high.render(sb);
		instructions.render(sb);
		sb.end();
	}

	public void handleInput() {
		
		if (Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (play.contains(mouse.x, mouse.y)) {
				gsm.set(new SelectionState(gsm));
			}	
			if (high.contains(mouse.x,  mouse.y)) {
				gsm.set(new ScoreState(gsm));
			}
			if (instructions.contains(mouse.x, mouse.y)) {
				gsm.set(new InstructionState(gsm));
			}
		}
		
	}
	
}
