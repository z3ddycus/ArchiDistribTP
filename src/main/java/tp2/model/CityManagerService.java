package tp2.model;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface CityManagerService {
    boolean addCity(City city);
    boolean removeCity(City city);
    List<City> getCities();
    List<City> searchFor(String cityName);
    City searchExactPosition(Position position) throws CityNotFound;
    City searchNearCity(Position position) throws CityNotFound;

}
