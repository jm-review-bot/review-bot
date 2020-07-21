/*
* По нажатию на "Добавить вопрос" открывается модальное окно
* с формой для добавления вопроса и в ней передается ID темы,
* в которую будет добавлен вопрос.
*
* add-question-to-theme - кнопка, по которой должно открываться
* модальное окно для добавления вопроса в тему.
*
* this.dataset.id - ID темы, который заложен в кнопку.
* */
$(document).on('click', '.add-question-to-theme', function () {
    $('#add-question-form').attr({'data-id' : this.dataset.id})
    $('#add-question-modal-window').modal('show')
})

/*
* После подтверждения пользователем (нажатия на "Сохранить")
* собираем данные формы в questionDto и отправляем его не бэкенд.
* В случае успешной процедуры должны закрыться модальное окно,
* обновиться список вопросов темы и очиститься форма.
*
* this.dataset.id - ID темы, который заложен в форму.
* */
$(document).on('submit', '#add-question-form', function (event) {
    event.preventDefault()

    let themeId = this.dataset.id
    let questionDto = {
        question : this.question.value,
        answer : this.answer.value,
        weight : this.answer.weight
    }

    $.ajax({
        url: `/api/admin/theme/${themeId}/question`,
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(questionDto),
        success: function() {
            $('#add-question-modal-window').modal('hide')
            buildListQuestionsByThemeId(themeId)
            $('#add-question-form')[0].reset()
        },
        error: function () {
            alert('Возникла ошибка при добавлении нового вопроса')
        }
    })
})