package knight.tour;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que extiende JPanel,
 * con el propósito de mantener siempre el ancho y el alto proporcionales
 *
 * Obtenido de StackOverflow:
 * https://stackoverflow.com/a/16075907/710274
 */
class SquarePanel extends JPanel {

    @Override
    public Dimension getPreferredSize() {
        if (getParent() != null) {
            Dimension d = getParent().getSize();
            int w = (int) d.getWidth();
            int h = (int) d.getHeight();
            int s = (Math.min(w, h));
            return new Dimension(s, s);
        } else {
            return new Dimension(100, 100);
        }
    }
}