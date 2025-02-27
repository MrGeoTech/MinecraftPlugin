package net.craftingstore.minestom.events;

import net.craftingstore.core.models.donation.Donation;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.UUID;

public class DonationReceivedEvent implements CancellableEvent {

    @Deprecated
    private final String command;
    @Deprecated
    private final String username;
    @Deprecated
    private final UUID uuid;
    @Deprecated
    private final String packageName;
    @Deprecated
    private final int packagePrice;
    @Deprecated
    private final int couponDiscount;

    private Donation donation;

    private boolean cancelled = false;

    @Deprecated
    public DonationReceivedEvent(String command, String username, UUID uuid, String packageName, int packagePrice, int couponDiscount) {
        this.command = command;
        this.username = username;
        this.uuid = uuid;
        this.packageName = packageName;
        this.packagePrice = packagePrice;
        this.couponDiscount = couponDiscount;
    }

    public DonationReceivedEvent(Donation donation) {
        this.command = donation.getCommand();
        this.username = donation.getPlayer().getUsername();
        this.uuid = donation.getPlayer().getUUID();
        this.packageName = donation.getPackage().getName();
        this.packagePrice = donation.getPackage().getPrice();
        this.couponDiscount = donation.getDiscount();
        this.donation = donation;
    }

    public Donation getDonation() {
        return this.donation;
    }

    @Deprecated
    public String getCommand() {
        return command;
    }

    @Deprecated
    public String getUsername() {
        return username;
    }

    @Deprecated
    public UUID getUuid() {
        return uuid;
    }

    @Deprecated
    public String getPackageName() {
        return packageName;
    }

    @Deprecated
    public int getPackagePrice() {
        return packagePrice;
    }

    @Deprecated
    public int getCouponDiscount() {
        return couponDiscount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}