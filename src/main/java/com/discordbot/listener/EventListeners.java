package com.discordbot.listener;

import java.util.List;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListeners extends ListenerAdapter{

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        User user =  event.getUser();
        String reaction = event.getEmoji().getName();
        String channelMention = event.getChannel().getAsMention();

        String message = user.getName() + " reacted to a message with " + reaction + " in the " + channelMention + " channel!";

        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if(message.contains("ping")){
            event.getChannel().sendMessage("pong").queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String avatar = event.getUser().getDefaultAvatarUrl();

        String userName = event.getMember().getEffectiveName(); 

        String welcomeMessage = "Welcome " + userName + " to the server!";
        
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(welcomeMessage).queue();
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(avatar).queue();
    }

    // @Override
    // public void onUserUpdateName(UserUpdateNameEvent event) {
    //     String oldName = event.getOldName();
    //     String newName = event.getUser().getName();

    //     String message = "* " + oldName + " has updated their name to " + newName; 
    //     event.getJDA().get

    // }

    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event) {
        List<Member> members = event.getGuild().getMembers();
        int onlineMembers = 0;
        for(Member member : members){
            if(member.getOnlineStatus() == OnlineStatus.ONLINE){
                onlineMembers++;
            }
        }

        User user = event.getUser();

        String message = "* " + user.getName() + " has updated their status to " + event.getNewOnlineStatus().name()+"!" + " There are " + onlineMembers + " members online now"; 
        event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getButton().getId().equals("rightAns")){
            event.reply("Your answer is right").setEphemeral(true).queue();
        }
        else if (event.getButton().getId().equals("wrongAns")) {
            event.reply("Your answer is wrong").setEphemeral(true).queue();
        }
    }

    
    
    
}
