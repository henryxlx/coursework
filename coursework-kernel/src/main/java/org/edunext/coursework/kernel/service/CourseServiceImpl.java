package org.edunext.coursework.kernel.service;

import com.jetwinner.security.UserAccessControlService;
import com.jetwinner.toolbag.ArrayToolkit;
import com.jetwinner.toolbag.HtmlToolkit;
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
import org.edunext.coursework.kernel.dao.*;
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
    private final CourseDraftDao courseDraftDao;
    private final CourseMemberDao memberDao;
    private final AppTagService tagService;
    private final AppLogService logService;

    public CourseServiceImpl(AppUserService userService,
                             FastAppConst appConst,
                             UserAccessControlService userAccessControlService,
                             AppCategoryService categoryService,
                             CourseDao courseDao, LessonDao lessonDao, CourseChapterDao chapterDao,
                             CourseDraftDao courseDraftDao, CourseMemberDao memberDao,
                             AppTagService tagService,
                             AppLogService logService) {

        this.userService = userService;
        this.appConst = appConst;
        this.userAccessControlService = userAccessControlService;
        this.categoryService = categoryService;
        this.courseDao = courseDao;
        this.lessonDao = lessonDao;
        this.chapterDao = chapterDao;
        this.courseDraftDao = courseDraftDao;
        this.memberDao = memberDao;
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
        course.put("about", EasyStringUtil.isNotBlank(fields.get("about")) ?
                EasyStringUtil.purifyHtml(String.valueOf(fields.get("about"))) : "");
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
        this.memberDao.addMember(member);

        course = getCourse(course.get("id"));
        logService.info(currentUser, "course", "create",
                String.format("创建课程《%s》(#%s)", course.get("title"), course.get("id")));

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

        CourseSerialize.unserialize(course);
        return course;
    }

    private boolean hasCourseManagerRole(Integer courseId, Integer userId) {
        if (userAccessControlService.hasRole("ROLE_ADMIN")) {
            return true;
        }

        Map<String, Object> member = memberDao.getMemberByCourseIdAndUserId(courseId, userId);
        if (member != null && "teacher".equals(member.get("role"))) {
            return true;
        }

        return false;
    }

    @Override
    public void updateCourse(AppUser currentUser, Integer id, Map<String, Object> fields) {
        Map<String, Object> course = courseDao.getCourse(id);
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("课程不存在，更新失败！");
        }

        this.filterCourseFields(fields);

        CourseSerialize.serialize(fields);

        int nums = courseDao.updateCourse(id, fields);
        if (nums > 0) {
            logService.info(currentUser, "course", "update",
                    String.format("更新课程《%s》(#%s)的信息", course.get("title"), course.get("id")), fields);
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
        List<Map<String, Object>> coursesUnserialized = this.courseDao.findCoursesByLikeTitle(title);
//        CourseSerialize.unserializes(coursesUnserialized);
//        return ArrayToolkit.index(coursesUnserialized, "id");
        return coursesUnserialized;
    }

    @Override
    public List<Map<String, Object>> findCoursesByIds(Set<Object> ids) {
        return this.courseDao.findCoursesByIds(ids);
    }

    @Override
    public List<Map<String, Object>> findLessonsByIds(Set<Object> ids) {
        List<Map<String, Object>> lessons = this.lessonDao.findLessonsByIds(ids);
//        LessonSerialize.unserializes(lessons);
//        return ArrayToolkit.index(lessons, "id");
        return lessons;
    }

    @Override
    public Integer searchMemberCount(Map<String, Object> conditions) {
        this.prepareCourseConditions(conditions);
        return this.memberDao.searchMemberCount(conditions);
    }

    @Override
    public Integer searchLearnTime(Map<String, Object> conditions) {
        return null;
    }

    @Override
    public Integer searchLessonCount(Map<String, Object> conditions) {
        return this.lessonDao.searchLessonCount(conditions);
    }

    public Map<String, Object> getCourse(Object id) {
        return courseDao.getCourse(id);
    }

    @Override
    public Map<String, Object> getCourse(Integer id) {
        Map<String, Object> course = courseDao.getCourse(id);
        CourseSerialize.unserialize(course);
        return course;
    }

    @Override
    public List<Map<String, Object>> searchLessons(Map<String, Object> conditions, OrderBy orderBy, int start, int limit) {
        return this.lessonDao.searchLessons(conditions, orderBy, start, limit);
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
    public Map<String, Object> getCourseMember(Integer courseId, Integer userId) {
        return this.memberDao.getMemberByCourseIdAndUserId(courseId, userId);
    }

    @Override
    public void hitCourse(Integer id) {
        Map<String, Object> checkCourse = this.getCourse(id);

        if (MapUtil.isEmpty(checkCourse)) {
            throw new RuntimeGoingException("课程不存在，操作失败。");
        }

        this.courseDao.waveCourse(id, "hitNum", +1);
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
                    String.format("推荐课程《%s》(#%s),序号为%s", course.get("title"), course.get("id"), number));
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
                    String.format("取消推荐课程《%s》(#%s)", course.get("title"), course.get("id")));
        }
    }

    @Override
    public void publishCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "published").toMap());
        logService.info(currentUser, "course", "publish",
                String.format("发布课程《%s》(#%s)", course.get("title"), course.get("id")));
    }

    @Override
    public void closeCourse(AppUser currentUser, Integer courseId) {
        Map<String, Object> course = this.tryManageCourse(currentUser, courseId);
        courseDao.updateCourse(courseId, new ParamMap().add("status", "closed").toMap());
        logService.info(currentUser, "course", "close",
                String.format("关闭课程《%s》(#%s)", course.get("title"), course.get("id")));
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
                String.format("删除课程《%s》(#%s)", course.get("title"), course.get("id")));

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
                String.format("更新课程《%s》(#%s)图片", course.get("title"), course.get("id")), fields.toMap());

        courseDao.updateCourse(ValueParser.toInteger(courseId), fields.toMap());
    }

    @Override
    public int findUserLeaningCourseCount(Integer userId, Map<String, Object> filters) {
        if (filters != null && EasyStringUtil.isNotBlank(filters.get("type"))) {
            return this.memberDao.findMemberCountByUserIdAndCourseTypeAndIsLearned(userId, "student",
                    String.valueOf(filters.get("type")), 0);
        }
        return this.memberDao.findMemberCountByUserIdAndRoleAndIsLearned(userId, "student", 0);
    }

    @Override
    public List<Map<String, Object>> findUserLeaningCourses(Integer userId, Integer start, Integer limit, Map<String, Object> filters) {
        List<Map<String, Object>> members;
        if (filters != null && EasyStringUtil.isNotBlank(filters.get("type"))) {
            members = this.memberDao.findMembersByUserIdAndCourseTypeAndIsLearned(userId, "student",
                    String.valueOf(filters.get("type")), "0", start, limit);
        } else {
            members = this.memberDao.findMembersByUserIdAndRoleAndIsLearned(userId, "student", "0", start, limit);
        }

        Map<String, Map<String, Object>> courses = ArrayToolkit.index(
                this.findCoursesByIds(ArrayToolkit.column(members, "courseId")), "id");

        List<Map<String, Object>> sortedCourses = new ArrayList<>();
        members.forEach(member -> {
            Map<String, Object> course = courses.get(member.get("courseId"));
            if (MapUtil.isEmpty(course)) {
                return;
            }
            course.put("memberIsLearned", 0);
            course.put("memberLearnedNum", member.get("learnedNum"));
            sortedCourses.add(course);
        });
        return sortedCourses;
    }

    @Override
    public int findUserTeachCourseCount(Integer userId, boolean onlyPublished) {
        return this.memberDao.findMemberCountByUserIdAndRole(userId, "teacher", onlyPublished);
    }

    @Override
    public List<Map<String, Object>> findUserTeachCourses(Integer userId, Integer start, Integer limit, boolean onlyPublished) {
        List<Map<String, Object>> members = this.memberDao.findMembersByUserIdAndRole(userId, "teacher", start, limit, onlyPublished);

        Map<String, Map<String, Object>> courses = ArrayToolkit.index(
                this.findCoursesByIds(ArrayToolkit.column(members, "courseId")), "id");

        /**
         * @todo 以下排序代码有共性，需要重构成一函数。
         */
        List<Map<String, Object>> sortedCourses = new ArrayList<>();
        members.forEach(member -> {
            Map<String, Object> course = courses.get(member.get("courseId"));
            if (MapUtil.isEmpty(course)) {
                return;
            }
            sortedCourses.add(course);
        });
        return sortedCourses;
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

    @Override
    public int deleteChapter(Integer courseId, Integer deletedChapterId) {
        Map<String, Object> deletedChapter = this.getChapter(courseId, deletedChapterId);
        if (MapUtil.isEmpty(deletedChapter)) {
            throw new RuntimeGoingException("章节(ID:" + deletedChapterId + ")不存在，删除失败！");
        }

        int nums = this.chapterDao.deleteChapter(deletedChapterId);

        Object prevChapterId = "0";
        int deletedChapterNumber = ValueParser.parseInt(deletedChapter.get("number"));
        List<Map<String, Object>> chapters = this.getCourseChapters(courseId);
        for (Map<String, Object> chapter : chapters) {
            if (ValueParser.parseInt(chapter.get("number")) < deletedChapterNumber) {
                prevChapterId = chapter.get("id");
            }
        }

        List<Map<String, Object>> lessons = this.lessonDao.findLessonsByChapterId(deletedChapterId);
        if (ListUtil.isNotEmpty(lessons)) {
            String updateChapterId = String.valueOf(prevChapterId);
            lessons.forEach(lesson -> lesson.put("chapterId", updateChapterId));
            this.lessonDao.batchUpdateLesson(lessons, "id", "chapterId");
        }
        return nums;
    }

    @Override
    public Map<String, Object> getChapter(Integer courseId, Integer chapterId) {
        Map<String, Object> chapter = this.chapterDao.getChapter(chapterId);
        return (MapUtil.isEmpty(chapter) || ValueParser.parseInt(chapter.get("courseId")) != courseId) ?
                MapUtil.newHashMap(0) : chapter;
    }

    @Override
    public Map<String, Object> updateChapter(Integer courseId, Integer chapterId, Map<String, Object> fields) {
        Map<String, Object> chapter = this.getChapter(courseId, chapterId);
        if (MapUtil.isEmpty(chapter)) {
            throw new RuntimeGoingException("章节#" + chapterId + "不存在！");
        }
        fields = ArrayToolkit.part(fields, "title");
        return this.chapterDao.updateChapter(chapterId, fields);
    }

    @Override
    public Map<String, Object> createLesson(Map<String, Object> lesson, AppUser currentUser) {
        ArrayToolkit.filter(lesson, new ParamMap()
                .add("courseId", 0)
                .add("chapterId", 0)
                .add("free", 0)
                .add("title", "")
                .add("summary", "")
                .add("tags", new String[0])
                .add("type", "text")
                .add("content", "")
                .add("media", new String[0])
                .add("mediaId", 0)
                .add("length", 0)
                .add("startTime", 0)
                .add("giveCredit", 0)
                .add("requireCredit", 0)
                .add("liveProvider", "none").toMap());

        if (!ArrayToolkit.required(lesson, "courseId", "title", "type")) {
            throw new RuntimeGoingException("参数缺失，创建课时失败！");
        }

        if (EasyStringUtil.isBlank(lesson.get("courseId"))) {
            throw new RuntimeGoingException("添加课时失败，课程ID为空。");
        }

        Map<String, Object> course = this.getCourse(lesson.get("courseId"));
        if (MapUtil.isEmpty(course)) {
            throw new RuntimeGoingException("添加课时失败，课程不存在。");
        }

        if (!ArrayUtil.inArray(lesson.get("type"), "text", "audio", "video", "testpaper", "live", "ppt", "document", "flash")) {
            throw new RuntimeGoingException("课时类型不正确，添加失败！");
        }

        this.fillLessonMediaFields(lesson);

        //课程内容的过滤 @todo
        // if(isset($lesson['content'])){
        // 	$lesson['content'] = $this->purifyHtml($lesson['content']);
        // }
        if (EasyStringUtil.isNotBlank(lesson.get("title"))) {
            lesson.put("title", HtmlToolkit.purifyHtml(lesson.get("title").toString()));
        }

        // 课程处于发布状态时，新增课时，课时默认的状态为“未发布"
        lesson.put("status", "published".equals(course.get("status")) ? "unpublished" : "published");
        lesson.put("free", EasyStringUtil.isBlank(lesson.get("free")) ? 0 : 1);
        lesson.put("number", this.getNextLessonNumber(lesson.get("courseId")));
        lesson.put("seq", this.getNextCourseItemSeq(lesson.get("courseId")));
        lesson.put("userId", currentUser.getId());
        lesson.put("createdTime", System.currentTimeMillis());

        Map<String, Object> lastChapter = this.chapterDao.getLastChapterByCourseId(lesson.get("courseId"));
        lesson.put("chapterId", MapUtil.isEmpty(lastChapter) ? 0 : lastChapter.get("id"));
        if ("live".equals(lesson.get("type"))) {
            lesson.put("endTime", ValueParser.parseInt(lesson.get("startTime")) +
                    ValueParser.parseInt(lesson.get("length")) * 60);
        }

        lesson = this.lessonDao.addLesson(lesson);

        // Increase the linked file usage count, if there's a linked file used by this lesson.
        if (EasyStringUtil.isNotBlank(lesson.get("mediaId"))) {
//            this.uploadFileService.increaseFileUsedCount(lesson.get("mediaId"));
        }

        this.updateCourseCounter(course.get("id"), new ParamMap()
                .add("lessonNum", this.lessonDao.getLessonCountByCourseId(course.get("id")))
                .add("giveCredit", this.lessonDao.sumLessonGiveCreditByCourseId(course.get("id"))).toMap());

        this.logService.info(currentUser, "course", "add_lesson",
                "添加课时《" + lesson.get("title") + "》(" + lesson.get("id") + ")", lesson);

//        this.dispatchEvent("course.lesson.create",
//                new ParamMap().add("courseId", lesson.get("courseId")).add("lessonId", lesson.get("id")).toMap());

        return lesson;
    }

    private void fillLessonMediaFields(Map<String, Object> lesson) {
        if (ArrayUtil.inArray(lesson.get("type"), "video", "audio", "ppt", "document", "flash")) {
            Map<String, Object> media = EasyStringUtil.isBlank(lesson.get("media")) ?
                    MapUtil.newHashMap(0) : JsonUtil.jsonDecodeMap(lesson.get("media"));
            if (MapUtil.isEmpty(media) || EasyStringUtil.isBlank(media.get("source")) ||
                    EasyStringUtil.isBlank(media.get("name"))) {

                throw new RuntimeGoingException("media参数不正确，添加课时失败！");
            }

            if ("self".equals(media.get("source"))) {
                media.put("id", media.get("id"));
                if (EasyStringUtil.isBlank(media.get("id"))) {
                    throw new RuntimeGoingException("media id参数不正确，添加/编辑课时失败！");
                }
//                Map<String, Object> file = this.uploadFileService.getFile(media.get("id"));
//                if (MapUtil.isEmpty(file)) {
//                    throw new RuntimeGoingException("文件不存在，添加/编辑课时失败！");
//                }

//                lesson.put("mediaId", file.get("id"));
//                lesson.put("mediaName", file.get("filename"));
                lesson.put("mediaSource", "self");
                lesson.put("mediaUri", "");
            } else {
                if (EasyStringUtil.isBlank(media.get("uri"))) {
                    throw new RuntimeGoingException("media uri参数不正确，添加/编辑课时失败！");
                }
                lesson.put("mediaId", 0);
                lesson.put("mediaName", media.get("name"));
                lesson.put("mediaSource", media.get("source"));
                lesson.put("mediaUri", media.get("uri"));
            }
        } else if ("testpaper".equals(lesson.get("type"))) {
            lesson.put("mediaId", lesson.get("mediaId"));
        } else if ("live".equals(lesson.get("type"))) {
        } else {
            lesson.put("mediaId", 0);
            lesson.put("mediaName", "");
            lesson.put("mediaSource", "");
            lesson.put("mediaUri", "");
        }

        lesson.remove("media");
    }

    @Override
    public void deleteCourseDrafts(Integer courseId, Integer lessonId, Integer userId) {
        this.courseDraftDao.deleteCourseDrafts(courseId, lessonId, userId);
    }

    @Override
    public Map<String, Object> findCourseDraft(Object courseId, Integer lessonId, Integer userId) {
        Map<String, Object> draft = this.courseDraftDao.findCourseDraft(courseId, lessonId, userId);
        if (MapUtil.isEmpty(draft) || (ValueParser.parseInt(draft.get("userId")) != userId)) {
            return MapUtil.newHashMap(0);
        }
        return draft;
    }

    @Override
    public void sortCourseItems(Object courseId, String[] itemIds) {
        List<Map<String, Object>> items = this.getCourseItems(courseId);
        if (itemIds.length != items.size()) {
            throw new RuntimeGoingException("itemdIds参数不正确");
        }

        Map<String, Map<String, Object>> itemMap = this.getCourseItemMap(items);
        for (String itemId : itemIds) {
            if (!itemMap.containsKey(itemId)) {
                throw new RuntimeGoingException("itemdIds参数不正确");
            }
        }

        int lessonNum = 0, chapterNum = 0, unitNum = 0, seq = 0;
        Map<String, Object> item, fields, currentChapter = new ParamMap().add("id", 0).toMap(),
                rootChapter = new ParamMap().add("id", 0).toMap();

        for (String itemId : itemIds) {
            seq++;
            String[] arr = itemId.split("-");
            String type = arr != null && arr.length > 0 ? arr[0] : "";
            switch (type) {
                case "lesson":
                    lessonNum++;
                    item = itemMap.get(itemId);
                    fields = new ParamMap().add("number", lessonNum).add("seq", seq).add("chapterId", currentChapter.get("id")).toMap();
                    if (ValueParser.parseInt(fields.get("number")) != ValueParser.parseInt(item.get("number")) ||
                            ValueParser.parseInt(fields.get("seq")) != ValueParser.parseInt(item.get("seq")) ||
                            ValueParser.parseInt(fields.get("chapterId")) != ValueParser.parseInt(item.get("chapterId"))) {

                        this.lessonDao.updateLesson(ValueParser.toInteger(item.get("id")), fields);
                    }
                    break;
                case "chapter":
                    item = currentChapter = itemMap.get(itemId);
                    if ("unit".equals(item.get("type"))) {
                        unitNum++;
                        fields = new ParamMap().add("number", unitNum).add("seq", seq).add("parentId", rootChapter.get("id")).toMap();
                    } else {
                        chapterNum++;
                        unitNum = 0;
                        rootChapter = item;
                        fields = new ParamMap().add("number", chapterNum).add("seq", seq).add("parentId", 0).toMap();
                    }
                    if (ValueParser.parseInt(fields.get("parentId")) != ValueParser.parseInt(item.get("parentId")) ||
                            ValueParser.parseInt(fields.get("number")) != ValueParser.parseInt(item.get("number")) ||
                            ValueParser.parseInt(fields.get("seq")) != ValueParser.parseInt(item.get("seq"))) {
                        this.chapterDao.updateChapter(ValueParser.toInteger(item.get("id")), fields);
                    }
                    break;
            }
        }
    }

    @Override
    public void setCourseTeachers(AppUser currentUser, Integer courseId, List<Map<String, Object>> teachers) {
        // 过滤数据
        List<Map<String, Object>> teacherMembers = new ArrayList<>();
        int index = 1;
        for (Map<String, Object> teacher : teachers) {
            if (EasyStringUtil.isBlank(teacher.get("id"))) {
                throw new RuntimeGoingException("教师ID不能为空，设置课程(#" + courseId + ")教师失败");
            }
            AppUser user = this.userService.getUser(teacher.get("id"));
            if (user == null) {
                throw new RuntimeGoingException("用户不存在或没有教师角色，设置课程(#" + courseId + ")教师失败");
            }

            teacherMembers.add(new ParamMap()
                    .add("courseId", courseId)
                    .add("userId", user.getId())
                    .add("role", "teacher")
                    .add("seq", index++)
                    .add("isVisible", EasyStringUtil.isBlank(teacher.get("isVisible")) ? 0 : 1)
                    .add("createdTime", System.currentTimeMillis()).toMap());
        }
        // 先清除所有的已存在的教师学员
        List<Map<String, Object>> existTeacherMembers = this.findCourseTeachers(courseId);
        List<Object> ids = existTeacherMembers.stream().map(member -> member.get("id")).collect(Collectors.toList());
        this.memberDao.batchDeleteMember(ids);

        // 逐个插入新的教师的学员数据
        List<Object> visibleTeacherIds = new ArrayList<>();
        for (Map<String, Object> member : teacherMembers) {
            // 存在学员信息，说明该用户先前是学生学员，则删除该学员信息。
            Map<String, Object> existMember = this.memberDao.getMemberByCourseIdAndUserId(courseId, member.get("userId"));
            if (MapUtil.isEmpty(existMember)) {
                this.memberDao.deleteMember(existMember.get("id"));
            }
            this.memberDao.addMember(member);
            if (EasyStringUtil.isNotBlank(member.get("isVisible"))) {
                visibleTeacherIds.add(member.get("userId"));
            }
        }

        Map<String, Object> dataMap = new HashMap<>(teacherMembers.size());
        teacherMembers.forEach(x -> dataMap.put(String.valueOf(x.get("id")), x));
        this.logService.info(currentUser, "course", "update_teacher",
                "更新课程#" + courseId + "的教师", dataMap);

        // 更新课程的teacherIds，该字段为课程可见教师的ID列表
        Map<String, Object> fields = new ParamMap().add("teacherIds", CourseSerialize.listToString(visibleTeacherIds)).toMap();
        this.courseDao.updateCourse(courseId, fields);

//        this.dispatchEvent("course.teacher.update", new ParamMap().add("courseId", courseId).toMap());
    }

    @Override
    public List<Map<String, Object>> findCourseTeachers(Integer courseId) {
        return this.memberDao.findMembersByCourseIdAndRole(courseId, "teacher", 0, MAX_TEACHER);
    }

    @Override
    public List<Map<String, Object>> searchMember(Map<String, Object> conditions, Integer start, Integer limit) {
        this.prepareCourseConditions(conditions);
        return this.memberDao.searchMember(conditions, start, limit);
    }

    @Override
    public List<Map<String, Object>> findCourseStudents(Integer courseId, Integer start, Integer limit) {
        return this.memberDao.findMembersByCourseIdAndRole(courseId, "student", start, limit);
    }

    @Override
    public void updateCourseCounter(Integer courseId, Map<String, Object> counter) {
        Map<String, Object> fields = ArrayToolkit.part(counter, "rating", "ratingNum", "lessonNum", "giveCredit");
        if (MapUtil.isEmpty(fields)) {
            throw new RuntimeGoingException("参数不正确，更新计数器失败！");
        }
        this.courseDao.updateCourse(courseId, fields);
    }

    @Override
    public Map<String, Object> getCourseLesson(Integer courseId, Integer lessonId) {
        Map<String, Object> lesson = this.lessonDao.getLesson(lessonId);
        if (MapUtil.isEmpty(lesson) || courseId.toString().equals(lesson.get("courseId"))) {
            return null;
        }
        return lesson;
    }

    private Map<String, Map<String, Object>> getCourseItemMap(List<Map<String, Object>> items) {
        Map<String, Map<String, Object>> mapForItems = new HashMap<>(items.size());
        for (Map<String, Object> item : items) {
            if ("lesson".equals(item.get("itemType"))) {
                mapForItems.put("lesson-" + item.get("id"), item);
            }
            if ("chapter".equals(item.get("itemType"))) {
                mapForItems.put("chapter-" + item.get("id"), item);
            }
        }
        return mapForItems;
    }

    public List<Map<String, Object>> getCourseChapters(Integer courseId) {
        return this.chapterDao.findChaptersByCourseId(courseId);
    }

    private void putNextUnitNumberAndParentId(Map<String, Object> chapter) {
        Object courseId = chapter.get("courseId");
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

    public Integer getNextLessonNumber(Object courseId) {
        return this.lessonDao.getLessonCountByCourseId(courseId) + 1;
    }

    public void updateCourseCounter(Object id, Map<String, Object> counter) {
        Map<String, Object> fields = ArrayToolkit.part(counter, "rating", "ratingNum", "lessonNum", "giveCredit");
        if (MapUtil.isEmpty(fields)) {
            throw new RuntimeGoingException("参数不正确，更新计数器失败！");
        }
        this.courseDao.updateCourse(ValueParser.toInteger(id), fields);
    }

}

class CourseSerialize {

    private static final String UNSERIALIZE_KEY = "unserialize";

    public static void objectToArray(Map<String, Object> map, String key) {
        Object obj = map.get(key);
        if (EasyStringUtil.isNotBlank(obj)) {
            String s = String.valueOf(obj);
            if (s.startsWith("|")) {
                s = s.substring(1);
            }
            map.put(key, s.split("\\|"));
        }
    }

    public static void unserialize(Map<String, Object> course) {
        if (course == null || course.isEmpty() || course.containsKey(UNSERIALIZE_KEY)) {
            return;
        }

        objectToArray(course, "goals");
        objectToArray(course, "audiences");
        objectToArray(course, "teacherIds");
        course.put(UNSERIALIZE_KEY, Boolean.TRUE);
    }

    public static String listToString(List<?> list) {
        List<String> strList = list.stream().map(String::valueOf).collect(Collectors.toList());
        return strList.stream().collect(Collectors.joining("|", "|", "|"));
    }

    public static void arrayToString(Map<String, Object> model, String key) {
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

    public static void serialize(Map<String, Object> course) {
        arrayToString(course, "goals");
        arrayToString(course, "audiences");
        arrayToString(course, "teacherIds");
    }
}
