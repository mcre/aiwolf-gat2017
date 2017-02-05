package net.mchs_u.mc.aiwolf.starter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mchs_u.mc.aiwolf.starter.component.PlayerDefinition;

public class MainAllSample {	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
		int set = 600;
		int times = 100;
		int playerNum = 15;
		boolean isVisualize = false;
		boolean isLog = false;
		boolean isSaveResult = true;
		
		List<PlayerDefinition> playerDefinitions = new ArrayList<>();
		Class<?> samplePlayerClass = Class.forName("org.aiwolf.sample.player.SampleRoleAssignPlayer");
		for(int i = 0; i < playerNum; i++){
			playerDefinitions.add(new PlayerDefinition(samplePlayerClass, String.format("smpl%02d", i), null));
		}
		
		DirectStarter ds = new DirectStarter(playerDefinitions, times, set, isVisualize, isLog, isSaveResult);
		ds.start();
	}
}
