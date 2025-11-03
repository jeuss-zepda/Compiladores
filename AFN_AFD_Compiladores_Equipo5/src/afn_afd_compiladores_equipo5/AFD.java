package afn_afd_compiladores_equipo5;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class AFD {

    public EdoAFD[] EdosAFD = null;
    public Set<Character> Alfabeto = new HashSet<>();
    public int NumEdos;

    public AFD() {
        NumEdos = 0;
        Alfabeto = new HashSet<>();
    }

    public AFD(int n) {
        EdosAFD = new EdoAFD[n];
        NumEdos = n;
        Alfabeto = new HashSet<>();
    }

    public AFD(int n, Set<Character> Alf) {
        EdosAFD = new EdoAFD[n];
        NumEdos = n;
        Alfabeto = new HashSet<>();
        Alfabeto.clear();
        Alfabeto.addAll(Alf);
    }

    public boolean guardarAFD(String nomArch, AFD afd) {
    try (FileWriter fw = new FileWriter(nomArch)) {
        // Escribir encabezado
        fw.write("Estado");
        for (char c : afd.Alfabeto) {
            fw.write("," + c);
        }
        fw.write("\n");

        // Escribir cada fila (estado)
        for (EdoAFD edo : afd.EdosAFD) {
            fw.write("q" + edo.id);
            for (char c : afd.Alfabeto) {
                int destino = edo.TransAFD[(int) c];
                if (destino != -1) {
                    fw.write("," + "q" + destino);
                } else {
                    fw.write("," + "-"); // sin transición
                }
            }
            fw.write("\n");
        }

        fw.flush();
        return true;

    } catch (IOException ex) {
        ex.printStackTrace();
        return false;
    }
}


    /*
    public boolean CargarAFD(String NomArch){
        
    }*/
    // asegura tamaño interno según num estados
    public void asegurarCapacidad(int n) {
        if (EdosAFD == null || EdosAFD.length < n) {
            EdoAFD[] neu = new EdoAFD[n];
            if (EdosAFD != null) {
                System.arraycopy(EdosAFD, 0, neu, 0, EdosAFD.length);
            }
            EdosAFD = neu;
        }
        NumEdos = n;
    }
}
