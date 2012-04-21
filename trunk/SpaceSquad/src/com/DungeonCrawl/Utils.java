package com.DungeonCrawl;

import java.util.ArrayList;
import java.util.List;

import com.DungeonCrawl.Collisions.DoNothingCollision;
import com.DungeonCrawl.KdTree.Entry;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.TimedLifeStep;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;

public class Utils {

	
	static int i_LastUpdatedKd = -1;
	static KdTree<GameObject> kd_enemies = null;
	static KdTree<GameObject> kd_obstacles = null;
	
	
	public enum Direction
	{
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
		
	public static double getDistanceToClosestPlayerShip(LogicEngine in_theLogicEngine, Point2d in_position)
	{
		
		
		
		//find closest player to the past in vehicle/Geometrie
		
		double d_closestPlayerDistance=Double.MAX_VALUE;
		
		
		for(int i=0;i<LogicEngine.NUMBER_OF_PLAYER_SHIPS;i++)
		{
			double d_distance = Math.abs(in_position.sub(in_theLogicEngine.objectsPlayers.get(i).v.getPos()).length());
			if(d_distance < d_closestPlayerDistance)
				d_closestPlayerDistance = d_distance;
					
		}
		return d_closestPlayerDistance;
	}
	
	public static int getAngleOfRotation(Vector2d in_v)
	{
		//SOH CAH TOA
		double i_angle=0;
		
		if(in_v != null)
			i_angle = 180 * Math.atan2(in_v.getY(), in_v.getX())/Math.PI;
		
		
		
		return (int)i_angle;
	}
		
	public static int getClosestPlayerShip(LogicEngine in_theLogicEngine, Geometrie in_point)
	{
		//find closest player to the past in vehicle/Geometrie
		int i_closestPlayer=0;
		double d_closestPlayerDistance=Double.MAX_VALUE;
		Point2d position = in_point.getPos();
		
		for(int i=0;i<LogicEngine.NUMBER_OF_PLAYER_SHIPS;i++)
		{
			double d_distance = position.sub(in_theLogicEngine.objectsPlayers.get(i).v.getPos()).length();
			if(d_distance < d_closestPlayerDistance)
			{
				d_closestPlayerDistance = d_distance;
				i_closestPlayer = i;
			}
			
		}
		return i_closestPlayer;
	}
	
	private static void buildKdTrees(LogicEngine in_theLogicEngine)
	{
		//build kd tree
		if(in_theLogicEngine.i_stepCounter != i_LastUpdatedKd)
		{
			i_LastUpdatedKd = in_theLogicEngine.i_stepCounter;
			kd_enemies = new KdTree.Manhattan<GameObject>(2,1000);
			kd_obstacles = new KdTree.Manhattan<GameObject>(2,1000);
			
			//build kd tree of enemies
			for(int i=0;i<in_theLogicEngine.objectsEnemies.size();i++)
			{
				GameObject go_currentEnemy = in_theLogicEngine.objectsEnemies.get(i);
					kd_enemies.addPoint(new double[]{go_currentEnemy.v.getX(),go_currentEnemy.v.getY()}, go_currentEnemy);
			}
			
			//build kd tree of obstacles
			for(int i=0;i<in_theLogicEngine.objectsObstacles.size();i++)
			{
				GameObject go_currentObstacle = in_theLogicEngine.objectsObstacles.get(i);
				kd_obstacles.addPoint(new double[]{go_currentObstacle.v.getX(),go_currentObstacle.v.getY()}, go_currentObstacle);
			}
		}
	}
	
	public static double getDistanceToClosestEnemyShip(LogicEngine in_theLogicEngine, Point2d in_point)
	{
		
		buildKdTrees(in_theLogicEngine);
		
		List<Entry<GameObject>> go_closestEnemy = kd_enemies.nearestNeighbor(new double[]{in_point.getX(),in_point.getY()}, 1, true);
		List<Entry<GameObject>> go_closestObstacle = kd_obstacles.nearestNeighbor(new double[]{in_point.getX(),in_point.getY()}, 1, true);

		//find closest enemy/obstacle to point
		if(go_closestEnemy.size() > 0 && go_closestObstacle.size()>0)
			return Math.min(go_closestEnemy.get(0).distance, go_closestObstacle.get(0).distance);
		else
			if(go_closestEnemy.size() > 0) //only enemies exist
				return go_closestEnemy.get(0).distance;	
		if(go_closestObstacle.size() >0) //only obstacles exist
			return go_closestObstacle.get(0).distance;
		else
			return Double.MAX_VALUE; //nothing exists
				
	}
	
	public static Vector2d getVectorToClosestPlayer(LogicEngine in_theLogicEngine, Point2d in_point) {
	
		
		//find closest player to the past in vehicle/Geometrie
		Vector2d v_closest = null;
			
		double d_closestPlayerDistance=Double.MAX_VALUE;
		Point2d position = in_point;
		
		for(int i=0;i<LogicEngine.NUMBER_OF_PLAYER_SHIPS;i++)
		{
			Vector2d v_between =  position.sub(in_theLogicEngine.objectsPlayers.get(i).v.getPos());
			double d_distance = v_between.length();
			
			if(d_distance < d_closestPlayerDistance)
			{
				d_closestPlayerDistance = d_distance;
				v_closest = v_between;
			}
			
		}
		return v_closest;
		
	}
	public static GameObject getSimpleExplosion()
	{
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",0,0,0);
		
		explosion.collisionHandler = new DoNothingCollision(explosion,0);
		explosion.i_animationFrame =0;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add(new StaticAnimationStep(3,7, 0));
		
		return explosion;
	}
	
	public static GameObject getBossExplosion(GameObject in_boss)
	{
		//create final explosion
		//spawn an explosion
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",0,0,0);
	
		explosion.i_animationFrame =0;
		explosion.i_animationFrameRow =3;
		explosion.i_animationFrameSizeWidth =64;
		explosion.i_animationFrameSizeHeight =64;
		explosion.stepHandlers.add(new StaticAnimationStep(4,12, 0));
		
		
		//create copy of boss and generate mini explosions
		GameObject bossCorpse = new GameObject();
		
		bossCorpse.str_spritename = in_boss.str_spritename;
		bossCorpse.c_Color = in_boss.c_Color;
		bossCorpse.f_forceRotation = in_boss.f_forceRotation;
		bossCorpse.f_forceScaleX = in_boss.f_forceScaleX;
		bossCorpse.f_forceScaleY = in_boss.f_forceScaleY;
		bossCorpse.i_animationFrame = in_boss.i_animationFrame;
		bossCorpse.i_animationFrameRow = in_boss.i_animationFrameRow;
		bossCorpse.i_animationFrameSizeHeight = in_boss.i_animationFrameSizeHeight;
		bossCorpse.i_animationFrameSizeWidth = in_boss.i_animationFrameSizeWidth;
		bossCorpse.v = new Vehicle();
		bossCorpse.rotateToV = in_boss.rotateToV;
		//bossCorpse.visibleBuffs = (ArrayList<Drawable>)bossCorpse.visibleBuffs.clone();
	
		bossCorpse.i_animationFrameRow = in_boss.i_animationFrameRow;
		
		//in_launchEvery, int in_numberToLaunch, int in_waveSpacing, boolean in_isEastLaunch)
		//mini explosions
		
		LaunchShipsStep explosionsStep = new LaunchShipsStep(Utils.getSimpleExplosion(),3,500,1,true);
		LaunchShipsStep explosionsStep2 = new LaunchShipsStep(Utils.getSimpleExplosion(),3,500,1,true);
		LaunchShipsStep explosionsStep3 = new LaunchShipsStep(Utils.getSimpleExplosion(),3,500,1,true);
		
		bossCorpse.stepHandlers.add(explosionsStep);
		bossCorpse.stepHandlers.add(explosionsStep2);
		bossCorpse.stepHandlers.add(explosionsStep3);
		explosionsStep.b_addToOverlay = true;
		explosionsStep.b_randomDirection = true;
		explosionsStep2.b_addToOverlay = true;
		explosionsStep2.b_randomDirection = true;
		explosionsStep3.b_addToOverlay = true;
		explosionsStep3.b_randomDirection = true;
		
		
		TimedLifeStep life = new TimedLifeStep(50);
		life.explosion = explosion;
		
		bossCorpse.stepHandlers.add(life);
		
		
		return bossCorpse;
	}
	
	
	public static GameObject getMiniBossExplosion(GameObject in_boss)
	{
		//create final explosion
		//spawn an explosion
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",0,0,0);
	
		explosion.i_animationFrame =0;
		explosion.i_animationFrameRow =3;
		explosion.i_animationFrameSizeWidth =64;
		explosion.i_animationFrameSizeHeight =64;
		explosion.stepHandlers.add(new StaticAnimationStep(4,6, 0));
		explosion.f_forceScaleX=0.5f;
		explosion.f_forceScaleY=0.5f;
		
		//create copy of boss and generate mini explosions
		GameObject bossCorpse = new GameObject();
		
		bossCorpse.str_spritename = in_boss.str_spritename;
		bossCorpse.c_Color = in_boss.c_Color;
		bossCorpse.f_forceRotation = in_boss.f_forceRotation;
		bossCorpse.f_forceScaleX = in_boss.f_forceScaleX;
		bossCorpse.f_forceScaleY = in_boss.f_forceScaleY;
		bossCorpse.i_animationFrame = in_boss.i_animationFrame;
		bossCorpse.i_animationFrameRow = in_boss.i_animationFrameRow;
		bossCorpse.i_animationFrameSizeHeight = in_boss.i_animationFrameSizeHeight;
		bossCorpse.i_animationFrameSizeWidth = in_boss.i_animationFrameSizeWidth;
		bossCorpse.v = new Vehicle();
		bossCorpse.rotateToV = in_boss.rotateToV;
		//bossCorpse.visibleBuffs = (ArrayList<Drawable>)bossCorpse.visibleBuffs.clone();
	
		bossCorpse.i_animationFrameRow = in_boss.i_animationFrameRow;
		
		//in_launchEvery, int in_numberToLaunch, int in_waveSpacing, boolean in_isEastLaunch)
		//mini explosions
		
		LaunchShipsStep explosionsStep = new LaunchShipsStep(Utils.getSimpleExplosion(),3,500,1,true);
		LaunchShipsStep explosionsStep2 = new LaunchShipsStep(Utils.getSimpleExplosion(),3,500,1,true);
	
		
		bossCorpse.stepHandlers.add(explosionsStep);
		bossCorpse.stepHandlers.add(explosionsStep2);
		
		
		explosionsStep.b_addToOverlay = true;
		explosionsStep.b_randomDirection = true;
		explosionsStep2.b_addToOverlay = true;
		explosionsStep2.b_randomDirection = true;
		
		
		TimedLifeStep life = new TimedLifeStep(50);
		life.explosion = explosion;
		
		bossCorpse.stepHandlers.add(life);
		
		
		return bossCorpse;
	}
	
	private static TextDisplaying systemMessage;
	private static long systemMessage_startedTick;
	
	
	public static void DisplaySystemMessage(String str_toDisplay, SpriteBatch in_batch)
	{
		if(str_toDisplay.equals(""))
			return;
		
		float f_durationToShowFor = 4000;
		
		//instantiate
		if(systemMessage == null)
		{
			systemMessage = new TextDisplaying(str_toDisplay,LogicEngine.SCREEN_WIDTH/2,LogicEngine.SCREEN_HEIGHT/1.7f,true);
			systemMessage_startedTick = System.currentTimeMillis();
		}
		
		//if it has changed from last time
		if(!systemMessage.toDisplay.equals(str_toDisplay))
		{
			systemMessage.toDisplay = str_toDisplay;
			systemMessage.display(in_batch, 1);
			systemMessage_startedTick = System.currentTimeMillis();
		}
		else
		{
			//slowly fade alpha over time (time since started) / f_durationToShowFor without going negative
			systemMessage.display(in_batch, Math.max(0, 1f - ((float)(System.currentTimeMillis() - systemMessage_startedTick)/f_durationToShowFor)));
			
		}
	}
	
	
	public static void drawRect(TextureRegion in_texr , Rect in_rect, SpriteBatch batch)
	{
		batch.draw(in_texr, (float)in_rect.getp1().getX() *GameRenderer.dpiTextureCoordinatesAdjust,(float)in_rect.getp1().getY()*GameRenderer.dpiTextureCoordinatesAdjust,
				(float)in_rect.getWidth()*GameRenderer.dpiTextureCoordinatesAdjust,
				(float)in_rect.getHeight()*GameRenderer.dpiTextureCoordinatesAdjust);
	
	}
	
	public static GameObject getClosestEnemy(LogicEngine in_theLogicEngine,
			Vehicle in_point) {
	
			buildKdTrees(in_theLogicEngine);
			
			
			List<Entry<GameObject>> go_closestEnemy = kd_enemies.nearestNeighbor(new double[]{in_point.getX(),in_point.getY()}, 1, true);
			List<Entry<GameObject>> go_closestObstacle = kd_obstacles.nearestNeighbor(new double[]{in_point.getX(),in_point.getY()}, 1, true);

			//find closest enemy/obstacle to point
			if(go_closestEnemy.size() > 0 && go_closestObstacle.size()>0)
				if(go_closestEnemy.get(0).distance > go_closestObstacle.get(0).distance) //enemy is closest
					return go_closestEnemy.get(0).value;
				else
					return go_closestObstacle.get(0).value;
			else
				if(go_closestEnemy.size() > 0) //only enemies exist
					return go_closestEnemy.get(0).value;	
			if(go_closestObstacle.size() >0) //only obstacles exist
				return go_closestObstacle.get(0).value;
			else
				return null; //nothing exists
		
	}
	public static int getPlayerShipIndex(LogicEngine in_theLogicEngine,GameObject in_ship)
	{
		
		for(int i=0;i<in_theLogicEngine.objectsPlayers.size();i++)
		{		
			
			if(in_ship == in_theLogicEngine.objectsPlayers.get(i))
					return ((PlayerStep)in_theLogicEngine.objectsPlayers.get(i).stepHandlers.get(0)).i_positionInFleet;
				
		}
		try {
			throw new Exception("couldnt find player");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public static boolean isPlayer(GameObject in_ship)
	{
		//return true if it has a player step
		for(int i=0;i<in_ship.stepHandlers.size();i++)
			if(in_ship.stepHandlers.get(i) instanceof PlayerStep)
				return true;
		
		return false;
	}

}
