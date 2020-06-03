/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Coordination.Coordinate;
import Coordination.Rotation;
import Events.BulletListener;
import Events.BulletEvent;
import Events.ModelEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tanks.GameModel;
import tanks.Cell;
import java.util.logging.Level;
import java.util.logging.Logger;
import tanks.Bullet;
import tanks.Tank;
import Events.ModelListener;
import java.awt.Point;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
/**
 *
 * @author str1k
 */
public class GamePanel extends JFrame implements KeyListener{
    private JPanel windowPanel = new JPanel();
    
    
    private final int CELL_SIZE = 50;
    
    private  GameModel _model;
    
    private JButton[][] _field;
    
    //Изображения
    private BufferedImage _explosionImg;
    private BufferedImage _cellImg;
    private BufferedImage _tankImg;
    private BufferedImage _wallImg;
    private BufferedImage _bulletImg;
    private BufferedImage _readyShootImg;
    private BufferedImage _notReadyShootImg;
    private BufferedImage _HPImg;
    
    //Корневая папка ресурсов
    private static final String folder = "./res/";    
   
    public GamePanel(GameModel model) {
        super();
        
        _model = model;
        
        // Листнеры
        GameModel.AddListener(new ListenerModel());
        addKeyListener(this);
        Bullet.AddListener(new ListeningBullet());
        
        this.setTitle("Танчики");
        
        //Загрузка изображений
        try
        {
            _cellImg = ImageIO.read(new File(folder + "Field/Cell.png"));
            _tankImg = ImageIO.read(new File(folder + "Tank1.png"));
            _wallImg = ImageIO.read(new File(folder + "Field/Wall.png"));
            _bulletImg = ImageIO.read(new File(folder + "Bullet.png"));
            _explosionImg = ImageIO.read(new File(folder + "explosion.png"));
            _readyShootImg = ImageIO.read(new File(folder + "ReadyShoot/ReadyShoot.png"));
            _notReadyShootImg = ImageIO.read(new File(folder + "ReadyShoot/NotReadyShoot.png"));
            _HPImg = ImageIO.read(new File(folder + "HP.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Создание панели информации
        windowPanel.add(createUpMenu());
        
        // Создание игрового поля
        windowPanel.add(createField());
        this.add(windowPanel);
        
        //Формирования меню игры
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu("Игра");
        fileMenu.insertSeparator(1);
        JMenuItem item1 = new JMenuItem("Новая");
        JMenuItem item2 = new JMenuItem("Управление");
        JMenuItem item3 = new JMenuItem("Выход");
        item3.addActionListener(new GameMenuListener());
        item2.addActionListener(new GameMenuListener());
        item1.addActionListener(new GameMenuListener());
        fileMenu.add(item1);
        fileMenu.add(item2);
        fileMenu.add(item3);
        menu.add(fileMenu);
        setJMenuBar(menu);
        
        pack();
        setResizable(false);
        
        //Обновления игровой информаци
        UpdateInfoGame();
    }
    
    //Окно с информацией об управлении
    private void about(){
        JOptionPane.showMessageDialog(null, "Управление\n"
                + "A --- Поворот против часовой стрелки.\n"
                + "D --- Поворот по часовой стрелке.\n"
                + "W --- Поехать на одну клетку в направлении движения.\n"
                + "F --- Выстрел в направлении движения.\n"            
                + "SPACE --- Пропуск хода.\n\n",
                "Об игре", JOptionPane.INFORMATION_MESSAGE); 
    }
    
    //Перезапуск игры (новая игра)
    void rerun(){
        //удаляем старые связи
        removeKeyListener(this);
        
        addKeyListener(this);
        
        //Завершение старой игры
        _model.finishGame();
        //Начало новой игры
        _model.startGame();
        //Удаление с поля старых элементов
        windowPanel.removeAll();        
        // Создание панели информации
        windowPanel.add(createUpMenu());        
        // Создание игрового поля
        windowPanel.add(createField());
        pack();
        UpdateInfoGame();        
        windowPanel.repaint();
    }
    
    private class GameMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if ("Управление".equals(command)) {
                about();
            }
            else if ("Новая".equals(command)) {
                try{ 
                    rerun(); 
                }
                catch(Throwable t)
                {};
            }
            else if ("Выход".equals(command)) {
                System.exit(0);
            }
        }  
    }
    
    private JPanel createUpMenu()
    {
        JPanel UpperMenu = new JPanel();
        
        //Панель текущего игрока (цвета)
        JPanel ColorPanel = new JPanel();
        UpperMenu.setLayout(new BoxLayout(UpperMenu, BoxLayout.Y_AXIS));
        UpperMenu.add(ColorPanel);
        UpperMenu.setPreferredSize(new Dimension(150,300));
        Font font = new Font("Courier",0,23);
        JPanel PointPanel = new JPanel();
        UpperMenu.add(PointPanel);
        PointPanel.setPreferredSize(new Dimension(130,100));
        PointPanel.setBorder(BorderFactory.createTitledBorder("Текущий игрок"));
        
        //Панель готовности к выстрелу
        JButton But = new JButton();
        PointPanel.add(But); 
        But.setPreferredSize(new Dimension(50,50));
        But.setFocusable(false);
        But.setBorderPainted(false);
        But.setFont(font);
        JPanel CooldownPanel = new JPanel();
        UpperMenu.add(CooldownPanel);
        CooldownPanel.setPreferredSize(new Dimension(130,100));
        CooldownPanel.setBorder(BorderFactory.createTitledBorder("Готов стрелять"));
        But = new JButton();
        CooldownPanel.add(But);
        
        //Панель жизней
        JPanel HPBar = new JPanel();
        HPBar.setPreferredSize(new Dimension(100,40));
        HPBar.setBorder(BorderFactory.createTitledBorder(""));
        But.setPreferredSize(new Dimension(CELL_SIZE,CELL_SIZE));
        But.setFocusable(false);
        But.setBorderPainted(false);
        UpperMenu.add(HPBar);
        JLabel canv = new JLabel();
        canv.setPreferredSize(new Dimension(90,20));
        canv.setLocation(new Point(0,0));
        HPBar.add(canv);
        
        return UpperMenu;
    }
    
    //Обновление поля иформации
    private void UpdateInfoGame()
    {
        
        JPanel UpperPanel = (JPanel)windowPanel.getComponent(0);
        JPanel PointPanel = (JPanel)UpperPanel.getComponent(1);
        JPanel CooldownPanel = (JPanel)UpperPanel.getComponent(2);
        JPanel HPPanel = (JPanel)UpperPanel.getComponent(3);
        JLabel HPConvas = (JLabel)HPPanel.getComponent(0);
        
        JButton But2 = (JButton)PointPanel.getComponent(0);
        JButton But3 = (JButton)CooldownPanel.getComponent(0);
        
        Color color = Color.RED;
        if(_model.getCurrentTank()==_model.getTanks()[1]){
            color = Color.BLUE;
        }
        But2.setEnabled(false);
        But2.setBackground(color);
        
        if(_model.getCurrentTank().isReadyShoot()){
            But3.setIcon(new ImageIcon(_readyShootImg));
        }
        else
            But3.setIcon(new ImageIcon(_notReadyShootImg));
        
        HPConvas.setIcon(new ImageIcon(getHPImgTank(_model.getCurrentTank())));
    }
    
    //Создание игрового поля
    private JPanel createField(){
        JPanel fieldPanel = new JPanel();
        fieldPanel.setDoubleBuffered(true);
        fieldPanel.setLayout(new GridLayout(_model.field().height(), _model.field().width()));
        Dimension fieldDimension = new Dimension(CELL_SIZE*_model.field().height(), CELL_SIZE*_model.field().width());
        fieldPanel.setPreferredSize(fieldDimension);
        fieldPanel.setMinimumSize(fieldDimension);
        fieldPanel.setMaximumSize(fieldDimension);
        
        //Заполнение поля ячейками
        fieldPanel.removeAll();
        _field = new JButton[_model.field().height()][_model.field().width()];
        for (int row = 0; row < _model.field().height(); row++) 
        {
            for (int col = 0; col < _model.field().width(); col++) 
            {
                JButton button = new JButton("");
                _field[row][col] = button;
                fieldPanel.add(button);
            }
        }
        fieldPanel.validate();
        repaintField();
        
        return fieldPanel;
    }
    
    //Переписовка игрового поля
    private void repaintField() {
        for (int row = 0; row < _model.field().height(); row++){
            for (int col = 0; col < _model.field().width(); col++) 
            {       
                Cell curCell = _model.field().getCell(new Coordinate(col+1, row+1));   
                _field[row][col].setIcon(new ImageIcon(GetCellImage(_model.field().getCells()[row][col])));
                _field[row][col].setFocusable(false);
                _field[row][col].setBorderPainted(false);
            }
        }
    }
    
    //Генерация изображения для определенной ячейки
    private BufferedImage GetCellImage(Cell curCell){
        BufferedImage cellImg = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        cellImg.getGraphics().drawImage(_cellImg, 0, 0, null);
        if(curCell.hereTank()){                
             cellImg.getGraphics().drawImage(getImgTank(curCell.getTank()), 0, 0, null);
        }
        if(curCell.hereWall()){
            cellImg.getGraphics().drawImage(_wallImg, 0, 0, null);
        }
        return cellImg;
        
    }
    
    //Генерация изображения жизней танка
    private BufferedImage getHPImgTank(Tank tank){
        BufferedImage tankImg = new BufferedImage(100, 40, BufferedImage.TYPE_INT_ARGB);        
        Graphics2D g = tankImg.createGraphics();        
        for(int i = 0; i< tank.getHP(); i++){
            g.drawImage(_HPImg, i*30, 10, null);
        }        
        return tankImg;
    }
    
    //Генерация изображения танка
    private BufferedImage getImgTank(Tank tank){
        BufferedImage tankImg = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
                
        Color color = Color.RED;
        if(tank == _model.getTanks()[1])
            color = Color.BLUE;
        Graphics2D g = tankImg.createGraphics();
        g.rotate(tank.getDirection().direct()*Math.PI/2, 25, 25);
        g.drawImage(_tankImg, 0, 0, null);
        g.setColor(color);
        g.fillRect(6, 8, 6, 34);
        g.fillRect(38, 8, 6, 34);
        g.fillRect(23, 1, 5, 2);
        return tankImg;
    }
    
    //Отрисока снаряда
    private void paintBullet(Bullet bullet){
        //Отрисока поля без снаряда
        repaintField();
        
        //Отрисока снаряда
        Coordinate coord = _model.field().getCoordinateCell(bullet.getCell());
        BufferedImage img = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        _field[coord.getY()-1][coord.getX()-1].getIcon().paintIcon(this, g, 0, 0);        
        g.rotate(bullet.getDirection().direct()*Math.PI/2, 25, 25);
        g.drawImage(_bulletImg, 0, 0, null);
        _field[coord.getY()-1][coord.getX()-1].setIcon(new ImageIcon(img));
    }
      
    //Открыть панель окончания игры
    private void openEndGame(String message){
        JPanel panel = (JPanel)windowPanel;
        //panel.setLayout(new GridLayout(1,1));
        
        panel.removeAll();
        
        JLabel _endGameLabel = new JLabel(message);
        _endGameLabel.setVerticalAlignment(JLabel.CENTER);
        _endGameLabel.setHorizontalAlignment(JLabel.CENTER);
        Dimension dimen = new Dimension(panel.getWidth(),panel.getHeight());
        _endGameLabel.setPreferredSize(dimen);
        
        _endGameLabel.setOpaque(true);
        panel.add(_endGameLabel);
        panel.validate();
        
    }
    
    //Отрисока взрыва
    private void PrintExplose(Bullet bullet)    {
        Cell cell = bullet.getCell();
        Coordinate coord = _model.field().getCoordinateCell(cell);
        BufferedImage img = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        _field[coord.getY()-1][coord.getX()-1].getIcon().paintIcon(this, g, 0, 0);
        g.drawImage(_explosionImg, 0, 0, null);
        _field[coord.getY()-1][coord.getX()-1].setIcon(new ImageIcon(img));
        
        try { 
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        _field[coord.getY()-1][coord.getX()-1].setIcon(new ImageIcon(GetCellImage(cell)));
    }
    
    //Конец игры
    public void endGame(String message){
        removeKeyListener(this);
        openEndGame(message);
    }
    
    //----------------Observering--------------------
    
    //События модели
    public class ListenerModel implements ModelListener{

        @Override
        public void RebuildFieldEvent(Events.ModelEvent e) {
            repaintField();
        }

        @Override
        public void ChangeCurrentTank(ModelEvent e) {
            UpdateInfoGame();
        }

        @Override
        public void StartGame(ModelEvent e) {
            
        }

        @Override
        public void EndGame(ModelEvent e) {
            endGame(e._message);
        }
        
    }
    
    //----Взрыв-----------   
    public class ListeningBullet implements BulletListener{

        @Override
        public void ExplosiveBullet(BulletEvent e) {
            PrintExplose(e._bullet);
        }

        @Override
        public void MoveBullet(BulletEvent e) {
            paintBullet(e._bullet);
            System.out.println("move bullet");
        }

        @Override
        public void StartShootBullet(BulletEvent e) {
            
        }
        
    }
    
    
   @Override
    public void keyPressed(KeyEvent e)
    {
        
        if(_model.field()!= null && _model.getCurrentTank()!= null)
        {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    _model.getCurrentTank().rotate(Rotation.Left());
                    break;
                case KeyEvent.VK_D:
                    _model.getCurrentTank().rotate(Rotation.Right());                  
                    break;
                case KeyEvent.VK_W:
                    _model.getCurrentTank().move();                   
                    break;
                case KeyEvent.VK_F:
                    _model.getCurrentTank().shoot();
                    break;
                case KeyEvent.VK_SPACE:
                    _model.getCurrentTank().skipStep();
                    break;
                
                default:
                    break;
            }
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
