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
		
		debuff = new MovementPowerup(.50f,new Color(0.5f,1f,0.5f,1f));
			
	}
	
	public void growArea()
	{
		this.area.getp1().setX( this.area.getp1().getX() - (0.25 * this.area.getWidth()));
		this.area.getp2().setX( this.area.getp2().getX() + (0.25 * this.area.getWidth()));
		this.area.getp1().setY( this.area.getp1().getY() - (0.25 * this.area.getHeight()));
		this.area.getp2().setY( this.area.getp2().getY() + (0.25 * this.area.getHeight()));
		this.c_Color.a =this.c_Color.a/2; 
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
					enemy.stepHandlers.add(new TimedLifePowerupStep(10,debuffcopy));
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
