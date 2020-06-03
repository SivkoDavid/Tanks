/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;

import java.util.EventObject;

/**
 *
 * @author David
 */
public class ModelEvent extends EventObject {
    public String _message;
    public ModelEvent(Object source, String message) {
        super(source);
        _message = message;
    }
    
}
