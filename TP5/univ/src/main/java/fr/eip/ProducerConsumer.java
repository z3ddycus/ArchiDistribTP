package fr.eip;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.BasicConfigurator;

import java.util.Scanner;

import static org.apache.camel.model.rest.RestParamType.body;

/**
 * A producerConsumer.
 */
public class ProducerConsumer {
    /**
     * The key word.
     */
    public static final String KEY_WORD = "ADFTW";

    /**
     * The main.
     */
    public static void main(String[] args) throws Exception {
        //Configure le logger par défaut
        BasicConfigurator.configure();

        //Contexte Camel par défaut
        CamelContext context = new DefaultCamelContext();

        //Crée une route contenant le consommateur
        RouteBuilder routeBuilder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                // On définit un consommateur 'consumer-1'
                //qui va écrire le message
                from("jgroups:m1gil").filter(exchange -> exchange.getIn().getBody().toString().contains(KEY_WORD))
                        .log("Le mot clé est " + KEY_WORD);
                from("direct:consumer-1").to("log:affiche-1-log");
                from("direct:consumer-2").to("file:messages");
                from("direct:geonames")
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .setHeader(Exchange.HTTP_QUERY, simple("q=${in.headers.geonames}"))
                        .setHeader(Exchange.HTTP_QUERY, constant("country="))
                        .setHeader(Exchange.HTTP_QUERY, constant("maxRows=1"))
                        .setHeader(Exchange.HTTP_QUERY, constant("username=m1gil"))
                        .to("http://api.geonames.org/search?")
                        .log("reponse received : ${body}");
                from("direct:citymanager")
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .setHeader(Exchange.HTTP_QUERY, simple("ville=${in.headers.ville}"))
                        .to("http://127.0.0.1:8084/")
                        .log("reponse received : ${body}");
                from("direct:consumer-all")
                        .choice()
                            .when(header("destinataire").isEqualTo("écrire"))
                                .to("direct:consumer-2")
                            .when(header("geonames").isNotNull())
                                .to("direct:geonames")
                            .when(header("ville").isNotNull())
                                .to("direct:citymanager")
                            .otherwise()
                                .to("direct:consumer-1");
            }
        };

        //On ajoute la route au contexte
        routeBuilder.addRoutesToCamelContext(context);

        //On démarre le contexte pour activer les routes
        context.start();

        //On crée un producteur
        ProducerTemplate pt = context.createProducerTemplate();
        //qui envoie un message au consommateur 'consumer-1'


        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String read = sc.nextLine();
            System.out.println("\tRead : " + read);
            if (read.equals("exit")) {
                context.stop();
                pt.stop();
                System.exit(1);
            } else if (read.length() > 0 && read.charAt(0) == 'w') {
                pt.sendBodyAndHeader("direct:consumer-all", read, "destinataire", "écrire");
            } else if (read.startsWith("ville")) {
                pt.sendBodyAndHeader("direct:consumer-all", read.substring(5).trim(), "ville", read.substring(5).trim());
            } else if (read.startsWith("geonames")) {
                pt.sendBodyAndHeader("direct:consumer-all", read.substring(8).trim(), "geonames", read.substring(8).trim());
            } else {
                pt.sendBody("direct:consumer-all", read);
            }
        }
    }
}
