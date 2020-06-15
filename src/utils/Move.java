package utils;

public class Move implements Comparable<Move>{
	private int x;
	private int y;

	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int compareTo(Move o) {
		if(o.getX() == x && o.getY() == y)return 0;
		int different = ((x * 15) + y) - ((o.getX() * 15) + o.getY());
		return different;
	}
	
	public String toString() {
		return this.x + " " + this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
