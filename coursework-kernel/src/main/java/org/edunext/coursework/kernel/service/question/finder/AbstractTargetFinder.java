package org.edunext.coursework.kernel.service.question.finder;

import java.util.Map;
import java.util.Set;

/**
 * @author xulixin
 */
public abstract class AbstractTargetFinder {

    abstract public Map<String, Map<String, Object>> find(Set<Object> ids);
}
