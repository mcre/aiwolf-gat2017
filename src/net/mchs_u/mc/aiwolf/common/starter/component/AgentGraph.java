package net.mchs_u.mc.aiwolf.common.starter.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class AgentGraph extends JPanel {
	private static final long serialVersionUID = 1L;

	private double rateVillagerTeam = 0.33333d;
	private double rateWerewolf  = 0.3333d;

	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
		double a = this.getHeight() * rateVillagerTeam;
		double b = this.getHeight() * rateWerewolf;
		double h = this.getHeight();
		double w = this.getWidth();

		g2.setColor(Color.decode("#8ec6ff"));
		g2.fill(new Rectangle2D.Double(0d, 0d, w, a));
		g2.setColor(Color.decode("#ffc68e"));
		g2.fill(new Rectangle2D.Double(0d, a, w, h - a - b));
		g2.setColor(Color.decode("#ff8e8e"));
		g2.fill(new Rectangle2D.Double(0d, h - b, w, b));
	}

	public void setRateVillagerTeam(double rateVillagerTeam) {
		this.rateVillagerTeam = rateVillagerTeam;
		repaint();
	}

	public void setRateWerewolf(double rateWerewolf) {
		this.rateWerewolf = rateWerewolf;
		repaint();
	}

}
