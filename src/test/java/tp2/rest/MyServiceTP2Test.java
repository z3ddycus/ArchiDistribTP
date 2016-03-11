package tp2.rest;

import org.junit.Before;
import org.junit.Test;
import tp2.model.City;
import tp2.model.CityNotFound;
import tp2.model.Position;
import tp2.model.CityManagerService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * Created by zeddycus on 11/03/16.
 */
public class MyServiceTP2Test {
    private final QName SERVICE_NAME = new QName("http://model.tp2/", "CityManagerService");
    private final QName PORT_NAME = new QName("http://model.tp2/",  "CityManagerPort");
    private CityManagerService cityManager = null;

    @Before public void initialize() throws MalformedURLException {
        URL wsdlURL = null;
        wsdlURL = new URL("http://127.0.0.1:8084/citymanager?wsdl");
        Service service = Service.create(wsdlURL, SERVICE_NAME);
        cityManager = service.getPort(PORT_NAME, CityManagerService.class);
    }

    @Test
    public void testAddAndRemove() {
        City c = new City("Zanarkand", 16, 64, "Hyrule");
        cityManager.addCity(c);
        assertTrue(cityManager.getCities().contains(c));
        cityManager.removeCity(c);
        System.out.println(cityManager.getCities());
        assertFalse(cityManager.getCities().contains(c));
    }

    @Test
    public void testSearchExactPosition() {
        City c = new City("Zanarkand", 16, 64, "Hyrule");
        cityManager.addCity(c);
        try {
            City found = cityManager.searchExactPosition(new Position(16, 64));
            assertEquals(c, found);
        } catch (CityNotFound cityNotFound) {
            fail();
        }
    }

    @Test
    public void testSearchNearPosition() {
        City c = new City("Zanarkand", 16, 64, "Hyrule");
        cityManager.addCity(c);
        try {
            City found = cityManager.searchNearCity(new Position(16.002, 64.001));
            assertEquals(c, found);
        } catch (CityNotFound cityNotFound) {
            fail();
        }
    }

}