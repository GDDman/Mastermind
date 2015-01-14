package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Other;

public class LoseState extends State {
	
	private Other back;	
	private Other lose;
	
	public LoseState(GSM gsm) {
		super(gsm);
		lose = new Other("lose", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 100, 300, 80);
		back = new Other("back", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 250, 100, 80);
	}
	
	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		back.render(sb);
		lose.render(sb);
		sb.end();
	}

	public void handleInput() {
		
		if (Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (back.contains(mouse.x, mouse.y)) {
				gsm.set(new StartState(gsm));
			}
		}
		
	}
}
