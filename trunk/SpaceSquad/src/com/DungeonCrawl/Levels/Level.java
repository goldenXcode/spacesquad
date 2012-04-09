package com.DungeonCrawl.Levels;

import com.DungeonCrawl.LogicEngine;

public interface Level 
{



	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine);
	
	public void gameOver(LogicEngine in_logicEngine);

}
