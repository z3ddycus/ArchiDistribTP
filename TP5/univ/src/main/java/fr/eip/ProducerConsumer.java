package fr.eip;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.log4j.BasicConfigurator;

import java.util.Scanner;

public class ProducerConsumer {

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
                from("direct:consumer-1").to("log:affiche-1-log");
                from("direct:consumer-2").to("file:messages");
                from("direct:consumer-all")
                        .choice()
                            .when(header("destinataire").isEqualTo("écrire"))
                                .to("direct:consumer-2")
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
            String read = sc.next();
            if (read.equals("exit")) {
                System.exit(1);
            }
            if (read.length() > 0 && read.charAt(0) == 'w') {
                pt.sendBodyAndHeader("direct:consumer-all", read, "destinataire", "écrire");
            }
            pt.sendBody("direct:consumer-all", read);
        }
    }
}
