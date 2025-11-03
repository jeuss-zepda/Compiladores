package afn_afd_compiladores_equipo5;

import java.util.*;

public class AFN {

    public Set<Estado> Estados;
    public Estado EdoInicial;
    public Set<Character> Alfabeto;
    public Set<Estado> EdosAcept;

    public AFN() {
        Estados = new HashSet<>();
        EdoInicial = null;
        Alfabeto = new HashSet<>();
        EdosAcept = new HashSet<>();
    }

    public AFN crearBasico(char c) {
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        Estados.add(e1);
        Estados.add(e2);
        EdoInicial = e1;
        e1.Transiciones.add(new Transicion(c, e2));
        e2.EdoAcept = true;
        EdosAcept.add(e2);
        Alfabeto.add(c);
        return this;
    }

    // rango c1-c2 -> agrega transición por rango en el estado inicial
    public AFN crearBasico(char c, int token) {
    Estado e1 = new Estado();
    Estado e2 = new Estado(token); // Pasar token al constructor
    Estados.add(e1);
    Estados.add(e2);
    EdoInicial = e1;
    e1.Transiciones.add(new Transicion(c, e2));
    // e2.EdoAcept se establece en true automáticamente en el constructor si token != -1
    EdosAcept.add(e2);
    Alfabeto.add(c);
    return this;
}

// Mantener el método original para compatibilidad

public AFN crearBasico(char c1, char c2, int token) {
    Estado e1 = new Estado();
    Estado e2 = new Estado(token);
    Estados.add(e1);
    Estados.add(e2);
    EdoInicial = e1;
    e1.Transiciones.add(new Transicion(c1, c2, e2));
    EdosAcept.add(e2);
    Alfabeto.add(c1);
    Alfabeto.add(c2);
    return this;
}

// Mantener el método original para compatibilidad
public AFN crearBasico(char c1, char c2) {
    return crearBasico(c1, c2, -1);
}

    public AFN unirAFN(AFN F2) {
        Estado e1 = new Estado();
        Estado e2 = new Estado();

        // epsilon desde e1 hacia inicios
        e1.Transiciones.add(new Transicion(SimbEspecial.EPSILON, this.EdoInicial));
        e1.Transiciones.add(new Transicion(SimbEspecial.EPSILON, F2.EdoInicial)); // también directo al otro
        // conectar aceptaciones anteriores a e2
        for (Estado e : this.EdosAcept) {
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, e2));
            e.EdoAcept = false;
        }
        for (Estado e : F2.EdosAcept) {
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, e2));
            e.EdoAcept = false;
        }

        this.EdoInicial = e1;
        this.Estados.addAll(F2.Estados);
        this.Estados.add(e1);
        this.Estados.add(e2);
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Alfabeto.addAll(F2.Alfabeto);
        return this;
    }

    public AFN concatenar(AFN F2) {
        // conectar las aceptaciones de this a las transiciones del inicio de F2 (sin duplicar estado inicial de F2)
        for (Estado e : this.EdosAcept) {
            for (Transicion t : F2.EdoInicial.Transiciones) {
                e.Transiciones.add(t);
            }
            e.EdoAcept = false;
        }
        this.EdosAcept.clear();
        this.EdosAcept.addAll(F2.EdosAcept);
        this.Alfabeto.addAll(F2.Alfabeto);

        // remover el estado inicial de F2 (puesto que sus transiciones fueron copiadas)
        F2.Estados.remove(F2.EdoInicial);
        this.Estados.addAll(F2.Estados);
        return this;
    }

    public AFN cerraduraPos() {
        Estado e1 = new Estado();
        Estado e2 = new Estado();

        for (Estado e : this.EdosAcept) {
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, this.EdoInicial));
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, e2));
            e.EdoAcept = false;
        }
        e1.Transiciones.add(new Transicion(SimbEspecial.EPSILON, this.EdoInicial));
        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);
        return this;
    }

    public AFN cerraduraKleene() {
        Estado e1 = new Estado();
        Estado e2 = new Estado();

        for (Estado e : this.EdosAcept) {
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, e2));
            e.EdoAcept = false;
            e.Transiciones.add(new Transicion(SimbEspecial.EPSILON, this.EdoInicial)); // para repetir
        }

        e1.Transiciones.add(new Transicion(SimbEspecial.EPSILON, this.EdoInicial));
        e1.Transiciones.add(new Transicion(SimbEspecial.EPSILON, e2)); // epsilon directo para aceptar epsilon
        this.EdoInicial = e1;
        this.EdosAcept.clear();
        this.EdosAcept.add(e2);
        this.Estados.add(e1);
        this.Estados.add(e2);
        return this;
    }

    // Cerradura epsilon de un estado
    public Set<Estado> CerraduraEpsilon(Estado e) {
        Set<Estado> C = new HashSet<>();
        Stack<Estado> P = new Stack<>();
        P.push(e);

        while (!P.empty()) {
            Estado e2 = P.pop();
            if (!C.contains(e2)) {
                C.add(e2);
                for (Transicion t : e2.Transiciones) {
                    if (t.SimboloInf == SimbEspecial.EPSILON) {
                        P.push(t.EdoDestino);
                    }
                }
            }
        }
        return C;
    }

    // Cerradura epsilon de un conjunto
    public Set<Estado> CerraduraEpsilon(Set<Estado> C) {
        Set<Estado> R = new HashSet<>();
        for (Estado e : C) {
            R.addAll(CerraduraEpsilon(e));
        }
        return R;
    }

    // Mover: desde un estado con símbolo c (rango incluido)
    public Set<Estado> Mover(Estado e, char c) {
        Set<Estado> R = new HashSet<>();
        for (Transicion t : e.Transiciones) {
            if (t.SimboloInf != SimbEspecial.EPSILON && t.acepta(c)) {
                R.add(t.EdoDestino);
            }
        }
        return R;
    }

    // Mover: desde conjunto
    public Set<Estado> Mover(Set<Estado> E, char c) {
        Set<Estado> R = new HashSet<>();
        for (Estado e : E) {
            for (Transicion t : e.Transiciones) {
                if (t.SimboloInf != SimbEspecial.EPSILON && t.acepta(c)) {
                    R.add(t.EdoDestino);
                }
            }
        }
        return R;
    }

    public Set<Estado> IrA(Estado e, char c) {
        return CerraduraEpsilon(Mover(e, c));
    }

    public Set<Estado> IrA(Set<Estado> E, char c) {
        return CerraduraEpsilon(Mover(E, c));
    }

    public AFD ConvertirAFD() {
        AFD AFD_Conv = new AFD();
        Set<EdoAFD> EdosAFD = new HashSet<>();
        Set<ElemSj> C = new HashSet<>();
        Queue<ElemSj> Q = new LinkedList<>();
        int NumElemSj = 0;
        int NumEdoAFD = 0;

        // Estado inicial del AFN -> conjunto inicial del AFD
        ElemSj SjIni = new ElemSj();
        SjIni.S = CerraduraEpsilon(this.EdoInicial);
        SjIni.Id = NumElemSj++;
        C.add(SjIni);
        Q.add(SjIni);

        // Crear primer estado del AFD
        EdoAFD EdoTempAFD = new EdoAFD(SjIni.Id);
        EdosAFD.add(EdoTempAFD);

        while (!Q.isEmpty()) {
            ElemSj SjAct = Q.poll();

            // Buscar el EdoAFD correspondiente al SjAct
            EdoAFD EdoAct = buscarEdoAFD(EdosAFD, SjAct.Id);

            for (char c : this.Alfabeto) {
                ElemSj SjAux = new ElemSj();
                SjAux.S = IrA(SjAct.S, c);

                if (SjAux.S.isEmpty()) {
                    continue; // no hay transición
                }
                // Revisar si este conjunto ya fue creado
                ElemSj existente = null;
                for (ElemSj e : C) {
                    if (e.S.equals(SjAux.S)) {
                        existente = e;
                        break;
                    }
                }

                if (existente == null) {
                    // Nuevo conjunto -> nuevo estado
                    SjAux.Id = NumElemSj++;
                    C.add(SjAux);
                    Q.add(SjAux);

                    // Crear nuevo EdoAFD
                    EdoAFD EdoNuevo = new EdoAFD(SjAux.Id);
                    EdosAFD.add(EdoNuevo);

                    // Registrar transición del estado actual
                    EdoAct.TransAFD[(int) c] = SjAux.Id;

                } else {
                    // Conjunto ya existe -> usar su ID
                    EdoAct.TransAFD[(int) c] = existente.Id;
                }
            }
        }

        // Asignar al AFD final
        AFD_Conv.NumEdos = EdosAFD.size();
        AFD_Conv.EdosAFD = new EdoAFD[AFD_Conv.NumEdos];
        AFD_Conv.Alfabeto = new HashSet<>(this.Alfabeto);

        int i = 0;
        for (EdoAFD e : EdosAFD) {
            AFD_Conv.EdosAFD[i++] = e;
        }

        return AFD_Conv;
    }

    private EdoAFD buscarEdoAFD(Set<EdoAFD> EdosAFD, int id) {
        for (EdoAFD e : EdosAFD) {
            if (e.id == id) {
                return e;
            }
        }
        return null;
    }
}
