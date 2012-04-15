package com.DungeonCrawl;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;



public class DungeonCrawlMain implements ApplicationListener{

	private GameRenderer gr;
	private LogicEngine theLogicEngine;
	Thread gameThread;
	
	public static boolean b_isAndroid=false;
	
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
		gameThread = new Thread( theLogicEngine);
		gameThread.start();
		 
	}

	@Override
	public void dispose() {
		//close all threads
		if(DungeonCrawlMain.b_isAndroid)
			System.exit(0);
		else
			Gdx.app.exit();
		
		theLogicEngine.b_exit=true;
		
		
	}

	@Override
	public void pause() {

    	System.out.println("closing");
		
    	//close all threads
		if(DungeonCrawlMain.b_isAndroid)
			System.exit(0);
		else
			Gdx.app.exit();
		
    	theLogicEngine.b_exit=true;
		
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
