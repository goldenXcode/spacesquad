package com.DungeonCrawl;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.DungeonCrawl.DungeonCrawlMain;

public class DesktopStarter {
	public static void main(String[] args)
	{
		new LwjglApplication(new DungeonCrawlMain(),"Dungeon Crawl",2000,2000,false);
	}
}
