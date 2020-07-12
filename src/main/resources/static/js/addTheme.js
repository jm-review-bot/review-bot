function addTheme() {
    let value = document.getElementById('idInputForNameOfTheNewTheme').value;
    let j_son = {
        title:value,
        criticalWeight:8,
        reviewPoint:4
    }
    let json = JSON.stringify(j_son);
    let ty = 9;
    console.debug(json);
    $.ajax({
        url: "/api/admin/theme",
        type: "POST",
        data: json,
        dataType: "json"
    });
}