
package ASCII_ART_VIEWER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageLoader 
{   
    public ImageLoader()
    {        
    }      
    
    // returns a new AsciiImage with the nescissary data from the Image file 
    // the new AsciiImage will have an emtpy brightness array by default 
    public AsciiImage getAsciiImage(String fileName) throws IOException
    {
        BufferedImage image = ImageIO.read(new File(fileName));
        
        int[][] brightnessValues = new int[image.getHeight()][image.getWidth()];
        Color[][] colorMap = new Color[image.getHeight()][image.getWidth()];              
        
        for(int y = 0;y < image.getHeight();y++)
        {
            for(int x = 0;x < image.getWidth();x++)
            {
                Color col = new Color(image.getRGB(x, y));                  
                colorMap[y][x] = col;
            }            
        }     
                
        return new AsciiImage(brightnessValues,image.getHeight(),image.getWidth(),colorMap);
    }      
}
