package flairings.agency.misplace.base;

import flairings.agency.misplace.util.CustomLocation;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

public class MisplaceUser {

    @Getter private final String name;
    @Getter private final UUID uuid;
    @Setter @Getter private double misplace;
    @Getter @Setter private CustomLocation lastMovePacket;
    private final Map<UUID, List<CustomLocation>> recentPlayerPackets = new HashMap<>();

    public MisplaceUser(String name, UUID uuid, double misplace) {
        this.name = name;
        this.uuid = uuid;
        this.misplace = misplace;
    }

    public void addPlayerPacket(final UUID playerUUID, final CustomLocation customLocation) {
        List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
        if (customLocations == null) {
            customLocations = new ArrayList<CustomLocation>();
        }
        if (customLocations.size() == 20) {
            customLocations.remove(0);
        }
        customLocations.add(customLocation);
        this.recentPlayerPackets.put(playerUUID, customLocations);
    }

    public CustomLocation getLastPlayerPacket(final UUID playerUUID, final int index) {
        final List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
        if (customLocations != null && customLocations.size() > index) {
            return customLocations.get(customLocations.size() - index);
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setMisplace(final double misplace) {
        this.misplace = misplace;
    }

    public double getMisplace() {
        return this.misplace;
    }

    public CustomLocation getLastMovePacket() {
        return this.lastMovePacket;
    }

    public void setLastMovePacket(final CustomLocation lastMovePacket) {
        this.lastMovePacket = lastMovePacket;
    }
}