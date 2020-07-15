$('.add-new-theme').click(function () {
        let title = $('#idInputForNameOfTheNewTheme').val();
        let criticalWeight = $('#idInputForCriticalWeightOfTheNewTheme').val();
        let reviewPoint = $('#idInputForReviewPointOfTheNewTheme').val();
        let json = {
            title:title,
            criticalWeight:criticalWeight,
            reviewPoint:reviewPoint
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
)