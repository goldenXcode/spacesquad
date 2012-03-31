package com.DungeonCrawl.Levels;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Difficulty.DIFFICULTY;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.Steps.StaticAnimationStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Level0 implements Level {
	int i_stepCounter=0;
	
		
	GameObject go_arrowLeft;
	GameObject go_arrowRight;
	GameObject go_arrowLives;
	TextDisplaying level0Text;
	
	
	public boolean stepLevel(LevelManager in_manager, LogicEngine in_logicEngine)
	{
		if(i_stepCounter==0)
		{
			in_logicEngine.background.setBackground(0);
			
			in_manager.spawnDifficultyIcon(in_logicEngine);
			
			String msg = "Welcome To";
			String msg2 =  "Space Squad";
			in_logicEngine.currentTextBeingDisplayed.add(new TextDisplaying( msg, (float)(LogicEngine.rect_Screen.getWidth()/2) , (float)LogicEngine.rect_Screen.getHeight()/1.3f,true));
			in_logicEngine.currentTextBeingDisplayed.add(new TextDisplaying( msg2, (float)(LogicEngine.rect_Screen.getWidth()/2) , -32f+(float)LogicEngine.rect_Screen.getHeight()/1.3f,true));
		}
		if(i_stepCounter ==1)
		{
			SoundEffects.getInstance().intro.play();
		}
		
		//make sure player doesn't die
		if(Difficulty.difficulty == DIFFICULTY.EASY)
			if( in_logicEngine.MyLifeCounter.i_livesRemaining <5)
				in_logicEngine.MyLifeCounter.addLives(20);
		
		
		if(i_stepCounter==100)
		{
			String msg = "These Are Your Ships:";
						
			
			in_logicEngine.currentTextBeingDisplayed.add(new TextDisplaying( msg, (float)(LogicEngine.rect_Screen.getWidth()/2) , (float)LogicEngine.rect_Screen.getHeight()/3f,true));
					
			
			go_arrowLeft = new GameObject("data/"+GameRenderer.dpiFolder+"/arrows.png",-32+(float)(LogicEngine.rect_Screen.getWidth()/3),-32+(float)LogicEngine.rect_Screen.getHeight()/3f,0);
			go_arrowLeft.i_animationFrame=0;
			go_arrowLeft.i_animationFrameSizeWidth=32;
			go_arrowLeft.i_animationFrameSizeHeight=32;
			go_arrowLeft.v.setX(LogicEngine.rect_Screen.getWidth()/2 - 32);
			go_arrowLeft.v.setY(LogicEngine.rect_Screen.getHeight()/3 - 64);
			go_arrowLeft.allegiance = GameObject.ALLEGIANCES.NONE;
			in_logicEngine.objectsUnderlay.add(go_arrowLeft);
			
			go_arrowRight = new GameObject("data/"+GameRenderer.dpiFolder+"/arrows.png",-32+(float)(LogicEngine.rect_Screen.getWidth()/3),-32+(float)LogicEngine.rect_Screen.getHeight()/3f,0);
			go_arrowRight.i_animationFrame=1;
			go_arrowRight.i_animationFrameSizeWidth=32;
			go_arrowRight.i_animationFrameSizeHeight=32;
			go_arrowRight.v.setX(LogicEngine.rect_Screen.getWidth()/2 + 32);
			go_arrowRight.v.setY(LogicEngine.rect_Screen.getHeight()/3 - 64);
			go_arrowRight.allegiance = GameObject.ALLEGIANCES.NONE;
			in_logicEngine.objectsUnderlay.add(go_arrowRight);
			
			//,(double)(LogicEngine.rect_Screen.getWidth()/2)+100,(double)LogicEngine.rect_Screen.getHeight()-32f,0)
			go_arrowLives = new GameObject("data/"+GameRenderer.dpiFolder+"/arrows.png",(double)(LogicEngine.rect_Screen.getWidth()/2.0) +100.0, (double)LogicEngine.rect_Screen.getHeight()-32.0,0);
			go_arrowLives.i_animationFrame = 1;
			go_arrowLives.i_animationFrameRow=1;
			go_arrowLives.i_animationFrameSizeHeight = 32;
			go_arrowLives.i_animationFrameSizeWidth = 32;
			go_arrowLives.allegiance = GameObject.ALLEGIANCES.NONE;
		}
		
		//flash
		if(i_stepCounter>=131 && i_stepCounter<=239)
		{
			if(i_stepCounter%20==0)
			{
			
				in_logicEngine.objectsUnderlay.remove(go_arrowRight);
				in_logicEngine.objectsUnderlay.remove(go_arrowLeft);
			}
			
			
			if(i_stepCounter%20==10)
			{
				in_logicEngine.objectsUnderlay.add(go_arrowRight);
				in_logicEngine.objectsUnderlay.add(go_arrowLeft);
			}
		}
		
		if(i_stepCounter>=250 && i_stepCounter <=350 )
		{
			//scroll text off the bottom of the screen
			for(int i=0; i<in_logicEngine.currentTextBeingDisplayed.size();i++)
				in_logicEngine.currentTextBeingDisplayed.get(i).y -=5 ;
			
			for(int i=0; i<in_logicEngine.objectsUnderlay.size();i++)
				in_logicEngine.objectsUnderlay.get(i).v.setY(in_logicEngine.objectsUnderlay.get(i).v.getY()-5);
		}
		
		//explain lives
		if(i_stepCounter==400)
		{
			in_logicEngine.currentTextBeingDisplayed.add(new TextDisplaying("Replacement Ships", (in_logicEngine.SCREEN_WIDTH/2)  , in_logicEngine.SCREEN_HEIGHT-32, true));
			in_logicEngine.objectsUnderlay.add(go_arrowLives);
		}
		
		if(i_stepCounter==500)
		{
			//clear intro from memory
			in_logicEngine.currentTextBeingDisplayed.clear();
			in_logicEngine.objectsUnderlay.clear();
			
			level0Text = new TextDisplaying("Level 0",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight()/2,true);
			
			in_logicEngine.currentTextBeingDisplayed.add(level0Text);
		}
		
		if(i_stepCounter==600)
			in_logicEngine.currentTextBeingDisplayed.remove(level0Text);
		
		if(i_stepCounter==600)
			SoundEffects.getInstance().intro2.play();
		
		//spawn boulders
		if(i_stepCounter>650 && i_stepCounter<1000 &&i_stepCounter%10==0)
			in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
		
		//spawn clouds all the time
		if(i_stepCounter>650 && i_stepCounter%40 ==0)
			in_manager.spawnCloud(in_logicEngine, (int)(Math.random()*3.0f+1.0f),(float)(Math.random()*320.0+1.0f));
		
		if(i_stepCounter==1000)
		{
			//multitouch
			level0Text = new TextDisplaying("Try Multitouch",(float)LogicEngine.rect_Screen.getWidth()/2 ,(float)LogicEngine.rect_Screen.getHeight(),true);
			
			in_manager.spawnRandomPowerup(in_logicEngine, (float)in_logicEngine.rect_Screen.getWidth()-50f);
			in_manager.spawnRandomPowerup(in_logicEngine, 50f);
			
			in_logicEngine.currentTextBeingDisplayed.add(level0Text);
		}

		if(i_stepCounter>=1000 && i_stepCounter<=1200)
		{
			//clear intro from memory
			level0Text.y -=5.0f;
		}
		
		if(i_stepCounter>=1200 && i_stepCounter<=1600 && i_stepCounter%8==0)
			in_manager.spawnAsteroid(in_logicEngine, (int)(Math.random()*10.0f),(float)(Math.random()*320.0+1.0f),false);
		
	
	

		if(i_stepCounter>=1610 && i_stepCounter<=2000)
		{
			//clear intro from memory
			level0Text.y -=5.0f;
		}
		
		if(i_stepCounter==2000)
			in_logicEngine.currentTextBeingDisplayed.remove(level0Text);

		//--------------------------------		
		//seekers
		//--------------------------------
		if(i_stepCounter == 1600)
			SoundEffects.getInstance().intro3.play();
		
		if(i_stepCounter>1700 && i_stepCounter<2000 &&i_stepCounter%20==0)
		{
			in_manager.spawnSeeker(in_logicEngine,  (float)(Math.random()*320.0+1.0f));
			in_manager.spawnSeeker(in_logicEngine,  (float)(Math.random()*320.0+1.0f));
		}	
		
		if(i_stepCounter==2200)
		{
			level0Text = new TextDisplaying("Tutorial Complete",(float)LogicEngine.rect_Screen.getWidth()/2,(float)LogicEngine.rect_Screen.getHeight()/1.4f,true);
			in_logicEngine.currentTextBeingDisplayed.add(level0Text);
			
			//blow up enemies
			for(int i=0;i<in_logicEngine.objectsEnemies.size();i++)
			{
				//spawn an explosion
				GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",in_logicEngine.objectsEnemies.get(i).v.getPos().getX(),in_logicEngine.objectsEnemies.get(i).v.getPos().getY(),0);
			
				explosion.i_animationFrame =0;
				explosion.i_animationFrameSizeWidth =16;
				explosion.i_animationFrameSizeHeight=16;
				explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
				in_logicEngine.objectsOverlay.add(explosion);
				//TODO play Big explosion!
				SoundEffects.getInstance().explosion.play();
			}
			
			in_logicEngine.objectsEnemies.clear();
			
		
		}
		
		
		i_stepCounter++;
		
		
		if(i_stepCounter==2300)
		{
			in_logicEngine.currentTextBeingDisplayed.remove(level0Text);
			//end of level
			return true;
		}
		
		//not end of level yet
		return false;
	}

}
