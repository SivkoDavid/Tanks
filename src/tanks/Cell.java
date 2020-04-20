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
    private boolean hereTank;
    private boolean hereWall;
    
    
    Cell(GameField field)
    {
        _field = field;
        hereTank = false;
        hereWall = false;
    }
    
    public void SetCell(Cell cell, Direction direct)
    {
        _NearbyCell[direct.direct()] = cell;
    }
    
    Cell nextCell(Direction dir)
    {
        return _NearbyCell[dir.direct()];
    }
    
    public boolean hereTank(){
        return hereTank;
    }
    
    public boolean hereWall(){
        return hereWall;
    }
    
    public boolean hereEmpty(){
        return !hereWall && !hereTank;
    }
    
    public boolean setTank(){
        if(hereEmpty()){
            hereTank = true;
            return true;
        }
        return false;
    }
    
    public boolean setWall(){
        if(hereEmpty()){
            hereWall = true;
            return true;
        }
        return false;
    }
    
    public void explode(){
        _field.explodeObjectInsideCell(this);
    }
}
