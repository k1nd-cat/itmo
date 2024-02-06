package lab4.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CalcResultRepository extends JpaRepository<CalcResult, Long> {
	
	List<CalcResult> findByLoginOrderByIdDesc(String login);
	
}