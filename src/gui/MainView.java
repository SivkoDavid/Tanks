/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.Random;
import javax.swing.JFrame;
import tanks.GameModel;

/**
 *
 * @author David
 */
public class MainView extends JFrame{
    Random random = new java.util.Random();
    private GameModel model;
    private GamePanel game;
    
    public MainView()
    {
        model = new GameModel();
        model.startGame();
        game = new GamePanel(model);
        game.setVisible(true);
    }
}
