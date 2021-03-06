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
import com.sasha.asuna.mod.misc.Manager;
import com.sasha.simplecmdsys.SimpleCommand;
import com.sasha.simplecmdsys.SimpleCommandInfo;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Sasha on 08/08/2018 at 9:26 PM
 **/
@SimpleCommandInfo(description = "Toggle a named feature", syntax = {"<feature>"})
public class ToggleCommand extends SimpleCommand {
    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void onCommand() {
        if (this.getArguments() == null) {
            AsunaMod.logErr(false, "Arguments required! \"-toggle <feature>\"");
            return;
        }
        AtomicBoolean found = new AtomicBoolean(false);
        Manager.Feature.getTogglableFeatures().forEachRemaining(mod -> {
            if (mod.getFeatureName().equalsIgnoreCase(this.getArguments()[0])) {
                mod.toggleState();
                AsunaMod.logMsg(false, "Toggled " + mod.getFeatureName());
                found.set(true);
            }
        });
        if (!found.get()) {
            AsunaMod.logErr(false, "Couldn't find the specified feature.");
        }
    }
}
