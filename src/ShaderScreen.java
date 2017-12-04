import ch.bildspur.postfx.*;
import ch.bildspur.postfx.builder.PostFX;
import ch.bildspur.postfx.pass.*;

import postprocessing.NegatePass;
import processing.core.PApplet;
import processing.core.PGraphics;

import engine.*;

import static processing.core.PConstants.BLEND;
import static processing.core.PConstants.P3D;
import static processing.core.PConstants.SCREEN;

public class ShaderScreen extends Screen {
    PGraphics canvas;
    PostFXSupervisor supervisor;
    SobelPass sobelPass;
    NegatePass negatePass;

    float rotationX = 0;
    float rotationY = 0;

    ShaderScreen(PApplet applet, PostFXSupervisor applet_supervisor) {
        super(applet);
        supervisor = applet_supervisor;
        canvas = applet.createGraphics(applet.width, applet.height, P3D);
        sobelPass = new SobelPass(applet);
        negatePass = new NegatePass(applet);
    }

    @Override
    public void load() {
    }

    @Override
    public void update(float deltatime) {

    }

    @Override
    public PGraphics render() {
        // draw a simple rotating cube around a sphere
        canvas.beginDraw();
        canvas.background(55);

        canvas.pushMatrix();

        canvas.translate(canvas.width / 2, canvas.height / 2);
        canvas.rotateX(rotationX);
        canvas.rotateY(rotationY);

        canvas.noStroke();
        canvas.fill(20, 20, 20);
        canvas.box(100);

        canvas.fill(150, 255, 255);
        canvas.sphere(60);

        canvas.popMatrix();
        canvas.endDraw();

        return canvas;
    }

    public void renderFX(PostFX fx) {
        applet.blendMode(SCREEN);
        fx.render(canvas).sobel().blur(5, 50).compose();
        applet.blendMode(BLEND);
    }

//    public void display(PGraphics g) {
//        g.blendMode(BLEND);
//        supervisor.render(canvas);
//        supervisor.pass(sobelPass);
//        supervisor.pass(negatePass);
//        supervisor.compose();
//    }

    @Override
    public void unload(Screen next_screen) {

    }
}
