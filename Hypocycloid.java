package Challenge5;

import javax.swing.SwingUtilities;

public class Hypocycloid {
  private static final float fixedRadius = 100;
  private static final float movingRadius = 23;
  private static final float offset = 65;
  private static final float scale = 2.25f;

  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          Spirograph spirograph = new Spirograph(fixedRadius, movingRadius, offset, scale);
          spirograph.setVisible(true);
        });
  }
}
