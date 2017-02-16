package net.mchs_u.mc.aiwolf.common;

import java.util.Map;

import org.aiwolf.common.data.Agent;

public abstract class AbstractEstimate {
	public abstract Map<Agent, Double> getWerewolfLikeness();
	public abstract Map<Agent, Double> getVillagerTeamLikeness();
}
