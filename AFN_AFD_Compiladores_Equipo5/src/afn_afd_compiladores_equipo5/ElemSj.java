/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package afn_afd_compiladores_equipo5;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Rogelio Colunga R
 */
public class ElemSj {

    public int Id;
    public Set<Estado> S = new HashSet<>();

    public ElemSj() {
        Id = -1;
        S = new HashSet<>();
        S.clear();
    }

    public ElemSj(int id, Set<Estado> conjunto) {
        this.Id = id;
        this.S = new HashSet<>(conjunto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ElemSj)) {
            return false;
        }
        ElemSj e = (ElemSj) o;
        return Objects.equals(S, e.S);
    }

    @Override
    public int hashCode() {
        return Objects.hash(S);
    }
}
