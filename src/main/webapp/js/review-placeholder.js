function changeReviewPlaceholder() {
    const urlParams = new URLSearchParams(window.location.search);
    const merchantID = urlParams.get('id');
    let reviewUrl = "review.html?id=" + merchantID
    $.get(reviewUrl, function(data){
         $("#review-placeholder").replaceWith(data);
    });
}

changeReviewPlaceholder()

