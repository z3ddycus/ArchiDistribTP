package fr.model;

import javax.jws.WebService;
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
@WebService(endpointInterface = "fr.model.CityManagerService", serviceName = "CityManagerService")
public class CityManager {
	/**
	 * La liste des City
	 */
	private List<City> cities;

	/**
	 * Renvoit une référence vers un CityManager vide.
	 */
	public CityManager() {
		this.cities = new LinkedList<>();
	}

	/**
	 * Les City du CityManager
	 * @return
	 * 		La liste des City contenues dans le CityManager.
	 */
	public List<City> getCities() {
		return cities;
	}

	/**
	 * Remplace la liste des City
	 * @param cities
	 * 		La List des City qui remplacera celle du CityManager.
	 */
	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	/**
	 * Ajoute une City au CityManager
	 * @param city
	 * 		La City qui sera rajoutée
	 * @return
	 * 		Renvoit true si city n'était pas présent dans le CityManager, false sinon
	 */
	public boolean addCity(City city){
		return cities.add(city);
	}

	/**
	 * Retire la City city du CityManager.
	 * @param city
	 * 		La City a supprimée.
	 * @return
	 * 		Renvoit true si city était présent dans CityManager, false sinon
	 */
	public boolean removeCity(City city){
		return cities.remove(city);
	}

	/**
	 * Renvoit les City dont le nom des cityName
	 * @param cityName
	 * 		Le nom recherché
	 * @return
	 * 		La List des City dont le nom est cityName
	 */
	public List<City> searchFor(String cityName){
		List<City> result = new LinkedList<>();
		for (City city : cities) {
			if (city.getName().equals(cityName)) {
				result.add(city);
			}
		}
		return result;
	}

	/**
	 * Supprime toutes les City du CityManager
	 */
	public void clearCities() {
		cities.clear();
	}

	/**
	 * Renvoit la City à la position donnée
	 * @param position
	 * 		La Position donnée
	 * @return
	 * 		La City à la position donnée
	 * @throws CityNotFound
	 * 		Throw si aucune ville ne correspond
	 */
	public City searchExactPosition(Position position) throws CityNotFound{
		for(City city:cities){
			if (position.equals(city.getPosition())){
				return city;
			}
		}
		throw new CityNotFound();
	}

	/**
	 * Renvoit les City dont la position est à moins de 10km de la Position donnée
	 * @param position
	 * 		La Position donnée
	 * @return
	 * 		Les City à moins de 10km de position
	 */
	public CityManager searchNearCity(Position position) {
		CityManager result = new CityManager();
		for (City city : cities){
			double distanceToCity = position.distance(city.getPosition());
			if (distanceToCity < 10) {
				result.addCity(city);
			}
		}
		return result;
	}
}
