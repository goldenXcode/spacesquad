package com.DungeonCrawl.Collisions;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StepHandler;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class HitpointShipCollision implements CollisionHandler{

	GameObject thisObject;
	double collisionRadius;
	public float f_numberOfHitpoints;
	boolean b_flash;
	int i_flashFrame;
	int i_oldFrameBeforeFlash;
	int i_flashStartedOnStep;
	GameObject go_healthBar = null;
	GameObject go_healthBarWindow =null;
	
	float f_startingHitpoints;
	
	protected GameObject go_explosion=null;
	
	public Color c_initialColour = null;;
	public Color c_flashColor = Color.RED;
	protected int i_flashCounter =0;
	protected int i_flashDuration = 2;
	
	float i_scrollBossBar = 0;
	float i_scrollBossBarWindow = 0;
	private double f_healthBarStartX;
	private float f_newNumberOfHitpoints = Float.MIN_VALUE;
	
	public void addHitpointBossBar( LogicEngine toDisplayIn)
	{		
		
		go_healthBarWindow = new GameObject("data/"+GameRenderer.dpiFolder+"/newui.png",LogicEngine.rect_Screen.getWidth()/2,LogicEngine.rect_Screen.getHeight()-15,0);
		go_healthBarWindow.i_animationFrameSizeHeight = 34;
		go_healthBarWindow.i_animationFrameSizeWidth = 100;
		go_healthBarWindow.i_animationFrameRow=2;
		
		f_healthBarStartX = (LogicEngine.rect_Screen.getWidth()/2)+1;
		go_healthBar = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",f_healthBarStartX, LogicEngine.rect_Screen.getHeight()-13,0);
		go_healthBar.stepHandlers.add(new FlyStraightStep(new Vector2d(0.0,0.0)));
		
		go_healthBar.i_animationFrame=3;
		go_healthBar.i_animationFrameSizeWidth=64;
		go_healthBar.i_animationFrameSizeHeight=16;
		
		//add it
		toDisplayIn.objectsOverlay.add(go_healthBarWindow);
		toDisplayIn.objectsOverlay.add(go_healthBar);
		
		f_startingHitpoints = f_numberOfHitpoints;
		
		
	}
	public HitpointShipCollision(GameObject in_for,float in_numberOfHitpoints, double in_collisionRadius)
	{
		thisObject = in_for;
		collisionRadius = in_collisionRadius;
		f_numberOfHitpoints = in_numberOfHitpoints;
		
		if( in_for.c_Color != null)
			c_initialColour = in_for.c_Color ;

		
	}
	public HitpointShipCollision(GameObject in_for,float in_numberOfHitpoints, double in_collisionRadius,boolean in_flash,int in_flashFrame)
	{
		thisObject = in_for;
		collisionRadius = in_collisionRadius;
		f_numberOfHitpoints = in_numberOfHitpoints;
		b_flash = in_flash;
		
		i_flashFrame = in_flashFrame;
		if( in_for.c_Color != null)
			c_initialColour = in_for.c_Color ;
	}
	
	public HitpointShipCollision(GameObject in_for,float in_numberOfHitpoints, double in_collisionRadius, boolean in_simpleExplosion)
	{
		thisObject = in_for;
		collisionRadius = in_collisionRadius;
		f_numberOfHitpoints = in_numberOfHitpoints;
		
		if( in_for.c_Color != null)
			c_initialColour = in_for.c_Color ;
		
		if(in_simpleExplosion)
			setSimpleExplosion();
	}
	public void setExplosion(GameObject in_explosion)
	{
		go_explosion = in_explosion;
	}
	public void setSimpleExplosion()
	{
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",thisObject.v.getPos().getX(),thisObject.v.getPos().getY(),0);
		
		explosion.i_animationFrame =0;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
			
		
		go_explosion = explosion;
	}
	
	@Override
	public boolean handleCollision(GameObject collidingWith, LogicEngine toRunIn) {

		if(	c_initialColour ==null)
			 c_initialColour = thisObject.c_Color;
		
		//remove flash if we are to support flashing AND the current frame is a flash frame AND at least 1 step has happened since the flash was set
		if(b_flash &&  thisObject.i_animationFrame == i_flashFrame &&  toRunIn.i_stepCounter != i_flashStartedOnStep)
		{
			thisObject.i_animationFrame = i_oldFrameBeforeFlash;
			
		}
		
		
		//if it's not lethal collision or a collision with a non friendly 
		if(collidingWith.allegiance == GameObject.ALLEGIANCES.LETHAL || (collidingWith.allegiance!= GameObject.ALLEGIANCES.NONE && thisObject.allegiance != collidingWith.allegiance ))
		{
			//if colliding with another hp ship
			if(collidingWith.collisionHandler instanceof HitpointShipCollision)
			{
				//only update number of hitpoints in step NOT HERE, here we use temporary value so that we dont get the wrong value when doing collision e.g. rock with hp5 collides with boss with hp100 rock gets set to 0 then boss gets set to 100-0 (oops)
				if(thisObject.isBoss)
					if(f_newNumberOfHitpoints == Float.MIN_VALUE)
						f_newNumberOfHitpoints = f_numberOfHitpoints - (Math.max(0f,((HitpointShipCollision)collidingWith.collisionHandler).f_numberOfHitpoints)/2f);
					else
						f_newNumberOfHitpoints -= (Math.max(0f,((HitpointShipCollision)collidingWith.collisionHandler).f_numberOfHitpoints)/2f);
				else
					if(f_newNumberOfHitpoints == Float.MIN_VALUE)
						f_newNumberOfHitpoints = f_numberOfHitpoints - Math.max(0f,((HitpointShipCollision)collidingWith.collisionHandler).f_numberOfHitpoints);
					else
						f_newNumberOfHitpoints -= Math.max(0f,((HitpointShipCollision)collidingWith.collisionHandler).f_numberOfHitpoints);
				
			}
			else
			{
				if(f_newNumberOfHitpoints == Float.MIN_VALUE)
					f_newNumberOfHitpoints = f_numberOfHitpoints-1;
				else
					f_newNumberOfHitpoints--;
				
				
			}
			
			
			//if graphics flash supported by this collision 
			if(b_flash)
			{
				//if not already flashing
				if(thisObject.i_animationFrame != i_flashFrame)
				{
					i_oldFrameBeforeFlash = thisObject.i_animationFrame;
					thisObject.i_animationFrame = i_flashFrame;
					i_flashStartedOnStep = toRunIn.i_stepCounter;
				}
			}
			else
			{
				i_flashCounter = i_flashDuration;
				thisObject.c_Color = this.c_flashColor;
			}
			
			if(f_numberOfHitpoints <=0)
			{
				return handleDestroy(toRunIn);
				
				
			}
			
			
			
		}
		// TODO Auto-generated method stub
		return false;
	}

	public void updateBossHealth(LogicEngine toRunIn) {
		//if theres a health bar reduce its size
		if(go_healthBar != null)
		{
			go_healthBar.f_forceScaleX = (float)f_numberOfHitpoints/(float)f_startingHitpoints;
			
			float f_moveBarLeft = (1.0f - go_healthBar.f_forceScaleX)/2;
			f_moveBarLeft = f_moveBarLeft*go_healthBar.i_animationFrameSizeWidth;
			
			go_healthBar.v.setX(f_healthBarStartX-f_moveBarLeft);
			go_healthBar.f_forceScaleY = 1f;
		}
		
	}
	@Override
	public double getCollisionRadius() {
		// TODO Auto-generated method stub
		return collisionRadius;
	}
	@Override
	public CollisionHandler cloneForShip(GameObject in_ship) {
		HitpointShipCollision returnVal = new HitpointShipCollision(in_ship, (int)f_numberOfHitpoints, collisionRadius);
		
		returnVal.c_flashColor = this.c_flashColor;
		returnVal.c_initialColour = this.c_initialColour;
		returnVal.i_flashDuration = this.i_flashDuration;
		returnVal.f_numberOfHitpoints = this.f_numberOfHitpoints;
		
		if(this.go_explosion != null)
			returnVal.go_explosion = this.go_explosion.clone();
		
		return returnVal;
		
	}
	
	@Override
	public StepHandler clone()
	{
		return this;
	}
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		//if hp changed 
		if(f_newNumberOfHitpoints  != Float.MIN_VALUE)
		{
			this.f_numberOfHitpoints = f_newNumberOfHitpoints;
			f_newNumberOfHitpoints = Float.MIN_VALUE;
			this.updateBossHealth(in_theLogicEngine);
		}
		
			//remove flash if we are to support flashing AND the current frame is a flash frame AND at least 1 step has happened since the flash was set
			if(b_flash &&  thisObject.i_animationFrame == i_flashFrame &&  in_theLogicEngine.i_stepCounter != i_flashStartedOnStep)
			{
				thisObject.i_animationFrame = i_oldFrameBeforeFlash;
				
			}
		
		if(o_runningOn.c_Color != null)
			if(o_runningOn.c_Color.equals((c_flashColor)))
				if(i_flashCounter >0)
					i_flashCounter--;
				else
				{
					if(c_initialColour != null)
						o_runningOn.c_Color = c_initialColour;
					else
						o_runningOn.c_Color = Color.WHITE;
				}
	
		return false;
	}
	@Override
	public boolean handleDestroy(LogicEngine toRunIn) {
		
		if(go_explosion!=null)
		{
			go_explosion.v.setX(thisObject.v.getPos().getX());
			go_explosion.v.setY(thisObject.v.getPos().getY());
			
			if(thisObject.isBoss)
			{
				Vector2d v = thisObject.v.getVel();
				v.scale(0.1);
				
				go_explosion.stepHandlers.add(new FlyStraightStep(v));
			}
			else
				go_explosion.stepHandlers.add(new FlyStraightStep(thisObject.v.getVel()));
		
			
			//spawn an explosion
			if(thisObject.isBoss)
				toRunIn.objectsUnderlay.add(go_explosion);
			else
				toRunIn.objectsOverlay.add(go_explosion);
			//TODO:replace this with support for multiple sounds
			SoundEffects.getInstance().explosion.play(0.5f);
		}
		
		//remove boss bar
		if(go_healthBar != null)
			toRunIn.objectsOverlay.remove(go_healthBar);
		
		// TODO Auto-generated method stub
		return true;
	}

}
