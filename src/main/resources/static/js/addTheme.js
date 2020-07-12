$(function () {
    let buttonsForAddNewTheme = document.getElementsByClassName('add-new-theme');
    for(var i = 0; i < buttonsForAddNewTheme.length; i++) {
        buttonsForAddNewTheme[i].onclick = function () {
            let value = document.getElementById('idInputForNameOfTheNewTheme').value;
            let json = {
                title:value,
                criticalWeight:8,
                reviewPoint:4
            }
            $.ajax({
                url: "/api/admin/theme",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(json)
            });
        }
    }
})