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
            <div class="card mb-4">
                <div class="card-header">
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
                <div id="theme-${theme.id}" class="collapse" aria-labelledby="card-header-theme-${theme.id}">
                    <div class="card-body">
                        <div id="card-theme-${theme.id}" class="card border-white">
                        <!--Здесь располагаются вопросы темы или список проверяющих-->
                        </div>
                    </div>
                    <div>
                        <button type="button" class="add-question-to-theme btn btn-lg btn-block" data-id="${theme.id}">
                            Добавить вопрос
                        </button>
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
