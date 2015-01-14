package com.mygdx.game.ui;

public class Circle {

	protected float x;
	protected float y;
	protected float radius;
	
	public boolean contains(float x, float y) {
		return (Math.pow(x - this.x, 2) + Math.pow(y - this.y,  2)) < Math.pow(radius,  2);
	}
	
}
