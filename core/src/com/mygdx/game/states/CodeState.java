package com.mygdx.game.states;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Button;
import com.mygdx.game.ui.MenuButtons;
import com.mygdx.game.ui.Other;
import com.mygdx.game.ui.Peg;

public class CodeState extends State {
	
	private int[] key;
	private Peg[] pegs;
	private int[] keyplus;
	private Peg[] pegplus;
			
	private int tries;
	private int numpegs;
	private Button button;
	private boolean difcon;
	
	private Other random;
	private Other back;
	private int difficulty;
	private MenuButtons dif;
	private boolean animating;
	private float randomizetime;
	private float randtimer;
	private float buffer;
	private float lag;
	
	public CodeState(GSM gsm) {
		
		super(gsm);
		difficulty = 1;
		setDif(difficulty);
		key = new int[4];
		pegs = new Peg[4];
		keyplus = new int[5];
		pegplus = new Peg[5];
		difcon = true;
		random = new Other("create", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 + 100, 200, 50);
		back = new Other("back", CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT/2 - 320, 80, 60);
		button = new Button(CodeBreaker.WIDTH/2, 200, 300, 100);
		dif = new MenuButtons(CodeBreaker.WIDTH/2, 700, 100, 60, 1);
		animating = false;
		randomizetime = 2.0f;
		randtimer = 0;
		buffer = 0;
		lag = 0;
		for (int i = 0; i < 4; i++) {
			key[i] = 1;
			float offset = (float)((i-2)*80 + 40);
			pegs[i] = new Peg(CodeBreaker.WIDTH/2 + offset , CodeBreaker.HEIGHT/2 + 200, 30);
			pegs[i].setColor(1);
		}
		for (int i = 0; i < 5; i++) {
			keyplus[i] = 1;
			float offset = (float)((i-5/2.0f)*80 + 40);
			pegplus[i] = new Peg(CodeBreaker.WIDTH/2 + offset , CodeBreaker.HEIGHT/2 + 200, 30);
			pegplus[i].setColor(1);
		}
		lag = 0;
	}
	
	public int[] randomKey(int max) {	
		Random rando = new Random();
		int[] temp = new int[max];
		for (int p = 0; p < max; p++) {
			temp[p] = rando.nextInt(5) + 1;
		}
		return temp;
	}
	
	public void animatepegs(float dt) {
		randtimer += dt;
		lag += dt;
		if (randtimer >= randomizetime) {
			animating = false;
		}
		
		buffer += 0.0011f;
		
		if (lag >= buffer) {
			chooseRandom();
			lag = 0;
		}

	}
	
	public void chooseRandom() {
		if (difficulty == 1 || difficulty == 2){
			key = randomKey(4);			
		}
		else {
			keyplus = randomKey(5);
		}
	}
	
	public void update(float dt) {		
		
		for (int i = 0; i < 4; i++) {
			pegs[i].setColor(key[i]);
		}
		for (int i = 0; i < 5; i++) {
			pegplus[i].setColor(keyplus[i]);
		}		
		
		if (animating) {
			animatepegs(dt);
		}
		else {
	 		handleInput();	
		}
	}
	
	public void setDif(int d) {
		if (d == 1) {
			tries = 12;
			numpegs = 4;
		}
		else if (d == 2) {
			tries = 10;
			numpegs = 4;
		}
		else if (d == 3) {
			tries = 18;
			numpegs = 5;
		}
		else {
			tries = 15;
			numpegs = 5;
		}
	}

	public void render(SpriteBatch sb) {		
		
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		random.render(sb);
		back.render(sb);
		button.render(sb);
		dif.render(sb);
		if (difficulty == 1 || difficulty == 2) {
			for (Peg p: pegs) {
				p.render(sb);
			}		
		}
		else {
			for (Peg b: pegplus) {
				b.render(sb);
			}			
		}
		sb.end();
	}

	public void handleInput() {
		
		if(Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (random.contains(mouse.x,  mouse.y)) {
				animating = true;
				randtimer = 0;
				lag = 0;
				buffer = 0;
				return;
			}
			
			if (button.contains(mouse.x, mouse.y)) {
				button.setPressed(true);
				if (difficulty == 1 || difficulty == 2) {
					for (int d = 0; d < 4; d++) {
						if (key[d] == 0) {
							break;
						}
						else if (d == 3) {
							setDif(difficulty);	
							gsm.set(new MultiState(gsm, key, tries, numpegs));								
						}
					}					
				}
				else {
					for (int y = 0; y < 5; y++) {
						if (keyplus[y] == 0) {
							break;
						}
						else if (y == 4) {
							setDif(difficulty);
							gsm.set(new MultiState(gsm, keyplus, tries, numpegs));
						}
					}
				}
			}
			else {
				button.setPressed(false);
			}
			
			if (back.contains(mouse.x, mouse.y)) {
				gsm.set(new CasualState(gsm));
			}
			if (dif.contains(mouse.x, mouse.y) && difcon) {
				difficulty++;
				if (difficulty > 4) {
					difficulty = 1;
				}
				dif.setDif(difficulty);
				float tempx;
				switch(difficulty) {
					case 1:
						tempx = 100;
						break;
					case 2:
						tempx = 200;
						break;
					case 3:
						tempx = 100;
						break;
					case 4: 
						tempx = 180;
						break;
					default:
						tempx = 100;
				}
				dif.changeX(tempx);
				difcon = false;
			}
			
			if (difficulty == 1 || difficulty == 2) {
				for (int i = 0; i < 4; i++) {
					if (pegs[i].contains(mouse.x,  mouse.y)) {
						pegs[i].changeColor();
						pegs[i].setPressed(false);
						key[i] = pegs[i].getColor();
					}
				}				
			}
			else{
				for (int n = 0; n < 5; n++) {
					if (pegplus[n].contains(mouse.x,  mouse.y)) {
						pegplus[n].changeColor();
						pegplus[n].setPressed(false);
						keyplus[n] = pegplus[n].getColor();
					}
				}
			}
		}
		else {
			for (Peg l: pegs) {
				l.setPressed(true);
			}
			for (Peg m: pegplus) {
				m.setPressed(true);
			}
			button.setPressed(false);
			difcon = true;
		}
	}
}
