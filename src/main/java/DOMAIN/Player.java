package DOMAIN;

import jakarta.persistence.*;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

// OBJECT Player - Hibernate annotations

@Entity
@Table(name = "Player")
public class Player {
	
	// Attributes:
	@Id
	@Column(name = "playerID")
	private int iPlayerID;
	
	@Column(name = "fullname")
	private String stFullName;
	
	@Column(name = "country")
	private String stCountry;
	
	@Column(name = "ELO")
	private int iELO;
	
	// Many to Many (N:M) - relationship with Tournaments - A Player will have a set of Tournaments, this will populate the Game table:
	@ManyToMany(targetEntity = Tournament.class)
	@JoinTable(name = "Game", joinColumns = {@JoinColumn(name = "playerID") }, // Joins the Game table with the iPlayerID column
			inverseJoinColumns = {@JoinColumn(name="code")})				
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<Tournament> relTournaments;
	
	
	// Empty constructor:
	public Player() {
		
	}
	
	// Full constructor (without the set):
	public Player(int iPlayerID, String stFullName, String stCountry, int iELO) {
		this.iPlayerID = iPlayerID;
		this.stFullName = stFullName;
		this.stCountry = stCountry;
		this.iELO = iELO;
	}
	
	// Getters:
	public int getPlayerID() {
		return iPlayerID;
	}

	public String getFullName() {
		return stFullName;
	}
	
	public String getCountry() {
		return stCountry;
	}
	
	public int getELO() {
		return iELO;
	}
	
	public Set<Tournament> getTournaments(){
		return relTournaments;
	}
	
	// Setters:
	public void setPlayerID(int iPlayerID) {
		this.iPlayerID = iPlayerID;
	}
	
	public void setFullName(String stFullName) {
		this.stFullName = stFullName;
	}
	
	public void setCountry(String stCountry) {
		this.stCountry = stCountry;
	}
	
	public void setELO(int iELO) {
		this.iELO = iELO;
	}
	
	public void setTournaments(Set<Tournament> relTournaments) {
		this.relTournaments = relTournaments;
	}
	
	@Override
	public int hashCode() {
		final int iPrime = 80;
		int iHash = 5;
		iHash = iPrime * iHash * iPlayerID;			// I had to simplify the hashCode method in order to avoid exceptions.
		return iHash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player objPlayer = (Player) obj;
		if (relTournaments == null) {
			if (objPlayer.relTournaments != null)
				return false;
		} else if (!objPlayer.equals(objPlayer.relTournaments))
			return false;
		if (iPlayerID == 0) {
			if (objPlayer.iPlayerID != 0)
				return false;
		}
		return true;
	}
}
