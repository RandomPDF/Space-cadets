package Challenge6;

import java.awt.Color;
import java.util.ArrayList;

public class CircleDetection {

  public static void main(String[] args) {
    Image originalImage = new Image("Mona.jpg", true);
    houghTransform(originalImage, 100, 0.7, 10);
  }

  /**
   * Detects circles in an image using the Hough transform.
   *
   * @param image The image to detect circles in.
   * @param edgeThreshold The threshold for edge detection.
   * @param circleThreshold The threshold for circle detection.
   * @param minimumRadius The minimum radius of the circles to detect.
   */
  public static void houghTransform(
      Image image, int edgeThreshold, double circleThreshold, int minimumRadius) {

    int radiusPrecision = 2;
    int thetaPrecision = 12;
    System.out.println("Finding edges");
    int[][] edges = EdgeDetection.getEdges(image, Color.red, true, edgeThreshold);

    System.out.println("Initialize accumulator");
    int[][][] accumulator = new int[edges.length][edges[0].length][edges.length / 2];

    for (int r = minimumRadius; r < accumulator[0][0].length; r += radiusPrecision) {
      for (int y = 0; y < edges.length; y++) {
        for (int x = 0; x < edges[y].length; x++) {
          if (edges[y][x] <= 0) continue;
          for (int t = 0; t < 360; t += thetaPrecision) {
            int centreY = (int) (y - r * Math.sin(t * Math.PI / 180));
            int centreX = (int) (x - r * Math.cos(t * Math.PI / 180));
            if (centreY < 0
                || centreX < 0
                || centreY >= accumulator.length
                || centreX >= accumulator[centreY].length) continue;

            accumulator[centreY][centreX][r]++;
          }
        }
      }
    }

    System.out.println("Find circles");

    ArrayList<Integer> centreY = new ArrayList<>();
    ArrayList<Integer> centreX = new ArrayList<>();
    ArrayList<Integer> radius = new ArrayList<>();
    for (int r = minimumRadius; r < accumulator[0][0].length; r += radiusPrecision) {
      for (int a = 0; a < accumulator.length; a++) {
        for (int b = 0; b < accumulator[a].length; b++) {
          if (accumulator[a][b][r] < 360 * circleThreshold / thetaPrecision) continue;

          centreY.add(a);
          centreX.add(b);
          radius.add(r);
        }
      }
    }

    Image circleDetection = image;
    for (int i = 0; i < radius.size() - 1; i++) {
      int[][] circles = new int[edges.length][edges[0].length];
      for (double t = 0; t < 360; t += (double) 100 / radius.get(i)) {
        int x = (int) (centreX.get(i) - radius.get(i) * Math.sin(t * Math.PI / 180));
        int y = (int) (centreY.get(i) - radius.get(i) * Math.cos(t * Math.PI / 180));

        if (x < 0 || y < 0 || x >= circles[0].length || y >= circles.length) continue;
        circles[y][x] = 255;
      }

      Image circle = new Image(Image.getOutputImage(Color.red, circles));

      int[][] detection = Image.add(circleDetection, circle);
      circleDetection = new Image(Image.getOutputImage(null, detection));
    }

    Image.show("Circle detection", circleDetection.image);
  }
}
