/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Direction;
import Coordination.Traectory;
import Events.BulletEvent;
import Events.BulletListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author David
 */
public abstract class AbstractAmmo extends MovableUnit{
    protected ArrayList<Direction> traectory = null;
    protected int RadiusAffect = 1;
    
    public AbstractAmmo(Cell cell, Direction direction) {
        super(cell, direction);
        this.traectory = new ArrayList<Direction>();
    }
    
    protected Thread bulletThread;
    
    
    public void shoot() {
        //Вызывает метод выстрела в ассинхронном режиме
        bulletThread = new Thread(new Runnable(){
            @Override
            public void run() {
                runShoot();
            }
        });
        bulletThread.start();
    }
    
    //Метод выполняющийся асинхронно
    public abstract void runShoot();
    
    @Override
    public boolean move() {
        boolean result = super.move();
        if(result)
            InformAboutMove();
        return result; 
    }
    
    protected void explode(){
        //Сообщить о взрыве
        bulletThread.interrupt();      
        _cell.remove(this);
        InformAboutExplosion(); 
        _cell.explode();
    }
    
    public void destroy(){
        _cell.remove(this);
        _cell=null;
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
        BulletEvent event = new BulletEvent(this, this._cell);
        for(BulletListener i : _listeners){
            i.ExplosiveBullet(event);
        }
    }
    
    private void InformAboutStart()
    {
        BulletEvent event = new BulletEvent(this, this._cell);
        for(BulletListener i : _listeners){
            i.StartShootBullet(event);
        }
    }
    
    private void InformAboutMove()
    {
        BulletEvent event = new BulletEvent(this, this._cell);
        for(BulletListener i : _listeners){
            i.MoveBullet(event);
            
        }
    }
}
