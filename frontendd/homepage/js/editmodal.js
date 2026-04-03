let editingConfessionId = null;

const editModalOverlay = document.querySelector(".edit-modal-overlay");
const editConfessionText = document.getElementById("editConfessionText");

export function openEditModal(id) {
  editingConfessionId = id;

  const card = document.querySelector(`.confession-card[data-id="${id}"]`);
  const textElement = card.querySelector(".confession-text");

  editConfessionText.value = textElement.textContent;
  editModalOverlay.style.display = "flex";
}

export function editpost() {
  const updatedText = editConfessionText.value.trim();
  const token = localStorage.getItem("token");

  if (updatedText === "") return;

  fetch(`http://localhost:8080/api/confessions/${editingConfessionId}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: JSON.stringify({ text: updatedText })
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to update confession");
      }
      return response.json();
    })
    .then(updatedConfession => {
      const card = document.querySelector(`.confession-card[data-id="${editingConfessionId}"]`);
      const textElement = card.querySelector(".confession-text");

      textElement.textContent = updatedConfession.text;
      editModalOverlay.style.display = "none";
      editingConfessionId = null;
    })
    .catch(error => {
      console.error("Error updating confession:", error);
    });
}