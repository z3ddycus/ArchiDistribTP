package tp1.rest;

import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import tp1.model.City;
import tp1.model.CityManager;
import tp1.model.Position;

public class MyClient {
	/**
	 * Le service
	 */
	private Service service;
	/**
	 * Le JAXBContext
	 */
	private JAXBContext jc;
	/**
	 * Le QName
	 */
	private static final QName qname = new QName("", "");
	/**
	 * L'url du service
	 */
	private static final String url = "http://127.0.0.1:8084";

	/**
	 * Un nouveau client
	 */
	public MyClient() {
		try {
			jc = JAXBContext.newInstance(CityManager.class, City.class,
					Position.class);
		} catch (JAXBException je) {
			System.out.println("Cannot create JAXBContext " + je);
		}
	}

	/**
	 * Une requetes de mode "mode" d'url "url"
	 * @param url
	 * 		L'url
	 * @param mode
	 * 		Le type de requetes
     */
	public void handleUrl(String url, String mode) throws JAXBException {
		service = Service.create(qname);
		service.addPort(qname, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(qname,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, mode);
		Source result = dispatcher.invoke(new JAXBSource(jc, new Position(12,43)));
		printSource(result);
	}

	/**
	 * Envoie une requete pour ajouter city
	 * @param city
	 * 		La ville a rajoutée
     */
	public void addCity(City city) throws JAXBException {
		service = Service.create(qname);
		service.addPort(qname, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(qname,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "PUT");
		Source result = dispatcher.invoke(new JAXBSource(jc, city));
		printSource(result);
	}

	/**
	 * Envoie une requete pour rechercher une ville
	 * @param url
	 * 		l'url
	 * @param position
	 * 		la position de la ville
     */
	public void searchForCity(String url, Position position) throws JAXBException {
		service = Service.create(qname);
		service.addPort(qname, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(qname,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, "POST");
		Source result = dispatcher.invoke(new JAXBSource(jc, position));
		printSource(result);
	}

	/**
	 * Affichage de la source
	 * @param s
	 * 		La source à afficher
     */
	public void printSource(Source s) {
		try {
			System.out.println("============================= Response Received =========================================");
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.transform(s, new StreamResult(System.out));
			System.out.println("\n======================================================================");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Point d'entrée
     */
	public static void main(String args[]) throws Exception {
		MyClient client = new MyClient();

		System.out.println("Affichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");

		System.out.println("\nSuppression de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "DELETE");

		System.out.println("\nAffichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");

		System.out.println("\nAjout de Rouen, 49.443889, 1.103333, France");
		client.addCity(new City("Rouen", 49.443889, 1.103333, "FR"));

		System.out.println("\nAjout de Mogadiscio, 2.333333, 48.85, SO");
		client.addCity(new City("Mogadiscio", 2.333333, 48.85, "SO"));

		System.out.println("\nAjout de Rouen, 49.443889, 1.103333, France");
		client.addCity(new City("Rouen", 49.443889, 1.103333, "FR"));

		System.out.println("\nAjout de Bihorel, 49.455278, 1.116944, France");
		client.addCity(new City("Bihorel", 49.455278, 1.116944, "FR"));

		System.out.println("\nAjout de Londres, 51.504872, -0.07857, UnitedKingdom");
		client.addCity(new City("Londres", 51.504872, -0.07857, "UK"));

		System.out.println("\nAjout de Paris, 48.856578, 2.351828, France");
		client.addCity(new City("Paris", 48.856578, 2.351828, "FR"));

		System.out.println("\nAjout de Paris, 43.2, -80.38333, Canada");
		client.addCity(new City("Paris", 43.2, -80.38333, "CA"));

		System.out.println("\nAffichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");

		System.out.println("\nAjout de Villers-Bocage, 49.083333, -0.65, France");
		client.addCity(new City("Villers-Bocage", 49.083333, -0.65, "FR"));

		System.out.println("\nAjout de Villers-Bocage, 50.021858, 2.326126, France");
		client.addCity(new City("Villers-Bocage", 50.021858, 2.326126, "FR"));

		System.out.println("\nAffichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");

		System.out.println("\nSuppression de 49.083333;-0.65");
		client.handleUrl("http://127.0.0.1:8084/?position=49.083333;-0.65", "DELETE");

		System.out.println("\nAffichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");

		System.out.println("\nSuppression de 51.504872;-0.07857");
		client.handleUrl("http://127.0.0.1:8084/?position=51.504872;-0.07857", "DELETE");

		System.out.println("\nSuppression de 51.504872;-0.07857");
		client.handleUrl("http://127.0.0.1:8084/?position=51.504872;-0.07857", "DELETE");

		System.out.println("\nRecherche d'une ville en 49.443889;1.103333");
		client.searchForCity("http://127.0.0.1:8084/", new Position(49.443889, 1.103333));

		System.out.println("\nRecherche d'une ville en 49.083333;-0.65");
		client.searchForCity("http://127.0.0.1:8084/", new Position(49.083333, -0.65));

		System.out.println("\nRecherche d'une ville en 43.2;-80.38");
		client.searchForCity("http://127.0.0.1:8084/", new Position(43.2, -80.38));

		System.out.println("\nRecherche d'une ville proche de 48.85;2.34");
		client.searchForCity("http://127.0.0.1:8084/near", new Position(48.85, 2.34));

		System.out.println("\nRecherche d'une ville proche de 42;64");
		client.searchForCity("http://127.0.0.1:8084/near", new Position(42, 64));

		System.out.println("\nRecherche d'une ville proche de 49.45;1.11");
		client.searchForCity("http://127.0.0.1:8084/near", new Position(49.45, 1.11));

		System.out.println("\nAffichage des villes nommées Mogadiscio");
		client.handleUrl("http://127.0.0.1:8084/?ville=Mogadiscio", "GET");

		System.out.println("\nAffichage des villes nommées Paris");
		client.handleUrl("http://127.0.0.1:8084/?ville=Paris", "GET");

		System.out.println("\nAffichage des villes nommées Hyrule");
		client.handleUrl("http://127.0.0.1:8084/?ville=Hyrule", "GET");

		System.out.println("\nSuppression de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "DELETE");

		System.out.println("\nAffichage de toutes les villes");
		client.handleUrl("http://127.0.0.1:8084/all", "GET");
	}
}
