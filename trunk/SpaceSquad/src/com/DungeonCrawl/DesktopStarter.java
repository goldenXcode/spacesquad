package com.DungeonCrawl;

import com.badlogic.gdx.backends.jogl.JoglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopStarter {
	public static void main(String[] args)
	{
		new JoglApplication(new DungeonCrawlMain(),"Dungeon Crawl",640,1024,false);
	}
}
