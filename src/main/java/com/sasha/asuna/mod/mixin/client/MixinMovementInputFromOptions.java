package com.sasha.asuna.mod.mixin.client;

import com.sasha.asuna.mod.AsunaMod;
import com.sasha.asuna.mod.feature.impl.movement.InventoryMoveFeature;
import com.sasha.asuna.mod.misc.Manager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = MovementInputFromOptions.class, priority = 999)
public class MixinMovementInputFromOptions extends MovementInput {

    @Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        if (Manager.Feature.isFeatureEnabled(InventoryMoveFeature.class) && AsunaMod.minecraft.currentScreen instanceof GuiContainer) {
            return Keyboard.isKeyDown(keyBinding.getKeyCode());
        }
        return keyBinding.isKeyDown();
    }

}
