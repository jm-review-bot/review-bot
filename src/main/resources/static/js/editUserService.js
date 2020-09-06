$(document).on('click', '.edit-user', function () {
    let userId = this.dataset.id
    let userDto = getUserDtoById(userId)

    let allRoles = getAllRoles()
    let htmlContent = ''
    for (let i = 0; i < allRoles.length; i++) {
        let role = allRoles[i]
        let htmlTheme = `
            <option value="${role.name}" ${userDto.role == role.name ? 'selected' : ''}>
                ${role.name}
            </option>
        `
        htmlContent += htmlTheme
    }
    $('#edit-user-form select').html(htmlContent)
    $('#edit-user-form :input[name~="name"]').val(`${userDto.name}`)
    $('#edit-user-form :input[name~="vkId"]').val(userDto.vkId)
    $('#edit-user-form').attr({'data-id' : userId})
    $('#edit-user-modal-window').modal('show')
})

$(document).on('submit', '#edit-user-form', function (event) {
    event.preventDefault()

    let userId = this.dataset.id
    let userDto = {
        id : userId,
        name : this.name.value,
        stringVkId : this.vkId.value,
        role : $('#edit-user-form select :selected')[0].value
    }

    $.ajax({
        url: `/api/admin/user/${userId}`,
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(userDto),
        success: function() {
            $('#edit-user-modal-window').modal('hide')
            buildUsersTable()
            $('#edit-user-form')[0].reset()
        },
        error: function () {
            alert('Возникла ошибка при редактировании пользователя')
        }
    })
})
