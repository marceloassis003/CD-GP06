/*
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjva.httpclient5.ApacheDockerHttpClient;
import redis.clients.jedis.Jedis;



public class DockerApp {
    public static void main(String[] args) {
        // arg0 : windows: tcp://localhost:2375 linux: unix:///var/run/docker.sock
        // arg1 : container name
        // arg2 : image name
        // arg3 : number to calculate if it is prime
        // arg4 : Redis Host IP
        //arg 5: Redis port on host
        try {
            String HOST_URI = args[0]; String containerName = args[1]; String imageName=args[2];
            long number=Long.parseLong(args[3]);
            String redisHostIP=args[4];
            int redisHostport=Integer.parseInt(args[5]);
            List<String> command=new ArrayList<>();
            for (int i=3; i < args.length; i++) command.add(args[i]);

            DockerClient dockerclient = DockerClientBuilder
                    .getInstance()
                    .withDockerHttpClient(
                            new ApacheDockerHttpClient.Builder()
                                    .dockerHost(URI.create(HOST_URI)).build()
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
*/
