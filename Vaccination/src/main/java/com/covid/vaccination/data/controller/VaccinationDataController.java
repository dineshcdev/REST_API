package com.covid.vaccination.data.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.covid.vaccination.data.model.VaccinationData;
import com.covid.vaccination.data.repository.VaccinationDataRepository;



@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class VaccinationDataController {
	   private static final Logger logger = LoggerFactory.getLogger(VaccinationDataController.class);

	@Autowired
	VaccinationDataRepository vaccinationRepository;

	@GetMapping("/vaccinationData")
	public ResponseEntity<List<VaccinationData>> getAllVaccinationDataForCountry(@RequestParam(required = false) String country) {
		try {
			List<VaccinationData> vaccinationData = new ArrayList<VaccinationData>();
			logger.debug("Invoked getAllVaccinationDataForCountry with params ----"+country);
			if (country == null)
				vaccinationRepository.findAll().forEach(vaccinationData::add);
			else
				vaccinationRepository.findByCountryContaining(country).forEach(vaccinationData::add);

			if (vaccinationData.isEmpty()) {
				logger.info("---- vaccinationData is empty ----");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.debug("getAllVaccinationDataForCountry ::: returned ----"+vaccinationData);
			return new ResponseEntity<>(vaccinationData, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in getAllVaccinationDataForCountry ----"+e,e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/vaccinationData/{id}")
	public ResponseEntity<VaccinationData> getVaccinationDataById(@PathVariable("id") long id) {
		logger.debug("Invoked getVaccinationDataById with params ----"+id);
		Optional<VaccinationData> vaccinationData = vaccinationRepository.findById(id);

		if (vaccinationData.isPresent()) {
			logger.debug("getVaccinationDataById ::: returned ----"+vaccinationData);
			return new ResponseEntity<>(vaccinationData.get(), HttpStatus.OK);
		} else {
			logger.info("---- vaccinationData is empty for the id ----"+id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/vaccinationData")
	public ResponseEntity<VaccinationData> addVaccinationData(@Valid @RequestBody VaccinationData vaccinationData) {
		try {
			logger.debug("Invoked addVaccinationData with params ----"+vaccinationData);
			VaccinationData _vaccinationData = vaccinationRepository.save(new VaccinationData(vaccinationData.getName(),vaccinationData.getAge(),vaccinationData.getGender(), vaccinationData.getCountry(), false));
			logger.debug("addVaccinationData ::: returned ----"+_vaccinationData);
			return new ResponseEntity<>(_vaccinationData, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.error("Error in addVaccinationData ----"+e,e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/vaccinationData/{id}")
	public ResponseEntity<VaccinationData> updateVaccinationData(@PathVariable("id") long id,@Valid @RequestBody VaccinationData vaccinationData) {
		logger.debug("Invoked updateVaccinationData with params --ID--"+id+"---vaccinationData---"+vaccinationData);
		Optional<VaccinationData> vaccineDataExisted = vaccinationRepository.findById(id);

		if (vaccineDataExisted.isPresent()) {
			VaccinationData vaccinationDataUpdated = vaccineDataExisted.get();
			vaccinationDataUpdated.setName(vaccinationData.getName());
			vaccinationDataUpdated.setAge(vaccinationData.getAge());
			vaccinationDataUpdated.setGender(vaccinationData.getGender());
			vaccinationDataUpdated.setCountry(vaccinationData.getCountry());
			vaccinationDataUpdated.setVaccinated(vaccinationData.isVaccinated());
			logger.debug("updateVaccinationData ::: returned ----"+vaccinationDataUpdated);
			return new ResponseEntity<>(vaccinationRepository.save(vaccinationDataUpdated), HttpStatus.OK);
		} else {
			logger.info("---- vaccinationData is empty for the id ----"+id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/vaccinationData/{id}")
	public ResponseEntity<HttpStatus> deleteVaccinationData(@PathVariable("id") long id) {
		logger.debug("Invoked deleteVaccinationData with params ----"+id);

		try {
			vaccinationRepository.deleteById(id);
			logger.debug("deleteVaccinationData ::: returned --Data Removed successfully for id--"+id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error in deleteVaccinationData ----"+e,e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/vaccinationData")
	public ResponseEntity<HttpStatus> deleteAllVaccinationData() {
		try {
			vaccinationRepository.deleteAll();
			logger.debug("deleteAllVaccinationData ::: returned --All Data Removed successfully --");
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			logger.error("Error in deleteAllVaccinationData ----"+e,e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/vaccinationData/vaccinated")
	public ResponseEntity<List<VaccinationData>> findByVaccinated() {
		logger.debug("Invoked findByVaccinated ----");
		try {
			List<VaccinationData> vaccinationData = vaccinationRepository.findByIsVaccinated(true);

			if (vaccinationData.isEmpty()) {
				logger.info("---- vaccination People Data is empty ----");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			logger.debug("findByVaccinated ::: returned ----"+vaccinationData);
			return new ResponseEntity<>(vaccinationData, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in findByVaccinated ----"+e,e.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
