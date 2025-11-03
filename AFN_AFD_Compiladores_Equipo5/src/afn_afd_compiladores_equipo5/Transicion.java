package afn_afd_compiladores_equipo5;

public class Transicion {
    public char SimboloInf;
    public char SimboloSup;
    public Estado EdoDestino;

    public Transicion() {
        EdoDestino = null;
        SimboloInf = SimboloSup = 0;
    }

    // transición simple símbolo -> destino
    public Transicion(char c, Estado e) {
        SimboloInf = SimboloSup = c;
        EdoDestino = e;
    }

    // transición por rango (ej. a-z)
    public Transicion(char cinf, char csup, Estado e) {
        SimboloInf = cinf;
        SimboloSup = csup;
        EdoDestino = e;
    }

    // verifica si el símbolo 'c' hace que ésta transición sea aplicable
    public boolean acepta(char c) {
        return c >= SimboloInf && c <= SimboloSup;
    }

    // AGREGA ESTE MÉTODO PARA MOSTRAR TRANSICIONES MÁS LEGIBLES
    @Override
    public String toString() {
        if (SimboloInf == SimbEspecial.EPSILON) {
            return "ε -> " + (EdoDestino == null ? "null" : EdoDestino.toString());
        } else if (SimboloInf == SimboloSup) {
            return "'" + SimboloInf + "' -> " + (EdoDestino == null ? "null" : EdoDestino.toString());
        } else {
            return "'" + SimboloInf + "-" + SimboloSup + "' -> " + (EdoDestino == null ? "null" : EdoDestino.toString());
        }
    }
}