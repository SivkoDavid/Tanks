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
public class Wall extends StaticUnit{
    private Cell _cell;
    
    public Wall(Cell cell) {
        super(cell);
        _HP = NOT_DESTROYED_AND_NOT_MISS;
    }
}
