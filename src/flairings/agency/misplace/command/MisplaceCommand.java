package flairings.agency.misplace.command;

import flairings.agency.misplace.base.MisplaceUser;
import flairings.agency.misplace.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import flairings.agency.misplace.MisplacePlugin;

public class MisplaceCommand extends BukkitCommand {

    public MisplaceCommand(){
        super("misplace");
    }


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can execute this command");
            return false;
        }

        Player p = (Player)commandSender;

        if(!p.hasPermission("misplace.use")) {
            p.sendMessage(CC.translate("&cInsufficient rank to execute this command."));
            return false;
        }

        if(args.length != 1) {
            p.sendMessage(CC.translate("&7&m-------------------------------------------"));
            p.sendMessage(CC.translate("&c&lMisplace &7┃ &fCommand usage"));
            p.sendMessage(CC.translate("&7&m-------------------------------------------"));
            p.sendMessage(CC.translate("&c/misplace <value> &7┃ &fSet a your misplace value"));
            p.sendMessage(CC.translate("&7&m-------------------------------------------"));
            return false;
        }

        try {
            double misplace = Double.parseDouble(args[0]);
            MisplaceUser misplaceUser = MisplacePlugin.getProfileManager().getUser(p);

            if (misplace > 10) {
                commandSender.sendMessage(CC.translate("&cArgument must be below 10"));
                return false;
            }

            misplaceUser.setMisplace(misplace);
            commandSender.sendMessage(CC.translate("&aYour misplace value has been updated to &f" + misplace));

        } catch (Exception ex) {
            p.sendMessage(ChatColor.RED + "Argument must be a double (ex: 0.25)");
            return false;
        }
        return false;
    }
}
