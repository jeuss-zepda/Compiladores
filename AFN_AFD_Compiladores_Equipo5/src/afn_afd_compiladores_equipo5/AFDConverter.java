package afn_afd_compiladores_equipo5;

import java.util.*;

public class AFDConverter {

   public AFD convertirAFNaAFD(AFN afn) {
    Map<Set<Estado>, Integer> mapaConj = new HashMap<>();
    List<Set<Estado>> listaConj = new ArrayList<>();
    Queue<Set<Estado>> cola = new LinkedList<>();

    Set<Estado> inicio = afn.CerraduraEpsilon(afn.EdoInicial);
    listaConj.add(inicio);
    mapaConj.put(inicio, 0);
    cola.add(inicio);

    AFD afd = new AFD();
    afd.Alfabeto = new HashSet<>(afn.Alfabeto);
    afd.asegurarCapacidad(listaConj.size());

    while (!cola.isEmpty()) {
        Set<Estado> conjunto = cola.poll();
        int idActual = mapaConj.get(conjunto);

        if (afd.EdosAFD[idActual] == null) {
            afd.EdosAFD[idActual] = new EdoAFD(idActual);
        }

        // Para cada símbolo en el alfabeto
        for (char simbolo : afd.Alfabeto) {
            if (simbolo == SimbEspecial.EPSILON) continue;
            
            Set<Estado> destino = afn.IrA(conjunto, simbolo);
            if (destino.isEmpty()) continue;

            Integer idDestino = mapaConj.get(destino);
            if (idDestino == null) {
                listaConj.add(destino);
                idDestino = listaConj.size() - 1;
                mapaConj.put(destino, idDestino);
                cola.add(destino);
                afd.asegurarCapacidad(listaConj.size());
            }
            
            afd.EdosAFD[idActual].TransAFD[(int) simbolo] = idDestino;
        }
    }

    afd.NumEdos = listaConj.size();
    return afd;
    } 
   
}
