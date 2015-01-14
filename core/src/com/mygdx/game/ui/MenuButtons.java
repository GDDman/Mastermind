package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;

public class MenuButtons extends Box {

	private TextureRegion play, easy, medium, hard, expert;
	private TextureRegion[] buttons = new TextureRegion[5];
	private int index;
	
	public MenuButtons(float xpos, float ypos, float mwidth, float mheight, int choice) {		
		
		index = choice;
		x = xpos;
		y = ypos;
		width = mwidth;
		height = mheight;
		
		play = CodeBreaker.res.getAtlas("menupack").findRegion("play"); 
		easy = CodeBreaker.res.getAtlas("menupack").findRegion("easy"); 
		medium = CodeBreaker.res.getAtlas("menupack").findRegion("medium"); 
		hard = CodeBreaker.res.getAtlas("menupack").findRegion("hard"); 
		expert = CodeBreaker.res.getAtlas("menupack").findRegion("expert"); 
		
		buttons[0] = play;
		buttons[1] = easy;
		buttons[2] = medium;
		buttons[3] = hard;
		buttons[4] = expert;
		
	}
	
	public void changeX(float x) {
		width = x;
	}
	
	public void setDif(int i) {
		index = i;
	}
	
	public void update(float dt) {
		
	}
	
	public void render(SpriteBatch sb) {
		sb.draw(buttons[index], x - width/2, y - height/2, width, height);
	}
	
	
}
