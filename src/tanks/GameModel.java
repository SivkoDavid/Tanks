/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tanks;

import units.Tank;
import Coordination.Coordinate;
import Coordination.Direction;
import Coordination.Rotation;
import Events.FortEvent;
import Events.FortListener;
import Events.ModelEvent;
import Events.TankEvent;
import java.util.ArrayList;
import java.util.List;
import Events.TankListener;
import Events.ModelListener;
import units.Fort;

/**
 *
 * @author David
 */
public class GameModel {
    //------------Field-------------
    private GameField _field;
    private Tank[] _tanks;
    private Tank _currentTank;
    private boolean _gameRunning = false;
     

    //Создание игрового поля
    private void createGameField() {
        _field = new GameField(9, 9, this);
        _field.generateFortsAndTanks();
        _tanks = _field.getTanks();
        
        listeningTanksAndForts();        
        _currentTank = _tanks[0];
        
        //Позиции стен
        ArrayList<Coordinate> _wallsPositions = new ArrayList<Coordinate>();        
        _wallsPositions.add(new Coordinate(8, 1));
        _wallsPositions.add(new Coordinate(3, 4));
        _wallsPositions.add(new Coordinate(3, 5));
        _wallsPositions.add(new Coordinate(3, 6));
        _wallsPositions.add(new Coordinate(4, 6));
        _wallsPositions.add(new Coordinate(5, 6));
        _wallsPositions.add(new Coordinate(5, 7));
        _field.generateWalls(_wallsPositions);
        
        //Позиции воды
        ArrayList<Coordinate> _watersPositions = new ArrayList<Coordinate>(); 
        _watersPositions.add(new Coordinate(6, 6));
        _watersPositions.add(new Coordinate(7, 6));
        _watersPositions.add(new Coordinate(8, 6));
        _watersPositions.add(new Coordinate(8, 5));
        _watersPositions.add(new Coordinate(9, 5));
        _field.generateWaters(_watersPositions);
    }
    
    public GameField field(){
        return _field;
    }

    //-----------Game---------------
    
    //Начало игры
    public void startGame() {
        //Установить индикатор идущей игры
        _gameRunning = true;
        InformAboutStartGame("");
        createGameField(); 
    }
    
    public Tank getCurrentTank(){
        return _currentTank;
    }
    
    public Tank[] getTanks(){
        return _tanks;
    }

    //Сделать текущим следующий танк
    void nextCurrentTank() {
        for(int i = 0; i < _tanks.length; i++){
            if(_currentTank == _tanks[i]){
                if(i < _tanks.length-1)
                    _currentTank = _tanks[i+1];
                else 
                    _currentTank = _tanks[0];
                InformChangeCurrentTank("");
                return;
            }
        }        
        return;
    }

    //Отнять жизнь игрока
    private void hitTank(Tank tank) {
        if (tank.getHP() > 0) {
            _field.tankToStartPosition(tank);
            nextCurrentTank();
            printField();
        }
        else{
            theEnd(tank);
        }        
    }
    
    //Испустить событие изменения ситуации игры
    private void printField(){
        InformAboutRebuildField();
    }
    
    //Конец игры
    private void theEnd(Tank luseTank){
        
        String mess = "Выиграл красный танк";
        if(luseTank == _tanks[0])
            mess = "Выиграл синий танк";
        InformAboutEndGame(mess);
        finishGame();
    }
    
    //Прекращение игры
    public void finishGame(){
        _gameRunning = false;
        if(_tanks != null){
            _field.destroy();
            _tanks = null;
            _currentTank = null;
        }
    }
    
    public boolean gameIsRunning(){
        return _gameRunning;
    }
    
    //----------------Observering--------------------
    //---------События танка------------
    TankListener listenTanks = new TankEventsForModel();
    FortListener listenFort = new FortEvents();
    
    private void listeningTanksAndForts(){
            Tank.AddListener(listenTanks);
            Fort.AddListener(listenFort);
    }
    
    public class TankEventsForModel implements TankListener{
        @Override
        public void ExplosiveTank(TankEvent e){
            hitTank(e._tank);
            System.out.println("hit tank "+e._tank);
            
        }

        @Override
        public void RotateTank(TankEvent e) {
            System.out.println("rotate tank "+e._tank);
            printField();
        }

        @Override
        public void MoveTank(TankEvent e) {
            nextCurrentTank();
            System.out.println("move tank "+e._tank);
            printField();
        }

        @Override
        public void SkipStep(TankEvent e) {
            System.out.println("skip tank "+e._tank);
            nextCurrentTank();
        }

        @Override
        public void FireTank(TankEvent e) {
            System.out.println("fire tank "+e._tank);
            nextCurrentTank();
        }
    }
    
    //-------Fort event-------
    public class FortEvents implements FortListener{

        @Override
        public void FortExplose(FortEvent e) {
            theEnd(e._fort.getTank());
        }
        
    }
    
    
    //-------Event model------
    static private ArrayList<ModelListener> _listenersRebuild = new ArrayList<ModelListener>();
    
    public static void AddListener(ModelListener list)
    {
        _listenersRebuild.add(list);
    }
    
    public static void RemoveListener(ModelListener list)
    {
        _listenersRebuild.remove(list);
    }
    
    private void InformAboutRebuildField()
    {
        ModelEvent event = new ModelEvent(this, "");
        for(ModelListener i : _listenersRebuild){
            i.RebuildFieldEvent(event);
        }
    }
    
    private void InformChangeCurrentTank(String mess)
    {
        ModelEvent event = new ModelEvent(this, mess);
        for(ModelListener i : _listenersRebuild){
            i.ChangeCurrentTank(event);
        }
    }
    
    private void InformAboutStartGame(String mess)
    {
        ModelEvent event = new ModelEvent(this, mess);
        for(ModelListener i : _listenersRebuild){
            i.StartGame(event);
        }
    }
    
    private void InformAboutEndGame(String mess)
    {
        ModelEvent event = new ModelEvent(this, mess);
        for(ModelListener i : _listenersRebuild){
            i.EndGame(event);
        }
    }
    
}
