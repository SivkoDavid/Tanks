/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

/**
 *
 * @author David
 */
public abstract class GameUnit {
    protected Integer _HP;
    public static Integer NOT_DESTROYED_AND_NOT_MISS = -1;
    public static Integer NOT_DESTROYED_AND_MISS = -2;
    
    protected Cell _cell;
        
    public GameUnit(Cell cell) {
        _cell = cell;
        _HP = null;
        _cell.setUnit(this);
    }
    
    public void setCell(Cell cell){
        _cell.remove(this);
        _cell = cell;
        _cell.setUnit(this);
    }
    
    public Cell getCell(){
        return _cell;
    }
    
    public int getHP(){
        return _HP;
    }
}
