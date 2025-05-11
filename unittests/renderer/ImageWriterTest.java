package renderer;

import org.junit.jupiter.api.Test;
import primitives.*;

class ImageWriterTest {

    @Test
    void testWriteToImage() {
        ImageWriter imageWriter=new ImageWriter(800,500);
        for (int i = 0; i < imageWriter.nX(); i++) {
               for (int j = 0; j < imageWriter.nY(); j++) {
                           imageWriter.writePixel(i,j , i % 50 == 0 || j % 50 == 0 ? new Color(java.awt.Color.RED) : new Color(java.awt.Color.yellow));
            }
        }

        imageWriter.writeToImage("test_image");
    }
}