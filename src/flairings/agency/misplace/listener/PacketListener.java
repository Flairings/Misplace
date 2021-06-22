package flairings.agency.misplace.listener;

import com.minexd.spigot.handler.PacketHandler;
import flairings.agency.misplace.base.MisplaceUser;
import flairings.agency.misplace.util.CustomLocation;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import flairings.agency.misplace.MisplacePlugin;

public class PacketListener implements PacketHandler {


    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer();
        MisplaceUser misplaceUser = MisplacePlugin.getProfileManager().getUser(player);
        switch (packet.getClass().getSimpleName()) {
            case "PacketPlayInFlying": {
                handleFlyPacket((PacketPlayInFlying) packet, misplaceUser);
                break;
            }
        }
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer();
        MisplaceUser misplaceUser = MisplacePlugin.getProfileManager().getUser(player);
        switch (packet.getClass().getSimpleName()) {
            case "PacketPlayOutEntityTeleport": {
                this.handleTeleportPacket((PacketPlayOutEntityTeleport) packet, misplaceUser, player);
                break;
            }
            case "PacketPlayOutEntityLook":
            case "PacketPlayOutRelEntityMove":
            case "PacketPlayOutRelEntityMoveLook":
            case "PacketPlayOutEntity": {
                this.handleEntityPacket((PacketPlayOutEntity) packet, misplaceUser, player);
                break;
            }
        }
    }

    private void handleTeleportPacket(PacketPlayOutEntityTeleport packet, MisplaceUser misplaceUser, final Player player) {
        final Entity targetEntity = ((CraftPlayer) player).getHandle().getWorld().a(packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            Player target = (Player) targetEntity.getBukkitEntity();
            double x = packet.getB() / 32.0;
            double y = packet.getC() / 32.0;
            double z = packet.getD() / 32.0;
            float yaw = packet.getE() * 360.0f / 256.0f;
            float pitch = packet.getF() * 360.0f / 256.0f;
            if (misplaceUser.getMisplace() != 0.0) {
                CustomLocation lastLocation = misplaceUser.getLastMovePacket();
                float entityYaw = this.getAngle(x, z, lastLocation);
                double addX = Math.cos(Math.toRadians(entityYaw + 90.0f)) * misplaceUser.getMisplace();
                double addZ = Math.sin(Math.toRadians(entityYaw + 90.0f)) * misplaceUser.getMisplace();
                x -= addX;
                z -= addZ;
                packet.setB(MathHelper.floor(x * 32.0));
                packet.setD(MathHelper.floor(z * 32.0));
            }
            misplaceUser.addPlayerPacket(target.getUniqueId(), new CustomLocation(x, y, z, yaw, pitch));
        }
    }

    private void handleEntityPacket(final PacketPlayOutEntity packet, MisplaceUser misplaceUser, final Player player) {
        final Entity targetEntity = ((CraftPlayer) player).getHandle().getWorld().a(packet.getA());
        if (targetEntity instanceof EntityPlayer) {
            final Player target = (Player) targetEntity.getBukkitEntity();
            final CustomLocation customLocation = misplaceUser.getLastPlayerPacket(target.getUniqueId(), 1);
            if (customLocation != null) {
                final double x = packet.getB() / 32.0;
                final double y = packet.getC() / 32.0;
                final double z = packet.getD() / 32.0;
                float yaw = packet.getE() * 360.0f / 256.0f;
                float pitch = packet.getF() * 360.0f / 256.0f;
                if (!packet.isH()) {
                    yaw = customLocation.getYaw();
                    pitch = customLocation.getPitch();
                }
                misplaceUser.addPlayerPacket(target.getUniqueId(), new CustomLocation(customLocation.getX() + x, customLocation.getY() + y, customLocation.getZ() + z, yaw, pitch));
            }
        }
    }

    private void handleFlyPacket(final PacketPlayInFlying packet, final MisplaceUser misplaceUser) {
        final CustomLocation customLocation = new CustomLocation(packet.a(), packet.b(), packet.c(), packet.d(), packet.e());
        final CustomLocation lastLocation = misplaceUser.getLastMovePacket();
        if (lastLocation != null) {
            if (!packet.g()) {
                customLocation.setX(lastLocation.getX());
                customLocation.setY(lastLocation.getY());
                customLocation.setZ(lastLocation.getZ());
            }
            if (!packet.h()) {
                customLocation.setYaw(lastLocation.getYaw());
                customLocation.setPitch(lastLocation.getPitch());
            }
        }
        misplaceUser.setLastMovePacket(customLocation);
    }

    private float getAngle(final double posX, final double posZ, final CustomLocation location) {
        final double x = posX - location.getX();
        final double z = posZ - location.getZ();
        float newYaw = (float) Math.toDegrees(-Math.atan(x / z));
        if (z < 0.0 && x < 0.0) {
            newYaw = (float) (90.0 + Math.toDegrees(Math.atan(z / x)));
        } else if (z < 0.0 && x > 0.0) {
            newYaw = (float) (-90.0 + Math.toDegrees(Math.atan(z / x)));
        }
        return newYaw;
    }
}
