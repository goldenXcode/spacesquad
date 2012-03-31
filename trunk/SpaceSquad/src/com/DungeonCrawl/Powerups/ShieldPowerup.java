package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Collisions.PlayerCollision;

public class ShieldPowerup implements Powerup
{

		
	private boolean b_havePlayedSound;
	Drawable d_shield = new Drawable();
	
	@Override
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {
		
	
		
		for(int i=0 ; i<in_toApplyTo.activePowerups.size();i++)
			if(in_toApplyTo.activePowerups.get(i) instanceof ShieldPowerup)
			{
				//already has a shield powerup
				return;
			}
		
		//if its not a real player it wont have a playercollision
		if(in_toApplyTo.collisionHandler instanceof PlayerCollision ==false)
			return;
		
		

		
		PlayerCollision pc = (PlayerCollision)in_toApplyTo.collisionHandler;
		pc.setShielded(true);
	
		
		
		d_shield.i_animationFrameRow = 3;
		d_shield.i_animationFrameSizeHeight = 16;
		d_shield.i_animationFrameSizeWidth = 16;
		d_shield.str_spritename="data/"+GameRenderer.dpiFolder+"/tinyship.png";
		
		
		in_toApplyTo.visibleBuffs.add(d_shield); 
		
		in_toApplyTo.activePowerups.add(this);
		
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
	
	
		in_toApplyTo.visibleBuffs.remove(d_shield);
		in_toApplyTo.activePowerups.remove(this);
		
		PlayerCollision pc = (PlayerCollision)in_toApplyTo.collisionHandler;
		pc.setShielded(false);
	}

	@Override
	public void collected() {
		if(!b_havePlayedSound)
		{
			SoundEffects.getInstance().shield.play(SoundEffects.SPEECH_VOLUME);
			b_havePlayedSound=true;
		}
		
	}

}
