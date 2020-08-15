$('#search-form').on('submit', function (event) {
    event.preventDefault()
    let searchString = this.search.value

    $.ajax({
        url: "/api/admin/search",
        type: "GET",
        data: {
            'search' : searchString
        },
        contentType: "application/json",
        success: function (data) {
            showSearchResults(data)
        },
        error: function () {
            alert("При выполнении поиска возникла непредвиденная ошибка");
        }
    })
})

function showSearchResults(results) {
    let themesResults = results.themes
    let questionsResults = results.questions

    buildThemesAccordion(themesResults)
}
