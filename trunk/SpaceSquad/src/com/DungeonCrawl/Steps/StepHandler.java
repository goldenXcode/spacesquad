package com.DungeonCrawl.Steps;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public interface StepHandler {
	
	public boolean handleStep(LogicEngine in_theLogicEngine,GameObject o_runningOn);

	public StepHandler clone() throws CloneNotSupportedException;

}
