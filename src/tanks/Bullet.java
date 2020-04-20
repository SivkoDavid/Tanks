/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Direction;

/**
 *
 * @author David
 */
public class Bullet {
    private Cell _cell;
    
    private Tank myTank;
    private Tank enemyTank;
    
    public Bullet(Cell cell) {
        _cell = cell;
    }
    
    public void moveToObstacle(Direction direct){
        while(_cell.hereEmpty() && _cell.nextCell(direct) == null){
            _cell = _cell.nextCell(direct);
        }
        exeption();
    }
    
    public void exeption(){
        _cell.explode();
    }
    
}
