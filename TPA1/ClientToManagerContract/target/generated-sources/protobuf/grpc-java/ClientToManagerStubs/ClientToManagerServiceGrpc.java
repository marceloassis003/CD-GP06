package ClientToManagerStubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class ClientToManagerServiceGrpc {

  private ClientToManagerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "clientToManager.ClientToManagerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ClientToManagerStubs.ImageInfo> getGetServerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetServer",
      requestType = com.google.protobuf.Empty.class,
      responseType = ClientToManagerStubs.ImageInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ClientToManagerStubs.ImageInfo> getGetServerMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ClientToManagerStubs.ImageInfo> getGetServerMethod;
    if ((getGetServerMethod = ClientToManagerServiceGrpc.getGetServerMethod) == null) {
      synchronized (ClientToManagerServiceGrpc.class) {
        if ((getGetServerMethod = ClientToManagerServiceGrpc.getGetServerMethod) == null) {
          ClientToManagerServiceGrpc.getGetServerMethod = getGetServerMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ClientToManagerStubs.ImageInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetServer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ClientToManagerStubs.ImageInfo.getDefaultInstance()))
              .setSchemaDescriptor(new ClientToManagerServiceMethodDescriptorSupplier("GetServer"))
              .build();
        }
      }
    }
    return getGetServerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ClientToManagerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceStub>() {
        @java.lang.Override
        public ClientToManagerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToManagerServiceStub(channel, callOptions);
        }
      };
    return ClientToManagerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static ClientToManagerServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceBlockingV2Stub>() {
        @java.lang.Override
        public ClientToManagerServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToManagerServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return ClientToManagerServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ClientToManagerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceBlockingStub>() {
        @java.lang.Override
        public ClientToManagerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToManagerServiceBlockingStub(channel, callOptions);
        }
      };
    return ClientToManagerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ClientToManagerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToManagerServiceFutureStub>() {
        @java.lang.Override
        public ClientToManagerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToManagerServiceFutureStub(channel, callOptions);
        }
      };
    return ClientToManagerServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getServer(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ClientToManagerStubs.ImageInfo> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetServerMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ClientToManagerService.
   */
  public static abstract class ClientToManagerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ClientToManagerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ClientToManagerService.
   */
  public static final class ClientToManagerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ClientToManagerServiceStub> {
    private ClientToManagerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToManagerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToManagerServiceStub(channel, callOptions);
    }

    /**
     */
    public void getServer(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ClientToManagerStubs.ImageInfo> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetServerMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ClientToManagerService.
   */
  public static final class ClientToManagerServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<ClientToManagerServiceBlockingV2Stub> {
    private ClientToManagerServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToManagerServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToManagerServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public ClientToManagerStubs.ImageInfo getServer(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetServerMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service ClientToManagerService.
   */
  public static final class ClientToManagerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ClientToManagerServiceBlockingStub> {
    private ClientToManagerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToManagerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToManagerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ClientToManagerStubs.ImageInfo getServer(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetServerMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ClientToManagerService.
   */
  public static final class ClientToManagerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ClientToManagerServiceFutureStub> {
    private ClientToManagerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToManagerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToManagerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ClientToManagerStubs.ImageInfo> getServer(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetServerMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SERVER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SERVER:
          serviceImpl.getServer((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ClientToManagerStubs.ImageInfo>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetServerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ClientToManagerStubs.ImageInfo>(
                service, METHODID_GET_SERVER)))
        .build();
  }

  private static abstract class ClientToManagerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ClientToManagerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ClientToManagerStubs.ClientToManagerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ClientToManagerService");
    }
  }

  private static final class ClientToManagerServiceFileDescriptorSupplier
      extends ClientToManagerServiceBaseDescriptorSupplier {
    ClientToManagerServiceFileDescriptorSupplier() {}
  }

  private static final class ClientToManagerServiceMethodDescriptorSupplier
      extends ClientToManagerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ClientToManagerServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ClientToManagerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ClientToManagerServiceFileDescriptorSupplier())
              .addMethod(getGetServerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
