package net.mchs_u.mc.aiwolf.curry;

import java.util.HashSet;
import java.util.Set;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameSetting;

public class RoleCombination {
	private Set<Agent> wolfs = null;
	private Set<Agent> possesseds = null;
	
	public RoleCombination(Set<Agent> wolfs, Set<Agent> possesseds) {
		this.wolfs = wolfs;
		this.possesseds = possesseds;
	}
	
	public boolean isValid(int playerNum, GameSetting gameSetting) {
		int w = gameSetting.getRoleNum(Role.WEREWOLF);
		int p = gameSetting.getRoleNum(Role.POSSESSED);
		
		if(wolfs.size() != w)
			return false;
		if(possesseds.size() != p)
			return false;
		
		Set<Agent> wp = new HashSet<>();
		wp.addAll(wolfs);
		wp.addAll(possesseds);
		if(wp.size() != w + p)
			return false;
		
		return true;
	}
	
	public Set<Agent> getWolfs() {
		return wolfs;
	}

	public Set<Agent> getPossesseds() {
		return possesseds;
	}
	
	public boolean isWolf(Agent agent) {
		return wolfs.contains(agent);
	}
	
	public boolean isPossessed(Agent agent) {
		return possesseds.contains(agent);
	}
	
	public boolean contains(Agent agent) {
		return wolfs.contains(agent) || possesseds.contains(agent);
	}
	
	public boolean isVillagerTeam(Agent agent){
		return !isWolf(agent) && !isPossessed(agent);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((possesseds == null) ? 0 : possesseds.hashCode());
		result = prime * result + ((wolfs == null) ? 0 : wolfs.hashCode());
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
		if (wolfs == null) {
			if (other.wolfs != null)
				return false;
		} else if (!wolfs.equals(other.wolfs))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoleCombination [wolfs=" + wolfs + ", possesseds=" + possesseds + "]";
	}
	
}
