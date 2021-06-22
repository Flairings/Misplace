package flairings.agency.misplace;

import com.minexd.spigot.SpigotX;
import flairings.agency.misplace.command.MisplaceCommand;
import flairings.agency.misplace.listener.PacketListener;
import flairings.agency.misplace.manager.ProfileManager;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class MisplacePlugin extends JavaPlugin {

    @Getter private static ProfileManager profileManager;

    @Override
    public void onEnable(){
            profileManager = new ProfileManager();
            ((CraftServer) getServer()).getCommandMap().register("misplace", new MisplaceCommand());
            SpigotX.INSTANCE.addPacketHandler(new PacketListener());
    }

    public static ProfileManager getProfileManager() {
        return MisplacePlugin.profileManager;
    }
}