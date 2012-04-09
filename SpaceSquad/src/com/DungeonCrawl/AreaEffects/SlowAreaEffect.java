package com.DungeonCrawl.AreaEffects;

import java.util.ArrayList;
import java.util.Iterator;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect.Effect;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Powerups.MovementPowerup;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Steps.TimedLifePowerupStep;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Rect;

public class SlowAreaEffect extends AreaEffect {

	public enum WhatToSlow
	{
		ENEMY_BULLETS, ALL_ENEMIES
		
	}
	
	MovementPowerup debuff;
	WhatToSlow toSlow;
	Drawable slimeDebuff;
	
	public SlowAreaEffect(Rect in_area,WhatToSlow in_toSlow, Drawable in_d) {
		super(in_area, in_d);
		
		toSlow = in_toSlow;
		 slimeDebuff = new Drawable();

		slimeDebuff.i_animationFrameRow = 1;
		slimeDebuff.i_animationFrameSizeWidth = 16;
		slimeDebuff.i_animationFrameSizeHeight = 16;
		
		slimeDebuff.str_spritename = "data/"+GameRenderer.dpiFolder+"/slime.png";
		
		debuff = new MovementPowerup(.75f,new Color(0f,1f,0f,1f));
			
	}
	
	@Override
	public void stepEffect(LogicEngine in_logicEngine)
	{
		super.stepEffect(in_logicEngine);
	
		
		if(toSlow == WhatToSlow.ENEMY_BULLETS || toSlow == WhatToSlow.ALL_ENEMIES)
			slowList(in_logicEngine.objectsEnemyBullets,in_logicEngine);
		
		if(toSlow == WhatToSlow.ALL_ENEMIES)
			slowList(in_logicEngine.objectsEnemies,in_logicEngine);
		
		if(toSlow == WhatToSlow.ALL_ENEMIES)
			slowList(in_logicEngine.objectsObstacles,in_logicEngine);
	}
	
	private void slowList(ArrayList<GameObject> listToSlow, LogicEngine in_logicEngine) {
		for(int i=0;i<listToSlow.size();i++)
		{
			GameObject enemy = listToSlow.get(i);
			
			//if in area
			if(area.inRect(enemy.v.getPos()))
				if(!b_shipIsAlreadySlowed(enemy))
				{
					MovementPowerup debuffcopy = (MovementPowerup) debuff.clone();
					
					//if it doesnt already have a slow debuff, give it one
					debuffcopy.applyPowerup(enemy, in_logicEngine);
					enemy.stepHandlers.add(new TimedLifePowerupStep(3,debuffcopy));
				}
		}
		
	}

	private boolean b_shipIsAlreadySlowed(GameObject enemy)
	{
		for(int i=0;i<enemy.activePowerups.size();i++)
			if(enemy.activePowerups.get(i) instanceof MovementPowerup)
				return true;
		
		return false;
	}
	
	
}
