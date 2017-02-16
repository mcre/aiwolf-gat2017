package net.mchs_u.mc.aiwolf.common;

import org.aiwolf.common.data.Player;

public interface EstimatePlayer extends Player {
	public AbstractEstimate getObjectiveEstimate();
	public AbstractEstimate getSubjectiveEstimate();
	public AbstractEstimate getPretendVillagerEstimate();
}
