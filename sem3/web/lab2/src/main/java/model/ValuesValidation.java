package model;

import java.util.List;
import java.util.stream.Collectors;

public class ValuesValidation {

	private List<Point> points;
	
	public ValuesValidation(List<Point> points) {
		super();
		this.points = points;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void validate(List<Point> points) {
		if (points == null || points.isEmpty()) return;
		List<Point> filteredPoints = points.stream().filter(this::checkPoint).collect(Collectors.toList());
		points.clear();
		points.addAll(filteredPoints);
	}

	private boolean checkPoint(Point point) {
		return checkX(point.getX()) && checkY(point.getY()) && checkR(point.getR());
	}

	private boolean checkX(double x) {
		return -3D <= x && x <= 5D;
	}

	private boolean checkY(double y) {
		return -3D < y && y < 3D;
	}

	private boolean checkR(double r) {
		for (double i = 1D; i <= 3D; i += 0.5D)
			if (r == i) return true;

		return false;
	}
}
