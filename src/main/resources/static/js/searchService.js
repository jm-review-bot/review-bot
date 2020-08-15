$('#search-form').on('submit', function (event) {
    event.preventDefault()
    let searchString = this.search.value

    $.ajax({
        url: "/api/admin/search",
        type: "POST",
        contentType: "application/json",
        data: searchString,
        success: function (data) {

        },
        error: function () {
            alert("При выполнении поиска возникла непредвиденная ошибка");
        }
    })
})
