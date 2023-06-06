package org.edunext.coursework.kernel.dao.impl;

import com.jetwinner.webfast.dao.support.FastJdbcDaoSupport;
import org.edunext.coursework.kernel.dao.CourseMemberDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xulixin
 */
@Repository
public class CourseMemberDaoImpl extends FastJdbcDaoSupport implements CourseMemberDao {

    @Override
    public void batchDeleteMember(List<Object> ids) {

    }

    @Override
    public Map<String, Object> getMemberByCourseIdAndUserId(Integer courseId, Object userId) {
        return null;
    }

    @Override
    public void deleteMember(Object id) {

    }

    @Override
    public void addMember(Map<String, Object> member) {

    }

    @Override
    public List<Map<String, Object>> findMembersByCourseIdAndRole(Integer courseId, String roleName,
                                                                  Integer start, Integer limit) {

        return new ArrayList<>(0);
    }
}
