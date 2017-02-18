package net.mchs_u.mc.aiwolf.curry_snapshot.role;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.lib.ComingoutContentBuilder;
import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.DivinedResultContentBuilder;
import org.aiwolf.client.lib.VoteContentBuilder;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class McrePossessed extends AbstractMcreRole {
	private Agent declaredVoteTarget = null; //今日最後に投票宣言をした対象
	
	private boolean co = false;
	private boolean divinedToday = false;
	private List<Agent> divinedList = null;
	
	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		super.initialize(gameInfo, gameSetting);

		co = false;
		divinedList = new ArrayList<>();
	}	
	
	@Override
	public void dayStart() {
		super.dayStart();
		divinedToday = false;
		declaredVoteTarget = null;
	}
	
	@Override
	public String talk() {
		//COしてない場合はCO
		if(!co){
			co = true;
			return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), Role.SEER)).getText();
		}
		
		//占い結果を言ってなければ占い
		if(getGameInfo().getDay() > 0 && !divinedToday){
			divinedToday = true;
			Agent target = decideDivineTarget();
			if(target != null){
				divinedList.add(target);
				getPretendVillagerEstimate().updateDefinedSpecies(target, Species.HUMAN);//村人目線に占い情報を更新
				return new Content(new DivinedResultContentBuilder(target, Species.WEREWOLF)).getText();
			}
		}
		
		//投票対象を宣言
		Agent target = decideVoteTarget();
		
		if(target == null)
			return Talk.SKIP;
		if(declaredVoteTarget != null && target.equals(declaredVoteTarget))
			return Talk.SKIP;
		declaredVoteTarget = target;
		return new Content(new VoteContentBuilder(target)).getText();
	}
	
	@Override
	public Agent vote() {
		return decideVoteTarget();
	}

	//自分目線で最も人間っぽい人を占って黒出し(3人まで)
	private Agent decideDivineTarget(){
		if(divinedList.size() >= 3)//3人黒出ししたらもう占わない
			return null;
		
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		for(Agent a:divinedList){
			candidate.remove(a);
		}
		return max(candidate, getSubjectiveEstimate().getVillagerTeamLikeness(), true);
	}
	
	//村人目線で、生存者のうち最も人狼っぽいひとに投票
	private Agent decideVoteTarget(){
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		
		return max(candidate, getPretendVillagerEstimate().getWerewolfLikeness(), false);
	}

}
