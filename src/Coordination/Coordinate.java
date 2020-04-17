/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordination;

/**
 *
 * @author str1k
 */
public class Coordinate {
    private int X, Y;
    
    public Coordinate(int x, int y)
    {
        X=x;
        Y=y;
    }
    
    public int getX()
    {
        return X;
    }
    
    public int getY()
    {
        return Y;
    }
    
    Coordinate Next(Direction dir)
    {
        return new Coordinate(X+Direction.x[dir.direct()],Y+Direction.y[dir.direct()]);
    }
}
