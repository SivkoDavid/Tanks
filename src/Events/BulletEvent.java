/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;


import java.util.EventObject;
import tanks.Bullet;
import tanks.Cell;

/**
 *
 * @author David
 */
public class BulletEvent extends EventObject{
    public Bullet _bullet;
    
    public BulletEvent(Object source, Bullet bullet)
    {
        super(source);
        _bullet = bullet;
    }
}
