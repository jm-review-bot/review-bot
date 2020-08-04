$(document).on('click', '.delete-reviewer-from-theme', function () {
    let themeId = this.dataset.idtheme
    let reviewerId = this.dataset.idreviewer

    $.ajax({
        url: `/api/admin/theme/${themeId}/reviewer/${reviewerId}`,
        type: 'delete',
        success: function () {
            buildListReviewersByThemeId(themeId)
        },
        error: function () {
            alert("При удаления проверяющего возникла непредвиденная ошибка")
        }
    });
})
