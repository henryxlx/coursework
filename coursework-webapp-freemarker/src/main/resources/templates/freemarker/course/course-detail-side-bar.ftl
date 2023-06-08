<@renderController path='/classroom/classroomBlock' params={'courseId': course.id}/>


<@renderController path='/course/teachersBlock' params={'teacherIds': course.teacherIds}/>

<@renderController path='/courseThread/latestBlock' params={'course': course}/>

<@renderController path='/course/latestMembersBlock' params={'courseId': course.id}/>

<@renderController path='/courseAnnouncement/block' params={'course': course}/>

