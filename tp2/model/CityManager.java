package tp2.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

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
 */
@XmlRootElement
public class CityManager {

	private List<City> cities;
	
	public CityManager() {
		this.cities = new LinkedList<>();
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
		List<City> result = new LinkedList<>();
		for (City city : cities) {
			if (city.getName().equals(cityName)) {
				result.add(city);
			}
		}
		return result;
	}
	public void clearCities() {
		cities.clear();
	}
	public City searchExactPosition(Position position) throws CityNotFound {
		for(City city:cities){
			if (position.equals(city.getPosition())){
				return city;
			}
		}
		throw new CityNotFound();
	}

	public City searchNearCity(Position position) throws CityNotFound {
		City result = null;
		double distance = 10;
		for (City city : cities){
			double distanceToCity = position.distance(city.getPosition());
			if (distanceToCity < distance) {
				result = city;
				distance = distanceToCity;
			}
		}
		if (result == null) {
			throw new CityNotFound();
		}
		return result;
	}
}
