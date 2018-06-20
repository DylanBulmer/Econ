package com.piggahbrostudios.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class EconExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.GREEN, "Hello console!"));
			return CommandResult.success();
		} else {
			Player player = (Player) src;		// Player who send command
						
			player.sendMessage(Text.of(TextColors.AQUA, "Hello, " + player.getName() + ". You entered: " + args.getOne("id")));
			return CommandResult.success();
		}
	}

}
