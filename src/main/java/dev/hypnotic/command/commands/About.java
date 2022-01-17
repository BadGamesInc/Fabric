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
package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.command.Command;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class About extends Command {

	public About() {
		super("about", "For people who need help");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			Wrapper.tellPlayerRaw(ColorUtils.red + "\n\u00A7l\u00A7nHypnotic\n");
			Wrapper.tellPlayerRaw(ColorUtils.red + "Current build" + ColorUtils.gray + ": " + Hypnotic.version);
			Wrapper.tellPlayerRaw(ColorUtils.red + "Modules" + ColorUtils.gray + ": " + ModuleManager.INSTANCE.modules.size());
			Wrapper.tellPlayerRaw(ColorUtils.red + "Commands" + ColorUtils.gray + ": " + CommandManager.get().getAll().size());
			Wrapper.tellPlayerRaw(ColorUtils.red + "Website" + ColorUtils.gray + ": https://hypnotic.dev");
			Wrapper.tellPlayerRaw("\n\n\u00A7c                 .,;;;,.                \n"
					+ "                .xXNNN0:    .....       \n"
					+ "               .lXMMMNd.   ;OXXKd.      \n"
					+ "               :KWMWNk'   ,OWMM0:       \n"
					+ "              'kWMW0d;   .dNMMNo.       \n"
					+ "             .oNMMXl..   cXMMWx.        \n"
					+ "            .cKMMWk,.  .;OWMW0:.        \n"
					+ "  ..;ldxkkxxOXWMMWKkkxkOXWMMWXOxxxxxxxdo\n"
					+ ".:kXWWXOdc:oKMMMKo:;;;l0WMMNkc;;;;;;;;,.\n"
					+ "ONMWKd,.  .dXMMKc.    ;0MMWO'           \n"
					+ "MMMXc.   .dNMWO:     'OWMM0:            \n"
					+ "MMWXc...l0NNKo.    ..cKNNKl.            \n"
					+ "OKNWKOkO0Od:.        .','..             \n"
					+ "..;:cc:,..   \n");
			return SINGLE_SUCCESS;
		});
	}
	
}
