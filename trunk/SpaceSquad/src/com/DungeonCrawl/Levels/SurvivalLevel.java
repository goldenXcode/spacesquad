package com.DungeonCrawl.Levels;

import java.util.ArrayList;
import java.util.Random;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Levels.Level7.CARRIER_TYPE;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Collisions.HitpointShipCollision;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class SurvivalLevel extends BasicLevel {
	
	long l_levelStarted;
	TextDisplaying txt_timer = new TextDisplaying("00:00", LogicEngine.SCREEN_WIDTH/2,  LogicEngine.SCREEN_HEIGHT-20, true);
	TextDisplaying txt_best = new TextDisplaying("Record: 00:00", LogicEngine.SCREEN_WIDTH/2,  LogicEngine.SCREEN_HEIGHT-5, true);
	int i_spawnEasyEvery=20;
	int i_spawnMediumEvery=400;
	int i_spawnHardEvery=1000;
	
	int i_bestScoreEasy;
	int i_bestScoreMedium;
	int i_bestScoreHard;
	
	Random r = new Random();
	
	public SurvivalLevel()
	{
		if(Difficulty.progressArray.containsKey("SURVIVAL_BEST_EASY"))
			i_bestScoreEasy = Integer.parseInt(Difficulty.progressArray.get("SURVIVAL_BEST_EASY"));
		else
			i_bestScoreEasy =0;
	}
	
	@Override
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine) {

		//TODO:survival
		//level text
		if(i_stepCounter==0)
		{
			in_logicEngine.background.setBackground(r.nextInt(3));
			
			in_logicEngine.currentTextBeingDisplayed.add(txt_timer);
			in_logicEngine.currentTextBeingDisplayed.add(txt_best);
			txt_best.toDisplay = "Record: " +  elapsedTimeToString(i_bestScoreEasy);
				
			levelText = new TextDisplaying("Survival Mode",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(levelText);
		}
		
		if(i_stepCounter>=100 && i_stepCounter%i_spawnEasyEvery == 0)
			spawnRandomEasyEnemy(in_manager, in_logicEngine);

		if(i_stepCounter>=100 && i_stepCounter%i_spawnMediumEvery == 0)
			spawnRandomMediumEnemy(in_manager, in_logicEngine);

		if(i_stepCounter>=100 && i_stepCounter%i_spawnHardEvery == 0)
			spawnRandomHardEnemy(in_manager, in_logicEngine);
		
		////////////make harder//////////
		if(i_stepCounter % 500 ==0 && i_stepCounter>1)
		{
			i_spawnEasyEvery = Math.max(i_spawnEasyEvery-1, 1);		
		}
		if( i_stepCounter % 1000 ==0 && i_stepCounter>1 )
		{
			i_spawnMediumEvery = Math.max(i_spawnMediumEvery-30, 100);
			i_spawnHardEvery = Math.max(i_spawnHardEvery-50, 150);
		}
		/////////////////////////////////
		
		//spawn powerups (offensive then defensive)
		if(i_stepCounter%600 == 0)
			in_manager.spawnRandomPowerup(true,in_logicEngine, (float) (Math.random()*LogicEngine.SCREEN_WIDTH));

		if(i_stepCounter%600 == 300)
			in_manager.spawnRandomPowerup(false,in_logicEngine, (float) (Math.random()*LogicEngine.SCREEN_WIDTH));

		
		if(i_stepCounter==100)
		{
			in_logicEngine.currentTextBeingDisplayed.remove(levelText);
			l_levelStarted = System.currentTimeMillis();
		}
		
		if(i_stepCounter > 100)
		{
			//update timer
			int elapsedTime = ((int) (System.currentTimeMillis() - l_levelStarted))/1000;
			
			
			txt_timer.toDisplay = elapsedTimeToString(elapsedTime);
			txt_best.toDisplay = "Record: " +  elapsedTimeToString(i_bestScoreEasy);
			
			
			//TODO add other difficulties
			//update score
			if(elapsedTime > i_bestScoreEasy)
			{
				i_bestScoreEasy = elapsedTime;
				Difficulty.progressArray.put("SURVIVAL_BEST_EASY", ""+i_bestScoreEasy);
			}
		}		
		
		
		if(b_bossSpawned && in_logicEngine.objectsEnemies.indexOf(boss)==-1 && in_logicEngine.objectsObstacles.indexOf(boss)==-1)
		{
			i_levelEndCountdown--;
		}
		
		i_stepCounter++;
		
		
		
		if(i_levelEndCountdown==0)
			return true;
		
		return false;
	}
	private String elapsedTimeToString(int in_elapsedTime)
	{
		String str_timer = ""; 
		
		//add leading 0 for minutes
		if(in_elapsedTime/60<10)
			str_timer += "0";
		
		//add minutes
		str_timer += (in_elapsedTime/60) + ":";
		
		if(in_elapsedTime%60<10)
			str_timer += "0";
		
		str_timer+= in_elapsedTime%60;
		
		return str_timer;
	}
	@Override
	public void gameOver(LogicEngine in_logicEngine)
	{
		Difficulty.saveProgress();
	}



	private void spawnRandomEasyEnemy(LevelManager in_manager, LogicEngine in_logicEngine)
	{
		switch((int)(Math.random()*8f))
		{
			case 0: 
				//asteroid
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
				in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
			break;
			case 1: 
				//slime
				in_manager.spawnSlime(in_logicEngine,(float)(Math.random()*320.0+1.0f));
			break;
			case 2: 
				//seeker
				in_manager.spawnSeeker(in_logicEngine, (float)(Math.random()*320.0+1.0f));
			break;
			case 3: 
				//baterang
				FlyStraightStep fs = new FlyStraightStep(new Vector2d(0,-5));
				fs.setIsAccelleration(true);
				in_manager.spawnBaterang(in_logicEngine,  (float)(Math.random()*320.0f) , LogicEngine.f_worldCoordTop+25,fs);
				break;
			case 4:
				//mine
				in_manager.mineWave(in_logicEngine,1, false,true);
				break;
			case 5:
				//bubble
				in_manager.spawnBubble(in_logicEngine, (float) (Math.random() * in_logicEngine.SCREEN_WIDTH));
				break;
			case 6:
				//turret ship
				in_manager.spawnTurretShip(in_logicEngine,  (float)(Math.random()*320.0f));
				break;
			case 7:
				in_manager.pathFinderStraightDown(in_logicEngine, (int)(Math.random()*320.0f));
				in_manager.pathFinderStraightDown(in_logicEngine, (int)(Math.random()*320.0f));
				in_manager.pathFinderStraightDown(in_logicEngine, (int)(Math.random()*320.0f));
			
			case 8:
				//three red triangles
				in_manager.spawnWanderingSeeker(in_logicEngine);
				in_manager.spawnWanderingSeeker(in_logicEngine);
				in_manager.spawnWanderingSeeker(in_logicEngine);
				break;	
		}
	}
	
	private void spawnRandomMediumEnemy(LevelManager in_manager, LogicEngine in_logicEngine)
	{
		switch((int)(Math.random()*2f))
		{
		case 0:
			//beam ship (satelite ship)
			in_manager.spawnSateliteShip(in_logicEngine, (float)(Math.random()*320.0f));
			break;
		case 1:
			//splitter
			in_manager.spawnSplitter(in_logicEngine);
			break;

		}
				
	
	}
	
	
	private void spawnRandomHardEnemy(LevelManager in_manager,
			LogicEngine in_logicEngine) {
		switch((int)(Math.random()*2f))
		{
		case 0:
			GameObject ship1 = in_manager.spawnCarrier(in_logicEngine, (float)(Math.random()*320.0f),CARRIER_TYPE.PATHFINDERS_BOTH_SIDES);
			((HitpointShipCollision)(ship1.collisionHandler)).f_numberOfHitpoints=30f;
			break;
		case 1:
			GameObject ship2 = in_manager.spawnWaveRider(in_logicEngine, (float)(Math.random()*320.0f),2);
			((HitpointShipCollision)(ship2.collisionHandler)).f_numberOfHitpoints=30f;
			break;
		}
	}
}

