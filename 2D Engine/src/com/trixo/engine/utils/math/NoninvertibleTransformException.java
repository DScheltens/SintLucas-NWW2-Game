//
// Pythagoras - a collection of geometry classes
// http://github.com/samskivert/pythagoras

package com.trixo.engine.utils.math;

/**
 * An exception thrown by {@code Transform} when a request for an inverse transform cannot be
 * satisfied.
 */
public class NoninvertibleTransformException extends RuntimeException
{
    public NoninvertibleTransformException (String s) {
        super(s);
    }
}
