function getAllFeedbacksDto() {
    let allFeedbacksDto;
    $.ajax({
        url: '/api/admin/feedback',
        type: 'get',
        async: false,
        success: (data) => {
            allFeedbacksDto = data;
        }
    });
    return allFeedbacksDto;
}

function getStudentCommentByFeedbackId(feedbackId) {
    let studentComment;
    $.ajax({
        url: `/api/admin/feedback/${feedbackId}/comment`,
        type: 'get',
        async: false,
        success: (data) => {
            studentComment = data
        }
    });
    return studentComment;
}