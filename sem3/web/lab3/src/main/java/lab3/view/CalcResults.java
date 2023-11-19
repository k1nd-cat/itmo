package lab3.view;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import lab3.persistence.CalcResult;
import lab3.service.CalcResultService;

public class CalcResults {

    @Inject
    private CalcResultService calcResultService;

    public List<CalcResult> getResults() {
    	List<CalcResult> results = calcResultService.list();
        return results;
    }

    public void setResultsStr(String resultsStr) {}

    public String getResultsStr() {
    	List<CalcResult> results = calcResultService.list();
    	String resultStr = "[" + results.stream().map(result -> "{x: " + result.getX() + ", y: " + result.getY() + ", r: " + result.getR() + "}").collect(Collectors.joining(", ")) + "]";
    	return resultStr;
    }
	
}
