function changeReviewPlaceholder() {
    const urlParams = new URLSearchParams(window.location.search);
    const userID = urlParams.get('user');
    let reviewUrl = "user-review.html?user=" + userID
    $.get(reviewUrl, function(data){
         $("#review-placeholder").replaceWith(data);
    });
}

changeReviewPlaceholder()
