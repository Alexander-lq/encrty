package com.ugdsec.encrty.service;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;

public interface SQLExecutor {
    String sql( String input);
}

