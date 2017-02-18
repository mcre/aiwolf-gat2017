package net.mchs_u.mc.aiwolf.curry;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

import net.mchs_u.mc.aiwolf.common.AbstractEstimate;
import net.mchs_u.mc.aiwolf.common.EstimatePlayer;

public class McrePlayer implements EstimatePlayer {
	private EstimatePlayer player;
	
	public String getName() {
		return player.getName();
	}

	public final void update(GameInfo gameInfo) {
		player.update(gameInfo);
	}

	public final void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		Role myRole = gameInfo.getRole();
		switch (myRole) {
		case VILLAGER:
			player = new McreVillager();
			break;
		case SEER:
			player = new McreSeer();
			break;
		case MEDIUM:
			player = new McreMedium();
			break;
		case BODYGUARD:
			player = new McreBodyguard();
			break;
		case POSSESSED:
			player = new McrePossessed();
			break;
		case WEREWOLF:
			player = new McreWerewolf();
			break;
		default:
			player = new McreVillager();
			break;
		}
		player.initialize(gameInfo, gameSetting);
	}

	public final void dayStart() {
		player.dayStart();
	}

	public final String talk() {
		return player.talk();
	}

	public final String whisper() {
		return player.whisper();
	}

	public final Agent vote() {
		return player.vote();
	}

	public final Agent attack() {
		return player.attack();
	}

	public final Agent divine() {
		return player.divine();
	}

	public final Agent guard() {
		return player.guard();
	}

	public final void finish() {
		player.finish();
	}

	public AbstractEstimate getObjectiveEstimate() {
		return player.getObjectiveEstimate();
	}

	public AbstractEstimate getSubjectiveEstimate() {
		return player.getSubjectiveEstimate();
	}

	public AbstractEstimate getPretendVillagerEstimate() {
		return player.getPretendVillagerEstimate();
	}
}
