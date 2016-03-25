package fr.model;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface CityManagerService {
    /**
     * Ajoute une City au CityManager
     * @param city
     * 		La City qui sera rajoutée
     * @return
     * 		Renvoit true si city n'était pas présent dans le CityManager, false sinon
     */
    boolean addCity(City city);
    /**
     * Retire la City city du CityManager.
     * @param city
     * 		La City a supprimée.
     * @return
     * 		Renvoit true si city était présent dans CityManager, false sinon
     */
    boolean removeCity(City city);


    /**
     * Les City du CityManager
     * @return
     * 		La liste des City contenues dans le CityManager.
     */
    List<City> getCities();

    /**
     * Renvoit les City dont le nom des cityName
     * @param cityName
     * 		Le nom recherché
     * @return
     * 		La List des City dont le nom est cityName
     */
    List<City> searchFor(String cityName);/**
     * Renvoit la City à la position donnée
     * @param position
     * 		La Position donnée
     * @return
     * 		La City à la position donnée
     * @throws CityNotFound
     * 		Throw si aucune ville ne correspond
     */
    City searchExactPosition(Position position) throws CityNotFound;

    /**
     * Renvoit les City dont la position est à moins de 10km de la Position donnée
     * @param position
     * 		La Position donnée
     * @return
     * 		Les City à moins de 10km de position
     */
    CityManager searchNearCity(Position position);

    /**
     * Supprime toutes les City du CityManager
     */
    void clearCities();
}
