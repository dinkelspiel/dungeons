package dev.keii.dungeons.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;

public class MathUtil {
    public static double lerp(double x, double y, double t) {
        return (1 - t) * x + t * y;
    }

    public static Vec direction(Pos from, Pos to) {
        Vec dir = new Vec(
                to.x() - from.x(),
                to.y() - from.y(),
                to.z() - from.z());
        double length = dir.length();
        if (length == 0.0)
            return Vec.ZERO; // same position, no direction
        return dir.div(length);
    }

    public static Vec lookDirection(Entity entity) {
        Pos pos = entity.getPosition();
        double yawRad = Math.toRadians(pos.yaw());
        double pitchRad = Math.toRadians(pos.pitch());

        double xz = Math.cos(pitchRad);

        double x = -xz * Math.sin(yawRad);
        double y = -Math.sin(pitchRad);
        double z = xz * Math.cos(yawRad);

        Vec dir = new Vec(x, y, z);
        double length = dir.length();
        if (length == 0.0)
            return Vec.ZERO;
        return dir.div(length);
    }

    public static boolean isLookingAt(Entity source, Pos target, double maxAngleDeg) {
        Pos eyePos = source.getPosition().add(0, source.getEyeHeight(), 0);

        Vec toTarget = direction(eyePos, target);
        if (toTarget == Vec.ZERO)
            return false;

        Vec look = lookDirection(source);
        if (look == Vec.ZERO)
            return false;

        // both vectors are normalized, dot = cos(angle)
        double dot = toTarget.dot(look);
        // clamp for safety
        dot = Math.max(-1.0, Math.min(1.0, dot));

        double cosMax = Math.cos(Math.toRadians(maxAngleDeg));
        return dot >= cosMax;
    }

    public static Vec randomVectorInRadius(double radius) {
        double x = (Math.random() * 2 - 1) * radius;
        double y = (Math.random() * 2 - 1) * radius;
        double z = (Math.random() * 2 - 1) * radius;
        return new Vec(x, y, z);
    }
}
