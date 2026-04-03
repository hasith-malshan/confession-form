export function loadMoodDistributionToday() {
  const moodDistributionList = document.querySelector(".mood-distribution-list");
  const moodDistributionTotal = document.querySelector(".mood-distribution-total");

  fetch("http://localhost:8080/api/confessions/mood-distribution/today")
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to load mood distribution");
      }
      return response.json();
    })
    .then(data => {
      moodDistributionList.innerHTML = "";

      if (data.length === 0) {
        moodDistributionList.innerHTML = `<p class="empty-sidebar-text">No confessions today yet.</p>`;
        moodDistributionTotal.textContent = "";
        return;
      }

      let totalCount = 0;

      data.forEach(item => {
        totalCount += item.count;

        const moodItem = `
          <div class="mood-row">
            <div class="mood-top">
              <span class="mood-name">${getMoodEmoji(item.mood)} ${item.mood}</span>
              <span class="mood-percent">${item.percentage.toFixed(1)}%</span>
            </div>
            <div class="mood-bar">
              <div class="mood-bar-fill ${getMoodClass(item.mood)}" style="width: ${item.percentage}%"></div>
            </div>
          </div>
        `;

        moodDistributionList.insertAdjacentHTML("beforeend", moodItem);
      });

      moodDistributionTotal.textContent = `Based on ${totalCount} confessions today`;
    })
    .catch(error => {
      console.error("Error loading mood distribution:", error);
      moodDistributionList.innerHTML = `<p class="empty-sidebar-text">Could not load mood distribution.</p>`;
      moodDistributionTotal.textContent = "";
    });
}

function getMoodEmoji(mood) {
  if (mood === "Love") return "❤️";
  if (mood === "Happy") return "😊";
  if (mood === "Sad") return "😢";
  if (mood === "Angry") return "😠";
  if (mood === "Fear") return "😨";
  if (mood === "Funny") return "😂";
  if (mood === "Embarrassing") return "😳";
  if (mood === "Overthinking") return "💭";
  return "✨";
}

function getMoodClass(mood) {
  if (mood === "Love") return "mood-love";
  if (mood === "Happy") return "mood-happy";
  if (mood === "Sad") return "mood-sad";
  if (mood === "Angry") return "mood-angry";
  if (mood === "Fear") return "mood-fear";
  if (mood === "Funny") return "mood-funny";
  if (mood === "Embarrassing") return "mood-embarrassing";
  if (mood === "Overthinking") return "mood-overthinking";
  return "";
}