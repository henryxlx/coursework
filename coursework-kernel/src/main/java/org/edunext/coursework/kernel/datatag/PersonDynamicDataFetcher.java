package org.edunext.coursework.kernel.datatag;

import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.util.FastHashMap;
import com.jetwinner.util.ValueParser;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.datatag.BaseDataFetcher;
import com.jetwinner.webfast.kernel.datatag.annotations.FastDataTag;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.service.AppUserStatusService;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
@FastDataTag
public class PersonDynamicDataFetcher extends BaseDataFetcher {

    private final AppUserService userService;
    private final AppUserStatusService statusService;

    public PersonDynamicDataFetcher(ApplicationContext applicationContext) {
        super(applicationContext);
        this.userService = applicationContext.getBean(AppUserService.class);
        this.statusService = applicationContext.getBean(AppUserStatusService.class);
    }

    /**
     * 获取个人动态
     * <p>
     * count    必需
     *
     * @param arguments 参数
     * @return list 个人动态
     */
    @Override
    public List<Map<String, Object>> getData(Map<String, Object> arguments) {
        List<Map<String, Object>> personDynamics = this.statusService.searchStatuses(
                FastHashMap.build(1).add("private", 0).toMap(),
                OrderBy.build(1).addDesc("createdTime"),
                0,
                ValueParser.toInteger(arguments.get("count")));

        Set<Object> ownerIds = ArrayToolkit.column(personDynamics, "userId");
        Map<String, AppUser> owners = this.userService.findUsersByIds(ownerIds);

        personDynamics.forEach(e -> e.put("user", owners.get(String.valueOf(e.get("userId")))));

        return personDynamics;
    }

}
