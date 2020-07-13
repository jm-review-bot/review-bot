$(function () {
    $('.add-new-theme').onclick(function () {
            let value = document.getElementById('idInputForNameOfTheNewTheme').value;
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
                },
                contentType: "application/json",
                data: JSON.stringify(json)
            });
        }
    )
})