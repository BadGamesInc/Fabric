package badgamesinc.hypnotic.module.combat;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import badgamesinc.hypnotic.utils.player.inventory.FindItemResult;
import badgamesinc.hypnotic.utils.player.inventory.InventoryUtils;
import badgamesinc.hypnotic.utils.world.EntityUtils;
import badgamesinc.hypnotic.utils.world.SortPriority;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoCity extends Mod {

	public NumberSetting range = new NumberSetting("Range", 3, 1, 6, 0.1);
	public BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true);
	public BooleanSetting rotate = new BooleanSetting("Rotate", true);
	public BooleanSetting support = new BooleanSetting("Support", true);
	public BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true);
	
	private PlayerEntity target;
    private BlockPos blockPosTarget;
    private boolean sentMessage;
    
	public AutoCity() {
		super("AutoCity", "City the peeps", Category.WORLD);
		addSettings(range, autoDisable, rotate, support, autoSwitch);
	}
	
    public void onTick() {
        if (EntityUtils.Target.isBadTarget(target, range.getValue())) {
            PlayerEntity search = EntityUtils.Target.getPlayerTarget(range.getValue(), SortPriority.LowestDistance);
            if (search != target) sentMessage = false;
            target = search;
        }

        if (EntityUtils.Target.isBadTarget(target, range.getValue())) {
            target = null;
            blockPosTarget = null;
            if (autoDisable.isEnabled()) this.toggle();
            return;
        }

        blockPosTarget = EntityUtils.getCityBlock(target);

        if (blockPosTarget == null) {
            if (autoDisable.isEnabled()) {
                toggle();
            }
            target = null;
            return;
        }

        if (PlayerUtils.distanceTo(blockPosTarget) > mc.interactionManager.getReachDistance() && autoDisable.isEnabled()) {
            toggle();
            return;
        }

        if (!sentMessage) {
            sentMessage = true;
        }

        FindItemResult pickaxe = InventoryUtils.find(itemStack -> itemStack.getItem() == Items.DIAMOND_PICKAXE || itemStack.getItem() == Items.NETHERITE_PICKAXE);

        if (!pickaxe.isHotbar()) {
            if (autoDisable.isEnabled()) {
                toggle();
            }
            return;
        }

        if (support.isEnabled()) WorldUtils.place(blockPosTarget.down(1), InventoryUtils.findInHotbar(Items.OBSIDIAN), rotate.isEnabled(), 0, true);

        if (autoSwitch.isEnabled()) InventoryUtils.swap(pickaxe.getSlot(), false);

        if (rotate.isEnabled()) RotationUtils.rotate(RotationUtils.getYaw(blockPosTarget), RotationUtils.getPitch(blockPosTarget), () -> mine(blockPosTarget));
        else mine(blockPosTarget);

        if (autoDisable.isEnabled()) toggle();
    }

    private void mine(BlockPos blockPos) {
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, blockPos, Direction.UP));
        mc.player.swingHand(Hand.MAIN_HAND);
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, Direction.UP));
    }
}
