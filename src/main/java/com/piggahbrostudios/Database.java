package com.piggahbrostudios;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.sql.SqlService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;


public class Database {
	private SqlService sql;
	private Logger logger;
	private Object plugin;
    String uri;
	
	public Database(Object plugin, String path, Logger logger) {
		this.logger = logger;
		this.plugin = plugin;
		
		this.uri = "jdbc:h2:" + path + "\\econ\\econ.db";
		
		verifyDatabase();
	}
	
	public DataSource getDataSource(Object plugin, String jdbcUrl) throws SQLException {
	    if (sql == null) {
	        sql = Sponge.getServiceManager().provide(SqlService.class).get();
	    }
	    return sql.getDataSource(jdbcUrl);
	}
	
	// Later on
	public double getBalance(Player player) throws SQLException {
	    String sql = "SELECT * FROM balance WHERE id = `" + player.getIdentifier() + "`";
	    double balance;
	    
	    try (Connection conn = getDataSource(plugin, uri).getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet results = stmt.executeQuery()) {
	
	    	results.next();
	    	balance = results.getDouble(2);
	
	    } catch (SQLException e) {
		    sql = "INSERT INTO `balance` (id, amount) VALUES ('" + player.getIdentifier() + "', 100 )";
		    
		    try (Connection conn = getDataSource(plugin, uri).getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql);) {
			    	
		    	stmt.executeUpdate();
		    	stmt.close();
		    	
		    	balance = 100;
		
		    } catch (SQLException e1) {
		    	logger.error(e1.getMessage());
		    	balance = -1;
		    }
	    }
	    
	    return balance;
	
	}
	
	private void verifyDatabase() {
	    String sql = "CREATE TABLE IF NOT EXISTS `balance` (`id` varchar(50) NOT NULL, `amount` double(15) NOT NULL);";
	    
	    try (Connection conn = getDataSource(plugin, uri).getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);) {
	    	
	    	int rowsAltered = stmt.executeUpdate();
	    	stmt.close();
	    	
	    	logger.info("Database: " + rowsAltered + " rows altered.");
	    	
	    } catch (SQLException e) {
	    	logger.error(e.getMessage());
	    }
	}
}
