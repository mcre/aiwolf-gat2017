package net.mchs_u.mc.aiwolf.curry_snapshot;

import java.util.ArrayList;
import java.util.List;

import org.aiwolf.client.lib.ComingoutContentBuilder;
import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.DivinedResultContentBuilder;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class McreSeer extends McreVillager {
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
	}
	
	//0日目CO
	@Override
	public String talk() {
		//COしてない場合はCO
		if(!co){
			co = true;
			return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), Role.SEER)).getText();
		}
		
		//占い結果を言ってなければ占い
		Judge j = getGameInfo().getDivineResult();
		if(!divinedToday && j != null){
			divinedToday = true;
			divinedList.add(j.getTarget());
			getSubjectiveEstimate().updateDefinedSpecies(j.getTarget(), j.getResult());//自分目線に占い情報を更新
			return new Content(new DivinedResultContentBuilder(j.getTarget(), j.getResult())).getText();
		}
		
		//村人と同じtalk
		return super.talk();
	}
	
	//占っていない生存者のうち自分目線で最も人狼っぽい人を占う。占い師COした人はあとまわし。
	@Override
	public Agent divine(){
		List<Agent> candidate = new ArrayList<>(getGameInfo().getAliveAgentList());
		candidate.remove(getGameInfo().getAgent());
		for(Agent a:divinedList){
			candidate.remove(a);
		}
		
		List<Agent> tmp = candidate;

		//占い師COした人は除く
		for(Agent a: getSubjectiveEstimate().getCoSeerSet()){
			candidate.remove(a);
		}
		if(candidate.size() == 0)
			//誰も占う人が居ない場合のみ占い師COの人を占う
			return max(tmp, getSubjectiveEstimate().getWerewolfLikeness(), false);
		else
			return max(candidate, getSubjectiveEstimate().getWerewolfLikeness(), false);
	}	

}
