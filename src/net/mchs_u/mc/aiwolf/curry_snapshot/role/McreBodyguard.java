package net.mchs_u.mc.aiwolf.curry_snapshot.role;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.common.data.Agent;

public class McreBodyguard extends McreVillager {
	
	//生存者のうち、自分目線で最も村人陣営っぽいひと
	@Override
	public Agent guard() {
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		return max(candidate, getSubjectiveEstimate().getVillagerTeamLikeness(), true);
	}	
}
