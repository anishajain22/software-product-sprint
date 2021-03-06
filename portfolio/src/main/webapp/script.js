// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random facts to the page.
 */
function addRandomFact() {
  const facts =
      ['I like sketching a lot!', 'I am a foodie<3', 'I have a massive sweet tooth!', 'A big movie buff:)'];

  // Pick a random fact
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}


function loadComments() {
    fetch('/data').then(response => response.json()).then((json) => {
    const statsListElement = document.getElementById('comments-container');
    statsListElement.innerHTML = '';
    for(let element of json){
        statsListElement.appendChild(createListElement(element.text));
        if(element.imageUrl){
            var img = document.createElement('img'); 
            img.src =  element.imageUrl;
            statsListElement.appendChild(img);
        }
    }
    console.log(json);
    });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/blobstore-upload-url')
      .then((response) => {
        return response.text();
      })
      .then((uploadUrl) => {
        const messageForm = document.getElementById('form');
        messageForm.action = uploadUrl;
      });
}



