package com.piggahbrostudios.commands;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.inject.Inject;
import com.piggahbrostudios.Database;

public class Balance implements CommandExecutor {
	@Inject
	Logger logger;
	
	private Database db;
	
	public Balance(Database database) {
		this.db = database;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		double bal = -2;
		
		// If argument "other" is a Player -> get balance
		if (args.getOne("player").isPresent()) {
			Player other = (Player) args.getOne("player").get();
			
			// Get the balance of the other player
			try {
				bal = db.getBalance(other);
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			
			// Report back to command src
			src.sendMessage(Text.of(TextColors.GREEN, "Balance of " + other.getName() + ": " + bal));
			return CommandResult.success();
		} else {
			// Check to see if src is a player
			if (!(src instanceof Player)) {
				// report the console cannot use this command
				src.sendMessage(Text.of(TextColors.RED, "Console cannot use this command"));
				return CommandResult.success();
			} else {
				
				// get balance of requesting player
				try {
					bal = db.getBalance((Player) src);
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
				
				// Report amount back to player.
				src.sendMessage(Text.of(TextColors.GREEN, "Balance: " + bal));
				return CommandResult.success();
			}
		}
	}

}
