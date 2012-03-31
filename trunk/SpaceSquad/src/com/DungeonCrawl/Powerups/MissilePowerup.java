package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.AnimateRollStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.SeekInputTarget;
import com.DungeonCrawl.Steps.SeekNearestEnemyStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class MissilePowerup implements Powerup {

	GameObject go_missile = null;
	private static Drawable d_leftLauncher;
	private static Drawable d_rightLauncher;
	
	
	 LaunchShipsStep leftLaunch;
	 LaunchShipsStep rightLaunch;
	private boolean b_havePlayedSound=false;
	
	public MissilePowerup()
	{
		if(d_leftLauncher == null)
		{
			d_leftLauncher = new Drawable();
			d_rightLauncher = new Drawable();
			d_leftLauncher.str_spritename = "data/"+GameRenderer.dpiFolder+"/warpin.png";
			d_leftLauncher.i_animationFrame = 2;
			d_leftLauncher.i_animationFrameRow = 2;
			d_leftLauncher.i_animationFrameSizeHeight = 16;
			d_leftLauncher.i_animationFrameSizeWidth = 16;
			
			d_rightLauncher.str_spritename = "data/"+GameRenderer.dpiFolder+"/warpin.png";
			d_rightLauncher.i_animationFrame = 3;
			d_rightLauncher.i_animationFrameRow = 2;
			d_rightLauncher.i_animationFrameSizeHeight = 16;
			d_rightLauncher.i_animationFrameSizeWidth = 16;
		}
		
		go_missile = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",0,50,20);
		go_missile.i_animationFrame=1;
		go_missile.i_animationFrameRow=1;
		
		go_missile.i_animationFrameSizeWidth=16;
		go_missile.i_animationFrameSizeHeight=16;
		go_missile.allegiance = GameObject.ALLEGIANCES.PLAYER;
		go_missile.collisionHandler = new DestroyIfEnemyCollision(go_missile, 6.0f, true);
		go_missile.str_name = "missile";
				
		SeekNearestEnemyStep seekstep = new SeekNearestEnemyStep(1000,1.0);
		seekstep.v_noTarget = new Vector2d(0,5);
		
		go_missile.stepHandlers.add(seekstep);

			
		leftLaunch = new LaunchShipsStep(go_missile,50,1,1,false);
		rightLaunch = new LaunchShipsStep(go_missile,50,1,1,true);
		leftLaunch.b_addToBullets = true;
		rightLaunch.b_addToBullets = true;
		
		go_missile.rotateToV = true;
		
		go_missile.v.setMaxForce(0.5);
		go_missile.v.setMaxVel(17.0);
		
	}
	
	@Override
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {
			
		//if it already has a missile powerup, dont give it another one
		for(int i=0;i<in_toApplyTo.activePowerups.size();i++)
			if(in_toApplyTo.activePowerups.get(i) instanceof MissilePowerup)
				return;
			
			
		if(Utils.getPlayerShipIndex(in_logicEngine,in_toApplyTo) == 1)
		{
				
			in_toApplyTo.stepHandlers.add(leftLaunch);
			//do not convert big shot ships icon but still give missiles
			in_toApplyTo.visibleBuffs.add(d_leftLauncher);
		}
		else
			if(Utils.getPlayerShipIndex(in_logicEngine,in_toApplyTo) == 3)
			{
			
				
				in_toApplyTo.stepHandlers.add(rightLaunch);
				in_toApplyTo.visibleBuffs.add(d_rightLauncher);
			}
			else 
				return;
		
		in_toApplyTo.activePowerups.add(this);
		
	
		
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {

		in_toApplyTo.activePowerups.remove(this);
		
		in_toApplyTo.stepHandlers.remove(leftLaunch);
		in_toApplyTo.stepHandlers.remove(rightLaunch);
				
		in_toApplyTo.visibleBuffs.remove(d_leftLauncher);
		in_toApplyTo.visibleBuffs.remove(d_rightLauncher);
	}

	@Override
	public void collected() {
		if(!b_havePlayedSound)
		{
			SoundEffects.getInstance().missiles.play(SoundEffects.SPEECH_VOLUME);
			b_havePlayedSound=true;
		}
		
	}

}
