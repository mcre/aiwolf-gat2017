package net.mchs_u.mc.aiwolf.curry;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.VoteContentBuilder;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.util.Counter;

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
		if(!isTodayVoted()) { // 初回
			setTodayVoted(true);
			return decideVoteTarget();
		}
		// 再投票
		Counter<Agent> c = new Counter<>();
		for(Vote v: getGameInfo().getLatestVoteList())
			c.add(v.getTarget());
		
		// 最多投票のうち客観目線で最も人狼っぽいひとに投票
		return max(c.getOver(c.get(c.getLargest())).keySet(), getObjectiveEstimate().getWerewolfLikeness(), false);
	}
	
	//生存者のうち、客観目線で最も人狼っぽいひとに投票
	private Agent decideVoteTarget(){
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		
		return max(candidate, getObjectiveEstimate().getWerewolfLikeness(), false);
	}	
}
