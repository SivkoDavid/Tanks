/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Coordinate;
import java.util.List;

/**
 *
 * @author David
 */
public class GameModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    //------------Field-------------
    
    private GameField _field;
    private Tank[] _tanks;
    private Tank _currentTank;
    private List<Coordinate> _wallsPositions;
    
    private void createGameField(){
        _field = new GameField(20,20);
        _tanks = _field.getTanks();
        
        _wallsPositions.add(new Coordinate(3,4));
        _wallsPositions.add(new Coordinate(3,5));
        _wallsPositions.add(new Coordinate(3,6));
        _wallsPositions.add(new Coordinate(4,6));
        _wallsPositions.add(new Coordinate(5,6));
        _wallsPositions.add(new Coordinate(5,7));
        
        _field.generateWalls(_wallsPositions);
    }
    
    //-----------Game---------------
    private void startGame(){
        
        
    }
    
    private void nextCurrentTank(){
        
    }
}
