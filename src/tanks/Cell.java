/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Direction;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class Cell {
    //public ArrayList<AbstractFieldUnit> fieldUnits = new ArrayList<AbstractFieldUnit>();
    
    private final Cell[] _NearbyCell = new Cell[Direction.NUMDIRECT];
    private GameField _field;
    
    private Tank _tank;
    private Wall _wall;
    
    
    Cell(GameField field)
    {
        _field = field;
    }
    
    public void SetCell(Cell cell, Direction direct){
        _NearbyCell[direct.direct()] = cell;
    }
    
    public boolean setObjectInside(Tank tank){
        if(hereEmpty()){
            _tank = tank;
            return true;
        }
        else
            return false;
    }
    
    public boolean setObjectInside(Wall wall){
        if(hereEmpty()){
            _wall = wall;
            return true;
        }
        else
            return false;
    }
    
    Cell nextCell(Direction dir)
    {
        return _NearbyCell[dir.direct()];
    }
    
    public boolean hereTank(){
        return _tank != null;
    }
    
    public boolean hereWall(){
        return _wall != null;
    }
    
    public boolean hereEmpty(){
        return !hereWall() && !hereTank();
    }
    
    public Tank getTank(){
        return _tank;
    }
    
    public void clean(){
        _tank = null;
        _wall = null;
    }
    
    public void explode(){
        if(hereTank()){
            _tank.destroy();
        }
    }
}
