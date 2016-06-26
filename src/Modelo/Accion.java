
package Modelo;

import Controladores.ControladorBatalla;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Accion {
    
    ControladorBatalla controlador;
    
    public Accion(ControladorBatalla cb){
        this.controlador = cb;
    }
   
    //En el siguiente método, el jugador1 es el que selecciona a su criatura para que
    //Ataque a la criatura2 del jugador2.
    protected void ataque(Criatura criatura1, Criatura criatura2){
        int cantidadAtk = criatura1.getAtaque();
        int cantidadDef = criatura2.getDefensa();
        int dañoResultante = cantidadAtk - cantidadDef;
        if(dañoResultante <= 0){
            int PV = criatura1.getPuntosDeVida();
            criatura1.setPuntosDeVida(PV + dañoResultante);
        }
        else {
            int PV = criatura2.getPuntosDeVida();
            criatura2.setPuntosDeVida(PV - dañoResultante);
        }
    }
    
    //En el siguiente método, el jefe de terreno del jugador1 es atacado por
    //una criatura del jugador2.
    public void ataqueJefe(JefeDeTerreno jefe, Criatura criatura){
        int PVJefe = jefe.getPuntosDeVida();
        int dañoRealizado = criatura.getAtaque();
        jefe.setPuntosDeVida(PVJefe - dañoRealizado);
    }
    public int[] moverCriatura(Criatura criatura, MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int rialX = x - 12;
        int rialY = y - 12;
        int coordX = (rialX / 25);
        int coordY = (rialY / 25);
        int[] coordenadas = {coordX, coordY};
        return coordenadas;
    }
    
    public void trampaOsos(Criatura criatura){
        criatura.setTrampa(21);
    }
    
    public void trampaLadrones(Criatura criatura){
        criatura.setTrampa(22);
    }
    public void renacerMuertos(Criatura criatura){
        criatura.setTrampa(23);
    }
    
    public void lanzamientoPnj(int jugador){
        PuzleDeDados dadosPnj = this.controlador.getPuzles()[jugador];
        List<String> seleccionadosPnj = new ArrayList<>();
        ArrayList<String> nombreDadosPnj = dadosPnj.getPuzleDeDados();
        int cantidadDados = nombreDadosPnj.size();
        if(cantidadDados > 4){
            for(int i = 0; i < 4; i++){
                String dadoLanzar = nombreDadosPnj.get(i);
                seleccionadosPnj.add(dadoLanzar);
            }
        }
        else{
            for(String dado: nombreDadosPnj){
                seleccionadosPnj.add(dado);
            }
        }
        this.controlador.setDadosSeleccionados(seleccionadosPnj);
        this.controlador.lanzarDados();
        
    }
    
    public int[] jefeCercano(){
        String jugadorActual = this.controlador.getCombate().getJugadorActual();
        if(jugadorActual.equals(this.controlador.getCombate().getJugador1())){
            int[] posicion = {7,0};
            return posicion;
        }
        else if(jugadorActual.equals(this.controlador.getCombate().getJugador2())){
            int [] posicion = {14, 7};
            return posicion;
            
        }
        else{
            int[] falta = {0,0};
            return falta;
        }
    }
    
    public void distanciaMasCorta(int[][] tablero,  int jugador){
        int posicionMasCercana;
        int[] jefeMasCercano = jefeCercano();
        int y = jefeMasCercano[0];
        int x = jefeMasCercano[1];
        double min = 1000;
        ArrayList<Integer[]> registros = new ArrayList<>();
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tablero[i][j] == jugador){
                    int der = tablero[i][j+1];
                    int izq = tablero[i][j-1];
                    int arr = tablero[i-1][j];
                    int aba = tablero[i+1][j];
                    if(der == 0){
                        double distanciaX = y-i;
                        double distanciaY = x-j+1;
                        int distanciaTotal = (int)Math.pow(distanciaX*distanciaX + distanciaY*distanciaY, 0.5);
                        if(distanciaTotal < min){
                            min = distanciaTotal;
                        }
                        Integer[] registro = {distanciaTotal, j+1, i};
                        registros.add(registro);
                    }
                    if(izq == 0){
                        double distanciaX = y-i;
                        double distanciaY = x-j-1;
                        int distanciaTotal = (int)Math.pow(distanciaX*distanciaX + distanciaY*distanciaY, 0.5);
                        if(distanciaTotal < min){
                            min = distanciaTotal;
                        }
                        Integer[] registro = {distanciaTotal, j-1, i};
                        registros.add(registro);
                    }
                    if(aba == 0){
                        double distanciaX = y-i+1;
                        double distanciaY = x-j;
                        int distanciaTotal = (int)Math.pow(distanciaX*distanciaX + distanciaY*distanciaY, 0.5);
                        if(distanciaTotal < min){
                            min = distanciaTotal;
                        }
                        Integer[] registro = {distanciaTotal, j, i+1};
                        registros.add(registro);
                    }
                    if(arr == 0){
                        double distanciaX = y-i-1;
                        double distanciaY = x-j;
                        int distanciaTotal = (int)Math.pow(distanciaX*distanciaX + distanciaY*distanciaY, 0.5);
                        if(distanciaTotal < min){
                            min = distanciaTotal;
                        }
                        Integer[] registro = {distanciaTotal, j, i-1};
                        registros.add(registro);
                    }
                    
                }
            }
        }
        for(Integer[] registro : registros){
            if(registro[0] == min){
                int xReal = registro[1];
                int yReal = registro[2];
                tablero[yReal][xReal] = jugador;
            }
        }
        
    }
    public void ataquePnj(int[][] tablero, Criatura[][] tableroCriatura){
        String actual = this.controlador.getCombate().getJugadorActual();
        if(actual == this.controlador.getCombate().getJugador1()){
            int cantidadAtaque = Integer.parseInt(this.controlador.getVistaBatalla().ataqueJ1.getText());
            if(cantidadAtaque > 0){
                ataque(tablero, tableroCriatura, 1);
            }
        }
        else if(actual == this.controlador.getCombate().getJugador2()){
            int cantidadAtaque = Integer.parseInt(this.controlador.getVistaBatalla().ataqueJ2.getText());
            if(cantidadAtaque > 0){
                ataque(tablero, tableroCriatura, 2);
            }
        }
        else if(actual == this.controlador.getCombate().getJugador3()){
            int cantidadAtaque = Integer.parseInt(this.controlador.getVistaBatalla().ataqueJ3.getText());
            if(cantidadAtaque > 0){
                ataque(tablero, tableroCriatura, 3);
            }
        }
        else if(actual == this.controlador.getCombate().getJugador4()){
            int cantidadAtaque = Integer.parseInt(this.controlador.getVistaBatalla().ataqueJ4.getText());
            if(cantidadAtaque > 0){
                ataque(tablero, tableroCriatura, 4);
            }
        }
    }
    public void ataque(int[][] tablero, Criatura[][] tableroCriatura, int jugador){
        for(int j = 0; j < 15; j++){
            for(int i = 0; i < 15; i++){
                if(tablero[j][i] == jugador){
                    int izq = tablero[j][i-1];
                    int der = tablero[j][i+1];
                    int aba = tablero[j+1][i];
                    int arr = tablero[j-1][i];
                    if(izq != jugador && izq != 0){
                        Criatura atacante = tableroCriatura[j][i];
                        Criatura defensora = tableroCriatura[j][i-1];
                        ataque(atacante, defensora);
                    }
                    if(der != jugador && der != 0){
                        Criatura atacante = tableroCriatura[j][i];
                        Criatura defensora = tableroCriatura[j][i+1];
                        ataque(atacante, defensora);
                    }
                    if(aba != jugador && aba != 0){
                        Criatura atacante = tableroCriatura[j][i];
                        Criatura defensora = tableroCriatura[j+1][i];
                        ataque(atacante, defensora);
                    }
                    if(arr != jugador && arr != 0){
                        Criatura atacante = tableroCriatura[j][i];
                        Criatura defensora = tableroCriatura[j-1][i];
                        ataque(atacante, defensora);
                    }
                }
            
            }
        }
    }
    
    public void invocacionPnj(int[][][] tablero, Criatura[][] tableroCriatura, Criatura criaturaInvocada, int jugador){
        int[] jefeMasCercano = jefeCercano();
        int y = jefeMasCercano[0];
        int x = jefeMasCercano[1];
        int[] minimo = {1000, 0, 0};
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(tablero[i][j][0] == jugador && tablero[i][j][1] != jugador){
                    double distanciaY = y-i;
                    double distanciaX = x-j;
                    int distancia = (int)Math.pow(distanciaY*distanciaY + distanciaX*distanciaX, 0.5);
                    if(distancia < minimo[0]){
                        int[] registro = {distancia, i, j};
                        minimo = registro;
                    }
                }
            }
            int newY = minimo[1];
            int newX = minimo[2];
            tablero[newY][newX][1] = jugador;
            tableroCriatura[newY][newX] = criaturaInvocada;
        }
    }
    
    
}
