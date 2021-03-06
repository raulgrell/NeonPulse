package extra;

import engine.*;
import effects.Projectile;
import game.Player;
import processing.core.PGraphics;
import processing.sound.SoundFile;

import java.util.ArrayList;

public class Launcher implements Item {
    private final ArrayList<Projectile> bullets;
    private final SoundFile sound;
    private final Player player;

    private float cooldown;

    private static final float BULLET_SPEED = 600;
    private static final float DELAY = 0.2f;

    public Launcher(Player player, SoundFile launcher_sound) {
        bullets = new ArrayList<>(4);
        sound = launcher_sound;
        this.player = player;
    }

    @Override
    public void ready() {

    }

    public void activate() {
        if (cooldown < 0) {
            sound.play();
            bullets.add(new Projectile(player.position, player.target, BULLET_SPEED));
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
            if (bullet.getLifetime() < 0) {
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

    public boolean collideWithAgent(Agent agent) {
        boolean collided = false;
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Projectile bullet = bullets.get(i);
            if (bullet.collideWithAgent(agent, a -> a.addImpulse(bullet.getPosition(), 1024))) {
                bullets.remove(i);
                collided = true;
            }
        }
        return collided;
    }

    public void collideWithLevel(Level level) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Projectile bullet = bullets.get(i);
            if(level.checkCollision(bullet.getPosition().x, bullet.getPosition().y))
                bullets.remove(i);
        }
    }
}
