package cyano.lootable.events;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cyano.lootable.LootableBodies;
import cyano.lootable.entities.EntityLootableBody;

public class PlayerDeathEventHandler {

    // Vanilla clears these slots before PlayerDropsEvent. The references identify
    // their corresponding drops without taking inventory from other death handlers.
    private final Map<EntityPlayer, ItemStack[]> equipmentAtDeath = new WeakHashMap<EntityPlayer, ItemStack[]>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void rememberEquipment(LivingDeathEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer) || event.entityLiving.worldObj.isRemote || event.isCanceled()) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.entityLiving;
        ItemStack[] equipment = new ItemStack[5];
        equipment[0] = player.getHeldItem();
        for (int i = 0; i < 4; i++) {
            equipment[i + 1] = player.getCurrentArmor(i);
        }
        equipmentAtDeath.put(player, equipment);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void playerDeathEvent(PlayerDropsEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack[] equipment = equipmentAtDeath.remove(player);
        if (player.worldObj.isRemote || event.isCanceled()) {
            return;
        }

        World world = player.worldObj;
        float rotation = player.getRotationYawHead();
        EntityLootableBody corpse = new EntityLootableBody(world);
        corpse.setPositionAndRotation(player.posX, player.posY, player.posZ, rotation, 0);
        corpse.setDeathTime(world.getTotalWorldTime());
        corpse.setOwnerWithoutLookup(player.getGameProfile());

        if (equipment != null) {
            for (Iterator<EntityItem> drops = event.drops.iterator(); drops.hasNext();) {
                EntityItem drop = drops.next();
                ItemStack stack = drop.getEntityItem();
                int slot = equipmentSlot(equipment, stack);
                if (slot >= 0 && !isSoulbound(stack)) {
                    corpse.setCurrentItemOrArmor(slot, EntityLootableBody.applyItemDamage(stack));
                    equipment[slot] = null;
                    drops.remove();
                }
            }
        }

        for (Iterator<EntityItem> drops = event.drops.iterator(); drops.hasNext();) {
            EntityItem drop = drops.next();
            ItemStack stack = drop.getEntityItem();
            if (stack == null || isSoulbound(stack)) {
                continue;
            }
            ItemStack remainder = corpse.vacuumItem(stack);
            if (remainder == null) {
                drops.remove();
            } else {
                drop.setEntityItemStack(remainder);
            }
        }

        if (LootableBodies.addBonesToCorpse) {
            corpse.vacuumItem(new ItemStack(Items.rotten_flesh, world.rand.nextInt(3) + 1));
            corpse.vacuumItem(new ItemStack(Items.bone, world.rand.nextInt(3) + 1));
        }

        world.spawnEntityInWorld(corpse);
        corpse.setRotation(rotation);
    }

    private static int equipmentSlot(ItemStack[] equipment, ItemStack stack) {
        for (int i = 0; i < equipment.length; i++) {
            if (stack != null && stack == equipment[i]) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isSoulbound(ItemStack stack) {
        if (LootableBodies.eioSoulboundID < 0) {
            return false;
        }
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        return enchantments != null && enchantments.containsKey(LootableBodies.eioSoulboundID);
    }
}
