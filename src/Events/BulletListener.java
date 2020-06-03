/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.BulletEvent;
import java.util.EventListener;

/**
 *
 * @author David
 */
public interface BulletListener extends EventListener{
    void ExplosiveBullet(BulletEvent e);
    
    void MoveBullet(BulletEvent e);
    
    void StartShootBullet(BulletEvent e);
}
