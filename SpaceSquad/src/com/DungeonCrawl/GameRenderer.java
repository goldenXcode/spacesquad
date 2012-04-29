package com.DungeonCrawl;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;



import com.DungeonCrawl.AreaEffects.AreaEffect;
import com.DungeonCrawl.AreaEffects.SimpleAreaEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.steeringbehaviors.simulation.renderer.Vector2d;

public class GameRenderer {
    
	public static Vector2d NORTH_VECTOR = new Vector2d(1.0,0.0);
	   private Texture        targetTexture;          
	             
	   
       private SpriteBatch batch;
       
       private LogicEngine theLogicEngine;
        
       private Map<String,Texture> textureDictionary = new HashMap<String, Texture>();
       private Map<String,TextureRegion> textureRegionDictionary = new HashMap<String, TextureRegion>();
       private UI theUI;
       private MyInput MyInputProcessor;

       public static float f_pixelAdjustX;
       public static float f_pixelAdjustY;
       
       
       public static String dpiFolder;
       public static float dpiTextureCoordinatesAdjust;
       public static float f_pixelAdjustAbsoluteX;
       public static float f_pixelAdjustAbsoluteY;
	
       
       public GameRenderer(LogicEngine in_LogicEngine )
       {
    	   
    	   theLogicEngine = in_LogicEngine;
    	   	
    	   //setup user interface
    	   theUI = new  UI(in_LogicEngine);
    	   
    	   //setup input
    	   MyInputProcessor = new MyInput(theUI);
    	   MyInputProcessor.setLogicEngine(in_LogicEngine);
    	   
    	   
    	   in_LogicEngine.init(MyInputProcessor);
    	   in_LogicEngine.setUI(theUI);
    	   
    	   
   
    	  
       }
       public Texture loadTexture(String str_spritename)
       {
    	   Texture toReturn = textureDictionary.get(str_spritename);
		   
		   //if texture is not in dictionary available
		   if(toReturn == null)
		   {
			   toReturn = new Texture(Gdx.files.internal(str_spritename));
			   textureDictionary.put(str_spritename, toReturn);
		   }
		   
		   return toReturn;
       }
       public TextureRegion loadTextureRegion(String str_spritename,Texture toDrawTex, int i_animationFrameRow, int i_animationFrame, int in_animationFrameSizeWidth, int in_animationFrameSizeHeight)
       {
    	   TextureRegion toReturn = textureRegionDictionary.get(str_spritename + i_animationFrameRow+i_animationFrame+in_animationFrameSizeWidth+ in_animationFrameSizeHeight);
		   
		   if(toReturn ==null)
		   {   
			   //create new texture region at the relevant offset
			   toReturn = new TextureRegion(toDrawTex,
			   (int)(i_animationFrame * in_animationFrameSizeWidth * GameRenderer.dpiTextureCoordinatesAdjust),
			   (int)(in_animationFrameSizeHeight*i_animationFrameRow * GameRenderer.dpiTextureCoordinatesAdjust),
					  (int)( in_animationFrameSizeWidth * GameRenderer.dpiTextureCoordinatesAdjust),
					  (int) (in_animationFrameSizeHeight * GameRenderer.dpiTextureCoordinatesAdjust));
			    
		   
		   textureRegionDictionary.put(str_spritename +i_animationFrameRow+ i_animationFrame+in_animationFrameSizeWidth+ in_animationFrameSizeHeight, toReturn);
		   }
		   
		   return toReturn;
       }
       
       public void drawObject(GameObject toDraw)
       {
    	   if(toDraw==null)
    		   return;
    	 
    	    //TODO simplify this a lot!
		   Texture toDrawTex = loadTexture(toDraw.str_spritename);
	
		   
	
		   //see if it is in dictionary
		   TextureRegion tr = loadTextureRegion(toDraw.str_spritename, toDrawTex, toDraw.i_animationFrameRow, toDraw.i_animationFrame, toDraw.i_animationFrameSizeWidth, toDraw.i_animationFrameSizeHeight);
		   
		   
		   Sprite s = null;
		   	
		   if(toDraw.i_animationFrameSizeWidth !=0)   
			   s = new Sprite(tr);
		   else
			   s = new Sprite(toDrawTex);
		   
		   s.setPosition(((float)toDraw.v.getX()-toDraw.i_animationFrameSizeWidth/2)*f_pixelAdjustX, ((float)toDraw.v.getY()-toDraw.i_animationFrameSizeHeight/2)*f_pixelAdjustY);
		   
		   if(toDraw.f_forceRotation != 0 )
			   s.setRotation(toDraw.f_forceRotation);
		   else
		   if(toDraw.rotateToV)
			   s.setRotation(85+Utils.getAngleOfRotation(toDraw.v.getVel()));
		   
		   
		   if(toDraw.f_forceScaleX !=0 || toDraw.f_forceScaleY !=0)
			   s.setScale(toDraw.f_forceScaleX,toDraw.f_forceScaleY);
		
		   if(toDraw.c_Color != null)	
			   s.setColor(toDraw.c_Color);
		   
		   if(toDraw.b_mirrorImageHorizontal)
			   s.flip(true, false);
		   
		   s.draw(batch);
    	   
    	   
    	   //draw objects buffs/debuffs ontop of it
    	   for(int i=0;i<toDraw.visibleBuffs.size();i++)
    		   drawDrawable(toDraw.visibleBuffs.get(i),(float)toDraw.v.getX(),(float)toDraw.v.getY());
    	   
       }
       
       public void drawDrawable(Drawable toDraw, float in_x, float in_y)
       {
    	   //TODO Fix PENDING
    	   Texture toDrawTex = loadTexture(toDraw.str_spritename);
		   
    	   TextureRegion tr = loadTextureRegion(toDraw.str_spritename, toDrawTex, toDraw.i_animationFrameRow, toDraw.i_animationFrame, toDraw.i_animationFrameSizeWidth, toDraw.i_animationFrameSizeHeight);
    	   
    	   Sprite s = new Sprite(tr);
    	       	   
    	   if(toDraw.i_animationFrameSizeWidth !=0)   
			   s = new Sprite(tr);
		   else
			   s = new Sprite(toDrawTex);
		   
    	   
		   s.setPosition(((float)in_x-toDraw.i_animationFrameSizeWidth/2)*f_pixelAdjustX, ((float)in_y-toDraw.i_animationFrameSizeHeight/2)*f_pixelAdjustY);
		   
		   if(toDraw.f_forceRotation != 0 )
			   s.setRotation(85+toDraw.f_forceRotation); //TODO this is a hack hmn
		   		   
		   if(toDraw.f_forceScaleX !=0 || toDraw.f_forceScaleY !=0)
			   s.setScale(toDraw.f_forceScaleX,toDraw.f_forceScaleY);

    	   
		   s.draw(batch);
    	    	   
       }
       
   	public void drawAbsoluteDrawable(Drawable toDraw, float in_x,
			float in_y) {
   	  //TODO Fix PENDING
 	   Texture toDrawTex = loadTexture(toDraw.str_spritename);
		   
 	   TextureRegion tr = loadTextureRegion(toDraw.str_spritename, toDrawTex, toDraw.i_animationFrameRow, toDraw.i_animationFrame, toDraw.i_animationFrameSizeWidth, toDraw.i_animationFrameSizeHeight);
 	   
 	   Sprite s = new Sprite(tr);
 	       	   
 	   if(toDraw.i_animationFrameSizeWidth !=0)   
			   s = new Sprite(tr);
		   else
			   s = new Sprite(toDrawTex);
		   
 	   
		   
		   if(toDraw.f_forceRotation != 0 )
			   s.setRotation(85+toDraw.f_forceRotation); //TODO this is a hack hmn
		   		   
		   if(toDraw.f_forceScaleX !=0 || toDraw.f_forceScaleY !=0)
			   s.setScale(toDraw.f_forceScaleX,toDraw.f_forceScaleY);

		   s.setPosition(in_x * GameRenderer.dpiTextureCoordinatesAdjust, in_y* GameRenderer.dpiTextureCoordinatesAdjust);
		   
 	   
		   s.draw(batch);
 	   
		
	}
    int i_frameRate=0;
    long l_frameRateLastReportedMillis=0;
    
	public void render()
	{
		//report frame rate every 5s
		if(System.currentTimeMillis() - l_frameRateLastReportedMillis > 5000)
		{
			l_frameRateLastReportedMillis =System.currentTimeMillis() ;
			System.out.println("Frame Rate:" + (i_frameRate/5));
			i_frameRate = 0;
			
		}
		else
		{
			i_frameRate++;
		}
			//if we need to load textures
		   if(targetTexture == null && batch == null)
           {
			 
			 
			   targetTexture = new Texture(Gdx.files.internal("data/"+GameRenderer.dpiFolder+"/target.png"));    // #3

         		 
         		  batch = new SpriteBatch();                                      // #4
           }
                                // #7
		   //clear screen
		   Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		   
		   
		   batch.begin();                                  // #5
		   		                
		  
		   if(theLogicEngine.b_paused)
		   {
			   theLogicEngine.background.drawBackground(batch);
			   theLogicEngine.background.stepBackgroundSlow();
			   theUI.drawUI(this, batch);
			   batch.end(); 
			   return;
		   }

			   
		  
           try
           {
        	   theLogicEngine.background.drawBackground(batch);
	           
	           //draw obstacles

        	   theLogicEngine.objectsObstaclesLock.writeLock().lock();
        	   
	           Iterator<GameObject> iterator = theLogicEngine.objectsObstacles.iterator();
	           
	           
	           while(iterator.hasNext())
	           {
	        	   GameObject toDraw = iterator.next();
	        	   drawObject(toDraw);
	        		   
	           }
	           
	           theLogicEngine.objectsObstaclesLock.writeLock().unlock();
	           
	           
	           
	           
	           //draw underlay
	           for(int i=0;i<theLogicEngine.objectsUnderlay.size();i++)
	        		   drawObject(theLogicEngine.objectsUnderlay.get(i));
	        		   
	           //draw text
	           if(theLogicEngine.currentTextBeingDisplayed!=null)
	        	   for(int i=0;i<theLogicEngine.currentTextBeingDisplayed.size();i++)
	        		   theLogicEngine.currentTextBeingDisplayed.get(i).display(batch);
			   
	           

	           //draw area effects
	           for(int i=0 ; i<theLogicEngine.currentAreaEffects.size();i++)
	        	   drawArea(theLogicEngine.currentAreaEffects.get(i));
	           	   

	           //draw player area effects
	           for(int i=0 ; i<theLogicEngine.currentAreaEffectsPlayers.size();i++)
	        	   drawArea(theLogicEngine.currentAreaEffectsPlayers.get(i));
	           	   
	           
	           //draw targets
	           for(int i=0 ;i<5 ;i++)
	        	   if(theLogicEngine.MyInputProcessor.targetsActive[i])
	        		   batch.draw(targetTexture, theLogicEngine.MyInputProcessor.Targets[i].x * f_pixelAdjustX, theLogicEngine.MyInputProcessor.Targets[i].y* f_pixelAdjustY);             // #6
	           
	           
	           
	           //draw objects
	           for(int i=0 ; i<theLogicEngine.objectsPlayers.size() ;i++)
	        	   if(theLogicEngine.objectsPlayers.get(i) !=null)
	        	   {
	        		  drawObject(theLogicEngine.objectsPlayers.get(i));
	        		   
	        	   }
	        	   else
	        		   break;
	          
	           //draw enemies
	           for(int i=0 ; i<theLogicEngine.objectsEnemies.size();i++)
	           {
	        	   drawObject(theLogicEngine.objectsEnemies.get(i));
	           }
	           
	           //draw powerups
	           for(int i=0 ; i<theLogicEngine.objectsPowerups.size();i++)
	           {
	        	   drawObject(theLogicEngine.objectsPowerups.get(i));
	           }
	           
	        
	           
	           
	           //draw bullets
	           for(int i=0 ; i<theLogicEngine.objectsPlayerBullets.size();i++)
	           {
	        	   drawObject(theLogicEngine.objectsPlayerBullets.get(i));
	           }
	        	           
	           //draw asteroids etc
	           for(int i=0 ; i<theLogicEngine.objectsEnemyBullets.size();i++)
	           {
	        	   drawObject(theLogicEngine.objectsEnemyBullets.get(i));
	           }
	           
	        
	           //draw overlays
	           for(int i=0 ; i<theLogicEngine.objectsOverlay.size();i++)
	           {
	        	   drawObject(theLogicEngine.objectsOverlay.get(i));
	           }
       
	      
	           //draw life total
	           theLogicEngine.MyLifeCounter.drawLifeCounter(this,batch);
           }
           catch(Exception x)
           {
        	   if( !(x instanceof ArrayIndexOutOfBoundsException )&& !(x instanceof IndexOutOfBoundsException))
        		   x.printStackTrace();
        	   //	something was removed while we were in the process of rendering, nevermind we can render agian next frame!   
           }    
           
		   //if game over
		   if(theLogicEngine.MyLifeCounter.isGameOver(theLogicEngine))
			   drawDrawable(theUI.gameOverScreen,(LogicEngine.SCREEN_WIDTH/2), (LogicEngine.SCREEN_HEIGHT/2));
		   
           //batch.draw(druidTexture, 32, 32);             // #6
           batch.end();       
	}
	
	private void drawArea(AreaEffect areaEffect) {
		
		Texture toDrawTex = loadTexture(areaEffect.str_spritename);
		//see if it is in dictionary
		TextureRegion tr = loadTextureRegion(areaEffect.str_spritename, toDrawTex, areaEffect.i_animationFrameRow, areaEffect.i_animationFrame, areaEffect.i_animationFrameSizeWidth, areaEffect.i_animationFrameSizeHeight);
		Sprite s = new Sprite(tr);
   		s.setColor(areaEffect.c_Color);
   		
   		s.setPosition(
   				((float)areaEffect.area.getp1().getX() + ((float)areaEffect.area.getWidth()/2)-(areaEffect.i_animationFrameSizeWidth/2))*f_pixelAdjustX,
   				((float)areaEffect.area.getp1().getY()+((float)areaEffect.area.getHeight()/2)-(float)(areaEffect.i_animationFrameSizeHeight/2f))*f_pixelAdjustY);
	   
   		s.setScale(
   				((float)areaEffect.area.getWidth()/areaEffect.i_animationFrameSizeWidth),
   				((float)areaEffect.area.getHeight()/areaEffect.i_animationFrameSizeHeight));
	   
   		s.draw(batch);
		 
			//batch.draw(druidTexture, 200, 100, 32, 32, 64, 64, 1f, 2.0f, 45f, 0, 0, 64, 64, false, false);
		
	}

}
