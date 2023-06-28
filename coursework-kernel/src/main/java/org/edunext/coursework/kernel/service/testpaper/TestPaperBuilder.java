package org.edunext.coursework.kernel.service.testpaper;

import java.util.Map;

public interface TestPaperBuilder {

    TestPaperBuildResult build(Map<String, Object> testpaper, Map<String, Object> options);

    TestPaperBuildResult canBuild(Map<String, Object> options);
}
