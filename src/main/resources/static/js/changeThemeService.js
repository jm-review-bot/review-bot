$(document).on('click' , '.edit-theme' , function () {
    let themeId = this.dataset.id
    let themeDto = getThemeDtoById(themeId)

    openModal(themeDto)

    $('#saveChanging').on('click' , function () {
        closeWindowAndRefreshPage();
    })

    $('.delete-theme-button').on('click' , function () {
        debugger;
        if (askUserToDeleteTheme(themeDto)) {
            deleteTheme(themeId)
            closeWindowAndRefreshPage()
        }
        openModal(themeDto)
    });
})

function openModal(themeDto) {
    $('#changeThemeModal').modal();
    $('.theme-title').html(themeDto.title);
}

function closeWindowAndRefreshPage() {
    $('#changeThemeModal').modal('hide');
    buildThemesAccordion(getAllThemesDto());
}

function deleteTheme(themeId) {
    debugger;
    $.ajax({
        url : `/api/admin/theme/${themeId}`,
        type: 'DELETE' ,
        async: false ,
    })
}

function getThemeDtoById(themeId) {
    let themeDto
    $.ajax({
        url : `/api/admin/theme/${themeId}` ,
        type : 'GET' ,
        async : false ,
        success: (data) => {
            themeDto = data
        }
    });
    return themeDto;
}

function askUserToDeleteTheme(theme) {
    debugger;
    let title = `Вы действительно хотите удалить тему: ${theme.title} ?`;
    return confirm(title);
}