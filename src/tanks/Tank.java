/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Events.ExplosionsListener;
import Events.ExplosionEvent;
import Coordination.Direction;
import Coordination.Rotation;
import Events.TankEvent;
import java.util.ArrayList;
import Events.TankListener;

/**
 *
 * @author David
 */
public class Tank {

    private final int COUNT_STAP_FOR_SHOOT = 0;

    private Cell _cell;
    private Direction _direct;
    private int _stepCount;
    private int _healPoint;
    private GameField _field;

    public Tank(GameField field, Cell cell) {
        _field = field;
        _cell = cell;
        _direct = Direction.Up();
        _healPoint = 3;
        _stepCount = 0;
        _cell.setObjectInside(this);
        System.out.println(this+" orig");
    }

    

    public void setCell(Cell cell) {
        _cell.clean();
        _cell = cell;
        _cell.setObjectInside(this);
    }

    public Cell getCell() {
        return _cell;
    }
    
    public int getHP(){
        return _healPoint;
    }

    public void setDirect(Direction direct) {
        _direct = direct;
    }

    public Direction getDirection(){
        return _direct;
    }
    //--------------actions------------
    public boolean shoot() {
        if (_stepCount >= COUNT_STAP_FOR_SHOOT) {
            Bullet bullet = new Bullet(_cell);
            bullet.moveToObstacle(_direct);
            _stepCount = 0;
            return true;
        }
        return false;
    }

    public boolean move() {
        Cell newCell = _cell.nextCell(_direct);
        
        if (newCell != null && newCell.hereEmpty()) {
            _cell.clean();
            _cell = newCell;
            _cell.setObjectInside(this);
            _stepCount++;
            InformAboutMove(this);
            return true;
        }
        return false;
    }

    public void rotate(Rotation rotate) {
        _direct.Rotate(rotate);
        InformAboutRotate(this);
    }
    
    public void skipStep(){
        InformAboutSkip(this);
    }
    
    public void takeLife(){
        _healPoint --;
    }
    
    public void destroy(){
        
    }
    
    public void explode(){
        takeLife();
        InformAboutExplosion(this);
        
    }
    
    // событие взрыва танка
    
    static private ArrayList<TankListener> _listeners = new ArrayList<TankListener>();
    
    public static void AddListener(TankListener list)
    {
        _listeners.add(list);
    }
    
    public static void RemoveListener(TankListener list)
    {
        _listeners.remove(list);
    }
    
    private void InformAboutExplosion(Tank tank)
    {
        TankEvent event = new TankEvent(this,tank);
        for(TankListener i : _listeners){
            i.ExplosiveTank(event);
        }
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
}
