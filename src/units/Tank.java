/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import Coordination.Direction;
import Coordination.Rotation;
import Events.TankEvent;
import java.util.ArrayList;
import Events.TankListener;
import java.util.ConcurrentModificationException;
import tanks.Cell;

/**
 *
 * @author David
 */
public class Tank extends MovableUnit{

    private final int COUNT_STAP_FOR_SHOOT = 3; //Кол-во ходов после которых есть возможность сделать выстрел
    private int _stepCount; //Счетчик ходов от последнего выстрела
    private Fort _fort;
    

    public Tank(Cell cell, Direction direction, Fort fort) {
        super(cell, direction);
        _HP = 3;
        _stepCount = 0;
        _fort = fort;
        _fort.setTank(this);
    }
    
    /**
     * Вернуть свой форт
     * @return 
     */
    public Fort getFort(){
        return _fort;
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
    
    /**
     * Выстрелить
     * @param ammo снаряд которым происходит выстрел
     * @return 
     */
    public boolean shoot(AbstractAmmo ammo) {
        if (isReadyShoot()) {
            InformAboutFire(this);
            ammo.shoot();
            _stepCount = 0;
            
            return true;
        }
        
        return false;
    }
   
    
    /**
     * Готовность стрелять
     * @return 
     */
    public boolean isReadyShoot(){
        return _stepCount >= COUNT_STAP_FOR_SHOOT;
    }

    
    /**
     * Перемещение
     * @return 
     */
    @Override
    public boolean move() {
        //Если можно переместиться
        if (canMove(_direct) && _cell.nextCell(_direct).getUnit() == null) {
            super.move();
            _stepCount++;
            InformAboutMove(this);
            return true;
        }
        return false;
    }

    /**
     * Пропустить ход
     */
    public void skipStep(){
        InformAboutSkip(this);
        _stepCount++;
    }
    
    
    @Override
    /**
     * Взрыв танка
     */
    public void explode(){
        super.explode();       
        InformAboutExplosion(this);        
    }
    
    @Override
    /**
     * Уничтожиться
     */
    public void destroy(){
        super.destroy();
        _listeners.clear();
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
    
    
}
