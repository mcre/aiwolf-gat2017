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

	public EstimateGraph(int playerNum) {
		GridLayout l = new GridLayout(3,11);
		l.setHgap(10);
		l.setVgap(5);
		this.setLayout(l);
		
		agentGraphs = new ArrayList<>();
		for(int i = 0; i < playerNum; i++)
			agentGraphs.add(new AgentGraph());

		if(playerNum == 3) {
			a( ); a( ); a( ); a( ); a( ); a( ); a( ); a( ); a( );
			a(2); a( ); a(0); a( ); a( ); a( ); a( ); a( ); a( );
			a( ); a(1); a( ); a( ); a( ); a( ); a( ); a( ); a( );
		}
		
		if(playerNum == 4) {
			a( ); a(0); a( ); a( ); a( ); a( ); a( ); a( ); a( );
			a(3); a( ); a(1); a( ); a( ); a( ); a( ); a( ); a( );
			a( ); a(2); a( ); a( ); a( ); a( ); a( ); a( ); a( );
		}

		if(playerNum == 5) {
			a( ); a(0); a( ); a( ); a( ); a( ); a( ); a( ); a( );
			a(4); a( ); a( ); a(1); a( ); a( ); a( ); a( ); a( );
			a( ); a(3); a(2); a( ); a( ); a( ); a( ); a( ); a( );
		}
		
		if(playerNum == 6) {
			a( ); a(0); a(1); a( ); a( ); a( ); a( ); a( ); a( );
			a(5); a( ); a( ); a(2); a( ); a( ); a( ); a( ); a( );
			a( ); a(4); a(3); a( ); a( ); a( ); a( ); a( ); a( );
		}
		
		if(playerNum == 7) {
			a( ); a(0); a(1); a( ); a( ); a( ); a( ); a( ); a( );
			a(6); a( ); a( ); a( ); a(2); a( ); a( ); a( ); a( );
			a( ); a(5); a(4); a(3); a( ); a( ); a( ); a( ); a( );
		}

		if(playerNum == 8) {
			a( ); a(0); a(1); a(2); a( ); a( ); a( ); a( ); a( );
			a(7); a( ); a( ); a( ); a(3); a( ); a( ); a( ); a( );
			a( ); a(6); a(5); a(4); a( ); a( ); a( ); a( ); a( );
		}

		if(playerNum == 9) {
			a( ); a(0); a(1); a(2); a( ); a( ); a( ); a( ); a( );
			a(8); a( ); a( ); a( ); a( ); a(3); a( ); a( ); a( );
			a( ); a(7); a(6); a(5); a(4); a( ); a( ); a( ); a( );
		}

		if(playerNum == 10) {
			a( ); a(0); a(1); a(2); a(3); a( ); a( ); a( ); a( );
			a(9); a( ); a( ); a( ); a( ); a(4); a( ); a( ); a( );
			a( ); a(8); a(7); a(6); a(5); a( ); a( ); a( ); a( );
		}
		
		if(playerNum == 11) {
			a(  ); a(0); a(1); a(2); a(3); a( ); a( ); a( ); a( );
			a(10); a( ); a( ); a( ); a( ); a( ); a(4); a( ); a( );
			a(  ); a(9); a(8); a(7); a(6); a(5); a( ); a( ); a( );
		}
		
		if(playerNum == 12) {
			a(  ); a( 0); a(1); a(2); a(3); a(4); a( ); a( ); a( );
			a(11); a(  ); a( ); a( ); a( ); a( ); a(5); a( ); a( );
			a(  ); a(10); a(9); a(8); a(7); a(6); a( ); a( ); a( );
		}
		
		if(playerNum == 13) {
			a(  ); a( 0); a( 1); a(2); a(3); a(4); a( ); a( ); a( );
			a(12); a(  ); a(  ); a( ); a( ); a( ); a( ); a(5); a( );
			a(  ); a(11); a(10); a(9); a(8); a(7); a(6); a( ); a( );
		}
		
		if(playerNum == 14) {
			a(  ); a( 0); a( 1); a( 2); a(3); a(4); a(5); a( ); a( );
			a(13); a(  ); a(  ); a(  ); a( ); a( ); a( ); a(6); a( );
			a(  ); a(12); a(11); a(10); a(9); a(8); a(7); a( ); a( );
		}
		
		if(playerNum == 15) {
			a(  ); a( 0); a( 1); a( 2); a( 3); a(4); a(5); a( ); a( );
			a(14); a(  ); a(  ); a(  ); a(  ); a( ); a( ); a( ); a(6);
			a(  ); a(13); a(12); a(11); a(10); a(9); a(8); a(7); a( );
		}
		
		if(playerNum == 16) {
			a(  ); a( 0); a( 1); a( 2); a( 3); a( 4); a(5); a(6); a( );
			a(15); a(  ); a(  ); a(  ); a(  ); a(  ); a( ); a( ); a(7);
			a(  ); a(14); a(13); a(12); a(11); a(10); a(9); a(8);
		}
		
		if(playerNum == 17) {
			a(  ); a( 0); a( 1); a( 2); a( 3); a( 4); a( 5); a(6); a( );
			a(16); a(  ); a(  ); a(  ); a(  ); a(  ); a(  ); a( ); a(7);
			a(  ); a(15); a(14); a(13); a(12); a(11); a(10); a(9); a(8);
		}
		
		if(playerNum == 18) {
			a(  ); a( 0); a( 1); a( 2); a( 3); a( 4); a( 5); a(6); a(7);
			a(17); a(  ); a(  ); a(  ); a(  ); a(  ); a(  ); a( ); a(8);
			a(16); a(15); a(14); a(13); a(12); a(11); a(10); a(9); a( );
		}
		
		
	}
	
	private void a(int x){
		this.add(agentGraphs.get(x));
	}
	private void a(){
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
