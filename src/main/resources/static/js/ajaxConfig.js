$.ajaxSetup({
    headers: {
        'X-CSRF-TOKEN' : $('#_csrf').attr('content')
    }
})