package com.discordbot;

import javax.security.auth.login.LoginException;

import com.discordbot.commands.commandManager;
import com.discordbot.commands.commandManager2;
import com.discordbot.listener.EventListeners;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {

    private final Dotenv config;
    private final ShardManager shardManager;

    public Dotenv getDotenv(){
        return config;
    }
    public ShardManager getShardManager(){
        return shardManager;
    }
// throws loginException when bot token is invalid
    public Bot() throws LoginException{
        config = Dotenv.configure().ignoreIfMissing().load();
        String token = config.get("TOKEN");
        
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("YOU"));
        builder.enableIntents(  GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                GatewayIntent.GUILD_PRESENCES,
                                GatewayIntent.MESSAGE_CONTENT);
        builder.setMemberCachePolicy(MemberCachePolicy.ONLINE);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS, CacheFlag.ACTIVITY);
        shardManager = builder.build();

        // registering eventlisteners
        shardManager.addEventListener(new EventListeners(), new commandManager(), new commandManager2());
    }
    
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
        } catch (LoginException e) {
            System.out.println("ERROR: Provided Discord bot token is invalid");
        }
    }
}
