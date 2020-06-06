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
public class Water extends StaticUnit{
    
    public Water(Cell cell) {
        super(cell);
        _HP = GameUnit.NOT_DESTROYED_AND_MISS;
    }
    
}
