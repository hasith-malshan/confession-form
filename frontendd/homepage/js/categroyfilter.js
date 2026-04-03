let selectedCategoryFilter = "";

export function setupCategoryFilter(onFilterChange) {
  const categoryFilterButtons = document.querySelectorAll(".category-filter-btn");

  categoryFilterButtons.forEach(button => {
    button.addEventListener("click", function () {
      const clickedCategory = this.dataset.category;

      if (selectedCategoryFilter === clickedCategory) {
        selectedCategoryFilter = "";
        this.classList.remove("active");
      } else {
        selectedCategoryFilter = clickedCategory;

        categoryFilterButtons.forEach(btn => btn.classList.remove("active"));
        this.classList.add("active");
      }

      onFilterChange();
    });
  });
}

export function getSelectedCategoryFilter() {
  return selectedCategoryFilter;
}