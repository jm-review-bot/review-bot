/*По нажатию кнопки "Редактировать вопрос" открывается модальное окно с формой.
* Данные формы заполняются текущими полями выбранного вопроса */
$(document).on('click', '.edit-question', function () {

    // На основании данных, заложенных в кнопку, из БД извлекается QuestionDto
    let questionId = this.dataset.idquestion
    let themeId = this.dataset.idtheme
    let questionDto = getQuestionByQuestionIdAndThemeId(questionId, themeId)

    // Форма для редактирования заполняется текущими полями вопроса, затем открывается модальное окно с этой формой
    $('#edit-question-form :input[name~="question"]').val(questionDto.question)
    $('#edit-question-form :input[name~="answer"]').val(questionDto.answer)
    $('#edit-question-form :input[name~="weight"]').val(questionDto.weight)
    $('#edit-question-form').attr({'data-idquestion' : questionId, 'data-idtheme' : themeId})
    $('#edit-question-modal-window').modal('show')

})

/* По нажатию кнопки "Сохранить" в модальном окне редактирования вопроса
* вносятся соответствующие изменения в БД */
$(document).on('submit', '#edit-question-form', function (event) {
    event.preventDefault()

    // Из БД извлекается редактируемый вопрос QuestionDto и в него вносятся измения, указанные пользователем
    let questionId = this.dataset.idquestion
    let themeId = this.dataset.idtheme
    let questionDto = getQuestionByQuestionIdAndThemeId(questionId, themeId)
    questionDto.question = this.question.value
    questionDto.answer = this.answer.value
    questionDto.weight = this.weight.value

    // На основании данных формы отправляется запрос на сервер
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
