$('.add-new-theme').click(function () {
        let title = $('#idInputForNameOfTheNewTheme').val();
        let criticalWeight = $('#idInputForCriticalWeightOfTheNewTheme').val();
        let reviewPoint = $('#idInputForReviewPointOfTheNewTheme').val();
        let type = $('#typeThemeNewThemeInput option:selected').val();
        let json = {
            title:title,
            criticalWeight:criticalWeight,
            reviewPoint:reviewPoint,
            type : type
        };
        $.ajax({
            url: "/api/admin/theme",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(json),
            success: function() {
                buildThemesAccordion(getAllThemesDto());
                $('#containerForAddingTheme').modal('hide');
                document.getElementById("formForAddingTheme").reset();
            },
            error: function() {
                alert("ОШИБКА: POST-запрос добавления темы неудачно завершился.");
                $('#containerForAddingTheme').modal('hide');
                document.getElementById("formForAddingTheme").reset();
            }
        })
    }
)