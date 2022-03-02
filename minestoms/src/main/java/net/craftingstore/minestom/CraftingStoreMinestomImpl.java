package net.craftingstore.minestom;

import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.logging.impl.JavaLogger;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.minestom.events.DonationReceivedEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;

public class CraftingStoreMinestomImpl implements CraftingStorePlugin {

    private final CraftingStoreExtension minestomExtension;
    private final Logger logger;
    private final MinestomExtensionConfiguration configuration;

    CraftingStoreMinestomImpl(CraftingStoreExtension minestomExtension) {
        this.minestomExtension = minestomExtension;
        this.configuration = new MinestomExtensionConfiguration(minestomExtension);
        this.logger = minestomExtension.getLogger();
    }

    @Override
    public boolean executeDonation(Donation donation) {
        if (donation.getPlayer().isRequiredOnline()) {
            Player player = MinecraftServer.getConnectionManager().getPlayer(donation.getPlayer().getUsername());
            if (player == null) {
                return false;
            }
        }

        DonationReceivedEvent event = new DonationReceivedEvent(donation);
        MinecraftServer.getGlobalEventHandler().call(event);
        if (event.isCancelled()) {
            return false;
        }

        MinecraftServer.getSchedulerManager().buildTask(() ->
                MinecraftServer.getCommandManager().execute(
                        MinecraftServer.getCommandManager().getConsoleSender(), donation.getCommand()));
        return true;
    }

    @Override
    public CraftingStoreLogger getLogger() {
        return new JavaLogger(java.util.logging.Logger.getLogger(this.logger.getName()));
    }

    @Override
    public void registerRunnable(Runnable runnable, int delay, int interval) {
        MinecraftServer.getSchedulerManager()
                .scheduleTask(runnable, TaskSchedule.seconds(delay), TaskSchedule.seconds(interval), ExecutionType.SYNC);
    }

    @Override
    public void runAsyncTask(Runnable runnable) {
        MinecraftServer.getSchedulerManager()
                .buildTask(runnable)
                .executionType(ExecutionType.ASYNC)
                .schedule();
    }

    @Override
    public String getToken() {
        return minestomExtension.getConfig().getToken();
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return configuration;
    }

}
