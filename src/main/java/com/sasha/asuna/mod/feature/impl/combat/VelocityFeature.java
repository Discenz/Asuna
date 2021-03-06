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
import com.sasha.asuna.mod.events.client.ClientPacketRecieveEvent;
import com.sasha.asuna.mod.events.playerclient.PlayerKnockbackEvent;
import com.sasha.asuna.mod.feature.AbstractAsunaTogglableFeature;
import com.sasha.asuna.mod.feature.AsunaCategory;
import com.sasha.asuna.mod.feature.IAsunaTickableFeature;
import com.sasha.asuna.mod.feature.annotation.FeatureInfo;
import com.sasha.asuna.mod.feature.option.AsunaFeatureOption;
import com.sasha.asuna.mod.feature.option.AsunaFeatureOptionBehaviour;
import com.sasha.eventsys.SimpleEventHandler;
import com.sasha.eventsys.SimpleListener;
import net.minecraft.network.play.server.SPacketExplosion;


/**
 * Created by Sasha at 7:59 PM on 9/2/2018
 */
@FeatureInfo(description = "Reduce or completely ignore knockback")
public class VelocityFeature extends AbstractAsunaTogglableFeature
        implements SimpleListener, IAsunaTickableFeature {
    public VelocityFeature() {
        super("Velocity", AsunaCategory.COMBAT,
                new AsunaFeatureOptionBehaviour(true),
                new AsunaFeatureOption<>("Ignore", true),
                new AsunaFeatureOption<>("Reduce", false));
    }


    @Override
    public void onTick() {
        this.setSuffix(this.getFormattableOptionsMap());
    }

    @SimpleEventHandler
    public void onPlayerKnockBack(PlayerKnockbackEvent e) {
        if (!this.isEnabled()) return;
        if (this.getOption("Ignore")) {
            e.setCancelled(true);
            return;
        }
        e.setMotionX(e.getMotionX() / 3);
        e.setMotionY(e.getMotionY() / 3);
        e.setMotionZ(e.getMotionZ() / 3);
    }
    @SimpleEventHandler
    public void onPlayerPacketKnockBack(ClientPacketRecieveEvent e) {
        if (!this.isEnabled()) return;
        if (e.getRecievedPacket() instanceof SPacketExplosion) {
            SPacketExplosion explosion = e.getRecievedPacket();
            PlayerKnockbackEvent event = new PlayerKnockbackEvent(explosion.getMotionX(),
                    explosion.getMotionY(),
                    explosion.getMotionZ());
            AsunaMod.EVENT_MANAGER.invokeEvent(event);
            if (event.isCancelled()) {
                e.setCancelled(true);
                return;
            }
            explosion.motionX = (float) event.getMotionX();
            explosion.motionY = (float) event.getMotionY();
            explosion.motionZ = (float) event.getMotionZ();
        }
    }
}
