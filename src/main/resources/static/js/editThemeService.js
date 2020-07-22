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

function getAllThemesDto() {
    let allThemesDto;
    $.ajax({
        url: '/api/admin/theme',
        type: 'get',
        async: false,
        success: (data) => {
            allThemesDto = data;
        }
    });
    return allThemesDto;
}

function buildThemesAccordion(allThemesDto) {
    let htmlContent = ''
    /*
    * Для того, чтобы темы отображались в порядке возрастания номера позиции,
    * запускается цикл по количеству тем и из всего массива выбирается лишь та,
    * у которой номер позиции совпадает с текущей итерацией цикла
    * */
    for (let i = 1; i <= allThemesDto.length; i++) {
        let theme = $(allThemesDto).filter(index => {
            return allThemesDto[index].position == i
        })[0]
        let themeHtmlAccordion = `
            <div class="card">
                <div class="card-header">
                    <h4 class="mb-0">
                        <div class="row">
                            <div class="col-10 theme-expand" role="button" data-toggle="collapse" data-target="#theme-${theme.id}" aria-expanded="true" aria-controls="theme-${theme.id}" data-id="${theme.id}">
                                ${theme.title}
                            </div>
                            <div class="col-2 text-right">
                                <button class="move-down-theme btn btn-link" type="button" data-id="${theme.id}">
                                    <span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
                                </button>
                                <button class="move-up-theme btn btn-link" type="button" data-id="${theme.id}">
                                    <span class="glyphicon glyphicon-menu-up" aria-hidden="true"></span>
                                </button>
                                <button class="edit-theme btn btn-link" type="button" data-id="${theme.id}">
                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                </button>
                            </div>
                        </div>
                    </h4>
                </div>
                <div id="theme-${theme.id}" class="collapse" aria-labelledby="card-header-theme-${theme.id}">
                    <div class="card-body">
                        <div id="card-theme-${theme.id}" class="card">
                        <!--Здесь располагаются вопросы темы-->
                        </div>
                    </div>
                    <div>
                        <button type="button" class="add-question-to-theme btn btn-outline-primary btn-lg btn-block" data-id="${theme.id}">
                            Добавить вопрос
                        </button>
                    </div>
                </div>
            </div>
        `
        htmlContent += themeHtmlAccordion;
    }
    $('#theme-accordion').html(htmlContent)
}