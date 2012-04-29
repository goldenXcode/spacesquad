package com.DungeonCrawl;

import java.util.ArrayList;
import java.util.Collection;

import com.DungeonCrawl.Collisions.CollisionHandler;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Powerups.Powerup;
import com.DungeonCrawl.Shooting.ShotHandler;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.StepHandler;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.behaviors.*;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;
import de.steeringbehaviors.simulation.simulationobjects.Vehicle;

public class GameObject extends Drawable implements Cloneable, Comparable<GameObject>{

	
	//the vehicle (position etc for the movement libraries)
	public Vehicle v;
	public int shootEverySteps=0;
	public ShotHandler shotHandler=null;
	
	public String str_name = "";
	
	public ArrayList<StepHandler> stepHandlers = new ArrayList<StepHandler>(); 
	public CollisionHandler collisionHandler =null;


	public boolean b_ignorePowerupFrameChanges=false;
	
	public ArrayList<Powerup> activePowerups = new ArrayList<Powerup>();
	public ArrayList<Drawable> visibleBuffs = new ArrayList<Drawable>();
	
	
	public int i_offScreenCounter=0;
	

	public boolean rotateToV=false;
	public boolean isBoss=false;
	
	//for use in collision detection, so we dont have to keep subtracting collision radii
	int _xmax;
	int _ymax;
	int _xmin;
	int _ymin;
	
	
	public GameObject()
	{
		
	}
	
	 public GameObject clone()
     {
         GameObject ship = new GameObject(this.str_spritename, this.v.getX(), this.v.getY(), this.shootEverySteps);
         
         ship.allegiance = this.allegiance;
         ship.b_ignorePowerupFrameChanges = this.b_ignorePowerupFrameChanges;
         ship.b_isDisposed = this.b_isDisposed;
         
         
         
         
         try {
        	 if(this.collisionHandler != null)
        		 ship.collisionHandler = this.collisionHandler.cloneForShip(ship);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<this.stepHandlers.size();i++)
			try {
				ship.stepHandlers.add(this.stepHandlers.get(i).clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
         ship.f_forceRotation = this.f_forceRotation;
         ship.f_forceScaleX = this.f_forceScaleX;
         ship.f_forceScaleY = this.f_forceScaleY;
         ship.i_animationFrame = this.i_animationFrame;
         ship.i_animationFrameRow = this.i_animationFrameRow;
         ship.i_animationFrameSizeHeight = this.i_animationFrameSizeHeight;
         ship.i_animationFrameSizeWidth = this.i_animationFrameSizeWidth;
         ship.i_offScreenCounter = this.i_offScreenCounter;
         ship.i_stepCounter = this.i_stepCounter;
         ship.rotateToV = this.rotateToV;
         ship.shotHandler = this.shotHandler;
         ship.str_name =this.str_name;
         
         ship.v.setMaxForce(this.v.getMaxForce());
         ship.v.setMaxVel(this.v.getMaxVel());
         ship.v.setVel(new Vector2d(this.v.getVel().getX(),this.v.getVel().getY()));
         
         
         //copy waypoints
         ship.v.getPath().addAll((Collection)this.v.getPath().clone());
                 
         for(int i=0 ;i<this.visibleBuffs.size();i++)
        	 ship.visibleBuffs.add(ship.visibleBuffs.get(i));
         
         
         return ship;
     } 
	
	public void setRotateToVelocity(boolean in_rotateToV)
	{
		rotateToV = in_rotateToV;
	}
	public GameObject(String in_spritename, double in_x, double in_y,int in_shootEverySteps) {
		// TODO Auto-generated constructor stub
		//face in the direction that it is moving when 
		
		shootEverySteps = in_shootEverySteps;
		//Vehicle(Point2d pos, Vector2d mvel, double radius, Rect boundingbox,double maxVel, double maxForce, double mass)
		
		
		Point2d pos = new Point2d(in_x,in_y);
		
		//starting velocity
		Vector2d mvel = new Vector2d(0,0);


		Rect boundingbox = new Rect(in_x -25,in_y -25,in_x +25,in_y +25);
		
		v = new Vehicle(pos,mvel,2.5,boundingbox,50.0,100.0,5.0);
		
		str_spritename = in_spritename;
		
	}
	public void updateBoundingBox(CollisionHandler in_c)
	{
		int collisionRadius = (int) in_c.getCollisionRadius();

		v.getBoundingBox().setp1(new Point2d(v.getX() - collisionRadius,v.getY() -collisionRadius)); 
		v.getBoundingBox().setp2(new Point2d(v.getX() +collisionRadius,v.getY()+collisionRadius));
	}

	public void move(Vector2d in_v) {
		
		v.addForce(in_v);
		
		
	}

	public boolean b_isDisposed=false;
	public void dispose() {

		b_isDisposed=true;
	}

	private int i_stepCounter=0;

	public enum ALLEGIANCES
	{
		NONE,
		PLAYER,
		ENEMIES,
		LETHAL
		
	}
	
	public ALLEGIANCES allegiance = ALLEGIANCES.NONE;
	public Color c_Color = Color.WHITE;
	
	public boolean b_mirrorImageHorizontal = false;

	
	
	
	
	public boolean processStep(LogicEngine in_theLogicEngine) {
		i_stepCounter++;
		boolean b_destroy=false;
		
		for(int i=0 ; i< this.stepHandlers.size() ;i++)
			if( stepHandlers.get(i).handleStep(in_theLogicEngine, this))
			{
				b_destroy=true;
			}
		
		//do steps for collisions too incase they need to for example clear flashing
		if(collisionHandler != null)
			if( collisionHandler.handleStep(in_theLogicEngine, this))
			b_destroy=true;
		
		
		//spawn bullets every 10 steps
		if(shootEverySteps!=0)
			if(shotHandler !=null)
				if(i_stepCounter%shootEverySteps == 0)
				{
					shotHandler.shoot(in_theLogicEngine,this);

				}
		
		//see if we need to dispose of it (its offscreen and not a boss)
		if(i_offScreenCounter > 100 && !this.isBoss)
			return true;
		else 
			return b_destroy;

	}

	//Sorts by X location (for sweep in collision detection).
	@Override
	public int compareTo(GameObject arg0) {
		if(_ymin < arg0._ymin)
			return -1;
		if(_ymin > arg0._ymin)
			return 1;
		else
			return 0;
	}

}
