/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import units.AbstractAmmo;
import units.Wall;
import units.GameUnit;
import units.Tank;
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
    private GameUnit _unit;
    private AbstractAmmo _ammo;
    
    
    Cell(GameField field)
    {
        _field = field;
    }
    
    public GameField field(){
        return _field;
    }
    
    public void SetCell(Cell cell, Direction direct){
        _NearbyCell[direct.direct()] = cell;
    }
    
    public void setUnit(GameUnit unit){
        if(unit instanceof AbstractAmmo){
            _ammo = (AbstractAmmo)unit;
        }
        else
            if(hereEmpty()){
                _unit = unit;
            }
    }
    
    public GameUnit getUnit(){
        return _unit;
    }
    
    public AbstractAmmo getAmmo(){
        return _ammo;
    }
    
    public Cell nextCell(Direction dir)
    {
        return _NearbyCell[dir.direct()];
    }
    
    
    public boolean hereEmpty(){
        return _unit == null;
    }
    
    public void remove(GameUnit unit){
        if(unit instanceof AbstractAmmo)
            _ammo = null;
        else
            _unit = null;
    }
    
    //Обработка взрыва в ячейке
    public void explode(){
        if(_unit != null){
            //Взорвать ее юнит, если он существует
            _unit.explode();
        }
    }
}
