package org.springframework.samples.petclinic.vet;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpecialtyRepository extends Repository<Specialty, Integer> {

	Specialty findById(Integer id);

	@Query("SELECT specialty FROM Specialty specialty ORDER BY specialty.name")
	@Transactional(readOnly = true)
	List<Specialty> findSpecialties();

}

