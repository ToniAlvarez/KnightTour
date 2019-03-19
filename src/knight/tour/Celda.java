package knight.tour;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa las celdas del tablero
 * Las celdas pueden estar bloqueadas
 * Solo una de ellas puede ser la casilla inicial
 * Una vez encontrada una solución, a cada casilla se le asigna un numero de movimiento
 */
public class Celda extends JButton {

    private boolean black = false;
    private boolean blocked = false;
    private boolean init = false;
    private int move = 0;

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;

        updateColor();
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {

        //Si la casilla es la inicial, no se puede bloquear
        if (init)
            return;

        this.blocked = blocked;

        updateColor();
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {

        //La casilla inicial no ppuede estar bloqueada
        if (init)
            this.blocked = false;

        this.init = init;

        updateColor();
    }

    private void updateColor() {
        if (init)
            setBackground(Color.GREEN);
        else if (blocked)
            setBackground(Color.RED);
        else if (black)
            setBackground(Color.GRAY);
        else
            setBackground(Color.WHITE);
    }
}
