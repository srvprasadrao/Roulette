package com.csun.roulette;

public class BetArea {
	private float x;
	private float y;
	private boolean inside;

	public BetArea(float x, float y, boolean inside) {
		this.x = x;
		this.y = y;
		this.inside = inside;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isInside() {
		return inside;
	}

	public void setInside(boolean inside) {
		this.inside = inside;
	}
}
