$(document).on('click', '.delete-question', function () {
    let themeId = this.dataset.idtheme
    let questionId = this.dataset.idquestion
    let questionDto = getQuestionDtoByThemeIdAndQuestionId(themeId, questionId)

    if (askUserToDeleteQuestion(questionDto)) {
        deleteQuestionByThemeIdAndQuestionId(themeId, questionId)
        buildListQuestionsByThemeId(themeId)
    }
})

function getQuestionDtoByThemeIdAndQuestionId(themeId, questionId) {
    let questionDto
    $.ajax({
        url: `/api/admin/theme/${themeId}/question/${questionId}`,
        type: 'get',
        async: false,
        success: (data) => {
            questionDto = data
        }
    });
    return questionDto
}

function deleteQuestionByThemeIdAndQuestionId(themeId, questionId) {
    $.ajax({
        url: `/api/admin/theme/${themeId}/question/${questionId}`,
        type: 'delete',
        async: false
    });
}

function askUserToDeleteQuestion(question) {
    let title = `Вы действительно хотите удалить вопрос с текстом: ${question.question}?`
    return confirm(title)
}