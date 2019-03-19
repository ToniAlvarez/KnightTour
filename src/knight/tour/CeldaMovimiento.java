package knight.tour;

import javax.swing.*;
import java.awt.*;

/**
 * Clase para representar las celdas de la pestaña de modificación de movimientos
 * Al clicar en una casilla se cambia su estado de validez.
 * Las casillas verdes representan movimientos válidos, las rojas no-válidos.
 * La casilla inicial es la casilla central
 */
public class CeldaMovimiento extends JButton {

    private boolean validMove = false;
    private boolean init = false;

    /**
     * Cambiar color de la CasillaMovimiento en función de sus parámetros
     */
    private void updateColor() {
        if (init)
            setBackground(Color.WHITE);
        else if (validMove)
            setBackground(Color.GREEN);
        else
            setBackground(Color.RED);
    }

    /**
     * @return true si se puede saltar a esta casilla, falso en caso contrario
     */
    public boolean isValidMove() {
        return validMove;
    }

    /**
     * Cambiar estado de la casilla
     * @param validMove nuevo estado de validez
     */
    public void setValidMove(boolean validMove) {

        //Si la casilla es la central, no es movimiento valido
        if (init)
            return;

        this.validMove = validMove;

        updateColor();
    }

    /**
     * @return true si es la casilla central, false en caso contrario
     */
    public boolean isInit() {
        return init;
    }

    /**
     * Configurar casilla central como inicial
     * @param init estado de casilla inicial
     */
    public void setInit(boolean init) {

        //La casilla inicial no puede estar bloqueada
        if (init)
            this.validMove = false;

        this.init = init;

        updateColor();
    }
}
