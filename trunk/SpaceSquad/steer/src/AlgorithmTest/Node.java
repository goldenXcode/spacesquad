package AlgorithmTest;


/**
 * This is one node of the scene. 
 */
public class Node implements Comparable
{
	public Node(int x, int y)
	{
		posX = x;
		posY = y;
	}		
	
	/** 
	 * Calculates the cost of going from this node to the other node
	 * It is presumed that both are neighbouring nodes.
	 */
	public int calcCost(Node next)
	{
		return (int) (cost + next.cost);	
	}
	
	public int compareTo(Object o)
	{
		Node n = (Node) o;
		int result = 0;	
		
		// The node with the least cost path goes first...
		if (n.f > f) result = -1;
		if (n.f < f) result = 1;
		
		return result;
	}
	
	//
	// Pathing States
	//
	
	/** The Node is in no pathfinding - list */
	public static final int NO_LIST = 0;
	/** The Node is in the open list */
	public static final int OPEN_LIST = 1;
	/** The Node is in the closed list */
	public static final int CLOSED_LIST = 2;
	/** The Node is part of the final path */
	public static final int FINAL_PATH = 3;
	
	//
	// Cost values
	//
	
	/** */
	public static final int COST_NONE = 2;
	public static final int COST_MEDIUM = 10;
	public static final int COST_HARD = 20;
	public static final int COST_IMPOSSIBLE = 200;
	
	/** The x position of the node */
	public int	posX;
	/** The y position of the node */
	public int	posY;
	
	/** The cost of entering this tile */
	public int	cost = COST_NONE;
	
	/** The list this node is currently in */
	public int list = NO_LIST;
	
	/** The cost of traversing from start to this node */
	public int	f = 0;
	/** The actual cheapest cost of arriving at this node from the start */
	public int	g = 0;
	/** The heuristic estimate of the cost from this node to the goal */
	public int	h = 0;
	
	/** The parent node. When the finish Node has been reached, go back along this Node to get the complete path */ 
	public Node parent;
}