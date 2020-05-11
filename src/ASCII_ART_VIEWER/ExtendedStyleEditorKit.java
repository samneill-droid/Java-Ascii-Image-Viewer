/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ASCII_ART_VIEWER;

import javax.swing.text.AbstractDocument;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * The ExtendedStyleEditorKit class allows for the asciiImageTextPane
 * To be resized when the zoom in function is called, this fixed an error
 * where the "Ascii Pixels" would wrap around the text pane
 */
public class ExtendedStyleEditorKit extends StyledEditorKit
{
    private static final ViewFactory styledEditorKitFactory = (new StyledEditorKit()).getViewFactory();
    
    private static final ViewFactory defaultFactory = new ExtendedStyleViewFactory();
    // this returns the new ExtendedStyleViewFactory
    @Override
    public ViewFactory getViewFactory()
    {
        return defaultFactory;
    }
    
    //this creates a new ExtendedParagraphView  
    static class ExtendedStyleViewFactory implements ViewFactory
    {        
        @Override
        public View create(Element elem) 
        {
            String elementName = elem.getName();
            
            if(elementName!=null)
            {
                if(elementName.equals(AbstractDocument.ParagraphElementName))
                {
                    return new ExtendedParagraphView(elem);
                }
            }
            return styledEditorKitFactory.create(elem);
        }
        
    }
    // creates a view that will dynamically resize when needed
    static class ExtendedParagraphView extends ParagraphView
    {
        public ExtendedParagraphView(Element elem)
        {
            super(elem);
        }
        @Override
        public float getMinimumSpan(int axis)
        {
            return super.getPreferredSpan(axis);
        }
    }
    
    
}
