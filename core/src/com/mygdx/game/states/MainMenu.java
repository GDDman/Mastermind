package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.MenuButtons;
import com.mygdx.game.ui.Other;

public class MainMenu extends State{
	
	private MenuButtons[] buttons = new MenuButtons[4];
	private Other title;
	private Other back;
	
	public MainMenu(GSM gsm) {
		
		super(gsm);
		buttons[0] = new MenuButtons(CodeBreaker.WIDTH/4 + 50, 360, 80, 40, 1);
		buttons[1] = new MenuButtons(CodeBreaker.WIDTH/4 + 50, 410, 160, 40, 2);
		buttons[2] = new MenuButtons(CodeBreaker.WIDTH/4 + 50, 460, 80, 40, 3);
		buttons[3] = new MenuButtons(CodeBreaker.WIDTH/4 + 50, 510, 140, 40, 4);
		
		title = new Other("title", CodeBreaker.WIDTH/2, 620, 330, 90);
		back = new Other("back", 3*CodeBreaker.WIDTH/4 - 10, 455, 80, 60);
		
	}

	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {		
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].render(sb);
		}
		title.render(sb);
		back.render(sb);
		sb.end();
	}

	public void handleInput() {
		
		if(Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			for (int j = 0; j < buttons.length; j++) {
				if (buttons[j].contains(mouse.x, mouse.y)) {
					gsm.set(new PlayState(gsm, j, 0, 0, 0, 0));
				}
			}
			if (back.contains(mouse.x, mouse.y)) {
				gsm.set(new CasualState(gsm));
			}
			
		}
			
	}

}
