package lab4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import lab4.utils.Security;

import java.sql.Connection;

import javax.sql.DataSource;

@Component
public class InitializeData {

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
		Connection conn = null;
    	try {
    		/*
    		Security security = Security.getSecurity();
    		conn = dataSource.getConnection();
    		
    		conn.createStatement().execute("drop table if exists calcresult");    		
    		conn.createStatement().execute("create table if not exists calcresult (id serial, login character varying(50), x double precision default 0, y double precision default 0, r double precision default 0, result boolean default false, constraint calcresult_pkey primary key (id))");
    		conn.createStatement().execute("drop table if exists users_lab4");    		
    		conn.createStatement().execute("create table if not exists users_lab4 (login character varying(50), password character varying(500), token character varying(500), constraint users_lab4_pkey primary key (login))");    		
    		conn.createStatement().execute("insert into users_lab4 select 'user1', '" + security.getPasswordHash("1") + "' on conflict on constraint users_lab4_pkey do nothing");    		
    		conn.createStatement().execute("insert into users_lab4 select 'user2', '" + security.getPasswordHash("2") + "' on conflict on constraint users_lab4_pkey do nothing");    		
    		conn.createStatement().execute("insert into users_lab4 select 'user3', '" + security.getPasswordHash("3") + "' on conflict on constraint users_lab4_pkey do nothing");
    		*/    		
    	} catch (Exception exc) {
    		throw new RuntimeException(exc);
		} finally {
			if (conn != null) try {
				conn.close();
			} catch (Exception exc2) {}
		}
//            ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(false, false, "UTF-8", new ClassPathResource("create_users.sql"));
//        resourceDatabasePopulator.execute(dataSource);
    }
}