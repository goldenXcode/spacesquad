package com.DungeonCrawl;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;


public class Difficulty {
	public static enum DIFFICULTY{
		NONE ,
		EASY,
		MEDIUM,
		HARD 		
	}
	
	
	public static DIFFICULTY difficulty = DIFFICULTY.EASY;
	
	
	static ArrayList<DIFFICULTY> ia_completedLevels;

	public static void setLevelCompleted(int in_levelCompleted,DIFFICULTY in_difficulty) {

		//if already completed
		if(in_difficulty == DIFFICULTY.EASY && (ia_completedLevels.get(in_levelCompleted) == DIFFICULTY.MEDIUM || ia_completedLevels.get(in_levelCompleted) == DIFFICULTY.HARD ))
			return;
		if(in_difficulty == DIFFICULTY.MEDIUM && ia_completedLevels.get(in_levelCompleted) == DIFFICULTY.HARD)
			return;
		
		
		ia_completedLevels.set(in_levelCompleted, in_difficulty); 
		
		FileHandle f = Gdx.files.external("squadcommanderprogress.dat");
		 
		
		try {
			
			ObjectOutputStream objectOut = new ObjectOutputStream(f.write(false));
			objectOut.writeObject(ia_completedLevels);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	static void loadLevelsCompleted() {
		
		FileHandle f = Gdx.files.external("squadcommanderprogress.dat");
		
		
		//if levels data file exists
		if (f.exists()) {
			
			try {
				ObjectInputStream objectOut = new ObjectInputStream(f.read());
				ia_completedLevels = (ArrayList<DIFFICULTY>) objectOut.readObject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			//create a new one
			ia_completedLevels = new ArrayList<DIFFICULTY>();
			for(int i=0;i<100;i++)
				ia_completedLevels.add(DIFFICULTY.NONE);
		}

	}

	public static boolean isEasy() {
			return difficulty == DIFFICULTY.EASY;
	}
	public static boolean isMedium() {
		return difficulty == DIFFICULTY.MEDIUM;
}
	public static boolean isHard() {
		return difficulty == DIFFICULTY.HARD;
}
}
