package com.google;

import com.google.client.GreetServer;
import com.google.client.GreeterService;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
      GreetServer server = new GreetServer();
      GreeterService service = new GreeterService();
      server.setGreeterService(service);
      server.activate();
    }

}
