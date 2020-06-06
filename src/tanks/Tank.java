/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Direction;
import Coordination.Rotation;
import Events.TankEvent;
import java.util.ArrayList;
import Events.TankListener;
import java.util.ConcurrentModificationException;

/**
 *
 * @author David
 */
public class Tank extends MovableUnit{

    private final int COUNT_STAP_FOR_SHOOT = 3;
    private int _stepCount;

    public Tank(Cell cell, Direction direction) {
        super(cell, direction);
        _HP = 3;
        _stepCount = 0;
    }

    @Override
    public void rotate(Rotation rotation) {
        super.rotate(rotation);
        InformAboutRotate(this);
    }

    @Override
    public void setCell(Cell cell) {
        super.setCell(cell);
        InformAboutMove(this);
    }
    
    
    
    //--------------actions------------
    public boolean shoot(AbstractAmmo ammo) {
        if (isReadyShoot()) {
            InformAboutFire(this);
            ammo.shoot();
            _stepCount = 0;
            
            return true;
        }
        
        return false;
    }
   
    //Готовность стрелять
    public boolean isReadyShoot(){
        return _stepCount >= COUNT_STAP_FOR_SHOOT;
    }

    //Перемещение
    @Override
    public boolean move() {
        //Если можно переместиться
        if (hasMove(_direct) && _cell.nextCell(_direct).getUnit() == null) {
            super.move();
            _stepCount++;
            InformAboutMove(this);
            return true;
        }
        return false;
    }

    
    public void skipStep(){
        InformAboutSkip(this);
        _stepCount++;
    }
    
    public void takeLife(){
        _HP --;
    }
        
    public void explode(){
        takeLife();        
        InformAboutExplosion(this);        
    }
    
    //События танка    
    static private ArrayList<TankListener> _listeners = new ArrayList<TankListener>();
    
    public static void AddListener(TankListener list)
    {
        _listeners.add(list);
    }
    
    public static void RemoveListener(TankListener list)
    {
        _listeners.remove(list);
    }
    
    public static void RemoveAllListener()
    {
        _listeners.clear();
    }
    
    private void InformAboutExplosion(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        try{
            for(TankListener i : _listeners){
                i.ExplosiveTank(event);
            }
        }
        catch(ConcurrentModificationException e){}
    }
    
    private void InformAboutRotate(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        for(TankListener i : _listeners){
            i.RotateTank(event);
        }
    }
    
    private void InformAboutMove(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        for(TankListener i : _listeners){
            i.MoveTank(event);
        }
    }
    
    private void InformAboutSkip(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        for(TankListener i : _listeners){
            i.SkipStep(event);
        }
    }
    
    private void InformAboutFire(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        for(TankListener i : _listeners){
            i.FireTank(event);
        }
    }
    
    
    public void destroy(){
        _cell.remove(this);
        _cell = null;
        _listeners.clear();
    }
}
