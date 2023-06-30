<tr>
    <td>
        <a href="#modal" data-toggle="modal"
           data-url="${ctx}/my/favorite/question/${question.id}/preview">${fastLib.plainText(question.stem, 40)}</a>
        <div class="text-muted">
            <small>来自试卷《${paper.name}》</small>
            <small class="mhs">•</small>
            <small>收藏于 ${favoriteQuestion.createdTime?number_to_datetime?string('yyyy-MM-dd HH:mm:ss')}</small>
        </div>
    </td>
    <td>
        <button data-url="${ctx}/question/${question.id}/unfavorite?targetType=testpaper&targetId=${paper.id}"
                class="btn btn-default btn-sm pull-right unfavorite-btn"><span
                    class="glyphicon glyphicon-star-empty"></span> 取消收藏
        </button>
    </td>
</tr>