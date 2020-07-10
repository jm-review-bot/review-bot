$(document).on('click', '.theme-expand', function () {
    let themeId = this.dataset.id
    let listQuestionsDtoByThemeId = getListQuestionsDtoByThemeId(themeId)
    buildListQuestionsByThemeId(themeId, listQuestionsDtoByThemeId)
})

function getListQuestionsDtoByThemeId(themeId) {
    let listQuestionsDto
    $.ajax({
        url: `/api/admin/theme/${themeId}/question`,
        type: 'get',
        async: false,
        success: (data) => {
            listQuestionsDto = data
        }
    });
    return listQuestionsDto
}

function buildListQuestionsByThemeId(themeId, listQuestions) {
    let htmlContent = ''
    $(listQuestions).each((i, question) => {
        let htmlQuestion = `
            <div class="card">
                <div class="row">
                    <div class="col-10">
                        ${question.position}. ${question.question}<br/>
                        Ответ: ${question.answer}
                    </div>
                    <div class="col-2 text-right">
                        <button class="move-down-question btn btn-link" type="button" data-id="${question.id}">
                            <span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
                        </button>
                        <button class="move-up-question btn btn-link" type="button" data-id="${question.id}">
                            <span class="glyphicon glyphicon-menu-up" aria-hidden="true"></span>
                        </button>
                        <button class="edit-question btn btn-link" type="button" data-id="${question.id}">
                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                        </button>
                        <button class="delete-question btn btn-link" type="button" data-id="${question.id}">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
            </div>
        `
        htmlContent += htmlQuestion
    })
    $(document).find(`#card-theme-${themeId}`).html(htmlContent)
}
