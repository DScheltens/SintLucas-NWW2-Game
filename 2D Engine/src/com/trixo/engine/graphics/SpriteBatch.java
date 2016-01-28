package com.trixo.engine.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import com.trixo.engine.math.Matrix4;
import com.trixo.engine.systems.content.ResourceSystem;
import com.trixo.engine.utils.VBOUtils;

/**
 * Created by brb55_000 on 4/20/2015.
 */
public class SpriteBatch {

    class RenderBatch {
        int offset;
        int numVertices;
        int texture;
    }

    // Buffers
    public int m_vbo = 0;
    private ByteBuffer m_buffer;
    // Uniform handles
    private int m_positionHandle;
    private int m_colorHandle;
    private int m_MVPMatrixHandle;
    private int m_TCoordHandle;
    private int m_textureHandle;
    // Constants
    private static final int BYTES_PER_VERTEX = 20;
    private static final int VERTS_PER_QUAD = 6;

    Shader shader = null;

    Vector<SpriteBatchGlyph> m_glyphs = new Vector<SpriteBatchGlyph>();
    Vector<RenderBatch> m_renderBatches = new Vector<RenderBatch>();

    public void init() {
        shader = (Shader) ResourceSystem.getResource("shaders.basic.texture");

        m_vbo = VBOUtils.createVBOID();

        int ph = shader.programID;
        m_MVPMatrixHandle = GL20.glGetUniformLocation(ph, "uMVPMatrix");
        m_positionHandle = GL20.glGetAttribLocation(ph, "vPosition");
        m_colorHandle = GL20.glGetAttribLocation(ph, "vColor");
        m_TCoordHandle = GL20.glGetAttribLocation(ph, "vTextCoords");
        m_textureHandle = GL20.glGetAttribLocation(ph, "texture");
    }

    // Begins a frame of rendering
    public void begin() {
        m_glyphs.clear();
        m_renderBatches.clear();
    }

    // Registers a glyph
    public void draw(int texture, float x, float y, float w, float h, float u, float v, float uw, float vw,
            float angle, Object colour[]) {
        this.draw(texture, x, y, w, h, u, v, uw, vw, angle, new byte[] {
                (byte) colour[0], (byte) colour[1], (byte) colour[2], (byte) colour[3]
        });
    }

    public void draw(int texture, float x, float y, float w, float h, float u, float v, float uw, float vw,
            float angle, byte colour[]) {
        v += vw;
        vw = -vw;
        SpriteBatchGlyph glyph = new SpriteBatchGlyph();
        glyph.texture = texture;
        glyph.x = x;
        glyph.y = y;
        glyph.w = w;
        glyph.h = h;
        glyph.u = u;
        glyph.v = v;
        glyph.uw = uw;
        glyph.vw = vw;
        glyph.angle = angle;
        glyph.r = colour[0];
        glyph.g = colour[1];
        glyph.b = colour[2];
        glyph.a = colour[2];
        m_glyphs.add(glyph);
    }

    // Ends a frame of rendering
    public void end() {
        // sortGlyphs();
        createRenderBatches();
    }

    // Renders to the screen
    public void render(Camera camera) {
        // Bind shader
        GL20.glUseProgram(shader.programID);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        // Upload uniforms
        GL20.glUniform1i(m_textureHandle, 0);

        Matrix4 mat = camera.getCameraMatrix();

        java.nio.DoubleBuffer dbuf = BufferUtils.createDoubleBuffer(16);
        mat.get(dbuf);

        float m[] = {
                (float) dbuf.get(0), (float) dbuf.get(1), (float) dbuf.get(2), (float) dbuf.get(3), (float) dbuf.get(4),
                (float) dbuf.get(5), (float) dbuf.get(6), (float) dbuf.get(7), (float) dbuf.get(8), (float) dbuf.get(9),
                (float) dbuf.get(10), (float) dbuf.get(11), (float) dbuf.get(12), (float) dbuf.get(13),
                (float) dbuf.get(14), (float) dbuf.get(15)
        };
        java.nio.FloatBuffer tMatrix = BufferUtils.createFloatBuffer(16);
        tMatrix.put(m);
        tMatrix.flip();

        GL20.glUniformMatrix4(m_MVPMatrixHandle, false, tMatrix);

        GL20.glEnableVertexAttribArray(m_positionHandle);
        GL20.glEnableVertexAttribArray(m_colorHandle);
        GL20.glEnableVertexAttribArray(m_TCoordHandle);

        // Bind the VBO
        VBOUtils.bindBuffer(m_vbo);
        // Loop through each batch
        for (RenderBatch b : m_renderBatches) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, b.texture);

            // Set attribute pointers
            GL20.glVertexAttribPointer(m_positionHandle, 2, GL11.GL_FLOAT, false, BYTES_PER_VERTEX, 0);
            GL20.glVertexAttribPointer(m_TCoordHandle, 2, GL11.GL_FLOAT, false, BYTES_PER_VERTEX, 8);
            GL20.glVertexAttribPointer(m_colorHandle, 3, GL11.GL_UNSIGNED_BYTE, true, BYTES_PER_VERTEX, 16);

            // Render the batch with offset and numVertices
            GL11.glDrawArrays(GL11.GL_TRIANGLES, b.offset, b.numVertices);
        }

        GL20.glDisableVertexAttribArray(m_positionHandle);
        GL20.glDisableVertexAttribArray(m_colorHandle);
        GL20.glDisableVertexAttribArray(m_TCoordHandle);

        VBOUtils.bindBuffer(0);

        GL20.glUseProgram(0);
        // Error checking
        int error = GL11.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            try {
                throw (new Exception("OpenGL error " + error));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    // For sorting glyphs
    class GlyphComparator implements Comparator<SpriteBatchGlyph> {
        public int compare(SpriteBatchGlyph a, SpriteBatchGlyph b) {
            return a.texture < b.texture ? -1 : a.texture == b.texture ? 0 : 1;
        }
    }

    @SuppressWarnings("unused")
    private void sortGlyphs() {
        Collections.sort(m_glyphs, new GlyphComparator());
    }

    private void createRenderBatches() {
        if (m_glyphs.isEmpty())
            return;
        // Allocate buffer storage
        final int bufferSize = m_glyphs.size() * BYTES_PER_VERTEX * VERTS_PER_QUAD;
        m_buffer = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        m_buffer.position(0);

        RenderBatch batch = new RenderBatch();
        batch.offset = 0;
        batch.numVertices = 0;
        batch.texture = m_glyphs.get(0).texture;
        m_renderBatches.add(batch);

        int offset = 0;

        for (SpriteBatchGlyph g : m_glyphs) {

            if (g.texture != batch.texture) {
                batch.numVertices = offset - batch.offset;
                batch = new RenderBatch();
                batch.offset = offset;
                batch.numVertices = 0;
                batch.texture = g.texture;
                m_renderBatches.add(batch);
            }

            // Get origin relative points
            float tloX = -g.w * 0.5f, tloY = g.h * 0.5f; // Top left
            float bloX = -g.w * 0.5f, bloY = -g.h * 0.5f; // Bottom left
            float broX = g.w * 0.5f, broY = -g.h * 0.5f; // Bottom right
            float troX = g.w * 0.5f, troY = g.h * 0.5f; // Top right
            
            // Handle rotation
            float tlX = rotateX(tloX, tloY, g.angle);
            float tlY = rotateY(tloX, tloY, g.angle);
            float blX = rotateX(bloX, bloY, g.angle);
            float blY = rotateY(bloX, bloY, g.angle);
            float brX = rotateX(broX, broY, g.angle);
            float brY = rotateY(broX, broY, g.angle);
            float trX = rotateX(troX, troY, g.angle);
            float trY = rotateY(troX, troY, g.angle);

            // Add the 6 vertices
            m_buffer.putFloat(g.x + tlX);
            m_buffer.putFloat(g.y + tlY);
            m_buffer.putFloat(g.u);
            m_buffer.putFloat(g.v + g.vw);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            m_buffer.putFloat(g.x + blX);
            m_buffer.putFloat(g.y + blY);
            m_buffer.putFloat(g.u);
            m_buffer.putFloat(g.v);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            m_buffer.putFloat(g.x + brX);
            m_buffer.putFloat(g.y + brY);
            m_buffer.putFloat(g.u + g.uw);
            m_buffer.putFloat(g.v);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            m_buffer.putFloat(g.x + brX);
            m_buffer.putFloat(g.y + brY);
            m_buffer.putFloat(g.u + g.uw);
            m_buffer.putFloat(g.v);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            m_buffer.putFloat(g.x + trX);
            m_buffer.putFloat(g.y + trY);
            m_buffer.putFloat(g.u + g.uw);
            m_buffer.putFloat(g.v + g.vw);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            m_buffer.putFloat(g.x + tlX);
            m_buffer.putFloat(g.y + tlY);
            m_buffer.putFloat(g.u);
            m_buffer.putFloat(g.v + g.vw);
            m_buffer.put(g.r);
            m_buffer.put(g.g);
            m_buffer.put(g.b);
            m_buffer.put(g.a);

            offset += VERTS_PER_QUAD;
        }
        batch.numVertices = offset - batch.offset;

        m_buffer.position(0);
        VBOUtils.bindBuffer(m_vbo);
        VBOUtils.bufferData(m_buffer.asFloatBuffer(), GL15.GL_DYNAMIC_DRAW);
        VBOUtils.bindBuffer(0);
    }

    private float rotateX(float x, float y, float angle) {
        return x * (float) Math.cos(angle) - y * (float) Math.sin(angle);
    }

    private float rotateY(float x, float y, float angle) {
        return x * (float) Math.sin(angle) + y * (float) Math.cos(angle);
    }

    public static class SpriteBatchGlyph {
        int texture;
        float x;
        float y;
        float w;
        float h;
        float u;
        float v;
        float uw;
        float vw;
        float angle;
        byte r;
        byte g;
        byte b;
        byte a;
    }
}