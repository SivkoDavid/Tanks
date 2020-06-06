/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;


import java.util.EventObject;
import tanks.AbstractAmmo;
import tanks.Bullet;
import tanks.Cell;

/**
 *
 * @author David
 */
public class BulletEvent extends EventObject{
    public Cell _cell;
    
    public BulletEvent(Object source, Cell cell)
    {
        super(source);
        _cell = cell;
    }
}
