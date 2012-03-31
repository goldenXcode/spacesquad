package com.DungeonCrawl.Steps;

import com.DungeonCrawl.Advantage;
import com.DungeonCrawl.Formation;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.SoundEffects;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class PlayerStep implements StepHandler{

	int i_respawnTimer;
	public int i_positionInFleet=0;
	int i_stepCounter;
	public boolean b_isDead=false;

	GameObject go_warpIn = new GameObject("data/"+GameRenderer.dpiFolder+"/warpin.png",0,0,0);
	
	GameObject playerShip;
	
	Arrive behaviourArrive;
	private int i_warpTimer;
	public PlayerStep(int in_positionInFleet, GameObject in_playerShip)
	{
		playerShip = in_playerShip;
		i_positionInFleet = in_positionInFleet;
		behaviourArrive = new Arrive(0,0,10,1);
		
		//setup warpin
		go_warpIn.i_animationFrameSizeWidth=32;
		go_warpIn.i_animationFrameSizeHeight=32;
		
	}
	
	@Override
	public boolean handleStep(LogicEngine in_theLogicEngine,
			GameObject o_runningOn) {
		
		//if have gained lives since dying
		if(b_isDead && in_theLogicEngine.MyLifeCounter.i_livesRemaining > 0)
		{
			//resurect and remove a life
			resurectPlayer(in_theLogicEngine);
			b_isDead=false;
			
		}
		
		//TODO make this so that if they collect extra lives after going down to 1/2 ships new ones spawn
		if(i_respawnTimer > 0 )
		{				
			i_respawnTimer--;
			
			//if its last step of being dead and theres new ships to warp in
			if(i_respawnTimer == 0 && in_theLogicEngine.MyLifeCounter.i_livesRemaining>0)
			{
				resurectPlayer(in_theLogicEngine);
			}
			else if(in_theLogicEngine.MyLifeCounter.i_livesRemaining ==0)
			{
				this.b_isDead=true;
			}
					
			return false;
		}
		
		if(i_warpTimer >0)
		{
			i_warpTimer--;
			
			if(i_warpTimer ==0)
			{
				in_theLogicEngine.objectsOverlay.remove(go_warpIn);
				
				//warp in at 
			
				playerShip.v.setX(go_warpIn.v.getX());
				playerShip.v.setY(go_warpIn.v.getY());
				
				for(int i=0;i<Advantage.b_advantages.length;i++)
					if(Advantage.b_advantages[i] == true)
						Advantage.applyAdvantageToShip(i,playerShip,i_positionInFleet,in_theLogicEngine);
				
			}
			return false;
		}
		//move ships
		
		//if there is at least one target
		if(in_theLogicEngine.MyInputProcessor.targetsActive[0]!=false || in_theLogicEngine.MyInputProcessor.targetsActive[1]!=false)
		{
			
			//if there is only one target
			if(in_theLogicEngine.MyInputProcessor.targetsActive[0]==true ^ in_theLogicEngine.MyInputProcessor.targetsActive[1]==true)
			{
				int i_follow = 0;
				if(in_theLogicEngine.MyInputProcessor.targetsActive[1]==true)
				i_follow=1;
				
				//even ships follow target 0, odd ships follow target 1
				behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[i_follow].x + Formation.getXOffsetForShip(i_positionInFleet),
						in_theLogicEngine.MyInputProcessor.Targets[i_follow].y+ Formation.getYOffsetForShip(i_positionInFleet));
			}
			else
			{
				//distribute beteween the two targets
				//even ships follow target 0, odd ships follow target 1
				switch(i_positionInFleet)
				{
				case 0:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[0].x,
						in_theLogicEngine.MyInputProcessor.Targets[0].y);
				break;
				case 1:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[0].x -25,
							in_theLogicEngine.MyInputProcessor.Targets[0].y -25);
					break;
				case 2:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[1].x,
							in_theLogicEngine.MyInputProcessor.Targets[1].y);
					break;
				case 3:
					behaviourArrive.setTargetXY(in_theLogicEngine.MyInputProcessor.Targets[1].x +25,
							in_theLogicEngine.MyInputProcessor.Targets[1].y -25);
					break;
				}
			}
	
			Vector2d movementVector = behaviourArrive.calculate(o_runningOn.v);
				
			o_runningOn.move(movementVector);
		}
		
		// 
		return false;
	}

	public void resurectPlayer(LogicEngine in_theLogicEngine) {
		//reduce a life because a new hero is spawning
		in_theLogicEngine.MyLifeCounter.addLives(-1);
		
		this.b_isDead=false;
		
		i_warpTimer=24;
		//spawn teleport warp
		go_warpIn.i_animationFrame = 0;
		in_theLogicEngine.objectsOverlay.add(go_warpIn);
		
		go_warpIn.stepHandlers.clear();
		go_warpIn.stepHandlers.add(new StaticAnimationStep(4,8, 0));
		go_warpIn.stepHandlers.add(new SeekInputTarget(i_positionInFleet));
		
		//warp in at 1
		if(in_theLogicEngine.MyInputProcessor.targetsActive[0])
		{
			go_warpIn.v.setX(in_theLogicEngine.MyInputProcessor.Targets[0].x);
			go_warpIn.v.setY(in_theLogicEngine.MyInputProcessor.Targets[0].y-50);
		}
		else if(in_theLogicEngine.MyInputProcessor.targetsActive[1])
		{
			go_warpIn.v.setX(in_theLogicEngine.MyInputProcessor.Targets[1].x);
			go_warpIn.v.setY(in_theLogicEngine.MyInputProcessor.Targets[1].y-50);
		}
		else
		{
			go_warpIn.v.setX(in_theLogicEngine.rect_Screen.getWidth()/2);
			go_warpIn.v.setY(in_theLogicEngine.rect_Screen.getHeight()/3);
		}
		
	}

	public void killPlayer() {
		i_respawnTimer = 10;
				
		//move off map
		playerShip.v.setX(Double.MAX_VALUE);
		playerShip.v.setY(Double.MAX_VALUE);
				
		
		//clear powerups
		while( playerShip.activePowerups.size() !=0)
		{
			//zero because this will actually remove it
			playerShip.activePowerups.get(0).unApplyPowerup(playerShip);
			
		}
	}

	
	public void killPlayer(boolean b,LogicEngine in_logicEngine) {
		
		//spawn player explosion
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",playerShip.v.getPos().getX(),playerShip.v.getPos().getY(),0);
	
		
		explosion.i_animationFrame =0;
		explosion.i_animationFrameRow =1;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add( new StaticAnimationStep(3,7, 0));
		in_logicEngine.objectsOverlay.add(explosion);
		
		
		killPlayer();
		SoundEffects.getInstance().explosion.play();
		
	}
	@Override
	public StepHandler clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
