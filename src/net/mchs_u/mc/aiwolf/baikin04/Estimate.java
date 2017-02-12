package net.mchs_u.mc.aiwolf.baikin04;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aiwolf.client.lib.Content;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameSetting;

public class Estimate {
	private Map<String,Double> rates = null;
	
	private List<Agent> agents = null;
	
	private Probabilities probs = null;
	private Set<Agent> coSeerSet = null; 
	private Set<Agent> coMediumSet = null;
	private Set<Agent> coBodyguardSet = null;
	private Set<Agent> coSet = null;
	
	private Map<Agent, Double> werewolfLikeness = null;
	private Map<Agent, Double> villagerTeamLikeness = null;
	private Map<Agent, Agent> todaysVotePlanMap = null;
	
	public Estimate(List<Agent> agents, GameSetting gameSetting) {
		this.agents = agents;
		
		rates = new HashMap<>();

		rates.put("VOTE_POSSESSED_TO_WEREWOLF"         , 0.900d);
		rates.put("VOTE_WEREWOLF_TO_POSSESSED"         , 0.900d);
		rates.put("VOTE_WEREWOLF_TO_WEREWOLF"          , 0.900d);
		rates.put("FALSE_INQUESTED_FROM_VILLAGER_TEAM" , 0.010d);
		rates.put("FALSE_DIVINED_FROM_VILLAGER_TEAM"   , 0.010d);
		rates.put("BLACK_DIVINED_POSSESSED_TO_WEREWOLF", 0.900d);
		rates.put("BLACK_DIVINED_WEREWOLF_TO_POSSESSED", 0.500d);
		rates.put("BLACK_DIVINED_WEREWOLF_TO_WEREWOLF" , 0.100d);
		rates.put("2_SEER_CO_FROM_VILLGER_TEAM"        , 0.001d);
		rates.put("2_MEDIUM_CO_FROM_VILLAGER_TEAM"     , 0.001d);
		rates.put("2_BODYGUARD_CO_FROM_VILLAGER_TEAM"  , 0.001d);
		rates.put("ONLY_SEER_CO_FROM_WEREWOLF_TEAM"    , 0.010d);
		rates.put("ONLY_MEDIUM_CO_FROM_WEREWOLF_TEAM"  , 0.010d);
		rates.put("TEAM_MEMBER_WOLF"                   , 0.500d);
				
		coSeerSet = new HashSet<>();
		coMediumSet = new HashSet<>();
		coBodyguardSet = new HashSet<>();
		coSet = new HashSet<>();
		
		werewolfLikeness = new HashMap<>();
		villagerTeamLikeness = new HashMap<>();

		probs = new Probabilities(agents, gameSetting);
	}
	
	public void dayStart(){
		todaysVotePlanMap = new HashMap<>();
	}
	
	public Map<Agent, Double> getWerewolfLikeness() {
		if(probs.isUpdated())
			calcLikeness();
		return werewolfLikeness;
	}

	public Map<Agent, Double> getVillagerTeamLikeness() {
		if(probs.isUpdated())
			calcLikeness();
		return villagerTeamLikeness;
	}

	//らしさを再計算
	private void calcLikeness(){
		for(Agent a: agents){
			werewolfLikeness.put(a, 0d);
			villagerTeamLikeness.put(a, 0d);
		}
		
		double sum = 0;
		for(RoleCombination rc: probs.getRoleCombinations()){
			double d = probs.getProbability(rc);
			sum += d;
			for(Agent a: agents){
				if(rc.isWolf(a)){
					werewolfLikeness.put(a, werewolfLikeness.get(a) + d);
				}else if(!rc.isPossessed(a)){
					villagerTeamLikeness.put(a, villagerTeamLikeness.get(a) + d);
				}
			}
		}
		
		for(Agent a: agents){
			werewolfLikeness.put(a, werewolfLikeness.get(a) / sum);
			villagerTeamLikeness.put(a, villagerTeamLikeness.get(a) / sum);
		}
		probs.resetUpdated();
	}
	
	//終了条件を満たしているパターン(狼が全滅してるのにゲームが終わってないなど)を削除
	public void updateAliveAgentList(List<Agent> aliveAgents){		
		Set<RoleCombination> reserveRemove = new HashSet<>();
		for(RoleCombination rc: probs.getRoleCombinations()){
			int countWerewolf = 0;
			for(Agent a: aliveAgents){
				if(rc.isWolf(a))
					countWerewolf++;
			}
			//狼が全滅
			if(countWerewolf == 0)
				reserveRemove.add(rc);
			//狼が人間と同数以上
			else if(countWerewolf >= aliveAgents.size() - countWerewolf)
				reserveRemove.add(rc);
		}
		for(RoleCombination rr:reserveRemove)
			probs.remove(rr);
	}
	
	//確定した役職（自分の役職、仲間の狼など）以外の確率をゼロにする
	public void updateDefinedRole(Agent agent, Role role){
		Set<RoleCombination> reserveRemove = new HashSet<>();
		for(RoleCombination rc: probs.getRoleCombinations()){
			if(role == Role.POSSESSED){
				if(!rc.isPossessed(agent))
					reserveRemove.add(rc);
			} else if(role == Role.WEREWOLF) {
				if(!rc.isWolf(agent))
					reserveRemove.add(rc);
			} else {
				if(!rc.isVillagerTeam(agent))
					reserveRemove.add(rc);
			}
		}
		for(RoleCombination rr:reserveRemove)
			probs.remove(rr);
	}
	
	//確定した人種（自分目線の占い結果など）以外の確率をゼロにする
	public void updateDefinedSpecies(Agent agent, Species species){
		Set<RoleCombination> reserveRemove = new HashSet<>();
		for(RoleCombination rc: probs.getRoleCombinations()){
			if(species == Species.WEREWOLF){
				if(!rc.isWolf(agent))
					reserveRemove.add(rc);
			} else {
				if(rc.isWolf(agent))
					reserveRemove.add(rc);
			}
		}
		for(RoleCombination rr:reserveRemove)
			probs.remove(rr);
	}
	
	//仲間の狼の確率を下げる（身内切りのためゼロにはしない）（村人目線のときにつかう）
	public void updateTeamMemberWolf(List<Agent> agents){
		for(RoleCombination rc: probs.getRoleCombinations()){
			for(Agent a: agents){
				if(rc.isWolf(a)){
					probs.update(rc, rates.get("TEAM_MEMBER_WOLF"));
					break;
				}
			}
		}
	}
	
	public void updateVoteList(List<Vote> voteList){
		for(Vote v: voteList){
			for(RoleCombination rc: probs.getRoleCombinations()){
				// 狂人から人狼への投票
				if(rc.isPossessed(v.getAgent()) && rc.isWolf(v.getTarget()))
					probs.update(rc, rates.get("VOTE_POSSESSED_TO_WEREWOLF"));
				// 人狼から狂人への投票
				else if(rc.isWolf(v.getAgent()) && rc.isPossessed(v.getTarget()))
					probs.update(rc, rates.get("VOTE_WEREWOLF_TO_POSSESSED"));
				// 人狼から人狼への投票
				else if(rc.isWolf(v.getAgent()) && rc.isWolf(v.getTarget()))
					probs.update(rc, rates.get("VOTE_WEREWOLF_TO_WEREWOLF"));
			}
		}
	}	
	
	public void updateAttackedAgent(Agent agent){
		if(agent == null)
			return;
		
		Set<RoleCombination> reserveRemove = new HashSet<>();
		
		for(RoleCombination rc: probs.getRoleCombinations()){
			//人狼が襲撃される
			if(rc.getWolfs().contains(agent)){
				reserveRemove.add(rc);
			}
		}
		
		for(RoleCombination rr:reserveRemove)
			probs.remove(rr);
	}
	
	public void updateTalk(Talk talk){
		Content content = new Content(talk.getText());
		
		switch (content.getTopic()) {
		case COMINGOUT:
			if(!talk.getAgent().equals(content.getTarget()))//自分自身のCOじゃない場合は無視
				break;
			if(coSet.contains(talk.getAgent())) //同じAgentの2回目以降のCOは無視
				break;
			coSet.add(talk.getAgent());
			
			if(content.getRole() == Role.BODYGUARD){
				coBodyguardSet.add(talk.getAgent());
				for(RoleCombination rc: probs.getRoleCombinations()){
					if(rc.isVillagerTeam(talk.getAgent())){
						// 村人陣営から二人目の狩人CO
						if(countVillagerTeam(rc, coBodyguardSet) == 2)
							probs.update(rc, rates.get("2_BODYGUARD_CO_FROM_VILLAGER_TEAM"));
					}
				}
			}else if(content.getRole() == Role.SEER){
				coSeerSet.add(talk.getAgent());
				for(RoleCombination rc: probs.getRoleCombinations()){
					if(rc.isVillagerTeam(talk.getAgent())){
						// 村人陣営から二人目の占いCO
						if(countVillagerTeam(rc, coSeerSet) == 2)
							probs.update(rc, rates.get("2_SEER_CO_FROM_VILLGER_TEAM"));
						// 既に人狼陣営が占いCOしている状態での初めての村人陣営占いCO(①を解除)
						if(countWereWolfTeam(rc, coSeerSet) > 0 && countVillagerTeam(rc, coSeerSet) == 1)
							probs.restore(rc, rates.get("ONLY_SEER_CO_FROM_WEREWOLF_TEAM"));
					}else{
						// 村人陣営が占いCOしていない状態で初めての人狼陣営占いCO(①)
						if(countVillagerTeam(rc, coSeerSet) < 1 && countWereWolfTeam(rc, coSeerSet) == 1)
							probs.update(rc, rates.get("ONLY_SEER_CO_FROM_WEREWOLF_TEAM"));
					}
				}
			}else if(content.getRole() == Role.MEDIUM){
				coMediumSet.add(talk.getAgent());
				for(RoleCombination rc: probs.getRoleCombinations()){
					if(rc.isVillagerTeam(talk.getAgent())){
						// 村人陣営から二人目の霊能CO
						if(countVillagerTeam(rc, coMediumSet) == 2)
							probs.update(rc, rates.get("2_MEDIUM_CO_FROM_VILLAGER_TEAM"));
						// 既に人狼陣営が霊能COしている状態での初めての村人陣営霊能CO(②を解除)
						if(countWereWolfTeam(rc, coMediumSet) > 0 && countVillagerTeam(rc, coMediumSet) == 1)
							probs.restore(rc, rates.get("ONLY_MEDIUM_CO_FROM_WEREWOLF_TEAM"));
					}else{
						// 村人陣営が霊能COしていない状態で初めての人狼陣営霊能CO(②)
						if(countVillagerTeam(rc, coMediumSet) < 1 && countWereWolfTeam(rc, coMediumSet) == 1)
							probs.update(rc, rates.get("ONLY_MEDIUM_CO_FROM_WEREWOLF_TEAM"));
					}
				}
			}

			break;
		case DIVINED:
			for(RoleCombination rc: probs.getRoleCombinations()){
				//狂人が人狼に黒出し
				if(rc.isPossessed(talk.getAgent()) && rc.isWolf(content.getTarget()))
					probs.update(rc, rates.get("BLACK_DIVINED_POSSESSED_TO_WEREWOLF"));
				//人狼が狂人に黒出し
				else if(rc.isWolf(talk.getAgent()) && rc.isPossessed(content.getTarget()))
					probs.update(rc, rates.get("BLACK_DIVINED_WEREWOLF_TO_POSSESSED"));
				//人狼が人狼に黒出し
				else if(rc.isWolf(talk.getAgent()) && rc.isWolf(content.getTarget()))
					probs.update(rc, rates.get("BLACK_DIVINED_WEREWOLF_TO_WEREWOLF"));	
				//村人陣営が嘘の占い
				else if(rc.isVillagerTeam(talk.getAgent())){
					if(rc.isWolf(content.getTarget()) && content.getResult() == Species.HUMAN){
						probs.update(rc, rates.get("FALSE_DIVINED_FROM_VILLAGER_TEAM"));
					}else if(!rc.isWolf(content.getTarget()) && content.getResult() == Species.WEREWOLF){
						probs.update(rc, rates.get("FALSE_DIVINED_FROM_VILLAGER_TEAM"));
					}
				}
			}			
			break;
		case IDENTIFIED:
			for(RoleCombination rc: probs.getRoleCombinations()){
				//村人陣営が嘘の霊能
				if(rc.isVillagerTeam(talk.getAgent())){
					if(rc.isWolf(content.getTarget()) && content.getResult() == Species.HUMAN){
						probs.update(rc, rates.get("FALSE_INQUESTED_FROM_VILLAGER_TEAM"));
					}else if(!rc.isWolf(content.getTarget()) && content.getResult() == Species.WEREWOLF){
						probs.update(rc, rates.get("FALSE_INQUESTED_FROM_VILLAGER_TEAM"));
					}
				}
			}
			break;
		case VOTE:
			todaysVotePlanMap.put(talk.getAgent(), content.getTarget());
			break;
		default:
			break;
		}
			
	}
	
	private static int countVillagerTeam(RoleCombination rc, Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(rc.isVillagerTeam(a))
				count++;
		}
		return count;
	}
	
	private static int countWereWolfTeam(RoleCombination rc, Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(!rc.isVillagerTeam(a))
				count++;
		}
		return count;
	}
	
	public void print(){
		Map<Agent, Double> w = getWerewolfLikeness();
		Map<Agent, Double> v = getVillagerTeamLikeness();
		for(Agent a: agents){
			System.out.print("[" + a.getAgentIdx() + "]\t");
			System.out.printf("%.4f\t",w.get(a));
			System.out.printf("%.4f\t",v.get(a));
			System.out.printf("%.4f\n",1d - v.get(a) - w.get(a));
		}
	}

	public Set<Agent> getCoSeerSet() {
		return coSeerSet;
	}

	public Set<Agent> getCoMediumSet() {
		return coMediumSet;
	}

	public Set<Agent> getCoBodyguardSet() {
		return coBodyguardSet;
	}
	
	public List<Agent> getMostVotePlanedAgents(){
		List<Agent> ret = new ArrayList<>();
		Map<Agent, Integer> count = new HashMap<>();
		for(Agent a: todaysVotePlanMap.values()){
			if(!count.containsKey(a)){
				count.put(a, 1);
			} else {
				count.put(a, count.get(a) + 1);
			}
		}
		int max = -1;
		for(int x: count.values()){
			if(max < x){
				max = x;
			}
		}
		for(Agent a: count.keySet()){
			if(count.get(a) == max){
				ret.add(a);
			}
		}
		return ret;
	}

	
}
