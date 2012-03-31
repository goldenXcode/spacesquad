package com.DungeonCrawl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Background {
	
	public Vector2 backgroundPosition;
	private TextureRegion  backgroundTexture;
	

	
	int i_backgroundWidth = 280;
	int i_backgroundHeight = 900;
	
	public Background()
	{
		backgroundPosition = new Vector2(0f,0f);
		 
		
		Texture toDrawTex = new Texture(Gdx.files.internal("data/"+GameRenderer.dpiFolder+"/background.png"));
		   
		   
		//see if it is in dictionary
		backgroundTexture = new TextureRegion(toDrawTex, 0,0, (int)(i_backgroundWidth * GameRenderer.dpiTextureCoordinatesAdjust), (int)(i_backgroundHeight* GameRenderer.dpiTextureCoordinatesAdjust));
		   
		   
	}

	public void setBackground(int in_background)
	{
		int x = i_backgroundWidth * in_background;
		
		backgroundTexture.setRegion((int)(x * GameRenderer.dpiTextureCoordinatesAdjust), 0, (int)(i_backgroundWidth* GameRenderer.dpiTextureCoordinatesAdjust) , (int)(i_backgroundHeight* GameRenderer.dpiTextureCoordinatesAdjust));
	}
	
	public void stepBackground()
	{
		//scroll background
    	backgroundPosition.y -= 3f;
    	//decide when to loop it
    	if(backgroundPosition.y < -i_backgroundHeight)
    		backgroundPosition.y = 0;
	}
	
	
	
	public void drawBackground(SpriteBatch batch) {
		  //draw background lead in if necessary
        if(this.backgroundPosition.y < (480-900) + (LogicEngine.f_worldCoordTop -480))
     	   	batch.draw(backgroundTexture, this.backgroundPosition.x , 
     	   			((this.backgroundPosition.y+i_backgroundHeight)*GameRenderer.f_pixelAdjustY )-1,
     	   			LogicEngine.f_worldCoordRight *GameRenderer.f_pixelAdjustX,
     	   			i_backgroundHeight*GameRenderer.f_pixelAdjustY);             

        //draw background
        batch.draw(backgroundTexture, this.backgroundPosition.x, 
        		(this.backgroundPosition.y)*GameRenderer.f_pixelAdjustY,
        		(LogicEngine.f_worldCoordRight *GameRenderer.f_pixelAdjustX),
        		i_backgroundHeight*GameRenderer.f_pixelAdjustY);

        
        //Gdx.gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        
		
	}

	public void stepBackgroundSlow() {
		// //scroll background
    	backgroundPosition.y -= 1.5f;
    	//decide when to loop it
    	if(backgroundPosition.y < -i_backgroundHeight)
    		backgroundPosition.y = 0;
		
	}

}
