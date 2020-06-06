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
public abstract class MovableUnit extends GameUnit{
    
    protected Direction _direct;
    
    public MovableUnit(Cell cell, Direction direction) {
        super(cell);
        _direct = direction;
        _HP = 1;
    }
    
    
    
    public void setDirection(Direction direction){
        _direct = direction;
    }
    
    public Direction getDirection(){
        return new Direction(_direct.direct());
    }
    
    public void rotate(Rotation rotation){
        _direct.Rotate(rotation);
    }
    
    public boolean move(){
        Cell newCell = _cell.nextCell(_direct);
        //Если можно переместиться
        if (newCell != null ) {
            _cell.remove(this);
            _cell = newCell;
            _cell.setUnit(this);
            return true;
        }
        return false;
    }
    
    public boolean hasMove(Direction direct){
        return _cell.nextCell(_direct) != null;
    }
}
