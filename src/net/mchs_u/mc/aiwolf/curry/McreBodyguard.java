package net.mchs_u.mc.aiwolf.curry;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.net.GameInfo;

public class McreBodyguard extends McreVillager {
	
	@Override
	public void dayStart() {
		super.dayStart();
		GameInfo gi = getGameInfo();
		getSubjectiveEstimate().updateGuardedResult(gi.getGuardedAgent(), gi.getLastDeadAgentList().size());
	}

	//生存者のうち、自分目線で最も村人陣営っぽいひと
	@Override
	public Agent guard() {
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		candidate.remove(getGameInfo().getLatestExecutedAgent());
		return max(candidate, getSubjectiveEstimate().getVillagerTeamLikeness(), true);
	}
	
	
}
