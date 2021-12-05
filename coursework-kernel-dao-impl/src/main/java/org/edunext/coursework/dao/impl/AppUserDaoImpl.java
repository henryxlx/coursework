package org.edunext.coursework.dao.impl;

import org.edunext.coursework.kernel.AppUser;
import org.edunext.coursework.kernel.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * @author xulixin
 */
@Repository
public class AppUserDaoImpl implements AppUserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public AppUser getByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * FROM appuser WHERE username = ?",
                new BeanPropertyRowMapper<>(AppUser.class), username);
    }
}
