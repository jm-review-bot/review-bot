$('.add-new-theme').click(function () {
        let title = $('#newThemeNameInput').val();
        let criticalWeight = $('#criticalWeightNewThemeInput').val();
        let reviewPoint = $('#reviewPointNewThemeInput').val();
        let type = $('#typeThemeNewThemeInput option:selected').val();
        let json = {
            title:title,
            criticalWeight:criticalWeight,
            reviewPoint:reviewPoint,
            type : type
        }
        $.ajax({
            url: "/api/admin/theme",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(json),
            success: function() {
                buildThemesAccordion(getAllThemesDto());
                $('#containerForAddingTheme').modal('hide');
            },
            error: function() {
                alert("ОШИБКА: POST-запрос добавления темы неудачно завершился.");
            }
        });
    }
);
