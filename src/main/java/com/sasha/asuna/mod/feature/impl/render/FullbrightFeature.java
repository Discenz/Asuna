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

package com.sasha.asuna.mod.feature.impl.render;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.IAsunaTickableFeature;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.feature.option.AsunaFeatureOption;
import com.sasha.asuna.mod.feature.option.AsunaFeatureOptionBehaviour;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * Created by Sasha on 11/08/2018 at 11:39 AM
 **/
@FeatureInfo(description = "Lets you see in the dark.")
public class FullbrightFeature extends AbstractAsunaTogglableFeature{

    public FullbrightFeature() {
        super(
                "Fullbright", AsunaCategory.RENDER,
                new AsunaFeatureOptionBehaviour(true),
                new AsunaFeatureOption<>("Potion", true),
                new AsunaFeatureOption<>("Gamma", false)
        );

    }

    @Override
    public void onEnable() {
        if(this.getOption("Potion")){
            Minecraft.getMinecraft().gameSettings.gammaSetting = 1;
            Minecraft.getMinecraft().player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, -32767));
        }
        else if(this.getOption("Gamma")){
            Minecraft.getMinecraft().player.removePotionEffect(MobEffects.NIGHT_VISION);
            Minecraft.getMinecraft().gameSettings.gammaSetting = 1000;
        }
    }

    @Override
    public void onDisable() {
        if(this.getOption("Potion")){
            Minecraft.getMinecraft().player.removePotionEffect(MobEffects.NIGHT_VISION);
        }
        else if(this.getOption("Gamma")){
            Minecraft.getMinecraft().gameSettings.gammaSetting = 1;

        }
    }
}
