package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Advantage;
import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.AnimateRollStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.SeekInputTarget;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class WingmenPowerup implements Powerup{


	boolean b_havePlayedSound = false;
	
	@Override
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

	
		//make sure powerup is only fired once

		//remove any existing wingmen
		for(int i=0;i<in_logicEngine.objectsPlayers.size();i++)
			if(in_logicEngine.objectsPlayers.get(i).str_name.equals("wingman"))
			{
					in_logicEngine.objectsPlayers.remove(i);
					i--;
			}
		
		for(int i=0;i<4;i++)
		{
			//spawn wave of wingmen
		
			GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/tinyship.png",(LogicEngine.rect_Screen.getWidth()/2) - (32*i) + 64,50,20);
			ship.i_animationFrame=0;
			ship.i_animationFrameRow=5;
			
			ship.i_animationFrameSizeWidth=16;
			ship.i_animationFrameSizeHeight=16;
			ship.allegiance = GameObject.ALLEGIANCES.PLAYER;
			ship.collisionHandler = new DestroyIfEnemyCollision(ship, 6.0f, true);
			ship.str_name = "wingman";
			
			if(i==0)
				ship.stepHandlers.add(new SeekInputTarget(-40, -0, false));
			if(i==1)
				ship.stepHandlers.add(new SeekInputTarget(-50, -0, true));
			
			if(i==2)
				ship.stepHandlers.add(new SeekInputTarget(40, -0, false));
			if(i==3)
				ship.stepHandlers.add(new SeekInputTarget(50, -0, true));
			
			//spawn the ship at the location of the thing that is collecting the powerup
			ship.v.setX(in_toApplyTo.v.getX());
			ship.v.setY(in_toApplyTo.v.getY());
			
			ship.shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/bullet.png",7.0f,new Vector2d(0,9));
			ship.stepHandlers.add(new AnimateRollStep());
			
			
			ship.v.setMaxForce(3);
			ship.v.setMaxVel(20.0);
			in_logicEngine.objectsPlayers.add(ship);
			
			
			
			//double firing speed on hell
			if(Difficulty.isHard())
				ship.shootEverySteps = (int) (ship.shootEverySteps * 0.75); 
		}
		
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		return;
		
	}

	@Override
	public void collected() {
		if(!b_havePlayedSound)
		{
			SoundEffects.getInstance().wingmen.play(SoundEffects.SPEECH_VOLUME);
			b_havePlayedSound=true;
		}
		
	}

}
