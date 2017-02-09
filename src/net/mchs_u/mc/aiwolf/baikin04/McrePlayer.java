package net.mchs_u.mc.aiwolf.baikin04;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

import net.mchs_u.mc.aiwolf.baikin04.role.McreBodyguard;
import net.mchs_u.mc.aiwolf.baikin04.role.McreMedium;
import net.mchs_u.mc.aiwolf.baikin04.role.McrePossessed;
import net.mchs_u.mc.aiwolf.baikin04.role.McreSeer;
import net.mchs_u.mc.aiwolf.baikin04.role.McreVillager;
import net.mchs_u.mc.aiwolf.baikin04.role.McreWerewolf;

public class McrePlayer implements Player {
	private Player rolePlayer;
	
	public String getName() {
		return rolePlayer.getName();
	}

	public final void update(GameInfo gameInfo) {
		rolePlayer.update(gameInfo);
	}

	public final void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		Role myRole = gameInfo.getRole();
		switch (myRole) {
		case VILLAGER:
			rolePlayer = new McreVillager();
			break;
		case SEER:
			rolePlayer = new McreSeer();
			break;
		case MEDIUM:
			rolePlayer = new McreMedium();
			break;
		case BODYGUARD:
			rolePlayer = new McreBodyguard();
			break;
		case POSSESSED:
			rolePlayer = new McrePossessed();
			break;
		case WEREWOLF:
			rolePlayer = new McreWerewolf();
			break;
		default:
			rolePlayer = new McreVillager();
			break;
		}
		rolePlayer.initialize(gameInfo, gameSetting);
	}

	public final void dayStart() {
		rolePlayer.dayStart();
	}

	public final String talk() {
		return rolePlayer.talk();
	}

	public final String whisper() {
		return rolePlayer.whisper();
	}

	public final Agent vote() {
		return rolePlayer.vote();
	}

	public final Agent attack() {
		return rolePlayer.attack();
	}

	public final Agent divine() {
		return rolePlayer.divine();
	}

	public final Agent guard() {
		return rolePlayer.guard();
	}

	public final void finish() {
		rolePlayer.finish();
	}
}
