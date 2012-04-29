package com.DungeonCrawl.Steps;

import java.util.Random;

import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class LaunchShipsStep implements StepHandler 
{
	Random r = new Random();
	public int i_launchEvery;
	int i_numberToLaunch;
	int i_waveSpacing;
	GameObject go_toLaunch;
	boolean b_east = false;
	public boolean b_forceVelocityChangeBasedOnParentMirror = false; 
	
	public LaunchShipsStep(GameObject in_toLaunch, int in_launchEvery, int in_numberToLaunch, int in_waveSpacing, boolean in_isEastLaunch)
	{
		i_launchEvery 		= in_launchEvery;
		i_numberToLaunch 	= in_numberToLaunch;
		i_waveSpacing 		= in_waveSpacing;
		go_toLaunch 		= in_toLaunch;
		b_east = in_isEastLaunch;
		
		
	}

	int i_stepCounter=0;
	int i_launchingCounter=0;
	public boolean b_randomDirection;
	public boolean b_addToOverlay;
	public boolean b_addToBullets;
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
				
		if(i_stepCounter == i_launchEvery)
		{
			//finished launching ships
			if(i_launchingCounter >= i_numberToLaunch*i_waveSpacing)
			{
				i_stepCounter=0;
				i_launchingCounter=0;
			}
			else
			{
				i_launchingCounter++;
				
				if(i_launchingCounter % i_waveSpacing ==0)
				{
					
					
					GameObject go_clone = (GameObject)go_toLaunch.clone();
					if(b_randomDirection)
					{
						go_clone.stepHandlers.add(new FlyStraightStep(new Vector2d(r.nextInt(10)-5,r.nextInt(10)-5)));
						go_clone.v.setX(o_runningOn.v.getX());
						
					}
					else
					if(b_east)
					{
						if(go_clone.v.getPath().size()>0)
							((Point2d)(go_clone.v.getPath().get(0))).setX(o_runningOn.v.getX()+100);
						go_clone.v.setX(o_runningOn.v.getX()+10);
					}
					else
					{
						if(go_clone.v.getPath().size()>0)
							((Point2d)(go_clone.v.getPath().get(0))).setX(o_runningOn.v.getX()-100);
						go_clone.v.setX(o_runningOn.v.getX()-10);
					}
					go_clone.v.setY(o_runningOn.v.getY());
					
					//if it has waypoints make first waypoint exitting the ship sidways
					if(go_clone.v.getPath().size()>0)
						((Point2d)(go_clone.v.getPath().get(0))).setY(o_runningOn.v.getY());
					
					//if parent is mirrored, mirror
					if(b_forceVelocityChangeBasedOnParentMirror)
						if(o_runningOn.b_mirrorImageHorizontal)
							go_clone.v.getVel().setX(-go_clone.v.getVel().getX());

					if(b_addToBullets)
						if(go_clone.allegiance == GameObject.ALLEGIANCES.PLAYER)
							in_theLogicEngine.objectsPlayerBullets.add(go_clone);
						else
							in_theLogicEngine.objectsEnemyBullets.add(go_clone);
					else
					if(b_addToOverlay)
						in_theLogicEngine.objectsOverlay.add(go_clone);
					else
						in_theLogicEngine.objectsEnemies.add(go_clone);
				}
			}
			
			
		}
		else
			i_stepCounter++; //not in a launching phase
		

		return false;
	}
	
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
}
