import { createCommentsSection } from "./htmlcard.js";

let currentCommentPostId = null;

export function openCommentModal(confessionId) {
  currentCommentPostId = confessionId;

  const commentModalOverlay = document.querySelector(".comment-modal-overlay");
  const commentText = document.getElementById("commentText");

  commentText.value = "";
  commentModalOverlay.style.display = "flex";
}

export function closeCommentModal() {
  const commentModalOverlay = document.querySelector(".comment-modal-overlay");
  commentModalOverlay.style.display = "none";
  currentCommentPostId = null;
}

export function submitComment() {
  const token = localStorage.getItem("token");
  const commentText = document.getElementById("commentText").value.trim();

  if (!token) {
    alert("Please login first");
    window.location.href = "login.html";
    return;
  }

  if (commentText === "") {
    return;
  }

  fetch(`http://localhost:8080/api/confessions/${currentCommentPostId}/comments`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({
      text: commentText
    })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to add comment");
      }
      return response.json();
    })
    .then(updatedConfession => {
      updateCommentSection(updatedConfession);
      closeCommentModal();
    })
    .catch(error => {
      console.error("Error adding comment:", error);
    });
}

function updateCommentSection(updatedConfession) {
  const card = document.querySelector(`.confession-card[data-id="${updatedConfession.id}"]`);

  if (!card) return;

  const oldCommentsWrapper = card.querySelector(".comments-wrapper");
  const newCommentsHtml = createCommentsSection(updatedConfession);

  oldCommentsWrapper.outerHTML = newCommentsHtml;
}