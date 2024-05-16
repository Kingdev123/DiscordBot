package com.discordbot.commands;

import java.util.List;
import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class commandManager2 extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("trivia")) {
            String question = event.getOption("question").getAsString();
            String choice1Label = event.getOption("choice1").getAsString();
            String choice2Label = event.getOption("choice2").getAsString();


            Button choice1 = Button.danger("rightAns", choice1Label);
            Button choice2 = Button.danger("wrongAns", choice2Label);

            EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setDescription(question);

            event.replyEmbeds(embedBuilder.build())
                        .addActionRow(choice1, choice2).queue();   
           
        }

        else if (command.equals("embed")){
            String title = event.getOption("title").getAsString();
            String description = event.getOption("description").getAsString();
            Integer color = event.getOption("color").getAsInt();

            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setColor(color);

            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();

        OptionData titleOption = new OptionData(OptionType.STRING, "title", "The title of the embed", true);
        OptionData descriptionOption = new OptionData(OptionType.STRING, "description", "The description of the embed", true);
        OptionData colorOption = new OptionData(OptionType.INTEGER, "color", "The color of the embed in integer format", true);
        commandData.add(Commands.slash("embed", "get your embed ready").addOptions(titleOption, descriptionOption, colorOption));

        OptionData question = new OptionData(OptionType.STRING, "question", "Write your trivia question here", true);
        OptionData choice1 = new OptionData(OptionType.STRING, "choice1", "enter the right answer here", true);
        OptionData choice2 = new OptionData(OptionType.STRING, "choice2", "enter the wrong answer here", true);
        commandData.add(Commands.slash("trivia", "play trivia with your guild members").addOptions(question, choice1, choice2));

        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onReady(ReadyEvent event) {

        List<CommandData> commandData = new ArrayList<>();

        OptionData titleOption = new OptionData(OptionType.STRING, "title", "The title of the embed", true);
        OptionData descriptionOption = new OptionData(OptionType.STRING, "description", "The description of the embed", true);
        OptionData colorOption = new OptionData(OptionType.INTEGER, "color", "The color of the embed in integer format", true);
        commandData.add(Commands.slash("embed", "get your embed ready").addOptions(titleOption, descriptionOption, colorOption));

        OptionData question = new OptionData(OptionType.STRING, "question", "Write your trivia question here", true);
        OptionData choice1 = new OptionData(OptionType.STRING, "choice1", "enter the right answer here", true);
        OptionData choice2 = new OptionData(OptionType.STRING, "choice2", "enter the wrong answer here", true);
        commandData.add(Commands.slash("trivia", "play trivia with your guild members").addOptions(question, choice1, choice2));

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }

}
