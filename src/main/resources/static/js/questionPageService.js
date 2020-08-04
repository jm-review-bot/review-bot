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

function buildListQuestionsByThemeId(themeId) {
    let listQuestions = getListQuestionsDtoByThemeId(themeId)
    let htmlContent = ''
    /*
    * Для того, чтобы вопросы отображались в порядке возрастания номера позиции,
    * запускается цикл по количеству вопросов и из всего массива выбирается лишь тот,
    * у которого номер позиции совпадает с текущей итерацией цикла
    * */
    for (let i = 1; i <= listQuestions.length; i++) {
        let question = $(listQuestions).filter(index => {
            return listQuestions[index].position == i
        })[0]
        let htmlQuestion = `<br>
            <div class="card">
                <div class="row">
                    <div class="col-10">
                        <h5>${getThemeDtoById(themeId).position}.${question.position}. ${question.question}</h5><br/>
                    </div>
                    <div class="col-2 text-right">
                        <button class="move-down-question btn btn-link" type="button" data-idquestion="${question.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
                        </button>
                        <button class="move-up-question btn btn-link" type="button" data-idquestion="${question.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-menu-up" aria-hidden="true"></span>
                        </button>
                        <button class="edit-question btn btn-link" type="button" data-idquestion="${question.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                        </button>
                        <button class="delete-question btn btn-link" type="button" data-idquestion="${question.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
            </div>
        `
        htmlContent += htmlQuestion
    }
    $(document).find(`#card-theme-${themeId}`).html(htmlContent).css('background-color' , '#3366CC').css('padding' , '10px');
}


