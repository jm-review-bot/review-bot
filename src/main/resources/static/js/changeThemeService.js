$(document).on('click' , '.edit-theme' , function () {
    let themeId = this.dataset.id
    let themeDto = getThemeDtoById(themeId)

    openModal(themeDto)

    $('#saveThemeChanging').on('click' , function () {
        closeWindowAndRefreshThemesList();
    })

    $('.delete-theme-button').on('click' , function () {
        if (askUserToDeleteTheme(themeDto)) {
            deleteTheme(themeId)
            closeWindowAndRefreshThemesList()
        }
        openModal(themeDto)
    });
})

function openModal(themeDto) {
    $('#changeThemeModal').modal();
    $('.edit-theme-title').html(themeDto.title);
}

function closeWindowAndRefreshThemesList() {
    $('#changeThemeModal').modal('hide');
    buildThemesAccordion(getAllThemesDto());
}

function deleteTheme(themeId) {
    $.ajax({
        url : `/api/admin/theme/${themeId}`,
        type: 'DELETE' ,
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
    let title = `Вы действительно хотите удалить тему: ${theme.title} ?`;
    return confirm(title);
}