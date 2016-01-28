package com.trixo.engine.graphics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

public class Shader {
    public int programID = 0;
    public boolean valid = false;

    public void load(String vert, String frag) {
        int vertID = 0;
        int fragID = 0;
        try {
            vertID = createShader(vert.replace(" ", ""), ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragID = createShader(frag.replace(" ", ""), ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        } catch (Exception exc) {
            exc.printStackTrace();
            return;
        } finally {
            if (vertID == 0 || fragID == 0)
                return;
        }

        programID = ARBShaderObjects.glCreateProgramObjectARB();
        if (programID == 0)
            return;

        ARBShaderObjects.glAttachObjectARB(programID, vertID);
        ARBShaderObjects.glAttachObjectARB(programID, fragID);

        ARBShaderObjects.glLinkProgramARB(programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(programID));
            return;
        }

        ARBShaderObjects.glValidateProgramARB(programID);
        if (ARBShaderObjects.glGetObjectParameteriARB(programID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            System.err.println(getLogInfo(programID));
            return;
        }

        valid = true;
    }

    private int createShader(String filename, int shaderType) throws Exception {
        int shaderID = 0;
        try {
            shaderID = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if (shaderID == 0)
                return 0;

            ARBShaderObjects.glShaderSourceARB(shaderID, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shaderID);

            if (ARBShaderObjects.glGetObjectParameteriARB(shaderID, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                throw new RuntimeException("Error creating shader: " + getLogInfo(shaderID));
            return shaderID;
        } catch (Exception exc) {
            ARBShaderObjects.glDeleteObjectARB(shaderID);
            throw exc;
        }
    }

    private static String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj,
                ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    private String readFileAsString(String filename) throws Exception {
        StringBuilder source = new StringBuilder();

        FileInputStream in = new FileInputStream(filename);

        Exception exception = null;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Exception innerExc = null;
            try {
                String line;
                while ((line = reader.readLine()) != null)
                    source.append(line).append('\n');
            } catch (Exception exc) {
                exception = exc;
            } finally {
                try {
                    reader.close();
                } catch (Exception exc) {
                    if (innerExc == null)
                        innerExc = exc;
                    else
                        exc.printStackTrace();
                }
            }

            if (innerExc != null)
                throw innerExc;
        } catch (Exception exc) {
            exception = exc;
        } finally {
            try {
                in.close();
            } catch (Exception exc) {
                if (exception == null)
                    exception = exc;
                else
                    exc.printStackTrace();
            }

            if (exception != null)
                throw exception;
        }

        return source.toString();
    }
}
