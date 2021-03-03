package com.covid.vaccination.data.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "vaccination_data")
public class VaccinationData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull(message="Name must not be null")
	@Column(name = "name")
	private String name;

	@Column(name = "age")
	private Integer age;
	
	@Column(name = "gender")
	private String gender;

	@NotNull
	@Size(min=2, message="CountryName should have atleast 2 characters")
	@Column(name = "country")
	private String country;
	
	@Column(name = "vaccinated")
	private boolean isVaccinated;

	public VaccinationData() {

	}


	public VaccinationData(String name, Integer age, String gender, String country, boolean isVaccinated) {
		super();
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.country = country;
		this.isVaccinated = isVaccinated;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isVaccinated() {
		return isVaccinated;
	}

	public void setVaccinated(boolean isVaccinated) {
		this.isVaccinated = isVaccinated;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("VaccineData [name=");
		builder.append(name);
		builder.append(", age=");
		builder.append(age);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", country=");
		builder.append(country);
		builder.append(", isVaccinated=");
		builder.append(isVaccinated);
		builder.append("]");
		return builder.toString();
	}

	


}
