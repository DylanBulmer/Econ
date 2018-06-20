package com.piggahbrostudios;

import org.spongepowered.api.Game;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;
import com.piggahbrostudios.commands.Balance;
import com.piggahbrostudios.commands.EconExecutor;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;


@Plugin(id = "econ", name = "Econ", version = "1.0")
public class Econ {
	
	// Get the components needed for the plugin
	@Inject
	Game game;
	
	@Inject
	Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public File configuration = null;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	public ConfigurationLoader<CommentedConfigurationNode> configLoader = null;
	
	public CommentedConfigurationNode configNode = null;

	private Database db;
	
	// Initialize when the server is ready for this plugin
    @Listener
    public void onInit(GameInitializationEvent event) {
    	
    	// Get configuration or create it.
    	try {
    		if (!configuration.exists()) {
    			// create new file and load it.
    			configuration.createNewFile();
    			configNode = configLoader.load();
    			
    			// create the configuration file contents.
    			configNode.getNode("econ", "currency").setValue("$");
    			configNode.getNode("econ").setComment("Econ | A complete economy system.");
    			
    			// save the configuration.
    			configLoader.save(configNode);
    		}
    		
    		// Load the configuration.
    		configNode = configLoader.load();
    		
    	} catch (IOException e) {
    		// report error to logger.
    		logger.error(e.getMessage());
    	}
    	
    	db = new Database(configuration.getParent(), logger);
        
    	// Building Commands
    	CommandSpec econCmd = CommandSpec.builder()
    			.description(Text.of("Basic command for figuring out stuff"))
    			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
    			.executor(new EconExecutor())
    			.build();
    	game.getCommandManager().register(this, econCmd, "econ", "text");
        
    	// Balance Commands
    	CommandSpec balOtherCmd = CommandSpec.builder()
    			.description(Text.of("Basic command for figuring out stuff"))
    			.permission("econ.command.bal.other")
    			.arguments(GenericArguments.player(Text.of("player")))
    			.executor(new Balance(db ))
    			.build();
    	
    	CommandSpec balCmd = CommandSpec.builder()
    			.description(Text.of("Basic command for figuring out stuff"))
    			.permission("econ.command.bal")
    			.arguments(GenericArguments.none())
    			.executor(new Balance(db))
    			.child(balOtherCmd, "other")
    			.build();

    	game.getCommandManager().register(this, balCmd, "balance", "bal");
    	
    	// Tell the console that everything is good.
    	logger.info("Econ has been initialized.");
    }
    
    public CommentedConfigurationNode rootNode() {
		return configNode;
    	
    }
    
    
}