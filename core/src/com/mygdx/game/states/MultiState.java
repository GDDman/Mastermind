package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Band;
import com.mygdx.game.ui.Button;
import com.mygdx.game.ui.Number;
import com.mygdx.game.ui.Other;
import com.mygdx.game.ui.Peg;

public class MultiState extends State {

	private int tries;
	private int numpegs;
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
	private int difficulty;
	
	private float megaoffset;
	private Peg marker;
	private float dis;
	private boolean swipping, scrollable, scrollableup;
	
	private float velocity;
	private float slow;
	private boolean touched;
	private float swipetimer;
	private float currenty;
	private Number remaining;
	private float shiftdis;
	private boolean shifting;
	
	private Color color;
	
	private TextureRegion zero, one, two, three, four, five;
	private TextureRegion[] nums;
	
	public MultiState(GSM gsm, int[] k, int r, int c) {
		
		super(gsm);
		back = new Other("back", CodeBreaker.WIDTH/2, 785, 480, 30);
		confirmed = false;
		currentturn = 0;
		timer = 0;
		maxtime = 1000000;
		marker = new Peg(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT - 80 + megaoffset, 40);
		megaoffset = 0;
		dis = 80;
		scrollable = false;
		scrollableup = false;
		velocity = 0;
		slow = 0.05f;
		touched = false;
		swipetimer = 0;
		currenty = 0;
		swipping = false;
		shiftdis = 0;
		shifting = false;
		
		Gdx.gl.glClearColor(202f/255f, 202f/255f, 202f/255f, 1f);
		
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
		
		tries = r;
		numpegs = c;
		
		remaining = new Number(tries, 390, 40, 20, 30);
		button = new Button(CodeBreaker.WIDTH/2 - 40, 50, 240, 80);
		band = new Band(CodeBreaker.WIDTH/2, CodeBreaker.HEIGHT - 80);

		key = new int[numpegs];
		for (int z = 0; z < numpegs; z++) {
			key[z] = k[z];
		}
		match = 0;
		pegs = new Peg[numpegs];
		matches = new int[tries]; 
		oldpegs = new Peg[numpegs*tries]; 
		oldpegpos = 0;
		newRow();		
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
			if (difficulty == 2 || difficulty == 3) {
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
			velocity = 0;
			touched = false;
			swipping = false;
			if (currentturn > 7) {
				setBottom();	
				scrollable = false;
				scrollableup = true;
			}
		}
		
		band.update(dt);
		for (Peg x: pegs) {
			x.update(dt);
		}
		
		if (marker.getY() <= CodeBreaker.HEIGHT - 100){
			velocity = 0;
			touched = false;
			swipping = false;
			if (currentturn > 7) {
				setTop();
				scrollableup = false;
				scrollable = true;
			}
		}
		
		timer += dt;
		if (timer > maxtime) {
			timer = maxtime;
		}
		for (int i = 0; i < numpegs; i++) {
			if (pegs[i].getColor() == 0) {
				confirmed = false;
				break;
			}
			confirmed = true;
		}
		handleInput();
		swipetimer = timer;
	}

	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		color = sb.getColor();
		band.render(sb);
		remaining.render(sb);
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
							// TODO do this better
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							difficulty = 5;
							gsm.set(new WinState(gsm, timer, currentturn, difficulty, (currentturn + 1)));
						}
						currentturn++;
						remaining.changeNum(tries - currentturn);
						if (currentturn != tries) {
							newRow();
							band.changeY(-dis);
						}
						else {
							gsm.set(new LoseState(gsm));
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
					gsm.set(new CasualState(gsm));
				}	
			}	
			if (touched) {
				float tempy = (mouse.y - currenty); 
				if (scrollableup && !scrollable) {
					if (tempy >= 0) {
						currenty = mouse.y;
						scroll(tempy);
						megaoffset += tempy;
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
