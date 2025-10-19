package app;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedisManager {


    private final String redisAddress;
    private final int port;

    public RedisManager(String redisAddress, int port) {
        this.redisAddress = redisAddress;
        this.port = port;
    }


    public void startRedis() throws InterruptedException {
        String containerName = UUID.randomUUID().toString();
        String imageName = "redis";
        DockerClient dockerclient = DockerClientBuilder
                .getInstance()
                .withDockerHttpClient(
                        new ApacheDockerHttpClient.Builder()
                                .dockerHost(URI.create("unix:///var/run/docker.sock")).build()
                ).build();
        HostConfig hostConfig = HostConfig
                .newHostConfig()
                .withPortBindings(PortBinding.parse(port + ":6379"));
        CreateContainerResponse containerResponse = dockerclient
                .createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(hostConfig)
                .exec();
        dockerclient.startContainerCmd(containerResponse.getId()).exec();
        for (; ; ) {
            InspectContainerResponse inspResp = dockerclient
                    .inspectContainerCmd(containerName).exec();
            System.out.println("Container Status: " + inspResp.getState().getStatus());
            if (inspResp.getState().getStatus().equals("running")) break;
            Thread.sleep(1 * 1000);
        }

    }

    public void calculatePrime(long primeNumber) throws InterruptedException {
        String containerName = UUID.randomUUID().toString();
        String imageName="simaocabral/isprime";
        List<String> command=new ArrayList<>();
        command.add(String.valueOf(primeNumber));
        command.add(redisAddress);
        command.add(String.valueOf(port));


        DockerClient dockerclient = DockerClientBuilder
                .getInstance()
                .withDockerHttpClient(
                        new ApacheDockerHttpClient.Builder()
                                .dockerHost(URI.create("unix:///var/run/docker.sock")).build()
                )
                .build();
        CreateContainerResponse containerResponse = dockerclient
                .createContainerCmd(imageName)
                .withName(containerName)
                .withCmd(command)
                .exec();
        System.out.println("ID:" + containerResponse.getId());
        dockerclient.startContainerCmd(containerResponse.getId()).exec();
        for(;;) {
            InspectContainerResponse inspResp = dockerclient
                    .inspectContainerCmd(containerName).exec();
            System.out.println("Container Status: " + inspResp.getState().getStatus());
            if (inspResp.getState().getStatus().equals("exited")) break;
            Thread.sleep(1*1000);
        }
        // if container is running
        // dockerclient.killContainerCmd(containerName).exec();
        // remove container
        dockerclient.removeContainerCmd(containerName).exec();
    }
}
