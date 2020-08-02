$(document).on('click', '.move-down-theme', function () {
        let themeId = this.dataset.id;
        let url = "/api/admin/theme/"+themeId+"/position/down";
        $.ajax({
            type : 'PATCH',
            url: url,
            success: function() {
                console.log("move-down-theme.click--WIN!");
                buildThemesAccordion(getAllThemesDto());//location.reload();
            },
            error: function () {
                console.log("move-down-theme.click--ERROR!");
            }
        })
    }
)

$(document).on('click', '.move-up-theme', function () {
        let themeId = this.dataset.id;
        let url = "/api/admin/theme/"+themeId+"/position/up";
        $.ajax({
            type : 'PATCH',
            url: url,
            success: function() {
                console.log("move-up-theme.click--WIN!");
                buildThemesAccordion(getAllThemesDto());//location.reload();
            },
            error: function () {
                console.log("move-up-theme.click--ERROR!");
            }
        })
    }
)