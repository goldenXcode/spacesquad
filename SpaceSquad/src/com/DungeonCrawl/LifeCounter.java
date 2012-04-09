package com.DungeonCrawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class LifeCounter {
	
	public int i_livesRemaining=95;
	Texture tex_digits;
//	TextureRegion[] tr_digits;
	
	Drawable d_healthPipGreen;
	Drawable d_healthPipYellow;
	Drawable d_healthPipRed;
	Drawable d_healthBar;
	
		
	public LifeCounter()
	{
		d_healthBar = new Drawable();
		d_healthPipGreen = new Drawable();
		d_healthPipYellow = new Drawable();
		d_healthPipRed = new Drawable();
		
		d_healthBar.str_spritename = "data/"+GameRenderer.dpiFolder+"/newui.png";
		d_healthBar.i_animationFrameSizeHeight = 34;
		d_healthBar.i_animationFrameSizeWidth = 100;
		
		
		d_healthPipGreen.str_spritename = "data/"+GameRenderer.dpiFolder+"/newui.png";
		d_healthPipGreen.i_animationFrameSizeHeight= 6;
		d_healthPipGreen.i_animationFrameSizeWidth= 6;
		d_healthPipGreen.i_animationFrameRow= 17;
		
		d_healthPipYellow.str_spritename = "data/"+GameRenderer.dpiFolder+"/newui.png";
		d_healthPipYellow.i_animationFrameSizeHeight= 6;
		d_healthPipYellow.i_animationFrameSizeWidth= 6;
		d_healthPipYellow.i_animationFrameRow= 17;
		d_healthPipYellow.i_animationFrame= 1;
		
		d_healthPipRed.str_spritename = "data/"+GameRenderer.dpiFolder+"/newui.png";
		d_healthPipRed.i_animationFrameSizeHeight= 6;
		d_healthPipRed.i_animationFrameSizeWidth= 6;
		d_healthPipRed.i_animationFrameRow= 17;
		d_healthPipRed.i_animationFrame= 2;
				
		
	}
	
	public void drawLifeCounter(GameRenderer in_render , SpriteBatch in_batch)
	{
		in_render.drawDrawable(d_healthBar, LogicEngine.SCREEN_WIDTH - 48, LogicEngine.SCREEN_HEIGHT - 15);
		
		Drawable d_pip = null;
		
		if(i_livesRemaining >10)
			d_pip = d_healthPipGreen;
		else
			if(i_livesRemaining >5)
				d_pip = d_healthPipYellow;
			else
				d_pip = d_healthPipRed;
		
		float f_pipStartX = LogicEngine.SCREEN_WIDTH -87;
		float f_pipStartY = LogicEngine.SCREEN_HEIGHT - 14;
		
		for(int i=0;i< Math.min(i_livesRemaining,15);i++)
			in_render.drawDrawable(d_pip, f_pipStartX  + (i*6) , f_pipStartY);
			//in_batch.draw(healthPip, (f_displayInX + 1 + (i*6))* GameRenderer.f_pixelAdjustX, (f_displayInY+13)* GameRenderer.f_pixelAdjustY);
		
		
		for(int i=15;i< i_livesRemaining ;i++)
			in_render.drawDrawable(d_pip, (f_pipStartX  + ((i-15)*6)), f_pipStartY+6);

			
	}

	public void addLives(int in_LivesToAdd) {
		// TODO Auto-generated method stub
		if(i_livesRemaining == 1 && in_LivesToAdd == -1)
			SoundEffects.getInstance().out_of_lives.play(SoundEffects.SPEECH_VOLUME);
		i_livesRemaining=Math.max(i_livesRemaining + in_LivesToAdd,0);
	}

	
	public void setLives(int i) {
		i_livesRemaining = i;
		
	}

	boolean areAllDead = false;
	
	public boolean isGameOver(LogicEngine in_logicEngine) {
		
		//if there are no ships remaining and all the ones on the playing field also dead
		if(i_livesRemaining ==0 && in_logicEngine.areAllPlayersDead())
		{
			//if they were not dead last step call gameover
			if(areAllDead ==false)
				in_logicEngine.MyLevelManager.getCurrentLevel().gameOver(in_logicEngine);
			
			areAllDead = true;
			return true;
		}
		
		areAllDead = false;
		return false;
	}

	public static int i_numberOfShipsAvailable =0;
	
	public void refreshLives() {
		setLives(i_numberOfShipsAvailable-4);
		
	}
		
}
