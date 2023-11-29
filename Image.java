package Challenge6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class Image {
  BufferedImage image;

  /**
   * Creates an image from a file.
   *
   * @param imageFile The file to create the image from.
   * @param debug Whether to output the image.
   */
  public Image(String imageFile, boolean debug) {
    String imagePath =
        "C:\\Users\\hjoce\\OneDrive\\Documents\\Uni\\Java\\Space cadets\\Challenge6\\Images\\"
            + imageFile;

    image = null;

    try {
      image = ImageIO.read(new File(imagePath));
      ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
      ColorConvertOp op = new ColorConvertOp(cs, null);
      image = op.filter(image, null);

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }

    if (debug) show("Original image", image);
  }

  public Image(BufferedImage image) {
    this.image = image;
  }

  /**
   * Displays an image in a window.
   *
   * @param title The title of the window.
   * @param image The image to display.
   */
  public static void show(String title, BufferedImage image) {
    JFrame frame;
    JLabel label;

    frame = new JFrame();
    frame.setTitle(title);
    frame.setSize(image.getWidth(), image.getHeight());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    label = new JLabel();
    label.setIcon(new ImageIcon(image));
    frame.getContentPane().add(label, BorderLayout.CENTER);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Adds two images together.
   *
   * @param base The base image.
   * @param addition The image to add to the base image.
   * @return The combined image.
   */
  public static int[][] add(Image base, Image addition) {
    int[][] result = new int[base.getHeight()][base.getWidth()];

    for (int y = 0; y < base.getHeight(); y++) {
      for (int x = 0; x < base.getWidth(); x++) {
        Color baseColour = new Color(base.getPixel(x, y));
        Color additionColour = new Color(addition.getPixel(x, y));

        if (additionColour.getRGB() == Color.black.getRGB()) {
          result[y][x] = baseColour.getRGB();
        } else {
          result[y][x] = additionColour.getRGB();
        }
      }
    }

    return result;
  }

  public static BufferedImage getOutputImage(Color color, int[][] pixels) {

    BufferedImage outputImage =
        new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_ARGB);

    for (int pixelY = 0; pixelY < outputImage.getHeight(); pixelY++) {
      for (int pixelX = 0; pixelX < outputImage.getWidth(); pixelX++) {
        if (color == null) {
          outputImage.setRGB(pixelX, pixelY, new Color((pixels[pixelY][pixelX])).getRGB());

        } else {
          double pixelMagnitude = (double) pixels[pixelY][pixelX] / 255;

          int r = (int) Math.round(color.getRed() * pixelMagnitude);
          int g = (int) Math.round(color.getGreen() * pixelMagnitude);
          int b = (int) Math.round(color.getBlue() * pixelMagnitude);

          outputImage.setRGB(pixelX, pixelY, new Color(r, g, b).getRGB());
        }
      }
    }

    return outputImage;
  }

  /**
   * Outputs an image.
   *
   * @param color The color to output the image in.
   * @param title The title of the window.
   * @param pixels The pixels of the image.
   * @return The output image.
   */
  public static BufferedImage outputImage(Color color, String title, int[][] pixels) {

    BufferedImage outputImage = getOutputImage(color, pixels);
    Image.show(title, outputImage);

    return outputImage;
  }

  public int getPixel(int x, int y) {
    return image.getRGB(x, y);
  }

  public int getWidth() {
    return image.getWidth();
  }

  public int getHeight() {
    return image.getHeight();
  }
}
