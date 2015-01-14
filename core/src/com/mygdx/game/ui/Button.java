package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;

public class Button extends Box {

	private TextureRegion up;
	private TextureRegion down;
	private boolean pressed;
	
	public Button(float xpos, float ypos, float bwidth, float bheight) {
		
		pressed = false;
		up = CodeBreaker.res.getAtlas("buttonpack").findRegion("up");
		down = CodeBreaker.res.getAtlas("buttonpack").findRegion("down");
		x = xpos;
		y = ypos;
		width = bwidth;
		height = bheight;
		
	}
	
	public void setPressed(boolean b) {
		pressed = b;
	}
	
	public void update (float dt) {

	}
	
	public void render(SpriteBatch sb) {
		if (pressed) { 
			sb.draw(down, x - width/2, y - height/2, width, height);
		}
		else {
			sb.draw(up, x - width/2, y - height/2, width, height);
		}
	}
	
}
