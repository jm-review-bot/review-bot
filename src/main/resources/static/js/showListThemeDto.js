$(function () {
    let allThemesDto = getAllThemesDto()
    buildThemesAccordion(allThemesDto)
})

function buildThemesAccordion(allThemesDto) {
    $('#theme-accordion').html('')
    $(allThemesDto).each((i, theme) => {

        let cardBodyThemeId = 'card-body-theme-' + theme.id
        let themeId = 'theme-' + theme.id

        $('#theme-accordion').append(
            $('<div>').attr({'class': 'card'})
                .append($('<div>').attr({'class': 'card-header'})
                    .append($('<h4>').attr({'class': 'mb-0'})
                        .append($('<div>').attr({'class': 'row'})
                            .append($('<div>').attr({
                                    'class': 'col-10',
                                    'role': 'button',
                                    'data-toggle': 'collapse',
                                    'data-target': '#' + themeId
                                }).text(theme.title)
                            )
                            .append($('<div>').attr({'class': 'col-2 text-right'})
                                .append($('<button>').attr({
                                        'class': 'btn btn-link',
                                        'type': 'button',
                                        'id': 'nextTheme-' + theme.id,
                                        'data-id': theme.id
                                    })
                                        .append($('<span>').attr({
                                            'class': 'glyphicon glyphicon-menu-down',
                                            'aria-hidden': 'true'
                                        }))
                                )
                                .append($('<button>').attr({
                                        'class': 'btn btn-link',
                                        'type': 'button',
                                        'id': 'previousTheme-' + theme.id,
                                        'data-id': theme.id
                                    })
                                        .append($('<span>').attr({
                                            'class': 'glyphicon glyphicon-menu-up',
                                            'aria-hidden': 'true'
                                        }))
                                )
                                .append($('<button>').attr({
                                        'class': 'btn btn-link',
                                        'type': 'button',
                                        'id': 'updateTheme-' + theme.id,
                                        'data-id': theme.id
                                    })
                                        .append($('<span>').attr({
                                            'class': 'glyphicon glyphicon-pencil',
                                            'aria-hidden': 'true'
                                        }))
                                )
                            )
                        )
                    )
                )
                .append($('<div>').attr({
                        'class': 'collapse',
                        'id': themeId
                    })
                        .append($('<div>').attr({
                            'class': 'card-body',
                            'id': cardBodyThemeId
                        }))
                )
        )
    })
}