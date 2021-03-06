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

package com.sasha.asuna.mod.feature.impl.chat;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.asuna.mod.events.playerclient.PlayerBlockBreakEvent;
import com.sasha.asuna.mod.events.playerclient.PlayerBlockPlaceEvent;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.IAsunaTickableFeature;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Sasha on 11/08/2018 at 4:31 PM
 **/
@FeatureInfo(description = "Sends a message in chat every 15 seconds about what you're doing in the world.")
public class AnnouncerFeature extends AbstractAsunaTogglableFeature implements SimpleListener,
        IAsunaTickableFeature {

    static boolean swap = false;
    private static int counter = 0;

    private LinkedHashMap<String, Integer> blocksBrokenMap = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> blocksPlacedMap = new LinkedHashMap<>();

    public AnnouncerFeature() {
        super("Announcer", AsunaCategory.CHAT);
    }

    @Override
    public void onTick() {
        if (!this.isEnabled()) return;
        counter++;
        if (counter > 20 * 15) {
            counter = 0;
            Random rand = new Random();
            boolean randBool = rand.nextBoolean();
            if (randBool) {
                if (blocksBrokenMap.isEmpty()) {
                    return;
                }
                String key = "";
                for (Map.Entry<String, Integer> stringIntegerEntry : blocksBrokenMap.entrySet()) {
                    key = stringIntegerEntry.getKey();
                    AsunaMod.minecraft.player.sendChatMessage("> I just mined " + stringIntegerEntry.getValue() + " " +
                            stringIntegerEntry.getKey());
                    break;
                }
                blocksBrokenMap.remove(key);
            } else {
                if (blocksPlacedMap.isEmpty()) {
                    return;
                }
                String key = "";
                for (Map.Entry<String, Integer> stringIntegerEntry : blocksPlacedMap.entrySet()) {
                    key = stringIntegerEntry.getKey();
                    AsunaMod.minecraft.player.sendChatMessage("> I just placed " + stringIntegerEntry.getValue() + " " +
                            stringIntegerEntry.getKey());
                    break;
                }
                blocksPlacedMap.remove(key);
            }
        }
    }

    @SimpleEventHandler
    public void onBlockBreak(PlayerBlockBreakEvent e) {
        if (!this.isEnabled()) return;
        //logMsg("ok");
        if (blocksBrokenMap.containsKey(e.getBlock().getLocalizedName())) {
            //logMsg("ok 1");
            blocksBrokenMap.put(e.getBlock().getLocalizedName(), blocksBrokenMap.get(e.getBlock().getLocalizedName()) + 1);
            return;
        }
        //logMsg("oh ok");
        blocksBrokenMap.put(e.getBlock().getLocalizedName(), 1);
    }

    @SimpleEventHandler
    public void onBlockBreak(PlayerBlockPlaceEvent e) {
        if (!this.isEnabled()) return;
        //logMsg("ok");
        if (blocksPlacedMap.containsKey(e.getBlock().getDisplayName())) {
            //logMsg("ok 1");
            blocksPlacedMap.put(e.getBlock().getDisplayName(), blocksPlacedMap.get(e.getBlock().getDisplayName()) + 1);
            return;
        }
        //logMsg("oh ok");
        blocksPlacedMap.put(e.getBlock().getDisplayName(), 1);
    }

}
