//
// Pythagoras - a collection of geometry classes
// http://github.com/samskivert/pythagoras

package com.trixo.engine.math;

/**
 * Dimension-related utility methods.
 */
public class Dimensions
{
    /** A dimension width zero width and height. */
    public static final IDimension ZERO = new Dimension(0, 0);

    /**
     * Returns a string describing the supplied dimension, of the form <code>widthxheight</code>.
     */
    public static String dimenToString (double width, double height) {
        return width + "x" + height;
    }
}
