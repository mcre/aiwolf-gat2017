package net.mchs_u.mc.aiwolf.curry.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

import net.mchs_u.mc.aiwolf.common.EstimatePlayer;
import net.mchs_u.mc.aiwolf.curry.Estimate;

public abstract class AbstractMcreRole implements EstimatePlayer {	
	private static final String NAME = "m_cre";
	private GameInfo gameInfo = null;
	
	private int talkListHead = 0;
	
	private Estimate subjectiveEstimate = null; //主観
	private Estimate objectiveEstimate = null; //客観
	private Estimate pretendVillagerEstimate = null; //村人目線
	
	private List<Double> random = null; //min, maxを選ぶ際に、同率のうちどれを優先にするかをゲームごとに乱数で決める。
	
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		this.gameInfo = gameInfo;
		
		List<Agent> agents = gameInfo.getAgentList();

		subjectiveEstimate      = new Estimate(agents, gameSetting);
		objectiveEstimate       = new Estimate(agents, gameSetting);
		pretendVillagerEstimate = new Estimate(agents, gameSetting);
		
		subjectiveEstimate.updateDefinedRole(getGameInfo().getAgent(), getGameInfo().getRole());
		pretendVillagerEstimate.updateDefinedRole(getGameInfo().getAgent(), Role.VILLAGER);
				
		random = new ArrayList<>();
		for(int i = 0; i < gameSetting.getPlayerNum(); i++){
			random.add(Math.random() / 100000d);
		}			
	}
	
	public void update(GameInfo gameInfo) {	
		this.gameInfo = gameInfo;
		
		List<Talk> talkList = gameInfo.getTalkList();
		for(int i = talkListHead; i < talkList.size(); i++){
			subjectiveEstimate.updateTalk(talkList.get(i));
			objectiveEstimate.updateTalk(talkList.get(i));
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
		
		subjectiveEstimate.dayStart(gameInfo);
		objectiveEstimate.dayStart(gameInfo);
		pretendVillagerEstimate.dayStart(gameInfo);
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
	protected List<Agent> removeDeadAgent(Collection<Agent> agents) {
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
	
	public Estimate getSubjectiveEstimate() {
		return subjectiveEstimate;
	}
	
	public Estimate getObjectiveEstimate() {
		return objectiveEstimate;
	}
	
	public Estimate getPretendVillagerEstimate() {
		return pretendVillagerEstimate;
	}
	
}
