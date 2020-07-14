/*
* По нажатию на "Добавить вопрос" формируется модальное окно
* с формой для добавления вопроса и в ней передается ID темы,
* в которую будет добавлен вопрос
*
* add-question-to-theme - кнопка, по которой должно открываться
* модальное окно для добавления вопроса в тему
* this.dataset.id - ID темы, который заложен в кнопку
* */
$(document).on('click', '.add-question-to-theme', function () {
    $('#modal-title').html('Новый вопрос')
    $('#modal-body').html(`
        <form id="add-question-form" data-id="${this.dataset.id}">
            <div class="form-group">
                Текст вопроса:
                <textarea rows="10" input class="col" type="text" name="question" placeholder="Введите текст вопроса" required></textarea>
            </div>
            <div class="form-group">
                Ответ на вопрос:
                <textarea rows="10" input class="col" type="text" name="answer" placeholder="Введите ответ на вопрос" required></textarea>
            </div>
            <div class="form-group">
                Вес вопроса:
                <input class="col" type="number" name="weight" placeholder="Введите вес вопроса" required>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Отмена</button>
                <button type="submit" class="btn btn-primary">Создать</button>
            </div>
        </form>
    `)
    $('#modal-window').modal('show')
})

/*
* После подтверждении вводимых данных пользователем собираем
* данные формы в questionDto и отправляем его не бэкенд
*
* this.dataset.id - ID темы, который заложен в форму
* */
$(document).on('submit', '#add-question-form', function (event) {
    event.preventDefault()
    let formData = $('#add-question-form').serializeArray();
    let questionDto = {};
    $(formData).each(
        (index, obj) => {
            questionDto[obj.name] = obj.value;
        }
    );
    addQuestion(this.dataset.id, questionDto)
})


/*
* Отправляем запрос на добавление вопроса в тему на бэкенд.
* В случае успешной процедуры должны закрыться модальное окно
* и обновиться список вопросов темы.
* */
function addQuestion(themeId, questionDto) {
    $.ajax({
        url: `/api/admin/theme/${themeId}/question`,
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(questionDto),
        success: function() {
            $('#modal-window').modal('hide')
            buildListQuestionsByThemeId(themeId)
        },
        error: function () {
            alert('Возникла ошибка при отправке запроса на добавление вопроса')
        }
    })
}