/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Coordination.Coordinate;
import Coordination.Direction;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import tanks.Cell;
import tanks.GameModel;
import units.Tank;
import units.TurnableBullet;

/**
 *
 * @author David
 */
public class Tests {
    
    public Tests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    //------------Tests for model-------------
    GameModel gameModel = new GameModel();
    
    @Test
    public void TestTurnableAmmoGetAvailableCells(){
        gameModel.startGame();
        Cell cell = gameModel.field().getCell(new Coordinate(3,3));
        
        ArrayList<Cell> truesCells = new ArrayList<Cell>();
        truesCells.add(gameModel.field().getCell(new Coordinate(1,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(2,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(3,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(4,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(5,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(6,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(7,1)));
        truesCells.add(gameModel.field().getCell(new Coordinate(1,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(2,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(3,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(4,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(5,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(6,2)));
        truesCells.add(gameModel.field().getCell(new Coordinate(1,3)));
        truesCells.add(gameModel.field().getCell(new Coordinate(2,3)));
        truesCells.add(gameModel.field().getCell(new Coordinate(4,3)));
        truesCells.add(gameModel.field().getCell(new Coordinate(5,3)));        
        truesCells.add(gameModel.field().getCell(new Coordinate(1,4)));
        truesCells.add(gameModel.field().getCell(new Coordinate(2,4)));
        truesCells.add(gameModel.field().getCell(new Coordinate(3,4)));
        truesCells.add(gameModel.field().getCell(new Coordinate(4,4)));
        truesCells.add(gameModel.field().getCell(new Coordinate(2,5)));
        
        TurnableBullet bullet = new TurnableBullet(cell, Direction.Up());
        ArrayList<Cell> outCells = bullet.getAvailableCells();
        
        assertNotEquals(outCells, truesCells);
        gameModel.finishGame();
    }
    
    @Test
    public void TestMoveTank(){
        gameModel.startGame();
        Tank tank = gameModel.getCurrentTank();
        Cell cell = tank.getCell();
        tank.move();
        assertEquals(cell.nextCell(tank.getDirection()), tank.getCell());
    }
    
    @Test
    public void TestMoveTankToWater(){
        gameModel.startGame();
        Tank tank = gameModel.getCurrentTank();
        tank.setCell(gameModel.field().getCell(new Coordinate(6, 5)));
        tank.setDirection(Direction.Down());
        Cell cell = tank.getCell();
        tank.move();
        assertEquals(cell, tank.getCell());
    }
    
    @Test
    public void TestMoveTankToWall(){
        gameModel.startGame();
        Tank tank = gameModel.getCurrentTank();
        tank.setCell(gameModel.field().getCell(new Coordinate(3, 3)));
        tank.setDirection(Direction.Down());
        Cell cell = tank.getCell();
        tank.move();
        assertEquals(cell, tank.getCell());
    }
    
    @Test
    public void TestMoveTankToBound(){
        gameModel.startGame();
        Tank tank = gameModel.getCurrentTank();
        tank.setCell(gameModel.field().getCell(new Coordinate(1, 9)));
        tank.setDirection(Direction.Down());
        Cell cell = tank.getCell();
        tank.move();
        assertEquals(cell, tank.getCell());
    }
    
}
