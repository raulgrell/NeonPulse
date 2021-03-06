package game;

import engine.*;
import effects.Pulse;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.sound.SoundFile;
import effects.ParticleSystem;

import static processing.core.PConstants.TWO_PI;

public class Player extends Agent {
    public PlayerInput input;
    public APManager apManager;
    public PVector target;

    public Grenade grenade;
    public Laser laser;
    public ParticleSystem particleSystem;
    public Pulse pulse;
    public Light[] lights;

    public boolean alive;

    public int fill = 0xffffffff;

    private PlayerState state;

    public static final int HEALTH = 1;
    private static final float SPEED = 96;
    private static final float RADIUS = 24;

    interface PlayerState {
        void handleInput(Player player, InputState inputState);
        void update(Player player, float delta_time);
    }

    static class NormalState implements PlayerState {
        @Override
        public void handleInput(Player player, InputState inputState) {
            player.input.handleInput(player);
        }

        @Override
        public void update(Player player, float delta_time) {
        }
    }

    public static class KilledState implements PlayerState {
        PVector spawn_point;
        float timer;

        final float LIFETIME;

        KilledState(PVector spawn_point, float duration) {
            this.spawn_point = new PVector(spawn_point.x, spawn_point.y);
            LIFETIME = duration;
            timer = duration;
        }

        @Override
        public void handleInput(Player player, InputState inputState) {
        }

        @Override
        public void update(Player player, float delta_time) {
            timer -= delta_time;
            player.impulse.set(0, 0);
            player.velocity.mult(0.5f);
            player.angle += 2 * TWO_PI * delta_time;
            player.radius = PApplet.map(timer, 0, LIFETIME, 0, RADIUS);
            if (timer < 0) {
                player.respawn(spawn_point);
            }
        }
    }

    public static final int[] PLAYER_COLORS = {
            0xffC400FF,
            0xff0C71E8,
            0xff00FF4A,
            0xffE8D90C,
            0xffFF740D
    };

    public Player(PApplet applet, PlayerInput playerInput, SoundFile player_sound, Lighting lighting) {
        super();
        input = playerInput;
        state = new NormalState();

        grenade = new Grenade(this, player_sound, applet, lighting);
        laser = new Laser(this, player_sound);
        pulse = new Pulse(player_sound);
        apManager = new APManager(this, RADIUS * 2);

        particleSystem = new ParticleSystem(applet, position, 1, 0, 64);

        lights = new Light[9];
        for (int i = 0; i < lights.length; i++) {
            lights[i] = new Light(position.x, position.y, 400);
        }

        target = new PVector(0, 0);
        health = HEALTH;
        radius = RADIUS;
        mass = 16;
        speed = SPEED;
        angle = 0;
        alive = true;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public void handleInput() {
        state.handleInput(this, null);
    }

    public void update(Level level, float delta_time) {
        state.update(this, delta_time);
        impulse.mult(speed);
        apManager.update(delta_time);
        particleSystem.update(delta_time, position.x, position.y);

        laser.update(delta_time);
        laser.collideWithLevel(level);

        pulse.update(delta_time);

        for (int i = 0; i < 4; i++) {
            grenade.updateMovement(delta_time / 4);
            grenade.collideWithLevel(level);
        }

        grenade.update(delta_time);
    }

    public boolean collideWithEffects(Player other) {
        boolean colliding = false;
        colliding |= other.grenade.collideWithAgent(this);
        colliding |= other.laser.collideWithAgent(this);
        colliding |= other.pulse.collideWithAgent(this);
        colliding |= other.pulse.collideWithPayload(grenade.payload);

        return colliding;
    }

    public void respawn(PVector spawn_point) {
        state = new Player.NormalState();
        alive = true;
        position.set(spawn_point);
        velocity.set(0, 0);
        radius = RADIUS;
        health = HEALTH;
    }

    public void kill(PVector spawn_point) {
        state = new Player.KilledState(spawn_point, 1);
        health = HEALTH;
        alive = false;
    }

    public void updateLights(float delta_time, float x, float y, Level level) {
        for (Light l : lights) {
            l.update(delta_time);
        }
        lights[0].updatePosition(x - level.levelWidth, y - level.levelHeight);
        lights[1].updatePosition(x - level.levelWidth, y);
        lights[2].updatePosition(x - level.levelWidth, y + level.levelHeight);
        lights[3].updatePosition(x, y - level.levelHeight);
        lights[4].updatePosition(x, y);
        lights[5].updatePosition(x, y + level.levelHeight);
        lights[6].updatePosition(x + level.levelWidth, y - level.levelHeight);
        lights[7].updatePosition(x + level.levelWidth, y);
        lights[8].updatePosition(x + level.levelWidth, y + level.levelHeight);
    }

    public void display(PGraphics g) {
        g.pushStyle();
        Draw.player(g, position.x, position.y, angle, radius, fill);

        g.fill(fill);
        g.stroke(fill);

        // Skills
        apManager.display(g, radius + 6);
        grenade.display(g);
        laser.display(g);
        pulse.display(g);
        particleSystem.display(g);

        g.popStyle();
    }

    public void setParticles(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }
}