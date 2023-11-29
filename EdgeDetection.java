package Challenge6;

import java.awt.Color;

public class EdgeDetection {

  public static void main(String[] args) {
    Image image = new Image("Mona.jpg", true);

    getEdges(image, Color.red, true, 255);
    getSharpen(image, Color.white, true, 0);
    getBlur(image, Color.white, true, 0);
  }

  /*
   * Detects edges in an image using the Sobel operator.
   *
   * @param image The image to detect edges in.
   * @param color The color to output the edges in.
   * @param debug Whether to output the edges.
   * @param threshold The threshold for edge detection.
   * @return The edges in the image.
   */

  public static int[][] getEdgesY(Image image, Color color, boolean debug, int threshold) {
    int[][] edgesY =
        convolution(image, new double[][] {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}}, threshold);

    if (debug) Image.outputImage(color, "Y detection", edgesY);
    return edgesY;
  }

  public static int[][] getEdgesX(Image image, Color color, boolean debug, int threshold) {
    int[][] edgesX =
        convolution(image, new double[][] {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}, threshold);

    if (debug) Image.outputImage(color, "X detection", edgesX);
    return edgesX;
  }

  public static int[][] getEdges(Color color, int[][] edgesX, int[][] edgesY, boolean debug) {

    int[][] edges = new int[edgesY.length][edgesY[0].length];

    for (int y = 0; y < edgesY.length; y++) {
      for (int x = 0; x < edgesY[0].length; x++) {
        double edgesX2 = Math.pow(edgesY[y][x], 2);
        double edgesY2 = Math.pow(edgesX[y][x], 2);
        double edgesClamped = Math.min(255, Math.max(0, Math.sqrt(edgesX2 + edgesY2)));

        edges[y][x] = (int) edgesClamped;
      }
    }

    if (debug) Image.outputImage(color, "Full edge detection", edges);

    return edges;
  }

  public static int[][] getEdges(Image image, Color color, boolean debug, int threshold) {
    int[][] edgesX = getEdgesX(image, color, debug, threshold);
    int[][] edgesY = getEdgesY(image, color, debug, threshold);

    return getEdges(color, edgesX, edgesY, debug);
  }

  /*
   * Sharpens an image using a 3x3 sharpening kernel.
   *
   * @param image The image to sharpen.
   * @param color The color to output the sharpen in.
   * @param debug Whether to output the sharpened image.
   * @param threshold The threshold for edge detection.
   * @return The sharpened image.
   */
  public static int[][] getSharpen(Image image, Color color, boolean debug, int threshold) {
    int[][] sharpen =
        convolution(image, new double[][] {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}}, threshold);

    if (debug) Image.outputImage(color, "Sharpen", sharpen);
    return sharpen;
  }

  /*
   * Blurs an image using a 5x5 Gaussian kernel.
   *
   * @param image The image to blur.
   * @param color The color to output the blur in.
   * @param debug Whether to output the blur.
   * @param threshold The threshold for edge detection.
   * @return The blurred image.
   */
  public static int[][] getBlur(Image image, Color color, boolean debug, int threshold) {
    double[][] blurKernel =
        new double[][] {
          {1d / 256, 4d / 256, 6d / 256, 4d / 256, 1d / 256},
          {4d / 256, 16d / 256, 24d / 256, 16d / 256, 4d / 256},
          {6d / 256, 24d / 256, 36d / 256, 24d / 256, 6d / 256},
          {4d / 256, 16d / 256, 24d / 256, 16d / 256, 4d / 256},
          {1d / 256, 4d / 256, 6d / 256, 4d / 256, 1d / 256}
        };

    int[][] blur = convolution(image, blurKernel, threshold);
    if (debug) Challenge6.Image.outputImage(color, "Blur", blur);

    return blur;
  }

  /*
   * Convolve an image with a kernel.
   *
   * @param image The image to convolve.
   * @param kernel The kernel used to convolve the image.
   * @param threshold The threshold for edge detection.
   * @return The convolved image.
   */
  public static int[][] convolution(Image image, double[][] kernel, int threshold) {
    int[][] outputPixels = new int[image.getHeight()][image.getWidth()];

    for (int pixelY = 0; pixelY < image.getHeight(); pixelY++) {
      for (int pixelX = 0; pixelX < image.getWidth(); pixelX++) {
        int accumulator = 0;

        for (int kernelY = 0; kernelY < kernel.length; kernelY++) {
          for (int kernelX = 0; kernelX < kernel[kernelY].length; kernelX++) {
            int elementY = (kernelY - (kernel.length - 1) / 2) + pixelY;
            int elementX = (kernelX - (kernel[kernelY].length - 1) / 2) + pixelX;

            elementX = Math.max(0, Math.min(image.getWidth() - 1, elementX));
            elementY = Math.max(0, Math.min(image.getHeight() - 1, elementY));

            Color color = new Color(image.getPixel(elementX, elementY), false);

            accumulator += (int) (kernel[kernelY][kernelX] * color.getRed());
          }
        }

        int result = Math.min(Math.max(0, Math.abs(accumulator)), 255);

        if (result >= threshold) outputPixels[pixelY][pixelX] = result;
        else outputPixels[pixelY][pixelX] = 0;
      }
    }

    return outputPixels;
  }
}
