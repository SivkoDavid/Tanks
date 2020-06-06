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
public class Direction {
    
    public static final int NUMDIRECT = 4;
    public final static int[] x = {0,1,0,-1};
    public final static int[] y = {-1,0,1,0};
    
    private int _dir;
    
    public Direction(int curDir)
    {
        _dir = curDir;
    }
    
    public static Direction Up()
    {
        return new Direction(0);
    }
    
    public static Direction Right()
    {
        return new Direction(1);
    }
    
    public static Direction Down()
    {
        return new Direction(2);
    }
    
    public static Direction Left()
    {
        return new Direction(3);
    }
    
    public Direction Rotate(Rotation rot)
    {
        Direction dir_rot = new Direction((_dir+rot.DirRotate()+NUMDIRECT)%NUMDIRECT); 
        _dir = dir_rot.direct();
        return  dir_rot;
    }
    
    public Direction Reverse()
    {
        int newDir = 0;
        switch(_dir){
            case 0:{
                newDir = 2;
                break;}
            case 1:{
                newDir = 3;
                break;}
            case 2:{
                newDir = 0;
                break;}
            case 3:{
                newDir = 1;
                break;}
        }    
        return  new Direction(newDir);
    }
    
    public int direct()
    {
        return _dir;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return this._dir==((Direction)obj)._dir;
    }
}
