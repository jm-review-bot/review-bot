$('#search-form').on('submit', function (event) {
    event.preventDefault()
    let searchString = this.search.value

    if (searchString == '') { // Если строка поиска пустая, то просто обновляется список тем
        buildThemesAccordion(getAllThemesDto())
    } else {
        $.ajax({
            url: "/api/admin/search",
            type: "GET",
            data: {
                'search': searchString
            },
            contentType: "application/json",
            success: function (data) {
                showSearchResults(data, searchString)
            },
            error: function () {
                alert("При выполнении поиска возникла непредвиденная ошибка");
            }
        })
    }
})

function showSearchResults(results, searchString) {
    let themesResults = results.themes
    let questionsResults = results.questions
    buildThemesAccordion(themesResults)
    $('.collapse').attr({'class': 'collapse show'})
    for (let i = 0; i < themesResults.length; i++) {
        buildListQuestionsByThemeId(themesResults[i].id)
    }

    for (let i = 0; i < questionsResults.length; i++) {
        $(`*[data-id='${questionsResults[i].id}']`).attr({'class' : 'card bg-success'})
    }
}
