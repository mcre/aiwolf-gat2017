/**
 * 
 */
package net.mchs_u.mc.aiwolf.common.starter.component;

import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;

import net.mchs_u.mc.aiwolf.common.EstimatePlayer;

/**
 * @author m_cre
 * DirectStarter用のプレイヤー定義（クラス・名前・役職（nullなら自動割当））
 */
public class PlayerDefinition {
	private Class<?> playerClass;
	private String name;
	private Role role;
	private boolean isVisualize;
	
	public PlayerDefinition(Class<?> samplePlayerClass, boolean isVisualize, Role role, String name) throws InstantiationException, IllegalAccessException{
		this.playerClass = samplePlayerClass;
		this.isVisualize = isVisualize;
		this.role = role;
		this.name = name;
		
		playerClass.newInstance(); // 例外発生確認用
	}

	public Class<?> getPlayerClass() {
		return playerClass;
	}
	
	public Player getNewPlayerInstance() {
		Player player = null;
		try {
			player = (Player)playerClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e){}
		if(name != null)
			player = new RenamedPlayer(player, name);
		if(isVisualize && player instanceof EstimatePlayer)
			player = new DebugVisualizePlayer((EstimatePlayer)player);
		return player;
	}
	
	public String getName() {
		return name;
	}
	
	public Role getRole() {
		return role;
	}	
	
}
