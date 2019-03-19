# Knight Tour con backtracking

Algoritmo para resolver de forma eficiente el [problema del caballo](https://es.wikipedia.org/wiki/Problema_del_caballo), usando la regla de Warnsdorff:

[Knight's_tour#Warnsdorff's_rule](https://en.wikipedia.org/wiki/Knight's_tour#Warnsdorff's_rule)

## Instrucciones de uso

### 1 - Configurar tablero

- Se puede cambiar el tamaño del tablero usando los botones `+` y `-` 

- Hacer click derecho en una casilla para configurarla como casilla inicial

- Hacer click izquierdo en una casilla para bloquearla, de forma que el caballo no tenga que pasar por ella.

### 2 - Configurar movimientos del caballo

En la pestaña `Movimientos` se pueden configurar los movimientos de la pieza. La casilla central representa la posición de la pieza, y las casillas en rojo representan los movimientos válidos de la pieza. Hacer click derecho en una casila para validar/invalidar un movimiento.

### 3 - Resolver

Una vez configurado el tablero y los movimientos, usar el botón `Resolver` para iniciar el algoritmo. En consola se van mostrando el progreso del mismo, mostrando el número de iteraciones y el número de vueltas hacia atrás hechas hasta el momento.

Si se encuentra una solución válida se mostrará en el tablero la solución, permitiendo visualizarla paso a paso para comprobar que los movimientos son válidos.