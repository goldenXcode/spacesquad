package com.DungeonCrawl;

import de.steeringbehaviors.simulation.renderer.Geometrie;

public class Pair<T> 
{
	public T item1;
	public T item2;
	
	Pair(T in_item1, T in_item2)
	{
		item1 = in_item1;
		item2 = in_item2;
	}

	public boolean contains(T a) {
		if(a == item1 || a==item2)
			return true;
		else
			return false;
	}
	
	

}
