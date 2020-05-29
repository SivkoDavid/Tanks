/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Events.ExplosionsListener;
import Events.ExplosionEvent;
import Coordination.Direction;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class Bullet {
    private Cell _cell;
    
    public Bullet(Cell cell) {
        _cell = cell;
    }
    
    public void moveToObstacle(Direction direct){
        if(_cell.nextCell(direct) != null){
            _cell = _cell.nextCell(direct);
            while(_cell.hereEmpty() && _cell.nextCell(direct) != null){
                _cell = _cell.nextCell(direct);
            }            
            exeption();
        }
        
    }
    
    //Взрыв снаряда
    public void exeption(){
        InformAboutExplosion(_cell);
        if(_cell.hereTank()){
            _cell.getTank().explode();
        }
    }
    
    // -- обработка слушателей
    
    static private ArrayList<ExplosionsListener> _listeners = new ArrayList<ExplosionsListener>();
    
    public static void AddListener(ExplosionsListener list)
    {
        _listeners.add(list);
    }
    
    public static void RemoveListener(ExplosionsListener list)
    {
        _listeners.remove(list);
    }
    
    private void InformAboutExplosion(Cell cell)
    {
        ExplosionEvent event = new ExplosionEvent(this,cell);
        for(ExplosionsListener i : _listeners){
            i.ExplosiveBullet(event);
        }
    }
    
}
