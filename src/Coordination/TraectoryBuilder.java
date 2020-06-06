/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import tanks.Cell;
import tanks.GameField;

/**
 *
 * @author David
 */
public class TraectoryBuilder {
    private GameField _field;
    private Cell fromCell;
    private int _maxLegth;
    private ArrayList<ArrayList<Direction> > allPath;
    
    public TraectoryBuilder(GameField field, Cell from, int maxLegth) {
        _field = field;
        fromCell = from;
        _maxLegth = maxLegth;
        
    }
    
    public ArrayList<Cell> getAvailableCells(){
        ArrayList<Cell> cells = new ArrayList<Cell>();
        
        buildAvailableCells(cells, fromCell, _maxLegth, 0);
        
        return cells;
    }
    
    private void buildAvailableCells(ArrayList<Cell> cells,  Cell curCell, int legth, int index){
        index++;
        for(int i = 0; i<4; i++){
            Cell _currCell = curCell.nextCell(new Direction(i));
            if(_currCell != null && !cells.contains(_currCell) && _currCell!= fromCell){
                cells.add(_currCell);
                if(_currCell.getUnit() != null && index<=legth ){
                    //Продолжаем рекурсию
                    buildAvailableCells(cells, _currCell, legth, index);
                }             
            }            
        }
    }
    
    public Traectory buildTraectory(Cell to){
        ArrayList<Direction> dirs = new ArrayList<Direction>();
        ArrayList<Cell> cellPath = new ArrayList<Cell>();
        allPath = new ArrayList<ArrayList<Direction> >();        
        buildAllPath(dirs, cellPath, fromCell, to, _maxLegth, 0);
        int minLegth = 1000000;
        ArrayList<Direction> minVector = new ArrayList<Direction>();
        for(ArrayList<Direction> i : allPath){
            if(i.size() < minLegth){
                minLegth = i.size();
                minVector = i;
            }
        }
        Traectory traect = new Traectory();
        traect.setPath(minVector);
        return traect;
    }
    
    //Сбор всех путей не превышающих дальность траектории
    void buildAllPath(ArrayList<Direction> oldVector, ArrayList<Cell> cellPath, Cell curCell, Cell to,  int legth, int index){
        index++;
        Direction lastDir = oldVector.get(oldVector.size()-1);
        
        for(int i = 0; i<4; i++){
            //Если не пришли из этого направления и если в данной ветке не посещалась данная ячейка
            if(lastDir.direct() != i && cellPath.contains(curCell.nextCell(new Direction(i)))){
                //Переход в следующую ячейку
                Cell _currCell = curCell.nextCell(new Direction(i));
                if(_currCell != null){
                    //Копирование предыдущих векторов и их дополнение
                    ArrayList<Direction> d = (ArrayList<Direction>) oldVector.clone();
                    ArrayList<Cell> c = (ArrayList<Cell>) cellPath.clone();
                    d.add(new Direction(i));
                    c.add(_currCell);

                    //Если найдена ячейка
                    if(_currCell != null && _currCell == fromCell){
                        //Записываем вариант пути
                        allPath.add((ArrayList<Direction>) d.clone());
                        d.clear();
                        c.clear();
                    }
                    else 
                        if(_currCell.getUnit() != null && index<=legth ){
                            buildAllPath(d,c, _currCell, to, legth, index);
                        }
                        else{
                            d.clear();
                            c.clear();
                        }
                }
            }
        }
    }
    
}
