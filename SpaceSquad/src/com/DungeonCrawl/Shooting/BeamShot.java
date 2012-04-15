package com.DungeonCrawl.Shooting;

import com.DungeonCrawl.Drawable;
import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.GameRenderer;
import com.DungeonCrawl.LogicEngine;
import com.DungeonCrawl.Utils;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.openal.Ogg.Music;
import com.badlogic.gdx.graphics.Color;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;

public class BeamShot  implements ShotHandler{

	public SimpleAreaEffect ae_beam;
	public SimpleAreaEffect ae_beamTracer;
	
	public boolean b_flare = true;
	public Utils.Direction b_direction=Utils.Direction.SOUTH;
	
	
	Drawable d_flare; 
	int i_fireEvery;
	public int i_flareDuration=15;
	public int i_beamDuration=10;
	int i_beamWidth=5;
	static Sound s_beam=null;
	
	
	
	
	
	public BeamShot(int in_fireEvery)
	{
		if(s_beam == null)
		{
			s_beam = Gdx.audio.newSound(Gdx.files.internal("data/sounds/shortbeam.ogg"));
		    
		}
		i_fireEvery = in_fireEvery;
		d_flare = new Drawable();
		d_flare.i_animationFrame = 7;
		d_flare.i_animationFrameSizeHeight=48;
		d_flare.i_animationFrameSizeWidth=16;
		d_flare.str_spritename = "data/"+GameRenderer.dpiFolder+"/gravitonlevel.png";
		
		
		Rect r = new Rect(0,0,LogicEngine.SCREEN_WIDTH/2 , LogicEngine.SCREEN_HEIGHT);
		Drawable d = new Drawable();
		d.str_spritename = "data/"+GameRenderer.dpiFolder+"/minelayer.png";
		d.i_animationFrame = 1;
		d.i_animationFrameRow=7;
		d.i_animationFrameSizeHeight=32;
		d.i_animationFrameSizeWidth=32;
		
		ae_beam = new SimpleAreaEffect(r,SimpleAreaEffect.Effect.KILL_EVERYTHING,d);
		 
		ae_beamTracer = new SimpleAreaEffect(r,SimpleAreaEffect.Effect.NONE,d);
		ae_beamTracer.c_Color = new Color(ae_beam.c_Color);
		ae_beamTracer.c_Color.a = 0.20f;
		
	}
	
	
	
	
	
	int i_stepCounter;
	@Override
	public void shoot(LogicEngine in_toShootIn, GameObject in_objectFiring) {
	
		if(in_toShootIn.currentAreaEffects.indexOf(ae_beamTracer) == -1)
			in_toShootIn.currentAreaEffects.add(ae_beamTracer);
		
		
		ae_beamTracer.setDuration(5);
		//move tracer beam
		if(b_direction == Utils.Direction.SOUTH)
		{
			ae_beamTracer.area.setp1(new Point2d(in_objectFiring.v.getX() - i_beamWidth,-100));
			ae_beamTracer.area.setp2(new Point2d(in_objectFiring.v.getX() + i_beamWidth,in_objectFiring.v.getY()-10));
		}
		else
		if(b_direction == Utils.Direction.EAST)
		{
			ae_beamTracer.area.setp1(new Point2d(in_objectFiring.v.getX() -15 + (in_objectFiring.i_animationFrameSizeWidth/2) ,in_objectFiring.v.getY() - i_beamWidth));
			ae_beamTracer.area.setp2(new Point2d(LogicEngine.SCREEN_WIDTH+50 ,in_objectFiring.v.getY()+i_beamWidth));
		}
		if(b_direction == Utils.Direction.NORTH)
		{
			ae_beamTracer.area.setp1(new Point2d(in_objectFiring.v.getX()  - (i_beamWidth/2) ,in_objectFiring.v.getY() + i_beamWidth));
			ae_beamTracer.area.setp2(new Point2d(in_objectFiring.v.getX()  + (i_beamWidth/2) ,LogicEngine.f_worldCoordTop+100));
		}
		
		
		//move beam with ship
		if(i_stepCounter >= i_fireEvery -(i_beamDuration ))
		{
			if(b_direction == Utils.Direction.SOUTH)
			{
				ae_beam.area.setp1(new Point2d(in_objectFiring.v.getX() - i_beamWidth,-100));
				ae_beam.area.setp2(new Point2d(in_objectFiring.v.getX() + i_beamWidth,in_objectFiring.v.getY()-10));
			}
			else
			if(b_direction == Utils.Direction.EAST)
			{
				ae_beam.area.setp1(new Point2d(in_objectFiring.v.getX() -15 + (in_objectFiring.i_animationFrameSizeWidth/2) ,in_objectFiring.v.getY() - i_beamWidth));
				ae_beam.area.setp2(new Point2d(LogicEngine.SCREEN_WIDTH+50 ,in_objectFiring.v.getY()+i_beamWidth));
			}
			if(b_direction == Utils.Direction.NORTH)
			{
				ae_beam.area.setp1(new Point2d(in_objectFiring.v.getX()  - (i_beamWidth/2) ,in_objectFiring.v.getY() + i_beamWidth));
				ae_beam.area.setp2(new Point2d(in_objectFiring.v.getX()  + (i_beamWidth/2) ,LogicEngine.f_worldCoordTop+100));
			}
		}
		
		//spawn flare
		if(b_flare)
			if(i_stepCounter == i_fireEvery -(i_beamDuration + i_flareDuration) )
				in_objectFiring.visibleBuffs.add(d_flare);
		
		//spawn beam
		if(i_stepCounter == i_fireEvery -(i_beamDuration ) )
		{
			ae_beam.setDuration(i_beamDuration);
			in_toShootIn.currentAreaEffects.add(ae_beam);
			s_beam.play();
		}
		
		//remove everything
		if(i_stepCounter == i_fireEvery)
		{
			if(b_flare)
				in_objectFiring.visibleBuffs.remove(d_flare);
			i_stepCounter = 0;
		}
		else
			i_stepCounter++;
	}

}
