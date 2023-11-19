package lab3.persistence;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lab3.model.Coords;

/**
 * Entity implementation class for Entity: CalcResult
 *
 */
@Entity
public class CalcResult implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
	private @NotNull double x;
    
    @Column(nullable = false)
	private @NotNull double y;
    
    @Column(nullable = false)
	private @NotNull double r;
    
    @Column(nullable = false)
	private @NotNull boolean result;

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getR() {
		return r;
	}
	
	public void setR(double r) {
		this.r = r;
	}
	
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Boolean checkResult() {
		if (x >= -1 * r && x <= 0 && y >= -1 * r / 2 && y <= 0
				|| x >= 0 && y <= 0 && x * x + y * y <= r * r / 4
				|| x >= 0 && y >= 0 && y <= -1 * x + r)
			return true;

		return false;
	}

	public static CalcResult fromCoords(Coords coords) {
		CalcResult calcResult = new CalcResult();
		calcResult.setX(coords.getX());
		calcResult.setY(coords.getY());
		calcResult.setR(coords.getR());
		
		calcResult.setResult(calcResult.checkResult());
		return calcResult;
	}
}
