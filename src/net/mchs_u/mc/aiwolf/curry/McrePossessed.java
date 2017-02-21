package net.mchs_u.mc.aiwolf.curry;

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
	private boolean joinedPowerPlay = false; //パワープレイに参加済み
	private List<Agent> divinedList = null;
	
	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		super.initialize(gameInfo, gameSetting);

		co = false;
		joinedPowerPlay = false;
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
		Estimate e = getSubjectiveEstimate();
		// パワープレイ状態もしくは可能状態で未参加であればパワープレイに参加
		if((e.isPowerPlay() || e.isPowerPlayPossible()) && !joinedPowerPlay) {
			joinedPowerPlay = true;
			return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), getGameInfo().getRole())).getText();
		}
		
		if(!joinedPowerPlay) {
			//COしてない場合はCO
			if(!co){
				co = true;
				return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), Role.SEER)).getText();
			}
			
			//占い結果を言ってなければ占い
			if(getGameInfo().getDay() > 0 && !divinedToday){
				divinedToday = true;
				Agent dTarget = decideDivineTarget();
				if(dTarget != null){
					divinedList.add(dTarget);
					getPretendVillagerEstimate().updateDefinedSpecies(dTarget, Species.HUMAN);//村人目線に占い情報を更新
					return new Content(new DivinedResultContentBuilder(dTarget, Species.WEREWOLF)).getText();
				}
			}
		}
		
		Agent vTarget = decideVoteTarget();
		
		if(vTarget == null)
			return Talk.SKIP;
		if(declaredVoteTarget != null && vTarget.equals(declaredVoteTarget))
			return Talk.SKIP;
		declaredVoteTarget = vTarget;
		return new Content(new VoteContentBuilder(vTarget)).getText();
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
	
	private Agent decideVoteTarget(){
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		
		if(!joinedPowerPlay) { //村人目線で、生存者のうち最も人狼っぽいひとに投票
			return max(candidate, getPretendVillagerEstimate().getWerewolfLikeness(), false);			
		} else {
			Agent target = getSubjectiveEstimate().getLastVoteRequestTargetByWerewolves(); // 人狼に投票を促された相手
			if(target == null) { // いなければ、自分目線で一番村人っぽい人
				target = max(candidate, getSubjectiveEstimate().getVillagerTeamLikeness(), true);
			}
			return target;
		}
	}

}
