package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Advantage;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Shooting.FlamerShot;
import com.DungeonCrawl.Shooting.ShotHandler;
import com.DungeonCrawl.Shooting.SplitShot;
import com.DungeonCrawl.Shooting.StraightLineShot;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class DualFirePowerup implements Powerup {

	ShotHandler oldShotHandler;
	private boolean b_havePlayedSound;

	
	
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

	
		
		
		//big ships dont get dual fire
		if(in_toApplyTo.shotHandler == Advantage.bigShot )
			return;
		
		//lazer ships dont get dual fire
		if(in_toApplyTo.shotHandler == Advantage.beamShot )
			return;
	
		//if it already had it don't apply it
		if(in_toApplyTo.shotHandler instanceof SplitShot)
			return;
			
		oldShotHandler = in_toApplyTo.shotHandler; 
		

		
		SplitShot ss = new SplitShot(oldShotHandler.getImagePath(),9.0);
		
		
		 
		if(oldShotHandler instanceof StraightLineShot)
		{
			ss.b_homing = ((StraightLineShot)oldShotHandler).b_homing;
			ss.f_damage = ((StraightLineShot)oldShotHandler).f_damage;
		}
		
		
		//split flame differently
		if(in_toApplyTo.shotHandler instanceof FlamerShot)
		{
			FlamerShot f = (FlamerShot)in_toApplyTo.shotHandler;
			f.a_flameVectors.clear();
			f.a_flameVectors.add(new Vector2d(2.5f,5f));
			f.a_flameVectors.add(new Vector2d(-2.5f,5f));
			
		}
		else
			in_toApplyTo.shotHandler =  ss;
			
		if(!in_toApplyTo.b_ignorePowerupFrameChanges)
			in_toApplyTo.i_animationFrameRow=2;
		
		in_toApplyTo.activePowerups.add(this);
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
		if(in_toApplyTo.shotHandler instanceof FlamerShot)
		{
			FlamerShot f = (FlamerShot)in_toApplyTo.shotHandler;
			f.a_flameVectors.clear();
			f.a_flameVectors.add(new Vector2d(0.0f,5f));
		}
		
		in_toApplyTo.activePowerups.remove(this);
		
		in_toApplyTo.shotHandler = oldShotHandler;
		
		if(!in_toApplyTo.b_ignorePowerupFrameChanges)
			in_toApplyTo.i_animationFrameRow=0;
	}

	@Override
	public void collected() {
		if(!b_havePlayedSound)
		{
			SoundEffects.getInstance().dualfire.play(SoundEffects.SPEECH_VOLUME);
			b_havePlayedSound=true;
		}
		
		
	}
		
		
}
