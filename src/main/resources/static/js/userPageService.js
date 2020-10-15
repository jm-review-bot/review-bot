$(function () {
    buildUsersTable()
})

function getAllUsersDto() {
    let allUsersDto
    $.ajax({
        url: '/api/admin/user',
        type: 'get',
        async: false,
        success: (data) => {
            allUsersDto = data
        },
        error: function () {
            alert("При получении данных возникла непредвиденная ошибка...")
        }
    })
    return allUsersDto
}

function getUserDtoById(userId) {
    let userDto
    $.ajax({
        url: `/api/admin/user/${userId}`,
        type: 'get',
        async: false,
        success: (data) => {
            userDto = data
        },
        error: function () {
            alert("При получении данных возникла непредвиденная ошибка...")
        }
    })
    return userDto
}

function buildUsersTable() {

    let allUsersDto = getAllUsersDto()

    let htmlContent = ''
    for (let i = 0; i < allUsersDto.length; i++) {
        let user = allUsersDto[i]
        let userAdminEditButton = ''
        let username
        let deleteRestoreButton
        if (user.role === "ADMIN") {
            userAdminEditButton = `<td>
            <button type="button" class="btn btn-outline-success change-password" data-id="${user.id}">
                Сменить пароль
            </button>
        </td>`
        } else{
            userAdminEditButton = `<td>
        </td>`
        }
        if (user.deleted === true){
            username = "<s>" + user.firstName + " " + user.lastName + "</s>"
            deleteRestoreButton = `<button type="button" class="btn btn-outline-info restore-user" data-id="${user.id}">
                        Восстановить
                    </button>`
        }
        else {
            username = user.firstName + " " + user.lastName
            deleteRestoreButton = `<button type="button" class="btn btn-outline-danger delete-user" data-id="${user.id}">
                        Удалить
                    </button>`
        }
        let usersHtmlTable = `
            <tr>
                <th>${user.id}</th>
                <td>${username}</td>
                <td>${user.vkId}</td>
                <td>${user.role}</td>
                <td>
                    <button type="button" class="btn btn-outline-success edit-user" data-id="${user.id}">
                        Редактировать
                    </button>
                </td>` + userAdminEditButton +
            `<td>` + deleteRestoreButton +
                `</td>
            </tr>
        `
        htmlContent += usersHtmlTable;
    }
    $('#users-table-body').html(htmlContent)
}
