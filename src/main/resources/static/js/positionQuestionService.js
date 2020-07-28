// Изменение позиции вопроса на одну вниз
$(document).on('click', '.move-down-question', function () {
    let themeId = this.dataset.idtheme
    let questionId = this.dataset.idquestion
    $.ajax({
        url: `/admin/theme/${themeId}/question/${questionId}/position/down`,
        type: 'patch',
        success: function() {
            buildListQuestionsByThemeId(themeId)
        },
        error: function () {
            alert('При изменении позиции вопроса возникла непредвиденная ошибка')
        }
    })
})

// Изменение позиции вопроса на одну вверх
$(document).on('click', '.move-up-question', function () {
    let themeId = this.dataset.idtheme
    let questionId = this.dataset.idquestion
    $.ajax({
        url: `/admin/theme/${themeId}/question/${questionId}/position/up`,
        type: 'patch',
        success: function() {
            buildListQuestionsByThemeId(themeId)
        },
        error: function () {
            alert('При изменении позиции вопроса возникла непредвиденная ошибка')
        }
    })
})