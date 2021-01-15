/*
 * Copyright (c) Sasha Stevens (2017 - 2018)
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.sasha.asuna.mod.feature.impl.combat;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.IAsunaTickableFeature;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.feature.option.AsunaFeatureOption;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import org.lwjgl.input.Mouse;


import static com.sasha.asuna.mod.feature.impl.combat.KillauraFeature.rotateTowardsEntity;

/**
 * Created by Sasha on 12/08/2018 at 8:53 AM
 **/
@FeatureInfo(description = "Automatically hit nearby crystals")
public class CrystalAuraFeature extends AbstractAsunaTogglableFeature implements IAsunaTickableFeature {
    public CrystalAuraFeature() {
        super("CrystalAura", AsunaCategory.COMBAT, new AsunaFeatureOption<>("NoSuicide", false));
    }

    //Calculate damage done to a player by a EnderCrystal
    private float calculateDamage(double x, double y, double z, EntityPlayer player) {
        //Vec3d of Crystal
        Vec3d vec3d = new Vec3d(x,y,z);
        //Explosion Size: Double the End Crystal explosion strength
        float explosionScale = 2.0F * 6.0F;
        //Adjusted for distance
        double distancedExplosionScale = player.getDistance(x,y,z) / (double) explosionScale;
        //Block density and scale
        double density = player.world.getBlockDensity(vec3d, player.getEntityBoundingBox());
        double densityScale = (1.0D - distancedExplosionScale) * density;

        //Unscaled damage
        float damage = (float) ((int) ((densityScale * densityScale + densityScale) / 2.0D * 7.0D * (double) explosionScale + 1.0D));

        //Adjust for difficulty
        damage *= 0.5f * Minecraft.getMinecraft().world.getDifficulty().getId();

        //Adjust for blast reduction
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        Explosion explosion = new Explosion(Minecraft.getMinecraft().world, null, x, y, z, 6.0F, false, true);

        damage *= (1.0F - MathHelper.clamp(EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0F, 20.0F) / 25.0F);

        damage = Math.max(damage - player.getAbsorptionAmount(), 0.0F);

        return damage;
    }

    private float calculateDamage(EntityEnderCrystal crystal, EntityPlayer player) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, player);
    }

    //Checks if totem is held. Used for NoSuicide
    public boolean totemSelected() {
        ItemStack offhand = AsunaMod.minecraft.player.getHeldItem(EnumHand.OFF_HAND);
        ItemStack mainHand = AsunaMod.minecraft.player.getHeldItem(EnumHand.MAIN_HAND);
        if(offhand.getItem() == Items.TOTEM_OF_UNDYING || mainHand.getItem() == Items.TOTEM_OF_UNDYING) return true;
        return false;
    }



    @Override
    public void onTick() {
        if (this.isEnabled()) {
            for (Entity e : AsunaMod.minecraft.world.loadedEntityList) {
                if (!(e instanceof EntityEnderCrystal)) continue;
                if (AsunaMod.minecraft.player.getDistance(e) >= 3.8f) continue;
                if (!e.isEntityAlive()) continue;

                EntityEnderCrystal crystal = (EntityEnderCrystal) e;

                // Do not hit crystal if it will kill you
                if (this.getOption("NoSuicide") && !totemSelected() && calculateDamage(crystal, Minecraft.getMinecraft().player) > AsunaMod.minecraft.player.getHealth())
                    continue;
                //Stop breaking if eating or whatnot
                if (Mouse.isButtonDown(1) && AsunaMod.minecraft.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
                    return;
                }

                AsunaMod.minecraft.playerController.attackEntity(AsunaMod.minecraft.player, crystal);
                AsunaMod.minecraft.player.swingArm(EnumHand.MAIN_HAND);

                break;
            }
        }
    }
}
