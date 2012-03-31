package com.DungeonCrawl.Levels;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;

public class Level10 implements Level{
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
			levelText = new TextDisplaying("Level 10",(float)LogicEngine.rect_Screen.getWidth()/2 - (3.5f*16),(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter==100)
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
		
		if(i_stepCounter==BOSS_STEP-100)
			SoundEffects.getInstance().warningThreatApproaching.play(SoundEffects.SPEECH_VOLUME);
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		
		i_stepCounter++;
		
		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}

}
