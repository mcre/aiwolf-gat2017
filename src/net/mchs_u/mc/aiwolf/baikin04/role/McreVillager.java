package net.mchs_u.mc.aiwolf.baikin04.role;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.VoteContentBuilder;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Talk;

public class McreVillager extends AbstractMcreRole {	
	protected Agent declareVotedTarget = null; //今日最後に投票宣言をした対象
	
	@Override
	public void dayStart() {
		super.dayStart();
		declareVotedTarget = null;
	}
	
	@Override
	public String talk() {
		Agent target = decideVoteTarget();
		if(target == null)
			return Talk.SKIP;
		if(declareVotedTarget != null && target.equals(declareVotedTarget))
			return Talk.SKIP;
		declareVotedTarget = target;
		return new Content(new VoteContentBuilder(target)).getText();
	}

	@Override
	public Agent vote() {
		return decideVoteTarget();
	}
	
	//生存者のうち、客観目線で最も人狼っぽいひとに投票
	private Agent decideVoteTarget(){
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		
		return max(candidate, getObjectiveEstimate().getWerewolfLikeness(), false);
	}	
}
