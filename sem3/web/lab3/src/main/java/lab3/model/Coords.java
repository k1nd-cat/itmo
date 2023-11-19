package lab3.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.faces.context.FacesContext;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import lab3.controller.Calculator;

public class Coords implements Serializable {

    private static final long serialVersionUID = 1L;
    
	Calculator calculator; 
	
	private FacesContext context;
	
	public void setCalculator(Calculator calculator) {
		this.calculator = calculator;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	@DecimalMin(value = "-5", message = "X должен быть больше или равен -5")
	@DecimalMax(value = "5", message = "X должен быть меньше или равен 5")
	private double x;
	private double y;
	@DecimalMin(value = "1", message = "R должен быть больше или равен -5")
	@DecimalMax(value = "3", message = "R должен быть меньше или равен 5")
	private double r = 2;

	private String error;
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		if (context != null) {
			context.getExternalContext().getSessionMap().put("x", x);
		}

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
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String processResult() {
		try {
			x = (Double)context.getExternalContext().getSessionMap().getOrDefault("x", 0.0);
		} catch (Exception exc) {}
		calculator.perform(this);
		setError(null);
		return "result?faces-redirect=true";
    }
	
	public void selectPoint() {
		double x = Double.valueOf(context.getExternalContext().getRequestParameterMap().get("valueX"));
		double y = Double.valueOf(context.getExternalContext().getRequestParameterMap().get("valueY"));
		double r = Double.valueOf(context.getExternalContext().getRequestParameterMap().get("valueR"));
		Coords coords = new Coords();
		coords.setX(x);
		coords.setY(y);
		coords.setR(r);
		calculator.perform(coords);
	}

	public Date getDateTime() {
		Calendar calendar = new GregorianCalendar();
		int second = calendar.get(Calendar.SECOND);
		second /= 10;
		second *= 10;
		calendar.set(Calendar.SECOND, second);

		return calendar.getTime();
	}

	@Override
	public String toString() { 
		return "Coords [x=" + x + ", y=" + y + ", r=" + r + "]";
	}
    
}