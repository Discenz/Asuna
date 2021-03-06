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

package com.sasha.asuna.mod.mixin.client;

import com.sasha.asuna.mod.feature.impl.movement.PigControlFeature;
import com.sasha.asuna.mod.misc.Manager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityPig.class, priority = 999)
public abstract class MixinEntityPig extends EntityAnimal {

    public MixinEntityPig(World worldIn) {
        super(worldIn);
    }

    @Inject(
            method = "canBeSteered",
            at = @At("HEAD"),
            cancellable = true
    )
    private void preCanBeSteered(CallbackInfoReturnable<Boolean> cir) {
        if (this.getControllingPassenger() instanceof EntityPlayer) {
            if (Manager.Feature.isFeatureEnabled(PigControlFeature.class)) {
                cir.setReturnValue(true);
            }
        }
    }
}
