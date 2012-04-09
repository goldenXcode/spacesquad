package com.DungeonCrawl.Levels;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.TextDisplaying;

public class Level9 implements Level{
	LevelManager levelManager;
	TextDisplaying levelText;
	int i_stepCounter=0;
	boolean b_bossSpawned;
	final int BOSS_STEP =3200;
	int i_levelEndCountdown=100;
	
	GameObject boss=null;
	
	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		
		
		//level text
		if(i_stepCounter==0)
		{
			levelText = new TextDisplaying("Level 9",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		
		i_stepCounter++;

		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	@Override
	public void gameOver(LogicEngine in_logicEngine) {

		
	}
}
