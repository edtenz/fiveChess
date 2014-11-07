package com.tenchael.game.chess;

/**
 * The machine side of chessing
 * @author tzz1002@gmail.com
 *
 */
public class Machine {
	ChessBoard cb = null;

	public Machine(ChessBoard cb) {
		this.cb = cb;
	}

	/**
	 * for testing
	 */
	public Machine() {
	}

	public void execute() {
		Location index = maxWtLct();
		int r = index.getRow();
		int c = index.getCol();
		cb.arr[r][c].setSide(Sides.WHITEONE);
		cb.chessmans.add(cb.arr[r][c]);
		cb.curLocation.set(r, c);
		cb.modifyAdjcent(index);
		if (cb.adjcent.contains(cb.arr[r][c])) {
			cb.adjcent.remove(cb.arr[r][c]);
		}
		cb.judgeWin(index, Sides.WHITEONE);
		cb.repaint();
	}

	public Location maxWtLct() {
		int index = 0;
		for (int i = 1; i < this.cb.adjcent.size(); i++) {
			int tmp = this.cb.adjcent.get(i).getWeightV();
			if (tmp > this.cb.adjcent.get(index).getWeightV()) {
				index = i;
			}
		}
		return this.cb.adjcent.get(index).getIndex();
	}

	public int count(Location curLct, Location dir, Sides side) {
		Location next = curLct.add(dir);
		int r = next.getRow();
		int c = next.getCol();
		if (r < 0 || c < 0 || r >= cb.rows || c >= cb.columns) {
			return 0;
		} else if (cb.arr[r][c].getSide() != side) {
			return 0;
		} else {
			return count(next, dir, side) + 1;
		}
	}

	public int value1(Location curLct, Sides side) {
		int n[] = new int[8];
		for (int i = 0; i < n.length; i++) {
			n[i] = count(curLct, cb.dirVector[i], side);
		}
		int index = maxValueIn(n);
		return n[index] + n[index + 4];
	}

	public int newValue(Location curLct, Sides sd) {
		int n[] = new int[8];
		for (int i = 0; i < n.length; i++) {
			n[i] = count(curLct, cb.dirVector[i], sd);
		}
		int index = maxValueIn(n);

		int max = n[index] + n[index + 4];
		if (max >= 4) {
			return max;
		}

		int deep1 = n[index] + 1;
		int deep2 = n[index + 4] + 1;
		Location next1 = new Location(curLct.getRow() + deep1
				* cb.dirVector[index].getRow(), curLct.getCol() + deep1
				* cb.dirVector[index].getCol());
		Location next2 = new Location(curLct.getRow() + deep2
				* cb.dirVector[index + 4].getRow(), curLct.getCol() + deep2
				* cb.dirVector[index + 4].getCol());

		boolean flag1 = cb.isBeyond(next1);
		boolean flag2 = cb.isBeyond(next2);

		if (!flag1 && !flag2) {
			boolean flag3 = (cb.arr[next1.getRow()][next1.getCol()].getSide() == cb
					.oppSides(sd));
			boolean flag4 = (cb.arr[next2.getRow()][next2.getCol()].getSide() == cb
					.oppSides(sd));
			if (flag3 || flag4) {
				return 1;
			}
		} else {
			return 0;
		}
		return max;
	}
/*
	private int maxValue(int n[]) {
		int max = -1;
		for (int i = 0; i < 4; i++) {
			if (max < n[i] + n[i + 4]) {
				max = n[i] + n[i + 4];
			}
		}
		return max;
	}
*/

	private int maxValueIn(int n[]) {
		int index = 0;
		int t;
		for (int i = 0; i < 4; i++) {
			t = n[index] + n[index + 4];
			if (t < n[i] + n[i + 4]) {
				index = i;
			}
		}
		return index;
	}

	public int weightValue(Location curLct) {
		int w[] = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096,
				8192, 16384, 32768, 65536 };
		int whiteV = newValue(curLct, Sides.WHITEONE);
		int blackV = newValue(curLct, Sides.BLACKONE);
		int weightV = w[2 * whiteV + 1] + w[2 * blackV];
		return weightV;
	}
}
