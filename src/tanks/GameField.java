/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import units.Water;
import units.Wall;
import units.Tank;
import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import java.util.ArrayList;
import java.util.List;
import units.Fort;
/**
 *
 * @author David
 */
public class GameField {

    private int width;
    private int height;
    private GameModel _model;

    public GameField(int width, int height, GameModel model) {
        this.width = width;
        this.height = height;
        _model = model;
        //Создание ячеек
        generateCells();
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }

    //------------Cells--------------
    private Cell[][] field;

    /**
    *Сгенерировать матрицу ячеек
    */
    private void generateCells() {
        field = new Cell[height][width];

        //Заполнение матрицы ячеек
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                field[i][j] = new Cell(this);
            }
        }

        //Установка соседей ячеек
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                int cnt = 0;
                Direction curDir = Direction.Up();
                do {
                    int nextDirX = j + Direction.x[cnt];
                    int nextDirY = i + Direction.y[cnt];
                    if (nextDirX >= 0 && nextDirX < width && nextDirY >= 0 && nextDirY < height) {
                        field[i][j].SetCell(field[nextDirY][nextDirX], curDir);
                    }
                    curDir = curDir.Rotate(Rotation.Right());
                    ++cnt;
                } while (curDir.direct() != Direction.Up().direct());
            }
        }
    }

    
    /**
    *Вернуть ячейку по ее координате на поле
    */
    public Cell getCell(Coordinate coord) {
        if (coord.getX() > width || coord.getY() > height) {
            return null;
        }
        return field[coord.getY()-1][coord.getX()-1];
    }

    
    /**
    *Вернуть матрицу ячеек
    */
    public Cell[][] getCells(){
        return field;
    }
    
    
    /**
    *Вернуть координаты ячейки
    */
    public Coordinate getCoordinateCell(Cell cell){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(field[i][j] == cell){
                    return new Coordinate(j+1, i+1);
                }
            }
        }
        return null;
    }

    //---------------Tanks & Forts------------
    private Fort[] _forts = new Fort[2];
    private Tank[] _tanks = new Tank[2];
    private Cell[] _startPositios = new Cell[2];

    /**
    *Генерация баз и их танков
    */
    public void generateFortsAndTanks() {
        //Форты
        _forts[0] = new Fort(getCell(new Coordinate(1, 1)));
        _forts[1] = new Fort(getCell(new Coordinate(width, height)));
        
        //Стандартные стартовые позиции танков
        _startPositios[0] = _forts[0].getCell().nextCell(Direction.Right());
        _startPositios[1] = _forts[1].getCell().nextCell(Direction.Left());

        //Танки
        _tanks[0] = new Tank(_startPositios[0], Direction.Right(), _forts[0]);
        _tanks[1] = new Tank(_startPositios[1], Direction.Left(), _forts[1]);
    }

    
    /**
    *Установка нестандартных стартовых позиций
    */
    public void setStartPositions(Coordinate coordStartTank1, Coordinate coordStartTank2) {
        _startPositios[0] = getCell(coordStartTank1);
        _startPositios[1] = getCell(coordStartTank2);
        tankToStartPosition(_tanks[0]);
        tankToStartPosition(_tanks[1]);
    }

    public Tank[] getTanks() {
        return _tanks;
    }
    
    
    /**
    *Премещение танка к его стартовой позиции
    */
    public void tankToStartPosition(Tank tank) {
        for (int i = 0; i < _tanks.length; i++) {
            if(_tanks[i]==tank){
                tank.setCell(_startPositios[i]);
            }
        }        
    }
    //---------------Walls------------

    private ArrayList<Wall> _walls = new ArrayList<Wall>();

    public void generateWalls(List<Coordinate> coords) {

        for (int i = 0; i < coords.size(); i++) {
            _walls.add(new Wall(getCell(coords.get(i))));
        }
    }
    
    //--------------Waters------------
    private ArrayList<Water> _waters = new ArrayList<Water>();
    
    public void generateWaters(List<Coordinate> coords) {

        for (int i = 0; i < coords.size(); i++) {
            _waters.add(new Water(getCell(coords.get(i))));
        }
    }
    
    
    /**
    *Уничтожить поле
    */
    public void destroy(){
        //Уничтожить танки
        for(Tank tank:_tanks){
            tank.destroy();
        }
        //Уничтожить форты
        for(Fort f:_forts){
            f.destroy();
        }
        
        field = null;
        _walls = null;
        _tanks = null;
    }
}
