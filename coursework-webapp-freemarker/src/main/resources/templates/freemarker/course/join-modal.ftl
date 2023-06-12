<#assign billable = false />
<#include '/bootstrap-modal-layout.ftl' />

<#macro blockTitle>加入学习</#macro>

<#macro blockBody>

    <#if billable && course.type=="live">
        <div class="alert alert-info">注意：请在30分钟内完成支付，否则订单将会过期失效</div>
    </#if>
    <form id="course-buy-form" class="form-horizontal" method="post"
          action="${ctx}/course/${course.id}/join/modify_user_info"
    >

        <div class="form-group">
            <div class="col-md-3 control-label">课程名称</div>
            <div class="col-md-9 controls">
                <span class="control-text text-muted">《${course.title}》</span>
            </div>
        </div>

        <#if billable>
            <div class="form-group">
                <div class="col-md-3 control-label">价格</div>
                <div class="col-md-9 controls money-text">
      	<span class="control-text">

          {% if setting('coin.coin_enabled') and setting('coin.price_type') == 'Coin' %}
          <strong class="money">{{ course.coinPrice }}</strong>
          <span class="text-muted">{{setting('coin.coin_name')}}</span>
          {% else %}
          <strong class="money">{{ course.price }}</strong>
      		<span class="text-muted">元</span>
          {% endif %}

      	</span>
                </div>
            </div>
        </#if>

        <#if billable>
            {% if not payments %}
            <div class="alert alert-info">{{ setting('payment.disabled_message') | default
                ('尚未开启支付模块，无法购买课程。') | raw }}
            </div>
            {% endif %}
        <#else>
            <div class="row">
                <div class="col-md-12">
                    <#if avatarAlert>
                        <div class="alert alert-warning">您还没有头像，设置以后才能加入学习<br/>拥有一个独有的头像，老师和同学们能更容易关注到你哦～～ <a
                                    href="{{ path('settings_avatar') }}" class="alert-link" target="_blank">点击设置</a>
                        </div>
                    <#else>
                        <div class="alert alert-info">此课程为免费课程，可直接加入学习。</div>
                    </#if>
                </div>
            </div>
        </#if>
        <input type="hidden" name="targetId" value="{{ course.id }}"/>
        <input type="hidden" name="payment" value="alipay"/>
        <input type="hidden" name="_csrf_token" value="${csrf_token('site')}">

        <#if (courseSetting.buy_fill_userinfo)??>
            {% if courseSetting.userinfoFields %}
            <br><br>
            <div class="nav nav-tabs">
                <div class="help-block">温情提示：如果您的资料有变更，请在此修改，以便更好的为您服务！</div>
            </div><br>

            {% for field in courseSetting.userinfoFields %}

            {% if field == 'truename' %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="truename">真实姓名 </label>
                <div class="col-md-6 controls">
                    {% if user.approvalStatus == 'approved' %}
                    <div class="control-text">{{ user.truename }} <small class="text-success">(已认证)</small></div>
                    {% elseif user.approvalStatus == 'approving' %}
                    <div class="control-text">{{ user.truename }} <small class="text-warning">(真实认证中)</small></div>
                    {% else %}
                    <input class="form-control" id="truename" type="text" name="truename" value="{{ user.truename }}">
                    {% endif %}
                </div>
            </div>
            {% endif %}
            {% if field == 'mobile' and noVerifiedMobile %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="mobile">手机</label>
                <div class="col-md-6 controls">
                    <input class="form-control" id="mobile" type="text" name="mobile" value="{{ user.mobile }}">
                </div>
            </div>
            {% elseif field == 'mobile' and (not noVerifiedMobile) %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="mobile">手机</label>
                <div class="col-md-6 controls">
                    <div class="control-text">{{ blur_phone_number(verifiedMobile) }}<small
                                class="text-success">(已绑定)</small></div>
                </div>
            </div>
            {% endif %}
            {% if field == 'qq' %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="qq">QQ</label>
                <div class="col-md-6 controls">
                    <input class="form-control" id="qq" type="text" name="qq" value="{{ user.qq }}">
                </div>
            </div>
            {% endif %}
            {% if field == 'company' %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="company">所在公司</label>
                <div class="col-md-6 controls">
                    <input class="form-control" id="company" type="text" name="company" value="{{ user.company }}">
                </div>
            </div>
            {% endif %}
            {% if field == 'job' %}
            <div class="form-group">
                <label class="col-md-3 control-label" for="job">职业</label>
                <div class="col-md-6 controls">
                    <input class="form-control" id="job" type="text" name="job" value="{{ user.job }}">
                </div>
            </div>
            {% endif %}
            {% if field == 'idcard' %}
            <div class="form-group">
                <label class="col-md-3 control-label required" for="idcard">身份证号</label>
                <div class="col-md-6 controls">
                    <input type="text" id="idcard" name="{{ field }}" class="form-control" value="{{user.idcard}}">
                </div>
            </div>
            {% endif %}
            {% if field == 'gender' %}
            <div class="form-group">
                <div class="controls">
                    <label class="col-md-3 control-label required" for="{{ field }}"
                           style="padding: 0 20px 0 0px;">性别</label>&nbsp;
                    <input type="radio" id="{{ field }}_0" name="{{ field }}" required="required" value="male" {% if
                           user.gender=="male"%}checked=true{% endif %}>
                    <label for="profile_gender_0" class="required" style="padding: 0 20px 0 0px;">男</label>
                    <input type="radio" id="{{ field }}_1" name="{{ field }}" required="required" value="female" {% if
                           user.gender=="female"%}checked=true{% endif %}>
                    <label for="profile_gender_1" class="required">女</label>
                </div>
            </div>
            {% endif %}
            {% if field == 'weixin' %}
            <div class="form-group">
                <label class="col-md-3 control-label required" for="weixin">微信</label>
                <div class="col-md-6 controls">
                    <input type="text" id="weixin" name="{{ field }}" class="form-control" value="{{user.weixin}}">
                </div>
            </div>
            {% endif %}
            {% if field == 'weibo' %}
            <div class="form-group">
                <label class="col-md-3 control-label required" for="weibo">微博</label>
                <div class="col-md-6 controls">
                    <input type="text" id="weibo" name="{{ field }}" class="form-control" value="{{user.weibo}}">
                </div>
            </div>
            {% endif %}
            {% for userField in userFields %}
            {% if field==userField.fieldName %}
            {% if userField.type=="text" %}
            <div class="form-group">
                <label for="{{userField.fieldName}}" class="col-md-3 control-label ">{{userField.title}}</label>
                <div class="col-md-6 controls">
                    <textarea id="{{userField.fieldName}}" name="{{userField.fieldName}}" class="form-control ">{{ user[userField.fieldName]|default('') }}</textarea>
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            {% elseif userField.type=="int" %}
            <div class="form-group">
                <label for="{{userField.fieldName}}" class=" col-md-3 control-label">{{userField.title}}</label>
                <div class="col-md-6 controls">
                    <input type="text" id="{{userField.fieldName}}" placeholder="最大值为9位整数"
                           name="{{userField.fieldName}}" class="{{userField.type}}  form-control"
                           data-widget-cid="widget-5" data-explain=""
                           value="{{ user[userField.fieldName]|default('') }}">
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            {% elseif userField.type=="float" %}
            <div class="form-group">
                <label for="{{userField.fieldName}}" class="col-md-3 control-label">{{userField.title}}</label>
                <div class=" col-md-6 controls">
                    <input type="text" id="{{userField.fieldName}}" placeholder="保留到2位小数" name="{{userField.fieldName}}"
                           class="{{userField.type}} form-control" data-widget-cid="widget-5" data-explain=""
                           value="{{ user[userField.fieldName]|default('') }}">
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            {% elseif userField.type=="date" %}
            <div class="form-group">
                <label for="{{userField.fieldName}}" class="col-md-3 control-label">{{userField.title}}</label>
                <div class=" col-md-6 controls">
                    <input type="text" id="{{userField.fieldName}}" name="{{userField.fieldName}}"
                           class="{{userField.type}}  form-control" data-widget-cid="widget-5" data-explain=""
                           value="{{ user[userField.fieldName]|default('') }}">
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            {% elseif userField.type=="varchar" %}
            <div class="form-group">
                <label for="{{userField.fieldName}}" class="col-md-3 control-label">{{userField.title}}</label>
                <div class="col-md-6  controls">
                    <input type="text" id="{{userField.fieldName}}" name="{{userField.fieldName}}" class="form-control"
                           data-widget-cid="widget-5" data-explain=""
                           value="{{ user[userField.fieldName]|default('') }}">
                    <div class="help-block" style="display:none;"></div>
                </div>
            </div>
            {% endif %}
            {% endif %}
            {% endfor %}
            {% endfor %}
            {% endif %}
        </#if>


    </form>
</#macro>

<#macro blockFooter>

    <#if member??>
        <span class="text-muted">预览模式无法加入学习</span>
        <button class="btn btn-primary" disabled="disabled">加入学习</button>
    <#else>
        <#if !avatarAlert>
            <button id="join-course-btn" class="btn btn-primary" data-loading-text="正在加入..." type="submit"
                    data-toggle="form-submit" data-target="#course-buy-form">加入学习
            </button>
        </#if>
    </#if>


    <script>
        app.load('course/buy-modal');
    </script>
</#macro>