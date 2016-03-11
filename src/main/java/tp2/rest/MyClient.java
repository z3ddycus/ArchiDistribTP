package tp2.rest;

import tp2.model.CityManagerService;
import tp2.model.City;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class MyClient {
	private static final QName SERVICE_NAME = new QName("http://model.tp2/", "CityManagerService");
	private static final QName PORT_NAME = new QName("http://model.tp2/",  "CityManagerPort");

	public static void main(String args[]) throws MalformedURLException {
		URL wsdlURL = new URL("http://127.0.0.1:8084/citymanager?wsdl");
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		CityManagerService cityManager = service.getPort(PORT_NAME, CityManagerService.class);

		// question2
		System.out.println(cityManager.getCities());
		City c = new City("Zanarkand", 16, 64, "Hyrule");
		cityManager.addCity(c);
		cityManager.removeCity(c);
		System.out.println(cityManager.getCities());
	}
}
