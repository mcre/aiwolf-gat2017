package net.mchs_u.mc.aiwolf.curry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aiwolf.client.lib.AttackContentBuilder;
import org.aiwolf.client.lib.ComingoutContentBuilder;
import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.VoteContentBuilder;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class McreWerewolf extends AbstractMcreRole {	
	protected Agent declaredVoteTarget = null; //今日最後に投票宣言をした対象
	protected Agent declaredAttackTarget = null; //今日最後に襲撃宣言をした対象
	
	private int wisperTurn; // 当日の囁きターン

	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		super.initialize(gameInfo, gameSetting);

		for(Agent a: getWolfList()){
			getSubjectiveEstimate().updateDefinedRole(a, Role.WEREWOLF); 
			getPretendVillagerEstimate().updateTeamMemberWolf(getWolfList());
		}
		
	}	

	@Override
	public void dayStart() {
		super.dayStart();
		declaredVoteTarget = null;
		declaredAttackTarget = null;
		
		wisperTurn = -1;
	}
	
	@Override
	public String whisper() {
		wisperTurn++;
		
		if(getGameInfo().getDay() == 0) {
			if(wisperTurn == 0)
				// 騙らない宣言
				return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), Role.VILLAGER)).getText();
			else
				return Talk.OVER;
		}
		
		Agent target = decideAttackTarget();
		if(target == null)
			return Talk.SKIP;
		if(declaredAttackTarget != null && target.equals(declaredAttackTarget))
			return Talk.SKIP;
		declaredAttackTarget = target;
		return new Content(new AttackContentBuilder(target)).getText();
	}
	
	@Override
	public String talk() {
		Agent target = decideVoteTarget();
		if(target == null)
			return Talk.SKIP;
		if(declaredVoteTarget != null && target.equals(declaredVoteTarget))
			return Talk.SKIP;
		declaredVoteTarget = target;
		return new Content(new VoteContentBuilder(target)).getText();
	}

	@Override
	public Agent attack() {
		return decideAttackTarget();
	}
	
	@Override
	public Agent vote() {
		return decideVoteTarget();
	}
	
	protected List<Agent> getWolfList(){
		List<Agent> wolfList = new ArrayList<Agent>();

		Map<Agent, Role> wolfMap = getGameInfo().getRoleMap();
		for(Entry<Agent, Role> set: wolfMap.entrySet()){
			if(set.getValue() == Role.WEREWOLF){
				wolfList.add(set.getKey());
			}
		}
		return wolfList;
	}
	
	//襲撃対象を決める
	protected Agent decideAttackTarget(){
		//客観的に、人狼らしくない人
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		for(Agent a: getWolfList()){
			candidate.remove(a);
		}
		return min(candidate, getObjectiveEstimate().getWerewolfLikeness(), false);	
	}
	
	
	//投票対象を決める
	protected Agent decideVoteTarget(){
		//村人目線で、生存者のうち最も人狼っぽいひと
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		
		return max(candidate, getPretendVillagerEstimate().getWerewolfLikeness(), false);
	}

}
