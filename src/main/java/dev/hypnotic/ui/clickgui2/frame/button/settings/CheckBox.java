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
package dev.hypnotic.ui.clickgui2.frame.button.settings;

import java.awt.Color;

import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.clickgui2.frame.button.Button;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

	private BooleanSetting boolSet = (BooleanSetting)setting;
	private Button parent;
	
	public CheckBox(int x, int y, Button parent, Setting setting) {
		super(x, y, setting, parent);
		this.parent = parent;
		this.setting = setting;
		this.boolSet = (BooleanSetting)setting;
		boolSet.displayName = boolSet.name + ": " + boolSet.isEnabled();
	}
	
	int animTicks = 0;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		boolSet.displayName = boolSet.name;
		Screen.fill(matrices, parent.getX(), parent.getY() + offset + parent.getHeight(), parent.getX() + parent.getWidth(), parent.getY() + offset + parent.getHeight() * 2, new Color(40, 40, 40, 255).getRGB());
		HypnoticScreen.fontSmall.drawWithShadow(matrices, (!boolSet.isEnabled() ? ColorUtils.gray : "") + boolSet.displayName, parent.getX() + 4, parent.getY() + 4 + offset + parent.getHeight(), parent.parent.color.getRGB());
		super.render(matrices, mouseX, mouseY, offset);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= parent.getX() && mouseX <= parent.getX() + parent.getWidth() && mouseY >= parent.getY() + offset + parent.getHeight() && mouseY <= parent.getY() + offset + parent.getHeight() * 2;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY) && button == 0 && parent.isExtended()) {
			boolSet.toggle();
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
}
