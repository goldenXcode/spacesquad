package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Advantage;
import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;

public class RapidFirePowerup implements Powerup{

	private boolean b_shootEveryWasOdd=false;
	private boolean b_wasAlready1=false;
	private boolean b_havePlayedSound;
	
	///Must only apply to players
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

		
		
		//if it is an odd number integer division will drop 
		if(in_toApplyTo.shootEverySteps%2 ==1)
			b_shootEveryWasOdd=true;
		//dont go below 10 on normal
		if(in_toApplyTo.shootEverySteps<=10)
		{
			b_wasAlready1=true;
			return;
		}	
		
		//do not convert big shot ships icon but still speed up shot
		if(!in_toApplyTo.b_ignorePowerupFrameChanges )
			in_toApplyTo.i_animationFrameRow=1;
		
		in_toApplyTo.shootEverySteps =in_toApplyTo.shootEverySteps-3; 
		in_toApplyTo.activePowerups.add(this);
	}

	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
		in_toApplyTo.activePowerups.remove(this);
		
		if(b_wasAlready1)
			return;
	
		in_toApplyTo.shootEverySteps =in_toApplyTo.shootEverySteps+3;
		
		//do not convert big shot ships icon but still speed up shot
		if(!in_toApplyTo.b_ignorePowerupFrameChanges)
			in_toApplyTo.i_animationFrameRow=0;
		
		if(b_shootEveryWasOdd)
			in_toApplyTo.shootEverySteps++;
		
	}

	@Override
	public void collected() {
		 if(!b_havePlayedSound)
		{
			SoundEffects.getInstance().rapidfire.play(SoundEffects.SPEECH_VOLUME);
			b_havePlayedSound=true;
		}
		
	}
	
	

}
