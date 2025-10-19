package mqconsumer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;


public class MarkApp {
    public static void main(String[] args) throws IOException {
        // args[0] - image pathname; args[1] - image result pathname
        // args[2]...args[n] keywords to mark image
        String inputPath = args[0];
        String outputPath = args[1];
        ArrayList<String> keywords = new ArrayList<>();
        for (int i = 2; i < args.length; i++) keywords.add(args[i]);
        BufferedImage img = null;

        img = ImageIO.read(Path.of(inputPath).toFile());
        annotateImage(img, keywords);
        int lastIndex = inputPath.lastIndexOf('.');
        String format = inputPath.substring(lastIndex + 1);
        ImageIO.write(img, format, Path.of(outputPath).toFile());

    }

    private static void annotateImage(BufferedImage img, ArrayList<String> keywords) {
        Graphics2D gfx = img.createGraphics();
        gfx.setFont(new Font("Arial", Font.PLAIN, 22));
        gfx.setColor(new Color(0x0000ff));
        String sentence = "";
        for (String s : keywords) sentence += s + " ";
        gfx.drawString(sentence, 10, 20);
        Polygon poly = new Polygon();
        poly.addPoint(3, 3);
        poly.addPoint(10 * sentence.length(), 3);
        poly.addPoint(10 * sentence.length(), 25);
        poly.addPoint(3, 25);
        poly.addPoint(3, 3);
        gfx.setColor(new Color(0xff0000));
        gfx.draw(poly);
    }
}

