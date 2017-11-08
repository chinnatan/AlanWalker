package com.alanwalker.entities;

public enum DIRECTION {
	
	NORTH(0, 0.5f),
	SOUTH(0, -0.5f),
	EAST(0.5f, 0),
	WEST(-0.5f, 0),
	;
	
	private float dx, dy;
	
	private DIRECTION(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public float getDx() {
		return dx;
	}

	public float getDy() {
		return dy;
	}
}
