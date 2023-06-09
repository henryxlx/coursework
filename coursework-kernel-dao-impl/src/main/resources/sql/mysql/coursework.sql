-- ------------------------------------------------------------
-- Table structure for `cw_course`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course`;
CREATE TABLE `cw_course` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `title` varchar(1024) NOT NULL COMMENT '课程标题',
  `subtitle` varchar(1024) NOT NULL DEFAULT '' COMMENT '课程副标题',
  `status` enum('draft','published','closed') NOT NULL DEFAULT 'draft' COMMENT '课程状态',
  `type` varchar(255) NOT NULL DEFAULT 'normal' COMMENT '课程类型',
  `maxStudentNum` int(11) NOT NULL DEFAULT '0' COMMENT '直播课程最大学员数上线',
  `price` float(10,2) NOT NULL DEFAULT '0.00' COMMENT '课程价格',
  `originPrice` FLOAT(10,2) NOT NULL DEFAULT  '0.00' COMMENT '课程人民币原价',
  `coinPrice` FLOAT(10,2) NOT NULL DEFAULT 0.00,
  `originCoinPrice` FLOAT(10,2) NOT NULL DEFAULT  0 COMMENT '课程虚拟币原价',
  `expiryDay` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课程过期天数',
  `showStudentNumType` enum('opened','closed') NOT NULL DEFAULT 'opened' COMMENT '学员数显示模式',
  `serializeMode` enum('none','serialize','finished') NOT NULL DEFAULT 'none' COMMENT '连载模式',
  `income` float(10,2) NOT NULL DEFAULT '0.00' COMMENT '课程销售总收入',
  `lessonNum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课时数',
  `giveCredit` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学完课程所有课时，可获得的总学分',
  `rating` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排行分数',
  `ratingNum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '投票人数',
  `vipLevelId` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '可以免费看的，会员等级',
  `categoryId` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '分类ID',
  `tags` text COMMENT '标签IDs',
  `smallPicture` varchar(255) NOT NULL DEFAULT '' COMMENT '小图',
  `middlePicture` varchar(255) NOT NULL DEFAULT '' COMMENT '中图',
  `largePicture` varchar(255) NOT NULL DEFAULT '' COMMENT '大图',
  `about` text COMMENT '简介',
  `teacherIds` text COMMENT '显示的课程教师IDs',
  `goals` text COMMENT '课程目标',
  `audiences` text COMMENT '适合人群',
  `recommended` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否为推荐课程',
  `recommendedSeq` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '推荐序号',
  `recommendedTime` bigint unsigned NOT NULL DEFAULT '0' COMMENT '推荐时间',
  `locationId` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '上课地区ID',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '上课地区地址',
  `studentNum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员数',
  `hitNum` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '查看次数',
  `userId` int(10) unsigned NOT NULL COMMENT '课程发布人ID',
  `discountId` INT UNSIGNED NOT NULL DEFAULT  '0' COMMENT  '折扣活动ID',
  `discount` FLOAT( 10, 2 ) NOT NULL DEFAULT  '10' COMMENT  '折扣',
  `deadlineNotify` enum('active','none') NOT NULL DEFAULT 'none' COMMENT '开启有效期通知',
  `daysOfNotifyBeforeDeadline` INT(10) NOT NULL DEFAULT '0',
  `useInClassroom` ENUM('single','more') NOT NULL DEFAULT 'single' COMMENT '课程能否用于多个班级' , 
  `singleBuy` INT(10) UNSIGNED NOT NULL DEFAULT '1' COMMENT '加入班级后课程能否单独购买' ,
  `createdTime` bigint unsigned NOT NULL COMMENT '课程创建时间',
  `freeStartTime` bigint NOT NULL DEFAULT '0',
  `freeEndTime` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------
-- Table structure for `cw_course_chapter`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course_chapter`;
CREATE TABLE `cw_course_chapter`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '课程章节ID',
    `courseId`    int(10) unsigned NOT NULL COMMENT '章节所属课程ID',
    `type`        enum('chapter','unit') NOT NULL DEFAULT 'chapter' COMMENT '章节类型：chapter为章节，unit为单元。',
    `parentId`    int(10) unsigned NOT NULL DEFAULT '0' COMMENT 'parentId大于０时为单元',
    `number`      int(10) unsigned NOT NULL COMMENT '章节编号',
    `seq`         int(10) unsigned NOT NULL COMMENT '章节序号',
    `title`       varchar(255) NOT NULL COMMENT '章节名称',
    `createdTime` bigint unsigned NOT NULL COMMENT '章节创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------
-- Table structure for `cw_course_lesson`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course_lesson`;
CREATE TABLE `cw_course_lesson`
(
    `id`            int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '课时ID',
    `courseId`      int(10) unsigned NOT NULL COMMENT '课时所属课程ID',
    `chapterId`     int(10) unsigned NOT NULL DEFAULT '0' COMMENT '课时所属章节ID',
    `number`        int(10) unsigned NOT NULL COMMENT '课时编号',
    `seq`           int(10) unsigned NOT NULL COMMENT '课时在课程中的序号',
    `free`          tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否为免费课时',
    `status`        enum('unpublished','published') NOT NULL DEFAULT 'published' COMMENT '课时状态',
    `title`         varchar(255) NOT NULL COMMENT '课时标题',
    `summary`       text COMMENT '课时摘要',
    `tags`          text COMMENT '课时标签',
    `type`          varchar(64)  NOT NULL DEFAULT 'text' COMMENT '课时类型',
    `content`       text COMMENT '课时正文',
    `giveCredit`    int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学完课时获得的学分',
    `requireCredit` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学习课时前，需达到的学分',
    `mediaId`       int(10) unsigned NOT NULL DEFAULT '0' COMMENT '媒体文件ID',
    `mediaSource`   varchar(32)  NOT NULL DEFAULT '' COMMENT '媒体文件来源(self:本站上传,youku:优酷)',
    `mediaName`     varchar(255) NOT NULL DEFAULT '' COMMENT '媒体文件名称',
    `mediaUri`      text COMMENT '媒体文件资源名',
    `length`        int(11) unsigned DEFAULT NULL COMMENT '时长',
    `materialNum`   int(10) unsigned NOT NULL DEFAULT '0' COMMENT '上传的资料数量',
    `quizNum`       int(10) unsigned NOT NULL DEFAULT '0' COMMENT '测验题目数量',
    `learnedNum`    int(10) unsigned NOT NULL DEFAULT '0' COMMENT '已学的学员数',
    `viewedNum`     int(10) unsigned NOT NULL DEFAULT '0' COMMENT '查看数',
    `startTime`     bigint unsigned NOT NULL DEFAULT '0' COMMENT '直播课时开始时间',
    `endTime`       bigint unsigned NOT NULL DEFAULT '0' COMMENT '直播课时结束时间',
    `memberNum`     int(10) unsigned NOT NULL DEFAULT '0' COMMENT '直播课时加入人数',
    `replayStatus`  enum('ungenerated','generating','generated') NOT NULL DEFAULT 'ungenerated',
    `liveProvider`  int(10) unsigned NOT NULL DEFAULT '0',
    `userId`        int(10) unsigned NOT NULL COMMENT '发布人ID',
    `createdTime`   bigint unsigned NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ------------------------------------------------------------
-- Table structure for `cw_course_draft`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course_draft`;
CREATE TABLE `cw_course_draft`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT,
    `title`       varchar(255) NOT NULL COMMENT '标题',
    `summary`     text COMMENT '摘要',
    `courseId`    int(10) unsigned NOT NULL COMMENT '课程ID',
    `content`     text COMMENT '内容',
    `userId`      int(10) unsigned NOT NULL COMMENT '用户ID',
    `lessonId`    int(10) unsigned NOT NULL COMMENT '课时ID',
    `createdTime` bigint unsigned NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

-- ------------------------------------------------------------
-- Table structure for `cw_course_member`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course_member`;
CREATE TABLE `cw_course_member`
(
    `id`                 int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '课程学员记录ID',
    `courseId`           int(10) unsigned NOT NULL COMMENT '课程ID',
    `classroomId`        INT(10) NOT NULL DEFAULT '0' COMMENT '班级ID',
    `joinedType`         ENUM('course','classroom') NOT NULL DEFAULT 'course' COMMENT '购买班级或者课程加入学习',
    `userId`             int(10) unsigned NOT NULL COMMENT '学员ID',
    `orderId`            int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员购买课程时的订单ID',
    `deadline`           int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学习最后期限',
    `levelId`            int(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户以会员的方式加入课程学员时的会员ID',
    `learnedNum`         int(10) unsigned NOT NULL DEFAULT '0' COMMENT '已学课时数',
    `credit`             int(10) unsigned NOT NULL DEFAULT '0' COMMENT '学员已获得的学分',
    `noteNum`            int(10) unsigned NOT NULL DEFAULT '0' COMMENT '笔记数目',
    `noteLastUpdateTime` bigint unsigned NOT NULL DEFAULT '0' COMMENT '最新的笔记更新时间',
    `isLearned`          tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否已学完',
    `seq`                int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序序号',
    `remark`             varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
    `isVisible`          tinyint(2) NOT NULL DEFAULT '1' COMMENT '可见与否，默认为可见',
    `role`               enum('student','teacher') NOT NULL DEFAULT 'student' COMMENT '课程会员角色',
    `locked`             tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '学员是否被锁定',
    `deadlineNotified`   int(10) NOT NULL DEFAULT '0' COMMENT '有效期通知',
    `createdTime`        bigint unsigned NOT NULL COMMENT '学员加入课程时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `courseId` (`courseId`,`userId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
-- ------------------------------------------------------------
-- Table structure for `cw_course_review`
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `cw_course_review`;
CREATE TABLE `cw_course_review`
(
    `id`          int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '课程评价ID',
    `userId`      int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评价人ID',
    `courseId`    int(10) unsigned NOT NULL DEFAULT '0' COMMENT '被评价的课程ID',
    `title`       varchar(255) NOT NULL DEFAULT '' COMMENT '评价标题',
    `content`     text         NOT NULL COMMENT '评论内容',
    `rating`      int(10) unsigned NOT NULL DEFAULT '0' COMMENT '评分',
    `private`     tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否隐藏',
    `createdTime` bigint unsigned NOT NULL COMMENT '评价创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;