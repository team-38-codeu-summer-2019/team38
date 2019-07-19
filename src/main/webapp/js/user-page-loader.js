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
          const messageForm = document.getElementById('message-form');
          messageForm.classList.remove('hidden');
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
        fetchAboutMe();
      })
      .then(() => {
        updateUniversities();
      })
}

/** Updates university options. */
function updateUniversities() {
  const universityInput = document.getElementsByName('university')[0];
  for (i = universityInput.options.length - 1; i; i--) {
    universityInput.remove(i);
  }
  if (typeof universities !== "undefined") {
    universities.forEach(function (university) {
      let ID = university.ID;
      let name = university.name;
      let len = universityInput.options.length;
      universityInput.options[len] = new Option(name, ID);
    })
  }
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
        if (responseJson.universityID >= 1) {
          universityInput.value = responseJson.universityID;
        }else{
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
  // showMessageFormIfViewingSelf();
  fetchUniversities();
  // fetchMessages();
}
