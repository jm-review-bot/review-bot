$(document).on('click' , '.edit-theme' , function () {
    let themeId = this.dataset.id;
    let themeDto = getThemeDtoById(themeId);
    $('.edit-theme-title').html(themeDto.title);

    // Форма наполняется текущими данными темы
    $('#edit-theme-form :input[name~="title"]').val(themeDto.title)
    $('#edit-theme-form :input[name~="criticalWeight"]').val(themeDto.criticalWeight)
    $('#edit-theme-form :input[name~="reviewPoint"]').val(themeDto.reviewPoint)
    $('#edit-theme-form :input[name~="themeType"]').val(themeDto.type)
    $('#edit-theme-form').attr({'data-id' : themeId})

    $('#changeThemeModal').modal('show');
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
        async : false ,
        success : closeWindowAndRefreshThemesList()
    });
}

$('#edit-theme-form').on('submit', function (event) {
    event.preventDefault()

    let themeId = this.dataset.idtheme
    let themeDto = getThemeDtoById(themeId)
    themeDto.title = this.title.value
    themeDto.criticalWeight = this.criticalWeight.value
    themeDto.reviewPoint = this.reviewPoint.value

    $.ajax({
        url: `/api/admin/theme/${themeId}`,
        type: 'put',
        contentType: "application/json",
        data: JSON.stringify(themeDto),
        success: function() {
            $('#changeThemeModal').modal('hide')
            buildThemesAccordion(getAllThemesDto())
            $('#edit-theme-form')[0].reset()
        },
        error: function () {
            alert('При редактировании темы возникла непредвиденная ошибка')
        }
    })
})