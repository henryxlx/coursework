<#if threads??>
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">最新讨论</h3>
        </div>
        <div id="scroll-threads" class="panel-body">

            <ul class="text-list">
                {% for thread in threads %}
                <li class="clearfix">

                    <span class="text-muted pull-right">{{ thread.createdTime | date
                        ('Y-m-d') }}</span>
                    <a
                            {% if setting("course.buy_fill_userinfo")|default(false) %}
                    href="#modal"
                    data-toggle="modal"
                    data-url="{{ path('course_buy', {id: course.id, targetType: 'course'}) }}"
                    {% else %}
                    href="{{ path('order_show', {targetId: course.id, targetType: 'course'}) }}"
                    {% endif %}
                    >{{ thread.title | plain_text
                    (15) }}</a>

                </li>
                {% endfor %}
            </ul>
        </div>
    </div>
</#if>