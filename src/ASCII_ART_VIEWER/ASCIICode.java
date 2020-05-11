/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ASCII_ART_VIEWER;

/**
 *
 * @author Sam
 */
public class ASCIICode 
{

    // a string of ascii characters that go from least bright to most bright 
    // this is based on how much of character takes up screen space relative to the emtpy sapce around it    
    public final static String  ASCIICHARACTERS = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
    
    // returns an ascii character from the ASCIICHARACTERS string based on a brightness value
    // param: int brightnessValue is a number between 0 - 255
    // converts the brigtness value from 0 - 255 to 0 - 64 
    // then returns an ascii character related to that value     
    public static char getAsciiChar(int brightnessValue)
    {            
        return ASCIICHARACTERS.charAt((int)Math.round((((double)brightnessValue)/255)*64));
    }
    
}
