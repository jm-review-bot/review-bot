$(function () {
    let allThemesDto = getAllThemesDto()
    buildThemesAccordion(allThemesDto)
})

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
    $('#theme-accordion').html('')
    let htmlContent = ''
    $(allThemesDto).each((i, theme) => {
        let themeHtmlAccordion = `
            <div class="card">
                <div class="card-header" id="card-header-theme-${theme.id}">
                    <h4 class="mb-0">
                        <div class="row">
                            <div class="col-10" role="button" data-toggle="collapse" data-target="#theme-${theme.id}"
                                 aria-expanded="true" aria-controls="theme-${theme.id}">
                                ${theme.title}
                            </div>
                            <div id="theme-${theme.id}-buttons" class="col-2 text-right">
                                <button class="btn btn-link" id="nextTheme-${theme.id}" type="button">
                                    <span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
                                </button>
                                <button class="btn btn-link" id="previousTheme-${theme.id}" type="button">
                                    <span class="glyphicon glyphicon-menu-up" aria-hidden="true"></span>
                                </button>
                                <button class="btn btn-link" id="updateTheme-${theme.id}" type="button">
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
                        <button id="addQuestion" type="button" class="btn btn-outline-primary btn-lg btn-block">Добавить вопрос</button>
                    </div>
                </div>
            </div>
        `
        htmlContent += themeHtmlAccordion;
    })
    $('#theme-accordion').append(htmlContent)
}