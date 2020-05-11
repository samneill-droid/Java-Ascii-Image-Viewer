
package ASCII_ART_VIEWER;

import java.awt.Color;
/**
 *  The AsciiImage class contains all the data that is needed for the GUI to render the 
 *  Ascii Image. As well as having a few methods to manipulate that data.
 */
public class AsciiImage   
{    
    // height of the image
    private int height;
    // width of the image
    private int width;
    // a 2D array that stores the brightness values 
    private int[][] charImage;
    // a 2D array that holds all the color data for each "ascii pixel"
    private Color [][] colorMap; 
    
    public AsciiImage(int[][] image,int height,int width,Color[][] colorMap)
    {
        this.height = height;
        this.width = width;
        charImage = image;
        this.colorMap = colorMap;        
    }
    
    public int[][] getAsciiString()
    {
        return charImage;
    }
            
    public Color[][] getColorMap()
    {
        return colorMap;
    }            
    
    public int getHeight() {return height;}
    
    public int getWidth() {return width;}
    
    public void Brighten()
    {
        for(int y = 0;y < height;y++)
        {
            for(int x = 0;x < width;x++)
            {
                if(charImage[y][x] < 255)
                {
                    charImage[y][x]++;
                }
            }
        }
    }
    // this sets the brightness values to a new value set
    public void setBrightnessValue(int[][] values)
    {
        charImage= values;
    }
    // this creates a new brightness value set depending on the AsciiMode that is passed
    // Average Mode creates a brightness value by averaging the RGB values
    // Perspective mode creates a brightness value by using a weighting the RGB values
    // Min Max mode creates a brightness value by averageing the smallest and largets RBG values
    // Matrix mode creates a brigtness value by taking the average of the RBG values
    // it then checks to see if is higher then 127 if it is it then it scales it to a higher value
    // if it is 127 and lower then it is scaled to a lower value  
    public int[][] getBrightnessValues(AsciiMode mode) 
    {
        int[][] tempImage = new int[height][width];
        try
        {            
        
        switch(mode)
        {
            
            case AVERAGE_BRIGHTNESS_MODE:
                for(int y = 0;y < height;y++)
                {
                    for(int x =0;x < width;x++)
                    {
                        Color col = colorMap[y][x];                        
                        tempImage[y][x] = (col.getBlue() + col.getGreen() + col.getRed())/3;                        
                    }
                }                
                break;
            case PERSPECTIVE_MODE:
                for(int y = 0;y < height;y++)
                {
                    for(int x = 0;x < width;x++)
                    {
                        Color col2 = colorMap[y][x];
                        tempImage[y][x] = (int)(col2.getBlue()*0.07 + col2.getGreen() * 0.72+col2.getRed()*0.21);
                    }
                } 
                break;
            case MIN_MAX_MODE:
                for(int y = 0;y < height;y++)
                {
                    for(int x = 0;x < width;x++)
                    {
                        Color col3 = colorMap[y][x];
                        
                        tempImage[y][x] = (getMaxValue(col3.getBlue(),col3.getGreen(),col3.getRed()) + 
                                getMinValue(col3.getBlue(),col3.getGreen(),col3.getRed()))/2;
                        
                    }
                }            
                break;
            case MATRIX_MODE:
                for(int y = 0;y < height;y++)
                {
                    for(int x = 0;x < width;x++)
                    {
                        Color col4 = colorMap[y][x];                        
                        tempImage[y][x] = (getMaxOrMinValue(col4.getBlue(),col4.getGreen(),col4.getRed()));                        
                    }
                }
                break;
            default:
                throw new ModeNotHandledException("MODE NOT RECONIZED");  
                        
        }
        }catch(ModeNotHandledException e)
        {
            e.printStackTrace();
        }
            
        return tempImage;
    }
    // takes the average between the values it then normalizes the average value 
    // if the average brightness is <= to 127 it scales the normalized average value
    // between 0 and 32, if the average brightness is > then 127 it scales the normalized 
    // average value between 232 and 255
    private int getMaxOrMinValue(int a,int b,int c)
    {
        double avg = (a + b + c)/3;
        double norm = avg/255;
        int min = (int)unNormalize(norm,0,32);
        int max = (int)unNormalize(norm,232,255);
        return (avg <= 127)? min : max;
    }
    // this takes a normalized piece of data between 0 and 1.0
    // and rerverses the normalization to a new scale between min and max
    private double unNormalize(double a ,double min, double max)
    {
        return (a * (max-min)) + min;
    }
    // this returns the largest number passed in the parameters 
    private int getMaxValue(int a,int b,int c)
    {
        return (a >= b && a >= c)? a : getMaxValue(b,c,a);
    }
    // this returns the smallest number passed in the parameters
    private int getMinValue(int a,int b,int c)
    {
        return (a <= b && a <= c)? a : getMinValue(b,c,a);
    }  
}
