
package fr;

import javax.jws.WebService;

@WebService(endpointInterface = "fr.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

    public String sayHi(String text) {
        return "Hello " + text;
    }
}

