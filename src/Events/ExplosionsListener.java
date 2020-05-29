/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import Events.ExplosionEvent;
import java.util.EventListener;

/**
 *
 * @author David
 */
public interface ExplosionsListener extends EventListener{
    void ExplosiveBullet(ExplosionEvent e);
}
