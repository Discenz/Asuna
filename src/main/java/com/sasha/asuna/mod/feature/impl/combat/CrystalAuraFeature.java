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
import com.sasha.asuna.mod.feature.option.AsunaFeatureOptionBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import org.lwjgl.input.Mouse;


import java.util.Objects;

import static com.sasha.asuna.mod.feature.impl.combat.KillauraFeature.rotateTowardsEntity;

/**
 * Created by Sasha on 12/08/2018 at 8:53 AM
 **/
@FeatureInfo(description = "Automatically hit nearby crystals")
public class CrystalAuraFeature extends AbstractAsunaTogglableFeature implements IAsunaTickableFeature {
    public CrystalAuraFeature() {
        super("CrystalAura", AsunaCategory.COMBAT, new AsunaFeatureOption<>("NoSuicide", false));
    }

    private float calculateExplosionDamage(EntityLivingBase entity, EntityEnderCrystal e) {
        return 0;
    }

    public int getTotemCount () {
        int i = 0;
        for (int x = 0; x <= 44; x++) {
            ItemStack stack = AsunaMod.minecraft.player.inventory.getStackInSlot(x);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void onTick() {
        if (this.isEnabled()) {
            for (Entity e : AsunaMod.minecraft.world.loadedEntityList) {
                if (!(e instanceof EntityEnderCrystal)) continue;
                if (AsunaMod.minecraft.player.getDistance(e) >= 3.8f) continue;
                if (!e.isEntityAlive()) continue;
                // Do not hit crystal if it will kill you
                if (this.getOption("NoSuicide") && getTotemCount() == 0 && calculateExplosionDamage(Minecraft.getMinecraft().player, (EntityEnderCrystal) e) > AsunaMod.minecraft.player.getHealth())
                    continue;
                //Stop breaking if eating or whatnot
                if (Mouse.isButtonDown(1) && AsunaMod.minecraft.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
                    return;
                }
                float yaw = AsunaMod.minecraft.player.rotationYaw;
                float pitch = AsunaMod.minecraft.player.rotationPitch;
                float yawHead = AsunaMod.minecraft.player.rotationYawHead;
                boolean wasSprinting = AsunaMod.minecraft.player.isSprinting();
                rotateTowardsEntity(e);
                AsunaMod.minecraft.player.setSprinting(false);
                AsunaMod.minecraft.playerController.attackEntity(AsunaMod.minecraft.player, e);
                AsunaMod.minecraft.player.swingArm(EnumHand.MAIN_HAND);
                AsunaMod.minecraft.player.rotationYaw = yaw;
                AsunaMod.minecraft.player.rotationPitch = pitch;
                AsunaMod.minecraft.player.rotationYawHead = yawHead;
                if (wasSprinting) {
                    AsunaMod.minecraft.player.setSprinting(true);
                }
                break;
            }
        }
    }
}
