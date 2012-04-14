package com.DungeonCrawl;

import java.util.ArrayList;
import java.util.Iterator;

import com.DungeonCrawl.AreaEffects.SimpleAreaEffect.Effect;
import com.DungeonCrawl.Collisions.DestroyIfEnemyCollision;
import com.DungeonCrawl.Collisions.PowerupCollision;
import com.DungeonCrawl.Powerups.DualFirePowerup;
import com.DungeonCrawl.Powerups.MissilePowerup;
import com.DungeonCrawl.Powerups.MovementPowerup;
import com.DungeonCrawl.Powerups.RapidFirePowerup;
import com.DungeonCrawl.Powerups.ShieldPowerup;
import com.DungeonCrawl.Powerups.SideCannonsPowerup;
import com.DungeonCrawl.Powerups.SlowFieldPowerup;
import com.DungeonCrawl.Shooting.BeamShot;
import com.DungeonCrawl.Shooting.ExplodeIfInRange;
import com.DungeonCrawl.Shooting.FragmentationProjectile;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.FlyStraightStep;
import com.DungeonCrawl.Steps.GravitonDevice;
import com.DungeonCrawl.Steps.LaunchShipsStep;
import com.DungeonCrawl.Steps.LoopingAnimationStep;
import com.DungeonCrawl.Steps.SeekInputTarget;
import com.DungeonCrawl.Steps.SeekNearestPlayerStep;
import com.DungeonCrawl.Steps.StealthStep;
import com.DungeonCrawl.Steps.StepHandler;
import com.badlogic.gdx.graphics.Color;


import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class Advantage {

	private static int i_numberOfAdvantages=10; 
	
	//array showing which advantages are currently active
	public static boolean[] b_advantages = new boolean[8];
	
	public static FragmentationProjectile bigShot = new FragmentationProjectile("data/"+GameRenderer.dpiFolder+"/bulletsheet.png",12.0f,new Vector2d(0,6));
	public static BeamShot beamShot = new BeamShot(50);
	
	private static SideCannonsPowerup sideCannonsPowerup = new SideCannonsPowerup();
	
	
	
	public static void applyAdvantageToShip(int in_advantage,GameObject in_ship, int in_position, LogicEngine in_logicEngine)
	{
		switch(in_advantage)
		{
		case 0:
			if(in_position == 0 || in_position == 2)
			{
				RapidFirePowerup rapid = new RapidFirePowerup();
				rapid.applyPowerup(in_ship,in_logicEngine);
			}
			else
			if(in_position == 1 || in_position == 3)
			{
				DualFirePowerup dual = new DualFirePowerup();
				dual.applyPowerup(in_ship,in_logicEngine);
			}
			break;
		case 1: //spawn with shields
				ShieldPowerup shield = new ShieldPowerup();
				shield.applyPowerup(in_ship,in_logicEngine);
			
		
			break;
		case 2:
			if(in_position == 0  || in_position == 2)
			{
				//only apply once
				if(in_ship.shotHandler != bigShot)
				{
					//big shot gun
					in_ship.i_animationFrameRow = 4;
					in_ship.shootEverySteps *= 1.25; 
					in_ship.shotHandler = bigShot;
					//in_ship.shootEverySteps*=2f; //does not work!
					in_ship.b_ignorePowerupFrameChanges=true;
				}
			}
			break;

		case 3:			//graviton gun
			if(in_position == 0 || in_position == 2)
			{
				
				in_ship.i_animationFrame=0;
				
				//if it already has it then return
				for(int i=0;i<in_ship.stepHandlers.size();i++)
					if(in_ship.stepHandlers.get(i) instanceof GravitonDevice)
						return;
				
				in_ship.stepHandlers.add(new GravitonDevice());
			}
		
			break;
		case 4:
			//stealth
			//if it already has it then return
			for(int i=0;i<in_ship.stepHandlers.size();i++)
				if(in_ship.stepHandlers.get(i) instanceof StealthStep)
					return;
			
			in_ship.stepHandlers.add(new StealthStep(20,80));
			break;
			
		case 5:
			sideCannonsPowerup.applyPowerup(in_ship, in_logicEngine);
			break;
		case 6:
			//if it is front ship and doesnt already have beam shot handler
			if(in_position == 0 )
			{
				//important to watch this if we put any shot handlers after this!
				in_ship.shootEverySteps = 1;
				
				beamShot.ae_beam.c_Color = Color.BLUE;
				beamShot.b_direction=Utils.Direction.NORTH;
				beamShot.b_flare = false;
				beamShot.i_beamDuration = 20;
				beamShot.ae_beam.effect = Effect.DAMAGE_EVERYTHING;
				
				in_ship.shotHandler = beamShot;
			}
			break;
		case 7:
			//homing
			if(in_ship.shotHandler instanceof StraightLineShot)
				((StraightLineShot)in_ship.shotHandler).b_homing=true;
			
			break;
		case 8:
			if(in_position == 3)
			{
				
			}
		break;
		case 9:
			if(in_position == 1)
			{	
				//PENDING this is a hack
				SlowFieldPowerup p = new SlowFieldPowerup();
				p.applyPowerup(in_ship, in_logicEngine);
			}
			break;
		}
	}

	public static void playAudioForAdvantage(int in_advantage) {
		
		switch(in_advantage)
		{
			case 0 : SoundEffects.getInstance().enhanced_weapons.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 1 : SoundEffects.getInstance().squad_shields.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 2 : SoundEffects.getInstance().cluster_missiles.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 3 : SoundEffects.getInstance().graviton.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 4 : SoundEffects.getInstance().stealth.play(SoundEffects.SPEECH_VOLUME); 
			break;
			case 5 : SoundEffects.getInstance().sidecannons.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 6 : SoundEffects.getInstance().laser.play(SoundEffects.SPEECH_VOLUME);
			break;
			case 7 : SoundEffects.getInstance().homing.play(SoundEffects.SPEECH_VOLUME);
			break;
		}
	}
	
	

	public static void clearSimulatedAdvantages( GameObject[] go_ships,
			ArrayList<GameObject> go_otherStuff) {
		
		//for each ship
		for(int i_position=0;i_position<4;i_position++)
		{
			go_ships[i_position].visibleBuffs.clear();
			go_ships[i_position].i_animationFrameRow=0;
			go_ships[i_position].b_ignorePowerupFrameChanges=false;
			go_ships[i_position].c_Color = new Color(1.0f,1.0f,1.0f,1.0f);
			
			Iterator<StepHandler> i = go_ships[i_position].stepHandlers.iterator();
			go_otherStuff.clear();
			
			//remove all stealth steps
			while(i.hasNext())
			{
				if(i.next() instanceof StealthStep)
					i.remove();
			}
		}
	}
	
	
	
	//this is for ships on the holoscreen so it doesnt involve the Logic Engine and changes shouldnt affect the real world but be undoable (quite a tall order!)
	public static void simulateAdvantages( GameObject[] go_ships,
			ArrayList<GameObject> go_otherStuff, Point2d in_centreOfHolo) {

		//for each advantage
		for(int i_advantage=0;i_advantage < Advantage.b_advantages.length;i_advantage++)
			if(Advantage.b_advantages[i_advantage])  //if advantage is chosen 
				for(int i_position=0;i_position<4;i_position++) //apply it to every ship
					switch(i_advantage)
					{
						case 0 : 
							
							if(i_position == 0 || i_position == 2)
							{
								if(!go_ships[i_position].b_ignorePowerupFrameChanges)
									go_ships[i_position].i_animationFrameRow=1;
								
							}
							else
							if(i_position == 1 || i_position == 3)
							{
								if(!go_ships[i_position].b_ignorePowerupFrameChanges)
									go_ships[i_position].i_animationFrameRow=2;
							}
							break;
						
						case 1 : 
						
							Drawable d_shield = new Drawable();
							d_shield.i_animationFrameRow = 3;
							d_shield.i_animationFrameSizeHeight = 16;
							d_shield.i_animationFrameSizeWidth = 16;
							d_shield.str_spritename="data/"+GameRenderer.dpiFolder+"/tinyship.png";
							go_ships[i_position].visibleBuffs.add(d_shield);
						
						
						break;
						case 2 :
							if(i_position == 0  || i_position == 2)
							{
								//big shot gun
								go_ships[i_position].i_animationFrameRow = 4;
								
								go_ships[i_position].b_ignorePowerupFrameChanges=true;
							}
							break;
						
						case 3 :
							
						break;
						case 4 :
							go_ships[i_position].stepHandlers.add(new StealthStep(20,80));
						break;
						case 5 :
							for(int i=0;i<2;i++)
							{
								//spawn 2 cannons
							
								GameObject ship = new GameObject("data/"+GameRenderer.dpiFolder+"/tinyship.png",(LogicEngine.rect_Screen.getWidth()/2) - (32*i) + 64,50,20);
								ship.i_animationFrame=2;
								ship.i_animationFrameRow=3;
								
								ship.i_animationFrameSizeWidth=16;
								ship.i_animationFrameSizeHeight=16;
								
								ship.str_name = "sidecannon";
								
								if(i==0)
								{
									ship.v.setX(in_centreOfHolo.getX()-30);
									ship.v.setY(in_centreOfHolo.getY()-10);
								}
									
								if(i==1)
								{
									ship.v.setX(in_centreOfHolo.getX() + 30);
									ship.v.setY(in_centreOfHolo.getY()-10);
								}
								
								go_otherStuff.add(ship);
								
							}
						break;
						case 6 :
						break;
						case 7 :
						break;
					}
			
	}

}
