package com.DungeonCrawl.Powerups;

import com.DungeonCrawl.GameObject;
import com.DungeonCrawl.LogicEngine;

public interface Powerup {
public void applyPowerup(GameObject in_toApplyTo, LogicEngine in_logicEngine);
public void unApplyPowerup(GameObject in_toApplyTo);
public void collected();
}
