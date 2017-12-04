package game;

import effects.Action;
import effects.Projectile;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.sound.SoundFile;
import engine.Agent;

import java.util.ArrayList;

public class Launcher implements Action {
    private ArrayList<Projectile> bullets;
    private float cooldown;
    SoundFile sound;

    private static final float BULLET_SPEED = 600;
    private static final float DELAY = 0.2f;

    public Launcher(SoundFile launcher_sound) {
        bullets = new ArrayList<>(4);
        sound = launcher_sound;
    }

    @Override
    public void ready(PVector position, PVector target) {

    }

    public void activate(PVector position, PVector target) {
        if (cooldown < 0) {
            sound.play();
            bullets.add(new Projectile(position, target, BULLET_SPEED));
            cooldown = DELAY;
        }
    }

    @Override
    public void interrupt() {

    }

    public void update(float delta_time) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Projectile bullet = bullets.get(i);
            bullet.update(delta_time);
            if (bullet.lifetime < 0) {
                bullets.remove(i);
            }
        }
        cooldown -= delta_time;
    }

    @Override
    public void display(PGraphics graphics) {
        for (Projectile bullet : bullets) {
            bullet.display(graphics);
        }
    }

    public void collideWithAgent(Agent agent) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Projectile bullet = bullets.get(i);
            if(bullet.collideWithAgent(agent, a -> a.addImpulse(bullet.position, 1024))) {
                bullets.remove(i);
            }
        }
    }

}