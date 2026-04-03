
import { fetchCurrentUser } from "./currentuser.js";
import { deleteConfession } from "./deletepost.js";
import { editpost, openEditModal } from "./editmodal.js";
import { createConfessionCard, createReactionSection } from "./htmlcard.js";
import { addReaction } from "./addreaction.js";

import { toggleComments } from "./commenttoggle.js";
import { openCommentModal, closeCommentModal, submitComment } from "./addcomment.js";

//mood filter
import { setupMoodFilter, getSelectedMoodFilter } from "./moodfilter.js";
import { setupCategoryFilter, getSelectedCategoryFilter } from "./categroyfilter.js";

//treding category
import { loadTrendingCategories } from "./trendingcategories.js";
import { loadMoodDistributionToday } from "./mooddistribution.js";

//comment modal
const commentModalOverlay = document.querySelector(".comment-modal-overlay");
const closeCommentModalBtn = document.querySelector(".close-comment-modal-btn");
const commentForm = document.querySelector(".comment-form");

let currentUser = null;

// Edit modal elements
const editModalOverlay = document.querySelector(".edit-modal-overlay");
const closeEditModalBtn = document.querySelector(".close-edit-modal-btn");
const editConfessionForm = document.querySelector(".edit-confession-form");

// Main page elements
const addConfessionBtn = document.querySelector(".add-confession-btn");
const modalOverlay = document.querySelector(".modal-overlay");
const closeModalBtn = document.querySelector(".close-modal-btn");

const toggleButtons = document.querySelectorAll(".toggle-btn");
const nameField = document.querySelector(".name-field");
const form = document.querySelector(".confession-form");
const confessionFeed = document.querySelector(".confession-feed");

const logoutBtn = document.getElementById("logoutBtn");

let isAnonymous = true;
let selectedMood = "";

// Open add confession modal
addConfessionBtn.addEventListener("click", function () {
  modalOverlay.style.display = "flex";
});

// Close add confession modal
closeModalBtn.addEventListener("click", function () {
  modalOverlay.style.display = "none";
});

// Close edit modal
closeEditModalBtn.addEventListener("click", function () {
  editModalOverlay.style.display = "none";
});

// Toggle anonymous / identity
toggleButtons[0].addEventListener("click", function () {
  toggleButtons[0].classList.add("active");
  toggleButtons[1].classList.remove("active");
  nameField.style.display = "none";
  isAnonymous = true;
});

toggleButtons[1].addEventListener("click", function () {
  toggleButtons[1].classList.add("active");
  toggleButtons[0].classList.remove("active");
  nameField.style.display = "block";
  isAnonymous = false;
});

// Mood selection
document.querySelectorAll(".mood-options button").forEach(btn => {
  btn.addEventListener("click", function () {
    selectedMood = this.dataset.mood;

    document.querySelectorAll(".mood-options button").forEach(b => {
      b.classList.remove("active");
    });

    this.classList.add("active");
  });
});

// Add confession form submit
form.addEventListener("submit", function (e) {
  e.preventDefault();

  const token = localStorage.getItem("token");

  if (!token) {
    alert("Please login first");
    window.location.href = "login.html";
    return;
  }

  const nameInput = document.getElementById("userName").value;
  const category = document.getElementById("category").value;
  const text = document.getElementById("confessionText").value;

  const confessionData = {
    name: isAnonymous ? "Anonymous" : nameInput,
    mood: selectedMood,
    category: category,
    text: text
  };

  newconfession(confessionData);
  
});

// Send new confession to backend
function newconfession(confessionData) {
  const token = localStorage.getItem("token");

  fetch("http://localhost:8080/api/confessions", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify(confessionData)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to save confession");
      }
      return response.json();
    })
    .then(savedConfession => {
      const cardHtml = createConfessionCard(savedConfession, currentUser);
      confessionFeed.insertAdjacentHTML("afterbegin", cardHtml);

       loadTrendingCategories();
      loadMoodDistributionToday();

      modalOverlay.style.display = "none";
      form.reset();
      nameField.style.display = "none";
      selectedMood = "";
      isAnonymous = true;

      document.querySelectorAll(".mood-options button").forEach(b => {
        b.classList.remove("active");
      });

      toggleButtons[0].classList.add("active");
      toggleButtons[1].classList.remove("active");
    })
    .catch(error => {
      console.error("Error:", error);
    });

    
}

// Logout
logoutBtn.addEventListener("click", function () {
  localStorage.removeItem("token");
  window.location.href = "login.html";
});

// Page load
document.addEventListener("DOMContentLoaded", async function () {
  currentUser = await fetchCurrentUser();

  setupMoodFilter(loadConfessions);
  setupCategoryFilter(loadConfessions);

  loadConfessions();
  loadTrendingCategories();
  loadMoodDistributionToday();
});

// Load confessions
function loadConfessions() {
  const selectedMood = getSelectedMoodFilter();
  const selectedCategory = getSelectedCategoryFilter();

  let url = "http://localhost:8080/api/confessions?page=0&size=10&sort=createdAt,desc";

  if (selectedMood) {
    url += `&mood=${encodeURIComponent(selectedMood)}`;
  }

  if (selectedCategory) {
    url += `&category=${encodeURIComponent(selectedCategory)}`;
  }

  fetch(url)
    .then(response => response.json())
    .then(data => {
      confessionFeed.innerHTML = "";

      data.content.forEach(confession => {
        const cardHtml = createConfessionCard(confession, currentUser);
        confessionFeed.insertAdjacentHTML("beforeend", cardHtml);
      });
    })
    .catch(error => {
      console.error("Error loading confessions:", error);
    });
}

// One event listener for both delete and edit and for reacting
confessionFeed.addEventListener("click", function (event) {
  const deleteBtn = event.target.closest(".delete-btn");
  const editBtn = event.target.closest(".edit-btn");
  const reactionBtn = event.target.closest(".reaction-btn");
  const toggleCommentsBtn = event.target.closest(".toggle-comments-btn");
const addCommentBtn = event.target.closest(".add-comment-btn");

  if (deleteBtn) {
    const confessionId = deleteBtn.dataset.id;
    deleteConfession(confessionId);
  }

  if (editBtn) {
    const confessionId = editBtn.dataset.id;
    openEditModal(confessionId);
  }

  if (reactionBtn) {
    const confessionId = reactionBtn.dataset.id;
    const reactionType = reactionBtn.dataset.reaction;
    addReaction(confessionId, reactionType);
  }

  if (toggleCommentsBtn) {
  toggleComments(toggleCommentsBtn);
}

if (addCommentBtn) {
  const confessionId = addCommentBtn.dataset.id;
  openCommentModal(confessionId);
}
});

// Edit form submit
editConfessionForm.addEventListener("submit", function (e) {
  e.preventDefault();
  editpost();
});


//comment modal
closeCommentModalBtn.addEventListener("click", function () {
  closeCommentModal();
});

commentModalOverlay.addEventListener("click", function (event) {
  if (event.target === commentModalOverlay) {
    closeCommentModal();
  }
});

commentForm.addEventListener("submit", function (e) {
  e.preventDefault();
  submitComment();
});
