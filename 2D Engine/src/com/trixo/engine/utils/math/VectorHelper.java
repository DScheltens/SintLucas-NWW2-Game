package com.trixo.engine.utils.math;

import com.trixo.engine.math.Vector3;
import com.trixo.engine.math.Vector4;
import com.trixo.engine.math.Vector;

public class VectorHelper {
    /* Vector4 */
    public static Vector4 add(Vector4 i, Vector4 j) {
        return new Vector4(i.x + j.x, i.y + j.y, i.z + j.z, i.w + j.w);
    }

    public static Vector4 sub(Vector4 i, Vector4 j) {
        return new Vector4(i.x - j.x, i.y - j.y, i.z - j.z, i.w - j.w);
    }

    public static Vector4 mult(Vector4 i, Vector4 j) {
        return new Vector4(i.x * j.x, i.y * j.y, i.z * j.z, i.w * j.w);
    }

    public static Vector4 devide(Vector4 i, Vector4 j) {
        return new Vector4(i.x / j.x, i.y / j.y, i.z / j.z, i.w / j.w);
    }
    
    /* Vector3 */
    public static Vector3 add(Vector3 i, Vector3 j) {
        return new Vector3(i.x + j.x, i.y + j.y, i.z + j.z);
    }

    public static Vector3 sub(Vector3 i, Vector3 j) {
        return new Vector3(i.x - j.x, i.y - j.y, i.z - j.z);
    }

    public static Vector3 mult(Vector3 i, Vector3 j) {
        return new Vector3(i.x * j.x, i.y * j.y, i.z * j.z);
    }

    public static Vector3 devide(Vector3 i, Vector3 j) {
        return new Vector3(i.x / j.x, i.y / j.y, i.z / j.z);
    }
    
    /* Vector */
    public static Vector add(Vector i, Vector j) {
        return new Vector(i.x + j.x, i.y + j.y);
    }

    public static Vector sub(Vector i, Vector j) {
        return new Vector(i.x - j.x, i.y - j.y);
    }

    public static Vector mult(Vector i, Vector j) {
        return new Vector(i.x * j.x, i.y * j.y);
    }

    public static Vector devide(Vector i, Vector j) {
        return new Vector(i.x / j.x, i.y / j.y);
    }
}
