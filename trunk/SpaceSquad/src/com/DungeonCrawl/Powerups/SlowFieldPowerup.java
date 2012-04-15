package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;
import com.DungeonCrawl.AreaEffects.SlowAreaEffect;
import com.DungeonCrawl.Steps.AreaFollowObjectStep;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;


public class SlowFieldPowerup implements Powerup{

	AreaFollowObjectStep slowArea;
	SlowAreaEffect slow;
	LogicEngine theLogicEngine;
	
	boolean b_haveApplied = false;
	
	@Override
	public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine) {

		//see if an existing ship has it
		for(int i=0;i<in_logicEngine.objectsPlayers.size();i++)
			for(int j=0;j<in_logicEngine.objectsPlayers.get(i).activePowerups.size();j++)
				if(in_logicEngine.objectsPlayers.get(i).activePowerups.get(j) instanceof SlowFieldPowerup)
				{
					//grow ship
					((SlowFieldPowerup)in_logicEngine.objectsPlayers.get(i).activePowerups.get(j)).growField();
					b_haveApplied = true;
					return;
				}
		
		if(b_haveApplied)
			return;
		else 
			b_haveApplied = true;
		
		theLogicEngine = in_logicEngine;
		
		//create square field
		Rect r = new Rect();
		r.setp1(new Point2d(0, 125));
		r.setp2(new Point2d(125, 0));
	
		//create the image of the field (white square with fade out)
		Drawable d = new Drawable();
		d.str_spritename = "data/"+GameRenderer.dpiFolder+"/redcube.png";
		d.i_animationFrameSizeHeight=80;
		d.i_animationFrameSizeWidth=80;
		d.i_animationFrameRow =0;
		d.i_animationFrame=1;
		
		//create area effect for slowing ships
		slow = new SlowAreaEffect(r,SlowAreaEffect.WhatToSlow.ALL_ENEMIES, d);
		slow.c_Color = new Color(0f,1f,0f,0.5f); //color for the area of effect (NOT THE SHIPS THAT ENTER IT!)
		
		if(in_toApplyTo.allegiance == GameObject.ALLEGIANCES.PLAYER)
			in_logicEngine.currentAreaEffectsPlayers.add(slow);
		else
			in_logicEngine.currentAreaEffects.add(slow);
		
		slowArea = new AreaFollowObjectStep(slow,in_toApplyTo);
		in_toApplyTo.stepHandlers.add(slowArea);
		in_toApplyTo.activePowerups.add(this);
		
	}

	private void growField() {
		this.slow.growArea();
		
	}

	boolean b_havePlayed=false;
	@Override
	public void collected() {
		//TODO add sound
		if(b_havePlayed==false)
		{
			SoundEffects.getInstance().slow.play();
			b_havePlayed= true;
		}
	}
	
	@Override
	public void unApplyPowerup(GameObject in_toApplyTo) {
		
		//TODO make this removable from active powerups
		in_toApplyTo.stepHandlers.remove(slowArea);
		theLogicEngine.currentAreaEffectsPlayers.remove(slow);
		theLogicEngine.currentAreaEffects.remove(slow);
		in_toApplyTo.activePowerups.remove(this);
	}


}
