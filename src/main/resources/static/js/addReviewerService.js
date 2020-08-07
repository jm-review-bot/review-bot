$(document).on('click', '.add-reviewer-to-theme', function () {

    let themeId = this.dataset.id
    $('#add-reviewer-form').attr({'data-id' : themeId})

    let reviewersDtoNotInThisTheme = getListReviewersDtoNotInThisTheme(themeId)
    let htmlContent = ''
    htmlContent += `
        <option selected value="0">Выберите из списка...</option>
    `
    for (let i = 0; i < reviewersDtoNotInThisTheme.length; i++) {
        let reviewer = reviewersDtoNotInThisTheme[i]
        let htmlReviewer = `
            <option value="${reviewer.id}">
                ${reviewer.firstName} ${reviewer.lastName}
            </option>
        `
        htmlContent += htmlReviewer
    }
    $('#add-reviewer-form select').html(htmlContent)

    $('#add-reviewer-modal-window').modal('show')
})

function getListReviewersDtoNotInThisTheme(themeId) {
    let listReviewersDto
    $.ajax({
        url: `/api/admin/theme/${themeId}/reviewer/notInThisTheme`,
        type: 'get',
        async: false,
        success: (data) => {
            listReviewersDto = data
        }
    })
    return listReviewersDto
}

$(document).on('submit', '#add-reviewer-form', function (event) {
    event.preventDefault()

    let themeId = this.dataset.id
    let newReviewerDto = {
        id : $('#add-reviewer-form select :selected')[0].value
    }

    $.ajax({
        url: `/api/admin/theme/${themeId}/reviewer`,
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(newReviewerDto),
        success: function() {
            $('#add-reviewer-modal-window').modal('hide')
            buildListReviewersByThemeId(themeId)
            $('#add-reviewer-form')[0].reset()
        },
        error: function () {
            alert('Возникла ошибка при добавлении нового проверяющего')
        }
    })
})
