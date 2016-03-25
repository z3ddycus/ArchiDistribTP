package fr.rest;

import fr.model.City;
import fr.model.CityManagerService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class MyClient {
	private static final QName SERVICE_NAME = new QName("http://model.fr/", "CityManagerService");
	private static final QName PORT_NAME = new QName("http://model.fr/",  "CityManagerPort");

	public static void main(String args[]) throws MalformedURLException {
		URL wsdlURL = new URL("http://127.0.0.1:8084/citymanager?wsdl");
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		CityManagerService cityManager = service.getPort(PORT_NAME, CityManagerService.class);
	}
}
