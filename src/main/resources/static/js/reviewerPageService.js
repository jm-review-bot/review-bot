function getListReviewersDtoByThemeId(themeId) {
    let listReviewersDto
    $.ajax({
        url: `/api/admin/theme/${themeId}/reviewer`,
        type: 'get',
        async: false,
        success: (data) => {
            listReviewersDto = data
        }
    })
    return listReviewersDto
}

function buildListReviewersByThemeId(themeId) {
    let listReviewers = getListReviewersDtoByThemeId(themeId)
    let htmlContent = '<h5 align="center">Список всех проверяющих:</h5></br>'
    for (let i = 0; i < listReviewers.length; i++) {
        let reviewer = $(listReviewers)[i]
        let htmlQuestion = `
            <div class="card bg-light">
                <div class="row">
                    <div class="col-10">
                        <h5>${reviewer.firstName} ${reviewer.lastName}</h5>
                    </div>
                    <div class="col-2 text-right">
                        <button class="delete-reviewer-from-theme btn btn-link" type="button" data-idreviewer="${reviewer.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
            </div></br>
        `
        htmlContent += htmlQuestion
    }
    htmlContent += `
        <div>
            <button type="button" class="add-reviewer-to-theme btn btn-lg btn-block" data-id="${themeId}">
                Добавить проверяющего
            </button>
        </div>
    `
    $(document).find(`#card-theme-${themeId}`).html(htmlContent)
}
