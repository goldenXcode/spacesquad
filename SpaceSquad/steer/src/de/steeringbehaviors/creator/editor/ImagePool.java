package de.steeringbehaviors.creator.editor;


	import java.io.File;
	import java.net.URL;
	import java.util.Hashtable;

	
	import javax.swing.ImageIcon;


	/**
	 * The ImagePool is used to load and cache images
	 */
	public class ImagePool {

	    public static final String BACK_TILE 				= "de.steeringbehaviors.creator.resources.BackTile";
	    public static final String VEHICLE 				    = "de.steeringbehaviors.creator.resources.vehicle";
	    public static final String CIRCLEOBSTACLE    	    = "de.steeringbehaviors.creator.resources.circleobstacle";
	    public static final String RECTOBSTACLE	    	    = "de.steeringbehaviors.creator.resources.rectobstacle";
	    public static final String BEHAVIOR    	    		= "de.steeringbehaviors.creator.resources.behavior";
	    public static final String MIND			    	    = "de.steeringbehaviors.creator.resources.mind";
	    public static final String STEERING		    	    = "de.steeringbehaviors.creator.resources.steering";
	    public static final String ADDVEHICLE       	    = "de.steeringbehaviors.creator.resources.addvehicle";
	    public static final String ADDOBSTACLE	    	    = "de.steeringbehaviors.creator.resources.addobstacle";
	    public static final String ADDMIND		    	    = "de.steeringbehaviors.creator.resources.addmind";
	    public static final String ADDBEHAVIOR		   	    = "de.steeringbehaviors.creator.resources.addbehavior";
	    public static final String DELETE		    	    = "de.steeringbehaviors.creator.resources.delete";
	    public static final String CLONE		    	    = "de.steeringbehaviors.creator.resources.clone";
	    public static final String RUN			    	    = "de.steeringbehaviors.creator.resources.run";
	    
	    
	    
	    
	    
	    /** The ImagePool instance */
	    private static ImagePool pool;
	    /** Hashtable to cache the icons */
	    private Hashtable imageCache;
	    
	    /**
	     * Constructor
	     */
	    private ImagePool() {
	        imageCache = new Hashtable();
	    }
	    
	    /**
	     * Returns the ImagePool instance
	     * @return The ImagePool instance
	     */
	    public static final ImagePool getPool() {
	        if (pool == null) {
	            pool = new ImagePool();
	        }
	        return pool;
	    }
	    
	    /**
	     * Returns the icon specified with the id
	     * @param id The id of the icon
	     * @return The icon
	     */
	    public ImageIcon getIcon(String id) {

	        ImageIcon icon;
	        if ((icon = (ImageIcon)imageCache.get(id)) != null) {
	            return icon;
	        } else {
	            String file = id.replace(".", File.separator) + ".gif";
	            ClassLoader cl = ClassLoader.getSystemClassLoader();
	            URL url = cl.getResource(file);
	            
	         
	           
	            if (url != null) {
	                icon = new ImageIcon(url);
	                imageCache.put(id, icon);
	                return icon;
	            }
	                
	             System.err.println("Cannot load image "+file);
	             return new ImageIcon();
	         }
	            
	      }
	        
	        
	    
	   
	}
