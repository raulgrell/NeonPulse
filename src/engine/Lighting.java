package engine;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;

import static processing.core.PApplet.*;

public class Lighting {
    private final PGraphics lighting;
    private final ArrayList<Light> lights;
    private final PImage lightTexture;

    private static float sqr(float x) {
        return x * x;
    }

    public Lighting(PApplet applet, PImage image) {
        this.lights = new ArrayList<>();
        this.lighting = applet.createGraphics(applet.width, applet.height, P2D);
        lightTexture = image;
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public void addLights(Light[] new_lights) {
        Collections.addAll(lights, new_lights);
    }

    void update(float delta_time) {

    }

    public void display(PGraphics g) {
        lighting.beginDraw();
        lighting.fill(0);
        lighting.stroke(0);
        lighting.background(0, 127);
        lighting.imageMode(CENTER);
        for (Light l : lights) {
            lighting.image(lightTexture, l.position.x, l.position.y, 2 * l.radius, 2 * l.radius);
        }
        lighting.endDraw();

        g.blendMode(MULTIPLY);
        g.image(lighting, 0, 0);
        g.blendMode(BLEND);
    }

    public void clear() {
        lights.clear();
    }
}