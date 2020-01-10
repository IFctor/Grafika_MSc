package hu.uni.miskolc.iit.game;

import hu.uni.miskolc.iit.config.AppConfig;
import hu.uni.miskolc.iit.engine.Utils;
import hu.uni.miskolc.iit.engine.Window;
import hu.uni.miskolc.iit.engine.graph.ShaderProgram;
import hu.uni.miskolc.iit.engine.graph.Transformation;
import hu.uni.miskolc.iit.game.managers.GameManager;
import org.joml.Matrix4f;

import java.io.File;

import static org.lwjgl.opengl.GL11.*;

/**
 * Simple renderer class
 *
 * @author Mileff Peter
 */
public class Renderer {
    private static final String PROJECTION_MATRIX = AppConfig.appConfig().getShaders().getUniforms().getProjectionMatrix();
    private static final String WORLD_MATRIX = AppConfig.appConfig().getShaders().getUniforms().getWorldMatrix();
    private static final String MODEL_MATRIX = AppConfig.appConfig().getShaders().getUniforms().getModelMatrix();
    private static final String TEXTURE_SAMPLER = AppConfig.appConfig().getShaders().getUniforms().getTexture_sampler();
    private static final String TEXTURE_COLOR = AppConfig.appConfig().getShaders().getUniforms().getTexture_color();
    private static final String LINE_COLOR = AppConfig.appConfig().getShaders().getUniforms().getLine_color();

    private static final String VERTEX_SHADER_EXTENSION = AppConfig.appConfig().getShaders().getVertex().getExtension();
    private static final String FRAGMENT_SHADER_EXTENSION = AppConfig.appConfig().getShaders().getFragment().getExtension();
    private static final String SYS_SEPARATOR = File.separator;
    private static final String SYS_EXTENSION_SEPARATOR = ".";
    private static final String SHADERS_LOCATION = AppConfig.appConfig().getShaders().getLocation();

    private static final String VERTEX_SHADER = SHADERS_LOCATION + SYS_SEPARATOR + AppConfig.appConfig().getShaders().getVertex().getName() + SYS_EXTENSION_SEPARATOR + VERTEX_SHADER_EXTENSION;
    private static final String LINE_VERTEX_SHADER = SHADERS_LOCATION + SYS_SEPARATOR + AppConfig.appConfig().getShaders().getLineVertex().getName() + SYS_EXTENSION_SEPARATOR + VERTEX_SHADER_EXTENSION;
    private static final String FRAGMENT_SHADER = SHADERS_LOCATION + SYS_SEPARATOR + AppConfig.appConfig().getShaders().getFragment().getName() + SYS_EXTENSION_SEPARATOR + FRAGMENT_SHADER_EXTENSION;
    private static final String LINE_FRAGMENT_SHADER = SHADERS_LOCATION + SYS_SEPARATOR + AppConfig.appConfig().getShaders().getLineFragment().getName() + SYS_EXTENSION_SEPARATOR + FRAGMENT_SHADER_EXTENSION;





    public static Renderer mRenderer;
    private final Transformation transformation;
    public ShaderProgram shaderProgram;
    // Our line drawing shader
    public ShaderProgram lineShader;
    // Orthogonal projection Matrix
    public Matrix4f projectionMatrix;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {

        mRenderer = this;

        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadFile(VERTEX_SHADER));
        shaderProgram.createFragmentShader(Utils.loadFile(FRAGMENT_SHADER));
        shaderProgram.link();

        // Create uniforms for world and projection matrices and texture
        shaderProgram.createUniform(PROJECTION_MATRIX);
        shaderProgram.createUniform(WORLD_MATRIX);
        shaderProgram.createUniform(TEXTURE_SAMPLER);
        shaderProgram.createUniform(TEXTURE_COLOR);

        lineShader = new ShaderProgram();
        lineShader.createVertexShader(Utils.loadFile(LINE_VERTEX_SHADER));
        lineShader.createFragmentShader(Utils.loadFile(LINE_FRAGMENT_SHADER));
        lineShader.link();

        // Create uniforms for world and projection matrices and texture
        lineShader.createUniform(PROJECTION_MATRIX);
        lineShader.createUniform(MODEL_MATRIX);
        lineShader.createUniform(LINE_COLOR);

        // Update orthogonal projection Matrix
        projectionMatrix = transformation.getOrthoProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // Render the sprite
        GameManager.sceneManager.Render();

    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
