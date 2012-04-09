package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Shooting.ExplodeIfInRange;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class MinesPowerup implements Powerup{
	
	private Drawable buff_minelayer = new Drawable();
	private boolean b_haveApplied = false;
	LaunchShipsStep s;
	
	public MinesPowerup()
	{
		buff_minelayer.str_spritename = "data/"+GameRenderer.dpiFolder+"/tinyship.png";
		
		buff_minelayer.i_animationFrame=0;
		buff_minelayer.i_animationFrameSizeWidth=16;
		buff_minelayer.i_animationFrameSizeHeight=16;
		buff_minelayer.i_animationFrame = 5;
		buff_minelayer.i_animationFrameRow = 3;
	}

	@Override
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

		if(b_haveApplied )
			return;
		else 
			b_haveApplied = true;
		
		//TODO make it only apply to 2 ships
		if(in_toApplyTo.visibleBuffs.indexOf(buff_minelayer)==-1)
			in_toApplyTo.visibleBuffs.add(buff_minelayer);
		
		GameObject mine = spawnPlayerMine();
		s = new LaunchShipsStep(mine, 40, 1, 1, false);
		s.b_addToBullets = true;
		in_toApplyTo.stepHandlers.add(s);
		
		in_toApplyTo.activePowerups.add(this);
	}
	
	
	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
		
		in_toApplyTo.visibleBuffs.remove(buff_minelayer);
		in_toApplyTo.activePowerups.remove(this);
		in_toApplyTo.stepHandlers.remove(s);
	}

	@Override
	public void collected() {
		//TODO add sound
	}
	

	private static GameObject spawnPlayerMine()
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/tinyship.png" , 0, 0,0);
		//give it seeker behaviour
		
		go.allegiance = GameObject.ALLEGIANCES.PLAYER;
		
		go.i_animationFrame=0;
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrame = 4;
		go.i_animationFrameRow = 3;
		
		
		go.v.setMaxForce(5);
		go.v.setMaxVel(10);
		//seeks players when close otherwise just flies straight down, also animates between frame 1 and 2 every 5 steps
		
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(0,1.0)));
		go.shootEverySteps=1;
		
		//String in_bulletIcon, float in_explodeRange,boolean in_upOnly,float in_bulletSpeed)
		go.shotHandler = new ExplodeIfInRange("data/"+GameRenderer.dpiFolder+"/fragbullet.png",50,false,5f);
		
		return go;
		
	}
	
	
}
