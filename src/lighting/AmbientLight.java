package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * The AmbientLight class represents the ambient light in a scene.
 * Ambient light is a constant light that affects all objects equally,
 * simulating indirect lighting in the environment.
 *
 * @author Ori meged and Natanel hasid
 */
public class AmbientLight {

    // The intensity of the ambient light
    private final Color intensity;

    // A static instance representing no ambient light
    public static AmbientLight NONE = new AmbientLight(Color.BLACK, 0);

    /**
     * Constructor to initialize ambient light with a color and a scaling factor.
     *
     * @param IA the base color of the ambient light
     * @param KA the scaling factor for the intensity
     */
    public AmbientLight(Color IA, Double3 KA) {
        this.intensity = IA.scale(KA);
    }

    /**
     * Constructor to initialize ambient light with a color and a single scaling factor.
     *
     * @param IA the base color of the ambient light
     * @param KA the scaling factor for the intensity
     */
    public AmbientLight(Color IA, double KA) {
        this.intensity = IA.scale(KA);
    }

    /**
     * Constructor to initialize ambient light with a specific intensity.
     *
     * @param intensity the intensity of the ambient light
     */
    public AmbientLight(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the intensity of the ambient light.
     *
     * @return the intensity of the ambient light
     */
    public Color getIntensity() {
        return intensity;
    }
}