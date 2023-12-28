package DOMAIN;

import jakarta.persistence.*;

// OBJECT Location - Hibernate annotations

@Entity
@Table(name = "Location")
public class Location {

	// Attributes:
	@Id
	@Column(name = "city")
	private String stCity;
	
	@Column(name = "country")
	private String stCountry;
	
	@Column(name = "population")
	private int iPopulation;
	
	// Methods
	
	// Empty constructor:
	public Location() {
		
	}
	
	// Constructor with all the fields:
	public Location (String stCity, String stCountry, int iPopulation) {
		this.stCity = stCity;
		this.stCountry = stCountry;
		this.iPopulation = iPopulation;
	}
	
	// Getters:
	public String getCity() {
		return stCity;
	}
	
	public String getCountry() {
		return stCountry;
	}
	
	public int getPopulation() {
		return iPopulation;
	}
	
	// Setters:
	public void setCity(String stCity) {
		this.stCity = stCity;
	}
	
	public void setCountry(String stCountry) {
		this.stCountry = stCountry;
	}
	
	public void setPopulation(int iPopulation) {
		this.iPopulation = iPopulation;
	}
}
