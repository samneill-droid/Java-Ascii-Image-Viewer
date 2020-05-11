package ASCII_ART_VIEWER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.SimpleAttributeSet;

/**
 * The GUI class creates a user interface for loading JPG files and converting
 * them into ascii art to be viewed in a text pane and manipulated with the buttons 
 */
public class GUI extends JFrame
{
    private final Container container;
    // This JTextpane will at as an ascii-image renderer 
    private JTextPane asciiImageDisplayPane = new JTextPane();
    // This will convert the ascii-image into a black and white format when clicked
    private JButton blackAndWhiteButton = new JButton("Black & White");
    // This will convert the ascii-image into a colored version when clicked     
    private JButton colorizeButton = new JButton("Colorize");
    // This will zoom in on the ascii-image when clicked
    private JButton zoomInButton = new JButton("Zoom In");
    // This will zoom out on the ascii-image when clicked
    private JButton zoomOutButton = new JButton("Zoom out");
    // This will build the ascii-image brightness vaules by using the average mode method when clicked
    private JButton averageModeButton = new JButton("Average");
    // This will build the ascii-image brigntness values using the perspective method when clicked
    private JButton perspectiveModeButton = new JButton("Perspective");
    // This will build the ascii-image brightness values using the Min Max method when clicked
    private JButton minMaxModeButton = new JButton("Min Max");
    // This will build the ascii-image into a cool matrix format from The Matrix
    private JButton matrixModeButton = new JButton("Matrix");
    // The SimpleAttributeset will be used to mark up the asciiImageDisplayPane which will allow  
    // for the dynamic color changes and font size changes 
    private SimpleAttributeSet attriSet;
    // This contains all the critical information about the image needed to render to the JTextPane
    private AsciiImage image;     
    // This will handle the file choosing GUI 
    private JFileChooser fileChooser = new JFileChooser();
    // a container for holding the file choosing menu
    private JMenuBar menuBar = new JMenuBar();
    // a menu to hold the load file menu item 
    private JMenu fileChooserMenu = new JMenu("File");
    // the menu item that will load the file chooser GUI when clicked
    private JMenuItem fileChooserMenuItem = new JMenuItem("Load File");
    
    // Native Screen size
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    
    
    GUI(String name) 
    {
        super(name);
        this.setSize(screenSize);        
        container = getContentPane();         
        init();
        addComponentsToTheContentPane();
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    // intializes all the graphical components
    private void init()
    {
        initAsciiDisplayPaneAndStyles(); 
        initJButtons();
        initMenuItems();
        initFileChooser();
    }
    //intializes the asciiImageDisplayPane and sets the CharacterAttribute set
    private void initAsciiDisplayPaneAndStyles()
    {
        asciiImageDisplayPane.setMinimumSize(screenSize);
        asciiImageDisplayPane.setEditorKit(new ExtendedStyleEditorKit());
        attriSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(attriSet,5);
        StyleConstants.setForeground(attriSet, Color.WHITE);
        StyleConstants.setBackground(attriSet, Color.BLACK);
        asciiImageDisplayPane.setCharacterAttributes(attriSet, true);
    }
    //intializes the menu items
    private void initMenuItems()
    {
        fileChooserMenuItem.addActionListener(new FileChooserListener());
        fileChooserMenu.add(fileChooserMenuItem);
        menuBar.add(fileChooserMenu);         
    }
    // intializes the file chooser
    private void initFileChooser()
    {
        fileChooser.setFileFilter(new JPGFileFilter());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }
    
    // initializes all the jbuttons
    private void initJButtons()
    {
        blackAndWhiteButton.addActionListener(new BlackAndWhiteActionListener());
        colorizeButton.addActionListener(new ColorizeActionListener());
        zoomInButton.addActionListener(new ZoomInActionListener());
        zoomOutButton.addActionListener(new ZoomOutActionListener());
        averageModeButton.addActionListener(new AverageModeListener());
        perspectiveModeButton.addActionListener(new PerspectiveModeListener());
        minMaxModeButton.addActionListener(new MinMaxModeListener());
        matrixModeButton.addActionListener(new MatrixModeListener());
    }
    // adds all the components to the content pane
    private void addComponentsToTheContentPane()
    {
        JScrollPane scrollPane = new JScrollPane(asciiImageDisplayPane); 
        container.add(menuBar,BorderLayout.NORTH);
        container.add(scrollPane,BorderLayout.CENTER);
        container.add(addButtonComponents(),BorderLayout.WEST);             
    }
    
    //adds all the JButtons to a JPanel using a box layout     
    private JPanel addButtonComponents()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS)); 
        panel.add(blackAndWhiteButton);
        panel.add(colorizeButton);
        panel.add(zoomInButton);
        panel.add(zoomOutButton);
        panel.add(averageModeButton);
        panel.add(perspectiveModeButton);
        panel.add(minMaxModeButton);
        panel.add(matrixModeButton);
        return panel;
    }
    // this rerenders the text pane
    // it first makes sure that an image is loaded into the program
    // then sets the asciiImageDisplayPane's text to emtpy
    // it then call the renderAsciiArt function to "redraw" the image
    private void rerenderAsciiArt()
    {
        if(!FlagAndControl.FILE_LOADED) return;        
        asciiImageDisplayPane.setText("");        
        renderAsciiArt();    
    }
    // this will render the ascii image to the asciiImageDisplayPane
   
    private void renderAsciiArt()
    {        
        for(int y = 0;y < image.getHeight();y++)
        {
            for(int x = 0;x < image.getWidth();x++)
            {
                attriSet = new SimpleAttributeSet();            
                // set font to monospaced so each row of text is evenly spaced
                StyleConstants.setFontFamily(attriSet,"monospaced"); 
                // sets the Font size to the image size scale by default its 2
                StyleConstants.setFontSize(attriSet, FlagAndControl.SCALE);
                //grab the color info related to the ascii charater
                Color c = image.getColorMap()[y][x];
                // if its being drawn in color then the color is set to the 
                // color related to the ascii character
                if(FlagAndControl.COLORIZE)
                {                    
                    StyleConstants.setForeground(attriSet, c);
                }
                //if the Matrix mode is selected then we use a couple of different greens
                // to give it a cool matrix feel
                else if(FlagAndControl.MATRIX)
                {                    
                    if(image.getAsciiString()[y][x] >= 246)
                    {                        
                        StyleConstants.setForeground(attriSet, new Color(0,255,65));
                    } 
                    else if(image.getAsciiString()[y][x] >= 127)
                    {
                        StyleConstants.setForeground(attriSet, new Color(0,143,17));
                    }
                    else                         
                    {
                        StyleConstants.setForeground(attriSet, new Color(0,59,0));
                    }                    
                }
                // if its not in color mode or in matrix mode then we will assume for
                // now that it is in black and white mode
                else 
                {
                    StyleConstants.setForeground(attriSet, Color.WHITE);
                }
                //set the background to black so we have high contrast for each character
                StyleConstants.setBackground(attriSet,Color.BLACK);
                // grab the document file related to the text pane
                Document doc = asciiImageDisplayPane.getStyledDocument();
                // It then trys to insert an ascii character based off the brigtness value into the document 
                // It inserts two characters to stretch out the image a bit
                // if it fails we will log it 
                try 
                {                    
                    doc.insertString(doc.getLength(),""+ASCIICode.getAsciiChar(image.getAsciiString()[y][x]), attriSet);
                    doc.insertString(doc.getLength(),""+ASCIICode.getAsciiChar(image.getAsciiString()[y][x]), attriSet);                   
                } 
                catch (BadLocationException ex) 
                {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            // inserts a new line into the document at the end of each row
            try 
            {
                Document doc = asciiImageDisplayPane.getStyledDocument();
                doc.insertString(doc.getLength(),"\n", attriSet);
            } 
            catch (BadLocationException ex) 
            {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // This class listens for any clicks to the Colorize Button 
    // it first checks to see if its already in color mode and if not 
    // sets the correct flags and then calls the rerenderAsciiArt() method
    private class ColorizeActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.COLORIZE == true) return;
            if(FlagAndControl.MATRIX) FlagAndControl.update(AsciiMode.AVERAGE_BRIGHTNESS_MODE);
            FlagAndControl.COLORIZE = true;   
            
            rerenderAsciiArt();
        }        
    }
    // This class listens for any clicks to the Black And White Button 
    // it first checks to see if its already in Black and white mode and if not 
    // sets the correct flags and then calls the rerenderAsciiArt() method
    private class BlackAndWhiteActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event)
        {
           if(FlagAndControl.COLORIZE == false) return;
           if(FlagAndControl.MATRIX) FlagAndControl.update(AsciiMode.AVERAGE_BRIGHTNESS_MODE);
           FlagAndControl.COLORIZE = false;
           rerenderAsciiArt();           
        }        
    }
    // This class listens for any clicks to the Zoom in Button 
    // it first checks to see if it is at max zoom and if not it increments 
    // the scale and then calls the rerenderAsciiArt() method
    private class ZoomInActionListener implements ActionListener 
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.SCALE >= 12) return;
            FlagAndControl.SCALE++;
            rerenderAsciiArt();            
        }        
    }
    // This class listens for any clicks to the Zoom out Button 
    // it first checks to see if it is at minimum zoom and if not it decrements 
    // the scale and then calls the rerenderAsciiArt() method
    private class ZoomOutActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.SCALE<=1) return;
            FlagAndControl.SCALE--;
            rerenderAsciiArt();
        }        
    }
    // This class listens for any clicks to the AverageMode Button 
    // it first checks to see if its already in Average mode and if not 
    // sets the correct flags, updates the brightness values to the new mode and
    // then calls the rerenderAsciiArt() method
    private class AverageModeListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.AVERAGE) return;
            FlagAndControl.update(AsciiMode.AVERAGE_BRIGHTNESS_MODE);            
            image.setBrightnessValue(image.getBrightnessValues(AsciiMode.AVERAGE_BRIGHTNESS_MODE)); 
            rerenderAsciiArt();
        }        
    }
    // This class listens for any clicks to the Perspective Mode Button 
    // it first checks to see if its already in Perspective mode and if not 
    // sets the correct flags, updates the brightness values to the new mode and
    // then calls the rerenderAsciiArt() method
    private class PerspectiveModeListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.PERSPECTIVE) return;
            FlagAndControl.update(AsciiMode.PERSPECTIVE_MODE);            
            image.setBrightnessValue(image.getBrightnessValues(AsciiMode.PERSPECTIVE_MODE)); 
            rerenderAsciiArt();
        }       
    }
    // This class listens for any clicks to the Min Max Button 
    // it first checks to see if its already in Min Max mode and if not 
    // sets the correct flags, updates the brightness values to the new mode and
    // then calls the rerenderAsciiArt() method
    private class MinMaxModeListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.MINMAX) return;
            FlagAndControl.update(AsciiMode.MIN_MAX_MODE);            
            image.setBrightnessValue(image.getBrightnessValues(AsciiMode.MIN_MAX_MODE)); 
            rerenderAsciiArt();
        }        
    }
    // This class listens for any clicks to the Matrix Mode Button 
    // it first checks to see if its already in Matrix mode and if not 
    // sets the correct flags, updates the brightness values to the new mode and
    // then calls the rerenderAsciiArt() method
    private class MatrixModeListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event) 
        {
            if(FlagAndControl.MATRIX) return;
            FlagAndControl.update(AsciiMode.MATRIX_MODE);            
            image.setBrightnessValue(image.getBrightnessValues(AsciiMode.MATRIX_MODE)); 
            rerenderAsciiArt();
        }        
    }
    // This class listens for any clicks to the file chooser menu item
    // when clicked it will load the file chooser GUI
    // it then will attempt to load the image in and set the FILE_LOADED flag to true
    // it then set the brightness values to AverageMode by default
    // and then will call the rerenderAsciiArt() method
    private class FileChooserListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            int fileReturnVal = fileChooser.showOpenDialog(GUI.this);
            
            if(fileReturnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fileChooser.getSelectedFile();
                try
                {
                    image = new ImageLoader().getAsciiImage(file.getPath());
                    FlagAndControl.FILE_LOADED = true;
                    image.setBrightnessValue(image.getBrightnessValues(AsciiMode.AVERAGE_BRIGHTNESS_MODE));
                    rerenderAsciiArt();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }                
            }    
        }
    }
    // This is a container class that holds boolean flags and
    // control vaules used by the GUI class
    // it also contains a few methods to make it easier when 
    // new modes are added to the gui
    private static class FlagAndControl
    {
        private static final int DEFAULT_SCALE = 2;
        
        public static boolean AVERAGE =false,PERSPECTIVE=false,MINMAX=false,MATRIX=false; 
        
        public static boolean COLORIZE = false; 
        
        public static int SCALE = DEFAULT_SCALE;
        
        public static boolean FILE_LOADED = false;
        //init the default values
        private FlagAndControl()
        {
            setDefaults();
        }
        // sets all the mode flags to false
        private static void setToFalse()
        {
            AVERAGE = false;
            PERSPECTIVE = false;
            MINMAX = false;
            MATRIX = false;
        }
        //sets the default values for an image 
        private static void setDefaults()
        {
            AVERAGE = true;
            COLORIZE = false;
        }
        // updates all the mode flags to false and then sets the flag
        // associaed to the mode passed to true
        // the only exception to this rule is matrix mode where 
        // the colorized flag is also set to false 
        public static void update(AsciiMode mode)
        {
            try
            {
                switch (mode)
                {
                    case AVERAGE_BRIGHTNESS_MODE:
                        setToFalse();
                        AVERAGE = true;                    
                        break;
                    case PERSPECTIVE_MODE:
                        setToFalse();
                        PERSPECTIVE = true;
                        break;
                    case MIN_MAX_MODE:
                        setToFalse();
                        MINMAX = true;
                        break;
                    case MATRIX_MODE:
                        setToFalse();
                        MATRIX = true;
                        COLORIZE = false;
                        break;
                    default:                     
                        throw new ModeNotHandledException("MODE NOT RECONIZED");
                    
                }
            }
            catch(ModeNotHandledException e)
            {
                e.printStackTrace();
            }                          
        }
    } 
    // this class is used by the file chooser to filter out all files except 
    // jpg type files
    private class JPGFileFilter extends FileFilter
    {

        @Override
        public boolean accept(File file) 
        {
            if(file.getName().endsWith(".jpg"))
            {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() 
        {
            return "";
        }
       
    }
}