package com.DungeonCrawl.Shooting;

import java.util.ArrayList;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class FlamerShot implements ShotHandler{

	GameObject go_flame;
	
	public FlamerShot(GameObject go_firing)
	{
		go_flame = new  GameObject("data/"+GameRenderer.dpiFolder+"/projectiles.png",0,0,0);
		go_flame.collisionHandler = new HitpointShipCollision(go_flame,0.80f,10f,false);
		go_flame.i_animationFrame =0;
		go_flame.i_animationFrameSizeWidth =8;
		go_flame.i_animationFrameSizeHeight =8;
		go_flame.stepHandlers.add(new StaticAnimationStep(7,3, 0));
	
		//TODO : b_advantages - dual fire, give dual fire effect
		if( go_firing.allegiance == GameObject.ALLEGIANCES.PLAYER)
			a_flameVectors.add(new Vector2d(0,5f));
		else
			a_flameVectors.add(new Vector2d(0,-5f));
	}
	
	public ArrayList<Vector2d> a_flameVectors = new ArrayList<Vector2d>();
	
	@Override
	public void shoot(LogicEngine in_toShootIn, GameObject in_objectFiring) {
		

		//for each flame vector
		for(int i=0;i<a_flameVectors.size();i++)
		{
			GameObject go_clone = go_flame.clone();
		
		
			go_clone.allegiance = in_objectFiring.allegiance;
		
			Vector2d currentVector = a_flameVectors.get(i);
			
			if(go_clone.allegiance == GameObject.ALLEGIANCES.PLAYER)
			{
				
				//player flame so go straight up
				go_clone.v.setX(in_objectFiring.v.getX() );
				go_clone.v.setY(in_objectFiring.v.getY() + (in_objectFiring.i_animationFrameSizeHeight/2));
				go_clone.stepHandlers.add(new FlyStraightStep(currentVector));
				in_toShootIn.objectsPlayerBullets.add(go_clone);
			}
			else
			{
				//enemy flame so go straight down
				go_clone.v.setX(in_objectFiring.v.getX() );
				go_clone.v.setY(in_objectFiring.v.getY() + (in_objectFiring.i_animationFrameSizeHeight/2));
				go_clone.stepHandlers.add(new FlyStraightStep(currentVector));
				
				in_toShootIn.objectsEnemyBullets.add(go_clone);
			}
		}
		
	}

	@Override
	public String getImagePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
