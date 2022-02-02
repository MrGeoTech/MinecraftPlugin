package net.craftingstore.minestom;

import net.craftingstore.core.PluginConfiguration;

public record MinestomExtensionConfiguration(CraftingStoreExtension extension) implements PluginConfiguration {

    @Override
    public String getName() {
        return "Minestom";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"csb"};
    }

    @Override
    public String getVersion() {
        return extension.getOrigin().getVersion();
    }

    @Override
    public String getPlatform() {
        return "b5cc2c4126";
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        return 200;
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return null;
    }

}
