package tp.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represent a city manager, it can  
 * <ul>
 * 	<li>add a city</li>
 * 	<li>remove a city</li>
 * 	<li>return the list of cities</li>	
 * 	<li>search a city with a given name</li>
 *  <li>search a city at a position</li>
 * 	<li>return the list of cities near 10km of the given position</li>
 * </ul>
 *
 */
@XmlRootElement
public class CityManager {

	private List<City> cities;
	
	public CityManager() {
		this.cities = new LinkedList<City>();
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public boolean addCity(City city){
		return cities.add(city);
	}
	
	public boolean removeCity(City city){
		return cities.remove(city);
	}
	
	public List<City> searchFor(String cityName){
		
		// TODO: à compléter
		
		return null;
	}
	
	public City searchExactPosition(Position position) throws CityNotFound{
		for(City city:cities){
			if (position.equals(city.getPosition())){
				return city;
			}
		}
		throw new CityNotFound();
	}
	
	/**
	 * TODO: searchNear : une fonction qui retourne la liste des villes à dix klomètres d'une position
	 */
}
