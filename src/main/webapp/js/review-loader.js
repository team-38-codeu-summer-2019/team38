

// Get ?merchant=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterMerchantID = urlParams.get('id');
//const merchantName = getMerchantName(parameterMerchantID);
// URL must include ?id=XYZ parameter. If not, redirect to homepage.
if (!parameterMerchantID) {
  window.location.replace('/search.html');
}

//function getMerchantName(merchantID) {
//    const url = '/merchants?id=' + id
//    let response = await fetch(url)
//    let json = await response.json()
//
//    return json.name
//}
/** [DEPRECATED] Sets the page title based on the URL parameter username. */
//function setPageTitle() {
//  document.getElementById('page-title').innerText = merchantName;
//  document.title = merchantName + ' - Review';
//}

/**
 * Shows the review form if the user is logged in.
 */
function showReviewFormIfLogin() {
    console.log("here login check")
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn) {
          const reviewForm = document.getElementById('review-form');
          reviewForm.classList.remove('hidden');
        }
      });
}

/** Fetches messages and add them to the page. */
function fetchReviews() {
console.log("here fetch reviews")
  const url = '/reviews?id=' + parameterMerchantID;
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
        ratingContainer.innerHTML = '<p>Rating = ' + rating.toFixed(1) + '</p>';
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
  const time = new Date(review.timestamp)
  const timeStr = time.getFullYear() + '-' + (time.getMonth()+1) + '-' + time.getDate()

  const card = document.createElement('div');
  card.classList.add("card");

  const headerDiv = document.createElement('div');
  headerDiv.classList.add('review-header');
  headerDiv.classList.add('card-header');
  headerDiv.appendChild(document.createTextNode(review.userEmail));
  card.appendChild(headerDiv)

  const bodyCard = document.createElement('div');
  bodyCard.classList.add('card-body')
  card.appendChild(bodyCard)
  
  const ratingDiv = document.createElement('div');
  ratingDiv.classList.add('rating-body');
  ratingDiv.innerHTML = 'Rating: ' + review.rating + '/5';
  bodyCard.appendChild(ratingDiv);

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('review-body');
  bodyDiv.classList.add('card-text');
  bodyDiv.innerHTML = review.text;
  bodyCard.appendChild(bodyDiv);

  const timeFoot = document.createElement('footer');
  timeFoot.classList.add('blockquote-footer');
  timeFoot.innerHTML = timeStr;
  card.appendChild(timeFoot);

  const reviewDiv = document.createElement('div');
  reviewDiv.classList.add('review-div');
  

  return card;
}

// set merchant name in all hidden input forms
function setMerchantInput() {
    document.getElementById('merchant-input').value = parameterMerchantID
}

/** Fetches data and populates the UI of the page. */
function buildReviewUI() {
  // setPageTitle();
  showReviewFormIfLogin();
  fetchReviews();
  setMerchantInput();
}

