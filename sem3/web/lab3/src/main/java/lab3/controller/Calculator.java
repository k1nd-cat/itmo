package lab3.controller;

import java.io.Serializable;

import javax.inject.Inject;

import lab3.model.Coords;
import lab3.persistence.CalcResult;
import lab3.service.CalcResultService;

public class Calculator implements Serializable {

    @Inject
    private CalcResultService calcResultService;
    
    private static final long serialVersionUID = 1L;

    public void perform(Coords coords) {
    	
    	System.out.println("calculator: " + coords);
    	
    	CalcResult calcResult = CalcResult.fromCoords(coords);
    	calcResultService.create(calcResult);
    }
}
