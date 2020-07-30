/*
* По нажатию на "Редактировать вопрос" открывается модальное окно
* с формой для редактирования вопроса, которая уже заполнена текущими
* существующими полями. В форму передается IDs темы и редактируемого вопроса.
*
* edit-question - кнопка, по которой должно открываться
* модальное окно для редактирования вопроса.
*
* this.dataset.idtheme - ID темы, который заложен в кнопку.
* this.dataset.idquestion - ID вопроса, который заложен в кнопку.
* */
$(document).on('click', '.edit-question', function () {

    let questionId = this.dataset.idquestion
    let themeId = this.dataset.idtheme
    let questionDto = getQuestionByQuestionIdAndThemeId(questionId, themeId)

    $('#edit-question-form :input[name~="question"]').val(questionDto.question)
    $('#edit-question-form :input[name~="answer"]').val(questionDto.answer)
    $('#edit-question-form :input[name~="weight"]').val(questionDto.weight)
    $('#edit-question-form').attr({'data-idquestion' : questionId, 'data-idtheme' : themeId})
    $('#edit-question-modal-window').modal('show')

})

/*
* После подтверждения пользователем (нажатия на "Сохранить")
* собираются данные формы в questionDto и отправляются на бэкенд.
* В случае успешной процедуры должны закрыться модальное окно,
* обновиться список вопросов темы и очиститься форма.
*
* this.dataset.idtheme - ID темы, который заложен в форму.
* this.dataset.idquestion - ID вопроса, который заложен в форму.
* */
$(document).on('submit', '#edit-question-form', function (event) {
    event.preventDefault()

    let questionId = this.dataset.idquestion
    let themeId = this.dataset.idtheme
    let questionDto = getQuestionByQuestionIdAndThemeId(questionId, themeId)
    questionDto.question = this.question.value
    questionDto.answer = this.answer.value
    questionDto.weight = this.weight.value

    $.ajax({
        url: `/api/admin/theme/${themeId}/question/${questionId}`,
        type: 'post',
        contentType: "application/json",
        data: JSON.stringify(questionDto),
        success: function() {
            $('#edit-question-modal-window').modal('hide')
            buildListQuestionsByThemeId(themeId)
            $('#edit-question-form')[0].reset()
        },
        error: function () {
            alert('При редактировании вопроса возникла непредвиденная ошибка')
        }
    })
})

function getQuestionByQuestionIdAndThemeId(questionId, themeId) {
    let QuestionDto
    $.ajax({
        url: `/api/admin/theme/${themeId}/question/${questionId}`,
        type: 'get',
        async: false,
        success: (data) => {
            QuestionDto = data
        },
        error: function () {
            alert('При получении данных вопроса возникла непредвиденная ошибка')
        }
    })
    return QuestionDto
}
