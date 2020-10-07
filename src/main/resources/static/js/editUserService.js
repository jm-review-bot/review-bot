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
    $('#edit-user-form :input[name~="name"]').val(`${userDto.firstName} ${userDto.lastName}`)
    $('#edit-user-form :input[name~="vkId"]').val(userDto.vkId)
    $('#edit-user-form').attr({'data-id': userId})
    $('#edit-user-modal-window').modal('show')
})

$(document).on('submit', '#edit-user-form', function (event) {
    event.preventDefault()

    let name = this.name.value.split(' ')
    if (name.length != 2) { // Проверка корректности ввода имени и фамилии
        alert('Введите имя и фамилию, разделенные пробелом')
    } else {
        let userId = this.dataset.id
        let userDto = {
            id: userId,
            firstName: name[0],
            lastName: name[1],
            stringVkId: this.vkId.value,
            role: $('#edit-user-form select :selected')[0].value
        }

        $.ajax({
            url: `/api/admin/user/${userId}`,
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(userDto),
            success: function () {
                $('#edit-user-modal-window').modal('hide')
                buildUsersTable()
                $('#edit-user-form')[0].reset()
            },
            error: function () {
                alert('Возникла ошибка при редактировании пользователя')
            }
        })
    }
})


$(document).on('click', '.change-password', function () {
    let userId = this.dataset.id
    $('#change-password-form :input[name~="userId"]').val(userId)
    let button = document.getElementById('passwordChangeSubmit')
    $('#change-password-form')[0].reset()
    button.disabled = true
    $('#message').html("")
    $('#change-password-modal-window').modal('show')
})

$(document).on('submit', '#change-password-form', function (event) {
    event.preventDefault()
    let userId = this.userId.value
    let newPassword = this.password.value
    $.ajax({
        url: `/api/admin/user/passwd/${userId}`,
        type: 'post',
        data: {newPassword: newPassword},
        success: function () {
            $('#change-password-modal-window').modal('hide')
            $('#change-password-form')[0].reset()
        },
        error: function () {
            alert('Возникла ошибка при изменения пароля пользователя')
        }
    })
})

$('#password, #confirmPassword').keyup(function () {
    let button = document.getElementById('passwordChangeSubmit')
    if ($('#password').val() == $('#confirmPassword').val()) {
        $('#message').html('Пароль и подтверждение совпадают').css('color', 'green');
        button.disabled = false
    } else {
        $('#message').html('Пароль и подтверждение не совпадают').css('color', 'red');
        button.disabled = true
    }
});
