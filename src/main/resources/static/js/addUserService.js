$(document).on('click', '#add-user-btn', function () {

    let allThemesDto = getAllThemesDto()

    let htmlContent = ''
    htmlContent += `
        <option selected value="0">Выберите из списка...</option>
    `
    for (let i = 0; i < allThemesDto.length; i++) {
        let themeDto = allThemesDto[i]
        let htmlTheme = `
            <option value="${themeDto.id}">
                ${themeDto.position}. ${themeDto.title}
            </option>
        `
        htmlContent += htmlTheme
    }
    $('#add-user-form select').html(htmlContent)

    $('#add-user-modal-window').modal('show')
})

$(document).on('submit', '#add-user-form', function (event) {
    event.preventDefault()

    let startThemeId = $('#add-user-form select :selected')[0].value
    if (startThemeId == 0) {
        alert('Не выбрана тема...')
    } else {

        let newUserDto = {
            stringVkId: this.vkId.value,
            startThemeId: startThemeId
        }

        $.ajax({
            url: `/api/admin/user`,
            type: 'put',
            contentType: 'application/json',
            data: JSON.stringify(newUserDto),
            success: function () {
                $('#add-user-modal-window').modal('hide')
                buildUsersTable()
                $('#add-user-form')[0].reset()
            },
            error: function (data) {
                if (data.status == 400) {
                    alert('Пользователь уже есть в базе')
                } else {
                    alert('Возникла ошибка при добавлении нового пользователя')
                }
            }
        })
    }
})
