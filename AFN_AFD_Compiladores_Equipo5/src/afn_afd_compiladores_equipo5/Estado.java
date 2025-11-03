package afn_afd_compiladores_equipo5;

import java.util.HashSet;
import java.util.Set;

public class Estado {
    public int idEdo;
    public boolean EdoAcept;
    public int Token; // <- Este campo ya existe, lo usaremos
    public Set<Transicion> Transiciones;
    private static int NumEstados = 0;

    public Estado() {
        idEdo = NumEstados++;
        EdoAcept = false;
        Token = -1; // -1 significa "sin token asignado"
        Transiciones = new HashSet<>();
        Transiciones.clear();
    }
    
    // Constructor que permite asignar token
    public Estado(int token) {
        this();
        this.Token = token;
        this.EdoAcept = (token != -1); // Si tiene token, es estado de aceptación
    }
    
    public Set<Estado> TieneTransicionCon(char c){
        Set<Estado> R = new HashSet<>();
        R.clear();
        for(Transicion t:this.Transiciones){
            if(t.SimboloInf<=c && c<=t.SimboloSup)
                R.add(t.EdoDestino);
        }
        
        return R;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idEdo);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Estado)) return false;
        Estado other = (Estado) obj;
        return this.idEdo == other.idEdo;
    }

    public void agregarTransicion(Transicion t) {
        Transiciones.add(t);
    }

    @Override
    public String toString() {
        return "q" + idEdo + (Token != -1 ? "(Token:" + Token + ")" : "");
    }
}