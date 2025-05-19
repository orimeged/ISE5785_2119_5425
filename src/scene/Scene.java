package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;
/**
 * The Scene class represents a 3D scene containing geometries, background color,
 * and lighting information.
 *
 * @author Ori meged and Natanel hasid
 */
public class Scene {
    // Collection of geometries in the scene
    public Geometries geometries = new Geometries();

    // Name of the scene
    public String name;

    // Background color of the scene
    public Color background = new Color(java.awt.Color.black);

    // Ambient light of the scene
    public AmbientLight ambientLight = AmbientLight.NONE;

    // Constructor to initialize the scene with a name
    public Scene(String sceneName) {
        name = sceneName;
    }

    // Method to set the geometries in the scene
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    // Method to set the background color of the scene
    public Scene setBackground(Color back) {
        this.background = back;
        return this;
    }

    // Method to set the ambient light of the scene
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }
}