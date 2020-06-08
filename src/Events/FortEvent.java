/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Events;
import java.util.EventObject;
import units.Fort;
/**
 *
 * @author David
 */
public class FortEvent extends EventObject{
    public Fort _fort;
    
    public FortEvent(Object source, Fort fort) {
        super(source);
        _fort = fort;
    }
    
}
