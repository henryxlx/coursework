package org.edunext.coursework.kernel.dao;

import org.edunext.coursework.kernel.AppUser;

/**
 * @author xulixin
 */
public interface AppUserDao {

    AppUser getByUsername(String username);
}
