package com.DungeonCrawl;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.HitpointShipCollision;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Difficulty.DIFFICULTY;
import com.DungeonCrawl.GameObject.ALLEGIANCES;
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
	
	
	KdTree<GameObject> objectsEnemiesTree;
	
	
	
	public ArrayList<GameObject> objectsUnderlay;
	public ArrayList<GameObject> objectsPlayers;
	public ArrayList<GameObject> objectsPlayerBullets;
	public ArrayList<GameObject> objectsEnemyBullets;
	
	//PENDING - add/remove manage this properly
	public ReentrantReadWriteLock objectsObstaclesLock = new ReentrantReadWriteLock();
	
	public ArrayList<GameObject> objectsObstacles;
	
	
	public ArrayList<GameObject> objectsEnemies;
	public ArrayList<GameObject> toAddObjectsEnemies = new ArrayList<GameObject>();
	public ArrayList<GameObject> objectsPowerups;
	public ArrayList<GameObject> objectsOverlay;
	public ArrayList<AreaEffect> currentAreaEffects;
	
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
	
	public void init(MyInput in_processor)
	{
		MyInputProcessor = in_processor;
		background = new Background();
	}
	public LogicEngine(){
		
		objectsEnemiesTree = new KdTree.WeightedManhattan<GameObject>(2,new Integer(500));
		
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
		
		currentTextBeingDisplayed = new ArrayList<TextDisplaying>();
		
		

		
		
	}
	


	
	public void processStep() throws Exception
	{
		
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
	
		
		//do stuff
    
		background.stepBackground();
		
		i_stepCounter++;
		
		//if the previous step qued up any adds, add them now
		if(toAddObjectsEnemies.size() >0)
		{
			objectsEnemies.addAll(toAddObjectsEnemies);
			toAddObjectsEnemies.clear();
		}		
		objectsObstaclesLock.writeLock().lock();
		MyLevelManager.handleStep(this);
		objectsObstaclesLock.writeLock().unlock();

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
		
		

		for(int i=0;i<objectsObstacles.size();i++)
			if(objectsObstacles.get(i).processStep(this)) //we are to delete this object
			{
				objectsObstaclesLock.writeLock().lock();
				//remove self if necessary
				//move object returned true so delete object
				GameObject toDelete = objectsObstacles.get(i);
				objectsObstacles.remove(i);
				toDelete.dispose();
				i--;
				objectsObstaclesLock.writeLock().unlock();
			}

		
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
		
		for(int i=0;i<currentAreaEffects.size();i++)
			currentAreaEffects.get(i).stepEffect(this);
		
		l_collisionDurationLast = System.currentTimeMillis();
		if(l_collisionDurationCounter > 20)
		{
			System.out.println("ms to collision detect:" + (l_collisionDurationCount/20));
			l_collisionDurationCount=0;
			l_collisionDurationCounter=0;
		}
	
	
		runCollisions();
		
		l_collisionDurationCount+= System.currentTimeMillis() - l_collisionDurationLast;
		l_collisionDurationCounter++;
	}

	private void collideArrays(ArrayList<GameObject> in_list1,ArrayList<GameObject> in_list2)
	{
		objectsObstaclesLock.writeLock().lock();
		Collections.sort(in_list1);
		Collections.sort(in_list2);
		objectsObstaclesLock.writeLock().unlock();
		
		if(in_list1.size() == 0 || in_list2.size() ==0)
			return;
		
		
		Iterator<GameObject> iterator1 = in_list1.iterator();
		Iterator<GameObject> iterator2 = in_list2.iterator();
		
		/*
		 * while (setIterator.hasNext()) {
    SomeClass currentElement = setIterator.next();
    if (setOfElementsToRemove(currentElement).size() > 0) {
        setIterator.remove();
    }
}
		 * 
		 */
		
		GameObject go1 = iterator1.next();
		GameObject go2 = iterator2.next();
		
		while(iterator1.hasNext() || iterator2.hasNext())
		{
			if(go1.collisionHandler == null)
				if(iterator1.hasNext())
					go1 = iterator1.next();
				else
					break;
			else
			if(go2.collisionHandler == null )
				if(iterator2.hasNext())
					go2 = iterator2.next();
				else
					break;
			else //if there is an intersection
			if(go1.v.getX() + go1.collisionHandler.getCollisionRadius() >= go2.v.getX() - go2.collisionHandler.getCollisionRadius() 
					&&
					go1.v.getX() - go1.collisionHandler.getCollisionRadius() <= go2.v.getX() + go2.collisionHandler.getCollisionRadius()
					&&
					go1.v.getY() + go1.collisionHandler.getCollisionRadius() >= go2.v.getY() - go2.collisionHandler.getCollisionRadius()
					&&
					go1.v.getY() - go1.collisionHandler.getCollisionRadius() <= go2.v.getY() + go2.collisionHandler.getCollisionRadius())							
					{
						if(go1.collisionHandler.handleCollision(go2, this))
						{
						
							objectsObstaclesLock.writeLock().lock();
							go1.dispose();
							iterator1.remove();
							objectsObstaclesLock.writeLock().unlock();
							
							
							if(iterator1.hasNext())
								go1 = iterator1.next();
							else
								break;
													
						}
						
						if(go2.collisionHandler.handleCollision(go1, this))
						{
					
							objectsObstaclesLock.writeLock().lock();
							
							go2.dispose();
							iterator2.remove();
							objectsObstaclesLock.writeLock().unlock();
							
							if(iterator2.hasNext())
								go2 = iterator2.next();
							else
								break;
						}
					}
					
				
			if(go1.compareTo(go2) < 0 && iterator1.hasNext())
				go1 = iterator1.next();
			else
			if(iterator2.hasNext())
				go2 = iterator2.next();
				else
					if(iterator1.hasNext())
					go1 = iterator1.next();
				
				
		}
	/*	
		for(int i=0; i<in_list1.size();i++)
				for(int j=0; j<in_list2.size();j++)
				{
					GameObject object1 =in_list1.get(i);
					GameObject object2 = in_list2.get(j);
					
					if(object1.collisionHandler != null && object2.collisionHandler != null )
						if(object1 != object2)
						{
							
							double distance = object1.v.getPos().sub(object2.v.getPos()).length();
							//if objects are within range
							if( distance < object1.collisionHandler.getCollisionRadius() || distance < object2.collisionHandler.getCollisionRadius() )
							{
								//collision occurs
								
								boolean b_delete1 = false;
								boolean b_delete2 = false;
								
								if(object1.collisionHandler !=null)
									//collide 1 with 2
									b_delete1 = object1.collisionHandler.handleCollision(object2,this);
								
								//collide 2 with 1
								if(object2.collisionHandler !=null)
									b_delete2 = object2.collisionHandler.handleCollision(object1,this);
		
								//if to destroy the j list thing
								if(b_delete2)
								{
									GameObject toDelete = object2;
									in_list2.remove(object2);
									toDelete.dispose();
									//set j back 1 to see if theres more things to collide with for i (incase its not destroyed)
									
								}
								
								if(b_delete1)
								{
									GameObject toDelete = object1;
									in_list1.remove(object1);
									toDelete.dispose();
									//i has been destroyed so break out of j loop and get a new i
									
									break;
								}
							}
						}
				}
		*/
	}
	
	private void runCollisions()
	{
		//collide player with enemies, obstacles and enemy bullets
		collideArrays(objectsPlayers,objectsEnemies);
		collideArrays(objectsPlayers,objectsEnemyBullets);
		
		
		collideArrays(objectsEnemies,objectsObstacles);
		collideArrays(objectsPlayerBullets,objectsObstacles);
		collideArrays(objectsPlayers,objectsObstacles);
	
		
		//collide powerups with players
		collideArrays(objectsPowerups,objectsPlayers);
		
		//collide bullets with enemies & obstacles
		collideArrays(objectsPlayerBullets,objectsEnemies);
		
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
		    
		}while(true);
	
	}

	private UI theUI = null;
	public void setUI(UI in_theUI) {
		// TODO Auto-generated method stub
		theUI = in_theUI;
	}

}
