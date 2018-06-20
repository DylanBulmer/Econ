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
	private String path;
	private Logger logger;
	
	public Database(String path, Logger logger) {
		this.path = path;
		this.logger = logger;
	}
	
	public DataSource getDataSource(String jdbcUrl) throws SQLException {
	    if (sql == null) {
	        sql = Sponge.getServiceManager().provide(SqlService.class).get();
	    }
	    return sql.getDataSource(jdbcUrl);
	}
	
	// Later on
	public double getBalance(Player player) throws SQLException {
	    String uri = "jdbc:h2:" + path + "\\econ\\econ.db";
	    String sql = "SELECT * FROM balance WHERE id = " + player.getIdentifier();
	
	    try (Connection conn = getDataSource(uri).getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet results = stmt.executeQuery()) {
	
	    	results.next();
	    	return results.getDouble(2);
	
	    } catch (SQLException e) {
	    	logger.error(e.getMessage());
			return -1;
	    }
	
	}
}
