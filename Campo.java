package br.com.letbarros.cm.modelo;

import br.com.letbarros.cm.excecao.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int linha;   //pq ta sendo aqui e nao no tabuleiro?
    private final int coluna;
    private boolean minado;  //ele tem o valor como falso
    private boolean aberto;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();

    Campo(int linha, int coluna) {  //pq pacote mesmo?
        this.linha = linha;
        this.coluna = coluna;
    }

    boolean adicionarVizinho(Campo vizinho){
        boolean linhaDiferente = linha != vizinho.linha;
        boolean colunaDiferente = coluna != vizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - vizinho.linha);
        int deltaColuna = Math.abs(coluna - vizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        if(deltaGeral == 1 && !diagonal) {
            vizinhos.add(vizinho);
            return true;
        } else if(deltaGeral == 2 && diagonal){
            vizinhos.add(vizinho);
            return true;
        } else {
            return false;
        }
    }
     void alternarMarcacao() {
        if(!aberto) {
            marcado = !marcado;
        }
     }

     boolean abrir(){
        if(!aberto && !marcado) {
            aberto = true;

            if(minado) {
                throw new ExplosaoException();
            }
            if(vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());

            }
            return true;
        } else {
            return false;
        }
     }

     boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v->v.minado);
     }

     void minar(){
            minado = true;
     }

     public boolean isMarcado(){
        return marcado;
     }

     public boolean isAberto(){
        return aberto;
     }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public boolean isFechado(){
        return !aberto;
    }

    public boolean isMinado(){
        return minado;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcancado(){
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    long minasNaVizinhanca(){ //long?
        return vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar(){
        aberto = false;
        minado = false;
        marcado = false;
    }

    public String toString(){
        if(marcado){
            return "x";
        } else if(aberto && minado){
            return "*";
        } else if(aberto && minasNaVizinhanca()>0) {
            return Long.toString(minasNaVizinhanca());
        } else if(aberto){
            return " ";
        } else {
            return "-";
        }
    }
}
