package net.andre601.commands.fun;

import net.andre601.commands.Command;
import net.andre601.commands.server.CmdPrefix;
import net.andre601.util.EmbedUtil;
import net.andre601.util.HttpUtil;
import net.andre601.util.PermUtil;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CmdHug implements Command {

    public void usage(Message msg){
        msg.getTextChannel().sendMessage(String.format(
                "%s Please mention a user at the end of the command to hug!\n" +
                "Example: `%shug @*Purr*#6875`",
                msg.getAuthor().getAsMention(),
                CmdPrefix.getPrefix(msg.getGuild())
        )).queue();
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent e) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent e) {

        TextChannel tc = e.getTextChannel();
        Message msg = e.getMessage();

        if (!PermUtil.canWrite(msg))
            return;

        if(!PermUtil.canSendEmbed(e.getMessage())){
            tc.sendMessage("I need the permission, to embed Links in this Channel!").queue();
            if(PermUtil.canReact(e.getMessage()))
                e.getMessage().addReaction("🚫").queue();

            return;
        }

        if(args.length < 1){
            usage(e.getMessage());
            return;
        }

        List<User> user = msg.getMentionedUsers();
        if(user.size() == 1){
            if(user == msg.getJDA().getSelfUser()){
                if(PermUtil.canReact(e.getMessage()))
                    e.getMessage().addReaction("❤").queue();

                tc.sendMessage(String.format("Awwwwww.... Thank you for the hug %s. :heart:",
                        msg.getMember().getAsMention())).queue();
                return;
            }
            if(user == msg.getAuthor()){
                tc.sendMessage("You wanna hug yourself? Are you lonely?").queue();
                return;
            }
            String name = msg.getGuild().getMember(user.get(0)).getAsMention();
            tc.sendMessage(String.format(
                    "%s gave you a hug %s",
                    msg.getMember().getEffectiveName(),
                    name
            )).queue(message -> {
                try{
                    message.editMessage(
                            EmbedUtil.getEmbed().setImage(HttpUtil.getHug()).build()
                    ).queue();
                }catch (Exception ignored){
                }
            });

        }else{
            String users = user.stream().map(User::getAsMention).collect(Collectors.joining(", "));
            tc.sendMessage(String.format(
                    "%s gave you a hug %s",
                    msg.getMember().getEffectiveName(),
                    users
            )).queue(message -> {
                try{
                    message.editMessage(
                            EmbedUtil.getEmbed().setImage(HttpUtil.getHug()).build()
                    ).queue();
                }catch (Exception ignored){
                }
            });
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
