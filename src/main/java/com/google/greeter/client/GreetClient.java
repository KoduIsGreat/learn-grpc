package com.google.greeter.client;

import com.google.GreetMessage;
import com.google.GreeterGrpc;
import com.google.Greeting;
import com.google.greeter.server.GreetServer;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
@Component(immediate = true)
public class GreetClient {
  private final String host = "localhost";

  private final int port = 5000;
  private ManagedChannel channel;
  private GreeterGrpc.GreeterBlockingStub blockingStub;


  private static  final Logger LOG = LoggerFactory.getLogger(GreetClient.class);

  @Activate
  public void activate() {
    channel = NettyChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build();
    blockingStub = GreeterGrpc.newBlockingStub(channel);
    try {
      greet("adahm", "{0} cant believe its butter!");
      shutdown();
    } catch (InterruptedException ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  /**
   * Say hello to server.
   */
  public void greet(String name, String message) {
    LOG.info("Will try to greet " + name + " ...");
    System.out.println("Will try to greet " + name + " ...");
    Greeting request = Greeting.newBuilder()
        .setName(name)
        .setMessage(message)
        .build();
    GreetMessage response;
    try {
      response = blockingStub.greet(request);
    } catch (StatusRuntimeException e) {
      LOG.warn("RPC failed: {0}", e.getStatus());
      System.out.println("RPC failed: "+ e.getStatus());
      return;
    }
    LOG.info("Greeting: " + response.getMsg());
    System.out.println("response from server Greeting : " + response.getMsg());
  }

  @Reference
  public void setGrpcServer(GreetServer grpcServer) {
    //ensures the server has started before we attempt to connect
  }
}
