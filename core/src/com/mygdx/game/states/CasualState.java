package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Other;

public class CasualState extends State {

	private Other title;
	private Other back;
	private Other create;
	private Other single;
	
	public CasualState(GSM gsm) {
		
		super(gsm);
		title = new Other("title", CodeBreaker.WIDTH/2, 620, 330, 90);
		back = new Other("back", CodeBreaker.WIDTH/2, 150, 80, 60);
		create = new Other("create", CodeBreaker.WIDTH/2, 310, 300, 60);
		single = new Other("single", CodeBreaker.WIDTH/2, 230, 200, 60);

	}
	
	public void update(float dt) {
		handleInput();
	}
	
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		title.render(sb);
		back.render(sb);
		create.render(sb);
		single.render(sb);
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
			if (single.contains(mouse.x, mouse.y)) {
				gsm.set(new MainMenu(gsm));
			}
			if (create.contains(mouse.x,  mouse.y)) {
				gsm.set(new CodeState(gsm));
			}
		}
	}
	
}
