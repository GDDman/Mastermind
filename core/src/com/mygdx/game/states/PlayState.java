package com.mygdx.game.states;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Band;
import com.mygdx.game.ui.Button;
import com.mygdx.game.ui.Other;
import com.mygdx.game.ui.Peg;
import com.mygdx.game.ui.Number;

public class PlayState extends State {
	
	private int tries;
	private int numpegs;
	private int difficulty;
	private int tempdiff;
	private boolean confirmed;
	private int currentturn;
	
	private Button button;
	private Peg[] pegs; 
	private Peg[] oldpegs;
	private int oldpegpos;
	private int[] key;
	private int match; 
	private int[] matches;
	
	private float timer;
	private int maxtime;
	
	private Other back;
	private Band band;
	private Number keynum;
	private int level;
	private boolean running;
	private int rows;
	private int score;
	private float totaltime;
	
	private float timelimit;
	private boolean timedout;
		
	private TextureRegion zero, one, two, three, four, five;
	private TextureRegion[] nums;
	private Number clock;
	private Number remaining;
	private float clocklag;
	private float dis;
	
	private boolean touched;
	private boolean scrollable;
	private boolean scrollableup;
	private float currenty;
	private float megaoffset;
	private Peg marker;
	private float velocity;
	private float slow;
	private float swipetimer;
	private boolean swipping;
	private float shiftdis;
	private boolean shifting;
	
	private Color color;
	
	public PlayState(GSM gsm, int dif, int lev, float tim, int row, int skor) {
		
		super(gsm);
		back = new Other("back", CodeBreaker.WIDTH/2, 785, 480, 30);
		difficulty = dif;
		confirmed = false;
		currentturn = 0;
		rows = row;
		timer = 0;
		totaltime = tim;
		maxtime = 1000000;
		score = skor;
		level = lev;
		timelimit = 0;
		timedout = false;
		clocklag = 2.0f;
		touched = false;
		scrollable = false;
		scrollableup = false;
		currenty = 0;
		dis = 80;
		megaoffset = 0;
		velocity = 0;
		slow = 0.05f;
		swipping = false;
		shiftdis = 0;
		shifting = false;
		
		if (dif == 4) {
			running = true;
		}
		else {
			running = false;
			tempdiff = difficulty;
		}
		
		Gdx.gl.glClearColor(202f/255f, 202f/255f, 202f/255f, 1f);
		
		band = new Band(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT - 80);
		zero = CodeBreaker.res.getAtlas("numberpack").findRegion("0"); 
		one = CodeBreaker.res.getAtlas("numberpack").findRegion("1"); 
		two = CodeBreaker.res.getAtlas("numberpack").findRegion("2"); 
		three = CodeBreaker.res.getAtlas("numberpack").findRegion("3"); 
		four = CodeBreaker.res.getAtlas("numberpack").findRegion("4"); 
		five = CodeBreaker.res.getAtlas("numberpack").findRegion("5");
		
		nums = new TextureRegion[6];
		nums[0] = zero;
		nums[1] = one;
		nums[2] = two;
		nums[3] = three;
		nums[4] = four;
		nums[5] = five;
		
		switch(difficulty) {
			case 0: 
				tries = 12;
				numpegs = 4;
				break;
			case 1:
				tries = 10;
				numpegs = 4;
				break;
			case 2:
				tries = 18;
				numpegs = 5;
				break;
			case 3:
				tries = 15;
				numpegs = 5;
				break;
			case 4:
				findLevel(level);
				break;
			default:
				tries = 12;
				numpegs = 4;
		}
		
		clock = new Number((int)timelimit, 420, 40, 20, 30);
		key = new int[numpegs];
	    randomKey();	
		match = 0;
		pegs = new Peg[numpegs];
		matches = new int[tries]; 
		oldpegs = new Peg[numpegs*tries]; 
		oldpegpos = 0;
		marker = new Peg(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT - 80 + megaoffset, 40);
		newRow();
		
		// TODO temporary
		int h = 0;
		for (int k = 0; k < key.length; k++) {
			h += key[key.length - k - 1]*((int)Math.pow(10, k));
		}
		keynum = new Number(h, CodeBreaker.WIDTH/2, 750, 10, 20);
		if (difficulty != 4) {
			remaining = new Number(tries, 380, 40, 20, 30);
			button = new Button(CodeBreaker.WIDTH/2 - 60, 50, 240, 80);
		}
		else {
			remaining = new Number(tries, 60 , 40, 20, 30);	
			button = new Button(CodeBreaker.WIDTH/2, 50, 240, 80);
		}
		
	}	
	
	private int returnScore(float dt, int turn) {
		if (dt >= 800) {
			dt = 800;
		}
		return (int) (80000 - dt*100)/(turn + 1);
	}
	
	private void findLevel(int n) {
		if (n >= 1 && n <= 4) {
			tries = 12;
			numpegs = 4;
			tempdiff = 0;
			timelimit = 160 - (level - 1)*10;
		}
		else if (n >= 5 && n <= 8) {
			tries = 10;
			numpegs = 4;
			tempdiff = 1;
			timelimit = 140 - (level - 5)*10;
		}
		else if (n >= 9 && n <= 12) {
			tries = 18;
			numpegs = 5;
			tempdiff = 2;
			timelimit = 300 - (level - 9)*10;
		}
		else if (n > 12) {
			tries = 15;
			numpegs = 5;
			tempdiff = 3;
			timelimit = 300 - (level - 13)*10;
		}
	}
	
	private void randomKey() {	
		Random rando = new Random();
		for (int p = 0; p < numpegs; p++) {
			key[p] = rando.nextInt(5) + 1;
		}
	}
	
	private void newRow() {
		
		float radius;
		radius = (numpegs == 5)? 26: 33;
		
		if (pegs != null) {
			for (int h = 0; h < numpegs; h++) {
				oldpegs[oldpegpos + h] = pegs[h];
			}
			oldpegpos += numpegs;
		}
		
		for (int i = 0; i < numpegs; i++) {
			if (difficulty == 2 || tempdiff == 2 || difficulty == 3 || tempdiff == 3) {
				pegs[i] = new Peg(55 + i*(60 + radius/2), CodeBreaker.HEIGHT - currentturn*dis - 80 + megaoffset, radius);
			} 
			else {
				pegs[i] = new Peg(64 + i*(2*radius + radius/2 + 13), CodeBreaker.HEIGHT - currentturn*dis - 80 + megaoffset, radius);
			}
		}
		if (currentturn > 7){
			setBottom();		
		}
	}
	
	private void shift(float f) {
		for (int k = 0; k < pegs.length; k++) {
			pegs[k].changeY(f);
		}
		for (int u = 0; u < oldpegs.length; u++) {
			if (oldpegs[u] != null) {
				oldpegs[u].changeY(f);			
			}
		}
		band.changeY(f);
		megaoffset += f;	
		marker.changeY(f);
	}
	
	private void setBottom() {
		float temporary = 150 - pegs[0].getY();
		float smalloffset = (temporary) - 0.1f;
		for (int k = 0; k < pegs.length; k++) {
			pegs[k].changeY(smalloffset);
		}
		for (int u = 0; u < oldpegs.length; u++) {
			if (oldpegs[u] != null) {
				oldpegs[u].changeY(smalloffset);			
			}
		}
		band.changeY(smalloffset);
		megaoffset += smalloffset;	
		marker.changeY(smalloffset);
	}
	
	private void setTop(){
		float temporary = CodeBreaker.HEIGHT - 100 - marker.getY();
		float smalloffset = (temporary) + 0.1f;
		for (int k = 0; k < pegs.length; k++) {
			pegs[k].changeY(smalloffset);
		}
		for (int u = 0; u < oldpegs.length; u++) {
			if (oldpegs[u] != null) {
				oldpegs[u].changeY(smalloffset);			
			}
		}
		band.changeY(smalloffset);
		megaoffset += smalloffset;	
		marker.changeY(smalloffset);
	}

	public void update(float dt) {
				
		if (swipping) {
			shift(velocity);
			if (velocity < 0) {
				velocity += slow;
				if (velocity >= 0) {
					velocity = 0;
					swipping = false;
				}
			}
			if (velocity > 0) {
				velocity -= slow;
				if (velocity <= 0) {
					velocity = 0;
					swipping = false;
				}
			}
		}
		
		if (currentturn > 7) {
			scrollableup = true;
			scrollable = true;
			if (shifting) {
				shift(shiftdis);
				shifting = false;
				return;
			}
		}
		if (pegs[0].getY() >= 150) {
			touched = false;
			swipping = false;
			if (currentturn > 7) {
				setBottom();	
				scrollable = false;
				scrollableup = true;
			}
		}
		if (marker.getY() <= CodeBreaker.HEIGHT - 100){
			touched = false;
			swipping = false;
			if (currentturn > 7) {
				setTop();
				scrollableup = false;
				scrollable = true;
			}
		}	
		
		for (int i = 0; i < numpegs; i++) {
			if (pegs[i].getColor() == 0) {
				confirmed = false;
				break;
			}
			confirmed = true;
		}
		
		handleInput();	
		band.update(dt);
		for (Peg x: pegs) {
			x.update(dt);
		}
		
		if (timer >= clocklag){
			clock.changeNum((int)((timelimit + clocklag) - timer));
		}
		swipetimer = timer;
		timer += dt;
		if (timer > maxtime) {
			timer = maxtime;
		}
		if (timer >= timelimit + clocklag) {
			timedout = true;
		}
		if (running && timedout) {
			rows += currentturn;
			score += returnScore(timer, currentturn);
			totaltime += timer;
			gsm.set(new WinState(gsm, totaltime, (rows - 1), difficulty, score));	
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		color = sb.getColor();
		band.render(sb);
		if (difficulty == 4){
			clock.render(sb);	
		}
		for (int i = 0; i < numpegs; i++) {
			pegs[i].render(sb);
		}
		color.a = 0.5f;
		sb.setColor(color);
		for (int j = 0; j < oldpegs.length; j++) {
			if (oldpegs[j] != null) {
				oldpegs[j].render(sb);
			}
		}
		color.a = 1f;
		sb.setColor(color);
		for (int m = 0; m < currentturn; m++) {
			if (CodeBreaker.HEIGHT - m*dis - 65 + megaoffset >= 125) {
				sb.draw(nums[matches[m]], 425, CodeBreaker.HEIGHT - m*dis - 90 + megaoffset, 20, 20);
			}
		}
		button.render(sb);
		back.render(sb);
		keynum.render(sb);
		remaining.render(sb);
		sb.end();
	}

	private void scroll(float changey) {
		velocity = (changey/(float)(timer - swipetimer))/300f;
		if (velocity < 0.1 && velocity > 0) {
			velocity = 0.1f;
		}
		if (velocity > -0.1 && velocity < 0) {
			velocity = - 0.1f;
		}
		for (int i = 0; i < oldpegs.length; i++) {
			if (oldpegs[i] != null) {
				oldpegs[i].changeY(changey);	
			}
		}		
		for (int j = 0; j < pegs.length; j++) {
			pegs[j].changeY(changey);
		}
		band.changeY(changey);
		marker.changeY(changey);
	}
	
	public void handleInput() {			
		
		if(Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (Gdx.input.justTouched()) {
				currenty = mouse.y;
				touched = true;
				swipping = false;
			}
			
			if (mouse.x > 0 && mouse.x < CodeBreaker.WIDTH && mouse.y > 0 && mouse.y < CodeBreaker.HEIGHT){
				
				for (int j = 0; j < numpegs; j++) {
					if (pegs[j].contains(mouse.x, mouse.y)) {
						pegs[j].changeColor();
						pegs[j].setPressed(false);
						touched = false;
					}
				}
				
				if (button.contains(mouse.x, mouse.y)) {
					button.setPressed(true);
					touched = false;
					if (confirmed) {
						for (int k = 0; k < numpegs; k++) {
							if (key[k] == pegs[k].getColor()) {
								match++;
							}
						}
						matches[currentturn] = match;
						if (match == numpegs) {
							//TODO
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (running) {
								level++;
								rows += (currentturn + 1);
								score += returnScore(timer, (currentturn + 1));
								totaltime += timer;
								gsm.set(new PlayState(gsm, difficulty, level, totaltime, rows, score));
							}
							else {
								gsm.set(new WinState(gsm, timer, currentturn, difficulty, 0));	
							}
						}
						currentturn++;
						if (currentturn != tries) {
							newRow();
							band.changeY(-dis);
							remaining.changeNum(tries-currentturn);
						}
						else {
							if (running) {
								rows += currentturn;
								score += returnScore(timer, currentturn);
								totaltime += timer;
								gsm.set(new WinState(gsm, totaltime, (rows - 1), difficulty, score));	
							}
							else{
								gsm.set(new LoseState(gsm));	
							}
						}
						match = 0;
						shiftdis = 150 - pegs[0].getY();
						shifting = true;
						band.setFade();
						for (Peg g: pegs) {
							g.setFade();
						}
					}
				}
				else {
					button.setPressed(false);
				}
				
				if (back.contains(mouse.x,  mouse.y)) {
					if (difficulty == 4) {
						gsm.set(new StartState(gsm));
					}
					else{
						gsm.set(new MainMenu(gsm));	
					}
				}
				
			}	
			if (touched && mouse.y >= 150) {
				float tempy = (mouse.y - currenty); 
				if (scrollableup && !scrollable) {
					if (tempy >= 0) {
						currenty = mouse.y;
						scroll(tempy);
						megaoffset += tempy;
						swipping = true;
					}
				}
				else if (scrollable && !scrollableup) {
					if (tempy <= 0) {
						currenty = mouse.y;
						scroll(tempy);
						megaoffset += tempy;
						swipping = true;
					}
				}
				else {
					if (currentturn > 7) {
						currenty = mouse.y;
						scroll(tempy);
						megaoffset += tempy;		
						swipping = true;
					}
				}
			}
		}
		else {
			swipping = true;
			button.setPressed(false);
			for (int i = 0; i < numpegs; i++) {
				pegs[i].setPressed(true);
			}
		}
	}
}


