package org.edunext.coursework.web.controller;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.util.*;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.Paginator;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.ActionGraspException;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.service.AppSettingService;
import com.jetwinner.webfast.kernel.service.AppUserFieldService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import org.edunext.coursework.kernel.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xulixin
 */
@Controller("frontCourseController")
public class CourseController {

    private final AppUserService userService;
    private final UserAccessControlService userAccessControlService;
    private final AppSettingService settingService;
    private final AppCategoryService categoryService;
    private final AppTagService tagService;
    private final AppUserFieldService userFieldService;
    private final CourseService courseService;

    public CourseController(AppUserService userService,
                            UserAccessControlService userAccessControlService,
                            AppSettingService settingService,
                            AppCategoryService categoryService,
                            AppTagService tagService,
                            AppUserFieldService userFieldService,
                            CourseService courseService) {

        this.userService = userService;
        this.userAccessControlService = userAccessControlService;
        this.settingService = settingService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.userFieldService = userFieldService;
        this.courseService = courseService;
    }

    @RequestMapping("/course/explore")
    public String exploreAction(HttpServletRequest request, Model model) {
        return exploreAction("", request, model);
    }

    @RequestMapping("/course/explore/{category}")
    public String exploreAction(@PathVariable(value = "") String category, HttpServletRequest request, Model model) {
        Map<String, Object> categoryModel;
        if (EasyStringUtil.isNotBlank(category)) {
            categoryModel = EasyStringUtil.isNumeric(category) ?
                    categoryService.getCategory(ValueParser.toInteger(category)) :
                    categoryService.getCategoryByCode(category);

            if (MapUtil.isEmpty(categoryModel)) {
                throw new RuntimeGoingException("课程分类信息为空，请联系管理员！");
            }
        } else {
            categoryModel = new ParamMap().add("id", null).toMap();
        }


        String sort = ServletRequestUtils.getStringParameter(request, "sort", "latest");
        Map<String, Object> conditions = new ParamMap().add("status", "published")
                .add("type", "normal").add("categoryId", categoryModel.get("id"))
                .add("recommended", "recommendedSeq".equals(sort) ? 1 : null)
                .add("price", "free".equals(sort) ? "0.00" : null).toMap();

        Paginator paginator = new Paginator(request, courseService.searchCourseCount(conditions), 10);

        model.addAttribute("courses", courseService.searchCourses(conditions, sort,
                paginator.getOffsetCount(), paginator.getPerPageCount()));

        model.addAttribute("category", categoryModel);
        model.addAttribute("sort", sort);
        model.addAttribute("paginator", paginator);

        Map<String, Object> group = categoryService.getGroupByCode("course");
        if (MapUtil.isEmpty(group)) {
            model.addAttribute("categories", new ArrayList<>());
        } else {
            model.addAttribute("categories", categoryService.getCategoryTree(group.get("id")));
        }
        model.addAttribute("consultDisplay", Boolean.TRUE);

        return "/course/explore";
    }

    @RequestMapping("/course/archive")
    public String archivePage() {
        return "/course/archive";
    }

    @RequestMapping("/course/create")
    public String createAction(HttpServletRequest request, Model model) {
        AppUser user = AppUser.getCurrentUser(request);
        model.addAttribute("userProfile", userService.getUserProfile(user.getId()));

        String isLive = request.getParameter("flag");
        String type = "isLive".equals(isLive) ? "live" : "normal";

        if (userAccessControlService.hasRole("ROLE_TEACHER") == false) {
            throw new RuntimeGoingException("没有权限不能访问！");
        }

        if ("POST".equals(request.getMethod())) {
            Map<String, Object> course = ParamMap.toFormDataMap(request);
            course = courseService.createCourse(user, course);
            return String.format("redirect:/course/%s/manage", course.get("id"));
        }

        model.addAttribute("type", type);
        return "/course/create";
    }

    /**
     * 如果用户已购买了此课程，或者用户是该课程的教师，则显示课程的Dashboard界面。
     * 如果用户未购买该课程，那么显示课程的营销界面。
     */
    @RequestMapping("/course/{id:[\\d]+}")
    public String showAction(HttpServletRequest request, @PathVariable Integer id, Model model) {
        Map<String, Object> course = courseService.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在!");
        }

        Map<String, Object> courseSetting = settingService.get("course");
        int coursesPrice = ValueParser.parseInt(courseSetting.getOrDefault("coursesPrice", "0"));

        Map<String, Object> nextLiveLesson = null;

        long currentTime = System.currentTimeMillis();

        if ("live".equals(course.get("type"))) {
            Map<String, Object> conditions = new ParamMap().add("courseId", course.get("id"))
                    .add("startTimeGreaterThan", System.currentTimeMillis()).add("status", "published").toMap();
            nextLiveLesson = courseService.searchLessons(conditions, OrderBy.build(1).addAsc("startTime"), 0, 1)
                    .stream().findFirst().orElse(null);
        }

        String previewAs = request.getParameter("previewAs");

        AppUser user = AppUser.getCurrentUser(request);

        List<Map<String, Object>> items = courseService.getCourseItems(course.get("id"));
        Map<String, List<Object>> mediaMap = new HashMap<>();
        items.forEach(item -> {
            if (EasyStringUtil.isNotBlank(item.get("mediaId"))) {
                mediaMap.computeIfAbsent(String.valueOf(item.get("mediaId")),
                        k -> new ArrayList<>()).add(item.get("id"));
            }
        });

        Set<String> mediaIds = mediaMap.keySet();
//        List<Map<String, Object>> files = uploadFileService.findFilesByIds(mediaIds);

        Map<String, Object> member = user != null ?
                courseService.getCourseMember(ValueParser.toInteger(course.get("id")), user.getId()) : new HashMap<>(0);

        courseService.hitCourse(id);

        member = this.previewAsMember(previewAs, member, course, user);

/*
        $homeworkLessonIds =array();
        $exercisesLessonIds =array();
        if($this->isPluginInstalled("Homework")) {
            $lessons = $this->getCourseService()->getCourseLessons($course['id']);
            $lessonIds = ArrayToolkit::column($lessons, 'id');
            $homeworks = $this->getHomeworkService()->findHomeworksByCourseIdAndLessonIds($course['id'], $lessonIds);
            $exercises = $this->getExerciseService()->findExercisesByLessonIds($lessonIds);
            $homeworkLessonIds = ArrayToolkit::column($homeworks,'lessonId');
            $exercisesLessonIds = ArrayToolkit::column($exercises,'lessonId');
        }

        if($this->isPluginInstalled("Classroom") && empty($member)) {
            $classroomMembers = $this->getClassroomMembersByCourseId($id);
            foreach ($classroomMembers as $classroomMember) {
                if(in_array($classroomMember["role"], array("student")) && !$this->getCourseService()->isCourseStudent($id, $user["id"])) {
                    $member = $this->getCourseService()->becomeStudentByClassroomJoined($id, $user["id"], $classroomMember["classroomId"]);
                }
            }
        }

        $classrooms=array();
        $isLearnInClassrooms=array();
        if ($this->isPluginInstalled("Classroom")) {
            $classroomIds=ArrayToolkit::column($this->getClassroomService()->findClassroomsByCourseId($id),'classroomId');
            foreach ($classroomIds as $key => $value) {
                $classrooms[$value]=$this->getClassroomService()->getClassroom($value);

                if ($this->getClassroomService()->isClassroomStudent($value, $user->id) or $this->getClassroomService()->isClassroomTeacher($value, $user->id)) {

                    $isLearnInClassrooms[] = $classrooms[$value];

                }
            }
        }
*/

        if (MapUtil.isNotEmpty(member) && ObjectUtils.isEmpty(member.get("locked"))) {
            model.addAttribute("learnStatuses", courseService.getUserLearnLessonStatuses(user.getId(), course.get("id")));

            if (coursesPrice == 1) {
                course.put("price", 0);
                course.put("coinPrice", 0);
            }

            model.addAttribute("course", course);
            model.addAttribute("type", course.get("type"));
            model.addAttribute("member", member);
            model.addAttribute("items", items);
            model.addAttribute("currentTime", currentTime);
//            model.addAttribute("files", ArrayToolkit.index(files, "id"));
//            model.addAttribute("ChargeCoin", ChargeCoin);
//            model.addAttribute("homeworkLessonIds", homeworkLessonIds);
//            model.addAttribute("exercisesLessonIds", exercisesLessonIds);
//            model.addAttribute("isLearnInClassrooms", isLearnInClassrooms);
            return "/course/dashboard";
        }


        Map<String, Object> checkMemberLevelResult = MapUtil.newHashMap(0);
        Map<String, Object> courseMemberLevel = null;
        if (EasyStringUtil.isNotBlank(courseSetting.get("vip.enabled"))) {
//            courseMemberLevel = ValueParser.parseInt(course.get("vipLevelId")) > 0 ?
//                    levelService.getLevel(course.get("vipLevelId")) : null;
//            if (MapUtil.isNotEmpty(courseMemberLevel)) {
//                checkMemberLevelResult = vipService.checkUserInMemberLevel(user.getId(), courseMemberLevel.get("id"));
//            }
        }

        Map<String, Object> freeLesson = courseService.searchLessons(new ParamMap().add("courseId", id)
                        .add("type", "video").add("status", "published").add("free", 1).toMap(),
                OrderBy.build(1).add("createdTime"), 0, 1).stream().findFirst().orElse(null);

        if (coursesPrice == 1) {
            course.put("price", 0);
            course.put("coinPrice", 0);
        }

        model.addAttribute("course", course);
        model.addAttribute("member", member);
        model.addAttribute("freeLesson", freeLesson);
        model.addAttribute("courseMemberLevel", courseMemberLevel);
        model.addAttribute("checkMemberLevelResult", checkMemberLevelResult);
        model.addAttribute("groupedItems", this.groupCourseItems(items));
        model.addAttribute("hasFavorited", courseService.hasFavoritedCourse(course.get("id")));
        model.addAttribute("category", categoryService.getCategory(ValueParser.toInteger(course.get("categoryId"))));
        model.addAttribute("previewAs", previewAs);
        model.addAttribute("tags", tagService.findTagsByIds(EasyStringUtil.explode(",", course.get("tags"))));
        model.addAttribute("nextLiveLesson", nextLiveLesson);
        model.addAttribute("currentTime", currentTime);
//        model.addAttribute("courseReviews", reviewService.findCourseReviews(course.get("id"), "0", "1"));
        model.addAttribute("consultDisplay", Boolean.TRUE);
//        model.addAttribute("ChargeCoin", ChargeCoin);
//        model.addAttribute("classrooms", classrooms);

        return "/course/show";

    }

    private Map<String, Object> previewAsMember(String as, Map<String, Object> member,
                                                Map<String, Object> course, AppUser user) {

        if (user.getId() == null) {
            return MapUtil.newHashMap(0);
        }

        if (ArrayUtil.inArray(as, "member", "guest")) {
            if (userAccessControlService.hasRole("ROLE_ADMIN")) {
                member = new ParamMap()
                        .add("id", 0)
                        .add("courseId", course.get("id"))
                        .add("userId", user.getId())
                        .add("levelId", 0)
                        .add("learnedNum", 0)
                        .add("isLearned", 0)
                        .add("seq", 0)
                        .add("isVisible", 0)
                        .add("role", "teacher")
                        .add("locked", 0)
                        .add("createdTime", new Date())
                        .add("deadline", 0)
                        .toMap();
            }

            if (member == null || !"teacher".equals(member.get("role"))) {
                return member;
            }

            if ("member".equals(as)) {
                member.put("role", "student");
            } else {
                member = null;
            }
        }

        return member;
    }

    private List<Map<String, Object>> groupCourseItems(List<Map<String, Object>> items) {
        List<Map<String, Object>> grouped = ListUtil.newArrayList();

        List<Map<String, Object>> list = ListUtil.newArrayList();
        for (Map<String, Object> item : items) {
            if ("chapter".equals(item.get("itemType"))) {
                if (list.size() > 0) {
                    grouped.add(new ParamMap().add("type", "list").add("data", list).toMap());
                    list = ListUtil.newArrayList();
                }
                grouped.add(new ParamMap().add("type", "chapter").add("data", item).toMap());
            } else {
                list.add(item);
            }
        }

        if (list.size() > 0) {
            grouped.add(new ParamMap().add("type", "list").add("data", list).toMap());
        }

        return grouped;
    }

    @RequestMapping("/course/check/{id}/reviewing/list")
    public String myquizAction(@PathVariable Integer id, HttpServletRequest request, Model model) {

        Map<String, Object> course = courseService.tryManageCourse(AppUser.getCurrentUser(request), id);
        model.addAttribute("course", course);
        return "/course/manage/myquiz/list_course_test_paper.ftl";
    }

    @RequestMapping("/course/{id}/joinlearning")
    public String joinLearningAction(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Map<String, Object> course = this.courseService.getCourse(id);

        AppUser user = AppUser.getCurrentUser(request);
        if (user == null) {
            throw new RuntimeGoingException("请登录系统才能加入学习！");
        }

//        int remainingStudentNum = this.getRemainStudentNum(course);
        int remainingStudentNum = 100;

        String previewAs = request.getParameter("previewAs");

        Map<String, Object> member = this.courseService.getCourseMember(ValueParser.toInteger(course.get("id")), user.getId());
        member = this.previewAsMember(previewAs, member, course, user);

        model.addAttribute("courseSetting", this.settingService.get("course"));

        Map<String, Object> userInfo = this.userService.getUserProfile(user.getId());
        userInfo.put("approvalStatus", user.getApprovalStatus());

        List<Map<String, Object>> userFields = this.userFieldService.getAllFieldsOrderBySeqAndEnabled();

        userFields.forEach(e -> {
            checkFieldNamePutType(e, "textField", "text");
            checkFieldNamePutType(e, "varcharField", "varchar");
            checkFieldNamePutType(e, "intField", "int");
            checkFieldNamePutType(e, "floatField", "float");
            checkFieldNamePutType(e, "dateField", "date");

        });
        model.addAttribute("userFields", null);

        model.addAttribute("course", course);
        model.addAttribute("user", userInfo);
        model.addAttribute("member", MapUtil.isEmpty(member) ? null : member);
        model.addAttribute("noVerifiedMobile", EasyStringUtil.isBlank(user.getVerifiedMobile()));
        model.addAttribute("verifiedMobile", EasyStringUtil.isNotBlank(user.getVerifiedMobile()) ?
                user.getVerifiedMobile() : "");
        model.addAttribute("avatarAlert", alertJoinCourse(user));
        return "/course/join-modal";
    }

    private void checkFieldNamePutType(Map<String, Object> field, String fieldNameContain, String type) {
        String fieldName = String.valueOf(field.get("fieldName"));
        if (fieldName.contains(fieldNameContain)) {
            field.put("type", type);
        }
    }

    private boolean alertJoinCourse(AppUser user) {
        Map<String, Object> setting = this.settingService.get("user_partner");
        if (EasyStringUtil.isBlank(setting.get("avatar_alert"))) {
            return false;
        }

        if ("when_join_course".equals(setting.get("avatar_alert")) && EasyStringUtil.isBlank(user.getMediumAvatar())) {
            return true;
        }

        return false;
    }

    @RequestMapping("/course/{id}/join/modify_user_info")
    public String modifyUserInfoAction(@PathVariable Integer id, HttpServletRequest request) {
        AppUser user = AppUser.getCurrentUser(request);
        if (user == null || user.getId() == null) {
            throw new RuntimeGoingException("用户未登录，不能加入课程学习。");
        }

        Map<String, Object> course = this.courseService.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，不能加入课程学习。");
        }

        if (!"published".equals(course.get("status"))) {
            throw new RuntimeGoingException("不能加入未发布课程.");
        }

        try {
            this.courseService.becomeStudent(course, id, user.getId(), null);
        } catch (ActionGraspException e) {
            throw new RuntimeGoingException("加入课程学习发生错误：" + e.getMessage());
        }
        return "redirect:/course/" + id;
    }
}
