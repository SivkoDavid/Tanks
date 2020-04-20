/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Coordination;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 *
 * @author str1k
 */
public class Rotation {
    private int _Rot;
    private Rotation(int Rot)
    {
        _Rot = Rot;
    }
    
    public static Rotation Left()
    {
        return new Rotation(-1);
    }
    
    public static Rotation Right()
    {
        return new Rotation(1);
    }
    
    public int DirRotate()
    {
        return _Rot;
    }
    
    
}
