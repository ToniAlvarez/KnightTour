package knight.tour;

/**
 * Clase que representa un movimiento desde la casilla actual
 */
public class Movimiento implements Comparable<Movimiento> {

    private int x;
    private int y;

    //Movimientos validos desde la casilla una vez realizado este movimiento
    private int alcance = 0;

    /**
     * Constructor
     * @param x numero de casillas en horizontal
     * @param y numero de casillas en vertical
     */
    public Movimiento(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param o Object con el que comparar
     * @return true si son objetos iguales, false een caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Movimiento))
            return false;

        Movimiento move = (Movimiento) o;

        return x == move.x && y == move.y;
    }

    /**
     * @return String que representa este movimiento
     */
    @Override
    public String toString() {
        return "Movimiento {" + "x=" + x + ", y=" + y + ", alcance=" + alcance + '}';
    }

    /**
     * @param move Movimiento con el que comparar
     * @return diferencia de alcance
     */
    @Override
    public int compareTo(Movimiento move) {
        return this.alcance - move.getAlcance();
    }


    /**
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x new X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y new Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return alcance
     */
    public int getAlcance() {
        return alcance;
    }

    /**
     * @param alcance new alcance
     */
    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }
}
