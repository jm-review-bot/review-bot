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

/* Если совпадения найдены по названиям темы, то пользователю отображаются только те темы, которые соответствует поисковому запросу.
* Если совпадения найдены и по названию темы, и по названию вопроса, то пользователю отображаются только те темы, которые соответствует
* поисковому запросу, и открываются все темы, в которых найдено совпадение по вопросам. Найденные вопросы подсвечиваются зелёным цветом */
function showSearchResults(results, searchString) {
    let themesResults = results.themes
    let questionsResults = results.questions
    buildThemesAccordion(themesResults)
    for (let i = 0; i < themesResults.length; i++) {
        buildListQuestionsByThemeId(themesResults[i].id)
    }
    for (let i = 0; i < questionsResults.length; i++) {
        $(`[data-id='${questionsResults[i].id}']`).attr({'class' : 'card bg-success'})
        $(`[data-id='${questionsResults[i].id}']`).closest('.collapse').attr({'class': 'collapse show'})
    }
}
