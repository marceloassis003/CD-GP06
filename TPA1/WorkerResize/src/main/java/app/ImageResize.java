package app;

import redis.clients.jedis.Jedis;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class ImageResize {


    public static void resizeImage(String inputPath, String outputPath, double percentage) {
        try {
            // Carregar imagem original
            BufferedImage originalImage = ImageIO.read(new File(inputPath));
            // Calcular novas dimensões
            int newWidth = (int) (originalImage.getWidth() * percentage);
            int newHeight = (int) (originalImage.getHeight() * percentage);
            // Criar nova imagem redimensionada: Cada pixel é armazenado como um inteiro de 32 bits (int)
            BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImg.createGraphics();
            // Desenhar imagem redimensionada
            g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();
            // Guardar
            ImageIO.write(resizedImg, "png", new File(outputPath));
        } catch (IOException e) {
            System.out.println("Error resize image: " + e.getMessage());
        }
    }

    private static void updateStatus (Jedis redis, String imgID, String field, String value) {
        redis.hset("IMG:" + imgID , field, value);
    }

    private static String buildOutputPath(String inputPath, String imgID) {
        String extension = getExtension(inputPath);
        String bname = inputPath.substring(0, inputPath.lastIndexOf('.'));
        return bname + "-resized." + extension;
    }

    private static String getExtension(String filename){
        return filename.substring(filename.lastIndexOf('.') + 1);
    }


    public static void main(String[] args) {
        //resizeImage("some.jpg", "output.jpg", 0.5); // 50%

        if (args.length < 3) {
            System.out.println("Use: java -jar worker.jar <imageId> <redisHost> <redisPort>");
            return;
        }
        String imgID = args[0];
        String redisHost = args[1];
        int redisPort = Integer.parseInt(args[2]);

        try (Jedis redis = new Jedis(redisHost, redisPort)) {

            String filePath = redis.hget("IMG:" + imgID , "input_path");

            if (filePath == null) {
                System.out.println("Error path of image not found on redis !");
                return;
            }

            // adiciona imagem para processamento
            updateStatus(redis, imgID, "status" , "PROCESSING");

            // Adiciona o novo outputh path com -resized
            String outPath = buildOutputPath(filePath, imgID);

            // faz o redimensionamento da imagem
            resizeImage(filePath, outPath, 0.5);

            // armazena o novo output no redis
            redis.hset("IMG:" + imgID , "output_path", outPath);
            updateStatus(redis, imgID, "status", "DONE");

            System.out.println("Image processed: " + outPath);

        } catch (Exception e) {
            System.out.println("Eror worker resize: " + e.getMessage());
        }
    }
}