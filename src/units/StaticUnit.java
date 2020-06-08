/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import tanks.Cell;

/**
 *
 * @author David
 */
public abstract class StaticUnit extends GameUnit{
    
    public StaticUnit(Cell cell) {
        super(cell);
        //Стандартно у статических объектов есть одна жизнь
        _HP = 1;
    }
    
}
