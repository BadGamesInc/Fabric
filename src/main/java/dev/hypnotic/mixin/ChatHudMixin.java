/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventReceiveChat;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

	@Shadow
    @Final
    private List<ChatHudLine<OrderedText>> visibleMessages;

    @Shadow
    private int scrolledLines;

    @Shadow protected abstract void addMessage(Text message, int messageId, int timestamp, boolean refresh);

    @Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;I)V", cancellable = true)
    private void onAddMessage(Text text, int id, CallbackInfo info) {
    	EventReceiveChat event = new EventReceiveChat(text, id);	
    	event.call();
        if (event.isCancelled()) info.cancel();
    }

    @Redirect(method = "addMessage(Lnet/minecraft/text/Text;IIZ)V", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    private int addMessageListSizeProxy(@SuppressWarnings("rawtypes") List<ChatHudLine> list) {
        return 1;
    }
}
