/**
 * 
 */
package net.mchs_u.mc.aiwolf.common.starter.component;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

/**
 * @author m_cre
 * DirectStarterで無理やりプレイヤー名を変えるためのラッパークラス
 */
public class RenamedPlayer implements Player {
	private Player player;
	private String name;
	
	public RenamedPlayer(Player player, String name) {
		this.player = player;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void update(GameInfo gameInfo) {
		player.update(gameInfo);
	}
	
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		player.initialize(gameInfo, gameSetting);
	}
	
	public void dayStart() {
		player.dayStart();
	}

	public String talk() {
		return player.talk();
	}

	public String whisper() {
		return player.whisper();
	}

	public Agent vote() {
		return player.vote();
	}

	public Agent attack() {
		return player.attack();
	}

	public Agent divine() {
		return player.divine();
	}

	public Agent guard() {
		return player.guard();
	}

	public void finish() {
		player.finish();
	}

}
