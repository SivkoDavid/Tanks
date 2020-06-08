/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.ArrayList;
import java.util.EventObject;
import tanks.Cell;
import units.Tank;

/**
 *
 * @author David
 */
public class TankEvent extends EventObject {
     public Tank _tank;
    
    public TankEvent(Object source, Tank tank)
    {
        super(source);
        _tank = tank;
        
    }
}
