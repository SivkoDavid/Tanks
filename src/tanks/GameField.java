/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import java.util.List;

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
        generateCells();
    }

    //------------Cells--------------
    private Cell[][] field;

    private void generateCells() {
        field = new Cell[height][width];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                field[i][j] = new Cell(this);
            }
        }

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

    public Cell GetCell(Coordinate coord) {
        if (coord.getX() >= width || coord.getY() >= height) {
            return null;
        }

        return field[coord.getY()][coord.getX()];
    }

    public void explodeObjectInsideCell(Cell cell) {
        for (int i = 0; i < _tanks.length; i++) {
            if (_tanks[i].getCell() == cell) {

            }
        }
    }

    //---------------Tanks------------
    private Tank[] _tanks;
    private Cell[] _startPositios;

    private void generateTanks() {
        //Standart start positions
        _startPositios[0] = GetCell(new Coordinate(1, 1));
        _startPositios[1] = GetCell(new Coordinate(width, height));

        _tanks[0] = new Tank(this, _startPositios[0], _model);
        _tanks[1] = new Tank(this, _startPositios[1], _model);
    }

    public void setStartPositions(Coordinate coordStart1, Coordinate coordStart2) {
        _startPositios[0] = GetCell(coordStart1);
        _startPositios[1] = GetCell(coordStart2);
    }

    public Tank[] getTanks() {
        return _tanks;
    }
    
    public void tankToStartPosition(Tank tank) {
        for (int i = 0; i < _tanks.length; i++) {
            if(_tanks[i]==tank){
                tank.setCell(_startPositios[i]);
            }
        }        
    }
    //---------------Walls------------

    private Wall[] _walls;

    public void generateWalls(List<Coordinate> coords) {

        for (int i = 0; i < coords.size(); i++) {
            _walls[i] = new Wall(GetCell(coords.get(i)));
        }
    }
}
