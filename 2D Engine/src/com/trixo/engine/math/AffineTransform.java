//
// Pythagoras - a collection of geometry classes
// http://github.com/samskivert/pythagoras

package com.trixo.engine.math;

import com.trixo.engine.utils.math.NoninvertibleTransformException;


/**
 * Implements an affine (3x2 matrix) transform. The transformation matrix has the form:
 * <pre>{@code
 * [ m00, m10, tx ]
 * [ m01, m11, ty ]
 * [   0,   0,  1 ]
 * }</pre>
 */
public class AffineTransform extends AbstractTransform
{
    /** Identifies the affine transform in {@link #generality}. */
    public static final int GENERALITY = 4;

    /** The scale, rotation and shear components of this transform. */
    public double m00, m01, m10, m11;

    /** The translation components of this transform. */
    public double tx, ty;

    /** Creates an affine transform configured with the identity transform. */
    public AffineTransform () {
        this(1, 0, 0, 1, 0, 0);
    }

    /** Creates an affine transform from the supplied scale, rotation and translation. */
    public AffineTransform (double scale, double angle, double tx, double ty) {
        this(scale, scale, angle, tx, ty);
    }

    /** Creates an affine transform from the supplied scale, rotation and translation. */
    public AffineTransform (double scaleX, double scaleY, double angle, double tx, double ty) {
        double sina = Math.sin(angle), cosa = Math.cos(angle);
        this.m00 =  cosa * scaleX; this.m01 = sina * scaleY;
        this.m10 = -sina * scaleX; this.m11 = cosa * scaleY;
        this.tx  =  tx;            this.ty  = ty;
    }

    /** Creates an affine transform with the specified transform matrix. */
    public AffineTransform (double m00, double m01, double m10, double m11, double tx, double ty) {
        this.m00 = m00; this.m01 = m01;
        this.m10 = m10; this.m11 = m11;
        this.tx  = tx;  this.ty  = ty;
    }

    @Override // from Transform
    public double uniformScale () {
        // the square root of the signed area of the parallelogram spanned by the axis vectors
        double cp = m00*m11 - m01*m10;
        return (cp < 0f) ? -Math.sqrt(-cp) : Math.sqrt(cp);
    }

    @Override // from Transform
    public double scaleX () {
        return Math.sqrt(m00*m00 + m01*m01);
    }

    @Override // from Transform
    public double scaleY () {
        return Math.sqrt(m10*m10 + m11*m11);
    }

    @Override // from Transform
    public double rotation () {
        // use the iterative polar decomposition algorithm described by Ken Shoemake:
        // http://www.cs.wisc.edu/graphics/Courses/838-s2002/Papers/polar-decomp.pdf

        // start with the contents of the upper 2x2 portion of the matrix
        double n00 = m00, n10 = m10;
        double n01 = m01, n11 = m11;
        for (int ii = 0; ii < 10; ii++) {
            // store the results of the previous iteration
            double o00 = n00, o10 = n10;
            double o01 = n01, o11 = n11;

            // compute average of the matrix with its inverse transpose
            double det = o00*o11 - o10*o01;
            if (Math.abs(det) == 0f) {
                // determinant is zero; matrix is not invertible
                throw new NoninvertibleTransformException(this.toString());
            }
            double hrdet = 0.5f / det;
            n00 = +o11 * hrdet + o00*0.5f;
            n10 = -o01 * hrdet + o10*0.5f;

            n01 = -o10 * hrdet + o01*0.5f;
            n11 = +o00 * hrdet + o11*0.5f;

            // compute the difference; if it's small enough, we're done
            double d00 = n00 - o00, d10 = n10 - o10;
            double d01 = n01 - o01, d11 = n11 - o11;
            if (d00*d00 + d10*d10 + d01*d01 + d11*d11 < MathUtil.EPSILON) {
                break;
            }
        }
        // now that we have a nice orthogonal matrix, we can extract the rotation
        return Math.atan2(n01, n00);
    }

    @Override // from Transform
    public double tx () {
        return this.tx;
    }

    @Override // from Transform
    public double ty () {
        return this.ty;
    }

    @Override // from Transform
    public void get (double[] matrix) {
        matrix[0] = m00; matrix[1] = m01;
        matrix[2] = m10; matrix[3] = m11;
        matrix[4] =  tx; matrix[5] = ty;
    }

    @Override // from Transform
    public Transform setUniformScale (double scale) {
        return setScale(scale, scale);
    }

    @Override // from Transform
    public Transform setScaleX (double scaleX) {
        // normalize the scale to 1, then re-apply
        double mult = scaleX / scaleX();
        m00 *= mult;
        m01 *= mult;
        return this;
    }

    @Override // from Transform
    public Transform setScaleY (double scaleY) {
        // normalize the scale to 1, then re-apply
        double mult = scaleY / scaleY();
        m10 *= mult;
        m11 *= mult;
        return this;
    }

    @Override // from Transform
    public Transform setRotation (double angle) {
        // extract the scale, then reapply rotation and scale together
        double sx = scaleX(), sy = scaleY();
        double sina = Math.sin(angle), cosa = Math.cos(angle);
        m00 =  cosa * sx; m01 = sina * sx;
        m10 = -sina * sy; m11 = cosa * sy;
        return this;
    }

    @Override // from Transform
    public Transform setTranslation (double tx, double ty) {
        this.tx = tx;
        this.ty = ty;
        return this;
    }

    @Override // from Transform
    public Transform setTx (double tx) {
        this.tx = tx;
        return this;
    }

    @Override // from Transform
    public Transform setTy (double ty) {
        this.ty = ty;
        return this;
    }

    @Override // from Transform
    public Transform setTransform (double m00, double m01, double m10, double m11, double tx, double ty) {
        this.m00 = m00;
        this.m01 = m01;
        this.m10 = m10;
        this.m11 = m11;
        this.tx = tx;
        this.ty = ty;
        return this;
    }

    @Override // from Transform
    public Transform uniformScale (double scale) {
        return scale(scale, scale);
    }

    @Override // from Transform
    public Transform scale (double scaleX, double scaleY) {
        m00 *= scaleX;
        m01 *= scaleX;
        m10 *= scaleY;
        m11 *= scaleY;
        return this;
    }

    @Override // from Transform
    public Transform scaleX (double scaleX) {
        return Transforms.multiply(this, scaleX, 0, 0, 1, 0, 0, this);
    }

    @Override // from Transform
    public Transform scaleY (double scaleY) {
        return Transforms.multiply(this, 1, 0, 0, scaleY, 0, 0, this);
    }

    @Override // from Transform
    public Transform rotate (double angle) {
        double sina = Math.sin(angle), cosa = Math.cos(angle);
        return Transforms.multiply(this, cosa, sina, -sina, cosa, 0, 0, this);
    }

    @Override // from Transform
    public Transform translate (double tx, double ty) {
        this.tx += m00*tx + m10*ty;
        this.ty += m11*ty + m01*tx;
        return this;
    }

    @Override // from Transform
    public Transform translateX (double tx) {
        return Transforms.multiply(this, 1, 0, 0, 1, tx, 0, this);
    }

    @Override // from Transform
    public Transform translateY (double ty) {
        return Transforms.multiply(this, 1, 0, 0, 1, 0, ty, this);
    }

    @Override // from Transform
    public Transform invert () {
        // compute the determinant, storing the subdeterminants for later use
        double det = m00*m11 - m10*m01;
        if (Math.abs(det) == 0f) {
            // determinant is zero; matrix is not invertible
            throw new NoninvertibleTransformException(this.toString());
        }
        double rdet = 1f / det;
        return new AffineTransform(
            +m11 * rdet,              -m10 * rdet,
            -m01 * rdet,              +m00 * rdet,
            (m10*ty - m11*tx) * rdet, (m01*tx - m00*ty) * rdet);
    }

    @Override // from Transform
    public Transform concatenate (Transform other) {
        if (generality() < other.generality()) {
            return other.preConcatenate(this);
        }
        if (other instanceof AffineTransform) {
            return Transforms.multiply(this, (AffineTransform)other, new AffineTransform());
        } else {
            AffineTransform oaff = new AffineTransform(other);
            return Transforms.multiply(this, oaff, oaff);
        }
    }

    @Override // from Transform
    public Transform preConcatenate (Transform other) {
        if (generality() < other.generality()) {
            return other.concatenate(this);
        }
        if (other instanceof AffineTransform) {
            return Transforms.multiply((AffineTransform)other, this, new AffineTransform());
        } else {
            AffineTransform oaff = new AffineTransform(other);
            return Transforms.multiply(oaff, this, oaff);
        }
    }

    @Override // from Transform
    public Transform lerp (Transform other, double t) {
        if (generality() < other.generality()) {
            return other.lerp(this, -t); // TODO: is this correct?
        }

        AffineTransform ot = (other instanceof AffineTransform) ?
            (AffineTransform)other : new AffineTransform(other);
        return new AffineTransform(
            m00 + t*(ot.m00 - m00), m01 + t*(ot.m01 - m01),
            m10 + t*(ot.m10 - m10), m11 + t*(ot.m11 - m11),
            tx  + t*(ot.tx  - tx ), ty  + t*(ot.ty  - ty ));
    }

    @Override // from Transform
    public Point transform (IPoint p, Point into) {
        double x = p.x(), y = p.y();
        return into.set(m00*x + m10*y + tx, m01*x + m11*y + ty);
    }

    @Override // from Transform
    public void transform (IPoint[] src, int srcOff, Point[] dst, int dstOff, int count) {
        for (int ii = 0; ii < count; ii++) {
            transform(src[srcOff++], dst[dstOff++]);
        }
    }

    @Override // from Transform
    public void transform (double[] src, int srcOff, double[] dst, int dstOff, int count) {
        for (int ii = 0; ii < count; ii++) {
            double x = src[srcOff++], y = src[srcOff++];
            dst[dstOff++] = m00*x + m10*y + tx;
            dst[dstOff++] = m01*x + m11*y + ty;
        }
    }

    @Override // from Transform
    public Point inverseTransform (IPoint p, Point into) {
        double x = p.x() - tx, y = p.y() - ty;
        double det = m00 * m11 - m01 * m10;
        if (Math.abs(det) == 0f) {
            // determinant is zero; matrix is not invertible
            throw new NoninvertibleTransformException(this.toString());
        }
        double rdet = 1 / det;
        return into.set((x * m11 - y * m10) * rdet,
                        (y * m00 - x * m01) * rdet);
    }

    @Override // from Transform
    public Vector transformPoint (IVector v, Vector into) {
        double x = v.x(), y = v.y();
        return into.set(m00*x + m10*y + tx, m01*x + m11*y + ty);
    }

    @Override // from Transform
    public Vector transform (IVector v, Vector into) {
        double x = v.x(), y = v.y();
        return into.set(m00*x + m10*y, m01*x + m11*y);
    }

    @Override // from Transform
    public Vector inverseTransform (IVector v, Vector into) {
        double x = v.x(), y = v.y();
        double det = m00 * m11 - m01 * m10;
        if (Math.abs(det) == 0f) {
            // determinant is zero; matrix is not invertible
            throw new NoninvertibleTransformException(this.toString());
        }
        double rdet = 1 / det;
        return into.set((x * m11 - y * m10) * rdet,
                        (y * m00 - x * m01) * rdet);
    }

    @Override // from Transform
    public Transform copy () {
        return new AffineTransform(m00, m01, m10, m11, tx, ty);
    }

    @Override // from Transform
    public int generality () {
        return GENERALITY;
    }

    @Override
    public String toString () {
        return "affine [" + MathUtil.toString(m00) + " " + MathUtil.toString(m01) + " " +
            MathUtil.toString(m10) + " " + MathUtil.toString(m11) + " " + translation() + "]";
    }

    // we don't publicize this because it might encourage someone to do something stupid like
    // create a new AffineTransform from another AffineTransform using this instead of copy()
    protected AffineTransform (Transform other) {
        this(other.scaleX(), other.scaleY(), other.rotation(),
             other.tx(), other.ty());
    }
}
