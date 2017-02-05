/**
 * 
 */
package net.mchs_u.mc.aiwolf.starter.component;

import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;

/**
 * @author m_cre
 * DirectStarter用のプレイヤー定義（クラス・名前・役職（nullなら自動割当））
 */
public class PlayerDefinition {
	private Class<?> playerClass;
	private String name;
	private Role role;
	
	public PlayerDefinition(Class<?> samplePlayerClass, String name, Role role) throws InstantiationException, IllegalAccessException{
		this.playerClass = samplePlayerClass;
		this.name = name;
		this.role = role;
		
		playerClass.newInstance(); // 例外発生確認用
	}

	public Class<?> getPlayerClass() {
		return playerClass;
	}
	
	public Player getNewPlayerInstance() {
		try {
			Player player = (Player)playerClass.newInstance();
			if(name != null)
				return new RenamedPlayer(player, name);
			return player;
		} catch (InstantiationException | IllegalAccessException e){}
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public Role getRole() {
		return role;
	}	
	
}
