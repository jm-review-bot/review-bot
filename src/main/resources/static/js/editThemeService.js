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
});

function closeWindowAndRefreshThemesList() {
    $('#changeThemeModal').modal('hide');
    buildThemesAccordion(getAllThemesDto());
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
        async : true ,
        success : buildThemesAccordion(getAllThemesDto()),
        error : buildThemesAccordion(getAllThemesDto())
    });
};