package net.mchs_u.mc.aiwolf.baikin04.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

import net.mchs_u.mc.aiwolf.baikin04.Estimate;

public abstract class AbstractMcreRole implements Player {	
	private static final String NAME = "m_cre";
	private GameInfo gameInfo = null;
	
	protected int talkListHead = 0;
	protected Estimate objectiveEstimate = null; //客観
	protected Estimate subjectiveEstimate = null; //主観
	protected Estimate pretendVillagerEstimate = null; //村人目線
	
	private List<Double> random = null; //min, maxを選ぶ際に、同率のうちどれを優先にするかをゲームごとに乱数で決める。
	
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		this.gameInfo = gameInfo;
		
		List<Agent> agents = gameInfo.getAgentList();

		objectiveEstimate       = new Estimate(agents, gameSetting);
		subjectiveEstimate      = new Estimate(agents, gameSetting);
		pretendVillagerEstimate = new Estimate(agents, gameSetting);
		
		subjectiveEstimate.updateDefinedRole(getGameInfo().getAgent(), getGameInfo().getRole());
		pretendVillagerEstimate.updateDefinedRole(getGameInfo().getAgent(), Role.VILLAGER);
				
		random = new ArrayList<>();
		for(int i = 0; i < 15; i++){
			random.add(Math.random() / 100000d);
		}			
	}
	
	public void update(GameInfo gameInfo) {	
		this.gameInfo = gameInfo;
		
		List<Talk> talkList = gameInfo.getTalkList();
		for(int i = talkListHead; i < talkList.size(); i++){
			objectiveEstimate.updateTalk(talkList.get(i));
			subjectiveEstimate.updateTalk(talkList.get(i));
			pretendVillagerEstimate.updateTalk(talkList.get(i));
			
			talkListHead++;
		}
	}

	public String getName() {
		return NAME;
	}
	
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	
	public void dayStart() {
		talkListHead = 0;
		
		objectiveEstimate.dayStart();
		objectiveEstimate.updateAliveAgentList(gameInfo.getAliveAgentList());
		objectiveEstimate.updateAttackedAgent(gameInfo.getAttackedAgent());
		objectiveEstimate.updateVoteList(gameInfo.getVoteList());
		
		subjectiveEstimate.dayStart();
		subjectiveEstimate.updateAliveAgentList(gameInfo.getAliveAgentList());
		subjectiveEstimate.updateAttackedAgent(gameInfo.getAttackedAgent());
		subjectiveEstimate.updateVoteList(gameInfo.getVoteList());

		pretendVillagerEstimate.dayStart();
		pretendVillagerEstimate.updateAliveAgentList(gameInfo.getAliveAgentList());
		pretendVillagerEstimate.updateAttackedAgent(gameInfo.getAttackedAgent());
		pretendVillagerEstimate.updateVoteList(gameInfo.getVoteList());	
	}
	
	public Agent attack() {
		return null;
	}

	public Agent divine() {
		return null;
	}

	public Agent guard() {
		return null;
	}

	public String whisper() {
		return null;
	}
	
	public void finish() {
	}
	
	protected Agent randomSelect(List<Agent> agents){
		int num = new Random().nextInt(agents.size());
		return agents.get(num);
	}
	
	protected Agent min(Collection<Agent> candidate, Map<Agent, Double> likeness, boolean plus){//村人らしさの場合true,人狼らしさの場合false
		List<Agent> candidateList = new ArrayList<Agent>(candidate); 
		
		Agent ret = null;
		double min = 2d;
		for(int i = 0; i < candidateList.size(); i++){
			Agent a = candidateList.get(i);
			double l = likeness.get(a);
			if(plus)
				l += random.get(i);
			else
				l -= random.get(i);
			
			if(min > l){
				min = l;
				ret = a;
			}
		}
		return ret;
	}
	
	protected Agent max(Collection<Agent> candidate, Map<Agent, Double> likeness, boolean plus){//村人らしさの場合true,人狼らしさの場合false
		List<Agent> candidateList = new ArrayList<Agent>(candidate); 
		
		Agent ret = null;
		double max = -1;
		for(int i = 0; i < candidateList.size(); i++){
			Agent a = candidateList.get(i);
			double l = likeness.get(a);
			if(plus)
				l += random.get(i);
			else
				l -= random.get(i);
			
			if(max < l){
				max = l;
				ret = a;
			}
		}
		return ret;
	}
	
	//しんだひとをリストから除く
	protected List<Agent> removeDeadAgent(Collection<Agent> agents){
		List<Agent> ret = new ArrayList<>();
		List<Agent> aliveList = gameInfo.getAliveAgentList();
		for(Agent a: agents){
			for(Agent al: aliveList){
				if(a.getAgentIdx() == al.getAgentIdx()){
					ret.add(a);
					break;
				}
			}
		}
		return ret;
	}
}
