package net.mchs_u.mc.aiwolf.starter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.Counter;
import org.aiwolf.server.AIWolfGame;
import org.aiwolf.server.GameData;
import org.aiwolf.server.net.DirectConnectServer;
import org.aiwolf.server.util.FileGameLogger;
import org.aiwolf.server.util.GameLogger;
import org.aiwolf.server.util.MultiGameLogger;
import org.aiwolf.ui.GameViewer;
import org.aiwolf.ui.log.ContestResource;

import net.mchs_u.mc.aiwolf.starter.component.PlayerDefinition;

/**
 * @author m_cre
 * 基本のスターター。
 */

@SuppressWarnings("deprecation")
public class DirectStarter {	
	private static final String LOG_DIR = "./log/";
	private static final String RESULT_DIR = "./result/";
	
	private int gameNum, setNum;
	private List<PlayerDefinition> playerDefinitions;
	private boolean isVisualize, isLog, isSaveResult;
	private Map<String, Counter<Role>> winCounterMap;
	private Map<String, Counter<Role>> roleCounterMap;
	
	private AIWolfGame game;
	
	public DirectStarter(List<PlayerDefinition> playerDefinitions, int gameNum, int setNum, boolean isVisualize, boolean isLog, boolean isSaveResult) {
		this.gameNum = gameNum;
		this.setNum = setNum;
		this.isVisualize = isVisualize;
		this.playerDefinitions = playerDefinitions;
		this.isLog = isLog;
		this.isSaveResult = isSaveResult;
		
		winCounterMap = new HashMap<>();
		roleCounterMap = new HashMap<>();
	}
	
	public void start() throws IOException {
		for(int i = 0; i < setNum; i++){
			game = null;
			
			Map<Player, Role> playerRoleMap = new HashMap<>();
			for(PlayerDefinition pd: playerDefinitions) {
				Player p = pd.getNewPlayerInstance();
				playerRoleMap.put(p, pd.getRole());
			}
			
			game = new AIWolfGame(GameSetting.getDefaultGame(playerRoleMap.size()), new DirectConnectServer(playerRoleMap));
			for(int j = 0; j < gameNum; j++){
				File logFile = new File(LOG_DIR + (new Date()).getTime() + ".txt"); 
				GameLogger logger = null;
				if(isLog)
					logger = new FileGameLogger(logFile);
				if(isVisualize){
					ContestResource resource = new ContestResource(game);
					GameViewer gameViewer = new GameViewer(resource, game);
					gameViewer.setAutoClose(true);
					if(logger == null)
						logger = gameViewer;
					else
						logger = new MultiGameLogger(logger, gameViewer);
				}
				game.setGameLogger(logger);
				game.start();
				Team winner = game.getWinner();
				GameData gameData = game.getGameData();

				for(Agent agent: gameData.getAgentList()){
					String n = game.getAgentName(agent);
					String agentName = n.substring(0, Math.min(6,n.length()));
					if(!winCounterMap.containsKey(agentName)){
						winCounterMap.put(agentName, new Counter<Role>());
					}
					if(!roleCounterMap.containsKey(agentName)){
						roleCounterMap.put(agentName, new Counter<Role>());
					}

					if(winner == gameData.getRole(agent).getTeam()){
						winCounterMap.get(agentName).add(gameData.getRole(agent));
					}
					roleCounterMap.get(agentName).add(gameData.getRole(agent));
				}
			}
			result(i, isSaveResult);
		}
	}

	public void result(int set, boolean isSave) throws IOException{
		Set<Role> roleSet = new HashSet<>();
		for(Entry<String, Counter<Role>> entry: roleCounterMap.entrySet())
			roleSet.addAll(entry.getValue().keySet());
		
		StringBuffer s = new StringBuffer();

		s.append("set : " + set + "\n");
		s.append("       ");
		
		for(Role role:Role.values()){
			if(!roleSet.contains(role)) continue;
			String r = role.toString();
			s.append(String.format("\t%4s",r.substring(0, Math.min(4, r.length()))));
		}
		s.append("\tTotal\n");
		
		for(String name:new TreeSet<>(roleCounterMap.keySet())){
			s.append(String.format("%7s\t", name));
			double win = 0;
			double cnt = 0;
			for(Role role:Role.values()){
				if(!roleSet.contains(role)) continue;
				double w = winCounterMap.get(name).get(role);
				double c = roleCounterMap.get(name).get(role);
				s.append(String.format("%.4f\t", w / c ));
				win += w;
				cnt += c;
			}
			s.append(String.format("%.4f\n", win/cnt));
		}
		s.append("\n");
		
		for(String name:new TreeSet<>(roleCounterMap.keySet())){
			s.append(String.format("%7s\t",name));
			int cnt = 0;
			for(Role role:Role.values()){
				if(!roleSet.contains(role)) continue;
				int c = roleCounterMap.get(name).get(role);
				s.append(String.format("%6d\t", c ));
				cnt += c;
			}
			s.append(String.format("%6d\n", cnt));
		}
		
		System.out.println(s.toString());
		
		if(isSave){
			try(FileWriter fw = new FileWriter(new File(RESULT_DIR + (new Date()).getTime() + "_" + String.format("%05d", set) + ".txt"))){
				fw.write(s.toString());
			}
		}
	}
}
