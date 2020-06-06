/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordination;

import java.util.ArrayList;

/**
 *
 * @author David
 */

public class Traectory {
    private ArrayList<Direction> vectors;

    public Traectory() {
        vectors = new ArrayList<Direction>();
    }
    
    public void addVector(Direction direction){
        vectors.add(direction);
    }
    
    public void setPath(ArrayList<Direction> vectors){
        this.vectors = vectors;
    }
    
    public ArrayList<Direction> getPath(){
        return vectors;
    }
    
}
