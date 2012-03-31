package com.DungeonCrawl;

import java.util.ArrayList;

import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.steeringbehaviors.simulation.renderer.Rect;

public class AreaEffect extends Drawable{

	
	public Color c_Color = Color.RED;
	public Rect area;
	Effect effect;
	private int i_duration;
	private boolean b_hasDuration=false;
	
	public void setDuration(int in_duration)
	{
		i_duration=in_duration;
		b_hasDuration=true;
	}
	public enum Effect
	{
		KILL_PLAYERS,
		KILL_EVERYTHING, KILL_NON_OBSTACLES, DAMAGE_EVERYTHING
		
	}
	
	public AreaEffect(Rect in_area, Effect in_e ,Drawable in_d)
	{
		area = in_area;
		effect = in_e;
		this.i_animationFrame = in_d.i_animationFrame;
		this.i_animationFrameRow = in_d.i_animationFrameRow;
		this.i_animationFrameSizeHeight = in_d.i_animationFrameSizeHeight;
		this.i_animationFrameSizeWidth = in_d.i_animationFrameSizeWidth;
		this.str_spritename = in_d.str_spritename;
	}
	
	public void stepEffect(LogicEngine in_logicEngine)
	{
		//if it expires after x steps
		if(b_hasDuration)
			if(i_duration-- ==0)
			{
				in_logicEngine.currentAreaEffects.remove(this);
				return;
			}
		
		
		
		if(effect == Effect.KILL_PLAYERS || effect ==  Effect.DAMAGE_EVERYTHING || effect == Effect.KILL_EVERYTHING || effect == Effect.KILL_NON_OBSTACLES) 
		{
			for(int i=0;i<in_logicEngine.objectsPlayers.size();i++)
			{
				GameObject player = in_logicEngine.objectsPlayers.get(i);
				//if player in the area
				if(area.inRect(player.v.getPos()))
				{
					boolean b_isPlayer=false;
					
					//find the player step handler and use it to kill them
					for(int j=0;j<in_logicEngine.objectsPlayers.get(i).stepHandlers.size();j++)
						if(in_logicEngine.objectsPlayers.get(i).stepHandlers.get(j) instanceof PlayerStep)
						{
							((PlayerStep)in_logicEngine.objectsPlayers.get(i).stepHandlers.get(j)).killPlayer(true,in_logicEngine);
							b_isPlayer=true;
						}
					
					if(b_isPlayer == false)
					{
						//its not a player so its probably a wingman or something so just blow it up
						//spawn an explosion
						GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",player.v.getPos().getX(),player.v.getPos().getY(),0);
					
						explosion.i_animationFrame =0;
						explosion.i_animationFrameSizeWidth =16;
						explosion.i_animationFrameSizeHeight=16;
						explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
						in_logicEngine.objectsOverlay.add(explosion);
						SoundEffects.getInstance().explosion.play();
						
						in_logicEngine.objectsPlayers.remove(in_logicEngine.objectsPlayers.get(i));
					}
				}
			}
		}
		
		if(effect == Effect.KILL_NON_OBSTACLES)
		{
			blowUpList(in_logicEngine.objectsEnemies,in_logicEngine);
			
		}
		
		if(effect == Effect.KILL_EVERYTHING)
		{
			blowUpList(in_logicEngine.objectsEnemies,in_logicEngine);
			
			in_logicEngine.objectsObstaclesLock.writeLock().lock();
			blowUpList(in_logicEngine.objectsObstacles,in_logicEngine);
			in_logicEngine.objectsObstaclesLock.writeLock().unlock();
		}
		if(effect == Effect.DAMAGE_EVERYTHING)
		{
			damageList(in_logicEngine.objectsEnemies,in_logicEngine);
			
			in_logicEngine.objectsObstaclesLock.writeLock().lock();
			damageList(in_logicEngine.objectsObstacles,in_logicEngine);
			in_logicEngine.objectsObstaclesLock.writeLock().unlock();
		}
	}
	
	private void blowUpList(ArrayList<GameObject> in_list, LogicEngine in_logicEngine)
	{
		//blow up list
		for(int i=0;i< in_list.size();i++)
		{
			GameObject objectToTest = in_list.get(i);

			//if its in the aoe kill it
			if(area.inRect(objectToTest.v.getPos()) && objectToTest.isBoss == false)
			{
				//spawn an explosion
				GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",objectToTest.v.getPos().getX(),objectToTest.v.getPos().getY(),0);
			
				explosion.i_animationFrame =0;
				explosion.i_animationFrameSizeWidth =16;
				explosion.i_animationFrameSizeHeight=16;
				explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
				in_logicEngine.objectsOverlay.add(explosion);
				SoundEffects.getInstance().explosion.play();
				
				in_list.remove(in_list.get(i));
			}
		}
	}
	//damage list
	private void damageList(ArrayList<GameObject> in_list, LogicEngine in_logicEngine)
	{
		//blow up list
		for(int i=0;i< in_list.size();i++)
		{
			GameObject objectToTest = in_list.get(i);
			boolean b_isInArea = false;
			
			if(objectToTest.collisionHandler != null)
			{
				objectToTest.updateBoundingBox(objectToTest.collisionHandler);
			
				//if its in the aoe kill it
				if(area.intersects(objectToTest.v.getBoundingBox()))
					b_isInArea = true;
			}
			else
			//if its not inside area but its collision radius is
			if(this.area.inRect(objectToTest.v.getPos()))
				b_isInArea = true;
			
			if(b_isInArea)
			{
				boolean b_destroyShip=true;
				if(objectToTest.collisionHandler != null && objectToTest.collisionHandler instanceof HitpointShipCollision)
				{
					((HitpointShipCollision)objectToTest.collisionHandler).f_numberOfHitpoints-= 0.4f;
					
					if(((HitpointShipCollision)objectToTest.collisionHandler).f_numberOfHitpoints >0)
					{
						//make it red
						objectToTest.c_Color = ((HitpointShipCollision)objectToTest.collisionHandler).c_flashColor;
						b_destroyShip=false;
					}
				}
				
				if(b_destroyShip && !objectToTest.isBoss)
				{
					//destroy object
					if(objectToTest.collisionHandler != null)
						if(objectToTest.collisionHandler.handleDestroy(in_logicEngine))
							in_list.remove(in_list.get(i));
				}
			}
		}
	}
	

}
