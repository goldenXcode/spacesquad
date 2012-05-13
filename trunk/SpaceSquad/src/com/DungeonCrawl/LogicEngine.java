package com.DungeonCrawl;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.DungeonCrawl.AreaEffects.AreaEffect;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Difficulty.DIFFICULTY;
import com.DungeonCrawl.GameObject.ALLEGIANCES;
import com.DungeonCrawl.KdTree.Entry;
import com.DungeonCrawl.Levels.LevelManager;
import com.DungeonCrawl.Powerups.DualFirePowerup;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Powerups.RapidFirePowerup;
import com.DungeonCrawl.Powerups.ShieldPowerup;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StaticAnimationStep;
import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Vector2;

import de.steeringbehaviors.simulation.behaviors.Arrive;
import de.steeringbehaviors.simulation.behaviors.Behavior;
import de.steeringbehaviors.simulation.behaviors.Flocking;
import de.steeringbehaviors.simulation.behaviors.Seek;
import de.steeringbehaviors.simulation.renderer.Geometrie;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Neighborhood;


public class LogicEngine implements Runnable
{


	
	//leave at 4 for now wont work if changed
	public static final int NUMBER_OF_PLAYER_SHIPS = 4;
	public MyInput MyInputProcessor;
	
	
	
	
	//PENDING - add/remove manage this properly
	public ReentrantReadWriteLock objectsObstaclesLock = new ReentrantReadWriteLock();
	
	public ArrayList<GameObject> objectsObstacles;
	public ArrayList<GameObject> objectsUnderlay;
	public ArrayList<GameObject> objectsPlayers;
	public ArrayList<GameObject> objectsPlayerBullets;
	public ArrayList<GameObject> objectsEnemyBullets;
	public ArrayList<GameObject> objectsEnemies;
	public ArrayList<GameObject> toAddObjectsEnemies = new ArrayList<GameObject>();
	public ArrayList<GameObject> toAddObjectsObstacles = new ArrayList<GameObject>();
	
	public ArrayList<GameObject> objectsPowerups;
	public ArrayList<GameObject> objectsOverlay;
	
	public KdTree<GameObject> kd_obstacles = null;
	public KdTree<GameObject> kd_enemies = null;
	//public KdTree<GameObject> kd_players = null;
	//public KdTree<GameObject> kd_playerBullets = null;
	//public KdTree<GameObject> kd_enemyBullets = null;
	
	//public KdTree<GameObject> kd_powerups = null;
	
	
	public ArrayList<AreaEffect> currentAreaEffects;
	public ArrayList<AreaEffect> currentAreaEffectsPlayers;
	public ArrayList<TextDisplaying> currentTextBeingDisplayed;
	
	public LifeCounter MyLifeCounter;
	public LevelManager MyLevelManager;
	
	public static Rect rect_Screen;
	public static float SCREEN_WIDTH;
	public static float SCREEN_HEIGHT;
	
	public int i_stepCounter=0;
	public boolean b_paused=true;
	private int l_collisionDurationCount;
	private long l_collisionDurationLast;
	private int l_collisionDurationCounter;
	public Background background;
	


	public static float f_worldCoordTop;
	public static float f_worldCoordRight;
	
	public boolean areAllPlayersDead()
	{
		int i_countOfDead = 0;
		
		for(int i=0;i<objectsPlayers.size();i++)
			for(int j=0;j<objectsPlayers.get(i).stepHandlers.size();j++)
				if(objectsPlayers.get(i).stepHandlers.get(j) instanceof PlayerStep)
					if(((PlayerStep)objectsPlayers.get(i).stepHandlers.get(j)).b_isDead)
						i_countOfDead++;
			
		if(i_countOfDead == NUMBER_OF_PLAYER_SHIPS)
			return true;
		
		return false;
	}
	
	public void resAllDeadPlayers()
	{
		//Resurrect any dead players
		for(int i=0;i<objectsPlayers.size();i++)
			for(int j=0;j<objectsPlayers.get(i).stepHandlers.size();j++)
				if(objectsPlayers.get(i).stepHandlers.get(j) instanceof PlayerStep)
					if(((PlayerStep)objectsPlayers.get(i).stepHandlers.get(j)).b_isDead)
						((PlayerStep)objectsPlayers.get(i).stepHandlers.get(j)).resurectPlayer(this);
			
	}
	

	

	

	private void buildKdTrees(LogicEngine in_theLogicEngine)
	{	
		//build kd tree
		kd_enemies = new KdTree.Manhattan<GameObject>(2,300);
		kd_obstacles = new KdTree.Manhattan<GameObject>(2,300);
	//	kd_players = new KdTree.Manhattan<GameObject>(2,300);
		//kd_enemyBullets = new KdTree.Manhattan<GameObject>(2,300);
		//kd_playerBullets = new KdTree.Manhattan<GameObject>(2,300);
		//kd_powerups = new KdTree.Manhattan<GameObject>(2,300);

				
		fillKdTree(kd_enemies,objectsEnemies);
		fillKdTree(kd_obstacles,objectsObstacles);
		//fillKdTree(kd_players,objectsPlayers);
		//fillKdTree(kd_enemyBullets,objectsEnemyBullets);
		//fillKdTree(kd_playerBullets,objectsPlayerBullets);
		//fillKdTree(kd_powerups,objectsPowerups);
	}
	
	private void fillKdTree( KdTree<GameObject> in_tree,ArrayList<GameObject> in_list )
	{
		//build kd tree of enemies
		for(int i=0;i<in_list.size();i++)
		{
			GameObject go_currentObject = in_list.get(i);
			if(!go_currentObject.str_name.equals("wall"))
				in_tree.addPoint(new double[]{go_currentObject.v.getX(),go_currentObject.v.getY()}, go_currentObject);
		}
		
	}
	
	public void init(MyInput in_processor)
	{
		MyInputProcessor = in_processor;
		background = new Background();
	}
	public LogicEngine(){
		
				
		//TODO: 
		/*
		 * 
		 * Score
		 *  Refugee transports
		 *  Carrier
		 *  Elite units
		 *  Levels
		 *  Sneaky level number attacks you
		 *  Green Goo
		 *  
		 * 
		 * 
		 * 
		 */
		Difficulty.difficulty = DIFFICULTY.EASY;
		
		
		GameRenderer.f_pixelAdjustX = (float)Gdx.graphics.getWidth()/320f;
		GameRenderer.f_pixelAdjustY = (float)Gdx.graphics.getHeight()/480f;
		
		GameRenderer.f_pixelAdjustAbsoluteX = GameRenderer.f_pixelAdjustX;
		GameRenderer.f_pixelAdjustAbsoluteY = GameRenderer.f_pixelAdjustY;
		
		GameRenderer.f_pixelAdjustX = Math.min(GameRenderer.f_pixelAdjustX, GameRenderer.f_pixelAdjustY);
		GameRenderer.f_pixelAdjustY = Math.min(GameRenderer.f_pixelAdjustX, GameRenderer.f_pixelAdjustY);
		

		GameRenderer.dpiFolder ="mdpi";
		GameRenderer.dpiTextureCoordinatesAdjust = 1.0f;
		
		if(GameRenderer.f_pixelAdjustX >= 1.5)
		{
			GameRenderer.dpiFolder ="hdpi";
			GameRenderer.dpiTextureCoordinatesAdjust = 1.5f;
		}
		
		if(GameRenderer.f_pixelAdjustX >= 2)
		{
			GameRenderer.dpiFolder ="xdpi";
			GameRenderer.dpiTextureCoordinatesAdjust = 2f;
		}
		 
		
		
		float f_extraSpaceTop = (float)Gdx.graphics.getHeight() - ( GameRenderer.f_pixelAdjustX * 480f);
		f_extraSpaceTop = Math.max(0, f_extraSpaceTop);
		
		float f_extraSpaceRight = (float)Gdx.graphics.getWidth()- ( GameRenderer.f_pixelAdjustY * 320f);
		f_extraSpaceRight = Math.max(0, f_extraSpaceRight);
		
		f_worldCoordTop = 480 + (f_extraSpaceTop/GameRenderer.f_pixelAdjustX);
		f_worldCoordRight = 320 + (f_extraSpaceRight/GameRenderer.f_pixelAdjustY);
		 
		
		rect_Screen = new Rect(0,0,f_worldCoordRight , LogicEngine.f_worldCoordTop);//Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		MyLifeCounter = new LifeCounter();
		MyLevelManager = new LevelManager();
		System.out.println("screen is size:" +  rect_Screen.getWidth()+","+ rect_Screen.getHeight());
		
		SCREEN_WIDTH = (int)rect_Screen.getWidth(); //Gdx.graphics.getWidth();
		SCREEN_HEIGHT = (int)rect_Screen.getHeight(); //Gdx.graphics.getHeight();
		
		
		
		objectsUnderlay = new ArrayList<GameObject>();
		objectsPlayers = new ArrayList<GameObject>();
		objectsPlayerBullets = new ArrayList<GameObject>();
		objectsEnemies = new ArrayList<GameObject>();
		objectsEnemyBullets = new ArrayList<GameObject>();
		objectsObstacles = new  ArrayList<GameObject>();
		objectsPowerups = new ArrayList<GameObject>();
		objectsOverlay = new ArrayList<GameObject>();
		currentAreaEffects= new ArrayList<AreaEffect>();
		currentAreaEffectsPlayers = new ArrayList<AreaEffect>();
		
		currentTextBeingDisplayed = new ArrayList<TextDisplaying>();
		

		
		
	}
	


	
	public void processStep() throws Exception
	{
		long l_timestamp2;
		l_timestamp2 = System.nanoTime();
		
		MyInputProcessor.stepInputProcessor();
		
		//if paused
		if(b_paused)
		{
			if(theUI != null)
				theUI.stepUI(this);
			return;
		}
		
		//or if game is over
		if(MyLifeCounter.isGameOver(this))
			return;
		
	
		
		
		//keep track of memory leaks!
		
		if(this.i_stepCounter %100 ==0)
		System.out.println(this.objectsPlayerBullets.size() + ","+
		this.objectsEnemies.size() + ","+
		this.objectsOverlay.size() + ","+
		this.objectsPlayers.size() + ","+
		this.objectsPowerups.size() + ","+
		this.objectsUnderlay.size() + ",");

		//get previous time
	

		long l_timestamp;
		l_timestamp = System.nanoTime();
		//do stuff
		buildKdTrees(this);
		
		if(UI.DEBUG)
			System.out.println("kd trees:" + (System.nanoTime() - l_timestamp ));
		
		l_timestamp = System.nanoTime();
		
		background.stepBackground();
		
		i_stepCounter++;
		
		//if the previous step qued up any adds, add them now
		if(toAddObjectsEnemies.size() >0)
		{
			objectsEnemies.addAll(toAddObjectsEnemies);
			toAddObjectsEnemies.clear();
		}
		
		if(toAddObjectsObstacles.size() >0)
		{
			objectsObstaclesLock.writeLock().lock();
			objectsObstacles.addAll(toAddObjectsObstacles);
			toAddObjectsObstacles.clear();
			objectsObstaclesLock.writeLock().unlock();
		}
		
		
		
		MyLevelManager.handleStep(this);

		if(UI.DEBUG)
			System.out.println("step level:" + (System.nanoTime() - l_timestamp ));
		
		l_timestamp = System.nanoTime();
		
		
		//call processStep on children to do shootng/collision detection etc
		for(int i=0;i<objectsPlayers.size();i++)
		{
			objectsPlayers.get(i).processStep(this);
		}
		
		for(int i=0;i<objectsPlayerBullets.size();i++)
			if(objectsPlayerBullets.get(i).processStep(this))
			{
				
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsPlayerBullets.get(i);
				objectsPlayerBullets.remove(i);
				toDelete.dispose();
				i--;
			}
		
		
		for(int i=0;i<objectsUnderlay.size();i++)
			if(objectsUnderlay.get(i).processStep(this)) //we are to delete this object
			{
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsUnderlay.get(i);
				objectsUnderlay.remove(i);
				toDelete.dispose();
				i--;
			}
		
		for(int i=0;i<objectsEnemies.size();i++)
			if(objectsEnemies.get(i).processStep(this)) //we are to delete this object
			{
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsEnemies.get(i);
				objectsEnemies.remove(i);
				toDelete.dispose();
				i--;
			}
		
		for(int i=0;i<objectsOverlay.size();i++)
			if(objectsOverlay.get(i).processStep(this)) //we are to delete this object
			{
				
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsOverlay.get(i);
				objectsOverlay.remove(i);
				toDelete.dispose();
				i--;
			}
		
		
		this.objectsObstaclesLock.writeLock().lock();
		for(int i=0;i<objectsObstacles.size();i++)
			if(objectsObstacles.get(i).processStep(this)) //we are to delete this object
			{
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsObstacles.get(i);
				objectsObstacles.remove(i);
				toDelete.dispose();
				i--;
				
			}
		
		this.objectsObstaclesLock.writeLock().unlock();

		
		for(int i=0;i<objectsEnemyBullets.size();i++)
			if(objectsEnemyBullets.get(i).processStep(this)) //we are to delete this object
			{
				
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsEnemyBullets.get(i);
				objectsEnemyBullets.remove(i);
				toDelete.dispose();
				i--;
			}
		
		
		for(int i=0;i<objectsPowerups.size();i++)
			if(objectsPowerups.get(i).processStep(this)) //we are to delete this object
			{
				
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsPowerups.get(i);
				objectsPowerups.remove(i);
				toDelete.dispose();
				i--;
			}
		
		if(UI.DEBUG)
			System.out.println("step rest:" + (System.nanoTime() - l_timestamp ));
		
		
		for(int i=0;i<currentAreaEffects.size();i++)
			currentAreaEffects.get(i).stepEffect(this);
		
		for(int i=0;i<currentAreaEffectsPlayers.size();i++)
			currentAreaEffectsPlayers.get(i).stepEffect(this);
		
		
		l_timestamp = System.nanoTime();
		runCollisions();
		
		if(UI.DEBUG)
			System.out.println("collisions rest:" + (System.nanoTime() - l_timestamp ));
		l_timestamp = System.nanoTime();
		
		l_collisionDurationCount+= System.currentTimeMillis() - l_collisionDurationLast;
		l_collisionDurationCounter++;
		
		if(UI.DEBUG)
			System.out.println("total processStep" + (System.nanoTime() - l_timestamp2)); 
	}

	private void collideArrays(ArrayList<GameObject> in_list1,ArrayList<GameObject> in_list2,KdTree<GameObject> in_tree)
	{
		//for each object in the list
		for(int i=0 ; i<in_list1.size();i++)
		{
			GameObject go_testing = in_list1.get(i);
			
			//if this object has a collision handler
			if(go_testing.collisionHandler !=null)
			{
				//get closest neighbour
				List<Entry<GameObject>> go_closestObject = in_tree.nearestNeighbor(new double[]{go_testing.v.getX(),go_testing.v.getY()}, 1, true);
				
				//see if it is within range of collision
				if(go_closestObject.size()!= 0) //if at least one result was returned
				{
					Entry<GameObject> closest = go_closestObject.get(0); 
					if(closest.value.collisionHandler != null) //if colliding with thing has a collision handler
						if(closest.distance < Math.max(closest.value.collisionHandler.getCollisionRadius(),go_testing.collisionHandler.getCollisionRadius())) //if within range
						{
							//nearest neighbour is within collision radius
							if(closest.value.collisionHandler.handleCollision(go_testing, this))
								in_list2.remove(closest.value);
							
							if(go_testing.collisionHandler.handleCollision(closest.value, this))
								in_list1.remove(go_testing);
							
						}
				}
			}
		}
	
	}
	
	private void collideArrays(ArrayList<GameObject> in_list1,ArrayList<GameObject> in_list2)
	{
		int index1=0;
		int index2=0;
		
		//while there are more nodes
		while(index1 < in_list1.size() && index2 < in_list2.size())
		{
			GameObject go1 = in_list1.get(index1);
			GameObject go2 = in_list2.get(index2);
			
			//if this object doesn't have a collision handler go to next go1
			if(go1.collisionHandler != null)
			{
				if(go2.collisionHandler != null)
					if(go1._ymin < go2._ymax && go1._ymax > go2._ymin) //y collision is overlapping
					{
						if(go1._xmin < go2._xmax && go1._xmax > go2._xmin)
						{
							//nearest neighbour is within collision radius
							if(go1.collisionHandler.handleCollision(go2, this))
								in_list1.remove(go1);
							
							if(go2.collisionHandler.handleCollision(go1, this))
								in_list2.remove(go2);
						}
							
						//see if x is overlapping
					}
			}
			
			//advance one of the indexes
			if(go1._ymin < go2._ymin)
				if(index1+1 != in_list1.size()) //as long as its not the last node in the list advance list 1
					index1++;
				else
					index2++;
			else
				if(index2+1 != in_list2.size())
					index2++; //as long as its not the last node in the list advance list 1
				else 
					index1++;
			
			
			
		}
	
	}
	
	private void runCollisions()
	{
		calculateMinMax(objectsPlayers);
		calculateMinMax(objectsEnemies);
		calculateMinMax(objectsObstacles);
		calculateMinMax(objectsEnemyBullets);
		calculateMinMax(objectsPlayerBullets);
		calculateMinMax(objectsPowerups);
		
		Collections.sort(objectsPlayers);
		Collections.sort(objectsEnemies);
		
		this.objectsObstaclesLock.writeLock().lock();
		Collections.sort(objectsObstacles);
		this.objectsObstaclesLock.writeLock().unlock();
		
		Collections.sort(objectsEnemyBullets);
		Collections.sort(objectsPlayerBullets);
		Collections.sort(objectsPowerups);
		
		
		//collide player with enemies, obstacles and enemy bullets
		collideArrays(objectsPlayers,objectsEnemies);
		collideArrays(objectsPlayers,objectsEnemyBullets);
		
		this.objectsObstaclesLock.writeLock().lock();
		collideArrays(objectsPlayers,objectsObstacles);
		collideArrays(objectsEnemies,objectsObstacles);
		collideArrays(objectsPlayerBullets,objectsObstacles);
		this.objectsObstaclesLock.writeLock().unlock();
	
		//collide powerups with players
		collideArrays(objectsPowerups,objectsPlayers);
		
		//collide bullets with enemies & obstacles
		collideArrays(objectsPlayerBullets,objectsEnemies);
		
	}

	private void calculateMinMax(ArrayList<GameObject>in_list) {
		for(int i=0;i<in_list.size();i++)
		{
			GameObject g = in_list.get(i);
			if(g.collisionHandler!=null)
			{
				g._xmin = (int) (g.v.getX() - g.collisionHandler.getCollisionRadius());
				g._xmax = (int) (g.v.getX() + g.collisionHandler.getCollisionRadius());
				g._ymin = (int) (g.v.getY() - g.collisionHandler.getCollisionRadius());
				g._ymax = (int) (g.v.getY() + g.collisionHandler.getCollisionRadius());
			}
		}
		
	}

	@Override
	public void run() {
		do{
		    try {
		    	long start = System.currentTimeMillis();
				//record time, 

		    	//take one logical step (PENDING - should probably move background scroll into here)
		    	try {
					processStep();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	//sleep remainder of time - record time
		    	long end = System.currentTimeMillis();
		    	
		    	if(end - start < 40)
		    		Thread.sleep(40 - (end-start));
		    	else
		    		System.out.println("runningSlow");

		    	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return;
			}
		    
		}while(!b_exit);
	
	}

	private UI theUI = null;
	public boolean b_exit = false;
	
	public void setUI(UI in_theUI) {
		// TODO Auto-generated method stub
		theUI = in_theUI;
	}

}
