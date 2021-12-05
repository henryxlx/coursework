package org.edunext.coursework;

import com.jetwinner.servlet.RequestContextPathUtil;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xulixin
 */
public class SimpleAppWorker {

    public static final String MODEL_VAR_NAME = "appWorker";

    private HttpServletRequest request;
    private ApplicationContext applicationContext;

    public SimpleAppWorker(HttpServletRequest request,
                           ApplicationContext applicationContext) {

        this.request = request;
        this.applicationContext = applicationContext;
    }


    public String csrfToken(String seed) {
        return "";
    }

    public String getSchemeAndHttpHost() {
        return RequestContextPathUtil.getSchemeAndHost(this.request);
    }

    public String getBasePath() {
        return RequestContextPathUtil.createBaseUrl(request);
    }

    private Boolean debug = Boolean.FALSE;

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
