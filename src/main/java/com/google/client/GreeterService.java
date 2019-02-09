package com.google.client;

import com.google.GreetMessage;
import com.google.GreeterGrpc;
import com.google.Greeting;
import io.grpc.BindableService;
import io.grpc.stub.StreamObserver;
import java.text.MessageFormat;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class GreeterService extends GreeterGrpc.GreeterImplBase implements BindableService {

  private static final Logger LOG = LoggerFactory.getLogger(GreeterService.class);
  @Override
  public void greet(Greeting request, StreamObserver<GreetMessage> responseObserver) {
    LOG.info("sayHello endpoint received request from " + request.getName());
    String fmtMsg = MessageFormat.format(request.getMessage(),request.getName());
    GreetMessage reply = GreetMessage.newBuilder()
        .setMsg(fmtMsg).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }
}
