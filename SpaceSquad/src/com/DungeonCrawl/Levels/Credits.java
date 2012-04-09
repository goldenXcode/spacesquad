package com.DungeonCrawl.Levels;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.TextDisplaying;

public class Credits implements Level {
	

		LevelManager levelManager;
		TextDisplaying levelText;
		int i_stepCounter=0;
		boolean b_bossSpawned;
		final int BOSS_STEP =3200;
		int i_levelEndCountdown=100;
		
		GameObject boss=null;
		
		
		TextDisplaying lines[] = new TextDisplaying[10];
		
		
		
		@Override
		public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

			if(i_stepCounter == 1300)
			{
				in_logicEngine.currentTextBeingDisplayed.clear();
				i_stepCounter = 0;
			}
			
			//level text
			if(i_stepCounter==0)
			{
				in_logicEngine.currentTextBeingDisplayed.remove(levelText);
				levelText = new TextDisplaying("Thanks for playing",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
				
				in_logicEngine.currentTextBeingDisplayed.add(levelText);
			}
			
			//level text
			if(i_stepCounter==150)
			{
				in_logicEngine.currentTextBeingDisplayed.remove(levelText);
				levelText = new TextDisplaying("Why not post a review?",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
				
				in_logicEngine.currentTextBeingDisplayed.add(levelText);
			}
			
			
			//level text
			if(i_stepCounter==300)
			{
				in_logicEngine.currentTextBeingDisplayed.remove(levelText);
				levelText = new TextDisplaying("Stand by for more levels in future",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/2,true);
				
				in_logicEngine.currentTextBeingDisplayed.add(levelText);
			}
			
			
			if(i_stepCounter == 450)
			{
				in_logicEngine.currentTextBeingDisplayed.remove(levelText);
				lines[0] = new TextDisplaying("Credits",(float)LogicEngine.rect_Screen.getWidth()/2,0,true); 
				lines[1] = new TextDisplaying("Game by Thomas Nind",(float)LogicEngine.rect_Screen.getWidth()/2,-30,true);
				lines[2] = new TextDisplaying("Music by Alex McDonald",(float)LogicEngine.rect_Screen.getWidth()/2,-60,true);
				lines[3] = new TextDisplaying("Artwork by:",(float)LogicEngine.rect_Screen.getWidth()/2,-90,true);
				lines[4] = new TextDisplaying("Thomas Nind",(float)LogicEngine.rect_Screen.getWidth()/2,-120,true);
				lines[5] = new TextDisplaying("MHX_Air",(float)LogicEngine.rect_Screen.getWidth()/2,-150,true);
				//lines[6] = new TextDisplaying("Credits",(float)LogicEngine.rect_Screen.getWidth()/2,180,true);
				//lines[7] = new TextDisplaying("Credits",(float)LogicEngine.rect_Screen.getWidth()/2,210,true);
				
				for(int i=0 ; i< 10 ;i++)
					if(lines[i] != null)
						in_logicEngine.currentTextBeingDisplayed.add(lines[i]);
			}
			
			if(i_stepCounter >450) 
				for(int i=0 ; i< 10 ;i++)
					if(lines[i] != null)
						lines[i].y +=1;
				
			
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
