package com.DungeonCrawl;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.DungeonCrawl.DungeonCrawlMain;

public class DesktopStarter {
	public static void main(String[] args)
	{
		new LwjglApplication(new DungeonCrawlMain(),"Dungeon Crawl",480,720,false);
	}
}
