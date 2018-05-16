package net.andre601.commands.owner;

import net.andre601.commands.Command;
import net.andre601.core.PurrBotMain;
import net.andre601.util.PermUtil;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class CmdRefresh implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {
        TextChannel tc = e.getTextChannel();

        if (!PermUtil.canWrite(e.getMessage()))
            return;

        if(PermUtil.isCreator(e.getMessage())){

            if(PurrBotMain.file.getItem("config", "beta").equalsIgnoreCase("true"))
                return;

            tc.sendMessage(
                    "Clearing stored messages and images...\n" +
                    "Updating Guild-count on discordbots.org..."
            ).queue(msg -> {
                PurrBotMain.clear();
                PurrBotMain.loadRandom();
                PurrBotMain.getAPI().setStats(e.getJDA().getSelfUser().getId(), e.getJDA().getGuilds().size());
                msg.editMessage(
                        "Clearing stored messages and images \\✅\n" +
                        "Updating Guild-count on discordbots.org \\✅"
                ).queueAfter(2, TimeUnit.SECONDS, react -> {
                    if(PermUtil.canReact(e.getMessage()))
                        e.getMessage().addReaction("✅").queue();
                });
            });

            tc.sendMessage("Refresh complete!").queueAfter(3, TimeUnit.SECONDS);
        }else{
            tc.sendMessage(String.format(
                    "%s You aren't my dad!",
                    e.getAuthor().getAsMention())).queue();

            if(PermUtil.canReact(e.getMessage()))
                e.getMessage().addReaction("🚫").queue();
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent e) {

    }

    @Override
    public String help() {
        return null;
    }
}
