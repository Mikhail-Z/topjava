const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return formatDate(data)
                        }
                        return data
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess)
            }
        })
    );
});

let startDate = $("#startDate")
let endDate = $("#endDate")
startDate.datetimepicker({
    format: "Y-m-d",
    timepicker: false,
    onShow: function (param) {
        this.setOptions({
            maxDate: endDate.val() ? endDate.val() : false
        })
    }
})
endDate.datetimepicker({
    format: "Y-m-d",
    timepicker: false,
    onShow: function (param) {
        this.setOptions({
            minDate: startDate.val() ? startDate.val() : false
        })
    }
})

let startTime = $("#startTime")
let endTime = $("#endTime")
startTime.datetimepicker({
    format: "H:i",
    datepicker: false,
    onShow: function (param) {
        this.setOptions({
            maxTime: endTime.val() ? endTime.val() : false
        })
    }
})
endTime.datetimepicker({
    format: "H:i",
    datepicker: false,
    onShow: function (param) {
        this.setOptions({
            minTime: startTime.val() ? startTime.val() : false
        })
    }
})

jQuery("#dateTime").datetimepicker({
    format: "Y-m-d H:i",
})