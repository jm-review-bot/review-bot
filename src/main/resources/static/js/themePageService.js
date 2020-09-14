$(function () {
    let allThemesDto = getAllThemesDto();
    buildThemesAccordion(allThemesDto);
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
    let htmlContent = ''
    for (let i = 0; i < allThemesDto.length; i++) {
        let theme = allThemesDto[i]
        let themeHtmlAccordion = `
            <div class="card mb-4 border-bottom">
                <div class="card-header border-bottom bg-secondary">
                    <h4 class="mb-0">
                        <div class="row">
                            <div class="col-10 theme-expand" role="button" data-toggle="collapse" data-target="#theme-${theme.id}" aria-expanded="true" aria-controls="theme-${theme.id}" data-id="${theme.id}" data-type="${theme.type}">
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
                <div id="theme-${theme.id}" class="collapse border-bottom bg-light" aria-labelledby="card-header-theme-${theme.id}">
                    <div class="card-body">
                        <div id="card-theme-${theme.id}" class="card border-light bg-light">
                        <!--Здесь располагаются вопросы темы или список проверяющих-->
                        </div>
                    </div>
                </div>
            </div>
        `
        htmlContent += themeHtmlAccordion;
    }
    $('#theme-accordion').html(htmlContent)
    $('.move-down-theme').click(function () {
            let themeId = this.dataset.id;
            let url = "/api/admin/theme/"+themeId+"/position/down";
            $.ajax({
                type : 'PATCH',
                url: url,
                success: function() {
                    buildThemesAccordion(getAllThemesDto());
                }
            });
        }
    );
    $('.move-up-theme').click(function () {
            let themeId = this.dataset.id;
            let url = "/api/admin/theme/"+themeId+"/position/up";
            $.ajax({
                type : 'PATCH',
                url: url,
                success: function() {
                    buildThemesAccordion(getAllThemesDto());
                }
            });
        }
    );
}

$(document).on('click', '.theme-expand', function () {
    let themeId = this.dataset.id
    let themeType = this.dataset.type
    if (themeType == "fixed") {
        buildListQuestionsByThemeId(themeId)
    } else if (themeType == "free") {
        buildListReviewersByThemeId(themeId)
    }
})
