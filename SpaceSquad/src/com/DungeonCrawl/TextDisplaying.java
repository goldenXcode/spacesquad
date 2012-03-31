package com.DungeonCrawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextDisplaying 
{
	
	static  BitmapFont bf_letters = null;
	static  BitmapFont bf_lettersHeaders = null;
	
	public String toDisplay;
	public float x;
	public float y;
	
	boolean b_isCentred=false;
	boolean b_isMultiLine=false;
	
	public TextDisplaying(String in_toDisplay,float in_x,float in_y,boolean in_isCentred)
	{
		if(in_toDisplay.indexOf("\n") != -1)
			b_isMultiLine=true;
		
		//use large text if not mdpi
		if(!GameRenderer.dpiFolder.equals("mdpi")) 
			b_isHeader=true;
		
		//create bitmap letters
		if(bf_letters == null)
			bf_letters = new BitmapFont(Gdx.files.internal("data/testfont.fnt"),new TextureRegion(new Texture(Gdx.files.internal("data/testfont.png"))), false);
		
		if(bf_lettersHeaders ==null)
			bf_lettersHeaders = new BitmapFont(Gdx.files.internal("data/largefont.fnt"),new TextureRegion(new Texture(Gdx.files.internal("data/largefont.png"))), false);
			
		toDisplay = in_toDisplay;
		
		x = in_x;
		y = in_y;
		
		if(in_isCentred)
			x -= (bf_letters.getBounds(in_toDisplay).width/2);
		
		b_isCentred = in_isCentred;
	}
	
	public void setX(float in_x)
	{
		x = in_x;
		
		if(b_isCentred)
			x -= (bf_letters.getBounds(toDisplay).width/2);
	}
	
	//for if you want to draw it yourself using one of the complicated draw methods
	public BitmapFont getFontForDrawing()
	{
		if(b_isHeader)
			return bf_lettersHeaders;
		else 
			return bf_letters;
	}

	public void display(SpriteBatch in_batch )
	{
		if(b_isHeader)
			if(b_isMultiLine)
				bf_lettersHeaders.drawMultiLine(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
			else
				bf_lettersHeaders.draw(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
		else
			if(b_isMultiLine)
				bf_letters.drawMultiLine(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
			else
				bf_letters.draw(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
	}
	public void display(SpriteBatch in_batch ,float f_alpha)
	{
		if(f_alpha == 0)
			return;
		if(b_isHeader)
		{
			bf_lettersHeaders.setColor(1, 1, 1, f_alpha);
			bf_lettersHeaders.draw(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
			bf_lettersHeaders.setColor(1,1,1,1);
		}
		else
		{
			bf_letters.setColor(1, 1, 1, f_alpha);
			bf_letters.draw(in_batch, toDisplay, x*GameRenderer.f_pixelAdjustX, y*GameRenderer.f_pixelAdjustY);
			bf_letters.setColor(1,1,1,1);
		}
	}

	boolean b_isHeader=false;
	public void makeHeader() {
		b_isHeader=true;	
		
		if(b_isCentred)
		{
			//move it back right the normal letters width (to undo previous adjustment)
			x += (bf_letters.getBounds(toDisplay).width/2);
			//move it back right for the new letters width
			x -= (bf_lettersHeaders.getBounds(toDisplay).width/2);
		}
		if(b_isCentred)
			y += (bf_lettersHeaders.getBounds(toDisplay).height/2);
	}

	public void displayAbsolute(SpriteBatch in_batch) {
		if(b_isHeader)
			if(b_isMultiLine)
				bf_lettersHeaders.drawMultiLine(in_batch, toDisplay, x, y);
			else
				bf_lettersHeaders.draw(in_batch, toDisplay, x, y);
		else
			if(b_isMultiLine)
				bf_letters.drawMultiLine(in_batch, toDisplay, x, y);
			else
				bf_letters.draw(in_batch, toDisplay, x, y);
		
	}


}
