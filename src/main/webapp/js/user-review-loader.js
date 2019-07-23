// Get ?user=XYZ parameter value
const url = new URLSearchParams(window.location.search);
const parameterUserID = url.get('user');
// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUserID) {
    window.location.replace('/');
}

/** Fetches messages and add them to the page. */
function fetchReviews() {
    console.log("here fetch reviews")
    const url = '/user-reviews?user=' + parameterUserID;
    fetch(url)
        .then((response) => {
            return response.json();
        })
        .then((reviews) => {
            const reviewsContainer = document.getElementById('reviews-container');
            if (reviews.length == 0) {
                reviewsContainer.innerHTML = '<p>This user has no reviews yet.</p>';
            } else {
                reviewsContainer.innerHTML = '';
            }
            reviews.forEach((review) => {
                const reviewDiv = buildReviewDiv(review);
                reviewsContainer.appendChild(reviewDiv);
            });
        });
}

/**
 * Builds an element that displays the review.
 * @param {Review} review
 * @return {Element}
 */
function buildReviewDiv(review) {
    const headerDiv = document.createElement('div');
    headerDiv.classList.add('review-header');
    headerDiv.appendChild(document.createTextNode(
        review.merchantName + ' - ' + new Date(review.timestamp)));

    const ratingDiv = document.createElement('div');
    ratingDiv.classList.add('rating-body');
    ratingDiv.innerHTML = 'Rating = ' + review.rating;

    const bodyDiv = document.createElement('div');
    bodyDiv.classList.add('review-body');
    bodyDiv.innerHTML = review.text;

    const reviewDiv = document.createElement('div');
    reviewDiv.classList.add('review-div');
    reviewDiv.appendChild(headerDiv);
    reviewDiv.appendChild(ratingDiv);
    reviewDiv.appendChild(bodyDiv);

    return reviewDiv;
}

/** Fetches data and populates the UI of the page. */
function buildReviewUI() {
    // setPageTitle();
    fetchReviews();
}
