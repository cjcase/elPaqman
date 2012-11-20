
package paqman;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

// Code by cjcase
public class Ghost extends Character {
    
    //Global Vars
    private Timer interval;
    private ArrayList<Integer> directions;
    private final int delaySec = 5;
    
    String right[][];
    String left[][];
    String up[][];
    String down[][];
    
    HashMap<String,ArrayList<String>> nodos;
    
    //Timed task class
    private class task extends TimerTask{
        @Override
        public void run() {
            createLpModel();
        }
    }  
  
    //Valores por defecto
    public Ghost(String path, Stage stage){
        super(path, stage);
        interval = new Timer();
        interval.scheduleAtFixedRate(new task(), 0, delaySec * 1000); //cada n segundos recalcular ruta
    }
    
  @Override
    public void run(){              
        while(true){
            move();
            try {
                runner.sleep(100);
            } catch (InterruptedException ex) {}
        }
    }
  
  private String createLpModel(){
      //here goes magic
      int x = stage.getMatrix_width();
      int y = stage.getMatrix_height();
      int[][] blocks = stage.getMatrix();
      float costoBase = 0.1f;
        
      right = new String[y][x - 1];
      left = new String[y][x-1];
      up = new String[y - 1][x];
      down = new String[y - 1][x];
      
      nodos = new HashMap<String, ArrayList<String>>();
      
      NumberFormat form = new DecimalFormat("000"); //Con esta regla, el escenario debe ser MxN =< 999
      String file;
      
      /* Registro de nodos bloqueados */
      HashMap<Integer, String> bloqueados = new HashMap<Integer, String>();
      for(int i = 0; i < y; i++){
          for(int j = 0; j < x; j++){
              if(blocks[i][j] == 1){
                  bloqueados.put((x*i)+j, form.format((x*i)+j));
              }
              nodos.put(form.format((x*i)+j), new ArrayList<String>());
          }
      }
      
      /* Generación de variables */
      //Conexiones a la derecha e izquierda
      for(int i = 0; i < y; i++){
          for(int j = 0; j < x - 1; j++){
              right[i][j] = "x" + form.format((x*i) + j) + form.format((x*i) + (j + 1));
              left[i][j] = "x" + form.format((x*i) + (j+1)) + form.format((x*i) + j);
              nodos.get(right[i][j].substring(1, 4)).add(right[i][j]);
              nodos.get(right[i][j].substring(4, 7)).add(right[i][j]);
              nodos.get(left[i][j].substring(1, 4)).add(left[i][j]);
              nodos.get(left[i][j].substring(4, 7)).add(left[i][j]);
          }
      }
      //Conexiones arriba y abajo
      for(int i = 0; i < y - 1; i++){
          for(int j = 0; j < x; j++){
              up[i][j] = "x" + form.format((x*(i+1)) + j) + form.format((x*i) + j);
              down[i][j] = "x" + form.format((x*i) + j) + form.format((x*(i+1)) + j);
              nodos.get(up[i][j].substring(1, 4)).add(up[i][j]);
              nodos.get(up[i][j].substring(4, 7)).add(up[i][j]);
              nodos.get(down[i][j].substring(1, 4)).add(down[i][j]);
              nodos.get(down[i][j].substring(4, 7)).add(down[i][j]);
          }
      }
      
      /* Acomodo de conexiones */
      for(int i = 0; i < x * y; i++){
          ArrayList<String> tmp = nodos.get(form.format(i));
          int itr = 0;
          int tmpSize = tmp.size();
          while(itr != tmpSize/2){
              String s = tmp.get(itr);
              if(!s.substring(1, 4).equals(form.format(i))){
                  String s2 = tmp.remove(itr);
                  tmp.add(s2);
              } else {
                  itr++; continue;
              }
          }
          nodos.remove(form.format(i));
          nodos.put(form.format(i), tmp);
      }

      
        file = "\n/*Modelo LP Auto-generado*/\n";

        /* Función Objetivo */
        file = file + "min:\n";

        //Derecha
        String aux = "\n";
        for(int i = 0; i < y; i++){
          for(int j = 0; j < x - 1; j++){
                float costo = costoBase;
                if(bloqueados.containsValue(right[i][j].substring(1, 4))){
                    costo = 10.0f;
                } else if(bloqueados.containsValue(right[i][j].substring(4, 7))){
                    costo = 10.0f;
                }
                aux = aux + costo + right[i][j] + " + ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Izquierda
        aux = "\n";
        for(int i = 0; i < y; i++){
          for(int j = 0; j < x - 1; j++){
                float costo = costoBase;
                if(bloqueados.containsValue(left[i][j].substring(1, 4))){
                    costo = 100.0f;
                } else if(bloqueados.containsValue(left[i][j].substring(4, 7))){
                    costo = 100.0f;
                }
                aux = aux + costo + left[i][j] + " + ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Arriba
        aux = "\n";
        for(int i = 0; i < y - 1; i++){
          for(int j = 0; j < x; j++){
              float costo = costoBase;
              if(bloqueados.containsValue(up[i][j].substring(1, 4))){
                    costo = 100.0f;
                } else if(bloqueados.containsValue(up[i][j].substring(4, 7))){
                    costo = 100.0f;
                }
                aux = aux + costo + up[i][j] + " + ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Abajo
        aux = "\n";
        for(int i = 0; i < y - 1; i++){
          for(int j = 0; j < x; j++){
              float costo = costoBase;
              if(bloqueados.containsValue(down[i][j].substring(1, 4))){
                    costo = 100.0f;
                } else if(bloqueados.containsValue(down[i][j].substring(4, 7))){
                    costo = 100.0f;
                }
              if(i == (y-1)-1 && j == x-1){
                aux = aux + costo + down[i][j] + " ; ";
              } else {
                aux = aux + costo + down[i][j] + " + ";
              }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        /* Registro Nodo Inicio y Nodo Final*/
        int myPos = (int)this.location.getX() + ((int)this.location.getY() * x);
        int pacPos = stage.getPacman_location();
        
        /* Restricciones Entradas = Salidas */
        aux = "\n/* Restriccones: Entradas = Salidas */\n";
        for(int i = 0; i < x*y; i++){
            //Nodo inicio
            if(i == myPos){
                aux = aux + "/* Nodo " + form.format(i) + " (Posición Inicial)*/\n";
                ArrayList<String> tmp = nodos.get(form.format(i));
                //Peor de los casos es nlogn, haciendo esto n2logn :(
                int tmpSize = tmp.size();
                for(int itr = 0; itr < tmpSize; itr++){
                    if(itr == 0){
                        aux = aux + tmp.get(itr);
                    } else if(itr < tmpSize/2){
                        aux = aux + " + " + tmp.get(itr); 
                    } else if(itr == tmpSize/2){
                        aux = aux + " = 1;\n" + tmp.get(itr);
                    } else if (itr > tmpSize/2){
                        aux = aux + " + " + tmp.get(itr);
                    }
                }
                aux = aux + " = 0;\n";
                continue;
            }
            //Nodo final
            if(i == pacPos){
                aux = aux + "/* Nodo " + form.format(i) + " (Posición Final)*/\n";
                ArrayList<String> tmp = nodos.get(form.format(i));
                //Peor de los casos es nlogn, haciendo esto n2logn :(
                int tmpSize = tmp.size();
                for(int itr = 0; itr < tmpSize; itr++){
                    if(itr == 0){
                        aux = aux + tmp.get(itr);
                    } else if(itr < tmpSize/2){
                        aux = aux + " + " + tmp.get(itr); 
                    } else if(itr == tmpSize/2){
                        aux = aux + " = 0;\n" + tmp.get(itr);
                    } else if (itr > tmpSize/2){
                        aux = aux + " + " + tmp.get(itr);
                    }
                }
                aux = aux + " = 1;\n";
                continue;
            }
            if(!bloqueados.containsValue(form.format(i))){
                aux = aux + "/* Nodo " + form.format(i) + " */\n";
                ArrayList<String> tmp = nodos.get(form.format(i));
                //Peor de los casos es nlogn, haciendo esto n2logn :(
                int tmpSize = tmp.size();
                for(int itr = 0; itr < tmpSize; itr++){
                    if(itr == 0){
                        aux = aux + tmp.get(itr);
                    } else if(itr < tmpSize/2){
                        aux = aux + " + " + tmp.get(itr); 
                    } else if (itr >= tmpSize/2){
                        aux = aux + " - " + tmp.get(itr);
                    }
                }
            } else { //Nodos Bloqueados
               aux = aux + "/* Nodo " + form.format(i) + " (bloqueado)*/\n";
                ArrayList<String> tmp = nodos.get(form.format(i));
                //Peor de los casos es nlogn, haciendo esto n2logn :(
                int tmpSize = tmp.size();
                for(int itr = 0; itr < tmpSize; itr++){
                    if(itr == 0){
                        aux = aux + tmp.get(itr);
                    } else if(itr < tmpSize/2){
                        aux = aux + " + " + tmp.get(itr); 
                    } else if(itr == tmpSize/2){
                        aux = aux + " = 0;\n" + tmp.get(itr);
                    } else if (itr > tmpSize/2){
                        aux = aux + " + " + tmp.get(itr);
                    }
                }
            }
            aux = aux + " = 0;\n";
        }
        file = file + aux;
        
        /* Variables Enteras */
        file = file + "\nint\n";
        //Derecha
        aux = "//Conexiones a la derecha\n";
        for(int i = 0; i < y; i++){
          for(int j = 0; j < x - 1; j++){
                aux = aux + right[i][j] + " , ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Izquierda
        aux = "//Conexiones a la izquierda\n";
        for(int i = 0; i < y; i++){
          for(int j = 0; j < x - 1; j++){
                aux = aux + left[i][j] + " , ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Arriba
        aux = "//Conexiones hacia arriba\n";
        for(int i = 0; i < y - 1; i++){
          for(int j = 0; j < x; j++){
              aux = aux + up[i][j] + " , ";
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //Abajo
        aux = "//Conexiones hacia abajo\n";
        for(int i = 0; i < y - 1; i++){
          for(int j = 0; j < x; j++){
              if(i == (y-1)-1 && j == x-1){
                aux = aux + down[i][j] + " ; ";
              } else {
                aux = aux + down[i][j] + " , ";
              }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        try {
            FileWriter fstream = new FileWriter("paq.lp");
            BufferedWriter fout = new BufferedWriter(fstream);
            fout.write(file);
            fout.close();
        } catch(IOException e){
          System.out.println(e.getMessage());
        }
        return file;
    }
    
}
