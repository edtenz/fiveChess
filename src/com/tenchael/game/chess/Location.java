package com.tenchael.game.chess;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * the location in board
 * @author tzz1002@gmail.com
 *
 */
public class Location {

	private int r;
	private int c;

	public Location(int row, int col) {
		set(row, col);
	}

	public Location() {
	}

	public int getRow() {
		return r;
	}

	public int getCol() {
		return c;
	}

	public void setRow(int row) {
		this.r = row;
	}

	public void setCol(int col) {
		this.c = col;
	}

	public void set(int row, int col) {
		this.setRow(row);
		this.setCol(col);
	}

	public Point getRealPoint() {
		int x = ChessBoard.X_TOBORDER + ChessBoard.LINE_BETWEEN * this.getCol();
		int y = ChessBoard.Y_TOBORDER + ChessBoard.LINE_BETWEEN * this.getRow();
		Point p = new Point(x, y);
		return p;
	}

	public static Location toIndex(Point p) {
		if (p.x >= ChessBoard.X_TOBORDER
				&& p.x < ChessBoard.BOARDWIDTH - ChessBoard.X_TOBORDER
				&& p.y >= ChessBoard.Y_TOBORDER
				&& p.y < ChessBoard.BOARDHEIGHT - ChessBoard.Y_TOBORDER) {
			int col = (p.x - ChessBoard.X_TOBORDER) / ChessBoard.LINE_BETWEEN;
			int row = (p.y - ChessBoard.Y_TOBORDER) / ChessBoard.LINE_BETWEEN;
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					Location index = new Location(row + i, col + j);
					if (index.contains(p)) {
						return index;
					}
				}
			}
		}
		return null;
	}

	public boolean contains(Point point) {
		Point p = this.getRealPoint();

		Rectangle rec = new Rectangle(p.x - Chessman.diam / 2, p.y
				- Chessman.diam / 2, Chessman.diam, Chessman.diam);

		if (rec.contains(point)) {
			return true;
		}
		return false;
	}

	public Location add(Location lct) {
		Location ret = new Location();
		int row = this.getRow() + lct.getRow();
		int col = this.getCol() + lct.getCol();
		ret.set(row, col);
		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof Location) {
			Location lct = (Location) obj;
			return (lct.getRow() == this.getRow())
					&& (lct.getCol() == this.getCol());
		}
		return false;
	}

}
