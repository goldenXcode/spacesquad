package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;

import de.steeringbehaviors.simulation.behaviors.Seek;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class SeekNearestEnemyStep implements StepHandler{
	

		
		Seek seek = null;
		
		private double distanceToStartSeeking;
		private double influence;
		
		public SeekNearestEnemyStep(double in_distanceToStartSeeking, double in_influence)
		{
			distanceToStartSeeking = in_distanceToStartSeeking;
			influence = in_influence;
			
			seek = new Seek();
			seek.setActiveDistance(in_distanceToStartSeeking);
			seek.setInfluence(in_influence);

	
		}
		
		int i_stepCounter=0;
		GameObject go_objectToSeek;

		public Vector2d v_noTarget = null;
		@Override
		public boolean handleStep(LogicEngine in_theLogicEngine,GameObject o_runningOn) {

			
			//seek enemy player
			i_stepCounter++;
			
			if(i_stepCounter%3 ==0)
			{
				go_objectToSeek =Utils.getClosestEnemy(in_theLogicEngine,o_runningOn.v);
				
				if(go_objectToSeek != null)
					//TODO could calculate this only every x steps
					seek.setTarget(go_objectToSeek.v);	
				
			}
			
			if(LogicEngine.rect_Screen.inRect(o_runningOn.v.getPos())==false)
				o_runningOn.i_offScreenCounter++;
			else
				o_runningOn.i_offScreenCounter=0;
				
			
			if(go_objectToSeek != null)
				o_runningOn.v.addForce(seek.calculate(o_runningOn.v));
			else
				if(v_noTarget != null)
				{				
					o_runningOn.v.addForce(v_noTarget);
					
				}
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public StepHandler clone() throws CloneNotSupportedException
		{
			SeekNearestEnemyStep toReturn = new SeekNearestEnemyStep(this.distanceToStartSeeking,this.influence);
			toReturn.v_noTarget = this.v_noTarget;
			
			return toReturn;
		}
	

}
