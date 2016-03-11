package tp1.rest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import tp1.model.City;
import tp1.model.CityManager;
import tp1.model.CityNotFound;
import tp1.model.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServiceProvider
                   
@ServiceMode(value=Service.Mode.MESSAGE)
public class MyServiceTP implements Provider<Source> {
	
	/**
	 * Gère les villes
	 */
	private CityManager cityManager = new CityManager();
	/**
	 * Le JAXBContext
	 */
	private JAXBContext jc;
	
	@javax.annotation.Resource(type=Object.class)
	protected WebServiceContext wsContext;
	
	public MyServiceTP(){
		try {
            jc = JAXBContext.newInstance(CityManager.class,City.class,Position.class, CityNotFound.class);
            
        } catch(JAXBException je) {
            System.out.println("Exception " + je);
            throw new WebServiceException("Cannot create JAXBContext", je);
        }
	}

	/**
	 * Methode permettant d'appeler les methodes du service depuis le client.
	 * @param source
	 * La Source du message.
	 * @return Le resultat d'une des methodes get, put, post, et delete.
	 */
    public Source invoke(Source source) {
    	
        try{
            MessageContext mc = wsContext.getMessageContext();
            String path = (String)mc.get(MessageContext.PATH_INFO);
            String method = (String)mc.get(MessageContext.HTTP_REQUEST_METHOD);
            System.out.println("Got HTTP "+method+" request for "+path);
		    if (method.equals("GET")) 
	                return get(mc);
			if (method.equals("POST")) 
				    return post(source, mc);
           	if (method.equals("PUT")) 
					return put(source, mc); 
           	if (method.equals("DELETE")) 
					return delete(source, mc); 
			throw new WebServiceException("Unsupported method:" +method);  
        } catch(JAXBException je) {
            throw new WebServiceException(je);
        }
    }

	/**
	 * Permet d'ajouter une ville.
	 * @param source
	 * La Source du message.
	 * @param mc
	 * Le MessageContext du message.
	 * @return
	 * un JAXBSource contenant la ville ajoutee.
	 */
	private Source put(Source source, MessageContext mc) throws JAXBException {
		// * ajouter une ville passée en paramètre au citymanager

		Unmarshaller u = jc.createUnmarshaller();
		City city = (City)u.unmarshal(source);
		if (!cityManager.getCities().contains(city)) {
			cityManager.addCity(city);
		}
		return new JAXBSource(jc, city);
	}

	/**
	 * Permet de supprimer une ville.
	 * @param source
	 * La Source du message.
	 * @param mc
	 * Le MessageContext du message.
	 * @return
	 * un JAXBSource contenant un CityManager qui contient les City supprimees.
	 */
	private Source delete(Source source, MessageContext mc) throws JAXBException {
		String path = (String) mc.get(MessageContext.PATH_INFO);
		if (path == null) path = "";

		Map<String,String> params = extractParameters((String) mc.get(MessageContext.QUERY_STRING));
		CityManager result = new CityManager();
		if (path.equals("all")) {
			cityManager.getCities().forEach(result::addCity);
		} else if (params.containsKey("ville")) {
			List<City> citiesToDelete = cityManager.searchFor(params.get("ville"));
			citiesToDelete.forEach(result::addCity);
		} else if (params.containsKey("position")) {
			String[] split = params.get("position").split(";");
			if (split.length >= 2) {
				Position p = new Position(Double.valueOf(split[0]),Double.valueOf(split[1]));
				cityManager.getCities().stream().filter(city -> city.getPosition().equals(p)).forEach(result::addCity);
			}

		}
		for (City city : result.getCities()) {
			cityManager.removeCity(city);
		}
		return new JAXBSource(jc, result);
	}

	/**
	 * Permet de recuperer une ville a partir des coordonnees passees en parametre, ou recupere une ville proche de ces coordonnees.
	 * @param source
	 * La Source du message.
	 * @param mc
	 * Le MessageContext du message.
	 * @return
	 * un JAXBSource contenant le message retourné.
	 */
	private Source post(Source source, MessageContext mc) throws JAXBException {
		Unmarshaller u = jc.createUnmarshaller();
		Position position=(Position)u.unmarshal(source);
		String path = (String) mc.get(MessageContext.PATH_INFO);
		if (path == null) path = "";
		Object message;
		try {
			if (path.contains("near")) {
				// * rechercher les villes proches de cette position si l'url de post contient le mot clé "near"
				message = cityManager.searchNearCity(position);
			} else {
				// * rechercher une ville à partir de sa position
				message = cityManager.searchExactPosition(position);
			}
		} catch (CityNotFound cnf) {
			message = new CityManager();
		}
		return new JAXBSource(jc, message);
	}

	/**
	 * Renvoie la liste de toutes les villes, ou de ville(s) en particulier dont le nom est passe en parametre.
	 * @param mc
	 * Le MessageContext du message.
	 * @return un JAXBSource contenant un CityManager qui contient les villes retrouves.
	 */
	private Source get(MessageContext mc) throws JAXBException {
		String path = (String) mc.get(MessageContext.PATH_INFO);
		if (path == null) path = "";

		if (path.equals("all")) {
			// * retourner tous les villes seulement si le chemin d'accès est "all"
			return new JAXBSource(jc, cityManager);
		} else {
			// * retourner seulement la ville dont le nom est contenu dans l'url d'appel
			String params = (String) mc.get(MessageContext.QUERY_STRING);
			Map<String, String> parameters = extractParameters(params);
			CityManager result = new CityManager();
			if (parameters.containsKey("ville")) {
				String name = parameters.get("ville");
				cityManager.searchFor(name).forEach(result::addCity);
			}
			return new JAXBSource(jc, result);
		}
	}

	/**
	 * Permet d'extraire les parametres depuis une URL.
	 * @param params
	 * URL passee en parametre.
	 * @return
	 * les parametres extraits.
	 */
	private Map<String, String> extractParameters (String params) {
		Map<String, String> parameters = new HashMap<>();
		if (params != null) {
			for (String couple : params.split("[&]")) {
				String[] split = couple.split("[=]");
				parameters.put(split[0], split[1]);
			}
		}
		return parameters;
	}

	/**
	 * Point d'entrée
     */
	public static void main(String args[]) {
	      Endpoint e = Endpoint.create( HTTPBinding.HTTP_BINDING,
	                                     new MyServiceTP());
	      e.publish("http://127.0.0.1:8084/");
	       // pour arrêter : e.stop();
	 }
}
