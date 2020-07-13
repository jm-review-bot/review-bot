$('.add-new-theme').click(function () {
        let value = $('#idInputForNameOfTheNewTheme').val();
        let json = {
            title:value,
            criticalWeight:8,
            reviewPoint:4
        }
        $.ajax({
            url: "/api/admin/theme",
            type: "POST",
            success: function() {
                buildThemesAccordion(getAllThemesDto());
                $('#containerForAddingTheme').modal('hide');
            },
            error: function() {
                alert("ОШИБКА: POST-запрос добавления темы неудачно завершился.");
            },
            contentType: "application/json",
            data: JSON.stringify(json)
        });
    }
)