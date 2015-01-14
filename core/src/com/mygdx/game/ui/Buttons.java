package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Buttons extends Box {

	private Texture texture;
	private float abwidth;
	private float abheight;
	
	public Buttons(String s, float xpos, float ypos, float bwidth, float bheight) {
		x = xpos;
		y = ypos;
		abwidth = bwidth;
		abheight = bheight;
		width = bwidth;
		height = bheight;
		
		texture = new Texture(Gdx.files.internal(s));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	public void setBounds(float w, float h) {
		width = w;
		if (h != 0) {
			height = h;
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.draw(texture, x - abwidth/2, y - abheight/2, abwidth, abheight);
	}
	
}
