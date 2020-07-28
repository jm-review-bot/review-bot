/*
* По нажатию на "Переместить вопрос ниже" производится запрос на бэкенд
* для выполнения соответсвующей операции. При успешном выполнении список
* вопросов текущей темы должен обновиться
*
* move-down-question - кнопка, по которой должен перемещаться
* вопрос на подну позицию ниже.
*
* this.dataset.idtheme - ID темы, который заложен в кнопку.
* this.dataset.idquestion - ID вопроса, который заложен в кнопку.
* */
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

/*
* По нажатию на "Переместить вопрос выше" производится запрос на бэкенд
* для выполнения соответсвующей операции. При успешном выполнении список
* вопросов текущей темы должен обновиться
*
* move-up-question - кнопка, по которой должен перемещаться
* вопрос на подну позицию ниже.
*
* this.dataset.idtheme - ID темы, который заложен в кнопку.
* this.dataset.idquestion - ID вопроса, который заложен в кнопку.
* */
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