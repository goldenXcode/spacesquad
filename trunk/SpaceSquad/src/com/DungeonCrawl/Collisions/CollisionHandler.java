package com.DungeonCrawl.Collisions;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Steps.StepHandler;

public interface CollisionHandler extends StepHandler{
	
	//returns true if to destroy 'this' - note that you cannot destroy the thing you are colliding with
	public boolean handleCollision(GameObject collidingWith,LogicEngine toRunIn);
	public double getCollisionRadius();
	public CollisionHandler cloneForShip(GameObject in_ship) throws CloneNotSupportedException;
	public boolean handleDestroy(LogicEngine toRunIn);
}
