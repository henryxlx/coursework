define(function (require, exports, module) {

    exports.run = function () {

        var $container = $('#announcement-table-container');
        var $table = $("#announcement-table");
        require('../../util/short-long-text')($table);
        require('../../util/batch-select')($container);
        require('../../util/batch-delete')($container);
        require('../../util/item-delete')($container);

    };

});