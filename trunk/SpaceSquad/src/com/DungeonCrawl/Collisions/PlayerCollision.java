package com.DungeonCrawl.Collisions;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Powerups.ShieldPowerup;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class PlayerCollision implements CollisionHandler 
{

	GameObject playerShip;
	
	public PlayerCollision(GameObject in_playerShip) {
		playerShip = in_playerShip;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean handleCollision(GameObject collidingWith, LogicEngine toRunIn) {
		
				
		//if hitting a different allegiance ship and not a NONE
		if(collidingWith.allegiance != GameObject.ALLEGIANCES.NONE && collidingWith.allegiance != GameObject.ALLEGIANCES.PLAYER)
		{
			//collided wih enemy 
			if(b_isShielded)
			{
				for(int i=0;i<playerShip.activePowerups.size();i++)
					if(playerShip.activePowerups.get(i) instanceof ShieldPowerup)
						{
							playerShip.activePowerups.get(i).unApplyPowerup(playerShip);
							//apply a bounce
							Vector2d incommingVelocity = new Vector2d(collidingWith.v.getVel());
							incommingVelocity.scale(25.0);
							playerShip.v.addForce(incommingVelocity);
							
							
							return false;
						}
			}
				
			return handleDestroy(toRunIn);
		
		}
		
		return false;
	}

	@Override
	public double getCollisionRadius() {

		return 6.0;
	}
	
	boolean b_isShielded=false;
	public void setShielded(boolean in_isShielded) {
		b_isShielded=in_isShielded;
		
	}

	@Override
	public CollisionHandler cloneForShip(GameObject in_ship)
			throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		
		//spawn player explosion
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",playerShip.v.getPos().getX(),playerShip.v.getPos().getY(),0);
	
		
		explosion.i_animationFrame =0;
		explosion.i_animationFrameRow =1;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add( new StaticAnimationStep(3,7, 0));
		toRunIn.objectsOverlay.add(explosion);
		SoundEffects.getInstance().explosion.play(0.5f);
		
		explosion.stepHandlers.add(new FlyStraightStep(playerShip.v.getVel()));
		
		//set dead
		PlayerStep p=null;
		for(int i=0;i<playerShip.stepHandlers.size();i++)
			if(playerShip.stepHandlers.get(i)instanceof PlayerStep)
				p=  (PlayerStep)playerShip.stepHandlers.get(i);
		
		p.killPlayer();
		
		return false;
	}


}
