/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import tanks.Cell;

/**
 *
 * @author David
 */
public class TurnableBullet extends AbstractAmmo{
    
    

    public TurnableBullet(Cell cell, Direction direction) {
        super(cell, direction);
        RADIUS_EXPLOSION = 2;
        _maxLegth = 4;
        fromCell = cell;
        
    }

    @Override
    public void runShoot() {
        if(traectory!= null){
            //Перемещение в соответствии с траекорией
            for(Direction d: traectory){
                try {
                    bulletThread.sleep(100);
                } catch (InterruptedException ex) {}
                setDirection(d);
                move();
            }
            explode();
        }
        else{
            System.err.println("no traectory in "+this );
            destroy();
        }
    }
    
    //---------------------------
    //Логика построения траектории
    //---------------------------
    
    private Cell fromCell;
    private int _maxLegth;
    private ArrayList<ArrayList<Direction> > allPath;
    
    
    /**
     * Поиск доступных ячеек для снаряда
     * @return 
     */
    public ArrayList<Cell> getAvailableCells(){
        ArrayList<Cell> cells = new ArrayList<Cell>();
        
        buildAvailableCells(cells, fromCell, _maxLegth, 0);
        
        return cells;
        
    }
    
    /**
     * Сбор всех доступных для снавряда клеток
     * @param cells общий список доступных ячеек
     * @param curCell текущая яейка
     * @param legth максимальная длина пути
     * @param index текущий индекс
     */
    private void buildAvailableCells(ArrayList<Cell> cells,  Cell curCell, int legth, int index){
        index++;
        for(int i = 0; i<4; i++){
            Cell _currCell = curCell.nextCell(new Direction(i));
            //Если есть ячейка 
            if(_currCell != null && _currCell!= fromCell){
                //Добавить в список, если ранее не посещалась
                if(!cells.contains(_currCell))
                    cells.add(_currCell);
                if((_currCell.getUnit() == null ||(_currCell.getUnit() != null && _currCell.getUnit().getHP() == NOT_DESTROYED_AND_MISS )) 
                        && index<=legth ){
                    //Продолжаем рекурсию
                    buildAvailableCells(cells, _currCell, legth, index);
                }             
            }            
        }
    }
    
    /**
     * Посмтоение наикротчайшего пути
     * @param to ячейка в которую нужно построить путь
     */
    public void buildMinPath(Cell to){
        Cell[][] _cells = _cell.field().getCells();
        int[][] matrix = new int[_cells.length][_cells[0].length];//МАтрица кротчайших расстояний от текущей ячейки
        Coordinate coordTo = null;
        //Создание пустой матрицы
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                matrix[i][j] = -1;
                if(_cells[i][j]==fromCell)
                    matrix[i][j] = 0;
                else
                    if(_cells[i][j]==to)
                        coordTo = new Coordinate(j, i);
            }
        }        
        
        boolean matrixFilled = false;//Индикатор окончания заполнения матрицы
        
        //Заполнение матрицы кротчайших расстояний
        while(!matrixFilled){
            matrixFilled = true;
            for(int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix[i].length; j++){
                    
                    int thisNum = matrix[i][j];
                    Cell thisCell = _cells[i][j];
                    
                    if(thisNum != -1 && thisNum != -2){
                        
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
                                //Пометить позицию следующим значением
                                if(matrix[y][x] > nextNum || matrix[y][x] == -1){
                                    matrix[y][x] = nextNum;
                                }
                                //Если стена, пометить позицию -2
                                if(_cells[y][x]!=to &&( _cells[y][x].getUnit() instanceof Wall))
                                    matrix[y][x] = -2;
                            }
                        }
                    }
                    else if(thisNum == -1)
                        matrixFilled = false;
                }
            }
        }
        
       
        int legthPathTo = matrix[coordTo.getY()][coordTo.getX()];
        //Координаты целевой ячейки
        int x = coordTo.getX();
        int y = coordTo.getY();
        
        //Запись кротчайшего пути
        for(int currNum = legthPathTo; currNum > 0; currNum--){
            
            for(int k = 0; k < 4; k++){

                int xd = x;
                int yd = y;

                switch(k){
                    case 0:{
                        yd--;
                        break;
                    } 
                    case 1:{
                        xd++;
                        break;
                    } 
                    case 2:{
                        yd++;
                        break;
                    } 
                    case 3:{
                        xd--;
                        break;
                    } 
                }
                
                if(xd>=0 && xd < matrix[y].length && yd >=0 && yd < matrix.length){
                    if(matrix[yd][xd] == currNum-1){
                        //Так как проход пути идет от обратного, разворачиваем направления и записываем их в начало траектории
                        traectory.add(0, new Direction(k).Reverse());
                        x = xd;
                        y = yd;
                        //Завершаем пробег по направлениям
                        k = 4;
                    }
                }
            }
        }
    }
    
}
