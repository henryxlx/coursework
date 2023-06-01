package org.edunext.coursework.kernel.service;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.MapKitOnJava8;
import com.jetwinner.util.*;
import com.jetwinner.webfast.image.ImageUtil;
import com.jetwinner.webfast.kernel.AppUser;
import com.jetwinner.webfast.kernel.FastAppConst;
import com.jetwinner.webfast.kernel.dao.support.OrderBy;
import com.jetwinner.webfast.kernel.exception.RuntimeGoingException;
import com.jetwinner.webfast.kernel.model.AppPathInfo;
import com.jetwinner.webfast.kernel.service.AppLogService;
import com.jetwinner.webfast.kernel.service.AppUserService;
import com.jetwinner.webfast.kernel.typedef.ParamMap;
import com.jetwinner.webfast.module.bigapp.service.AppCategoryService;
import com.jetwinner.webfast.module.bigapp.service.AppTagService;
import org.edunext.coursework.kernel.dao.CourseChapterDao;
import org.edunext.coursework.kernel.dao.CourseDao;
import org.edunext.coursework.kernel.dao.LessonDao;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xulixin
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final AppUserService userService;
    private final FastAppConst appConst;
    private final UserAccessControlService userAccessControlService;
    private final AppCategoryService categoryService;
    private final CourseDao courseDao;
    private final LessonDao lessonDao;
    private final CourseChapterDao chapterDao;
    private final AppTagService tagService;
    private final AppLogService logService;

    public CourseServiceImpl(AppUserService userService,
                             FastAppConst appConst,
                             UserAccessControlService userAccessControlService,
                             AppCategoryService categoryService,
                             CourseDao courseDao, LessonDao lessonDao, CourseChapterDao chapterDao,
                             AppTagService tagService,
                             AppLogService logService) {

        this.userService = userService;
        this.appConst = appConst;
        this.userAccessControlService = userAccessControlService;
        this.categoryService = categoryService;
        this.courseDao = courseDao;
        this.lessonDao = lessonDao;
        this.chapterDao = chapterDao;
        this.tagService = tagService;
        this.logService = logService;
    }

    @Override
    public int searchCourseCount(Map<String, Object> conditions) {
        prepareCourseConditions(conditions);
        return courseDao.searchCourseCount(conditions);
    }

    private void prepareCourseConditions(Map<String, Object> conditions) {
        MapKitOnJava8.filter(conditions);

        /*if (Objects.nonNull(conditions.get("date"))) {
            Map<String, Map<String, Object>> dates =
            $dates = array(
                    'yesterday'=>array(
                    strtotime('yesterday'),
                    strtotime('today'),
                    ),
                    'today'=>array(
                    strtotime('today'),
                    strtotime('tomorrow'),
                    ),
                    'this_week' => array(
                    strtotime('Monday this week'),
                    strtotime('Monday next week'),
                    ),
                    'last_week' => array(
                    strtotime('Monday last week'),
                    strtotime('Monday this week'),
                    ),
                    'next_week' => array(
                    strtotime('Monday next week'),
                    strtotime('Monday next week', strtotime('Monday next week')),
                    ),
                    'this_month' => array(
                    strtotime('first day of this month midnight'),
                    strtotime('first day of next month midnight'),
                    ),
                    'last_month' => array(
                    strtotime('first day of last month midnight'),
                    strtotime('first day of this month midnight'),
                    ),
                    'next_month' => array(
                    strtotime('first day of next month midnight'),
                    strtotime('first day of next month midnight', strtotime('first day of next month midnight')),
                    ),
			);

            if (array_key_exists($conditions['date'], $dates)) {
                $conditions['startTimeGreaterThan'] = $dates[$conditions['date']][0];
                $conditions['startTimeLessThan'] = $dates[$conditions['date']][1];
                unset($conditions['date']);
            }
        }*/

        if (Objects.nonNull(conditions.get("creator"))) {
            AppUser user = userService.getUserByUsername(String.valueOf(conditions.get("creator")));
            conditions.put("userId", user != null ? user.getId() : -1);
            conditions.remove("creator");
        }

        if (Objects.nonNull(conditions.get("categoryId"))) {
            Set<Object> childrenIds = categoryService.findCategoryChildrenIds(conditions.get("categoryId"));
            childrenIds.add(conditions.get("categoryId"));
            conditions.put("categoryIds", childrenIds);
            conditions.remove("categoryId");
        }

        if (Objects.nonNull(conditions.get("username"))) {
            AppUser user = userService.getUserByUsername(String.valueOf(conditions.get("username")));
            conditions.put("userId", user != null ? user.getId() : -1);
            conditions.remove("username");
        }
    }

    @Override
    public List<Map<String, Object>> searchCourses(Map<String, Object> conditions, String sort, Integer start, Integer limit) {
        prepareCourseConditions(conditions);
        OrderBy orderBy = OrderBy.build(0);
        if ("popular".equals(sort)) {
            orderBy.addDesc("hitNum");
        } else if ("recommended".equals(sort)) {
            orderBy.addDesc("recommendedTime");
        } else if ("Rating".equals(sort)) {
            orderBy.addDesc("Rating");
        } else if ("hitNum".equals(sort)) {
            orderBy.addDesc("hitNum");
        } else if ("studentNum".equals(sort)) {
            orderBy.addDesc("studentNum");
        } else if ("recommendedSeq".equals(sort)) {
            orderBy.add("recommendedSeq");
        } else if ("createdTimeByAsc".equals(sort)) {
            orderBy.add("createdTime");
        } else {
            orderBy.addDesc("createdTime");
        }

        return courseDao.searchCourses(conditions, orderBy, start, limit);
    }

    @Override
    public Map<String, Object> createCourse(AppUser currentUser, Map<String, Object> fields) {
        if (!ArrayToolkit.required(fields, "title")) {
            throw new RuntimeGoingException("缺少必要字段，创建课程失败！");
        }

        Map<String, Object> course = ArrayToolkit.part(fields, "title", "type", "about", "categoryId",
                "tags", "price", "startTime", "endTime", "locationId", "address");

        course.put("status", "draft");
        course.put("about", EasyStringUtil.isNotBlank(fields.get("about")) ? fields.get("about") : ""); // purifyHtml($course["about"])
        course.put("tags", EasyStringUtil.isNotBlank(fields.get("tags")) ? fields.get("tags") : "");
        course.put("userId", currentUser.getId());
        course.put("createdTime", System.currentTimeMillis());
        course.put("teacherIds", fields.get("userId"));
        course = courseDao.addCourse(course);

        Map<String, Object> member = new ParamMap()
                .add("courseId", course.get("id"))
                .add("userId", course.get("userId"))
                .add("role", "teacher")
                .add("createdTime", System.currentTimeMillis()).toMap();
        //MemberDao.addMember(member);

        course = getCourse(course.get("id"));
        logService.info(currentUser, "course", "create",
                String.format("创建课程《%s》(#%d)", course.get("title"), course.get("id")));

        return course;
    }

    @Override
    public Map<String, Object> tryManageCourse(AppUser currentUser, Integer courseId) {
        if (!userAccessControlService.isLoggedIn()) {
            throw new RuntimeGoingException("未登录用户，无权操作！");
        }

        Map<String, Object> course = courseDao.getCourse(courseId);
        if (course == null || course.isEmpty()) {
            throw new RuntimeGoingException("课程不存在！");
        }

        if (!hasCourseManagerRole(courseId, currentUser.getId())) {
            throw new RuntimeGoingException("您不是课程的教师或管理员，无权操作！");
        }

        unserialize(course);
        return course;
    }

    private void objectToArray(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (EasyStringUtil.isNotBlank(obj)) {
            String s = String.valueOf(obj);
            if (s.startsWith("|")) {
                s = s.substring(1);
            }
            map.put(key, s.split("\\|"));
        }
    }

    public void unserialize(Map<String, Object> course) {
        if (course == null || course.isEmpty()) {
            return;
        }

        objectToArray(course, "goals");
        objectToArray(course, "audiences");
        objectToArray(course, "teacherIds");
    }

    private void arrayToString(Map<String, Object> model, String key) {
        Object objTarget = model.get(key);
        boolean needSerialize = true;
        if (ArrayUtil.isNotArray(objTarget)) {
            if (EasyStringUtil.isNotBlank(objTarget)) {
                objTarget = new Object[]{objTarget};
            } else {
                needSerialize = false;
            }
        }
        if (needSerialize) {
            model.put(key, '|' + EasyStringUtil.implode("|", objTarget) + '|');
        }
    }

    private void serializeCourse(Map<String, Object> course) {
        arrayToString(course, "goals");
        arrayToString(course, "audiences");
        arrayToString(course, "teacherIds");
    }

    private boolean hasCourseManagerRole(Integer courseId, Integer userId) {
        if (userAccessControlService.hasRole("ROLE_ADMIN")) {
            return true;
        }

//        Map<String, Object> member = memberDao.getMemberByCourseIdAndUserId(courseId, userId);
//        if (member != null && "teacher".equals(member.get("role"))) {
//            return true;
//        }

        return false;
    }

    @Override
    public void updateCourse(AppUser currentUser, Integer id, Map<String, Object> fields) {
        Map<String, Object> course = courseDao.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，更新失败！");
        }

        this.filterCourseFields(fields);

        this.serializeCourse(fields);

        int nums = courseDao.updateCourse(id, fields);
        if (nums > 0) {
            logService.info(currentUser, "course", "update",
                    String.format("更新课程《%s》(#%d)的信息", course.get("title"), course.get("id")), fields);
        }
    }

    private void filterCourseFields(Map<String, Object> fields) {
        if (EasyStringUtil.isBlank(fields.get("categoryId"))) {
            fields.remove("categoryId");
        }
        ArrayToolkit.filter(fields, new ParamMap()
                .add("title", "").add("subtitle", "")
                .add("about", "").add("expiryDay", 0)
                .add("serializeMode", "none")
                .add("categoryId", 0)
                .add("vipLevelId", 0)
                .add("goals", "")
                .add("audiences", "")
                .add("tags", "")
                .add("freeStartTime", 0)
                .add("freeEndTime", 0)
                .add("locationId", 0)
                .add("address", "")
                .add("maxStudentNum", 0).toMap());

        if (EasyStringUtil.isNotBlank(fields.get("about"))) {
            fields.put("about", EasyStringUtil.purifyHtml(String.valueOf(fields.get("about"))));
        }

        if (fields.get("tags") != null) {
            String[] tagNames = EasyStringUtil.explode(",", fields.get("tags"));
            List<Map<String, Object>> tagList = tagService.findTagsByNames(tagNames);
            fields.put("tags",
                    tagList.stream().map(x -> String.valueOf(x.get("id"))).collect(Collectors.joining(",")));
        }
    }

    @Override
    public List<Map<String, Object>> findCoursesByLikeTitle(Object title) {
        return new ArrayList<>(0);
    }

    @Override
    public List<Map<String, Object>> findCoursesByIds(Set<Object> ids) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findLessonsByIds(Set<Object> ids) {
        return null;
    }

    @Override
    public Integer searchMemberCount(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public Integer searchLearnTime(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public Integer searchLessonCount(Map<String, Object> conditions) {
        return null;
    }

    public Map<String, Object> getCourse(Object id) {
        return courseDao.getCourse(id);
    }

    @Override
    public Map<String, Object> getCourse(Integer id) {
        Map<String, Object> course = courseDao.getCourse(id);
        unserialize(course);
        return course;
    }

    @Override
    public List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, int start, int limit) {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getCourseItems(Object courseId) {
        List<Map<String, Object>> lessons = lessonDao.findLessonsByCourseId(courseId);

        List<Map<String, Object>> chapters = chapterDao.findChaptersByCourseId(courseId);

        Map<String, Map<String, Object>> mapForItems = new HashMap<>();
        for (Map<String, Object> lesson : lessons) {
            lesson.put("itemType", "lesson");
            mapForItems.put("lesson-" + lesson.get("id"), lesson);
        }

        for (Map<String, Object> chapter : chapters) {
            chapter.put("itemType", "chapter");
            mapForItems.put("chapter-" + chapter.get("id"), chapter);
        }

        List<Map<String, Object>> items = new ArrayList<>(mapForItems.values());
        Comparator<Map<String, Object>> seqComparator = Comparator.comparingInt(item -> ValueParser.parseInt(item.get("seq")));
        items.sort(seqComparator);
        return items;
    }

    @Override
    public Map<String, Object> getCourseMember(Object courseId, Integer userId) {
        return null;
    }

    @Override
    public void hitCourse(Integer id) {

    }

    @Override
    public Map<String, Object> getUserLearnLessonStatuses(Integer userId, Object courseId) {
        return null;
    }

    @Override
    public Object hasFavoritedCourse(Object id) {
        return null;
    }

    @Override
    public Map<String, Object> recommendCourse(AppUser currentUser, Integer courseId, String number) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        if (!EasyStringUtil.isNumeric(number)) {
            throw new RuntimeGoingException("推荐课程序号只能为数字！");
        }

        Map<String, Object> updateMap = new ParamMap()
                .add("recommended", 1)
                .add("recommendedSeq", number)
                .add("recommendedTime", System.currentTimeMillis()).toMap();
        int nums = courseDao.updateCourse(courseId, updateMap);

        if (nums > 0) {
            course.putAll(updateMap);
            logService.info(currentUser, "course", "recommend",
                    String.format("推荐课程《%s》(#%d),序号为%s", course.get("title"), course.get("id"), number));
        }
        return course;
    }

    @Override
    public void cancelRecommendCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        int nums = courseDao.updateCourse(courseId, new ParamMap()
                .add("recommended", 0).add("recommendedTime", 0).add("recommendedSeq", 0).toMap());

        if (nums > 0) {
            logService.info(currentUser, "course", "cancel_recommend",
                    String.format("取消推荐课程《%s》(#%d)", course.get("title"), course.get("id")));
        }
    }

    @Override
    public void publishCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "published").toMap());
        logService.info(currentUser, "course", "publish",
                String.format("发布课程《%s》(#%d)", course.get("title"), course.get("id")));
    }

    @Override
    public void closeCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "closed").toMap());
        logService.info(currentUser, "course", "close",
                String.format("关闭课程《%s》(#%d)", course.get("title"), course.get("id")));
    }

    @Override
    public boolean deleteCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryAdminCourse(currentUser, courseId);

        // Decrease the course lesson files usage counts, if there are files used by the course lessons.
//        List<Map<String, Object>> lessons = lessonDao.findLessonsByCourseId(courseId);

//        if (ListUtil.isNotEmpty(lessons)) {
//            Set<Object> fileIds = ArrayToolkit.column(lessons, "mediaId");
//            if (fileIds != null) {
//                uploadFileService.decreaseFileUsedCount(fileIds);
//            }
//        }

        // Delete all linked course materials (the UsedCount of each material file will also be decreaased.)
//        courseMaterialService.deleteMaterialsByCourseId(courseId);

        // Delete course related data
//        memberDao.deleteMembersByCourseId(courseId);
//        lessonDao.deleteLessonsByCourseId(courseId);
//        chapterDao.deleteChaptersByCourseId(courseId);

        courseDao.deleteCourse(courseId);

        if ("live".equals(course.get("type"))) {
//            courseLessonReplayDao.deleteLessonReplayByCourseId(courseId);
        }

        logService.info(currentUser, "course", "delete",
                String.format("删除课程《%s》(#%d)", course.get("title"), course.get("id")));

        return true;
    }

    @Override
    public void changeCoursePicture(AppUser currentUser, Object courseId, String filePath, Map<String, Object> options) {
        Map<String, Object> course = courseDao.getCourse(courseId);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，图标更新失败！");
        }

        ParamMap fields = new ParamMap().add("id", courseId);
        AppPathInfo pathInfo = new AppPathInfo(filePath, "!");

        String cropImageFilePath = String.format("%s/%s_crop.%s",
                pathInfo.getDirname(), pathInfo.getFilename(), pathInfo.getExtension());
        int x = (int) ValueParser.parseFloat(options.get("x"));
        int y = (int) ValueParser.parseFloat(options.get("y"));
        int width = (int) ValueParser.parseFloat(options.get("width"));
        int height = (int) ValueParser.parseFloat(options.get("height"));
        ImageUtil.cropPartImage(filePath, cropImageFilePath, pathInfo.getExtension(), x, y, width, height);

        Calendar now = Calendar.getInstance();
        String courseUploadPath = String.format("course/%d/%02d-%02d", now.get(Calendar.YEAR),
                now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        String userUploadRealDir = appConst.getUploadPublicDirectory() + "/" + courseUploadPath;
        if (FastDirectoryUtil.dirNotExists(userUploadRealDir)) {
            FastDirectoryUtil.makeDir(userUploadRealDir);
        }
        String largeFilePath = String.format("%s/%s_large.%s",
                userUploadRealDir, pathInfo.getFilename(), pathInfo.getExtension());
        try {
            ImageUtil.resizeImage(cropImageFilePath, largeFilePath, pathInfo.getExtension(), 480, 0.9f);
        } catch (Exception e) {
            throw new RuntimeGoingException("Resize large course image error: " + e.getMessage());
        }
        fields.add("largePicture", String.format("public://%s/%s_large.%s",
                courseUploadPath, pathInfo.getFilename(), pathInfo.getExtension()));


        String mediumFilePath = String.format("%s/%s_medium.%s",
                userUploadRealDir, pathInfo.getFilename(), pathInfo.getExtension());
        try {
            ImageUtil.resizeImage(cropImageFilePath, mediumFilePath, pathInfo.getExtension(), 304, 0.9f);
        } catch (Exception e) {
            throw new RuntimeGoingException("Resize medium course image error: " + e.getMessage());
        }
        fields.add("middlePicture", String.format("public://%s/%s_medium.%s",
                courseUploadPath, pathInfo.getFilename(), pathInfo.getExtension()));

        String smallFilePath = String.format("%s/%s_small.%s",
                userUploadRealDir, pathInfo.getFilename(), pathInfo.getExtension());
        try {
            ImageUtil.resizeImage(cropImageFilePath, smallFilePath, pathInfo.getExtension(), 96, 0.9f);
        } catch (Exception e) {
            throw new RuntimeGoingException("Resize small course image error: " + e.getMessage());
        }
        fields.add("smallPicture", String.format("public://%s/%s_small.%s",
                courseUploadPath, pathInfo.getFilename(), pathInfo.getExtension()));

        // 删除替换的旧的课程图片
        int courseCount = this.searchCourseCount(new ParamMap().add("smallPicture", course.get("smallPicture")).toMap());
        if (courseCount <= 1) {
        }
        logService.info(currentUser, "course", "update_picture",
                String.format("更新课程《%s》(#%d)图片", course.get("title"), course.get("id")), fields.toMap());

        courseDao.updateCourse(ValueParser.toInteger(courseId), fields.toMap());
    }

    @Override
    public int findUserLeaningCourseCount(Integer userId) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findUserLeaningCourses(Integer userId, Integer start, Integer limit) {
        return null;
    }

    @Override
    public int findUserTeachCourseCount(Integer userId, boolean onlyPublished) {
        return 0;
    }

    @Override
    public List<Map<String, Object>> findUserTeachCourses(Integer userId, Integer start, Integer limit, boolean onlyPublished) {
        return null;
    }

    @Override
    public Map<String, Object> createChapter(Map<String, Object> chapter) {
        if (!ArrayUtil.inArray(chapter.get("type"), "chapter", "unit")) {
            throw new RuntimeGoingException("章节类型不正确，添加失败！");
        }

        if ("unit".equals(chapter.get("type"))) {
            this.putNextUnitNumberAndParentId(chapter);
//            chapter.put("number", pid);
//            chapter.put("parentId", pid);
        } else {
            chapter.put("number", this.getNextChapterNumber(chapter.get("courseId")));
            chapter.put("parentId", 0);
        }

        chapter.put("seq", this.getNextCourseItemSeq(chapter.get("courseId")));
        chapter.put("createdTime", System.currentTimeMillis());
        return chapterDao.addChapter(chapter);
    }

    private void putNextUnitNumberAndParentId(Map<String, Object> chapter) {
        Object courseId = chapter.get("id");
        Map<String, Object> lastChapter = chapterDao.getLastChapterByCourseIdAndType(courseId, "chapter");
        int parentId = MapUtil.isEmpty(lastChapter) ? 0 : ValueParser.parseInt(lastChapter.get("id"));
        int unitNum = 1 + chapterDao.getChapterCountByCourseIdAndTypeAndParentId(courseId, "unit", parentId);
        chapter.put("number", unitNum);
        chapter.put("parentId", parentId);
    }

    private Object getNextChapterNumber(Object courseId) {
        int counter = chapterDao.getChapterCountByCourseIdAndType(courseId, "chapter");
        return counter + 1;
    }

    private Object getNextCourseItemSeq(Object courseId) {
        int chapterMaxSeq = chapterDao.getChapterMaxSeqByCourseId(courseId);
        int lessonMaxSeq = lessonDao.getLessonMaxSeqByCourseId(courseId);
        return (chapterMaxSeq > lessonMaxSeq ? chapterMaxSeq : lessonMaxSeq) + 1;
    }

    public Map<String, Object> tryAdminCourse(AppUser user, Integer courseId) {
        Map<String, Object> course = courseDao.getCourse(courseId);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在！");
        }

        if (user == null || user.getId() == null) {
            throw new RuntimeGoingException("未登录用户，无权操作！");
        }

        if (!user.hasAnyRole("ROLE_ADMIN", "ROLE_SUPER_ADMIN")) {
            throw new RuntimeGoingException("您不是管理员，无权操作！");
        }

        return course;
    }
}
