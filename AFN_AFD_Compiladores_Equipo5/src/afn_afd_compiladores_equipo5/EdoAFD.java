package afn_afd_compiladores_equipo5;

import java.util.Set;

public class EdoAFD {
    
    public int[] TransAFD;
    public int id;

    public EdoAFD() {
        TransAFD = new int[257]; // índice por código de char
        id = -1;
        for (int i = 0; i < TransAFD.length; i++) TransAFD[i] = -1;
    }

    public EdoAFD(int idEdo) {
        this();
        this.id = idEdo;
    }
   
}
