package net.mchs_u.mc.aiwolf.common;

import org.aiwolf.common.data.Player;

import net.mchs_u.mc.aiwolf.baikin04.Estimate;

public interface EstimatePlayer extends Player {
	public Estimate getObjectiveEstimate();
	public Estimate getSubjectiveEstimate();
	public Estimate getPretendVillagerEstimate();
}
