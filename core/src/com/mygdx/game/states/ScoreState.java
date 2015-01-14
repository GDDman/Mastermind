package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.MenuButtons;
import com.mygdx.game.ui.Other;

public class ScoreState extends State {

	private Other back;
	private Other title;
	private MenuButtons[] buttons = new MenuButtons[4];
	private Other norm;
	private Other casual;
	
	public ScoreState(GSM gsm) {
		
		super(gsm);
		back = new Other("back", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 300, 100, 80);
		title = new Other("high", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 300, 300, 80);
		norm = new Other("normal", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 180, 220, 50);
		casual = new Other("casual", CodeBreaker.WIDTH/2, 580, 150, 50);
	
		buttons[0] = new MenuButtons(CodeBreaker.WIDTH/4, 475, 80, 40, 1);
		buttons[1] = new MenuButtons(CodeBreaker.WIDTH/4, 405, 100, 40, 2);
		buttons[2] = new MenuButtons(3*CodeBreaker.WIDTH/4, 475, 80, 40, 3);
		buttons[3] = new MenuButtons(3*CodeBreaker.WIDTH/4, 405, 100, 40, 4);

	}
	
	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for (MenuButtons button: buttons) {
			button.render(sb);
		}
		back.render(sb);
		title.render(sb);
		norm.render(sb);
		casual.render(sb);
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
			
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].contains(mouse.x, mouse.y)) {
					gsm.set(new ListState(gsm, i));
				}
			}
			
			if (norm.contains(mouse.x, mouse.y)) {
				gsm.set(new ListState(gsm, 4));
			}
			
		}
		
	}
	
}
