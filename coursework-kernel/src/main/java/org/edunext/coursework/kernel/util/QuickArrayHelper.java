package org.edunext.coursework.kernel.util;

import com.jetwinner.util.ValueParser;

import java.util.Set;

/**
 * @author xulixin
 */
public class QuickArrayHelper {

    public static int arraySum(Set<Object> set) {
        int sum = 0;
        for (Object obj : set) {
            sum = sum + ValueParser.parseInt(obj);
        }
        return sum;
    }
}
