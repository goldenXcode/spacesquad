package com.DungeonCrawl.Levels;

import java.util.Random;

import com.DungeonCrawl.Advantage;
import com.DungeonCrawl.Difficulty;
import com.DungeonCrawl.Difficulty.DIFFICULTY;
import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.TextDisplaying;
import com.DungeonCrawl.UI_LevelSelect;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Collisions.PlayerCollision;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Collisions.SplitCollision;
import com.DungeonCrawl.GameObject.ALLEGIANCES;
import com.DungeonCrawl.Levels.Level7.CARRIER_TYPE;
import com.DungeonCrawl.Powerups.DualFirePowerup;
import com.DungeonCrawl.Powerups.MinesPowerup;
import com.DungeonCrawl.Powerups.MissilePowerup;
import com.DungeonCrawl.Powerups.MovementPowerup;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Powerups.RapidFirePowerup;
import com.DungeonCrawl.Powerups.ShieldPowerup;
import com.DungeonCrawl.Powerups.SlowFieldPowerup;
import com.DungeonCrawl.Powerups.WingmenPowerup;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.ExplodeIfInRange;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Shooting.TurretShot;
import com.DungeonCrawl.Steps.AnimateRollStep;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.PullShipsStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.DungeonCrawl.Steps.StealthStep;
import com.DungeonCrawl.Steps.StepHandler;
import com.DungeonCrawl.Steps.TimedLifeStep;
import com.DungeonCrawl.Steps.WanderStep;
import com.badlogic.gdx.graphics.Color;


import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.behaviors.Wander;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class LevelManager  
{

	public int level=1;
	Random r = new Random();
	
	int i_stepCounter;
	public LevelManager()
	{
		i_stepCounter=0;
	}
	
	Level level0 = null;
	BasicLevel level1 = null;
	Level2 level2 = null;
	Level3 level3 = null;
	Level4 level4 = null;
	Level5 level5 = null;
	Level6 level6 = null;
	Level7 level7 = null;
	Level8 level8 = null;
	Level9 level9 = null;
	Level10 level10 = null;
	Level11 level11 = null;
	
	boolean b_refreshLivesBetweenLevels = true;

	public void newBossRunGame(LogicEngine in_logicEngine) {
	
		level = 1;
		
		//reload all levels
		initLevels(in_logicEngine);
		clearStuff(in_logicEngine);
		createPlayers(in_logicEngine);
	
		//set all levels to boss
		level1.i_stepCounter= level1.BOSS_STEP;
		level2.i_stepCounter= level2.BOSS_STEP;
		level3.i_stepCounter= level3.BOSS_STEP;
		level4.i_stepCounter= level4.BOSS_STEP;
		level5.i_stepCounter= level5.BOSS_STEP;
		level6.i_stepCounter= level6.BOSS_STEP;
		level7.i_stepCounter= level7.BOSS_STEP;
		level8.i_stepCounter= level8.BOSS_STEP;
		level9.i_stepCounter= level9.BOSS_STEP;
		level10.i_stepCounter= level10.BOSS_STEP;
		level11.i_stepCounter= level1.BOSS_STEP;
		
		b_refreshLivesBetweenLevels = false;
		
		in_logicEngine.resAllDeadPlayers();
		in_logicEngine.MyLifeCounter.refreshLives();
		
	}
	
	public void newSurvivalGame(LogicEngine in_logicEngine) {
		level = 1;
		
		//reload all levels
		level1 = new SurvivalLevel();
		
		clearStuff(in_logicEngine);
		createPlayers(in_logicEngine);
	
		b_refreshLivesBetweenLevels = false;
		
		in_logicEngine.resAllDeadPlayers();
		in_logicEngine.MyLifeCounter.refreshLives();
	}
	
	
	public void newGame( LogicEngine in_logicEngine,int i_level)
	{
		level = i_level;
		
		//reload all levels
		initLevels(in_logicEngine);
		clearStuff(in_logicEngine);
		createPlayers(in_logicEngine);
	
		b_refreshLivesBetweenLevels = true;
		
		in_logicEngine.resAllDeadPlayers();		
		in_logicEngine.MyLifeCounter.refreshLives();
		
	}
	

	private void initLevels(LogicEngine in_logicEngine)
	{
		level0 = new Level0();
		level1 = new Level1();
		level2 = new Level2();
		level3 = new Level3();
		level4 = new Level4();
		level5 = new Level5();
		level6 = new Level6();
		level7 = new Level7();
		level8 = new Level8();
		level9 = new Level9();
		level10 = new Level10();
		level11 = new Level11();
		
	}
	
	private void clearStuff(LogicEngine in_logicEngine)
	{
		in_logicEngine.objectsEnemies.clear();
		in_logicEngine.objectsEnemyBullets.clear();
		in_logicEngine.objectsObstacles.clear();
		in_logicEngine.objectsPlayerBullets.clear();
		in_logicEngine.objectsPowerups.clear();
		in_logicEngine.objectsUnderlay.clear();
		in_logicEngine.objectsPlayers.clear();
		in_logicEngine.objectsOverlay.clear();
		in_logicEngine.currentTextBeingDisplayed.clear();
		
		in_logicEngine.currentAreaEffects.clear();
		
	}
	
	private void createPlayers(LogicEngine in_logicEngine)
	{
		//TODO pending decide what to do with players prevent double spawns etc
		for(int i=0 ; i < 4 ;i++)
		{
			GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/tinyship.png",(LogicEngine.rect_Screen.getWidth()/2) - (32*i) + 64,50,20);
			ship.i_animationFrame=0;
			ship.i_animationFrameSizeWidth=16;
			ship.i_animationFrameSizeHeight=16;
			ship.allegiance = GameObject.ALLEGIANCES.PLAYER;
			ship.collisionHandler = new PlayerCollision(ship);
			ship.stepHandlers.add(new PlayerStep(i,ship));
			ship.shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/bullet.png",7.0f,new Vector2d(0,9));
			
			ship.stepHandlers.add(new AnimateRollStep());
				
			ship.str_name = "player";
			
			ship.c_Color = new Color(1.0f,1.0f,1.0f,1.0f);
			
			ship.v.setMaxForce(3);
			ship.v.setMaxVel(20.0);
			in_logicEngine.objectsPlayers.add(ship);
			
			//double firing speed on hell
			if(Difficulty.isHard())
				ship.shootEverySteps = (int) (ship.shootEverySteps * 0.75); 
			
			//for each advantage if it is enabled apply it
			for(int j=0;j< Advantage.b_advantages.length;j++)
				if(Advantage.b_advantages[j])
					Advantage.applyAdvantageToShip(j,ship,i,in_logicEngine);
			
		}
	}

	
	public void spawnDifficultyIcon( LogicEngine in_logicEngine)
	{	
		//dont bother showing easy
		if(Difficulty.isEasy())
			return;
		
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/interface.png",(LogicEngine.SCREEN_WIDTH/2), LogicEngine.SCREEN_HEIGHT/1.5,0);
		go.stepHandlers.add(new TimedLifeStep(100));
		
		go.i_animationFrameSizeHeight=50;
		go.i_animationFrameSizeWidth=50;
		go.i_animationFrame=0;
		go.f_forceScaleX=4;
		go.f_forceScaleY=4;
		
		if(Difficulty.isMedium())
			go.i_animationFrameRow=7;
		if(Difficulty.isHard())
			go.i_animationFrameRow=8;
		
		in_logicEngine.objectsOverlay.add(go);
	
	}

	
	public void spawnCloud( LogicEngine in_logicEngine, int in_cloudNumber,float in_x)
	{			
	/*	GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/cloud" + in_cloudNumber + ".png",in_x, LogicEngine.rect_Screen.getHeight()+50,0);
		go.stepHandlers.add(new FlyStraightStep(new Vector2d(0.0,(Math.random()*-5.0) -5.0f)));
		
		if(Math.random()<=0.5)
			in_logicEngine.objectsOverlay.add(go);
		else
			in_logicEngine.objectsUnderlay.add(go);*/
	}
	
	GameObject spawnMine( LogicEngine in_logicEngine, double in_x , double in_y)
	{
		GameObject mine = new GameObject("data/"+GameRenderer.dpiFolder+"/mine.png",in_x,in_y, 5);
				
		mine.i_animationFrame=1;
		mine.i_animationFrameSizeWidth=16;
		mine.i_animationFrameSizeHeight=16;
		
		mine.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-2)));
		
		mine.v.setMaxForce(2.0);
		mine.v.setMaxVel(5.0);
		
		HitpointShipCollision c = new HitpointShipCollision(mine,3,10);
		c.setSimpleExplosion();
		
		mine.collisionHandler =c; 
		mine.allegiance = GameObject.ALLEGIANCES.ENEMIES;		
		
		
		mine.shotHandler = new ExplodeIfInRange(true);
		

		
		in_logicEngine.objectsObstacles.add(mine);
		
		//return the object created 
		return mine;
	}
	
	public void spawnBaterang(LogicEngine in_logicEngine,float in_x, float in_y, StepHandler in_navigationStep)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",in_x,in_y,25+(int)(25 * Math.random()));
		go.stepHandlers.add(in_navigationStep);
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		go.i_animationFrame=1;
		go.i_animationFrameSizeHeight=32;
		go.i_animationFrameSizeWidth=32;
		go.rotateToV = true;
		
		go.v.setMaxForce(2.5);
		go.v.setMaxVel(2.5);
		
		CustomBehaviourStep cb = new CustomBehaviourStep(new Wander(-2.5,2.5,20,0.1));
		go.stepHandlers.add( cb);
		
		//shoots forwards whatever direction its going in
		StraightLineShot s = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png", 6,null);
		s.setShootForwards(true,8.0f);
		go.shotHandler = s;
		
		
		HitpointShipCollision collision = new HitpointShipCollision(go, 3, 25);
		collision.setSimpleExplosion();
		go.collisionHandler = collision;
		
		if(Difficulty.isMedium())
			collision.f_numberOfHitpoints=6;
			
		if(Difficulty.isHard())
		{
			go.shootEverySteps=15;
			collision.f_numberOfHitpoints=6;
		}	
			
		
		in_logicEngine.objectsEnemies.add(go);
		
	}
	
	public void spawnEnemyAsteroid(LogicEngine in_logicEngine, int i_asteroid, float in_x, boolean b_large)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/asteroids.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
		
		//lethal to everyone
		go.allegiance = GameObject.ALLEGIANCES.LETHAL;
		
		double xDrift = (Math.random()*2.0)-1.0;
		go.i_animationFrameSizeWidth=24;
		go.i_animationFrameSizeHeight=24;
		go.i_animationFrame=i_asteroid - ((int)(i_asteroid/5) *5);
		go.i_animationFrameRow = i_asteroid/5;
		
		//hitpoint collision with explosion
		HitpointShipCollision c =  new HitpointShipCollision(go,5, 17.0);
		
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",0,0,0);
		explosion.i_animationFrameRow = 2;
		explosion.i_animationFrame =0;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add( new StaticAnimationStep(3,7, 0));

		c.setExplosion(explosion);

		
		go.collisionHandler = c;
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(xDrift,-5)));
		in_logicEngine.objectsEnemies.add(go);
	}
	
	public GameObject spawnAsteroid(LogicEngine in_logicEngine, int i_asteroid, float in_x, boolean b_large)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/asteroids.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
		
		//lethal to everyone
		go.allegiance = GameObject.ALLEGIANCES.LETHAL;
		
		double xDrift = (Math.random()*2.0)-1.0;
		go.i_animationFrameSizeWidth=24;
		go.i_animationFrameSizeHeight=24;
		go.i_animationFrame=i_asteroid - ((int)(i_asteroid/5) *5);
		go.i_animationFrameRow = i_asteroid/5;
		
		//hitpoint collision with explosion
		HitpointShipCollision c =  new HitpointShipCollision(go,5, 17.0);
			
		
		
		GameObject explosion = new GameObject("data/"+GameRenderer.dpiFolder+"/smallexplosion.png",0,0,0);
		explosion.i_animationFrameRow = 2;
		explosion.i_animationFrame =0;
		explosion.i_animationFrameSizeWidth =16;
		explosion.i_animationFrameSizeHeight =16;
		explosion.stepHandlers.add( new StaticAnimationStep(3,7, 0));

		c.setExplosion(explosion);

		
		go.collisionHandler = c;
		
		if( Difficulty.isEasy())
			go.stepHandlers.add( new FlyStraightStep(new Vector2d(xDrift,-5)));
		if( Difficulty.isMedium())
			go.stepHandlers.add( new FlyStraightStep(new Vector2d(xDrift*1.5,-7.5)));
		if( Difficulty.isHard())
			go.stepHandlers.add( new FlyStraightStep(new Vector2d(xDrift*2,-10)));
		
		in_logicEngine.objectsObstacles.add(go);
		return go;
	}
	
	public void spawnWingmenPowerup(LogicEngine in_logicEngine, float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/powerups.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
		
		//not lethal to anyone
		go.allegiance = GameObject.ALLEGIANCES.NONE;
		
		
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrameRow = 0;
		Powerup p=null;
		 
		p = new WingmenPowerup();
		go.i_animationFrame=0;
		go.i_animationFrameRow=1;

		
		//apply powerup to all ships
		go.collisionHandler = new PowerupCollision(p,false);
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-2.5)));
		in_logicEngine.objectsPowerups.add(go);
	}
	public void spawnRandomPowerup(LogicEngine in_logicEngine, float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/powerups.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
		
		//not lethal to anyone
		go.allegiance = GameObject.ALLEGIANCES.NONE;
		
		
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrameRow = 0;
		Powerup p=null;

		switch(r.nextInt(7))
		{
		case 0:
			 p = new ShieldPowerup();
			 go.i_animationFrame=1;
			//apply powerup to all ships
				go.collisionHandler = new PowerupCollision(p,true);
			break;
		case 1 : 
			p = new RapidFirePowerup();
			go.i_animationFrame=0;
			//apply powerup to all ships
			go.collisionHandler = new PowerupCollision(p,true);
			break;
		case 2 : 
			p = new DualFirePowerup();
			go.i_animationFrame=2;
			//apply powerup to all ships
			go.collisionHandler = new PowerupCollision(p,true);
			break;
		case 3 : 
			p= new WingmenPowerup();
			go.i_animationFrame=0;
			go.i_animationFrameRow=1;
			//apply powerup once only!
			go.collisionHandler = new PowerupCollision(p,false);
			break;
		case 4 : 
			p= new MissilePowerup();
			go.i_animationFrame=1;
			go.i_animationFrameRow=1;
			//apply powerup once only!
			go.collisionHandler = new PowerupCollision(p,true);
			break;
		case 5 : 
			p= new MinesPowerup();
			go.i_animationFrame=2;
			go.i_animationFrameRow=1;
			//apply powerup once only!
			go.collisionHandler = new PowerupCollision(p,false);		
			break;
		case 6 : 
			p= new SlowFieldPowerup();
			go.i_animationFrame=3;
			go.i_animationFrameRow=1;
			//apply powerup once only!
			go.collisionHandler = new PowerupCollision(p,false);		
			break;
		}
		
		
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-2.5)));
		in_logicEngine.objectsPowerups.add(go);
	}
	
	public void spawnSeeker(LogicEngine in_logicEngine,float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/seeker.png" , in_x,  LogicEngine.rect_Screen.getHeight()+50,0);
		//give it seeker behaviour
		
		
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		go.v.setMaxForce(1);
		go.v.setMaxVel(5);
		
		if( Difficulty.isMedium())
			go.v.setMaxVel(7);
		
		if( Difficulty.isHard())
			go.v.setMaxVel(10);
		
		go.stepHandlers.add( new SeekNearestPlayerStep(1000.0));
		
		
		go.collisionHandler = new DestroyIfEnemyCollision(go,7.0,true);
		
			
		//bullet inherits the allegiance of whoever fired it
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
	
		in_logicEngine.objectsEnemies.add(go);
	}
	
	
	GameObject spawnBubble(LogicEngine in_logicEngine,float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+20,0);
		
		//lethal to everyone
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
	//	double xDrift = (Math.random()*2.0)-1.0;
		go.i_animationFrameSizeWidth=40;
		go.i_animationFrameSizeHeight=37;
		go.i_animationFrame = 0;
		
		
		go.v.setMaxForce(0.1);
		go.v.setMaxVel(3);
		go.stepHandlers.add( new SeekNearestPlayerStep(1000));
		
		//hitpoint collision with explosion
		SplitCollision c =  new SplitCollision(go,4, 32.0);
		c.setSimpleExplosion();
		c.c_flashColor = new Color(0,0,1,1);
		
		//---------------SUB BUBBLE---------------
		for(int i=0;i<2;i++)
		{
			GameObject go2 = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
			go2.i_animationFrameRow = 0;
			go2.i_animationFrame =0;
			go2.i_animationFrameSizeWidth =40;
			go2.i_animationFrameSizeHeight =37;
			go2.f_forceScaleX = 0.75f;
			go2.f_forceScaleY = 0.75f;
			go2.allegiance = GameObject.ALLEGIANCES.ENEMIES;
	
			if(i==0)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-1)));
			if(i==1)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-1)));
			if(i==2)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-2)));
			if(i==3)
				go2.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-2)));
			
			go2.v.setMaxForce(2);
			go2.v.setMaxVel(3);
			go2.stepHandlers.add( new SeekNearestPlayerStep(1000));
			
			SplitCollision c2 =  new SplitCollision(go2,4, 25.0);
			
			//-------------SUB-SUB BUBBLE--------------
			for(int j=0;j<4;j++)
			{
				GameObject go3 = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,LogicEngine.rect_Screen.getHeight()+50,0);
				go3.i_animationFrameRow = 0;
				go3.i_animationFrame =0;
				go3.i_animationFrameSizeWidth =40;
				go3.i_animationFrameSizeHeight =37;
				go3.f_forceScaleX = 0.5f;
				go3.f_forceScaleY = 0.5f;
				go3.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
				if(j==0)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-1)));
				if(j==1)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(-1,-2)));
				if(j==2)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-1)));
				if(j==3)
					go3.stepHandlers.add(new FlyStraightStep(new Vector2d(1,-2)));
				
				
				HitpointShipCollision c3 =  new HitpointShipCollision(go3,1, 15.0);
				
				c3.setSimpleExplosion();
				
				go3.collisionHandler = c3;
				
				go3.v.setMaxForce(2);
				go3.v.setMaxVel(3);
				go3.stepHandlers.add( new SeekNearestPlayerStep(1000));
				
				c2.splitObjects.add(go3);
			}
			
			//set the bubble to pop into another bubble
			
			c2.c_flashColor = new Color(0,0,1,1);
			c2.setSimpleExplosion();
			c.splitObjects.add(go2);
			go2.collisionHandler = c2;
		}

		go.collisionHandler = c;
		
		in_logicEngine.objectsEnemies.add(go);
		
		return go;
	
	}
	
	public void spawnSateliteShip(LogicEngine in_logicEngine,float in_x)
	{

		
		//spawn at left edge
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",in_x,LogicEngine.SCREEN_HEIGHT,1);
		ship.i_animationFrameSizeWidth = 64;
		ship.i_animationFrameSizeHeight = 32;
		ship.i_animationFrame = 2;
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		ship.v.setMaxForce(1.0f);
		ship.v.setMaxVel(4.0f);
		
		
		//fly down
		//ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f)));
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "10", null);
		
		//add waypoints
		for(int i=0 ; i< 20 ; i++)
		{
						
			//put in some edge ones too
			if(r.nextInt(6)%3==0)
				ship.v.addWaypoint(new Point2d((int) LogicEngine.SCREEN_WIDTH * (i%2), LogicEngine.SCREEN_HEIGHT/1.5 + r.nextInt((int) LogicEngine.SCREEN_HEIGHT/5)));
			else
				//put in some random doublebacks etc
				ship.v.addWaypoint(new Point2d(r.nextInt((int) LogicEngine.SCREEN_WIDTH), LogicEngine.SCREEN_HEIGHT/1.5 + r.nextInt((int) LogicEngine.SCREEN_HEIGHT/5)));
		}
		ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2, -100));
		
		if(Difficulty.isEasy())
			ship.shotHandler = new BeamShot(50);
		else
		if(Difficulty.isMedium())
			ship.shotHandler = new BeamShot(40);
		else
		if(Difficulty.isHard())
			ship.shotHandler = new BeamShot(30);
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		ship.stepHandlers.add(b);
		
		//destroy ships that get too close
		HitpointShipCollision hps = new HitpointShipCollision(ship, 10, 32);
		hps.setSimpleExplosion();
		
		ship.collisionHandler = hps; 
		
		in_logicEngine.objectsEnemies.add(ship);	
	
	}
	

	private void loadNextLevel(LogicEngine in_logicEngine)
	{
		//clear enemies
		in_logicEngine.objectsEnemies.clear();
		in_logicEngine.objectsEnemyBullets.clear();
		in_logicEngine.objectsObstacles.clear();
		in_logicEngine.objectsPlayerBullets.clear();
		in_logicEngine.objectsPowerups.clear();
		in_logicEngine.objectsUnderlay.clear();
		in_logicEngine.objectsOverlay.clear();
		in_logicEngine.currentTextBeingDisplayed.clear();
		in_logicEngine.currentAreaEffects.clear();
		
		
		

		if(b_refreshLivesBetweenLevels)
		{
			//TODO only this is a hack so it only marks stuff completed when done on normal mode
			Difficulty.setLevelCompleted(level,Difficulty.difficulty);
			
			in_logicEngine.resAllDeadPlayers();
			in_logicEngine.MyLifeCounter.refreshLives();
			
			
			
		}
		
		level++;
				
	}
	public Level getCurrentLevel() {
		
		switch(level)
		{
			case 0: return level0;
			case 1: return level1;
			case 2: return level2;
			case 3: return level3;
			case 4: return level4;
			case 5: return level5;
			case 6: return level6;
			case 7: return level7;
			case 8: return level8;
			case 9: return level9;
			case 10: return level10;
			case 11: return level11;
			default: return null;
		}
	}
	
	public void handleStep(LogicEngine in_logicEngine)
	{	
		switch(level)
		{
			case 0: if(level0.stepLevel(this, in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 1: if(level1.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 2: if(level2.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 3: if(level3.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 4: if(level4.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 5: if(level5.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 6: if(level6.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 7: if(level7.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 8: if(level8.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 9: if(level9.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 10: if(level10.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
			
			case 11: if(level11.stepLevel(this,in_logicEngine))
				loadNextLevel(in_logicEngine);
			break;
		}
	}
	
	//create dense mine wave
	/*
	 * 	(creates mines in this pattern), pass true for the second, fourth row etc)
	 *  	x	x	x	
	 * 	      x   x   x
	 * 		x	x	x	
	 * 	      x   x   x
	 * 
	 */
	void mineWave( LogicEngine in_logicEngine,int in_numberOfMines, boolean b_isStaggered,boolean randomX)
	{
		int i_numberOfMines=in_numberOfMines;
		if(randomX)
			i_numberOfMines=1;
		
		
		//String in_spritename, double in_x, double in_y, boolean in_rotateToV,int in_shootEverySteps
		for(int i=0 ; i< i_numberOfMines ; i++)
		{
			GameObject mine = new GameObject("data/"+GameRenderer.dpiFolder+"/mine.png",((double)i+0.5)* (LogicEngine.SCREEN_WIDTH/i_numberOfMines+1),LogicEngine.SCREEN_HEIGHT+5,5);
			
			//to do a interleafed pattern
			if(b_isStaggered)
				mine.v.setX(mine.v.getX()+(LogicEngine.SCREEN_WIDTH/((i_numberOfMines+1)*2)));
			else
				if(randomX)
					mine.v.setX(LogicEngine.SCREEN_WIDTH * Math.random());
			
			mine.i_animationFrame=0;
			mine.i_animationFrameSizeWidth=16;
			mine.i_animationFrameSizeHeight=16;
			

			mine.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-2)));
			
			HitpointShipCollision c = new HitpointShipCollision(mine,3,10);
			c.setSimpleExplosion();
			
			mine.shotHandler = new ExplodeIfInRange(true);
			
			mine.collisionHandler =c; 
			mine.allegiance = GameObject.ALLEGIANCES.LETHAL;
			
			in_logicEngine.objectsEnemies.add(mine);
		}
	}
	
	public void spawnWanderingSeeker(LogicEngine in_logicEngine)
	{
		//spawn left and right halves next to one another
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",(float)LogicEngine.SCREEN_WIDTH*Math.random(),LogicEngine.SCREEN_HEIGHT-10,0);
				
		//set animation frame
		ship.i_animationFrame=1;
		ship.i_animationFrameRow=0;
		ship.i_animationFrameSizeWidth=16;
		ship.i_animationFrameSizeHeight=16;
		
		//wander 	
		CustomBehaviourStep cb = new CustomBehaviourStep(new Wander(-2.5,2.5,20,0.1));
		ship.stepHandlers.add( cb);
		
		//chase player if on medium or hard
		if(Difficulty.difficulty == DIFFICULTY.MEDIUM || 
				Difficulty.difficulty == DIFFICULTY.HARD)
		{	
			SeekNearestPlayerStep sps = new SeekNearestPlayerStep(100);
			ship.stepHandlers.add(sps);
		}
	
		ship.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-2)));
		ship.collisionHandler = new DestroyIfEnemyCollision(ship, 10, true);
		ship.v.setMaxForce(2);
		ship.v.setMaxVel(2);
		
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		in_logicEngine.objectsEnemies.add(ship);
		
	}
	public GameObject spawnSlime(LogicEngine in_logicEngine, double in_x) {
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/slime.png" , in_x,  LogicEngine.rect_Screen.getHeight()+50,0);
		//give it seeker behaviour
		
		
		go.v.setMaxForce(5);
		go.v.setMaxVel(10);
		//seeks players when close otherwise just flies straight down, also animates between frame 1 and 2 every 5 steps
		go.stepHandlers.add( new SeekNearestPlayerStep(120.0));
		go.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-3.0)));
		go.stepHandlers.add( new LoopingAnimationStep(5,2));
		
		Drawable slimeDebuff = new Drawable();

		slimeDebuff.i_animationFrameRow = 1;
		slimeDebuff.i_animationFrameSizeWidth = 16;
		slimeDebuff.i_animationFrameSizeHeight = 16;
		
		slimeDebuff.str_spritename = "data/"+GameRenderer.dpiFolder+"/slime.png";
		
		go.collisionHandler = new PowerupCollision(new MovementPowerup(0.10f,slimeDebuff),false,true,go);
			
		go.i_animationFrame=0;
		go.i_animationFrameSizeWidth=16;
		go.i_animationFrameSizeHeight=16;
		//bullet inherits the allegiance of whoever fired it
		go.allegiance = GameObject.ALLEGIANCES.NONE;
		
		
		in_logicEngine.objectsEnemies.add(go);
		
		return go;
		
	}
	
	public void spawnTurretShip(LogicEngine in_logicEngine,float in_x)
	{
		//turret
		Drawable turret = new Drawable();
		turret.i_animationFrame = 6;
		turret.i_animationFrameSizeWidth=16;
		turret.i_animationFrameSizeHeight=16;
		turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
		turret.f_forceRotation = 90;
		
		//spawn at left edge
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png",in_x,LogicEngine.SCREEN_HEIGHT,20);
		ship.i_animationFrameSizeWidth = 32;
		ship.i_animationFrameSizeHeight = 48;
		ship.i_animationFrame = 2;
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		ship.v.setMaxForce(2.0f);
		ship.v.setMaxVel(6.0f);
		
		ship.visibleBuffs.add(turret);
		//fly down
		ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f)));
		ship.shotHandler = new TurretShot(turret,"data/"+GameRenderer.dpiFolder+"/redbullets.png",6,5.0f);
		
		if(Difficulty.isEasy())
			ship.collisionHandler = new HitpointShipCollision(ship, 6, 32,true);
		else		
		if(Difficulty.isMedium())
		{
			ship.shootEverySteps=15;
			ship.collisionHandler = new HitpointShipCollision(ship, 6, 32,true);
		}
		if(Difficulty.isHard())
		{
			ship.shootEverySteps=10;
			ship.collisionHandler = new HitpointShipCollision(ship, 8, 32,true);
		}
		//destroy ships that get too close
		
		
		
		in_logicEngine.objectsEnemies.add(ship);	
	
	}
	
	public void spawnBlackHole( LogicEngine in_logicEngine,float in_x)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/gravitonlevel.png", in_x,LogicEngine.SCREEN_HEIGHT,0);
		ship.i_animationFrameSizeWidth = 64;
		ship.i_animationFrameSizeHeight = 64;
		ship.allegiance = GameObject.ALLEGIANCES.LETHAL;
		
		ship.v.setMaxForce(2.0f);
		ship.v.setMaxVel(1.0f);
		
		//wander
		CustomBehaviourStep cb = new CustomBehaviourStep(new Wander(-2.5,2.5,20,1));
		ship.stepHandlers.add( cb);
		ship.stepHandlers.add( new FlyStraightStep(new Vector2d(0,-1f)));
		
		//pull ships
		ship.stepHandlers.add(new PullShipsStep(100,0.05f,true));
		
		
		//destroy ships that get too close
		ship.collisionHandler = new HitpointShipCollision(ship, 100, 32);
		
		
		in_logicEngine.objectsObstacles.add(ship);	
	}
	
	public GameObject spawnCarrier(LogicEngine in_logicEngine, float in_x , CARRIER_TYPE in_type)
	{
		
		//spawn left and right halves next to one another
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/thrusterboss.png",in_x,LogicEngine.SCREEN_HEIGHT+64,30);
		
		
		//set animation frame
		ship.i_animationFrame=4;
		ship.i_animationFrameRow=0;
		ship.i_animationFrameSizeWidth=32;
		ship.i_animationFrameSizeHeight=132;
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		//pause at the first waypoint
		followPathBehaviour.waitAtWaypoint(1, 50);
		
		followPathBehaviour.setInfluence(1);
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		followPathBehaviour.waitAtWaypoint(1, 300);
		
		//go straight down then split
		ship.v.addWaypoint(new Point2d(in_x, LogicEngine.SCREEN_HEIGHT/1.5));
		ship.v.addWaypoint(new Point2d(in_x,-100));
		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		
		//turret
		//turret
		Drawable turret = new Drawable();
		turret.i_animationFrame = 6;
		turret.i_animationFrameSizeWidth=16;
		turret.i_animationFrameSizeHeight=16;
		turret.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
		turret.f_forceRotation = 90;
		ship.shotHandler = new TurretShot(turret,"data/"+GameRenderer.dpiFolder+"/redbullets.png",6,5.0f);
		ship.visibleBuffs.add(turret);
		
		ship.v.setMaxForce(1);
		ship.v.setMaxVel(1);
		
		ship.stepHandlers.add(b);
		
		HitpointShipCollision s;
		if(!Difficulty.isHard())
			 s = new HitpointShipCollision(ship, 50, 32);
		else
			 s = new HitpointShipCollision(ship, 50, 32);
			
		s.setExplosion(Utils.getBossExplosion(ship));
		ship.collisionHandler = s; 
	
		//TODO:launch ships handler for shooting
		if(in_type == CARRIER_TYPE.PATHFINDERS_BOTH_SIDES)
		{
			ship.stepHandlers.add(new LaunchShipsStep(pathFinder(true), 50, 5, 5,true));
			ship.stepHandlers.add(new LaunchShipsStep(pathFinder(false), 50, 5, 5,false));
		}
		else
			if(in_type == CARRIER_TYPE.PATHFINDERS_RIGHT_ONLY)
			{
				ship.stepHandlers.add(new LaunchShipsStep(pathFinder(true), 50, 5, 5,true));
			}
			else
				if(in_type == CARRIER_TYPE.PATHFINDERS_LEFT_ONLY)
				{
					ship.stepHandlers.add(new LaunchShipsStep(pathFinder(false), 50, 5, 5,false));
				}
						 
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		in_logicEngine.objectsEnemies.add(ship);
		
		return ship;
	
	}
	
	
	private GameObject pathFinder(boolean b_goRight)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",(LogicEngine.SCREEN_WIDTH+10),LogicEngine.SCREEN_HEIGHT-10,0);
		
		ship.setRotateToVelocity(true);
		ship.i_animationFrame=0;
		ship.i_animationFrameSizeWidth=16;
		ship.i_animationFrameSizeHeight=16;
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2 + 75,LogicEngine.SCREEN_HEIGHT/1.5));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2 - 75,LogicEngine.SCREEN_HEIGHT/1.5));
		
		ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH/2,25));
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(50,50));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,50));
		
		
		if(b_goRight)
			ship.v.addWaypoint(new Point2d(50,Integer.MAX_VALUE));
		else
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,Integer.MAX_VALUE));
		

		
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		
		
		ship.v.setMaxForce(2.5);
		ship.v.setMaxVel(7.5);
		
	
		
		ship.stepHandlers.add(b);		
		ship.collisionHandler = new DestroyIfEnemyCollision(ship, 6, true);
		
		
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		return ship;
	}

	GameObject spawnWaveRider(LogicEngine in_logicEngine,float in_x)
	{
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/redcube.png",in_x,in_logicEngine.SCREEN_HEIGHT+25,0);
		
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		go.i_animationFrameSizeWidth=40;
		go.i_animationFrameSizeHeight=43;
		go.i_animationFrameRow = 1;
		go.i_animationFrame = 0;
		//go.rotateToV = true;
		
		go.v.setMaxForce(2.5);
		go.v.setMaxVel(100);

		go.stepHandlers.add(new FlyStraightStep(new Vector2d(0,-2)));
		
		HitpointShipCollision collision = new HitpointShipCollision(go, 15, 25);
		
		if(Difficulty.isMedium() || Difficulty.isHard())
		{
			collision.f_numberOfHitpoints = 20;
			go.v.setMaxVel(0.3);
		}
		
		collision.setExplosion(Utils.getMiniBossExplosion(go));
		go.collisionHandler = collision;
		
		LaunchShipsStep l1 = new LaunchShipsStep( spawnTadpole(in_logicEngine), 50, 5, 1, false);
		LaunchShipsStep l2 = new LaunchShipsStep( spawnTadpole(in_logicEngine), 50, 5, 1, true);
		l1.b_addToBullets = true;
		l2.b_addToBullets = true;
	
		if(Difficulty.isMedium())
		{
			l1.i_launchEvery = 40;
			l2.i_launchEvery = 40;
			
		}
		else
		if(Difficulty.isHard())
		{
			l1.i_launchEvery = 30;
			l2.i_launchEvery = 30;
		}
		go.stepHandlers.add(l1);
		go.stepHandlers.add(l2);
	
		in_logicEngine.objectsEnemies.add(go);
		
		//bullet/
		//75 * 12
		return go;
	}
	
	GameObject spawnTadpole(LogicEngine in_logicEngine)
	{
		
		GameObject go = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",0,0,0);
		
		go.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		go.i_animationFrameSizeWidth=5;
		go.i_animationFrameSizeHeight=16;
		go.i_animationFrameRow = 1;
		go.i_animationFrame = 1;
		go.rotateToV = true;
		go.stepHandlers.add( new SeekNearestPlayerStep(1000));
		go.v.setMaxForce(0.25f);
		go.v.setMaxVel(5);
		go.collisionHandler = new DestroyIfEnemyCollision(go, 5, true);
		
		TimedLifeStep destroyAfter = new TimedLifeStep(100);
		destroyAfter.explosion = Utils.getSimpleExplosion();
		go.stepHandlers.add(destroyAfter);
		
		return go;
	}
	
	
	public void pathFinderStraightDown(LogicEngine in_logicEngine , int in_x)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",in_x,LogicEngine.SCREEN_HEIGHT+10,0);
		
		//ship.setRotateToVelocity(true);
		ship.i_animationFrame=0;
		ship.i_animationFrameSizeWidth=16;
		ship.i_animationFrameSizeHeight=16;
				
		ship.v.setMaxForce(2.5);
		ship.v.setMaxVel(7.5);

		ship.stepHandlers.add(new FlyStraightStep(new Vector2d(0f,-7.5f)));
		
		if(Difficulty.isMedium())
		{
			ship.shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png",6 , new Vector2d(0,-10f));
			ship.shootEverySteps = 35;
		}
		
		if(Difficulty.isHard())
		{
			ship.shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png",6 , new Vector2d(0,-10f));
			ship.shootEverySteps = 25;
		}
		
			
		ship.collisionHandler = new DestroyIfEnemyCollision(ship, 6, true);
		
		System.out.println("wp ="+ship.v.getPath().size());
				 
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		in_logicEngine.objectsEnemies.add(ship);
	}
	
	public void pathFinderWave( LogicEngine in_logicEngine, int i_pathType)
	{
		GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",(LogicEngine.SCREEN_WIDTH+10),LogicEngine.SCREEN_HEIGHT-10,0);
		
		ship.setRotateToVelocity(true);
		ship.i_animationFrame=0;
		ship.i_animationFrameSizeWidth=16;
		ship.i_animationFrameSizeHeight=16;
		
		SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
		followPathBehaviour.setInfluence(1);
		
		followPathBehaviour.setAttribute("arrivedistance", "50", null);
		
		switch(i_pathType)
		{
		case 0:
			ship.v.addWaypoint(new Point2d(50,50));		
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,50));
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,LogicEngine.SCREEN_HEIGHT-50));
			ship.v.addWaypoint(new Point2d(-100,-100));
			break;
		case 1:
			ship.v.setX(-10);
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,50));
			ship.v.addWaypoint(new Point2d(50,50));
			ship.v.addWaypoint(new Point2d(50,LogicEngine.SCREEN_HEIGHT-50));
			ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH +100,-100));
			break;
		}
		CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
		
		
		ship.v.setMaxForce(2.5);
		ship.v.setMaxVel(7.5);
		
		if(Difficulty.isMedium())
		{
			ship.v.setMaxForce(5);
			ship.v.setMaxVel(12);
		}
		
		if(Difficulty.isHard())
		{
			ship.v.setMaxForce(10);
			ship.v.setMaxVel(20);
		}
		
		ship.stepHandlers.add(b);		
		ship.collisionHandler = new DestroyIfEnemyCollision(ship, 6, true);
		
		System.out.println("wp ="+ship.v.getPath().size());
				 
		ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
		
		in_logicEngine.objectsEnemies.add(ship);
	}



	public void spawnSplitter(LogicEngine in_logicEngine)
	{
		
		for(int i=0 ; i< 2 ;i++)
		{
			//spawn left and right halves next to one another
			GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/triangle.png",(LogicEngine.SCREEN_WIDTH/2)+7 + ((i-1)*14),LogicEngine.SCREEN_HEIGHT-10,60);
			
			
			//set animation frame
			ship.i_animationFrame=i;
			ship.i_animationFrameRow=1;
			ship.i_animationFrameSizeWidth=16;
			ship.i_animationFrameSizeHeight=32;
			
			SimplePathfollowing followPathBehaviour = new SimplePathfollowing();
			//pause at the first waypoint
			followPathBehaviour.waitAtWaypoint(1, 50);
			
			followPathBehaviour.setInfluence(1);
			followPathBehaviour.setAttribute("arrivedistance", "50", null);
			
			//go straight down then split
			ship.v.addWaypoint(new Point2d((LogicEngine.SCREEN_WIDTH/2)+7 + ((i-1)*14) , LogicEngine.SCREEN_HEIGHT/2));
			
			switch(i)
			{
			case 0: // left ship go to top left
				ship.v.addWaypoint(new Point2d(50,LogicEngine.SCREEN_HEIGHT - 50));		
				ship.v.addWaypoint(new Point2d(50,-100));
				break;
			case 1: //right ship go to top right
				ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,LogicEngine.SCREEN_HEIGHT - 50));		
				ship.v.addWaypoint(new Point2d(LogicEngine.SCREEN_WIDTH-50,-100));
				break;
			}
			CustomBehaviourStep b = new CustomBehaviourStep(followPathBehaviour);
			
			
			ship.v.setMaxForce(1);
			ship.v.setMaxVel(2);
			
			ship.stepHandlers.add(b);
			
			HitpointShipCollision s;
			if(!Difficulty.isHard())
				 s = new HitpointShipCollision(ship, 7, 13);
			else
				 s = new HitpointShipCollision(ship, 12, 13);
				
			s.setSimpleExplosion();
			ship.collisionHandler = s; 
			
			//normal shot
			if(Difficulty.isEasy())
				ship.shotHandler= new StraightLineShot("data/"+GameRenderer.dpiFolder+"/redbullets.png", 6, new Vector2d(0,-9f));
			
			//beams
			if(Difficulty.isMedium() )
			{
				ship.shootEverySteps=1;
				ship.shotHandler= new BeamShot(75);
			}
			if(	Difficulty.isHard())
			{
				ship.shootEverySteps=1;
				ship.shotHandler= new BeamShot(50);
			}
							 
			ship.allegiance = GameObject.ALLEGIANCES.ENEMIES;
			
			in_logicEngine.objectsEnemies.add(ship);
		}
	}




	
	

}
