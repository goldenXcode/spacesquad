package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public interface ShotHandler 
{
	public String getImagePath();
	
	

	public void shoot(LogicEngine in_toShootIn,GameObject in_objectFiring);
	

}
