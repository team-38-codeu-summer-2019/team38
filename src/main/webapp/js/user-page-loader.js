/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Get ?user=XYZ parameter value
const urlParams = new URLSearchParams(window.location.search);
const parameterUsername = urlParams.get('user');

// URL must include ?user=XYZ parameter. If not, redirect to homepage.
if (!parameterUsername) {
  window.location.replace('/');
}

/** Sets the page title based on the URL parameter username. */
function setPageTitle() {
  document.getElementById('page-title').innerText = parameterUsername;
  document.title = parameterUsername + ' - User Page';
}

/**
 * Shows the message form if the user is logged in and viewing their own page.
 */
function showMessageFormIfViewingSelf() {
  fetch('/login-status')
      .then((response) => {
        return response.json();
      })
      .then((loginStatus) => {
        if (loginStatus.isLoggedIn && loginStatus.username == parameterUsername) {
          const aboutMeForm = document.getElementById('about-me-form');
          aboutMeForm.classList.remove('hidden');
          // const messageForm = document.getElementById('message-form');
          // messageForm.classList.remove('hidden');
        }
      });
}

var universities;

/** Fetches university options. */
function fetchUniversities() {
  const url = '/university';
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((responseJson) => {
        universities = responseJson;
      })
      .then(() => {
        const universityInput = document.getElementsByName('university')[0];
        autocomplete(universityInput, universities);
        const universityyInput = document.getElementsByName('universityy')[0];

        /*changes POSTed value*/
        const form = document.getElementById("user-form");
        form.addEventListener("submit", function (e) {
          e.preventDefault();
          universityInput.disabled = true;
          universityyInput.value = -1;
          for (i = 0; i < universities.length; i++){
            if (universities[i].name == universityInput.value){
              universityyInput.value = universities[i].ID;
              break;
            }
          }
          form.submit();
        })
      })
}

/** Fetches About Me. */
function fetchAboutMe() {
  const url = '/about?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((responseJson) => {
        const universityInput = document.getElementsByName('university')[0];
        universityInput.value = responseJson.universityName;
        if (responseJson.universityName == '') {
          responseJson.universityName = 'This user has not entered their university yet.';
        }
        const universityContainer = document.getElementById('university-container');
        universityContainer.innerHTML = responseJson.universityName;

        const aboutMeInput = document.getElementsByName('about-me')[0];
        aboutMeInput.value = responseJson.aboutMe;
        if (responseJson.aboutMe == '') {
          responseJson.aboutMe = 'This user has not entered their about me yet.';
        }
        const aboutMeContainer = document.getElementById('about-me-container');
        aboutMeContainer.innerHTML = responseJson.aboutMe;
      });
}

/* Start of https://www.w3schools.com/howto/howto_js_autocomplete.asp */

function autocomplete(inp, arr) {
  /*the autocomplete function takes two arguments,
  the text field element and an array of possible autocompleted values:*/
  var currentFocus;
  /*execute a function when someone writes in the text field:*/
  inp.addEventListener("input", function(e) {
    var a, b, i, val = this.value;
    /*close any already open lists of autocompleted values*/
    closeAllLists();
    if (!val) { return false;}
    currentFocus = -1;
    /*create a DIV element that will contain the items (values):*/
    a = document.createElement("DIV");
    a.setAttribute("id", this.id + "autocomplete-list");
    a.setAttribute("class", "autocomplete-items");
    /*append the DIV element as a child of the autocomplete container:*/
    this.parentNode.appendChild(a);
    /*for each item in the array...*/
    for (i = 0; i < arr.length; i++) {
      /*check if the item starts with the same letters as the text field value:*/
      if (arr[i].name.substr(0, val.length).toUpperCase() == val.toUpperCase()) {
        /*create a DIV element for each matching element:*/
        b = document.createElement("DIV");
        /*make the matching letters bold:*/
        b.innerHTML = "<strong>" + arr[i].name.substr(0, val.length) + "</strong>";
        b.innerHTML += arr[i].name.substr(val.length);
        /*insert a input field that will hold the current array item's value:*/
        b.innerHTML += "<input type='hidden' value='" + arr[i].name + "'>";
        /*execute a function when someone clicks on the item value (DIV element):*/
        b.addEventListener("click", function(e) {
          /*insert the value for the autocomplete text field:*/
          inp.value = this.getElementsByTagName("input")[0].value;
          /*close the list of autocompleted values,
          (or any other open lists of autocompleted values:*/
          closeAllLists();
        });
        a.appendChild(b);
      }
    }
  });
  /*execute a function presses a key on the keyboard:*/
  inp.addEventListener("keydown", function(e) {
    var x = document.getElementById(this.id + "autocomplete-list");
    if (x) x = x.getElementsByTagName("div");
    if (e.keyCode == 40) {
      /*If the arrow DOWN key is pressed,
      increase the currentFocus variable:*/
      currentFocus++;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 38) { //up
      /*If the arrow UP key is pressed,
      decrease the currentFocus variable:*/
      currentFocus--;
      /*and and make the current item more visible:*/
      addActive(x);
    } else if (e.keyCode == 13) {
      /*If the ENTER key is pressed, prevent the form from being submitted,*/
      e.preventDefault();
      if (currentFocus > -1) {
        /*and simulate a click on the "active" item:*/
        if (x) x[currentFocus].click();
      }
    }
  });
  function addActive(x) {
    /*a function to classify an item as "active":*/
    if (!x) return false;
    /*start by removing the "active" class on all items:*/
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    /*add class "autocomplete-active":*/
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    /*a function to remove the "active" class from all autocomplete items:*/
    for (var i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    /*close all autocomplete lists in the document,
    except the one passed as an argument:*/
    var x = document.getElementsByClassName("autocomplete-items");
    for (var i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  /*execute a function when someone clicks in the document:*/
  document.addEventListener("click", function (e) {
    closeAllLists(e.target);
  });
}

/* End of https://www.w3schools.com/howto/howto_js_autocomplete.asp */

/** Fetches messages and add them to the page. */
function fetchMessages() {
  const url = '/messages?user=' + parameterUsername;
  fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((messages) => {
        const messagesContainer = document.getElementById('message-container');
        if (messages.length == 0) {
          messagesContainer.innerHTML = '<p>This user has no posts yet.</p>';
        } else {
          messagesContainer.innerHTML = '';
        }
        messages.forEach((message) => {
          const messageDiv = buildMessageDiv(message);
          messagesContainer.appendChild(messageDiv);
        });
      });
}

/**
 * Builds an element that displays the message.
 * @param {Message} message
 * @return {Element}
 */
function buildMessageDiv(message) {
  const headerDiv = document.createElement('div');
  headerDiv.classList.add('message-header');
  headerDiv.appendChild(document.createTextNode(
      message.user + ' - ' + new Date(message.timestamp)));

  const bodyDiv = document.createElement('div');
  bodyDiv.classList.add('message-body');
  bodyDiv.innerHTML = message.text;

  const messageDiv = document.createElement('div');
  messageDiv.classList.add('message-div');
  messageDiv.appendChild(headerDiv);
  messageDiv.appendChild(bodyDiv);

  return messageDiv;
}

/** Fetches data and populates the UI of the page. */
function buildUI() {
  setPageTitle();
  showMessageFormIfViewingSelf();
  fetchUniversities();
  fetchAboutMe();
  // fetchMessages();
}
