/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.EventListener;

/**
 *
 * @author David
 */
public interface TankListener extends EventListener {
    
    void ExplosiveTank(TankEvent e);
    
    void RotateTank(TankEvent e);
    
    void MoveTank(TankEvent e);
    
    void SkipStep(TankEvent e);
}
