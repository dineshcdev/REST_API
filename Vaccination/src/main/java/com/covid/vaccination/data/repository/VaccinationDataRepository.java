package com.covid.vaccination.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.covid.vaccination.data.model.VaccinationData;

@Repository
public interface VaccinationDataRepository extends JpaRepository<VaccinationData, Long> {
  List<VaccinationData> findByIsVaccinated(boolean isVaccinated);

  List<VaccinationData> findByCountryContaining(String country);
}
