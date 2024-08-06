package mimikko.zazalng.puddle.manager;

import mimikko.zazalng.puddle.handlers.EventHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.SessionControllerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.utils.cache.ShardCacheView;
import org.jetbrains.annotations.NotNull;
import java.util.EnumSet;

public class JDAshardManager implements ShardManager {
    protected ShardManager shardManager;
    protected DefaultShardManagerBuilder builder;

    public JDAshardManager(){
        this.shardManager = null;
    }

    public ShardManager getShardManager() {
        return this.shardManager;
    }

    public void setShardManager(ShardManager shardManager){
        this.shardManager = shardManager;
    }

    public void setShardManagerNull(){
        this.shardManager = null;
    }

    public ShardManager buildJDAshardManager(String discordAPI){
        builder = DefaultShardManagerBuilder.create(
                        EnumSet.of(
                                GatewayIntent.GUILD_MEMBERS,
                                GatewayIntent.GUILD_MODERATION,
                                GatewayIntent.GUILD_VOICE_STATES,
                                GatewayIntent.GUILD_PRESENCES,
                                GatewayIntent.MESSAGE_CONTENT,
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                                GatewayIntent.DIRECT_MESSAGES,
                                GatewayIntent.DIRECT_MESSAGE_REACTIONS
                        )
                )
                .setToken(discordAPI)
                .setSessionController(new SessionControllerAdapter())
                .setActivity(Activity.watching("Generating Puddle's World Instance..."))
                .setBulkDeleteSplittingEnabled(false)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.NONE)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                .setEnableShutdownHook(true)
                .setAutoReconnect(true)
                .setContextEnabled(true)
                .setShardsTotal(1);

        builder.addEventListeners(new EventHandler());

        return builder.build();
    }

    @Override
    public int getShardsQueued() {
        return 0;
    }

    @NotNull
    @Override
    public ShardCacheView getShardCache() {
        return null;
    }

    @Override
    public void restart() {

    }

    @Override
    public void restart(int id) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdown(int shardId) {

    }

    @Override
    public void start(int shardId) {

    }

    @Override
    public void login() {

    }
}
