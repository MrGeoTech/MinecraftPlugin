package net.craftingstore.minestom;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.core.exceptions.CraftingStoreApiException;
import net.craftingstore.core.jobs.ProcessPendingPaymentsJob;
import net.craftingstore.core.models.api.misc.CraftingStoreInformation;
import net.craftingstore.core.models.api.misc.UpdateInformation;
import net.craftingstore.minestom.commands.CraftingStoreCommand;
import net.craftingstore.minestom.config.Config;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extensions.Extension;

import java.io.File;

public class CraftingStoreExtension extends Extension {

    private Config config;
    private CraftingStore craftingStore;
    private final Component prefix = Utils.color("&7[&cCraftingStore&7] &f");

    @Override
    public void initialize() {
        config = new Config(this, new File(this.getDataDirectory().toFile(), "/src/src/main/resources/config.properties"));
        this.craftingStore = new CraftingStore(new CraftingStoreMinestomImpl(this));
        MinecraftServer.getCommandManager()
                .register(new CraftingStoreCommand(this));
        this.getEventNode()
                .addListener(PlayerLoginEvent.class, this::onPostLogin);
    }

    @Override
    public void terminate() {
        craftingStore.setEnabled(false);
    }

    public Config getConfig() {
        return config;
    }

    public Config getConfigWrapper() {
        return config;
    }

    public CraftingStore getCraftingStore() {
        return craftingStore;
    }

    public Component getPrefix() {
        return prefix;
    }

    public void onPostLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission(this.getCraftingStore().ADMIN_PERMISSION)) {
            return;
        }
        CraftingStoreInformation information = this.getCraftingStore().getInformation();
        UpdateInformation update = null;
        if (information != null) {
            update = information.getUpdateInformation();

            // Update notification
            if (update != null) {
                player.sendMessage(this.getPrefix().append(Utils.color(update.getMessage())));
            }
        }

        if (!this.getCraftingStore().isEnabled()) {
            if (update != null && update.shouldDisable()) {
                player.sendMessage(this.getPrefix().append(Utils.color("The CraftingStore plugin has been disabled because this is an outdated version. Please update the plugin.")));
            } else {
                player.sendMessage(this.getPrefix().append(Utils.color("The CraftingStore plugin has not been set-up correctly. Please set your API key using /csb key <your key>.")));
            }
        }

        String username = player.getUsername();
        this.getCraftingStore().getImplementation().runAsyncTask(() -> {
            try {
                new ProcessPendingPaymentsJob(this.getCraftingStore(), username);
            } catch (CraftingStoreApiException ex) {
                ex.printStackTrace();
            }
        });
    }

}
