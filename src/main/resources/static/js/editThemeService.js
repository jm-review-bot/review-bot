$(document).on('click' , '.edit-theme' , function () {
    let themeId = this.dataset.id;
    let themeDto = getThemeDtoById(themeId);
    $('.edit-theme-title').html(themeDto.title);
    $('#changeThemeModal').modal();
    $('.delete-theme-button').attr({'data-id' : themeId});

});

$('.delete-theme-button').on('click' , function () {
    let themeId = this.dataset.id;
    let themeDto = getThemeDtoById(themeId);
    if (askUserToDeleteTheme(themeDto)) {
        deleteTheme(themeId);
        closeWindowAndRefreshThemesList();
    }
    closeWindowAndRefreshThemesList();
});

function closeWindowAndRefreshThemesList() {
    console.log("editThemeService_closeWindowAndRefreshThemesList_1metka");
    $('#changeThemeModal').modal('hide');
    console.log("editThemeService_closeWindowAndRefreshThemesList_2metka");
    buildThemesAccordion(getAllThemesDto());
    console.log("editThemeService_closeWindowAndRefreshThemesList_3metka");
}

function getThemeDtoById(themeId) {
    let themeDto;
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

function deleteTheme(themeId) {
    $.ajax({
        url : `/api/admin/theme/${themeId}`,
        type: 'DELETE' ,
        async : false ,
        success : closeWindowAndRefreshThemesList()
    });
}