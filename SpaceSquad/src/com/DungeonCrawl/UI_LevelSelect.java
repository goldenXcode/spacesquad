package com.DungeonCrawl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


import com.DungeonCrawl.Difficulty.DIFFICULTY;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;

public class UI_LevelSelect {

	Texture tex_interface;
	boolean b_loadedButtons = false;

	// buttons
	ArrayList<Rect> rects_levels = new ArrayList<Rect>();
	ArrayList<TextureRegion> textrs_levels = new ArrayList<TextureRegion>();

	Rect btn_bosses;
	TextureRegion texr_bosses;

	Rect btn_survival;
	TextureRegion texr_survival;

	
	TextDisplaying txt_connecting;
	
	TextDisplaying txt_difficulty;
	LogicEngine myLogicEngine = null;

	TextureRegion tr_selected = null;
	
	int i_selectedDifficulty = 0;

	ArrayList<Rect> rects_difficulties = new ArrayList<Rect>();
	
	ArrayList<TextureRegion> textrs_difficulties = new ArrayList<TextureRegion>();
	
	ArrayList<TextureRegion> textrs_completedDifficulties = new ArrayList<TextureRegion>();
	TextureRegion textr_padlock;
	
	boolean b_visible = false;
	private TextDisplaying txt_alternatively;

	public UI_LevelSelect(LogicEngine in_LogicEngine) {
		txt_connecting = new TextDisplaying("Select Level:", 0, LogicEngine.SCREEN_HEIGHT - 100, false);
		
		
		myLogicEngine = in_LogicEngine;
		btn_bosses = new Rect(LogicEngine.SCREEN_WIDTH / 4 - 50, 25,
				LogicEngine.SCREEN_WIDTH / 4 + 50, 75);
		btn_survival = new Rect((LogicEngine.SCREEN_WIDTH*3) / 4 - 50, 25,
				(LogicEngine.SCREEN_WIDTH*3) / 4 + 50, 75);
		
		txt_difficulty = new TextDisplaying("Difficulty:", 0,
				LogicEngine.SCREEN_HEIGHT ,false);
		txt_alternatively = new TextDisplaying("Alternative modes:", 25, 100,false);

		Difficulty.loadLevelsCompleted();
		
	}

	// go back to main screen
	public void backPressed() {
		if (b_visible == true)
			b_visible = false;
	}

	public void onClick(UI in_from, int in_x, int in_y) {
		Point2d p_clicked = new Point2d(in_x, in_y);

		// if boss run
		if (btn_bosses.inRect(p_clicked)) {
			myLogicEngine.MyLevelManager.newBossRunGame(myLogicEngine);
			myLogicEngine.b_paused = false;

			// this screen is not visible anymore because they just launched a
			// game
			b_visible = false;

			in_from.b_cameFromGame = true;
		}
		
		// if survival mission
		if (btn_survival.inRect(p_clicked)) {
			myLogicEngine.MyLevelManager.newSurvivalGame(myLogicEngine);
			myLogicEngine.b_paused = false;

			// this screen is not visible anymore because they just launched a
			// game
			b_visible = false;

			in_from.b_cameFromGame = true;
		}

		// clicking difficulties
		for (int i = 0; i < rects_difficulties.size(); i++)
			if (rects_difficulties.get(i).inRect(p_clicked)) {
				i_selectedDifficulty = i;

				switch (i_selectedDifficulty) {
				case 0:
					SoundEffects.getInstance().easy.play(SoundEffects.SPEECH_VOLUME);
					Difficulty.difficulty = DIFFICULTY.EASY;
					break;
				case 1:
					SoundEffects.getInstance().medium.play(SoundEffects.SPEECH_VOLUME);
					Difficulty.difficulty = DIFFICULTY.MEDIUM;
					break;
				case 2:
					SoundEffects.getInstance().hard.play(SoundEffects.SPEECH_VOLUME);
					Difficulty.difficulty = DIFFICULTY.HARD;
					break;
				}
			}

		for (int i = 0; i < rects_levels.size(); i++)
			if (rects_levels.get(i).inRect(p_clicked)) {
				
				//if last level not complete
				if(i>0 && (Difficulty.ia_completedLevels.get(i-1) == DIFFICULTY.NONE))
				{
					SoundEffects.getInstance().locked.play(SoundEffects.SPEECH_VOLUME);
					return;
				}
				
				myLogicEngine.MyLevelManager.newGame(myLogicEngine, i);
				myLogicEngine.b_paused = false;

				// this screen is not visible anymore because they just launched
				// a game
				b_visible = false;

				in_from.b_cameFromGame = true;
			}
	}
	
	public static int i_maxLevel = 9;

	long l_lastAnimatedText=0;

	public void drawUI(GameRenderer in_render, SpriteBatch batch) {
		// load interface texture
		tex_interface = in_render.loadTexture("data/"+GameRenderer.dpiFolder+"/interface.png");
		txt_connecting.displayAbsolute(batch);
		
		
		
		// load buttons
		if (b_loadedButtons == false) {
			b_loadedButtons = true;

			int i_buttonsPerRow = 4;
			int i_numberOfRows = 3;
			

			// load level buttons
			for (int rows = 0; rows < i_numberOfRows; rows++)
				for (int i = 0; i < i_buttonsPerRow; i++) {
					if ((rows * i_buttonsPerRow) + i >= i_maxLevel)
						break;

					// centre x
					int i_StartingX = 25;
					int i_StartingY = (int) LogicEngine.SCREEN_HEIGHT - 125
							- (rows * 50);

					Rect rect_Button = new Rect(i_StartingX + i * 50,
							i_StartingY - 50, i_StartingX + ((i + 1) * 50),
							i_StartingY);
					TextureRegion textr_Button = in_render.loadTextureRegion(
							"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, rows, 6 + i,
							50, 50);
					rects_levels.add(rect_Button);
					textrs_levels.add(textr_Button);

					textr_Button = in_render.loadTextureRegion(
							"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, i, 3, 50, 50);
				}
			
			//load the alpha transparentish logos for level difficulty completed
			textrs_completedDifficulties.add( in_render.loadTextureRegion(	"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 6, 0, 50, 50));
			textrs_completedDifficulties.add( in_render.loadTextureRegion(	"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 7, 0, 50, 50));
			textrs_completedDifficulties.add( in_render.loadTextureRegion(	"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 8, 0, 50, 50));

			textr_padlock = in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 9, 0, 50, 50);
			
			//load selected texture
			tr_selected = in_render.loadTextureRegion(
					"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 0, 3,
					50, 50);
			// load rectangles
			for (int i = 0; i < 3; i++) {
				int i_StartingX = 25;
				int i_StartingY = (int) (LogicEngine.SCREEN_HEIGHT - 25);

				Rect rect_Button = new Rect(i_StartingX + i * 50,
						i_StartingY - 50, i_StartingX + ((i + 1) * 50),
						i_StartingY);
				TextureRegion textr_Button = in_render.loadTextureRegion(
						"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, i + 5, 4, 50, 50);
				rects_difficulties.add(rect_Button);
				textrs_difficulties.add(textr_Button);

			
			}
		}

		texr_bosses = in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png",
				tex_interface, 4, 0, 100, 50);
		
		texr_survival = in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png",
				tex_interface, 1, 0, 100, 50);

		// draw title screen
		Utils.drawRect(texr_bosses, btn_bosses, batch);
		Utils.drawRect(texr_survival, btn_survival, batch);

		// draw difficulties
		for (int i = 0; i < rects_difficulties.size(); i++)
			if (i != i_selectedDifficulty)
				Utils.drawRect(textrs_difficulties.get(i), rects_difficulties.get(i),
						batch);
			else
			{
				Utils.drawRect(textrs_difficulties.get(i), rects_difficulties.get(i),
						batch);
				Utils.drawRect(tr_selected,
						rects_difficulties.get(i), batch);
			}

		txt_difficulty.displayAbsolute(batch);
		txt_alternatively.displayAbsolute(batch);

		//draw levels
		for (int i = 0; i < textrs_levels.size(); i++)
		{
			Utils.drawRect(textrs_levels.get(i), rects_levels.get(i), batch);
			
			//if lvl completed
			if(Difficulty.ia_completedLevels.get(i) == DIFFICULTY.EASY)
				Utils.drawRect(textrs_completedDifficulties.get(0), rects_levels.get(i), batch);
			if(Difficulty.ia_completedLevels.get(i) == DIFFICULTY.MEDIUM)
				Utils.drawRect(textrs_completedDifficulties.get(1), rects_levels.get(i), batch);
			if(Difficulty.ia_completedLevels.get(i) == DIFFICULTY.HARD)
				Utils.drawRect(textrs_completedDifficulties.get(2), rects_levels.get(i), batch);
			
			//if last level not complete
			if(i>0 && (Difficulty.ia_completedLevels.get(i-1) == DIFFICULTY.NONE))
				Utils.drawRect(textr_padlock, rects_levels.get(i), batch);
		}
	}

	
}
