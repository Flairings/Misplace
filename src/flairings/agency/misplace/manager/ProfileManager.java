package flairings.agency.misplace.manager;

import flairings.agency.misplace.base.MisplaceUser;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class ProfileManager {

    private List<MisplaceUser> users;

    public ProfileManager() {
        users = new ArrayList<>();
    }

    public MisplaceUser getUser(Player player) {
        return this.users.stream().filter(user -> user.getUuid().equals(player.getUniqueId())).findFirst().orElse(new MisplaceUser(player.getName(), player.getUniqueId(), 0.0));
    }
}
