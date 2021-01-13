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
import com.sasha.asuna.mod.events.client.ClientPacketRecieveEvent;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.IAsunaTickableFeature;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.misc.Manager;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sasha.asuna.mod.AsunaMod.COMMAND_PROCESSOR;
import static com.sasha.asuna.mod.AsunaMod.logWarn;

@FeatureInfo(description = "Automatically ignore players that are spamming in chat.")
public class ChatSuffix extends AbstractAsunaTogglableFeature implements SimpleListener{

    private String suffix;

    public ChatSuffix() {
        super("ChatSuffix", AsunaCategory.CHAT);
        suffix = " | " + AsunaMod.NAME;
    }


    Map<String, String> smallText = new HashMap<String, String>()
    {
        {
            put("a", "\u1D00");
            put("b", "\u0299");
            put("c", "\u1D04");
            put("d", "\u1D05");
            put("e", "\u1D07");
            put("f", "\u0493");
            put("g", "\u0262");
            put("h", "\u029C");
            put("i", "\u026A");
            put("j", "\u1D0A");
            put("k", "\u1D0B");
            put("l", "\u029F");
            put("m", "\u1D0D");
            put("n", "\u0274");
            put("o", "\u1D0F");
            put("p", "\u1D18");
            put("q", "\u01EB");
            put("r", "\u0280");
            put("s", "\uA731");
            put("t", "\u1D1B");
            put("u", "\u1D1C");
            put("v", "\u1D20");
            put("w", "\u1D21");
            put("x", "\u0078");
            put("y", "\u028F");
            put("z", "\u1D22");
            put("|", "\u23D0");
            put("-", "\u2013");
            put("!", "\uFF01");
            put("?", "\uFF1F");
        }
    };

    public String getSmallChar(String character){
        for ( String key : smallText.keySet() ) {
            if(key.equals(character)) return smallText.get(key);
        }
        return null;
    }

    public String convert(String text) {
        String converted = text;
        for(char c : converted.toLowerCase().toCharArray()) {
            String character = String.valueOf(c);
            String newChar  = getSmallChar(character);
            converted = StringUtils.replaceIgnoreCase(converted, character, newChar);
        }
        return converted;
    }


    @SimpleEventHandler
    public void onChat(ClientPacketRecieveEvent e) {
        if (this.isEnabled() && e.getRecievedPacket() instanceof CPacketChatMessage) {

            final CPacketChatMessage packet = (CPacketChatMessage) e.getRecievedPacket();

            if(packet.getMessage().startsWith("/") || packet.getMessage().startsWith(COMMAND_PROCESSOR.getCommandPrefix())) return;

            packet.message = packet.getMessage() + convert(suffix);
        }
    }
}
