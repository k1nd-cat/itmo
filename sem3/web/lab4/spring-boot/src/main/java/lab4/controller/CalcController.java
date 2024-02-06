package lab4.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lab4.persistence.CalcResult;
import lab4.persistence.CalcResultRepository;
import lab4.persistence.User;
import lab4.persistence.UserRepository;

@RestController
@RequestMapping("calc")
public class CalcController {
	
	private final UserRepository userRepository;
	private final CalcResultRepository repository;

	CalcController(CalcResultRepository repository, UserRepository userRepository) {
		this.repository = repository;
		this.userRepository = userRepository;
	}
  
	@GetMapping("/results")
	List<CalcResult> all(@RequestParam String token) {
		Optional<User> userOpt = userRepository.findByToken(token);
		if (!userOpt.isPresent()) {
			return Collections.emptyList(); 
		}
		return repository.findByLoginOrderByIdDesc(userOpt.get().getLogin());
	}
	
	@PostMapping("/results")
	Map<String, Object> save(@RequestBody CalcResult calcResult) {
		if (calcResult.getX() < -3 || calcResult.getX() > 3) throw new RuntimeException("incorrect X");
		if (calcResult.getY() < -3 || calcResult.getY() > 5) throw new RuntimeException("incorrect Y");
		if (calcResult.getR() < 1 || calcResult.getR() > 3) throw new RuntimeException("incorrect R");
		checkResult(calcResult);
		Optional<User> userOpt = userRepository.findByToken(calcResult.getLogin());
		if (!userOpt.isPresent()) {
			return Collections.emptyMap();
		}
		calcResult.setLogin(userOpt.get().getLogin());
		repository.save(calcResult);
		return Collections.emptyMap();
	}
	
	private void checkResult(CalcResult calcResult) {
		double x = calcResult.getX();
		double y = calcResult.getY();
		double r = calcResult.getR();
		boolean result = x >= -1 * r / 2 && x <= 0 && y >= -1 * r && y <= 0
	        || x >= 0 && y <= 0 && x * x + y * y <= r * r
	        || x <= 0 && y >= 0 && y <= x + r;
	    calcResult.setResult(result);
	}
}
