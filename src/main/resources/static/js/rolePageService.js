function getAllRoles() {
    let allRoles
    $.ajax({
        url: '/api/admin/role',
        type: 'get',
        async: false,
        success: (data) => {
            allRoles = data
        },
        error: function () {
            alert("При получении данных возникла непредвиденная ошибка...")
        }
    })
    return allRoles
}
