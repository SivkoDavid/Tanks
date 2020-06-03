/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Events.BulletEvent;
import Coordination.Direction;
import java.util.ArrayList;
import Events.BulletListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author David
 */
public class Bullet{
    private Cell _cell;
    private Direction _direct;
    
    public Bullet(Cell cell) {
        _cell = cell;
    }
    
    public void moveToObstacle(Direction direct){        
        _direct = direct;
        Thread thr = new BulletMove(this);
        thr.start();
    }
    
    private static class BulletMove extends Thread {
        Bullet _bullet;
        public BulletMove(Bullet bullet) {
            _bullet = bullet;
        }
        
        @Override
        public void run() {
            //Если есть следующая ячейка
            if(_bullet._cell.nextCell(_bullet._direct) != null){
                _bullet._cell = _bullet._cell.nextCell(_bullet._direct);
                //Сообщить о перемещении
                _bullet.InformAboutMove();
                while(_bullet._cell.hereEmpty() && _bullet._cell.nextCell(_bullet._direct) != null){
                    try {
                        sleep(100);
                    } 
                    catch (InterruptedException ex) {}
                    _bullet._cell = _bullet._cell.nextCell(_bullet._direct);
                    //Сообщить о перемещении
                    _bullet.InformAboutMove();             
                }
            //Взорваться
            _bullet.exeplos();
            }
        }
    }

    
    //Взрыв снаряда
    public void exeplos(){
        //Сообщить о взрыве
        InformAboutExplosion();        
        if(_cell.hereTank()){
            _cell.getTank().explode();
        }
        
    }
    
    public Cell getCell(){
        return _cell;
    }
    
    public Direction getDirection(){
        return _direct;
    }
    
    // -- обработка слушателей --
    static private ArrayList<BulletListener> _listeners = new ArrayList<BulletListener>();
    
    public static void AddListener(BulletListener list)
    {
        _listeners.add(list);
    }
    
    public static void RemoveListener(BulletListener list)
    {
        _listeners.remove(list);
    }
    
    private void InformAboutExplosion()
    {
        BulletEvent event = new BulletEvent(this,this);
        for(BulletListener i : _listeners){
            i.ExplosiveBullet(event);
        }
    }
    
    private void InformAboutStart()
    {
        BulletEvent event = new BulletEvent(this,this);
        for(BulletListener i : _listeners){
            i.StartShootBullet(event);
        }
    }
    
    private void InformAboutMove()
    {
        BulletEvent event = new BulletEvent(this,this);
        for(BulletListener i : _listeners){
            i.MoveBullet(event);
            
        }
    }

    
}
