$('#search-form').on('submit', function (event) {
    event.preventDefault()
    let searchString = this.search.value

    $.ajax({
        url: "/api/admin/search",
        type: "POST",
        contentType: "application/json",
        data: searchString,
        success: function (data) {
            buildSearchResultsAccordion(data)
            $('#search-modal-window').modal('show')
        },
        error: function () {
            alert("При выполнении поиска возникла непредвиденная ошибка");
        }
    })
})

function buildSearchResultsAccordion(searchResults) {

    // Входные данные разделяются по типам
    let users = searchResults.users
    let themes = searchResults.themes
    let questions = searchResults.questions
    let feedbacks = searchResults.feedbacks
    let roles = searchResults.roles

    // Результат поиска по пользователям
    let usersSize = users.length
    if (usersSize > 0) {
        let htmlContent = ''
        for (let i = 0; i < usersSize; i++) {
            let user = users[i]
            htmlContent += `
                <tr>
                    <th>${user.id}</th>
                    <th>${user.firstName}</th>
                    <th>${user.lastName}</th>
                    <th>${user.vkId}</th>
                    <th>${user.role}</th>
                    <th>${user.reviewPoint}</th>
                </tr>
            `
        }
        $('#users-search-tbody').html(htmlContent)
    } else {
        $('#users-search-tbody').html('<tr><td colspan="6" align="center">Ничего не найдено</td></tr>')
    }
    $('#users-search-card-header').html(`Пользователи (найдено: ${usersSize})`)

    // Результат поиска по темам
    let themesSize = themes.length
    if (themesSize > 0) {
        let htmlContent = ''
        for (let i = 0; i < themesSize; i++) {
            let theme = themes[i]
            htmlContent += `
                <tr>
                    <th>${theme.id}</th>
                    <th>${theme.title}</th>
                    <th>${theme.criticalWeight}</th>
                    <th>${theme.position}</th>
                    <th>${theme.reviewPoint}</th>
                    <th>${theme.type}</th>
                </tr>
            `
        }
        $('#themes-search-tbody').html(htmlContent)
    } else {
        $('#themes-search-tbody').html('<tr><td colspan="6" align="center">Ничего не найдено</td></tr>')
    }
    $('#themes-search-card-header').html(`Темы (найдено: ${themesSize})`)

    // Результат поиска по вопросам
    let questionsSize = questions.length
    if (questionsSize > 0) {
        let htmlContent = ''
        for (let i = 0; i < questionsSize; i++) {
            let question = questions[i]
            htmlContent += `
                <tr>
                    <th>${question.id}</th>
                    <th>${question.question}</th>
                    <th>${question.answer}</th>
                    <th>${question.weight}</th>
                </tr>
            `
        }
        $('#questions-search-tbody').html(htmlContent)
    } else {
        $('#questions-search-tbody').html('<tr><td colspan="4" align="center">Ничего не найдено</td></tr>')
    }
    $('#questions-search-card-header').html(`Вопросы (найдено: ${questionsSize})`)

    // Результат поиска по отзывам к ревью
    let feedbacksSize = feedbacks.length
    if (feedbacksSize > 0) {
        let htmlContent = ''
        for (let i = 0; i < feedbacksSize; i++) {
            let feedback = feedbacks[i]
            htmlContent += `
                <tr>
                    <th>${feedback.id}</th>
                    <th>${feedback.studentFirstName} ${feedback.studentLastName}</th>
                    <th>${feedback.reviewerFirstName} ${feedback.reviewerLastName}</th>
                    <th>${feedback.ratingReview}</th>
                    <th>${feedback.ratingReviewer}</th>
                    <th>${feedback.studentComment}</th>
                </tr>
            `
        }
        $('#feedbacks-search-tbody').html(htmlContent)
    } else {
        $('#feedbacks-search-tbody').html('<tr><td colspan="6" align="center">Ничего не найдено</td></tr>')
    }
    $('#feedbacks-search-card-header').html(`Отзывы к ревью (найдено: ${feedbacksSize})`)

    // Результат поиска по ролям
    let rolesSize = roles.length
    if (rolesSize > 0) {
        let htmlContent = ''
        for (let i = 0; i < rolesSize; i++) {
            let role = roles[i]
            htmlContent += `
                <tr>
                    <th>${role.id}</th>
                    <th>${role.name}</th>
                </tr>
            `
        }
        $('#roles-search-tbody').html(htmlContent)
    } else {
        $('#roles-search-tbody').html('<tr><td colspan="2" align="center">Ничего не найдено</td></tr>')
    }
    $('#roles-search-card-header').html(`Роли (найдено: ${rolesSize})`)
}
