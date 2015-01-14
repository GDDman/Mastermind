package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;

public class Other extends Box {

	private TextureRegion texture;
	
	public Other (String choice, float xpos, float ypos, float rwidth, float rheight) {
		
		x = xpos;
		y = ypos;
		width = rwidth;
		height = rheight;
		texture = CodeBreaker.res.getAtlas("random").findRegion(choice);
		
	}
	
	public void update() {
		
	}
	
	public void render(SpriteBatch sb) {	
			sb.draw(texture, x - width/2, y - height/2, width, height);
	}
}
