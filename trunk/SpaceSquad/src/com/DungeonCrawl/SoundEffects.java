package com.DungeonCrawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundEffects 
{

		
	private static SoundEffects instance;
	
	
	public Sound easy;
	public Sound medium;
	public Sound hard;
	public Sound launch;
	
	public Sound explosion;
	public Sound shield;
	public Sound dualfire;
	public Sound rapidfire;
	public Sound wingmen;
	public Sound warningThreatApproaching;
	public Sound mines;
	public Sound slow;
	
	
	public Sound enhanced_weapons;
	public Sound squad_shields;
	public Sound cluster_missiles;
	public Sound extra_lives;
	public Sound graviton;
	public Sound stealth;
	public Sound sidecannons;
	public Sound laser;
	public Sound homing;
	public Sound elitesquad;
	public Sound missiles;
	public Sound locked;
	
	public Music intro;				
	public Music intro2;				
	public Music intro3	;			
	
	public Sound out_of_lives;		
	
	public Sound levels[];


	public static float SPEECH_VOLUME = 0.5f;

	
	private SoundEffects()
	{
		levels = new Sound[UI_LevelSelect.i_maxLevel];
		for(int i=1 ; i < UI_LevelSelect.i_maxLevel ;i++)
			levels[i] = Gdx.audio.newSound(Gdx.files.internal("data/sounds/level"+i+".ogg"));
		
		easy= Gdx.audio.newSound(Gdx.files.internal("data/sounds/easy.ogg"));
		medium= Gdx.audio.newSound(Gdx.files.internal("data/sounds/medium.ogg"));
		hard= Gdx.audio.newSound(Gdx.files.internal("data/sounds/hard.ogg"));
		launch= Gdx.audio.newSound(Gdx.files.internal("data/sounds/launch.ogg"));
		
		explosion = Gdx.audio.newSound(Gdx.files.internal("data/sounds/explosion.ogg"));
		shield = Gdx.audio.newSound(Gdx.files.internal("data/sounds/shields.ogg"));
		dualfire = Gdx.audio.newSound(Gdx.files.internal("data/sounds/dualfire.ogg"));
		rapidfire = Gdx.audio.newSound(Gdx.files.internal("data/sounds/rapidfire.ogg"));
		wingmen = Gdx.audio.newSound(Gdx.files.internal("data/sounds/wingmen.ogg"));
		mines = Gdx.audio.newSound(Gdx.files.internal("data/sounds/mines.ogg"));
		slow = Gdx.audio.newSound(Gdx.files.internal("data/sounds/slow.ogg"));
		warningThreatApproaching = Gdx.audio.newSound(Gdx.files.internal("data/sounds/warning.ogg"));
	
		
		enhanced_weapons = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/ehancedfirepower.ogg"));
		squad_shields = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/squadshields.ogg"));
		cluster_missiles = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/clustermissile.ogg"));
		extra_lives = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/additionalives.ogg"));
		graviton = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/graviton.ogg"));
		stealth = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/stealth.ogg"));
		sidecannons = Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/sidecannons.ogg"));
		laser= Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/laser.ogg"));
		homing= Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/homing.ogg"));
		elitesquad= Gdx.audio.newSound(Gdx.files.internal("data/sounds/advantages/elitesquad.ogg"));
		missiles =  Gdx.audio.newSound(Gdx.files.internal("data/sounds/missiles.ogg"));
		
		intro = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/intro.ogg"));
		intro2 = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/intro2.ogg"));
		intro3 = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/intro3.ogg"));
		out_of_lives = Gdx.audio.newSound(Gdx.files.internal("data/sounds/reinforcementsdepleted.ogg"));
		locked = Gdx.audio.newSound(Gdx.files.internal("data/sounds/locked.ogg"));
		
		intro.setVolume(SPEECH_VOLUME);
		intro2.setVolume(SPEECH_VOLUME);
		intro3.setVolume(SPEECH_VOLUME);
	}
	
	
	public static SoundEffects getInstance()
	{
		if(instance == null)
			instance = new SoundEffects();
		
		return instance;
	}


}
