package Challenge5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;

public class Spirograph extends JFrame {
  private final float movingRadius;
  private final float offset;
  private final float scale;
  private final int xResolution;
  private final int yResolution;
  private final float fixedRadius;

  public Spirograph(float fixedRadius, float movingRadius, float offset, float scale) {
    this.fixedRadius = fixedRadius;
    this.movingRadius = movingRadius;
    this.offset = offset;
    this.scale = scale;

    Dimension screenSize = getToolkit().getScreenSize();
    yResolution = (int) screenSize.getHeight();
    xResolution = (int) screenSize.getWidth();
    setTitle("Spirograph");
    setSize(xResolution, yResolution);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    getContentPane().setBackground(new Color(50, 50, 50));
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;

    float total = 10f;
    float end = 150f;
    float step = 0.001f;

    float time = 0;
    // Move to the starting point

    while (time < end) {
      GeneralPath path = new GeneralPath();
      path.moveTo(getX(time), getY(time));
      time += step;

      // Draw a line
      path.lineTo(getX(time), getY(time));
      path.moveTo(getX(time), getY(time));

      // Close the path
      path.closePath();

      float phase = (255 * time) / end;
      // Set the color and draw the path
      g2d.setColor(new Color((int) phase, (int) (255 - phase), (int) (phase / 2)));
      g2d.draw(path);

      try {
        long sleep = (long) (((total * step) / end) * 1000);
        Thread.sleep(sleep);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private float getX(float time) {

    return (float)
                ((fixedRadius - movingRadius) * Math.cos(time)
                    + offset * Math.cos(((fixedRadius - movingRadius) / movingRadius) * time))
            * scale
        + xResolution / 2;
  }

  private float getY(float time) {

    return (float)
                ((fixedRadius - movingRadius) * Math.sin(time)
                    - offset * Math.sin(((fixedRadius - movingRadius) / movingRadius) * time))
            * scale
        + yResolution / 2;
  }
}
