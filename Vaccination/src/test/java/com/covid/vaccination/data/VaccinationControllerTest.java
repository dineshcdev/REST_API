package com.covid.vaccination.data;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.covid.vaccination.data.model.VaccinationData;
import com.covid.vaccination.data.repository.VaccinationDataRepository;

@WebAppConfiguration
@SpringBootTest(webEnvironment=WebEnvironment.MOCK, classes={ VaccineDataApplication.class })
@RunWith(SpringRunner.class)
public class VaccinationControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean 
	VaccinationDataRepository vaccinationRepository;
	
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
		public void testCreateData() throws Exception {
			VaccinationData data = new VaccinationData();
			data.setAge(77);
			data.setName("Borris Becker1");
			data.setGender("Male");
			data.setCountry("UK");
			when(vaccinationRepository.save(any(VaccinationData.class))).thenReturn(data);
			
			
			mockMvc.perform(post("/api/vaccinationData")
			 .contentType(MediaType.APPLICATION_JSON)
	         .content("{\"name\": \"Borris Becker1\",\"age\": 77,\"gender\": \"Male\",\"country\": \"UK\"}") 
	         .accept(MediaType.APPLICATION_JSON))
	         .andExpect(status().isCreated())
	         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
	         .andExpect(jsonPath("$.name").value("Borris Becker1")) 
	         .andExpect(jsonPath("$.gender").value("Male"))
	         .andExpect(jsonPath("$.age").value(77))
			 .andExpect(jsonPath("$.vaccinated").value(false));

		}
		
	@Test
	public void testGetAllVaccinationDataForCountry() throws Exception {
		List<VaccinationData> vaccinationDataList = new ArrayList<VaccinationData>();
		VaccinationData data = new VaccinationData();
		data.setAge(77);
		data.setName("Borris Becker1");
		data.setGender("Male");
		data.setCountry("UK");
		vaccinationDataList.add(data);
		when(vaccinationRepository.findByCountryContaining("UK")).thenReturn(vaccinationDataList);
		
		mockMvc.perform(get("/api/vaccinationData?country=UK")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[*].country").value ("UK"))
				.andExpect(jsonPath("$[*].name").value("Borris Becker1"));

	}
	
	@Test
	public void testGetVaccinationDataById() throws Exception {
		VaccinationData data = new VaccinationData();
		data.setAge(77);
		data.setName("Borris Becker1");
		data.setGender("Male");
		data.setCountry("UK");
		when(vaccinationRepository.findById(1L)).thenReturn(Optional.of(data));
		
		mockMvc.perform(get("/api/vaccinationData/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.country").value ("UK"))
				.andExpect(jsonPath("$.name").value("Borris Becker1"));

	}
	
	
	@Test
	public void testUpdateVaccinationData() throws Exception {
		VaccinationData oldData = new VaccinationData();
		oldData.setAge(77);
		oldData.setName("Borris Becker");
		oldData.setGender("Male");
		oldData.setCountry("UK");
		oldData.setVaccinated(false);	
		when(vaccinationRepository.findById(1L)).thenReturn(Optional.of(oldData));

				VaccinationData data = new VaccinationData();
				data.setAge(77);
				data.setName("Borris Becker Modified");
				data.setGender("Male");
				data.setCountry("UK");
				data.setVaccinated(true);
				when(vaccinationRepository.save(any(VaccinationData.class))).thenReturn(data);
				
				
				mockMvc.perform(put("/api/vaccinationData/1")
				 .contentType(MediaType.APPLICATION_JSON)
		         .content("{\"name\": \"Borris Becker Modified\",\"age\": 77,\"gender\": \"Male\",\"country\": \"UK\",\"vaccinated\": true}") 
		         .accept(MediaType.APPLICATION_JSON))
		         .andExpect(status().isOk())
		         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		         .andExpect(jsonPath("$.name").value("Borris Becker Modified")) 
		         .andExpect(jsonPath("$.gender").value("Male"))
		         .andExpect(jsonPath("$.age").value(77))
				 .andExpect(jsonPath("$.vaccinated").value(true));

			}
	
	@Test
		public void testDeleteVaccinationData() throws Exception {
			VaccinationData data = new VaccinationData();
			data.setAge(77);
			data.setName("Borris Becker1");
			data.setGender("Male");
			data.setCountry("UK");
			vaccinationRepository.deleteById(2L);
			mockMvc.perform(delete("/api/vaccinationData/2")).andExpect(status().isNoContent());
		}
	
	@Test
	public void testFindByVaccinated() throws Exception {
		List<VaccinationData> vaccinationDataList = new ArrayList<VaccinationData>();
		VaccinationData data = new VaccinationData();
		data.setAge(77);
		data.setName("Borris Becker1");
		data.setGender("Male");
		data.setCountry("UK");
		data.setVaccinated(true);
		vaccinationDataList.add(data);
		when(vaccinationRepository.findByIsVaccinated(true)).thenReturn(vaccinationDataList);
		
		mockMvc.perform(get("/api/vaccinationData/vaccinated")
				 .accept(MediaType.APPLICATION_JSON))
		         .andExpect(status().isOk())
		         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
		         .andExpect(jsonPath("$[*].name").value("Borris Becker1")) 
		         .andExpect(jsonPath("$[*].gender").value("Male"))
		         .andExpect(jsonPath("$[*].age").value(77))
				 .andExpect(jsonPath("$[*].vaccinated").value(true));
	}
}
