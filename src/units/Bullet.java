/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import Events.BulletEvent;
import Coordination.Direction;
import java.util.ArrayList;
import Events.BulletListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import tanks.Cell;

/**
 *
 * @author David
 */
public class Bullet extends AbstractAmmo{
    
    
    public Bullet(Cell cell, Direction direction) {
        super(cell, direction);
    }

    
    @Override
    public void runShoot(){
        //Если есть следующая ячейка
            if(_cell.nextCell(_direct) != null){
                move();
                while((_cell.getUnit() == null || (_cell.getUnit() != null && _cell.getUnit().getHP() == GameUnit.NOT_DESTROYED_AND_MISS)) && _cell.nextCell(_direct) != null){
                    try {
                        bulletThread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Bullet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    move();
                }
                //Взорваться
                explode();
            }
    }
}
