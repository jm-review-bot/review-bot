$(function(){
    let currentThemeId = null;
    let currentThemeDto = null;

    $('.edit-theme').on('click' , function () {
        currentThemeId = this.dataset.id;
        currentThemeDto = getThemeDtoById(currentThemeId);
        openModal(currentThemeDto);
    });

    $('.delete-theme-button').on('click' , function () {
        if (askUserToDeleteTheme(currentThemeDto)) {
            deleteTheme(currentThemeId);
            closeWindowAndRefreshThemesList();
            buildThemesAccordion(getAllThemesDto());
            buildThemesAccordion(getAllThemesDto());
        }
    });

    $('#saveThemeChanging').click(function () {
        closeWindowAndRefreshThemesList();
    });

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
            async : true ,
            success : buildThemesAccordion(getAllThemesDto())
        });
    };

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

});