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
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Created by Sasha on 11/08/2018 at 8:27 PM
 **/
@FeatureInfo(description = "Automatically moves a totem into your offhand if it's empty")
public class AutoTotemFeature extends AbstractAsunaTogglableFeature implements IAsunaTickableFeature {
    public AutoTotemFeature() {
        super("AutoTotem", AsunaCategory.COMBAT);
    }


    @Override
    public void onTick() {
        if (!this.isEnabled())
            return;
        ItemStack offhand = AsunaMod.minecraft.player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        boolean swapOffhand = (offhand.getItem() != Items.TOTEM_OF_UNDYING);

        int i = 0;
        for (int x = 0; x <= 45; x++) {
            ItemStack stack = AsunaMod.minecraft.player.inventory.getStackInSlot(x);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                i++;
                if (swapOffhand && x<=36 && AsunaMod.minecraft.currentScreen == null) {
                    swapOffhand = false;

                    int index = x;

                    if (index<9) {
                        index += 36;
                    }

                    AsunaMod.minecraft.playerController.windowClick(AsunaMod.minecraft.player.inventoryContainer.windowId, index, 0, ClickType.PICKUP, AsunaMod.minecraft.player);
                    AsunaMod.minecraft.playerController.windowClick(AsunaMod.minecraft.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, AsunaMod.minecraft.player);
                    AsunaMod.minecraft.playerController.windowClick(AsunaMod.minecraft.player.inventoryContainer.windowId, index, 0, ClickType.PICKUP, AsunaMod.minecraft.player);
                    AsunaMod.minecraft.playerController.updateController();
                }
            }
        }

        this.setSuffix(i + "");

    }
}
