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
public class Wall {
    private Cell _cell;
    
    public Wall(Cell cell) {
        _cell = cell;
        _cell.setObjectInside(this);
    }
    
    public void setCell(Cell cell){
        _cell = cell;
    }
    
    public Cell getCell(Cell cell){
        return _cell;
    }
}
