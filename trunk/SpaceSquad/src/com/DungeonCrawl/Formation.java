package com.DungeonCrawl;

public class Formation {
	
	public static int i_formation;
	
	public static float getXOffsetForShip(int in_ship)
	{
		switch(i_formation)
		{
		//normal upside down T formation
		case 0:
			switch(in_ship)
			{
				case 0: return 0;
				case 1: return -15;
				case 2: return 0;
				case 3: return +15;
			}
		
			
		//2 front 2 side behind them formation
		case 1:
			switch(in_ship)
			{
				case 0: return -7;
				case 1: return -15;
				case 2: return +7;
				case 3: return +15;
			}
	
		//square
		case 2:
			switch(in_ship)
			{
				case 0: return -10;
				case 1: return -10;
				case 2: return +10;
				case 3: return +10;
			}
	
		//vertical line
		case 3:
			return 0.0f;
			//horizontal line 
		case 4:
			switch(in_ship)
			{
			case 0: return +27;
			case 1: return +9;
			case 2: return -9;
			case 3: return -27;
			}
	
		}
		return 0.0f;
	}
	
	public static float getYOffsetForShip(int in_ship)
	{
		switch(i_formation)
		{
		case 0:
			//normal upside down T formation
			switch(in_ship)
			{
				case 0: return 0;
				case 1: return -15;
				case 2: return -25;
				case 3: return -15;
			}
			
		//2 front 2 side behind them formation
		case 1:
			switch(in_ship)
			{
				case 0: return 0;
				case 1: return -25;
				case 2: return 0;
				case 3: return -25;
			}
			
		//square
		case 2:
			switch(in_ship)
			{
				case 0: return 0;
				case 1: return -25;
				case 2: return 0;
				case 3: return -25;
			}
			
			//vertical line
		case 3:
			switch(in_ship)
			{
			case 0: return +27;
			case 1: return +9;
			case 2: return -9;
			case 3: return -27;
			}
			
			//horizontal line 
		case 4:
			return 0.0f;
			
		}
		return 0.0f;
		
	}

}
