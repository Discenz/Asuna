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

package com.sasha.asuna.mod.command.commands;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.simplecmdsys.SimpleCommand;
import com.sasha.simplecmdsys.SimpleCommandInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

@SimpleCommandInfo(description = "Set or get gamma.", syntax = {""})
public class GammaCommand extends SimpleCommand {
    public GammaCommand() {
        super("gamma");
    }

    @Override
    public void onCommand() {
        if (this.getArguments() == null) {
            AsunaMod.logMsg(false, "Gamma: " + Minecraft.getMinecraft().gameSettings.gammaSetting);
        }
        else {
            int length = Integer.parseInt(this.getArguments()[0]);
            Minecraft.getMinecraft().gameSettings.gammaSetting = length;
        }
    }
}
