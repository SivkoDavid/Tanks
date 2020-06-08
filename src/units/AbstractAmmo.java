/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import Coordination.Coordinate;
import Coordination.Direction;
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
import tanks.Cell;

/**
 *
 * @author David
 */
public abstract class AbstractAmmo extends MovableUnit{
    protected ArrayList<Direction> traectory = null;
    protected int RADIUS_EXPLOSION = 1;
    
    public AbstractAmmo(Cell cell, Direction direction) {
        super(cell, direction);
        this.traectory = new ArrayList<Direction>();
    }
    
    protected Thread bulletThread;
    
    /**
    *Выстрелить
    */
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
    
    
    /**
     * Процесс передвижения пули после начала выстрела
     */
    public abstract void runShoot();
    
    @Override
    /**
     * 
     */
    public boolean move() {
        boolean result = super.move();
        if(result)
            InformAboutMove();
        return result; 
    }
    
    @Override
    public void explode(){
        bulletThread.interrupt(); 
        //Ячейки в которых произойдет взрыв
        ArrayList<Cell> explosiveCells = findExplosiveCells();
        _cell.remove(this);
        //Сообщить о предстоящих взрывах
        InformAboutExplosion(_cell); 
        for(Cell c:explosiveCells){
            InformAboutExplosion(c);    
        }
        //Взорвать все ячейки находящиеся в радиусе поражения
        _cell.explode();
        for(Cell c:explosiveCells){
            c.explode();    
        }
    }
    
    /**
     * уничтожиться
     */
    public void destroy(){
        _cell.remove(this);
        _cell=null;
        _listeners.clear(); 
    }
    
    /**
     * Сбор всех ячеек в радусе взрыва
     * @return 
     */
    private final ArrayList<Cell> findExplosiveCells(){
         
        Cell[][] _cells = _cell.field().getCells();
        int[][] matrix = new int[_cells.length][_cells[0].length];
        //Построение пустой матрицы
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                matrix[i][j] = -1;
                if(_cells[i][j]==_cell)
                    matrix[i][j] = 0;
            }
        }
        
        ArrayList<Cell> explosiveCells = new ArrayList<Cell>();
        
        //Заполнение матрицы взрыва
        for(int h = 0; h < RADIUS_EXPLOSION-1; h++){
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix[i].length; j++){
                    
                    int thisNum = matrix[i][j];
                    Cell thisCell = _cells[i][j];
                    
                    if(thisNum == h){
                        //Перебор всех направлений от выбраной позиции
                        for(int k = 0; k < 4; k++){
                            
                            int x = j;
                            int y = i;
                            
                            switch(k){
                                case 0:{
                                    y--;
                                } break;
                                case 1:{
                                    x++;
                                } break;
                                case 2:{
                                    y++;
                                } break;
                                case 3:{
                                    x--;
                                } break;
                            }
                            
                            if(x>=0 && x < matrix[i].length && y >=0 && y < matrix.length){
                                int nextNum = thisNum + 1;
                                //Заполнить следующим значением позицию
                                if(matrix[y][x] == -1){
                                    matrix[y][x] = nextNum;
                                    Cell nextCell = thisCell.nextCell(new Direction(k));
                                    if(explosiveCells.isEmpty() || (!explosiveCells.isEmpty() && !explosiveCells.contains(nextCell) && nextCell != _cell))
                                        //Добавить в общий список эту ячейку, если таковой еще нет в этом списке
                                        explosiveCells.add(nextCell);
                                }
                            }
                        }
                    }
                }
            }
        }
        return explosiveCells;
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
    
    protected void InformAboutExplosion(Cell cell)
    {
        BulletEvent event = new BulletEvent(this, cell);
        for(BulletListener i : _listeners){
            i.ExplosiveBullet(event);
        }
    }
    
    protected void InformAboutStart()
    {
        BulletEvent event = new BulletEvent(this, this._cell);
        for(BulletListener i : _listeners){
            i.StartShootBullet(event);
        }
    }
    
    protected void InformAboutMove()
    {
        BulletEvent event = new BulletEvent(this, this._cell);
        for(BulletListener i : _listeners){
            i.MoveBullet(event);
            
        }
    }
}
