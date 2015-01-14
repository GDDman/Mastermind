package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Buttons;
import com.mygdx.game.ui.Other;

public class SelectionState extends State {

	private Other back;
	private Buttons casual, normal;
	
	protected SelectionState(GSM gsm) {
		super(gsm);
		back = new Other("back", CodeBreaker.WIDTH/2, 100, 80, 60);
		casual = new Buttons("casual.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 200, 480f, 174.72f);
		casual.setBounds(360, 0);
		normal = new Buttons("normal.png", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 100, 480f, 174.72f);
		normal.setBounds(360, 0);
	}

	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		back.render(sb);
		casual.render(sb);
		normal.render(sb);
		sb.end();
	}

	public void handleInput() {
		
		if (Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX();
			mouse.y = Gdx.input.getY();
			cam.unproject(mouse);
		
			if (back.contains(mouse.x, mouse.y)) {
				gsm.set(new StartState(gsm));
			}
			if (casual.contains(mouse.x, mouse.y)) {
				gsm.set(new CasualState(gsm));
			}
			if (normal.contains(mouse.x, mouse.y)) {
				gsm.set(new PlayState(gsm, 4, 1, 0, 0, 0));
			}
		}
		
	}

}
