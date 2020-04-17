/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Direction;
import Coordination.Rotation;

/**
 *
 * @author David
 */
public class Tank {

    private final int COUNT_STAP_FOR_SHOOT = 2;
    
    private Cell _cell;
    private Direction _direct;
    private int _stepCount;
    private int _healPoint;
    private GameField _field;
    
    public Tank(GameField field,Cell cell) {
        _field = field;
        _cell = cell;
        _direct = Direction.Up();
    }
    
    public void setCell(Cell cell){
        _cell = cell;
    }
    
    public Cell getCell(Cell cell){
        return _cell;
    }
    
    public void setDirect(Direction direct){
        _direct = direct;
    }
    
    //--------------actions------------
    public boolean shoot(){
        if(_stepCount >= COUNT_STAP_FOR_SHOOT){
            Bullet bullet = new Bullet(_cell);
            bullet.moveToObstacle(_direct);
            _stepCount = 0;
        }
        return false;
    }
    
    public boolean move(Direction direct){
        Cell newCell = _cell.nextCell(direct);
        if(newCell != null && newCell.hereEmpty()){
            _cell = newCell;
            _cell.setTank();
            return true;
        }        
        return false;
    }
    
    public void rotate(Rotation rotate){
        _direct.Rotate(rotate);
    }
}
