/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import Events.FortEvent;
import Events.FortListener;
import Events.TankEvent;
import Events.TankListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import tanks.Cell;

/**
 *
 * @author David
 */
public class Fort extends StaticUnit{
    
    private Tank _tank;
    
    public Fort(Cell cell) {
        super(cell);
    }
    
    public Tank getTank(){
        return _tank;
    }
    
    public void setTank(Tank tank){
        _tank = tank;
    }

    @Override
    public void explode() {
        InformAboutExplosion(this);
        super.explode();
        
    }
    
    public void destroy(){
        _listeners.clear();
    }
    
    
    //------Listeners----
    //События танка    
    static private ArrayList<FortListener> _listeners = new ArrayList<FortListener>();
    
    public static void AddListener(FortListener list)
    {
        _listeners.add(list);
    }
    
    public static void RemoveListener(FortListener list)
    {
        _listeners.remove(list);
    }
    
    public static void RemoveAllListener()
    {
        _listeners.clear();
    }
    
    private void InformAboutExplosion(Fort fort)
    {
        FortEvent event = new FortEvent(this, fort);
        try{
            for(FortListener i : _listeners){
                i.FortExplose(event);
            }
        }
        catch(ConcurrentModificationException e){}
    }
    
}
