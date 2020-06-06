/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import Coordination.Traectory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author David
 */
public class TurnableBullet extends AbstractAmmo{

    public TurnableBullet(Cell cell, Direction direction) {
        super(cell, direction);
        
        _maxLegth = 6;
        fromCell = cell;
        
    }

    @Override
    public void runShoot() {
        if(traectory!= null){
            for(Direction d: traectory){
                try {
                    bulletThread.sleep(200);
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
                if(_currCell.getUnit() == null && index<=legth ){
                    //Продолжаем рекурсию
                    buildAvailableCells(cells, _currCell, legth, index);
                }             
            }            
        }
    }
    
   
    
    //----------------------------
    //----------------------------
    
    
    public void buildMinPath(Cell to){
        Cell[][] _cells = _cell.field().getCells();
        int[][] matrix = new int[_cells.length][_cells[0].length];
        Coordinate coordTo = null;
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
        
        boolean matrixFilled = false;
        
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
                                
                                if(matrix[y][x] > nextNum || matrix[y][x] == -1){
                                    matrix[y][x] = nextNum;
                                }
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
        
        //----------------------------------------------------------
        String s="";
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                s += "["+matrix[i][j]+"]";
            }
            s+="\n";
        }
        System.out.println(s);
        //----------------------------------------------------------
        
        int legthPathTo = matrix[coordTo.getY()][coordTo.getX()];
        int x = coordTo.getX();
        int y = coordTo.getY();
        
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
                        traectory.add(0, new Direction(k).Reverse());
                        x = xd;
                        y = yd;
                        //Завершаем пробег по направлениям
                        k = 1000;
                    }
                }
            }
        }
        return;
    }
    
    
    //----------------------------
    //----------------------------
    
    
    
}
