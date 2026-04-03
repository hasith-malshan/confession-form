import { createReactionSection } from "./htmlcard.js";

export function addReaction(confessionId, reactionType) {
  const token = localStorage.getItem("token");

  if (!token) {
    alert("Please login first to react");
    window.location.href = "login.html";
    return;
  }

  fetch(`http://localhost:8080/api/confessions/${confessionId}/reactions`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({
      reactionType: reactionType
    })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to add reaction");
      }
      return response.json();
    })
    .then(updatedConfession => {
      updateReactionSection(updatedConfession);
    })
    .catch(error => {
      console.error("Error adding reaction:", error);
    });
}

function updateReactionSection(updatedConfession) {
  const card = document.querySelector(`.confession-card[data-id="${updatedConfession.id}"]`);

  if (!card) return;

  const oldReactionSection = card.querySelector(".reaction-section");
  const newReactionSectionHtml = createReactionSection(updatedConfession);

  oldReactionSection.outerHTML = newReactionSectionHtml;
}