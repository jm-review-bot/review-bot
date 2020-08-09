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

function getTheTextOfTheComment(feedbackId) {
    let studentComment = getStudentCommentByFeedbackId(feedbackId);
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
        let ratingReview = feedback.ratingReview;
        let ratingReviewer = feedback.ratingReviewer;
        let tdRatingReviewColor = ratingReview < 6 ?
            "#"+dec2hex(255)+dec2hex(0)+dec2hex(Math.round(63.75*(ratingReview-1)))
            :
            "#"+dec2hex(0)+dec2hex(255)+dec2hex(Math.round(63.75*(10-ratingReview)));
        let tdRatingReviewerColor = ratingReviewer < 6 ?
            "#"+dec2hex(255)+dec2hex(0)+dec2hex(Math.round(63.75*(ratingReviewer-1)))
            :
            "#"+dec2hex(0)+dec2hex(255)+dec2hex(Math.round(63.75*(10-ratingReviewer)));
        let themeHtmlAccordion = `
              <tr>
                  <th>${feedback.id}</th>
                  <td>${feedback.studentFirstName}</td>
                  <td>${feedback.studentLastName}</td>
                  <td >${feedback.reviewerFirstName}</td>
                  <td>${feedback.reviewerLastName}</td>
                  <td bgcolor=${tdRatingReviewColor}>${ratingReview}</td>
                  <td bgcolor=${tdRatingReviewerColor}>${ratingReviewer}</td>
                  <td>
                      <button type="button" class="btn btn-dark" data-feedback-id="${feedback.id}" onclick="getTheTextOfTheComment(this.dataset.feedbackId)">
                      комментарий
                      </button>
                  </td>
              </tr>
        `;
        htmlContent += themeHtmlAccordion;
    }
    $('#feedback-accordion').html(htmlContent);
}

function dec2hex(d){
    if(d>15) {
        return d.toString(16)
    } else {
        return "0"+d.toString(16)
    }
}