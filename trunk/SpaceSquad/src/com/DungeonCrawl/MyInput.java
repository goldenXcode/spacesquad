package com.DungeonCrawl;
import javax.swing.JOptionPane;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.*;


public class MyInput implements InputProcessor{

	public Vector2[] Targets;
	public boolean[] targetsActive;
	public float SCREEN_HEIGHT=320;
	public float NOTARGET=Float.MIN_VALUE;
	private UI myUI;
	
	private LogicEngine myLogicEngine;
	
	/*
	 * 	Game coordinates are not the same as touch coordinates
	 * 
	 * 
	 */
	public void setLogicEngine(LogicEngine in_logicEngine)
	{
		myLogicEngine = in_logicEngine;
		Gdx.input.setCatchBackKey(true);
	}
	
	public MyInput(UI in_ui)
	{
		myUI = in_ui;
		 
		Gdx.input.setInputProcessor(this);
		Targets = new Vector2[5];
		targetsActive = new boolean[5];
		
		for(int i=0;i<5;i++)
		{
			Targets[i] = new Vector2();
			Targets[i].x = NOTARGET;
			Targets[i].y = NOTARGET;
		}
	
	}

	@Override
	public boolean keyDown(int keycode) {
		
		        if(keycode == Keys.KEYCODE_BACK || keycode == Keys.KEYCODE_ESCAPE){
		        	myUI.backPressed();
		        }
		        
		        
		        if(keycode == Keys.KEYCODE_DPAD_LEFT ){

		        	System.out.println("left");
		        	
		        	if(Targets[0].x == 0)
		        		Targets[1].x = Targets[0].x -150;
		        	
		        	
		        	Targets[1].y = Targets[0].y;  
		        	targetsActive[1] = true;
		        }
		      
		 
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		 if(keycode == Keys.KEYCODE_DPAD_LEFT ){
	        	
	        //	targetsActive[1] = false;
	        }
		 
		 if(keycode == Keys.KEYCODE_DPAD_LEFT ){
	        
	      //  	targetsActive[1] = false;
	        }
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		
		//if paused ignore
		if(myLogicEngine.b_paused)
 			return false;
		
		if(pointer == 0 && b_sticky2)
			targetsActive[1] = false;
		
		//TODO : Check
		Targets[pointer].x = x / GameRenderer.f_pixelAdjustX;
		
		Targets[pointer].y = Math.abs(Gdx.graphics.getHeight() + 60  - y)/GameRenderer.f_pixelAdjustY;
		
		targetsActive[pointer] = true;
		
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {

		
		//if paused ignore
		if(myLogicEngine.b_paused)
 			return false;
		
		
		if(pointer == 0 && b_sticky2)
			targetsActive[1] = false;
		
		
		
		//set where they clicked
		Targets[pointer].x = x / GameRenderer.f_pixelAdjustX;
		
		Targets[pointer].y = Math.abs(Gdx.graphics.getHeight()+ 60 - y)/GameRenderer.f_pixelAdjustY;
		
		Targets[pointer].x = Math.min(Targets[pointer].x, Gdx.graphics.getWidth()-1);
		
		targetsActive[pointer] = true;
		
		return false;
	}

	@Override
	public boolean touchMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean b_sticky2=false;
	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		
		//if paused give it to the UI instead
		if(myLogicEngine.b_paused)
		{
			myUI.onClick((int)x, (int) (Gdx.graphics.getHeight() - y));
 			return false;
		}
		
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		//set where they clicked
		Targets[pointer].x = x/GameRenderer.f_pixelAdjustX;
		
		Targets[pointer].y = Math.abs(Gdx.graphics.getHeight()+ 60 - y)/GameRenderer.f_pixelAdjustY;
		
		//if taking finger 2 off when 1 is still on
		if(pointer == 1 && targetsActive[0] == true)
		{	
			targetsActive[pointer] = false;
		}
		else
			b_sticky2=true;
		
		//if taking finger 1 off when 2 is still on
		if(pointer == 0 && targetsActive[1] == true)
			targetsActive[pointer] = false;
		
		return false;
	}
	
	
}
