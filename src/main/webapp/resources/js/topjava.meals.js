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
        data: queryParams
    }, updateTable)
})

function disableFilter() {
    $("form#filter")[0].reset()
    refreshTable()
}

// $(document).ready(function () {
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
                // {
                //     "render": function() {return '<a><span class="fa fa-pencil"></span></a>' },
                //     "orderable": false
                // },
                // {
                //     "render": function(data, type, row, meta) {
                //         console.log(data)
                //         console.log(row)
                //         return `<a onclick=deleteRow(${row.id})><span class="fa fa-remove"></span></a>`
                //     },
                //     "orderable": false
                // }
                // {
                //     "data": "id",
                //     "render": function(data, type, row, meta) {
                //         console.log(data)
                //         console.log(row)
                //         return data
                //         //return `<span class="fa fa-remove"></span>`
                //     },
                //     "orderable": false
                // }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ],
            "createdRow": function (row, data, index) {
                $(row).attr("data-meal-excess", data.excess)
            }
        })
    );
});