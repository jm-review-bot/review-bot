$(document).on('click', '.delete-user', function () {
    let userId = this.dataset.id
    let userDto = getUserDtoById(userId)

    if (askToDeleteUser(userDto)) {
        deleteUser(userId)
        buildUsersTable()
    }
})

$(document).on('click', '.restore-user', function () {
    let userId = this.dataset.id
    restoreUser(userId)
    buildUsersTable()
})

function deleteUser(userId) {
    $.ajax({
        url: `/api/admin/user/${userId}`,
        type: 'delete',
        async: false
    });
}

function restoreUser(userId) {
    $.ajax({
        url: `/api/admin/user/${userId}`,
        type: 'patch',
        async: false
    });
}


function askToDeleteUser(userDto) {
    let title = `Вы действительно хотите удалить пользователя ${userDto.firstName} ${userDto.lastName}?`
    return confirm(title)
}
