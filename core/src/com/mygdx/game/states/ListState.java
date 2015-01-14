package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.MenuButtons;
import com.mygdx.game.ui.Other;
import com.mygdx.game.ui.Number;

public class ListState extends State {

	private Preferences prefs;
	private int difficulty;
	private Other back;
	private Other high;
	private MenuButtons label;
	private Other norm;
	private Number[] scores = new Number[CodeBreaker.NUMSCORES];
	private Number[] nums = new Number[10];
	
	public ListState(GSM gsm, int dif) {
		
		super(gsm);
		difficulty = dif;		
		switch (difficulty) {
			case 0:
				 prefs = Gdx.app.getPreferences("PREFS");
				 label = new MenuButtons(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 70, 40, 1);
				 break;
			case 1:
				 prefs = Gdx.app.getPreferences("MEDPREFS");
				 label = new MenuButtons(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 110, 40, 2);
				 break;
			case 2:
				 prefs = Gdx.app.getPreferences("HARDPREFS");
				 label = new MenuButtons(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 70, 40, 3);
				 break;
			case 3:
				 prefs = Gdx.app.getPreferences("EXPREFS");
				 label = new MenuButtons(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 100, 40, 4);
				 break;
			case 4: 
				 prefs = Gdx.app.getPreferences("CHALPREFS");
				 norm = new Other("normal", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 160, 40);
				 break;
			default:
				 prefs = Gdx.app.getPreferences("PREFS");
		}
		
		back = new Other("back", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 300, 100, 80);
		high = new Other("high", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 200, 200, 40);
		
		for (int i = 1; i <= CodeBreaker.NUMSCORES; i++) {
			scores[i - 1] = new Number(prefs.getInteger("Score" + i), 3*CodeBreaker.WIDTH/4 - 20, CodeBreaker.HEIGHT - 320 - i*40, 15, 30);
			nums[i - 1] = new Number(i, CodeBreaker.WIDTH/4 - 20, CodeBreaker.HEIGHT - 320 - i*40, 15, 30);
		}
		
	}
	
	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for (int h = 0; h < CodeBreaker.NUMSCORES; h++) {
			if (scores[h].getNum() != 0) {
				scores[h].render(sb);				
			}
			nums[h].render(sb);
		}
		back.render(sb);
		high.render(sb);
		if (difficulty < 4) {
			label.render(sb);
		} 
		else {
			norm.render(sb);
		}
		sb.end();
	}

	public void handleInput() {
		
		if (Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (back.contains(mouse.x, mouse.y)) {
				prefs.flush();
				gsm.set(new ScoreState(gsm));
			}
		}
		
	}
	
}
