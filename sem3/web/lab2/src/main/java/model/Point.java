package model;

public class Point {
	private Double x;
	private Double y;
	private Double r;
	private Boolean hitting;

	public Point(Double x, Double y, Double r) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.hitting = false;
	}

	public Double getX() {
		return x;
	}

	public Double getY() {
		return y;
	}

	public Double getR() {
		return r;
	}

	public Boolean getHitting() {
		return hitting;
	}

	public void setHitting(Boolean hitting) {
		this.hitting = hitting;
	}
	
}
