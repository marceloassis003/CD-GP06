package clientToImageServiceStubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * serviço com operações sobre números
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class ClientToImageserverGrpc {

  private ClientToImageserverGrpc() {}

  public static final java.lang.String SERVICE_NAME = "clientToImageService.ClientToImageserver";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<clientToImageServiceStubs.Image,
      clientToImageServiceStubs.ImgID> getImageProcessingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "imageProcessing",
      requestType = clientToImageServiceStubs.Image.class,
      responseType = clientToImageServiceStubs.ImgID.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<clientToImageServiceStubs.Image,
      clientToImageServiceStubs.ImgID> getImageProcessingMethod() {
    io.grpc.MethodDescriptor<clientToImageServiceStubs.Image, clientToImageServiceStubs.ImgID> getImageProcessingMethod;
    if ((getImageProcessingMethod = ClientToImageserverGrpc.getImageProcessingMethod) == null) {
      synchronized (ClientToImageserverGrpc.class) {
        if ((getImageProcessingMethod = ClientToImageserverGrpc.getImageProcessingMethod) == null) {
          ClientToImageserverGrpc.getImageProcessingMethod = getImageProcessingMethod =
              io.grpc.MethodDescriptor.<clientToImageServiceStubs.Image, clientToImageServiceStubs.ImgID>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "imageProcessing"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.Image.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.ImgID.getDefaultInstance()))
              .setSchemaDescriptor(new ClientToImageserverMethodDescriptorSupplier("imageProcessing"))
              .build();
        }
      }
    }
    return getImageProcessingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID,
      clientToImageServiceStubs.Image> getDownloadImageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DownloadImage",
      requestType = clientToImageServiceStubs.ImgID.class,
      responseType = clientToImageServiceStubs.Image.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID,
      clientToImageServiceStubs.Image> getDownloadImageMethod() {
    io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID, clientToImageServiceStubs.Image> getDownloadImageMethod;
    if ((getDownloadImageMethod = ClientToImageserverGrpc.getDownloadImageMethod) == null) {
      synchronized (ClientToImageserverGrpc.class) {
        if ((getDownloadImageMethod = ClientToImageserverGrpc.getDownloadImageMethod) == null) {
          ClientToImageserverGrpc.getDownloadImageMethod = getDownloadImageMethod =
              io.grpc.MethodDescriptor.<clientToImageServiceStubs.ImgID, clientToImageServiceStubs.Image>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DownloadImage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.ImgID.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.Image.getDefaultInstance()))
              .setSchemaDescriptor(new ClientToImageserverMethodDescriptorSupplier("DownloadImage"))
              .build();
        }
      }
    }
    return getDownloadImageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID,
      clientToImageServiceStubs.StatusInfo> getConsultStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ConsultStatus",
      requestType = clientToImageServiceStubs.ImgID.class,
      responseType = clientToImageServiceStubs.StatusInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID,
      clientToImageServiceStubs.StatusInfo> getConsultStatusMethod() {
    io.grpc.MethodDescriptor<clientToImageServiceStubs.ImgID, clientToImageServiceStubs.StatusInfo> getConsultStatusMethod;
    if ((getConsultStatusMethod = ClientToImageserverGrpc.getConsultStatusMethod) == null) {
      synchronized (ClientToImageserverGrpc.class) {
        if ((getConsultStatusMethod = ClientToImageserverGrpc.getConsultStatusMethod) == null) {
          ClientToImageserverGrpc.getConsultStatusMethod = getConsultStatusMethod =
              io.grpc.MethodDescriptor.<clientToImageServiceStubs.ImgID, clientToImageServiceStubs.StatusInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ConsultStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.ImgID.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  clientToImageServiceStubs.StatusInfo.getDefaultInstance()))
              .setSchemaDescriptor(new ClientToImageserverMethodDescriptorSupplier("ConsultStatus"))
              .build();
        }
      }
    }
    return getConsultStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ClientToImageserverStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverStub>() {
        @java.lang.Override
        public ClientToImageserverStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToImageserverStub(channel, callOptions);
        }
      };
    return ClientToImageserverStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static ClientToImageserverBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverBlockingV2Stub>() {
        @java.lang.Override
        public ClientToImageserverBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToImageserverBlockingV2Stub(channel, callOptions);
        }
      };
    return ClientToImageserverBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ClientToImageserverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverBlockingStub>() {
        @java.lang.Override
        public ClientToImageserverBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToImageserverBlockingStub(channel, callOptions);
        }
      };
    return ClientToImageserverBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ClientToImageserverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ClientToImageserverFutureStub>() {
        @java.lang.Override
        public ClientToImageserverFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ClientToImageserverFutureStub(channel, callOptions);
        }
      };
    return ClientToImageserverFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<clientToImageServiceStubs.Image> imageProcessing(
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.ImgID> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getImageProcessingMethod(), responseObserver);
    }

    /**
     */
    default void downloadImage(clientToImageServiceStubs.ImgID request,
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.Image> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDownloadImageMethod(), responseObserver);
    }

    /**
     */
    default void consultStatus(clientToImageServiceStubs.ImgID request,
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.StatusInfo> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getConsultStatusMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ClientToImageserver.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static abstract class ClientToImageserverImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ClientToImageserverGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ClientToImageserver.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class ClientToImageserverStub
      extends io.grpc.stub.AbstractAsyncStub<ClientToImageserverStub> {
    private ClientToImageserverStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToImageserverStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToImageserverStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<clientToImageServiceStubs.Image> imageProcessing(
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.ImgID> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getImageProcessingMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void downloadImage(clientToImageServiceStubs.ImgID request,
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.Image> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getDownloadImageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void consultStatus(clientToImageServiceStubs.ImgID request,
        io.grpc.stub.StreamObserver<clientToImageServiceStubs.StatusInfo> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getConsultStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ClientToImageserver.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class ClientToImageserverBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<ClientToImageserverBlockingV2Stub> {
    private ClientToImageserverBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToImageserverBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToImageserverBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<clientToImageServiceStubs.Image, clientToImageServiceStubs.ImgID>
        imageProcessing() {
      return io.grpc.stub.ClientCalls.blockingClientStreamingCall(
          getChannel(), getImageProcessingMethod(), getCallOptions());
    }

    /**
     */
    @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/10918")
    public io.grpc.stub.BlockingClientCall<?, clientToImageServiceStubs.Image>
        downloadImage(clientToImageServiceStubs.ImgID request) {
      return io.grpc.stub.ClientCalls.blockingV2ServerStreamingCall(
          getChannel(), getDownloadImageMethod(), getCallOptions(), request);
    }

    /**
     */
    public clientToImageServiceStubs.StatusInfo consultStatus(clientToImageServiceStubs.ImgID request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getConsultStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service ClientToImageserver.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class ClientToImageserverBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ClientToImageserverBlockingStub> {
    private ClientToImageserverBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToImageserverBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToImageserverBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<clientToImageServiceStubs.Image> downloadImage(
        clientToImageServiceStubs.ImgID request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getDownloadImageMethod(), getCallOptions(), request);
    }

    /**
     */
    public clientToImageServiceStubs.StatusInfo consultStatus(clientToImageServiceStubs.ImgID request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getConsultStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ClientToImageserver.
   * <pre>
   * serviço com operações sobre números
   * </pre>
   */
  public static final class ClientToImageserverFutureStub
      extends io.grpc.stub.AbstractFutureStub<ClientToImageserverFutureStub> {
    private ClientToImageserverFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ClientToImageserverFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ClientToImageserverFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<clientToImageServiceStubs.StatusInfo> consultStatus(
        clientToImageServiceStubs.ImgID request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getConsultStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DOWNLOAD_IMAGE = 0;
  private static final int METHODID_CONSULT_STATUS = 1;
  private static final int METHODID_IMAGE_PROCESSING = 2;

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
        case METHODID_DOWNLOAD_IMAGE:
          serviceImpl.downloadImage((clientToImageServiceStubs.ImgID) request,
              (io.grpc.stub.StreamObserver<clientToImageServiceStubs.Image>) responseObserver);
          break;
        case METHODID_CONSULT_STATUS:
          serviceImpl.consultStatus((clientToImageServiceStubs.ImgID) request,
              (io.grpc.stub.StreamObserver<clientToImageServiceStubs.StatusInfo>) responseObserver);
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
        case METHODID_IMAGE_PROCESSING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.imageProcessing(
              (io.grpc.stub.StreamObserver<clientToImageServiceStubs.ImgID>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getImageProcessingMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              clientToImageServiceStubs.Image,
              clientToImageServiceStubs.ImgID>(
                service, METHODID_IMAGE_PROCESSING)))
        .addMethod(
          getDownloadImageMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              clientToImageServiceStubs.ImgID,
              clientToImageServiceStubs.Image>(
                service, METHODID_DOWNLOAD_IMAGE)))
        .addMethod(
          getConsultStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              clientToImageServiceStubs.ImgID,
              clientToImageServiceStubs.StatusInfo>(
                service, METHODID_CONSULT_STATUS)))
        .build();
  }

  private static abstract class ClientToImageserverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ClientToImageserverBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return clientToImageServiceStubs.ClientToImageServerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ClientToImageserver");
    }
  }

  private static final class ClientToImageserverFileDescriptorSupplier
      extends ClientToImageserverBaseDescriptorSupplier {
    ClientToImageserverFileDescriptorSupplier() {}
  }

  private static final class ClientToImageserverMethodDescriptorSupplier
      extends ClientToImageserverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ClientToImageserverMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ClientToImageserverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ClientToImageserverFileDescriptorSupplier())
              .addMethod(getImageProcessingMethod())
              .addMethod(getDownloadImageMethod())
              .addMethod(getConsultStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
