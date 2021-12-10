package org.edunext.coursework.freemarker;

import org.edunext.coursework.DataDictHolder;
import org.edunext.coursework.freemarker.tag.RenderControllerTag;
import org.edunext.coursework.kernel.BaseAppUser;
import org.edunext.coursework.kernel.dao.DataSourceConfig;
import org.edunext.coursework.kernel.service.UserAccessControlService;
import org.edunext.coursework.web.WebExtensionPack;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xulixin
 */
@Component
public class FreeMarkerViewReferenceInterceptor implements HandlerInterceptor {

    private static final String INSTALL_PATH = "/install";

    private DataSourceConfig dataSourceConfig;
    private RenderControllerTag renderControllerTag;
    private DataDictHolder dataDictHolder;
    private UserAccessControlService userAccessControlService;
    private ApplicationContext applicationContext;

    public FreeMarkerViewReferenceInterceptor(DataSourceConfig dataSourceConfig,
                                              RenderControllerTag renderControllerTag,
                                              DataDictHolder dataDictHolder,
                                              UserAccessControlService userAccessControlService,
                                              ApplicationContext applicationContext) {

        this.dataSourceConfig = dataSourceConfig;
        this.renderControllerTag = renderControllerTag;
        this.dataDictHolder = dataDictHolder;
        this.userAccessControlService = userAccessControlService;
        this.applicationContext = applicationContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (handler instanceof HandlerMethod) {
            if (this.dataSourceConfig.isDataSourceNotConfiguredProperly()) {
                response.sendRedirect(request.getContextPath() + "/install");
                return false;
            }
            if (userAccessControlService.isLoggedIn()) {
                request.setAttribute(BaseAppUser.MODEL_VAR_NAME, userAccessControlService.getCurrentUser());
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView mav)
            throws Exception {

        if (mav == null || mav.wasCleared()) {
            return;
        }

        mav.addObject("ctx", request.getContextPath());
        mav.addObject("dict", dataDictHolder.getDict());
        mav.addObject("userAcl", userAccessControlService);
        mav.addObject(WebExtensionPack.MODEL_VAR_NAME, new WebExtensionPack(request, applicationContext));
    }
}
