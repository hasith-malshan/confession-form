export function loadTrendingCategories() {
  const trendingList = document.querySelector(".trending-list");

  fetch("http://localhost:8080/api/confessions/trending-categories/week")
    .then(response => {
      if (!response.ok) {
        throw new Error("Failed to load trending categories");
      }
      return response.json();
    })
    .then(data => {
      trendingList.innerHTML = "";

      if (data.length === 0) {
        trendingList.innerHTML = `<p class="empty-sidebar-text">No trending categories this week.</p>`;
        return;
      }

      data.forEach((item, index) => {
        const trendingItem = `
          <div class="trending-item" data-category="${item.category}">
            <div class="trending-rank">#${index + 1}</div>
            <div class="trending-info">
              <p class="trending-category">${item.category}</p>
              <span class="trending-count">${item.count} confessions</span>
            </div>
          </div>
        `;

        trendingList.insertAdjacentHTML("beforeend", trendingItem);
      });
    })
    .catch(error => {
      console.error("Error loading trending categories:", error);
      trendingList.innerHTML = `<p class="empty-sidebar-text">Could not load trending categories.</p>`;
    });
}