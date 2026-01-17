package motd;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.uprank.server.proxy.module.api.Module;
import de.uprank.server.proxy.module.api.VelocityModule;
import de.uprank.server.proxy.module.api.context.ModuleContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

@Module(id = "motd-module", name = "MOTD Module", version = "1.0-SNAPSHOT")
public class MotdModule implements VelocityModule {

    private ModuleContext context;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String motdLine1 = "<gradient:#00ffcc:#0066ff><bold>Mein Velocity Netzwerk</bold></gradient>";
    private final String motdLine2 = "<gray>▶ <white>Lobby, Skyblock, Citybuild</white> <gray>| <green>Online!</green>";

    @Override
    public void onEnable(ModuleContext ctx) {
        this.context = ctx;

        this.context.moduleManager().registerListener(this);
    }

    @Override
    public void onDisable() {

    }

    @Subscribe
    public void onServerListPing(ProxyPingEvent event) {
        ServerPing ping = event.getPing();

        Component motd = Component.empty()
                .append(this.miniMessage.deserialize(motdLine1))
                .append(Component.newline())
                .append(this.miniMessage.deserialize(motdLine2));

        ServerPing.Builder builder = ping.asBuilder()
                .description(motd);

        event.setPing(builder.build());
    }

}
