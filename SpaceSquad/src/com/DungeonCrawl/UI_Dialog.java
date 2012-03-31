package com.DungeonCrawl;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.steeringbehaviors.simulation.renderer.Point2d;

public class UI_Dialog 
{
	TextDisplaying txt_msg;
	public boolean b_isVisible=false;
	Drawable d_dialog;
	
	public UI_Dialog(String in_msg) 
	{
		d_dialog = new Drawable();
		d_dialog.str_spritename = "data/"+GameRenderer.dpiFolder+"/dialog.png";
			
			
		d_dialog.i_animationFrameSizeHeight = 337;
		d_dialog.i_animationFrameSizeWidth = 250;
		
		 txt_msg = new TextDisplaying(in_msg, LogicEngine.SCREEN_WIDTH/2 - (d_dialog.i_animationFrameSizeWidth/2) + 10 ,  LogicEngine.SCREEN_HEIGHT/2 +(d_dialog.i_animationFrameSizeHeight/2) -30, false);
		 b_isVisible = true;
	}
	
	//returns true if to dispose of
	public boolean onClick(int in_x, int in_y)
	{
		b_isVisible = false;
		return true;
	}
	
	public void drawUI(GameRenderer in_render, SpriteBatch batch)
	{
		in_render.drawDrawable(d_dialog,LogicEngine.SCREEN_WIDTH/2 , LogicEngine.SCREEN_HEIGHT/2 );
		
		txt_msg.getFontForDrawing().drawWrapped(batch, txt_msg.toDisplay ,txt_msg.x * GameRenderer.f_pixelAdjustX, txt_msg.y* GameRenderer.f_pixelAdjustY, (d_dialog.i_animationFrameSizeWidth-20)* GameRenderer.f_pixelAdjustX,  BitmapFont.HAlignment.CENTER);
	}
	
	
}
