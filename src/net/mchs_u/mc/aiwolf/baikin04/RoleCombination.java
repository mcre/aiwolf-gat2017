package net.mchs_u.mc.aiwolf.baikin04;

import java.util.HashSet;
import java.util.Set;

import org.aiwolf.common.data.Agent;

public class RoleCombination {
	private Set<Agent> wolfs = null;
	private Agent possessed = null;
	
	public RoleCombination(Agent wolf1, Agent wolf2, Agent wolf3, Agent possessed) {
		this.wolfs = new HashSet<Agent>(3);
		this.wolfs.add(wolf1);
		this.wolfs.add(wolf2);
		this.wolfs.add(wolf3);
		this.possessed = possessed;
	}
	
	public Set<Agent> getWolfs() {
		return wolfs;
	}

	public Agent getPossessed() {
		return possessed;
	}
	
	public boolean isWolf(Agent agent){
		return wolfs.contains(agent);
	}
	
	public boolean isPossessed(Agent agent){
		return possessed.equals(agent);
	}
	
	public boolean isVillagerTeam(Agent agent){
		return !isWolf(agent) && !isPossessed(agent);
	}
	
	//自動生成、チェックしてない
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((possessed == null) ? 0 : possessed.hashCode());
		result = prime * result + ((wolfs == null) ? 0 : wolfs.hashCode());
		return result;
	}
	
	//自動生成、チェックしてない
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleCombination other = (RoleCombination) obj;
		if (possessed != other.possessed)
			return false;
		if (wolfs == null) {
			if (other.wolfs != null)
				return false;
		} else if (!wolfs.equals(other.wolfs))
			return false;
		return true;
	}

}
