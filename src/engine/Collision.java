package engine;

import processing.core.PApplet;
import processing.core.PVector;

// TODO: Make functions return collision depth?
// TODO: circleRect
// TODO: Probably avoid PVectors within the calcs
public final class Collision {

    private Collision() {}

    // Circular Collision
    public static boolean circular(PVector position_a, float radius_a, PVector position_b, float radius_b) {
        float collision_distance = radius_a + radius_b;
        float distance = PVector.dist(position_a, position_b);
        float collision_depth = collision_distance - distance;
        return (collision_depth > 0);
    }

    // TODO: Is this alright? Should I return early on wrong angle? Branching vs calc
    public static boolean arcCircle(PVector position_a, float radius_a, float angle, float range, PVector position_b, float radius_b) {
        float heading = PVector.sub(position_b, position_a).heading();
        float angle_diff = PApplet.abs(angle - heading);
        float collision_distance = radius_a + radius_b;
        float distance = PVector.dist(position_a, position_b);
        float collision_depth = collision_distance - distance;
        return (collision_depth > 0 && angle_diff < range / 2);
    }

    // Axis-Aligned Boundary Box Collision
    public static boolean AABB(PVector position_a, PVector dimensions_a, PVector position_b, PVector dimensions_b) {
        return AABB(position_a, dimensions_a, position_b, dimensions_b.x, dimensions_b.y);
    }

    public static boolean AABB(PVector position_a, PVector dimensions_a, PVector position_b, float width_b, float height_b) {
        float collision_distance_x = dimensions_a.x / 2 + width_b / 2;
        float collision_distance_y = dimensions_a.y / 2 + height_b / 2;
        PVector center = PVector.add(position_a, PVector.div(dimensions_a, 2));
        PVector offset = PVector.sub(center, position_b);
        float collision_depth_x = collision_distance_x - PApplet.abs(offset.x);
        float collision_depth_y = collision_distance_y - PApplet.abs(offset.y);
        return (collision_depth_x > 0) && (collision_depth_y > 0);
    }

    public static boolean lineCircle(PVector line_start, PVector line_end, PVector circle_position, float circle_radius) {
        PVector line_vector = PVector.sub(line_end, line_start);
        PVector circle_vector = PVector.sub(circle_position, line_start);

        float u = (circle_vector.x * line_vector.x + circle_vector.y * line_vector.y) / line_vector.magSq();

        PVector closestPoint = (u < 0)
                ? line_start
                : (u > 1)
                    ? line_end
                    : PVector.add(line_start, PVector.mult(line_vector, u));

        return (closestPoint.dist(circle_position) < circle_radius);
    }

    // TODO: Make another version of this that handles intersection point
    public static boolean lineSegments(PVector a_start, PVector a_end, PVector b_start, PVector b_end) {
        PVector a_vector = PVector.sub(a_end, a_start);
        PVector b_vector = PVector.sub(b_end, b_start);

        float d = b_vector.y * a_vector.x - b_vector.x * a_vector.y;

        // No collision if lines are parallel
        if (d == 0) return false;

        float n_a = b_vector.x * (a_start.y - b_start.y) - b_vector.y * (a_start.y - b_start.x);
        float n_b = a_vector.x * (a_start.y - b_start.y) - a_vector.y * (a_start.x - b_start.y);

        // Segment fraction will be between 0 and 1 inclusive if the lines intersect
        float ua = n_a / d;
        float ub = n_b / d;

        if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
            PVector intersection_point_a = PVector.add(a_start, PVector.mult(a_vector, ua));
            PVector intersection_point_b = PVector.add(b_start, PVector.mult(b_vector, ub));
            return true;
        }
        return false;
    }

    // Point Collision
    public static boolean pointBox(PVector point, PVector position, PVector dimensions) {
        return (point.x >= position.x) && (point.x <= (position.x + dimensions.x))
                && (point.y >= position.y) && (point.y <= (position.y + dimensions.y));
    }

    // Point Collision
    public static boolean pointCircle(PVector point, PVector position, float radius) {
        return PVector.sub(position, point).mag() < radius;
    }
}
