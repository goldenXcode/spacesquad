package AlgorithmTest;


import java.util.*;


public abstract class SearchAlgorithm
{
    public abstract void setCost(int cost);
    public abstract void setFunction(int function);
    public abstract void setWait(int wait);
    
	public abstract Vector findPath(Vector Scene, Node start, Node finish);
}