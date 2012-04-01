package com.DungeonCrawl;

import java.util.ArrayList;

import com.DungeonCrawl.Collisions.PlayerCollision;
import com.DungeonCrawl.Shooting.StraightLineShot;
import com.DungeonCrawl.Steps.AnimateRollStep;
import com.DungeonCrawl.Steps.CustomBehaviourStep;
import com.DungeonCrawl.Steps.PlayerStep;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.steeringbehaviors.simulation.behaviors.SimplePathfollowing;
import de.steeringbehaviors.simulation.renderer.Point2d;
import de.steeringbehaviors.simulation.renderer.Rect;
import de.steeringbehaviors.simulation.renderer.Vector2d;

public class UI 
{
	boolean DEBUG=false;
	int DEBUGLevelLoad=8;
	Difficulty.DIFFICULTY DEBUGDifficulty = Difficulty.DIFFICULTY.EASY;
	
	Texture tex_interface;
	
	//buttons and stuff
	TextureRegion texr_title;
	TextureRegion texr_launch;
	TextureRegion texr_exit;
	
	Rect btn_title;
	Rect btn_launch;
	Rect btn_exit;
	Rect btn_help;
	
	boolean b_loadedUI=false;
	
	//buttons
	ArrayList<Rect> rects_formations = new ArrayList<Rect>();	
	ArrayList<Rect> rects_advantages = new ArrayList<Rect>();
	ArrayList<Rect> rects_advantages2 = new ArrayList<Rect>();
	
	
	//buttons once selected
	TextureRegion tr_selected = null;
		
	Drawable d_newUI;
	
	TextDisplaying txt_chooseFormation;
	TextDisplaying txt_chooseAdvantage;
	TextDisplaying txt_copyright;
	
	
	TextDisplaying txt_Launch;
	TextDisplaying txt_Exit;
	
	
	String str_messageBox = "";
	TextureRegion textr_padlock;
	
	LogicEngine myLogicEngine=null;
	
	UI_Dialog myDialog;
	String str_help = "It is time to choose your squad advantages and formation.  Beware, the more advantages you choose the less ships (extra lives) you can afford.";

	int i_selectedFormation=0;

	Drawable gameOverScreen;
	
	UI_LevelSelect myUI_LevelSelect;

	GameObject[] go_ships;
	ArrayList<GameObject> go_otherStuff = new ArrayList<GameObject>();
	
	int i_budget =1000;
	int i_shipCost=0;
	
	TextDisplaying txt_cost;
	TextDisplaying txt_ships;
	
	private int calculateAndUpdateShipCost()
	{
		int i_numberAdvantages =0;
		
		for(int i=0;i<Advantage.b_advantages.length;i++)
			if(Advantage.b_advantages[i])
				i_numberAdvantages++;
		
		int i_shipCost=1;
		
		switch(i_numberAdvantages)
		{
			case 0 : i_shipCost = 35;
				break;
			case 1 : i_shipCost = 40;
			break;
			case 2 : i_shipCost = 45;
			break;
			case 3 : i_shipCost = 65;
			break;
			case 4 : i_shipCost = 90;
			break;
			case 5 : i_shipCost = 110;
			break;
			case 6 : i_shipCost = 150;
			break;
			case 7 : i_shipCost = 175;
			break;
			case 8 : i_shipCost = 225;
			break;
		
		}
		
		txt_cost = new TextDisplaying("" + i_shipCost, f_holoCentreX+123, f_holoCentreY+30, true);
		
		return i_shipCost;
	}
	//calculates how many ships the player can afford with a budget of 1000 credits.  Also updates text and updates the static number of lives variable.
	private int calculateAndUpdateNumberOfShips()
	{
		int i_shipCost = calculateAndUpdateShipCost();
		
		
		txt_ships = new TextDisplaying("" + (i_budget / i_shipCost), f_screenCentreX + 105, f_screenCentreY + 70, false);
		
		LifeCounter.i_numberOfShipsAvailable = (i_budget / i_shipCost);
		
		return i_budget / i_shipCost;
		
	}
	
	private String advantageLockedText(int in_advantage)
	{
		switch(in_advantage)
		{
			case 5 : return "Side Cannons\nThis advantage is currently locked, to unlock it:\n\n Complete Level 1 on Hard (you can set difficulties on the launch page)";
			case 6 : return "Beam Laser\nThis advantage is currently locked, to unlock it:\n\n Complete Level 5";
			case 7 : return "Homing\nThis advantage is currently locked, to unlock it:\n\n Complete Level 3 on Hard";
			case 8 : return "Got an idea for an advantage? email me it at: \n\ntnind@computing.dundee.ac.uk";
			case 9 : return "Got an idea for an advantage? email me it at tnind@computing.dundee.ac.uk";
		}
		
		return "";	
	}
	
	private boolean isAdvantageUnlocked(int in_advantage)
	{
		switch(in_advantage)
		{
			case 5 : return Difficulty.ia_completedLevels.get(1)== Difficulty.DIFFICULTY.HARD;
			case 6 : return Difficulty.ia_completedLevels.get(5)!= Difficulty.DIFFICULTY.NONE;
			case 7 : return Difficulty.ia_completedLevels.get(3)== Difficulty.DIFFICULTY.MEDIUM || Difficulty.ia_completedLevels.get(3)== Difficulty.DIFFICULTY.HARD;
			case 8 : return Difficulty.ia_completedLevels.get(4)!= Difficulty.DIFFICULTY.NONE;
			case 9 : return Difficulty.ia_completedLevels.get(7)!= Difficulty.DIFFICULTY.NONE;
		}
		return false;
	}
	
	SimplePathfollowing[] followPathBehaviours;
	
	public UI(LogicEngine in_LogicEngine)
	{
		//initialise sounds
		SoundEffects.getInstance();
		
		myLogicEngine = in_LogicEngine;
		myUI_LevelSelect = new UI_LevelSelect(in_LogicEngine);
		
		//for some reason Rect constructor description is lying upper left lower right is bullshit
		//it really wants lower left upper right.  Probably it measures y as distance from top instead of bottom or something
		
		btn_title = new Rect(LogicEngine.SCREEN_WIDTH/2 - 100,  LogicEngine.SCREEN_HEIGHT-115,LogicEngine.SCREEN_WIDTH/2 + 100,  LogicEngine.SCREEN_HEIGHT-15);
		btn_launch = new Rect( LogicEngine.SCREEN_WIDTH/2 - 115,25, (LogicEngine.SCREEN_WIDTH/2) - 65 + 50,75);
		btn_exit = new Rect((LogicEngine.SCREEN_WIDTH/2) +15,25, LogicEngine.SCREEN_WIDTH/2  +65 +50,75);
		btn_help = new Rect(216,257,245,284);
		///////////NEW UI Control Panel//////////////
		d_newUI = new Drawable();
		d_newUI.str_spritename = "data/"+GameRenderer.dpiFolder+"/shipcustomisation.png";
		d_newUI.i_animationFrameSizeWidth = 290;
		d_newUI.i_animationFrameSizeHeight = 290;
		

		f_holoCentreX = 150;
		f_holoCentreY = 300;
		
		//Create holo ships that go to their waypoints and animate like real ships
		go_ships = new GameObject[4];
		followPathBehaviours = new SimplePathfollowing[4];
		
		for(int i=0;i<go_ships.length ;i++ )
		{
			go_ships[i] = new GameObject("data/"+GameRenderer.dpiFolder+"/tinyship.png",(LogicEngine.rect_Screen.getWidth()/2) - (32*i) + 64,50,20);
			go_ships[i].i_animationFrame=0;
			go_ships[i].i_animationFrameSizeWidth=16;
			go_ships[i].i_animationFrameSizeHeight=16;
			
			followPathBehaviours[i] = new SimplePathfollowing();
			followPathBehaviours[i].setInfluence(1);
			followPathBehaviours[i].setAttribute("arrivedistance", "2", null);
			followPathBehaviours[i].waitAtWaypoint(1, Integer.MAX_VALUE);
			//PENDING probably broken
			
			go_ships[i].stepHandlers.add(new CustomBehaviourStep(followPathBehaviours[i]));
			
			go_ships[i].shotHandler = new StraightLineShot("data/"+GameRenderer.dpiFolder+"/bullet.png",7.0f,new Vector2d(0,9));
			go_ships[i].stepHandlers.add(new AnimateRollStep());
			go_ships[i].str_name = "player";
			go_ships[i].c_Color = new Color(1.0f,1.0f,1.0f,.9f);
			
			
			go_ships[i].v.setMaxForce(2);
			go_ships[i].v.setMaxVel(2);
			go_ships[i].v.setX(f_holoCentreX);
			go_ships[i].v.setY(f_holoCentreY);
			
			go_ships[i].v.addWaypoint(new Point2d(f_holoCentreX-10 + Formation.getXOffsetForShip(i),f_holoCentreY + Formation.getYOffsetForShip(i)));
		}
		///////////END UI Control Panel//////////////
		
		
		txt_chooseFormation = new TextDisplaying("Formation:", 25, 395,false);
		txt_chooseAdvantage = new TextDisplaying("Advantages:", 25, 265,false);

		txt_copyright = new TextDisplaying("\u00a9 Citadel Systems", 170,16,false);
		
		calculateAndUpdateNumberOfShips();
		
		//start game if in debug mode
		if(DEBUG)
		{
			Difficulty.difficulty = DEBUGDifficulty;
			myLogicEngine.MyLevelManager.newGame(myLogicEngine, DEBUGLevelLoad);
			myLogicEngine.b_paused=false;
			b_cameFromGame = true;
		}
		
		gameOverScreen = new Drawable();
		gameOverScreen.str_spritename = "data/"+GameRenderer.dpiFolder+"/gameover.png";
		gameOverScreen.i_animationFrame=0;
		gameOverScreen.i_animationFrameRow=0;
		gameOverScreen.i_animationFrameSizeHeight=128;
		gameOverScreen.i_animationFrameSizeWidth=128;
		
		//start backing track
		Music music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/astarteslow.ogg"));
	    music.setLooping(true);
	    music.setVolume(0.5f);
	    music.play();	
	}
	
	//when user presses back it should exit if they just launched but if they came from game it should return to current game
	public boolean b_cameFromGame = false;
	private float f_holoCentreX;
	private float f_holoCentreY;
	private float f_screenCentreX = 320/2;
	private float f_screenCentreY = 480/2;
	
	
	public void backPressed()
	{
		//if level select is visisble hand it on to it instead
		if( myUI_LevelSelect.b_visible == true)
		{
			myUI_LevelSelect.backPressed();
			return;
		}
			
		//if game is running go to menu
		if(myLogicEngine.b_paused==false)
			myLogicEngine.b_paused=true;
		else
		//if game has just loaded and they hit back then exit
		if(!b_cameFromGame)
			//exit
			Gdx.app.exit();
		else
        // Do your optional back button handling (show pause menu?)
    	myLogicEngine.b_paused=false;

	}
	
	public void onClick(int in_x, int in_y)
	{
		//scale back to absolute coordinates
		in_x = (int) (in_x / GameRenderer.dpiTextureCoordinatesAdjust);
		in_y = (int) (in_y / GameRenderer.dpiTextureCoordinatesAdjust);
		
		System.out.println(" in_x:"+ in_x + " in_y:"+ in_y);
		
		if(myDialog != null)
		{
			if(myDialog.onClick(in_x, in_y))
				myDialog = null;

			//clicked the dialog so dont do antying on underlying form
			return;
		}
		
		//if level select is visisble hand it on to it instead
		if( myUI_LevelSelect.b_visible == true)
		{
			myUI_LevelSelect.onClick(this,in_x, in_y);
			return;
		}
		
		Point2d p_clicked = new Point2d(in_x,in_y);
		
		//see what button they clicked on
		if(btn_exit.inRect(p_clicked))
			Gdx.app.exit();
		
		if(btn_launch.inRect(p_clicked))
		{
			SoundEffects.getInstance().launch.play(SoundEffects.SPEECH_VOLUME);
			myUI_LevelSelect.b_visible = true;
		}
		
		if(btn_help.inRect(p_clicked))
		{
			myDialog = new UI_Dialog(str_help);
		}
		
		//see if they clicked on a formation
		for(int i=0 ; i< rects_formations.size();i++)
			if(rects_formations.get(i).inRect(p_clicked))
			{
				i_selectedFormation = i;
				Formation.i_formation = i;
				for(int j=0;j<go_ships.length;j++)
				{	
					go_ships[j].v.clearWaypoints();
					go_ships[j].v.addWaypoint(new Point2d((f_holoCentreX -10)+ Formation.getXOffsetForShip(j),f_holoCentreY + Formation.getYOffsetForShip(j)));
					followPathBehaviours[j].i_waypointCounter=0;
				}
			}
		
		for(int i=0 ; i< rects_advantages.size();i++)
			if(rects_advantages.get(i).inRect(p_clicked))
			{	
				
				//invert selected status
				Advantage.b_advantages[i]=!Advantage.b_advantages[i];
				
				//only play sound when toggling on
				if(Advantage.b_advantages[i])
				{
					Advantage.playAudioForAdvantage(i);
				
				}
				
				//refresh the advantages
				Advantage.clearSimulatedAdvantages(go_ships,go_otherStuff);
				Advantage.simulateAdvantages(go_ships,go_otherStuff,new Point2d(this.f_holoCentreX-10,this.f_holoCentreY));
				calculateAndUpdateNumberOfShips();
			}
		
		for(int i=0 ; i< rects_advantages2.size();i++)
			if(rects_advantages2.get(i).inRect(p_clicked))
			{
				if(isAdvantageUnlocked(5+i) && i<3)
				{
					Advantage.b_advantages[5+i]=!Advantage.b_advantages[5+i];
					
					//only play sound when toggling it on
					if(Advantage.b_advantages[5+i] ==true)
						Advantage.playAudioForAdvantage(5+i);
					
					
				}
				else
				{
					
						
					//make sure it refreshes it
					if(str_messageBox.equals(this.advantageLockedText(5+i)))
						str_messageBox = this.advantageLockedText(5+i) + " "; //Such a hack
					else
						str_messageBox = this.advantageLockedText(5+i);
					
					myDialog = new UI_Dialog(this.advantageLockedText(i+5));
					
					SoundEffects.getInstance().locked.play(SoundEffects.SPEECH_VOLUME);
				}
				
				//refresh the advantages
				Advantage.clearSimulatedAdvantages(go_ships,go_otherStuff);
				Advantage.simulateAdvantages(go_ships,go_otherStuff,new Point2d(this.f_holoCentreX-10,this.f_holoCentreY));
				calculateAndUpdateNumberOfShips();
			}
	}
	
	public void stepUI(LogicEngine in_logicEngine)
	{
		
		for(int i=0;i<go_ships.length;i++)
			for(int j=0;j<go_ships[i].stepHandlers.size();j++)
				go_ships[i].stepHandlers.get(j).handleStep(in_logicEngine, go_ships[i]); 
	}
	
	public void drawUI(GameRenderer in_render, SpriteBatch batch)
	{
		//draw subscreens instead
		if(myUI_LevelSelect.b_visible)
		{
			myUI_LevelSelect.drawUI(in_render, batch);
			return;
		}
			
		txt_copyright.display(batch);
		
		//load UI
		if(b_loadedUI == false)
		{
			//load interface texture
			tex_interface = in_render.loadTexture("data/"+GameRenderer.dpiFolder+"/interface.png");
		
			
			tr_selected = in_render.loadTextureRegion(
						"data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 0, 3,
						50, 50);
			
			b_loadedUI = true;
			
			//load formations
			for(int i=0;i<5;i++)
			{

				int i_StartingX = 24;
				int i_StartingY = 372;
				int i_ButtonSize = 29; //assuming button is square
				int i_ButtonSpacing = 4;
				
				Rect rect_Button = new Rect(i_StartingX + i*(i_ButtonSize+i_ButtonSpacing),i_StartingY-i_ButtonSize,i_StartingX + ((i+1)*(i_ButtonSize+i_ButtonSpacing)),i_StartingY);
			
				rects_formations.add(rect_Button);
				
			}
			
			//load advantages
			for(int i=0;i<5;i++)
			{
				int i_StartingX = 25;
				int i_StartingY = 244;
				int i_blockSize =54;
				
				Rect rect_Button = new Rect(i_StartingX + i*i_blockSize,i_StartingY-i_blockSize,i_StartingX + ((i+1)*i_blockSize),i_StartingY);
				rects_advantages.add(rect_Button);
				
				Rect rect_Button2 = new Rect( i_StartingX + i*i_blockSize, (i_StartingY-(i_blockSize*2)),i_StartingX + ((i+1)*i_blockSize),i_StartingY-i_blockSize);
				rects_advantages2.add(rect_Button2);
			}
			
			//load padlock
			textr_padlock = in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 9, 0, 50, 50);
		}
		
		texr_title = in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 4, 1, 200, 100);
		texr_launch= in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 2, 0, 100, 50);
		texr_exit= in_render.loadTextureRegion("data/"+GameRenderer.dpiFolder+"/interface.png", tex_interface, 3, 0, 100, 50);
		
		//draw title screen
		Utils.drawRect(texr_title,btn_title,batch);
		Utils.drawRect(texr_launch,btn_launch,batch);
		Utils.drawRect(texr_exit,btn_exit,batch);
		
		
		in_render.drawAbsoluteDrawable(d_newUI,f_screenCentreX - (d_newUI.i_animationFrameSizeWidth/2) ,f_screenCentreY - (d_newUI.i_animationFrameSizeHeight/2)   );
		
		
		
	
		//draw "selected" texture on top row
		for(int i=0;i<Advantage.b_advantages.length;i++)
			if(Advantage.b_advantages[i]==true)
				if(i<5)
					Utils.drawRect(tr_selected, this.rects_advantages.get(i), batch);
				else
					Utils.drawRect(tr_selected, this.rects_advantages2.get(i-5), batch);	
		
			
		for(int i=0;i<go_ships.length;i++)
		{
			in_render.drawAbsoluteDrawable((Drawable)go_ships[i],(float)go_ships[i].v.getX(),(float)go_ships[i].v.getY());
			//draw buffs
			for(int j=0;j<go_ships[i].visibleBuffs.size();j++)
				in_render.drawAbsoluteDrawable((Drawable)go_ships[i].visibleBuffs.get(j),(float)go_ships[i].v.getX(),(float)go_ships[i].v.getY());
		}
		for(int i=0;i<go_otherStuff.size();i++)
			in_render.drawAbsoluteDrawable((Drawable)go_otherStuff.get(i),(float)go_otherStuff.get(i).v.getX(),(float)go_otherStuff.get(i).v.getY());
			
		
		//draw padlocks
		for(int i=0;i<3;i++)
			if(!isAdvantageUnlocked(5+i))
				Utils.drawRect(textr_padlock, rects_advantages2.get(i), batch);
		
		//draw ship costs
		this.txt_cost.displayAbsolute(batch);
		this.txt_ships.displayAbsolute(batch);
		
		txt_chooseFormation.displayAbsolute(batch);
		txt_chooseAdvantage.displayAbsolute(batch);
		
		if(myDialog != null)
			myDialog.drawUI(in_render, batch);
		
		//Utils.DisplaySystemMessage(str_messageBox, batch);
		
		
	}

}
