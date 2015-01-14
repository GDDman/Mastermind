package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;

public class Peg extends Circle {

	private Texture tex;
	private TextureRegion zero, one, two, three, four, five;
	private TextureRegion[] pegs = new TextureRegion[6];
	private int currentcolor;
	private boolean pressed;
	private boolean fading;
	private float alpha;
	private Color color;
	
	public Peg(float xpos, float ypos, float pradius) {
		
		currentcolor = 0;
		x = xpos;
		y = ypos;
		radius = pradius;
		pressed = true;
		fading = false;
		alpha = 1f;
		
		zero = CodeBreaker.res.getAtlas("pegpack").findRegion("0");
		one = CodeBreaker.res.getAtlas("pegpack").findRegion("2"); 
		two = CodeBreaker.res.getAtlas("pegpack").findRegion("5"); 
		three = CodeBreaker.res.getAtlas("pegpack").findRegion("3"); 
		four = CodeBreaker.res.getAtlas("pegpack").findRegion("4"); 
		five = CodeBreaker.res.getAtlas("pegpack").findRegion("1");
		
		tex = zero.getTexture();
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		pegs[0] = zero;
		pegs[1] = one;
		pegs[2] = two;
		pegs[3] = three;
		pegs[4] = four;
		pegs[5] = five;
		
	}
	
	public void changeColor() {		
		if (pressed) {
			currentcolor++;
			if (currentcolor > 5) {
				currentcolor = 0;
			}
		}
	}
	
	public void setFade() {
		alpha = 0;
		fading = true;
	}
	
	public void changeY(float dy) {
		y += dy;
	}
	
	public int getColor() {
		return currentcolor;
	}
	
	public void setColor(int i) {
		currentcolor = i;
	}
	
	public void setClear() {
		currentcolor = 0;
	}
	
	public void setPressed(boolean b) {
		pressed = b;
	}
	
	public void update(float dt) {
		if (fading) {
			alpha += dt;
			if (alpha >= 1) {
				alpha = 1;
				fading = false;
			}
		}
	}
	
	public float getY() {
		return y;
	}
	
	public void render(SpriteBatch sb) {
		if (fading) {
			color = sb.getColor();
			color.a = alpha;
			sb.setColor(color);
		}
		if (y - radius >= 80 && y <= 800 + radius) {
			sb.draw(pegs[currentcolor], x - radius, y - radius, radius*2, radius*2);					
		}
		if (fading) {
			color.a = 1;
			sb.setColor(color);
		}
	}
	
}
