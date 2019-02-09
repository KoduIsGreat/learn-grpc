package com.google.client;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class GreetServer {

  private static final Logger LOG = LoggerFactory.getLogger(GreetServer.class);
  private final int port = 5000;
  private Server server;
  private BindableService greeterService;

  @Activate
  public void activate() {
    start();
  }

  private void start() {
    try {
      server = NettyServerBuilder
          .forPort(port)
          .addService(greeterService)
          .build()
          .start();
      LOG.info("Server started, listening on {}", port);
      System.out.println("hi!");
      CompletableFuture.runAsync(() -> {
        try {
          System.out.println("we going!");
          server.awaitTermination();
          System.out.println("oh we died");
        } catch (InterruptedException ex) {
          LOG.error(ex.getMessage(), ex);
        }
      });
    } catch (IOException ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

  @Reference
  public void setGreeterService(BindableService greeterService) {
    this.greeterService = greeterService;
  }

  @Deactivate
  public void deactivate() {
    if (server != null) {
      server.shutdown();
    }
  }

}
