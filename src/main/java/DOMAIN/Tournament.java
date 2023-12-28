package DOMAIN;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

@Entity
@Table(name = "Tournament")
public class Tournament {
	
	// Attributes:
	@Id
	@Column(name = "code")
	private String stCode;
	
	@Column(name = "category")
	private String stCategory;
	
	@Column(name = "start_date")
	private Date dtStartDate;
	
	@Column(name = "end_date")
	private Date dtEndDate;
	
	// Many to One relationship with Location -
	@ManyToOne(targetEntity = Location.class)
	@JoinColumn(name = "city", referencedColumnName = "city")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Location objLocation;

	// Many to Many (N:M) relationship with Players - A Tournament will have a set of players, this will populate the Game table:
	@ManyToMany(targetEntity = Player.class)
	@JoinTable(name = "Game", joinColumns = {@JoinColumn(name = "code") },
			inverseJoinColumns = {@JoinColumn(name="playerID")})
	@OnDelete(action = OnDeleteAction.CASCADE)		// On delete -> Cascade 
	private Set<Player> relPlayers;
	
	// Empty constructor:
	public Tournament() {
		
	}
	
	// Full constructor:
	public Tournament(String stCode, String stCategory, Location objLocation, Date dtStartDate, Date dtEndDate) {
		this.stCode = stCode;
		this.stCategory = stCategory;
		this.objLocation = objLocation;
		this.dtStartDate = dtStartDate;
		this.dtEndDate = dtEndDate;
	}
	
	// Getters:
	public String getCode() {
		return stCode;
	}
	
	public String getCategory() {
		return stCategory;
	}
	
	public Date getStartDate() {
		return dtStartDate;
	}
	
	public Date getEndDate() {
		return dtEndDate;
	}
	
	public Location getLocation() {
		return objLocation;
	}
	
	public Set<Player> getPlayers(){
		return relPlayers;
	}
	
	// Setters:
	public void setCode(String stCode) {
		this.stCode = stCode;
	}
	
	public void setCategory(String stCategory) {
		this.stCategory = stCategory;
	}
	
	public void setStartDate(Date dtStartDate) {
		this.dtStartDate = dtStartDate;
	}
	
	public void setEndDate(Date dtEndDate) {
		this.dtEndDate = dtEndDate;
	}
	
	public void setLocation(Location objLocation) {
		this.objLocation = objLocation;
	}
	
	public void setPlayers(Set<Player> relPlayers) {
		this.relPlayers = relPlayers;
	}
	
	@Override
	public int hashCode() {
		final int iPrime = 37;
		int iHash = 1;
		iHash = iPrime * iHash + ((relPlayers == null) ? 0 : relPlayers.hashCode());
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
		Tournament objTournament = (Tournament) obj;
		if (relPlayers == null) {
			if (objTournament.relPlayers != null)
				return false;
		} else if (!objTournament.equals(objTournament.relPlayers))
			return false;
		if (stCode.equals("0")) {
			if (objTournament.stCode != "0")
				return false;
		}
		return true;
	}
	
}
