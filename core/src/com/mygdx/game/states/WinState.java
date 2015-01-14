package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Other;
import com.mygdx.game.ui.Number;

public class WinState extends State {

	private Other back;	
	private int score;
	private int difficulty;
	private int numturns;
	private float time;
	private int numscores;
	private Number scorenum;
	private Number seconds;
	private Number rows;
	private Other secs;
	private Other inputs;
	private Other skor;
	private Other newscore;
	
	private boolean highscored;
	private boolean nohigh;
	
	private Preferences prefs;
	
	public WinState(GSM gsm, float t, int turn, int mode, int scor) {
		
		super(gsm);
		time = t;
		numturns = turn;
		difficulty = mode;
		highscored = false;
		nohigh = false;
		if (difficulty == 4) {
			score = scor;
		}
		else{
			score = returnScore(time, numturns, difficulty);	
		}
		numscores = CodeBreaker.NUMSCORES;		
		back = new Other("back", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 250, 100, 80);
		scorenum = new Number(score, CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 50, 25, 50);
		seconds = new Number((int) time, CodeBreaker.WIDTH/4, CodeBreaker.HEIGHT/2 - 120, 20, 40);
		rows = new Number((numturns + 1), 3*CodeBreaker.WIDTH/4, CodeBreaker.HEIGHT/2 - 120, 20, 40);
		secs = new Other("secs", CodeBreaker.WIDTH/4, CodeBreaker.HEIGHT/2 - 60, 70, 35);
		inputs = new Other("inputs", 3*CodeBreaker.WIDTH/4, CodeBreaker.HEIGHT/2 - 60, 85, 30);
		skor = new Other("score", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 150, 160, 60);
		newscore = new Other("new", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT - 100, 400, 80);		
		
		switch (difficulty) {
			case 0:
				 prefs = Gdx.app.getPreferences("PREFS");
				 break;
			case 1:
				 prefs = Gdx.app.getPreferences("MEDPREFS");
				 break;
			case 2:
				 prefs = Gdx.app.getPreferences("HARDPREFS");
				 break;
			case 3:
				 prefs = Gdx.app.getPreferences("EXPREFS");
				 break;
			case 4:
				 prefs = Gdx.app.getPreferences("CHALPREFS");
				 break;
			case 5: 
				 nohigh = true;
				 break;
			default:
				 prefs = Gdx.app.getPreferences("PREFS");
		}
		if (!nohigh) {
			handleScores(score);	
		}
	}
	
	public void handleScores(int score) {

		for (int i = 1; i <= numscores; i++) {
			
			if (prefs.contains("Score" + i) && score > prefs.getInteger("Score" + i)) {
				if (i == 1) {
					highscored = true;
				}
				pushScores(i+1, numscores);
				prefs.putInteger("Score" + i, score);
				return;
			}	
			
			if (!prefs.contains("Score" + i)) {
				if (i == 1) {
					highscored = true;
				}
				prefs.putInteger("Score" + i, score);
				return;
			}

		}
	
	}
	
	public void pushScores(int a, int max) {
		for (int i = max; i >= a; i--) {
			prefs.putInteger("Score" + i, prefs.getInteger("Score" + (i-1))); 
		}
	}
	
	public int returnScore(float dt, int turn, int mode) {

		if (dt >= 800) {
			dt = 800;
		}
		return (int) (80000 - dt*100)/(turn + 1);
		
	}
	
	public void update(float dt) {
		handleInput();
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		back.render(sb);
		scorenum.render(sb);
		seconds.render(sb);
		rows.render(sb);
		secs.render(sb);
		inputs.render(sb);
		skor.render(sb);
		if (highscored) {
			newscore.render(sb);
		}
		sb.end();
	}

	public void handleInput() {
		
		if (Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (back.contains(mouse.x, mouse.y)) {
				if (difficulty != 5) {
					prefs.flush();
				}
				gsm.set(new StartState(gsm));
			}
		}
		
	}

}
