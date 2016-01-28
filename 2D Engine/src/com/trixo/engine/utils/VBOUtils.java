package com.trixo.engine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class VBOUtils {
    public static int createVBOID() {
        return GL15.glGenBuffers();
    }

    public static int createVAOID() {
        return GL30.glGenVertexArrays();
    }

    public static void bufferData(FloatBuffer buffer, int bufferUsage) {
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, bufferUsage);
    }

    public static void bufferElementData(ByteBuffer buffer, int bufferUsage) {
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, bufferUsage);
    }

    public static void bindArray(int id) {
        GL30.glBindVertexArray(id);
    }

    public static void bindBuffer(int id) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    }

    public static void bindElementBuffer(int id) {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    }

    public static void deleteBuffer(int id) {
        GL15.glDeleteBuffers(id);
    }

    public static void deleteArray(int id) {
        GL30.glDeleteVertexArrays(id);
    }
}