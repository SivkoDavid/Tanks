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
public interface ModelListener extends EventListener{
    void RebuildFieldEvent(ModelEvent e);
    
    void ChangeCurrentTank(ModelEvent e);
    
    void StartGame(ModelEvent e);
    
    void EndGame(ModelEvent e);
}
