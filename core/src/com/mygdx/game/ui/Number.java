package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.CodeBreaker;

public class Number extends Box {

	private int num;
	private int length;
	private int[] digits;
	private Texture tex;
	private TextureRegion digit;
	private TextureRegion zero, one, two, three, four, five, six, seven, eight, nine;
	
	public Number(int n, float xpos, float ypos, float nwidth, float nheight) {
		
		num = n;
		x = xpos;
		y = ypos;
		width = nwidth;
		height = nheight;
		length = String.valueOf(n).length();
		digits = getArray(num);
		
		zero = CodeBreaker.res.getAtlas("numberpack").findRegion("0");
		one = CodeBreaker.res.getAtlas("numberpack").findRegion("1");
		two = CodeBreaker.res.getAtlas("numberpack").findRegion("2");
		three = CodeBreaker.res.getAtlas("numberpack").findRegion("3");
		four = CodeBreaker.res.getAtlas("numberpack").findRegion("4");
		five = CodeBreaker.res.getAtlas("numberpack").findRegion("5");
		six = CodeBreaker.res.getAtlas("numberpack").findRegion("6");
		seven = CodeBreaker.res.getAtlas("numberpack").findRegion("7");
		eight = CodeBreaker.res.getAtlas("numberpack").findRegion("8");
		nine = CodeBreaker.res.getAtlas("numberpack").findRegion("9");
		
		tex = nine.getTexture();
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		digit = zero;
		
	}

	public void update() {}
	
	public int getNum() {
		return num;
	}
	
	public void changeNum(int n) {
		num = n;
		length = String.valueOf(n).length();
		digits = getArray(num);
	}
	
	public int[] getArray(int n) {
		
		int[] temp = new int[length];
		for (int i = 1; i <= length; i++) {
			temp[(length - i)] = (n % (int) Math.pow(10, i))/(int) Math.pow(10, (i-1));
		}
		return temp;
	}
	
	public void render (SpriteBatch sb) {
		
		float offset = 0;
		for (int j = 0; j < digits.length; j++) {
			switch(digits[j]) {
				case 0:
					digit = zero;
					break;
				case 1:
					digit = one;
					break;
				case 2:
					digit = two;
					break;
				case 3:
					digit = three;
					break;
				case 4:
					digit = four;
					break;
				case 5:
					digit = five;
					break;
				case 6:
					digit = six;
					break;
				case 7:
					digit = seven;
					break;
				case 8:
					digit = eight;
					break;
				case 9:
					digit = nine;
					break;
				default:
					digit = zero;	
			}
			offset = (j - (length/2.0f))*width;
			sb.draw(digit, x + offset, y - width/2, width, height);
		}
	}
	
	
}
