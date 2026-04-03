let selectedMoodFilter = "";

export function setupMoodFilter(onFilterChange) {
  const moodFilterButtons = document.querySelectorAll(".mood-filter-btn");

  moodFilterButtons.forEach(button => {
    button.addEventListener("click", function () {
      const clickedMood = this.dataset.mood;

      if (selectedMoodFilter === clickedMood) {
        selectedMoodFilter = "";
        this.classList.remove("active");
      } else {
        selectedMoodFilter = clickedMood;

        moodFilterButtons.forEach(btn => btn.classList.remove("active"));
        this.classList.add("active");
      }

      onFilterChange();
    });
  });
}

export function getSelectedMoodFilter() {
  return selectedMoodFilter;
}