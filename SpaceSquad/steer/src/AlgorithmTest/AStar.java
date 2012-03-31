package AlgorithmTest;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

public class AStar extends SearchAlgorithm 
{
	public AStar(int TilesX, int TilesY, AlgorithmTest theApplet)
	{		
		m_TilesX = TilesX;
		m_TilesY = TilesY;
		m_Applet = theApplet;				
	}			
	
	public void setCost(int cost)
    {
        m_costEstimate = cost;
    }
	
	public void setFunction(int function)
	{
	    m_EstimateFunction = function;
	}
	
	public void setWait(int wait)
	{
	    m_Wait = wait;
	}
	
    
	
    public Vector findPath(Vector Scene, Node start, Node finish)
    {
        Vector	Path = new Vector(20, 10);
        
        boolean isInOpen;		// Set to true, if the 'next' node is in the open list
        boolean isInClosed;		// Set to true, if the 'next' node is in the closed list
        
        int 	posX, posY;		// Position of the next node
        
        int 	newG;			// Cost for going to the new node
        
        Node	current = null;		// The current node
        Node	next = null;	// The next node to visit from the current node
        
        Vector closed = new Vector(50, 50);
        Vector open = new Vector(50, 50);
        
        start.g = 0;								// The start node
        start.h = CalcEstimate(start, finish);		// Calc. the estimate cost
        start.f = start.g + start.h;				// The score for this node
        start.parent = null;
        
        open.add(start);
        
        while (!open.isEmpty())
        {
        	Collections.sort(open);
        	
        	current = (Node) open.firstElement();
        	open.remove(current);
        	
        	//System.out.println("Pop Node " + current.posX + ", " + current.posY);
        	
        	if (current == finish)
        	{
        		// Construct the path
        		Path.add(current);
        		current.list = Node.FINAL_PATH;
        		//System.out.println("Path Node: " + current.posX + ", " + current.posY);
        		
        		while (current.parent != null)
        		{
        			current = current.parent;
        			current.list = Node.FINAL_PATH;
        			//System.out.println("Path Node: " + current.posX + ", " + current.posY);
        			Path.add(current);
        		}
        		// We are finished
        		return Path;
        	}
        	
        	// Go through all successors of n and add them to the 
        	// open list if necessary
        	for (int y = -1; y < 2; y++)
        	{        		        		
        		for (int x = -1; x < 2; x++)
        		{
        			if ((x == 0) && (y == 0)) continue;
        			
        			posX = current.posX + x;
        			posY = current.posY + y;
        			
        			// Check for invalid values first
        			if ((posX < 0) || (posX >= m_TilesX)) continue;
        			if ((posY < 0) || (posY >= m_TilesY)) continue;
        			
        			next = (Node) Scene.elementAt(posY * m_TilesX + posX);
        			
        			// Check for blocked tile
        			if (next.cost == Node.COST_IMPOSSIBLE) continue;
        			
        			// Calc the cost of going to the node
        			newG = current.g + current.calcCost(next);	
        			
        			isInOpen = false;
        			isInClosed = false;
        			
        			// Check if this node has been touched, yet
        			if (open.contains(next)) 
        			{
        				isInOpen = true;         				
        			}
        			if (closed.contains(next))
        			{
        				isInClosed = true;        				
        			}		
        			
        			// We can skip this node, if it has already been touched 
        			// and resulted in a better path
        			if ( (isInOpen || isInClosed) && (next.g <= newG) ) continue;        					        			
        			
        			next.parent = current;        			
        			next.g = newG;        			
        			next.h = CalcEstimate(next, finish);        			
        			next.f = next.g + next.h;
        			
        			// Remove it from the closed list, since we have a better path to it
        			if (isInClosed) 
        			{
        				next.list = Node.NO_LIST;
        				closed.remove(next);
        				//System.out.println("Remove Node " + next.posX + ", " + next.posY + " from closed");
        			}
        			
        			// Add it to the list of open nodes
        			if (!isInOpen) 
        			{
        				next.list = Node.OPEN_LIST;
        				open.add(next);        				
        				//System.out.println("Add Node " + next.posX + ", " + next.posY + " to open");
        			}   
        			
        			// Wait a little bit, to show the workings of the algorithm
        			for (int w = 0; w < m_Wait * 1000; w++)
        			{
        				w += 1;
        				w -= 1;
        			}
        		}
        	}
        	// Add this node to the list of finished nodes
        	current.list = Node.CLOSED_LIST;
        	closed.add(current);        	
        	
        	m_Applet.repaint();
        	
        	if (m_Applet.m_AbortSearch) break;
        	
        	//System.out.println("Add Node " + current.posX + ", " + current.posY + " to closed");
        }
        
        // If we ended here, no path has been found
        
        // Print out all remaining nodes in the open list
        Iterator it = open.iterator();    
        if (!it.hasNext())
        	System.out.println("Open list is empty!");
        
        while (it.hasNext())
        {
        	current = (Node) it.next();
        	System.out.println("Open List Node " + current.posX + ", " + current.posY);
        }
        
        return Path;
    }

	/**
	 * Calculates the estimated cost of going from Node Start to Node Finish
	 */
	protected int CalcEstimate(Node Start, Node Finish)
	{
		int cost = 0;
		int dx, dy;
		
		switch (m_EstimateFunction)
		{
			case MAX_DX_DY:
				dx = java.lang.Math.abs(Finish.posX - Start.posX);
				dy = java.lang.Math.abs(Finish.posY - Start.posY);
				if (dx > dy)
					cost = dx * m_costEstimate;
				else 
					cost = dy * m_costEstimate;
				break;
			case EUCLIDEAN:
				dx = java.lang.Math.abs(Finish.posX - Start.posX);
				dy = java.lang.Math.abs(Finish.posY - Start.posY);
				dx *= dx;
				dy *= dy;
				cost = (int) java.lang.Math.sqrt(dx + dy) * m_costEstimate;				
				break;
			case DIAGONAL:
				dx = java.lang.Math.abs(Finish.posX - Start.posX);
				dy = java.lang.Math.abs(Finish.posY - Start.posY);
				
				if (dx > dy)
				{
				    cost = dx - dy;
				    cost += java.lang.Math.sqrt(dy * dy);
				    cost *= m_costEstimate;
				}
				else
				{
				    cost = dy - dx;
				    cost += java.lang.Math.sqrt(dy * dy);
				    cost *= m_costEstimate;
				}
				
				break;
			case MANHATTAN:
				cost = java.lang.Math.abs(Finish.posX - Start.posX);
				cost += java.lang.Math.abs(Finish.posY - Start.posY);
				cost *= m_costEstimate;
				break;			
		}
		
		return cost;
	}

	public static final int MAX_DX_DY = 0;	// The maximum of either dx or dy
	public static final int EUCLIDEAN = 1;	// Square Root of (dx^2 + dy^2)
	public static final int DIAGONAL = 2;	// Going diagonal until the row / col is reached
	public static final int MANHATTAN = 3;	// dx + dy	
	
	protected AlgorithmTest m_Applet = null;	
	
	protected int m_costEstimate = 0;
	
	private int m_EstimateFunction = MANHATTAN;			
	
	protected Vector	m_Path = null;
	
	private int m_TilesX = 0;
	private int m_TilesY = 0;
	
	private int m_Wait = 0;
}