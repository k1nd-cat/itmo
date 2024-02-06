package lab4.persistence;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity implementation class for Entity: CalcResult
 */
@Entity
@Table(name = "calcresult")
public class CalcResult implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private double x;
    
	private double y;
    
	private double r;
    
	private boolean result;

	private String login;

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

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	/*
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
	*/
}
