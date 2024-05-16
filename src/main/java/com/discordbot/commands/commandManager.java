package com.discordbot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class commandManager extends ListenerAdapter {

    private Map<String, Integer> warnings = new HashedMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        // Command #1 /welcome := Greets the new joining member in Guild
        if (command.equals("welcome")) {

            String userTag = event.getUser().getAsMention();
            event.reply("Welcome to the server !! " + userTag + " !!").setEphemeral(true).queue();
        }

        // Command #2 /roles := provides the list of all the roles available in the Guild
        else if (command.equals("roles")) {

            event.deferReply().queue();
            String response = "";
            for (Role role : event.getGuild().getRoles()) {
                response += role.getAsMention() + "\n";
            }
            event.getHook().sendMessage(response).queue();
        }

        // Command #3 /say := spread any message from the Bot
        else if (command.equals("say")) {
            OptionMapping messageOption = event.getOption("message");
            String message = messageOption.getAsString();

            OptionMapping channelOption = event.getOption("channel");
            MessageChannel channel;

            if (channelOption != null) {
                channel = (MessageChannel) channelOption.getAsChannel();
            } else {
                channel = event.getChannel();
            }

            channel.sendMessage(message).queue();
            event.reply("your message is sent").setEphemeral(true).queue();

        }

        // #Command #4 /warn := warn any member for any actions, given by admin 
        else if (event.getName().equals("warn")) {

            // Check if the user who sent the command has administrator permissions
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

                OptionMapping userOption = event.getOption("username");

                User mentionedUser = userOption.getAsUser(); // Get the mentioned user

                String userId = mentionedUser.getId();
                // checking for warnings
                warnings.put(userId, warnings.getOrDefault(userId, 0) + 1);

                // checking if user has reached warning limit

                if (warnings.getOrDefault(userId, 0) >= 3) {
                    event.getGuild().kick(mentionedUser).queue();
                    event.reply("user @ " + userId + " got kicked from the server ").queue();
                } else {
                    event.reply("Warning user <@" + userId + "> for inappropriate action.").queue();
                }
            } else {
                event.reply("You must be an administrator to use this command.").setEphemeral(true).queue();
            }
        }

        // Command #5 /giverole := bot can give role to members, given by admin
        else if (command.equals("giverole")) {
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {

                Member member = event.getOption("user").getAsMember();
                Role role = event.getOption("role").getAsRole();
                event.getGuild().addRoleToMember(member, role).queue();
                event.reply(member.getAsMention() + " has been given the " + role.getAsMention() + " role!").queue();
            } else {
                event.reply("You need to have ADMINISTRATOR ROLE FOR USING THIS COMMAND").setEphemeral(true).queue();
            }
        }

    }

    // for tregistering the slash commands on the guilds
    // guild commands
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>()
        ;
        commandData.add(Commands.slash("welcome", "get the greetings from the bot"));
        commandData.add(Commands.slash("roles", "get all the roles in the guild"));

        OptionData option1 = new OptionData(OptionType.STRING, "message", "this will sent the message", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel", "this will send the message to specific channel");
        commandData.add(Commands.slash("say", "bot will send the message").addOptions(option1, option2));
        
        OptionData option3 = new OptionData(OptionType.USER, "username", "This will warn the user", true);
        commandData.add(Commands.slash("warn", "Will warn the user for inappropriate action").addOptions(option3));

        event.getGuild().updateCommands().addCommands(commandData).queue();

    }

    // GLOBAL COMMANDS ACCESSIBLE EVERYWHERE
    @Override
    public void onReady(ReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();
        
        commandData.add(Commands.slash("welcome", "get the greetings from the bot"));
        commandData.add(Commands.slash("roles", "get all the roles in the guild"));
        OptionData option1 = new OptionData(OptionType.STRING, "message", "this will sent the message", true);
        OptionData option2 = new OptionData(OptionType.CHANNEL, "channel",
                "this will send the message to specific channel")
                .setChannelTypes(ChannelType.TEXT, ChannelType.GUILD_PUBLIC_THREAD);
        commandData.add(Commands.slash("say", "bot will send the message").addOptions(option1, option2));
        OptionData option3 = new OptionData(OptionType.USER, "username", "This will warn the user", true);
        commandData.add(Commands.slash("warn", "Will warn the user for inappropriate action").addOptions(option3));
        OptionData option4 = new OptionData(OptionType.USER, "user", "The user to give the role to", true);
        OptionData option5 = new OptionData(OptionType.ROLE, "role", "The role to be given", true);
        commandData.add(Commands.slash("giverole", "Give a user a role").addOptions(option4, option5));

        event.getJDA().updateCommands().addCommands(commandData).queue();

    }

}
