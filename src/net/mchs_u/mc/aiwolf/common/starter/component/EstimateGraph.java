package net.mchs_u.mc.aiwolf.common.starter.component;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.aiwolf.common.data.Agent;

public class EstimateGraph extends JPanel{
	private static final long serialVersionUID = 1L;
	List<AgentGraph> agentGraphs = null;

	public EstimateGraph() {
		GridLayout l = new GridLayout(3,11);
		l.setHgap(10);
		l.setVgap(5);
		this.setLayout(l);
		
		agentGraphs = new ArrayList<>();
		for(int i = 0; i < 15; i++)
			agentGraphs.add(new AgentGraph());
		
		this.add(new JPanel());
		this.add(agentGraphs.get(0));
		this.add(agentGraphs.get(1));
		this.add(agentGraphs.get(2));
		this.add(agentGraphs.get(3));
		this.add(agentGraphs.get(4));
		this.add(agentGraphs.get(5));
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(agentGraphs.get(14));
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(new JPanel());
		this.add(agentGraphs.get(6));
		this.add(new JPanel());
		this.add(agentGraphs.get(13));
		this.add(agentGraphs.get(12));
		this.add(agentGraphs.get(11));
		this.add(agentGraphs.get(10));
		this.add(agentGraphs.get(9));
		this.add(agentGraphs.get(8));
		this.add(agentGraphs.get(7));
		this.add(new JPanel());
	}
	
	public void refreshWerewolfLikeness(Map<Agent, Double> likeness){
		for(Agent a: likeness.keySet()) {
			agentGraphs.get(a.getAgentIdx() - 1).setRateWerewolf(likeness.get(a));
		}
	}

	public void refreshVillagerTeamLikeness(Map<Agent, Double> likeness){
		for(Agent a: likeness.keySet()) {
			agentGraphs.get(a.getAgentIdx() - 1).setRateVillagerTeam(likeness.get(a));
		}
	}

}
