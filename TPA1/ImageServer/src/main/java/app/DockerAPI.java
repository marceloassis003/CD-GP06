package app;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DockerAPI {
    // Windows args example: tcp://localhost:2375 ctrest 8050 7500 d:\share-docker /usr/datafiles servicerest21
    // Linux args example: unix:///var/run/docker.sock 8050 7500 /usr/local/servicerest/  /usr/datafiles servicerest21
    public static void main(String[] args) {
        // arg0 windows: tcp://localhost:2375  arg0 linux: unix:///var/run/docker.sock
        // arg1 : container name
        // arg2 : host port to map
        // arg3 : container port to map
        // arg4 : name volume or host filesystem directory
        // arg5 : container (guest) filesystem directory
        // arg6 : Docker image name
        try {
            String HOST_URI = args[0];
            String containerName=args[1];
            int hostPort=Integer.parseInt(args[2]);
            int contPort=Integer.parseInt(args[3]);
            String pathVolDirHost =args[4];
            String pathVolDirGuest =args[5];
            String imageName=args[6];

            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(HOST_URI)
                    .build();
            DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                    .dockerHost(config.getDockerHost())
                    .build();
            DockerClient dockerclient = DockerClientImpl.getInstance(config, dockerHttpClient);

            Ports ports = new Ports();
            ports.add(PortBinding.parse(hostPort+":"+contPort));
            HostConfig hostConfig = HostConfig.newHostConfig()
                    .withPortBindings(ports)
                    .withBinds(new Bind(pathVolDirHost, new Volume(pathVolDirGuest)));

            List<String> containerAppArgs=new ArrayList<>();
            containerAppArgs.add(contPort+"");   // o ServiceREST.jar tem um argumento para definir porto
            CreateContainerResponse containerResponse = dockerclient
                    .createContainerCmd(imageName)
                    .withName(containerName)
                    .withHostConfig(hostConfig)
                    .withExposedPorts(new ExposedPort(contPort))  // linux é necessário, mas em windows não
                    .withCmd(containerAppArgs)
                    .exec();

            System.out.println("ID:" + containerResponse.getId());
            dockerclient.startContainerCmd(containerResponse.getId()).exec();

            InspectContainerResponse inspResp = dockerclient
                    .inspectContainerCmd(containerName)
                    .exec();
            System.out.println("Container Status: " + inspResp.getState().getStatus());

            System.out.println("Press enter to kill and remove container ");
            Scanner scan =new Scanner(System.in);
            scan.nextLine();
            dockerclient.killContainerCmd(containerName).exec();
            dockerclient.removeContainerCmd(containerName).exec();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
