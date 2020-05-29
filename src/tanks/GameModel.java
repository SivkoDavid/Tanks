/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import Events.TankEvent;
import java.util.ArrayList;
import java.util.List;
import Events.TankListener;

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
        GameModel model = new GameModel();
        model.createGameField();
        model.printField();
        model.startGame();
        
        
    }
    
    

    //------------Field-------------
    private GameField _field;
    private Tank[] _tanks;
    private Tank _currentTank;
    private ArrayList<Coordinate> _wallsPositions = new ArrayList<Coordinate>();

    void createGameField() {
        _field = new GameField(10, 10, this);
        _field.generateTanks();
        _tanks = _field.getTanks();
        listeningTanks();
        _field.setStartPositions(new Coordinate(1, 1), new Coordinate(7, 1));
        
        _currentTank = _tanks[0];
        
        _wallsPositions.add(new Coordinate(8, 1));
        _wallsPositions.add(new Coordinate(3, 4));
        _wallsPositions.add(new Coordinate(3, 5));
        _wallsPositions.add(new Coordinate(3, 6));
        _wallsPositions.add(new Coordinate(4, 6));
        _wallsPositions.add(new Coordinate(5, 6));
        _wallsPositions.add(new Coordinate(5, 7));

        _field.generateWalls(_wallsPositions);
    }

    //-----------Game---------------
    private void startGame() {
        
        _currentTank.move();        
        _currentTank.rotate(Rotation.Right());
        _currentTank.skipStep();
        _currentTank.move();         
        _currentTank.rotate(Rotation.Left());        
        _currentTank.move();        
        _currentTank.move();
        _currentTank.move();
        _currentTank.shoot();
        _currentTank.skipStep();
        _currentTank.shoot(); 
        _currentTank.skipStep();
        _currentTank.shoot();             
            
        
    }

    private void nextCurrentTank() {
        for(int i = 0; i < _tanks.length; i++){
            if(_currentTank == _tanks[i]){
                if(i < _tanks.length-1)
                    _currentTank = _tanks[i+1];
                else 
                    _currentTank = _tanks[0];
                return;
            }
        }
        return;
    }

    void hitTank(Tank tank) {
        if (tank.getHP() > 0) {
            _field.tankToStartPosition(tank);
            nextCurrentTank();
        }
        else{
            theEnd();
        }        
    }
    
    void theEnd(){
        System.out.println("wins tank - "+_currentTank);
    }
    
    //----------------Observering--------------------
    TankListener list = new TankEventsForModel();
    
    private void listeningTanks(){
            Tank.AddListener(list);
    }
    
    public class TankEventsForModel implements TankListener{
        @Override
        public void ExplosiveTank(TankEvent e){
            hitTank(e._tank);
            System.out.println("hit tank "+e._tank);
            printField();
        }

        @Override
        public void RotateTank(TankEvent e) {
            System.out.println("rotate tank "+e._tank);
            printField();
        }

        @Override
        public void MoveTank(TankEvent e) {
            nextCurrentTank();
            System.out.println("move tank "+e._tank);
            printField();
        }

        @Override
        public void SkipStep(TankEvent e) {
            System.out.println("skip tank "+e._tank);
            nextCurrentTank();
        }
    }
    
    //------------------------------------------------
    
    
    void printField(){
        String str_field = "";
        for(int i = 0; i < _field.getCells().length; i++){
            for(int j = 0; j < _field.getCells()[i].length; j++){
                if(_field.getCells()[i][j].hereTank()){
                    if(_field.getCells()[i][j].getTank().getDirection().equals(Direction.Up()))
                        str_field += "[A]"; 
                    else if(_field.getCells()[i][j].getTank().getDirection().equals(Direction.Left()))
                        str_field += "[<]";
                    else if(_field.getCells()[i][j].getTank().getDirection().equals(Direction.Right()))
                        str_field += "[>]";
                    else if(_field.getCells()[i][j].getTank().getDirection().equals(Direction.Down()))
                        str_field += "[V]";
                }
                else if(_field.getCells()[i][j].hereWall())
                    str_field += "[#]";
                else 
                    str_field += "[ ]";             
            }
            str_field += "\n";
        }
        System.out.println(str_field);
    }
}
