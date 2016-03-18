package tp2.rest;

import tp2.model.CityManager;
import javax.xml.ws.*;

public class MyServiceTP  {
	public MyServiceTP() {
		CityManager cityManager = new CityManager();
		// ajout des villes d'initialisation
		Endpoint.publish("http://127.0.0.1:8084/citymanager", cityManager);
	}

	public static void main(String args[]) throws InterruptedException {
		new MyServiceTP();
		Thread.sleep(15 * 60 * 1000);
		System.exit(0);
	 }
}
