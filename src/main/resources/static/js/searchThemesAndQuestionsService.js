$('#search-form').on('submit', function (event) {
    event.preventDefault()
    let searchString = this.search.value
    if (searchString == '') { // Если строка поиска пустая, то просто обновляется список тем
        buildThemesAccordion(getAllThemesDto())
    } else {
        showSearchResultsForThemesAndQuestions(
            searchRequest('theme', searchString),
            searchRequest('question', searchString)
        )
    }
})

function searchRequest(entity, searchString) {
    let searchResults
    $.ajax({
        url: "/api/admin/search",
        type: "GET",
        data: {
            'entity' : entity,
            'searchString': searchString
        },
        contentType: "application/json",
        async: false,
        success: function (data) {
            searchResults = data
        },
        error: function () {
            alert("При выполнении поиска возникла непредвиденная ошибка")
        }
    })
    return searchResults
}

/* Если совпадения найдены по названиям темы, то пользователю отображаются только те темы, которые соответствует поисковому запросу.
* Если совпадения найдены и по названию темы, и по названию вопроса, то пользователю отображаются только те темы, которые соответствует
* поисковому запросу, и открываются все темы, в которых найдено совпадение по вопросам. Найденные вопросы подсвечиваются зелёным цветом */
function showSearchResultsForThemesAndQuestions(foundThemes, foundQuestions) {
    buildThemesAccordion(foundThemes)
    for (let i = 0; i < foundThemes.length; i++) {
        buildListQuestionsByThemeId(foundThemes[i].id)
    }
    for (let i = 0; i < foundQuestions.length; i++) {
        $(`[data-idquestion='${foundQuestions[i].id}']`)
            .closest('.card').attr({'class' : 'card bg-success'}) // Подкрашивается вопрос
            .closest('.collapse').attr({'class': 'collapse show'}) // Разворачивается список вопросов темы
    }
}
