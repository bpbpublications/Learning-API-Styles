package com.apistyle.pbprestapi.servcies.grpc;

import com.example.grpc.HelloRequest;
import com.example.grpc.HelloResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

// Annotate the class as a gRPC service
@GrpcService
public class HelloServiceImpl {

    // Override the sayHello method
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        // Create a greeting message using the name from the request
        String greeting = "Hello, " + request.getName() + "!";

        // Build the response object
        HelloResponse response = HelloResponse.newBuilder()
                .setMessage(greeting) // Set the message field
                .build();

        // Send the response back to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted(); // Mark the response as complete
    }
}
