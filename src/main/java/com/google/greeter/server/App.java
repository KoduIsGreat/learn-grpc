package com.google.greeter.server;

import com.google.greeter.client.GreetClient;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
      GreetClient client = new GreetClient();
      GreetServer server = new GreetServer();
      GreeterService service = new GreeterService();
      server.setGreeterService(service);
      server.activate();

      client.setGrpcServer(server);
      client.activate();
    }

}
