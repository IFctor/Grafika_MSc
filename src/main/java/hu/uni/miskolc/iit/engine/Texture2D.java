package hu.uni.miskolc.iit.engine;

import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.graph.Mesh;
import hu.uni.miskolc.iit.engine.graph.PNGDecoder;
import hu.uni.miskolc.iit.engine.math.Vector2D;
import hu.uni.miskolc.iit.game.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Texture 2D Class
 *
 * @author Mileff Peter
 * <p>
 * University of Miskolc
 */
public class Texture2D {

    private static final String RESOURCES_LOCATION = AppConfig.appConfig().getResources().getLocation() + File.separator;

    // Hold the texture mesh
    private Mesh mesh = null;

    // Texture position
    private Vector3f position;

    // Texture scale
    private float scale;

    // Texture orientation
    private Vector3f rotation;

    // Texture width
    private int mWidth;

    // Texture height
    private int mHeight;

    // Texture OpenGL ID
    private int textureID;

    // Matrix for transformation
    private Matrix4f mTranformation;

    private int mID;

    // Texture color channels
    private float colorRed = 1.0f;
    private float colorGreen = 1.0f;
    private float colorBlue = 1.0f;
    private float colorAlpha = 1.0f;

    public Texture2D() {
        textureID = -1;
        scale = 1;
        mWidth = 0;
        mHeight = 0;

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

        mTranformation = new Matrix4f();

        mID = -1;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int getId() {
        return textureID;
    }

    /**
     * Creates a 2d textures mesh
     *
     * @param filename
     * @return
     */
    public boolean CreateTexture(String filename) {

        try {
            loadTexture(filename);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        // Create the Mesh
        float[] positions = new float[]{0.0f, mHeight, 0.0f, mWidth, mHeight, 0.0f, mWidth, 0.0f, 0.0f, mWidth, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, mHeight, 0.0f,};

        float[] textCoords = new float[]{0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

        mesh = new Mesh(positions, textCoords, this, 6);
        return true;
    }

    private void loadTexture(String fileName) throws Exception {

        // Load Texture file
        System.out.println("Loading file: " + fileName);

        File initialFile = new File(RESOURCES_LOCATION + fileName);
        InputStream targetStream = new FileInputStream(initialFile);

        PNGDecoder decoder = new PNGDecoder(targetStream);

        mWidth = decoder.getWidth();
        mHeight = decoder.getHeight();

        // Load texture contents into a byte buffer
        ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // Create a new OpenGL texture
        textureID = glGenTextures();

        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte
        // size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Upload the texture data
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                buf);

        // Generate Mip Map
        glGenerateMipmap(GL_TEXTURE_2D);
    }

    /**
     * Get the proper world matrix
     *
     * @return
     */
    public Matrix4f getWorldMatrix() {

        // First identity
        mTranformation.identity();

        // Translate
        mTranformation.translate(position);

        // Rotate object at the center of the texture
        float x = 0.5f * mWidth;
        float y = 0.5f * mHeight;

        // Do rotating
        mTranformation.translate(new Vector3f(x, y, 0));
        mTranformation.rotateZ((float) Math.toRadians(rotation.z));
        mTranformation.translate(new Vector3f(-x, -y, 0));

        // mTranformation.scale(scale, scale, 1);
        mTranformation.scaleAround(scale, x, y, 0);

        return mTranformation;
    }

    public void cleanup() {
        glDeleteTextures(textureID);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void render() {
        mesh.render();
    }

    public void Draw() {

        Renderer.mRenderer.shaderProgram.bind();

        Renderer.mRenderer.shaderProgram.setUniform("projectionMatrix", Renderer.mRenderer.projectionMatrix);
        Renderer.mRenderer.shaderProgram.setUniform("texture_sampler", 0);

        Matrix4f worldMatrix = getWorldMatrix();
        Renderer.mRenderer.shaderProgram.setUniform("worldMatrix", worldMatrix);
        Renderer.mRenderer.shaderProgram.setUniform4f("texture_color", this.getColorRed(), this.getColorGreen(), this.getColorBlue(),
                this.getColorAlpha());

        // Draw the texture
        mesh.render();

        Renderer.mRenderer.shaderProgram.unbind();
    }

    /**
     * Draw the texture
     *
     * @param pos
     */
    public void Draw(Vector2D pos) {

        position.x = pos.x;
        position.y = pos.y;

        Draw();
    }

    /**
     * Draw the texture
     *
     * @param x
     * @param y
     */
    public void Draw(float x, float y) {

        position.x = x;
        position.y = y;

        Draw();
    }

    public float GetWidth() {
        return mWidth;
    }

    public float GetHeight() {
        return mHeight;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public int GetID() {
        return mID;
    }

    public void SetID(int id) {
        mID = id;
    }

    public float getColorRed() {
        return colorRed;
    }

    public void setColorRed(float _colorRed) {

        colorRed = _colorRed;
        if (colorRed > 1.0f)
            colorRed = 1.0f;

        if (colorRed < 0.0f)
            colorRed = 0.0f;
    }

    public float getColorGreen() {
        return colorGreen;
    }

    public void setColorGreen(float _colorGreen) {

        colorGreen = _colorGreen;
        if (colorGreen > 1.0f)
            colorGreen = 1.0f;

        if (colorGreen < 0.0f)
            colorGreen = 0.0f;
    }

    public float getColorBlue() {
        return colorBlue;
    }

    public void setColorBlue(float _colorBlue) {
        colorBlue = _colorBlue;
        if (colorBlue > 1.0f)
            colorBlue = 1.0f;

        if (colorBlue < 0.0f)
            colorBlue = 0.0f;
    }

    public float getColorAlpha() {
        return colorAlpha;
    }

    public void setColorAlpha(float _colorAlpha) {
        colorAlpha = _colorAlpha;
        if (colorAlpha > 1.0f)
            colorAlpha = 1.0f;

        if (colorAlpha < 0.0f)
            colorAlpha = 0.0f;
    }
}