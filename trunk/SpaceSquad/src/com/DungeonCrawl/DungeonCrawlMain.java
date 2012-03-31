package com.DungeonCrawl;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;



public class DungeonCrawlMain implements ApplicationListener{

	private GameRenderer gr;
	private LogicEngine theLogicEngine;
	
	@Override
	public void create() {
		if(theLogicEngine != null)
			try {
				throw new Exception("create tried to create a logic engine when one already existed");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		theLogicEngine = new LogicEngine();
		gr = new GameRenderer(theLogicEngine);
		
		//start game loop
		Thread gameThread = new Thread( theLogicEngine);
		gameThread.start();
		 
	}

	@Override
	public void dispose() {
		//close all threads
		System.exit(0);
	}

	@Override
	public void pause() {

    	System.out.println("closing");
    	System.exit(0);
	}

	@Override
	public void render() {
	gr.render();
	
	}

	@Override
	public void resize(int arg0, int arg1) {
		
	}

	@Override
	public void resume() {
		
	}

}
