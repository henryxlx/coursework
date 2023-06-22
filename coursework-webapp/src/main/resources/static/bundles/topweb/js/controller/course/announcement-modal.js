define(function (require, exports, module) {

    exports.run = function () {

        $('#modal').on('click', '#show-all-announcement', function () {
            $.get($('#show-all-announcement').data('url'), function (html) {
                $('#modal').html(html);
            });
        });

    };

});