package com.tenchael.game.chess;

import java.awt.*;

/**
 * the chessman
 * @author tzz1002@gmail.com
 *
 */
public class Chessman {

	public static final int diam = 35;

	private Location index = null;

	private Sides side = Sides.NEUTRAL;

	private int weightV = 0;	

	public Chessman(Location index, Sides side) {
		this.index = index;
		this.side = side;
	}

	public Chessman(Location index) {
		this.index = index;
	}

	public void setIndex(Location index) {
		this.index = index;
	}

	public Location getIndex() {
		return this.index;
	}

	public void draw(Graphics g) {
		Color c = g.getColor();

		if (side == Sides.BLACKONE) {
			g.setColor(Color.BLACK);
		}
		if (side == Sides.WHITEONE) {
			g.setColor(Color.WHITE);
		}

		/* for testing
		if (side == Sides.NEUTRAL) {
			g.setColor(Color.GREEN);
			Point p = this.getIndex().getRealPoint();
			g.drawOval(p.x - diam / 2, p.y - diam / 2, diam, diam);
			String str = wV + "," + bV;
			g.drawString(str, p.x - diam / 2, p.y - diam / 2);
			g.setColor(c);
			return;
		}
		 */

		Point p = this.getIndex().getRealPoint();
		g.fillOval(p.x - diam / 2, p.y - diam / 2, diam, diam);

		g.setColor(c);
	}

	public int getWeightV() {
		return weightV;
	}

	public void setWeightV(int weightV) {
		this.weightV = weightV;
	}

	public void setSide(Sides side) {
		this.side = side;
	}

	public Sides getSide() {
		return this.side;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof Chessman) {
			Chessman chsm = (Chessman) obj;
			return this.getIndex().equals(chsm.getIndex());
		}
		return false;
	}

}
