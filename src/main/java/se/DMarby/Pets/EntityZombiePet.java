package se.DMarby.Pets;

import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityZombie;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;

public class EntityZombiePet extends EntityZombie { // new AI
    private final Player owner;

    public EntityZombiePet(World world, Player owner) {
        super(world);
        this.owner = owner;
        if (owner != null) {
            Util.clearGoals(this.goalSelector, this.targetSelector);
            setBaby(true);
        }
    }

    public EntityZombiePet(World world) {
        this(world, null);
    }

    private int distToOwner() {
        EntityHuman handle = ((CraftPlayer) owner).getHandle();
        return (int) (Math.pow(locX - handle.locX, 2) + Math.pow(locY - handle.locY, 2) + Math.pow(locZ
                - handle.locZ, 2));
    }

    @Override
    protected void bi() {
        super.bi();
        if (owner == null)
            return;
        this.getNavigation().a(((CraftPlayer) owner).getHandle(), 0.3F);
        if (distToOwner() > Util.MAX_DISTANCE)
            this.getBukkitEntity().teleport(owner);
    }

    @Override
    public Entity getBukkitEntity() {
        if (owner != null && bukkitEntity == null)
            bukkitEntity = new BukkitZombiePet(this);
        return super.getBukkitEntity();
    }

    public static class BukkitZombiePet extends CraftZombie implements PetEntity {
        private final Player owner;

        public BukkitZombiePet(EntityZombiePet entity) {
            super((CraftServer) Bukkit.getServer(), entity);
            this.owner = entity.owner;
        }

        @Override
        public Zombie getBukkitEntity() {
            return this;
        }

        @Override
        public Player getOwner() {
            return owner;
        }

        @Override
        public void upgrade() {
        }

        @Override
        public void setLevel(int level) {
            // setSize(level);
        }
    }
}