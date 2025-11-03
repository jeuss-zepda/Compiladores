/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package afn_afd_compiladores_equipo5;

/**
 *
 * @author Rogelio Colunga R
 */
public class TransicionesAFD {
    
    public char Simbolo;
    public EdoAFD EdoDestino;
    
    public TransicionesAFD(char c, EdoAFD e) {
        Simbolo = c;
        EdoDestino = e;
    }
    
}
