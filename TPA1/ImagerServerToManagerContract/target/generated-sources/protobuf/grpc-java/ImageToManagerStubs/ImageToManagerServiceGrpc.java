package ImageToManagerStubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 *package
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.0)",
    comments = "Source: ImageToManager.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ImageToManagerServiceGrpc {

  private ImageToManagerServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "imageToManagerService.ImageToManagerService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ImageToManagerStubs.ImageRegistInfo,
      ImageToManagerStubs.ImageInfo> getRegistServerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegistServer",
      requestType = ImageToManagerStubs.ImageRegistInfo.class,
      responseType = ImageToManagerStubs.ImageInfo.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ImageToManagerStubs.ImageRegistInfo,
      ImageToManagerStubs.ImageInfo> getRegistServerMethod() {
    io.grpc.MethodDescriptor<ImageToManagerStubs.ImageRegistInfo, ImageToManagerStubs.ImageInfo> getRegistServerMethod;
    if ((getRegistServerMethod = ImageToManagerServiceGrpc.getRegistServerMethod) == null) {
      synchronized (ImageToManagerServiceGrpc.class) {
        if ((getRegistServerMethod = ImageToManagerServiceGrpc.getRegistServerMethod) == null) {
          ImageToManagerServiceGrpc.getRegistServerMethod = getRegistServerMethod =
              io.grpc.MethodDescriptor.<ImageToManagerStubs.ImageRegistInfo, ImageToManagerStubs.ImageInfo>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegistServer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ImageToManagerStubs.ImageRegistInfo.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ImageToManagerStubs.ImageInfo.getDefaultInstance()))
              .setSchemaDescriptor(new ImageToManagerServiceMethodDescriptorSupplier("RegistServer"))
              .build();
        }
      }
    }
    return getRegistServerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ImageToManagerServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceStub>() {
        @java.lang.Override
        public ImageToManagerServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageToManagerServiceStub(channel, callOptions);
        }
      };
    return ImageToManagerServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ImageToManagerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceBlockingStub>() {
        @java.lang.Override
        public ImageToManagerServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageToManagerServiceBlockingStub(channel, callOptions);
        }
      };
    return ImageToManagerServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ImageToManagerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ImageToManagerServiceFutureStub>() {
        @java.lang.Override
        public ImageToManagerServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ImageToManagerServiceFutureStub(channel, callOptions);
        }
      };
    return ImageToManagerServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *package
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     *GetServer
     * </pre>
     */
    default void registServer(ImageToManagerStubs.ImageRegistInfo request,
        io.grpc.stub.StreamObserver<ImageToManagerStubs.ImageInfo> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegistServerMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ImageToManagerService.
   * <pre>
   *package
   * </pre>
   */
  public static abstract class ImageToManagerServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ImageToManagerServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ImageToManagerService.
   * <pre>
   *package
   * </pre>
   */
  public static final class ImageToManagerServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ImageToManagerServiceStub> {
    private ImageToManagerServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageToManagerServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageToManagerServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     *GetServer
     * </pre>
     */
    public void registServer(ImageToManagerStubs.ImageRegistInfo request,
        io.grpc.stub.StreamObserver<ImageToManagerStubs.ImageInfo> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegistServerMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ImageToManagerService.
   * <pre>
   *package
   * </pre>
   */
  public static final class ImageToManagerServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ImageToManagerServiceBlockingStub> {
    private ImageToManagerServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageToManagerServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageToManagerServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *GetServer
     * </pre>
     */
    public ImageToManagerStubs.ImageInfo registServer(ImageToManagerStubs.ImageRegistInfo request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegistServerMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ImageToManagerService.
   * <pre>
   *package
   * </pre>
   */
  public static final class ImageToManagerServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ImageToManagerServiceFutureStub> {
    private ImageToManagerServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ImageToManagerServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ImageToManagerServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *GetServer
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ImageToManagerStubs.ImageInfo> registServer(
        ImageToManagerStubs.ImageRegistInfo request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegistServerMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGIST_SERVER = 0;

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
        case METHODID_REGIST_SERVER:
          serviceImpl.registServer((ImageToManagerStubs.ImageRegistInfo) request,
              (io.grpc.stub.StreamObserver<ImageToManagerStubs.ImageInfo>) responseObserver);
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
          getRegistServerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ImageToManagerStubs.ImageRegistInfo,
              ImageToManagerStubs.ImageInfo>(
                service, METHODID_REGIST_SERVER)))
        .build();
  }

  private static abstract class ImageToManagerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ImageToManagerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ImageToManagerStubs.ManagerServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ImageToManagerService");
    }
  }

  private static final class ImageToManagerServiceFileDescriptorSupplier
      extends ImageToManagerServiceBaseDescriptorSupplier {
    ImageToManagerServiceFileDescriptorSupplier() {}
  }

  private static final class ImageToManagerServiceMethodDescriptorSupplier
      extends ImageToManagerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ImageToManagerServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (ImageToManagerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ImageToManagerServiceFileDescriptorSupplier())
              .addMethod(getRegistServerMethod())
              .build();
        }
      }
    }
    return result;
  }
}
