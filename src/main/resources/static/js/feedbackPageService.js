$(function () {
    let allFeedbacksDto = getAllFeedbacksDto()
    buildFeedbacksAccordion(allFeedbacksDto)
})

function getAllFeedbacksDto() {
    let allFeedbacksDto;
    $.ajax({
        url: '/api/admin/feedback',
        type: 'get',
        async: false,
        error: function() {
            console.log("get-feedback--ERROR!");
        },
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

function getTheTextOfTheComment(studentComment) {//modalFormForStudentComment
    let htmlContent = `
                <p>${studentComment}</p>
        `;
    $('#studentComment').html(htmlContent);
    $('#modalFormForStudentComment').modal('show');
}

function buildFeedbacksAccordion(allFeedbacksDto) {
    let htmlContent = ''
    for (let i = 0; i < allFeedbacksDto.length; i++) {
        let feedback = $(allFeedbacksDto)[i];
        let themeHtmlAccordion = `
              <tr>
                  <th>${feedback.id}</th>
                  <td>${feedback.studentFirstName}</td>
                  <td>${feedback.studentLastName}</td>
                  <td>${feedback.reviewerFirstName}</td>
                  <td>${feedback.reviewerLastName}</td>
                  <td>${feedback.ratingReview}</td>
                  <td>${feedback.ratingReviewer}</td>
                  <td>
                      <button type="button" class="btn btn-dark" data-feedback-id="${feedback.id}" data-feedback-comment="${feedback.studentComment}" onclick="getTheTextOfTheComment(this.dataset.feedbackComment)">
                      комментарий
                      </button>
                  </td>
              </tr>
        `;
        htmlContent += themeHtmlAccordion;
    }
    $('#feedback-accordion').html(htmlContent);
}