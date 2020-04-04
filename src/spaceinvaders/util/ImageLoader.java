package spaceinvaders.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author insan
 */
public class ImageLoader {
    
    private ImageLoader() { }

    /**
     * Load an image from the classloader
     * @param path to image to load
     * @return buffered image
     */
    public static BufferedImage loadImage(String path) {
        BufferedImage bufferedImage = null;
        try {
            System.out.println(ImageLoader.class.getResource(path));
            bufferedImage = ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, "Could not load image " + path + "!", e);
            System.exit(-1);
        }

        return bufferedImage;
    }

}
