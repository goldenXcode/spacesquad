package com.DungeonCrawl.Levels;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;

public abstract class BasicLevel implements Level
{
	TextDisplaying levelText;
	int i_stepCounter=0;
	int BOSS_STEP =3000;
	int i_levelEndCountdown=100;
	boolean b_bossSpawned;
	int i_levelNumber;
	
	GameObject boss=null;
	
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {
		//level text
		if(i_stepCounter==0)
		{
			SoundEffects.getInstance().levels[i_levelNumber].play(SoundEffects.SPEECH_VOLUME);
			
			levelText = new TextDisplaying("Level " + i_levelNumber,(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1 && in_logicEngine.objectsObstacles.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		
		i_stepCounter++;
		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
		
		
	}
	
}
