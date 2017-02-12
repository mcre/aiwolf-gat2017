package net.mchs_u.mc.aiwolf.baikin04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.net.GameSetting;

public class Probabilities {
	private boolean updated = false;
	private Map<RoleCombination, Double> probs = null;

	public Probabilities(List<Agent> agents, GameSetting gameSetting) {
		updated = true;
		probs = new HashMap<>();
		
		int w = gameSetting.getRoleNum(Role.WEREWOLF);
		int p = gameSetting.getRoleNum(Role.POSSESSED);
		for(List<Integer> ids: makeLoops(w + p, agents.size())){
			Set<Agent> wolfs = new HashSet<>();
			for(int i = 0; i < w; i++)
				wolfs.add(agents.get(ids.get(i)));
			
			Set<Agent> possesseds = new HashSet<>();
			for(int i = w; i < w + p; i++)
				possesseds.add(agents.get(ids.get(i)));
			
			RoleCombination rc = new RoleCombination(wolfs, possesseds);
			if(rc.isValid(agents.size(), gameSetting))
				probs.put(rc, 1d);
		}

		System.out.println("probs: " + probs.size());
	}
	
	private List<List<Integer>> makeLoops(int nFold, int num) {
		return makeLoops(new ArrayList<>(), nFold, num);
	}
		
	public static List<List<Integer>> makeLoops(List<Integer> list, int nFold, int num) {
		if(nFold <= 0) {
			List<List<Integer>> ans = new ArrayList<>();
			ans.add(list);
			return ans;
		}
		
		List<List<Integer>> loops = new ArrayList<>();
		for(int i = 0; i < num; i++) {
			List<Integer> lsub = new ArrayList<>(list);
			lsub.add(i);
			loops.addAll(makeLoops(lsub, nFold - 1, num));
		}
		return loops;
	}	

	public Set<RoleCombination> getRoleCombinations() {
		return probs.keySet();
	}
	
	public void update(RoleCombination rc, double rate) {
		probs.put(rc, probs.get(rc) * rate);
		updated = true;
	}
	
	public void restore(RoleCombination rc, double rate) {
		probs.put(rc, probs.get(rc) / rate);
		updated = true;
	}
	
	public void remove(RoleCombination rc) {
		probs.remove(rc);
		updated = true;
	}

	public boolean isUpdated() {
		return updated;
	}
	
	public void resetUpdated() {
		updated = false;
	}
	
	public double getProbability(RoleCombination roleCombination){
		return probs.get(roleCombination);
	}

}
