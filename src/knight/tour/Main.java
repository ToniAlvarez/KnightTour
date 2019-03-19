package knight.tour;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

/**
 * Clase principal
 */
public class Main extends JFrame {

    /**
     * Constant fields
     */
    private static final int ANCHO = 500;
    private static final int ALTO = 580;

    private static final int MIN_BOARD_SIZE = 2;
    private static final int MAX_BOARD_SIZE = 10;

    //Tamaño del tablero de movimientos válidos
    //Solo funciona con números impares, para que haya casilla central. Mínimo 5
    private static final int MOVES_BOARD_SIZE = 7;

    //Movimientos válidos por defecto
    private final static int[][] KNIGHT_MOVES = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};


    /**
     * Array de celdas que representan el tablero
     */
    private Celda[][] celdas;

    /**
     * Array de movimientos válidos, por defecto los movimientos del caballo
     */
    private ArrayList<Movimiento> movimientosValidos = new ArrayList<>();

    /**
     * UI fields
     */
    private JPanel panelPrincipal;
    private JPanel panelTablero;
    private JPanel panelMovimientos;

    /**
     * UI Menu fields
     */
    private JButton resolver = new JButton();
    private JButton ampliarTablero = new JButton("+");
    private JButton reducirTablero = new JButton("-");
    private JButton movimientoAnterior = new JButton("<<");
    private JButton movimientoSiguiente = new JButton(">>");
    private JLabel labelMovimientosMostrados = new JLabel();
    private JLabel labelTamanoTablero = new JLabel();
    private JLabel labelMenu = new JLabel();

    /**
     * Status
     */
    private int board_size = 8;
    private int blocked_cells;
    private int movimiento;
    private boolean finalizado = false;
    private int movimientosMostrados = 0;
    private long numLlamadasRecursivas;
    private long numBacktracking;

    /**
     * Constructor
     */
    private Main() {

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Añadir como válidos los movimientos del caballo
        for (int[] KNIGHT_MOVE : KNIGHT_MOVES)
            movimientosValidos.add(new Movimiento(KNIGHT_MOVE[0], KNIGHT_MOVE[1]));

        //Border Layout para poner el menú en la parte de arriba
        panelPrincipal = new JPanel(new BorderLayout(0, 0));
        panelPrincipal.setFocusable(true);

        //Inicializar Celdas
        initCeldas();

        //Inicializar UI
        initUI();

        //Inicializar menú
        initMenu();
        initMenuActions();

        setLocationByPlatform(true);
        setContentPane(panelPrincipal);
        setSize(new Dimension(ANCHO, ALTO));
        setMinimumSize(new Dimension(ANCHO, ALTO));
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Inicizalizar UI
     *
     * Gestión de pestañas obtenida de la web de Oracle:
     * https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
     * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
     */
    private void initUI() {

        //Panel con pestañas
        JTabbedPane tabbedPane = new JTabbedPane();

        //Panel de tablero
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridBagLayout());
        panelTablero.setBackground(new Color(40, 140, 0));

        //Panel de modificacion de movimientos
        panelMovimientos = new JPanel();
        panelMovimientos.setLayout(new GridBagLayout());
        panelMovimientos.setBackground(new Color(40, 140, 0));

        tabbedPane.addTab("   Tablero   ", panelTablero);
        tabbedPane.addTab(" Movimientos ", panelMovimientos);

        //Add the tabbed pane to this panel.
        panelPrincipal.add(tabbedPane);

        initTabTablero();

        initTabMovimientos();

    }

    /**
     * Inicializar UI del la pestaña del tablero principal
     */
    private void initTabTablero() {
        //Tablero usando un Panel que siempre es cuadrado
        SquarePanel tablero = new SquarePanel();
        tablero.setLayout(new GridLayout(0, board_size));

        panelTablero.add(tablero);

        //Rellenar el GridLayout con las celdas
        for (int row = 0; row < board_size; row++)
            for (int col = 0; col < board_size; col++)
                tablero.add(celdas[col][row]);
    }

    /**
     * Inicializar el array de celdas
     * Se tiene que ejecutar cada vez que se cambia el tamaño del tablero
     */
    private void initCeldas() {

        //Inicializar el array de Celdas que representa el tablero
        celdas = new Celda[board_size][board_size];

        for (int row = 0; row < celdas.length; row++) {
            for (int col = 0; col < celdas[row].length; col++) {

                Celda celda = new Celda();

                if ((col % 2 == 1 && row % 2 == 1) || (col % 2 == 0 && row % 2 == 0))
                    celda.setBlack(true);
                else
                    celda.setBlack(false);

                if (row == 0 && col == 0)
                    celda.setInit(true);

                celda.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent mouseEvent) {

                        if (SwingUtilities.isLeftMouseButton(mouseEvent))
                            celda.setBlocked(!celda.isBlocked());

                        if (SwingUtilities.isRightMouseButton(mouseEvent))
                            setInitCel(celda);
                    }
                });

                celdas[col][row] = celda;
            }
        }
    }

    /**
     * Inicializar UI de la pestaña del tablero de modificación de movimientos
     */
    private void initTabMovimientos() {

        //Tablero de movimientos usando un Panel que siempre es cuadrado
        SquarePanel tableroMovimientos = new SquarePanel();
        tableroMovimientos.setLayout(new GridLayout(0, MOVES_BOARD_SIZE));

        panelMovimientos.add(tableroMovimientos);

        //Inicializar el array de Celdas que representa el tablero de movimientoss
        CeldaMovimiento[][] celdasMovimientos = new CeldaMovimiento[MOVES_BOARD_SIZE][MOVES_BOARD_SIZE];

        for (int row = 0; row < celdasMovimientos.length; row++) {
            for (int col = 0; col < celdasMovimientos[row].length; col++) {

                CeldaMovimiento celdaMovimiento = new CeldaMovimiento();

                celdaMovimiento.setValidMove(false);

                int center = MOVES_BOARD_SIZE / 2;

                if (row == center && col == center)
                    celdaMovimiento.setInit(true);

                Movimiento newMove = new Movimiento((center - row) * (-1), (center - col) * (-1));

                //Marcar las casillas válidas
                for (Movimiento movimientoValido : movimientosValidos)
                    if (newMove.equals(movimientoValido))
                        celdaMovimiento.setValidMove(true);

                celdaMovimiento.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent mouseEvent) {

                        if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                            celdaMovimiento.setValidMove(!celdaMovimiento.isValidMove());

                            //Añadir o eliminar movimiento a la lista de movimientos válidos
                            if (celdaMovimiento.isValidMove())
                                movimientosValidos.add(newMove);
                            else
                                movimientosValidos.remove(newMove);

                            printMovimientos();
                        }
                    }
                });

                celdasMovimientos[col][row] = celdaMovimiento;
            }
        }

        //Rellenar el GridLayout con las celdas
        for (int row = 0; row < MOVES_BOARD_SIZE; row++)
            for (int col = 0; col < MOVES_BOARD_SIZE; col++)
                tableroMovimientos.add(celdasMovimientos[col][row]);
    }

    /**
     * Método que imprime por consola el listado de movimientos válidos
     * Por defecto son los movimientos del caballo
     */
    private void printMovimientos() {

        System.out.println("\n\n\n\n");

        for (Movimiento movimientoValido : movimientosValidos)
            System.out.println(movimientoValido.toString());
    }

    /**
     * Cambiar casilla inicial
     * @param celda nuevva casilla inicial
     */
    private void setInitCel(Celda celda) {
        //Resetear la anterior casilla inicial
        for (int row = 0; row < board_size; row++)
            for (int col = 0; col < board_size; col++)
                celdas[col][row].setInit(false);

        //Configurar nueva casilla inicial
        celda.setInit(true);
    }

    /**
     * Inicilizar UI del menu
     */
    private void initMenu() {

        JToolBar menu = new JToolBar();
        menu.setFloatable(false);
        panelPrincipal.add(menu, BorderLayout.PAGE_START);

        resolver.setText(finalizado ? "Reiniciar" : "Resolver");
        labelMenu.setText("Tamaño del tablero: ");

        movimientosMostrados = 0;
        labelMovimientosMostrados.setText(" " + movimientosMostrados + " ");
        labelTamanoTablero.setText(" " + board_size + "x" + board_size + " ");

        menu.add(labelMenu);

        menu.add(reducirTablero);
        menu.add(labelTamanoTablero);
        menu.add(ampliarTablero);

        menu.add(movimientoAnterior);
        menu.add(labelMovimientosMostrados);
        menu.add(movimientoSiguiente);

        movimientoAnterior.setVisible(false);
        movimientoSiguiente.setVisible(false);
        labelMovimientosMostrados.setVisible(false);

        menu.addSeparator();
        menu.add(Box.createHorizontalGlue());
        menu.add(resolver);

    }

    /**
     * Inicilizar acciones del menu
     */
    private void initMenuActions() {

        ampliarTablero.addActionListener(e -> {
            if (board_size < MAX_BOARD_SIZE)
                board_size++;

            initCeldas();
            reiniciar();
        });

        reducirTablero.addActionListener(e -> {
            if (board_size > MIN_BOARD_SIZE)
                board_size--;

            initCeldas();
            reiniciar();
        });

        resolver.addActionListener(e -> {

            if (finalizado) {
                reiniciar();
                return;
            }

            System.out.println("Resolviendo...");

            blocked_cells = 0;

            //Actualizar el numero de celdas bloqueadas
            for (int row = 0; row < celdas.length; row++) {
                for (int col = 0; col < celdas[row].length; col++) {

                    celdas[row][col].setMove(0);
                    celdas[row][col].setText("");

                    if (celdas[row][col].isBlocked())
                        blocked_cells++;
                }
            }

            //Encontrar la celda inicial y llamar al metodo de resolver
            encontrarCeldaInicial:
            for (int row = 0; row < celdas.length; row++) {
                for (int col = 0; col < celdas[row].length; col++) {
                    if (celdas[row][col].isInit()) {

                        movimiento = 0;
                        numLlamadasRecursivas = 0;
                        numBacktracking = 0;

                        if (resolveBacktracking(row, col)) {
                            finalizar();
                            resolver.setText("Reiniciar");
                            JOptionPane.showMessageDialog(panelPrincipal, "¡Solución encontrada!");
                            System.out.println("¡Solución encontrada!");
                        } else {
                            JOptionPane.showMessageDialog(panelPrincipal, "No hay solucion!");
                            System.out.println("¡No hay solucion!");
                        }

                        pintarTableroConsola();

                        System.out.println("Llamadas recursivas: " + numLlamadasRecursivas);
                        System.out.println("Llamadas backtracking: " + numBacktracking);

                        //Parar ambos loops
                        break encontrarCeldaInicial;
                    }
                }
            }
        });

        movimientoAnterior.addActionListener(e -> {
            if (movimientosMostrados > 0)
                movimientosMostrados--;

            printResult(movimientosMostrados);

            labelMovimientosMostrados.setText(" " + movimientosMostrados + " ");
        });

        movimientoSiguiente.addActionListener(e -> {
            if (movimientosMostrados < (board_size * board_size) - blocked_cells)
                movimientosMostrados++;

            printResult(movimientosMostrados);

            labelMovimientosMostrados.setText(" " + movimientosMostrados + " ");
        });

    }

    /**
     * Método que imprime por consola el estado del tablero
     */
    private void pintarTableroConsola() {

        for (int row = 0; row < celdas.length; row++) {
            System.out.print("\n{");
            for (int col = 0; col < celdas[row].length; col++) {
                if (col < board_size - 1)
                    System.out.print(celdas[col][row].getMove() + ", ");
                else
                    System.out.print(celdas[col][row].getMove());
            }
            System.out.print("}");
        }
        System.out.print("\n");
    }

    /**
     * Imprimir el resultado completo
     */
    private void printFullResult() {
        for (int row = 0; row < celdas.length; row++)
            for (int col = 0; col < celdas[row].length; col++)
                celdas[row][col].setText(String.valueOf(celdas[row][col].getMove()));
    }

    /**
     * Método que imprime el número de movimiento de cada casilla, hasta maxMove (incluido)
     * @param maxMove máximo número de movimiento a mostrar
     */
    private void printResult(int maxMove) {

        if (maxMove == 0) {
            printFullResult();
            return;
        }

        for (int row = 0; row < celdas.length; row++)
            for (int col = 0; col < celdas[row].length; col++)
                if (celdas[row][col].getMove() <= maxMove)
                    celdas[row][col].setText(String.valueOf(celdas[row][col].getMove()));
                else
                    celdas[row][col].setText("");
    }


    /**
     * Método de resolución mediante backtracking
     *
     * @param row de la casilla inicial
     * @param col de la casilla inicial
     * @return true si se ha encontrado un resultado, false en caso contrario
     */
    private boolean resolveBacktracking(int row, int col) {
        numLlamadasRecursivas++;

        if (numLlamadasRecursivas % 1000000 == 0) {
            System.out.println("Llamadas recursivas: " + numLlamadasRecursivas + " - Backtracking: " + numBacktracking + " - Move: " + movimiento);
        }

        if (!isValidMove(row, col))
            return false;

        movimiento++;
        celdas[row][col].setMove(movimiento);

        //Comprobar si se ha resuelto
        if (movimiento == (board_size * board_size) - blocked_cells)
            return true;

        ArrayList<Movimiento> movimientos = warnsdorffOrder(row, col, movimientosValidos);
        //printMovimientos(movimientos);

        if (movimientos.size() == 0) {
            celdas[row][col].setMove(0);
            this.movimiento--;
            return false;
        }

        int counter = 0;

        for (Movimiento possibleMove : movimientos) {
            counter++;
            int newRow = row + possibleMove.getY();
            int newCol = col + possibleMove.getX();

            if (resolveBacktracking(newRow, newCol)) {
                return true;
            } else {
                if (counter == movimientos.size()) {
                    numBacktracking++;
                    movimiento--;
                    celdas[row][col].setMove(0);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Metodo que devuleve un array de Movimientos usando la regla de Warnsdorff,
     * para ello hay que calcular el número de movimientos válidos desde todas las casillas
     * que están aal alcance del caballo:
     *
     * https://en.wikipedia.org/wiki/Knight%27s_tour#Warnsdorf's_rule
     * http://warnsdorff.com/
     *
     * @param row de la casilla actual
     * @param col de la casilla actual
     * @param movimientosValidos array de movimientos válidos
     * @return array de movimientos ordenados en función de la regla de Warnsdorff
     */
    private ArrayList<Movimiento> warnsdorffOrder(int row, int col, ArrayList<Movimiento> movimientosValidos) {

        ArrayList<Movimiento> movimientos = new ArrayList<>();

        for (Movimiento move : movimientosValidos) {

            int nextRow = row + move.getY();
            int nextCol = col + move.getX();

            if (isValidMove(nextRow, nextCol)) {
                move.setAlcance(calcularAlcance(nextRow, nextCol));
                movimientos.add(move);
            }
        }

        //Ordenar el array de movimientos en función del alcance
        Collections.sort(movimientos);

        return movimientos;
    }

    /**
     * Funcion que calcula los posibles movimientos (alcance) de un Movimiento
     * antes de ser ejecutado
     *
     * @param row de la casilla
     * @param col de la casilla
     * @return
     */
    private int calcularAlcance(int row, int col) {

        int alcance = 0;

        for (Movimiento move : movimientosValidos) {

            int nextRow = row + move.getY();
            int nextCol = col + move.getX();

            if (nextRow == row && nextCol == col)
                continue;

            if (!isValidMove(nextRow, nextCol))
                continue;

            alcance++;
        }

        return alcance;
    }

    /**
     * Comprobar si un movimiento es válido
     * <p>
     * El motivo más probable de un movimiento inválido es que la casilla ya esté visitada,
     * por lo que lo más óptimo sería hacer esta comprobación primero,
     * pero para comprobarlo primero hay que comprobar los limites para evitar un ArrayIndexOutOfBoundsException
     *
     * @param row de la casilla
     * @param col de la casilla
     * @return
     */
    private boolean isValidMove(int row, int col) {

        //Comprobar limites del tablero
        if (row < 0 || col < 0 || row >= board_size || col >= board_size)
            return false;

        //Comprobar que la casilla no haya sido ya visitada
        if (celdas[row][col].getMove() > 0)
            return false;

        //Comprobar que la casilla no esté bloqueada
        return !celdas[row][col].isBlocked();
    }

    /**
     * Cambiar estado a finalizado
     */
    private void finalizar() {
        finalizado = true;

        labelMenu.setText("Ver paso a paso: ");
        ampliarTablero.setVisible(false);
        reducirTablero.setVisible(false);
        labelTamanoTablero.setVisible(false);
        movimientoAnterior.setVisible(true);
        movimientoSiguiente.setVisible(true);
        labelMovimientosMostrados.setVisible(true);

        printFullResult();
    }

    /**
     * Reinicializar el tablero y el estado
     */
    private void reiniciar() {
        for (int row = 0; row < celdas.length; row++) {
            for (int col = 0; col < celdas[row].length; col++) {
                celdas[row][col].setMove(0);
                celdas[row][col].setText("");
            }
        }

        finalizado = false;
        ampliarTablero.setVisible(true);
        reducirTablero.setVisible(true);
        labelTamanoTablero.setVisible(true);
        panelPrincipal.removeAll();
        initUI();
        initMenu();
    }

    /**
     * Init
     * @param args
     */
    public static void main(String[] args) {
        new Main();
    }
}