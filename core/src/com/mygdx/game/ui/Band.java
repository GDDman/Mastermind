package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;

public class Band extends Box {

	private Texture band;
	private float alpha;
	private Color color;
	private boolean fading;
	private float fadetimer;
	
	public Band(float xpos, float ypos) {
		
		alpha = 1f;
		fading = false;
		fadetimer = 0;
		x = xpos;
		y = ypos;
		width = CodeBreaker.WIDTH;
		height = 71;
		band = new Texture(Gdx.files.internal("band.png"));
		band.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
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
	
	public void changeY(float dy) {
		y += dy;
	}
	
	public void setFade() {
		alpha = 0;
		fading = true;
	}
	
	public void render(SpriteBatch sb) {	
		
		if (fading) {
			color = sb.getColor();
			color.a = alpha;
			sb.setColor(color);
		}
		if (y > 110) {
			sb.draw(band, x - width/2, y - height/2, width, height);			
		}
		if (fading) {
			color.a = 1;
			sb.setColor(color);
		}
	}
	
}
