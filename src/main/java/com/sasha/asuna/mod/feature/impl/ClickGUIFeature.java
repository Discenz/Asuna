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

package com.sasha.asuna.mod.feature.impl;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.gui.clickgui.AsunaClickGUI;


/**
 * Created by Sasha on 11/08/2018 at 10:27 AM
 **/
@FeatureInfo(description = "Displays the Clickgui")
public class ClickGUIFeature extends AbstractAsunaTogglableFeature {
    public ClickGUIFeature() {
        super("ClickGUI", AsunaCategory.NA);
    }

    @Override
    public void onEnable() {
        AsunaMod.minecraft.displayGuiScreen(new AsunaClickGUI());
        this.toggleState();
    }
}
