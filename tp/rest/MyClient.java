package tp.rest;

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

import tp.model.City;
import tp.model.CityManager;
import tp.model.Position;

public class MyClient {
	private Service service;
	private JAXBContext jc;

	private static final QName qname = new QName("", "");
	private static final String url = "http://127.0.0.1:8084";

	public MyClient() {
		try {
			jc = JAXBContext.newInstance(CityManager.class, City.class,
					Position.class);
		} catch (JAXBException je) {
			System.out.println("Cannot create JAXBContext " + je);
		}
	}

	public void addCity(City city) throws JAXBException {
		launchRequestHTTP("PUT", new JAXBSource(jc, city));
	}

	public void searchForCity(Position position) throws JAXBException {
		launchRequestHTTP("POST", new JAXBSource(jc, position));
	}

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

	public static void main(String args[]) throws Exception {
		MyClient client = new MyClient();
		client.addCity(new City("Rabat", 34.01, -6.83, "Maroc"));
		client.searchForCity(new Position(34.01, -6.83));
	}

	// TOOLS

	private void launchRequestHTTP(String method, JAXBSource argument) {
		service = Service.create(qname);
		service.addPort(qname, HTTPBinding.HTTP_BINDING, url);
		Dispatch<Source> dispatcher = service.createDispatch(qname,
				Source.class, Service.Mode.MESSAGE);
		Map<String, Object> requestContext = dispatcher.getRequestContext();
		requestContext.put(MessageContext.HTTP_REQUEST_METHOD, method);
		Source result = dispatcher.invoke(argument);
		printSource(result);
	}
}
