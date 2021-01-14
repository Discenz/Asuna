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
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.misc.Manager;
import com.sasha.simplesettings.annotation.Setting;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sasha on 11/08/2018 at 11:39 AM
 **/
@FeatureInfo(description = "Makes chosen blocks invisible so that you can find ores or other blocks.")
public class XrayFeature extends AbstractAsunaTogglableFeature {

    @Setting
    public static ArrayList<Integer> xRayBlocks = new ArrayList<>();

    private boolean wasNightVisionsOff = false;

    public XrayFeature() {
        super("XRay", AsunaCategory.RENDER);
    }

    public static List<Block> getXrayBlocks() {
        return XrayFeature.xRayBlocks.stream().map(Block::getBlockById).collect(Collectors.toList());
    }

    public static boolean isVisibleInXray(Block b) {
        return xRayBlocks.contains(b);
    }

    @Override
    public void onEnable() {
        if (!Manager.Feature.isFeatureEnabled(FullbrightFeature.class)) {
            Manager.Feature.findFeature(FullbrightFeature.class).setState(true, true);
            wasNightVisionsOff = true;
        }
        AsunaMod.minecraft.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        if (wasNightVisionsOff) {
            Manager.Feature.findFeature(FullbrightFeature.class).setState(false, true);
            wasNightVisionsOff = false;
        }
        AsunaMod.minecraft.renderGlobal.loadRenderers();
    }

}
