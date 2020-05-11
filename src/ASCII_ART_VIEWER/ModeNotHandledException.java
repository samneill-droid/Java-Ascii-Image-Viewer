/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ASCII_ART_VIEWER;

/**
 *  A class to handle AsciiMode Exceptions 
 *  nothing special about his class
 */
public class ModeNotHandledException extends Exception
{
    public ModeNotHandledException(String error)
    {
        super(error);
    }
}
