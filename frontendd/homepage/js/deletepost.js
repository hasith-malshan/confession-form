import { loadTrendingCategories } from "./trendingcategories.js";
import { loadMoodDistributionToday } from "./mooddistribution.js";



export function deleteConfession(id) {
  const token = localStorage.getItem("token");

  fetch(`http://localhost:8080/api/confessions/${id}`, {
    method: "DELETE",
    headers: {
      "Authorization": "Bearer " + token
    }
  })
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to delete confession");
      }

      const card = document.querySelector(`.confession-card[data-id="${id}"]`);
      if (card) {
        card.remove();
        loadTrendingCategories();
loadMoodDistributionToday();
      }
    })
    .catch(error => {
      console.error("Error deleting confession:", error);
    });
}

