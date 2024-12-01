const userAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

$("form#filter").submit(function (e) {
    e.preventDefault()
    var form = this
    var filterUrlSegment = $(form).attr('action')
    var queryParams = $(form).serialize()
    $.ajax({
        type: "GET",
        url: ctx.ajaxUrl + filterUrlSegment,
        data: queryParams,
        success: updateTable
    })
})

function disableFilter() {
    $("form#filter")[0].reset()
    refreshTable()
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, index) {
                $(row).attr("data-meal-excess", data.excess)
            }
        })
    );
});