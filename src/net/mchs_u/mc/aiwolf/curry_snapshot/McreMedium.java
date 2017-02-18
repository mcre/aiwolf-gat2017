package net.mchs_u.mc.aiwolf.curry_snapshot;

import org.aiwolf.client.lib.ComingoutContentBuilder;
import org.aiwolf.client.lib.Content;
import org.aiwolf.client.lib.IdentContentBuilder;
import org.aiwolf.common.data.Judge;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class McreMedium extends McreVillager {	
	private boolean co = false;
	private boolean inquestedToday = false;

	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		super.initialize(gameInfo, gameSetting);

		co = false;
	}
	
	@Override
	public void dayStart() {
		super.dayStart();
		inquestedToday = false;
	}
	
	@Override
	public String talk() {
		//COしてない場合はCO
		if(!co){
			co = true;
			return new Content(new ComingoutContentBuilder(getGameInfo().getAgent(), Role.MEDIUM)).getText();
		}
		
		//今日霊能結果を言ってなくて霊能結果があれば霊能結果を言う
		Judge j = getGameInfo().getMediumResult();
		if(!inquestedToday && j != null){
			inquestedToday = true;
			getSubjectiveEstimate().updateDefinedSpecies(j.getTarget(), j.getResult());//自分目線に霊能情報を更新
			return new Content(new IdentContentBuilder(j.getTarget(), j.getResult())).getText();
		}
		
		//村人と同じtalk
		return super.talk();
	}
}
