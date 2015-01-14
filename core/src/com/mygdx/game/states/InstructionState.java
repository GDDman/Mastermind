package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.CodeBreaker;
import com.mygdx.game.ui.Buttons;
import com.mygdx.game.ui.Other;

public class InstructionState extends State {

	private Other back;
	private Buttons instructions;
	
	public InstructionState(GSM gsm) {
		super(gsm);
		instructions = new Buttons("instructions.png", CodeBreaker.WIDTH/2, 650,  480f, 84.48f);
		back = new Other("back", CodeBreaker.WIDTH/2, 500, 80, 60);		
	}
	
	public void update(float dt) {
		handleInput();
	}
	
	public void render(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		back.render(sb);
		instructions.render(sb);
		sb.end();
	}
	
	public void handleInput() {
		if(Gdx.input.isTouched(0)) {
			mouse.x = Gdx.input.getX(0);
			mouse.y = Gdx.input.getY(0);
			cam.unproject(mouse);
			
			if (back.contains(mouse.x, mouse.y)) {
				gsm.set(new StartState(gsm));
			}
		}
	}
	
}
