package com.alanwalker.util;

import java.util.HashMap;
import java.util.Map;

import com.alanwalker.entities.DIRECTION;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSet {
	
	private Map<DIRECTION, Animation> walking;
	private Map<DIRECTION, TextureRegion> standing;
	
	public AnimationSet(Animation walkNorth, Animation walkSouth, Animation walkEast, Animation walkWest, TextureRegion standNorth, TextureRegion standSouth, TextureRegion standEast, TextureRegion standWest) {
		walking = new HashMap<DIRECTION, Animation>();
		walking.put(DIRECTION.NORTH, walkNorth);
		walking.put(DIRECTION.SOUTH, walkSouth);
		walking.put(DIRECTION.EAST, walkEast);
		walking.put(DIRECTION.WEST, walkWest);
		standing = new HashMap<DIRECTION, TextureRegion>();
		standing.put(DIRECTION.NORTH, standNorth);
		standing.put(DIRECTION.SOUTH, standSouth);
		standing.put(DIRECTION.EAST, standEast);
		standing.put(DIRECTION.WEST, standWest);
	}
	
	public Animation getWalking(DIRECTION dir) {
		return walking.get(dir);
	}
	
	public TextureRegion getStanding(DIRECTION dir) {
		return standing.get(dir);
	}
	
//	private Animation walkNorth, walkSouth, walkEast, walkWest;
//	private TextureRegion standNorth, standSouth, standEast, standWest;
//	
//	public AnimationSet(Animation walkNorth, Animation walkSouth, Animation walkEast, Animation walkWest, TextureRegion standNorth, TextureRegion standSouth, TextureRegion standEast, TextureRegion standWest) {
//		this.walkEast = walkEast;
//		this.walkNorth = walkNorth;
//		this.walkSouth = walkSouth;
//		this.walkWest = walkWest;
//		this.standEast = standEast;
//		this.standNorth = standNorth;
//		this.standSouth = standSouth;
//		this.standWest = standWest;
//	}
//
//	public Animation getWalkNorth() {
//		return walkNorth;
//	}
//
//	public Animation getWalkSouth() {
//		return walkSouth;
//	}
//
//	public Animation getWalkEast() {
//		return walkEast;
//	}
//
//	public Animation getWalkWest() {
//		return walkWest;
//	}
//
//	public TextureRegion getStandNorth() {
//		return standNorth;
//	}
//
//	public TextureRegion getStandSouth() {
//		return standSouth;
//	}
//
//	public TextureRegion getStandEast() {
//		return standEast;
//	}
//
//	public TextureRegion getStandWest() {
//		return standWest;
//	}
}
