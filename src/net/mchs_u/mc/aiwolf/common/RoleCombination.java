package net.mchs_u.mc.aiwolf.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameSetting;

public class RoleCombination {
	private Set<Agent> werewolves = null;
	private Set<Agent> possesseds = null;
	
	public RoleCombination(Set<Agent> wolves, Set<Agent> possesseds) {
		this.werewolves = wolves;
		this.possesseds = possesseds;
	}
	
	public boolean isValid(int playerNum, GameSetting gameSetting) {
		int w = gameSetting.getRoleNum(Role.WEREWOLF);
		int p = gameSetting.getRoleNum(Role.POSSESSED);
		
		if(werewolves.size() != w)
			return false;
		if(possesseds.size() != p)
			return false;
		
		Set<Agent> wp = new HashSet<>();
		wp.addAll(werewolves);
		wp.addAll(possesseds);
		if(wp.size() != w + p)
			return false;
		
		return true;
	}
	
	public Set<Agent> getWerewolves() {
		return werewolves;
	}

	public Set<Agent> getPossesseds() {
		return possesseds;
	}
	
	public boolean isWerewolf(Agent agent) {
		return werewolves.contains(agent);
	}
	
	public boolean isPossessed(Agent agent) {
		return possesseds.contains(agent);
	}
	
	public boolean isVillagerTeam(Agent agent){
		return !isWerewolf(agent) && !isPossessed(agent);
	}

	public int countVillagerTeam(Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(isVillagerTeam(a))
				count++;
		}
		return count;
	}
	
	public int countWerewolfTeam(Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(!isVillagerTeam(a))
				count++;
		}
		return count;
	}

	public int countWerewolves(Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(isWerewolf(a))
				count++;
		}
		return count;
	}
	
	public int countPossesseds(Collection<Agent> collection){
		int count = 0;
		for(Agent a: collection){
			if(isPossessed(a))
				count++;
		}
		return count;
	}
	
	public boolean contains(Agent agent) {
		return werewolves.contains(agent) || possesseds.contains(agent);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((possesseds == null) ? 0 : possesseds.hashCode());
		result = prime * result + ((werewolves == null) ? 0 : werewolves.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleCombination other = (RoleCombination) obj;
		if (possesseds == null) {
			if (other.possesseds != null)
				return false;
		} else if (!possesseds.equals(other.possesseds))
			return false;
		if (werewolves == null) {
			if (other.werewolves != null)
				return false;
		} else if (!werewolves.equals(other.werewolves))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoleCombination [werewolves=" + werewolves + ", possesseds=" + possesseds + "]";
	}
	
}
