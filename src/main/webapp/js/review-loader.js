

// Get ?merchant=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterMerchant = urlParams.get('merchant');

// URL must include ?merchant=XYZ parameter. If not, redirect to homepage.
if (!parameterMerchant) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterMerchant;
  document.title = parameterMerchant + ' - Review';
}


/** Fetches messages and add them to the page. */
function fetchReviews() {
  const url = '/reviews?merchant=' + parameterMerchant;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((reviews) => {
        const reviewsContainer = document.getElementById('reviews-container');
        if (reviews.length == 0) {
          reviewsContainer.innerHTML = '<p>This merchant has no reviews yet.</p>';
        } else {
          reviewsContainer.innerHTML = '';
        }
        reviews.forEach((review) => {
          const reviewDiv = buildReviewDiv(review);
          reviewsContainer.appendChild(reviewDiv);
        });

        setOverallRating(reviews);
      });
}

/** Calculate average rating from all reviews.
  * @param {Array of Reviews} reviews
  * @return {Float} avg rating
  */
function calculateAvgRating(reviews) {
    avg = 0.0;
    reviews.forEach((review) => {
        avg += review.rating;
    });
    avg /= reviews.length;
    return avg;
}

/** Set the overall rating from a particular merchant.
  * @param {Array of Reviews} reviews
  */
function setOverallRating(reviews) {
    const ratingContainer = document.getElementById('overall-rating-container');

    console.log(reviews);
    if (reviews.length > 0) {
        rating = calculateAvgRating(reviews);
        ratingContainer.innerHTML = '<p>Rating = ' + rating + '</p>';
    } else {
        ratingContainer.innerHTML = '';
    }

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
      review.user + ' - ' + new Date(review.timestamp)));

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

// set merchant name in all hidden input forms
function setMerchantInput() {
    document.getElementById('merchant-input').value = parameterMerchant
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  fetchReviews();
  setMerchantInput();
}

