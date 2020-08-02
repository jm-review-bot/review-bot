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
    let htmlContent = ''
    for (let i = 0; i < listReviewers.length; i++) {
        let reviewer = $(listReviewers)[i]
        let htmlQuestion = `
            <div class="card">
                <div class="row">
                    <div class="col-10">
                        ${reviewer.firstName}. ${reviewer.secondName}<br/>
                    </div>
                    <div class="col-2 text-right">
                        <button class="delete-reviewer-from-theme btn btn-link" type="button" data-idreviewer="${reviewer.id}" data-idtheme="${themeId}">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                        </button>
                    </div>
                </div>
            </div>
        `
        htmlContent += htmlQuestion
    }
    $(document).find(`#card-theme-${themeId}`).html(htmlContent)
}
