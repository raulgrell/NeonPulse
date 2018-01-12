package effects;

import engine.Agent;
import engine.Tile;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.sound.SoundFile;

public abstract class Effect {
    public final PVector position;
    public boolean active;
    public float lifetime;
    SoundFile sound;
    public boolean live;

    Effect(SoundFile effect_sound) {
        position = new PVector(0,0);
        sound = effect_sound;
        active = false;
        live = false;
        lifetime = 0;
    }

    public abstract boolean collideWithAgent(Agent agent);
    public abstract boolean collideWithTile(Tile tile);

    public abstract void activate(PVector source, PVector target);
    public abstract void update(float delta_time);
    public abstract void display(PGraphics g);
}

