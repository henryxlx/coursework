package org.edunext.coursework.kernel.service;

import com.jetwinner.util.EasyStringUtil;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.mvc.PageActionModelService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Map;

/**
 * @author xulixin
 */
@Service
public class CoursePageActionModelServiceImpl implements PageActionModelService {

    private final CourseService courseService;
    private final AppSettingService settingService;
    private final AppCategoryService categoryService;

    public CoursePageActionModelServiceImpl(CourseService courseService,
                                            AppSettingService settingService,
                                            AppCategoryService categoryService) {

        this.courseService = courseService;
        this.settingService = settingService;
        this.categoryService = categoryService;
    }

    @Override
    public void addMoreObject(Model model) {
        Map<String, Object> conditions = new ParamMap().add("status", "published").add("type", "normal").toMap();
        model.addAttribute("courses", courseService.searchCourses(conditions, "latest", 0, 12));

        Map<String, Object> courseSetting = settingService.get("course");
        if (EasyStringUtil.isNotBlank(courseSetting.get("live_course_enabled"))) {
//            model.addAttribute("recentLiveCourses", getRecentLiveCourses());
        }

//        model.addAttribute("categories", categoryService.findGroupRootCategories("course"));
        model.addAttribute("consultDisplay", Boolean.TRUE);

        Map<String, Object> coinSetting = settingService.get("coin");
        Object cashRate = 1;
        if (EasyStringUtil.isNotBlank(coinSetting.get("cash_rate"))) {
            cashRate = coinSetting.get("cash_rate");
        }
        model.addAttribute("cashRate", cashRate);
    }
}
