package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class AntiHunger extends Mod {

	private boolean lastOnGround;
    private boolean sendOnGroundTruePacket;
    private boolean ignorePacket;
    
	public AntiHunger() {
		super("AntiHunger", "reduce your need to consume yummies", Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		lastOnGround = mc.player.isOnGround();
        sendOnGroundTruePacket = true;
		super.onEnable();
	}
	
	@EventTarget
	public void sendPacket(EventSendPacket event) {
		if (ignorePacket) return;
		
		if (event.getPacket() instanceof ClientCommandC2SPacket) {
			ClientCommandC2SPacket.Mode mode = ((ClientCommandC2SPacket) event.getPacket()).getMode();
			
			if (mode == ClientCommandC2SPacket.Mode.START_SPRINTING || mode == ClientCommandC2SPacket.Mode.STOP_SPRINTING) {
				event.setCancelled(true);
			}
		}
		
		if (event.getPacket() instanceof PlayerMoveC2SPacket && mc.player.isOnGround() && mc.player.fallDistance <= 0.0 && !mc.interactionManager.isBreakingBlock()) {
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(false);
        }
	}
	
	@Override
	public void onTick() {
		if (mc.player.isOnGround() && !lastOnGround && !sendOnGroundTruePacket) sendOnGroundTruePacket = true;

        if (mc.player.isOnGround() && sendOnGroundTruePacket) {
            ignorePacket = true;
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
            ignorePacket = false;

            sendOnGroundTruePacket = false;
        }

        lastOnGround = mc.player.isOnGround();
		super.onTick();
	}
}
