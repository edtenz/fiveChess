package com.tenchael.game.chess;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Chess main board
 * @author tzz1002@gmail.com
 *
 */
public class ChessBoard extends Frame {

	private static final long serialVersionUID = 1L;

	public static final int BOARDWIDTH = 800;

	public static final int BOARDHEIGHT = 800;

	public static final int X_TOBORDER = 50;

	public static final int Y_TOBORDER = 50;

	public static final int LINE_BETWEEN = 50;

	public static final int X_LOCATION = 150;

	public static final int Y_LOCATION = 10;

	private Sides whoWins = Sides.NEUTRAL;

	private Machine mdo = null;

	public static long step = 0L;
	public int rows = 0;
	public int columns = 0;
	public Chessman arr[][] = null;
	public Location dirVector[] = { new Location(-1, 0), new Location(-1, -1),
			new Location(0, -1), new Location(1, -1), new Location(1, 0),
			new Location(1, 1), new Location(0, 1), new Location(-1, 1) };

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public List<Chessman> chessmans = new ArrayList<Chessman>();
	public List<Chessman> adjcent = new ArrayList<Chessman>();
	public Location curLocation = new Location();

	private Image iBuffer;

	private Graphics gBuffer;

	public void launchFrame() {
		this.setTitle("FIVE CHESS");
		this.setSize(BOARDWIDTH, BOARDHEIGHT);
		this.setLocation(X_LOCATION, Y_LOCATION);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);

		mdo = new Machine(this);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

		});

		this.addMouseListener(new mouseMonitor());

		this.setVisible(true);
		rows = (BOARDHEIGHT - (2 * Y_TOBORDER)) / LINE_BETWEEN + 1;
		columns = (BOARDWIDTH - (2 * X_TOBORDER)) / LINE_BETWEEN + 1;

		arr = new Chessman[rows][];
		for (int i = 0; i < rows; i++) {
			arr[i] = new Chessman[columns];
			for (int j = 0; j < columns; j++) {
				arr[i][j] = new Chessman(new Location(i, j));
			}
		}

		int r = rows / 2;
		int c = columns / 2;
		curLocation.set(r, c);
		arr[r][c].setSide(Sides.WHITEONE);
		chessmans.add(arr[r][c]);
		curLocation.set(r, c);
		modifyAdjcent(curLocation);
	}

	public void paint(Graphics g) {

		Color c = g.getColor();

		g.setColor(Color.RED);
		for (int x = X_TOBORDER; x <= BOARDWIDTH - X_TOBORDER; x += LINE_BETWEEN) {
			g.drawLine(x, Y_TOBORDER, x, BOARDHEIGHT - Y_TOBORDER);
		}
		for (int y = Y_TOBORDER; y <= BOARDHEIGHT - Y_TOBORDER; y += LINE_BETWEEN) {
			g.drawLine(X_TOBORDER, y, BOARDWIDTH - X_TOBORDER, y);
		}

		for (int i = 0; i < chessmans.size(); i++) {
			Chessman cm = chessmans.get(i);
			cm.draw(g);
		}
		ChessBoard.drawMark(g, curLocation);

		if (whoWins != Sides.NEUTRAL) {

			g.setFont(new Font(null, Font.BOLD, 50));
			g.setColor(Color.BLUE);
			g.drawString("" + whoWins + "  Won!", X_TOBORDER, BOARDHEIGHT / 2);

		}

		g.setColor(c);
	}

	@Override
	public void update(Graphics g) {
		//Double Buffer technology to avoid scream shine
		if (iBuffer == null) {
			iBuffer = createImage(this.getSize().width, this.getSize().height);
			gBuffer = iBuffer.getGraphics();
		}
		gBuffer.setColor(getBackground());
		gBuffer.fillRect(0, 0, this.getSize().width, this.getSize().height);
		paint(gBuffer);
		g.drawImage(iBuffer, 0, 0, this);
	}

	public void judgeWin(Location cur, Sides side) {
		if (mdo.value1(cur, side) == 4) {
			whoWins = side;
		}
	}

	public void updateAdjcentWt() {
		for (int i = 0; i < dirVector.length; i++) {
			int curR = curLocation.getRow();
			int curC = curLocation.getCol();
			int deep = 1 + mdo.count(curLocation, dirVector[i],
					arr[curR][curC].getSide());
			Location next = new Location(curR + deep * dirVector[i].getRow(),
					curC + deep * dirVector[i].getCol());
			int nextR = next.getRow();
			int nextC = next.getCol();
			if (!(isBeyond(next))
					&& (arr[nextR][nextC].getSide() == Sides.NEUTRAL)) {
				Chessman tmp = arr[nextR][nextC];
				int wtV = mdo.weightValue(tmp.getIndex());
				tmp.setWeightV(wtV);
			}

		}
	}

	public static void drawMark(Graphics g, Location lct) {
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		Point p = lct.getRealPoint();
		for (int i = 0; i < 3; i++) {
			g.drawOval((p.x - Chessman.diam / 2) - i, (p.y - Chessman.diam / 2)
					- i, Chessman.diam + 2 * i, Chessman.diam + 2 * i);
		}
		g.setColor(c);
	}

	private class mouseMonitor extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			if (whoWins != Sides.NEUTRAL) {
				return;
			}

			Point point = e.getPoint();
			Location index = Location.toIndex(point);

			if (null != index
					&& arr[index.getRow()][index.getCol()].getSide() == Sides.NEUTRAL) {
				int r = index.getRow();
				int c = index.getCol();
				arr[r][c].setSide(Sides.BLACKONE);
				if (chessmans.add(arr[r][c])) {
					curLocation.set(r, c);
					modifyAdjcent(index);
					if (adjcent.contains(arr[r][c])) {
						adjcent.remove(arr[r][c]);
					}
					judgeWin(index, Sides.BLACKONE);
					machinedo();
					repaint();
				}

			}

		}

		private void machinedo() {
			mdo.execute();
		}

	}

	public void modifyAdjcent(Location index) {
		for (int i = 0; i < dirVector.length; i++) {
			Location lct = index.add(dirVector[i]);
			int r = lct.getRow();
			int c = lct.getCol();

			if (!isBeyond(lct) && arr[r][c].getSide() == Sides.NEUTRAL) {
				int weightV = mdo.weightValue(lct);
				arr[r][c].setWeightV(weightV);

				if (!this.adjcent.contains(arr[r][c])) {
					this.adjcent.add(arr[r][c]);
				}
			}

		}
		updateAdjcentWt();
	}

	public boolean isBeyond(Location index) {
		return !((index.getRow() >= 0 && index.getRow() < rows
				&& index.getCol() >= 0 && index.getCol() < columns));
	}

	public Sides oppSides(Sides side) {
		if (Sides.BLACKONE == side) {
			return Sides.WHITEONE;
		} else if (Sides.WHITEONE == side) {
			return Sides.BLACKONE;
		} else {
			return Sides.NEUTRAL;
		}
	}
}
