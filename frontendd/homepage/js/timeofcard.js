const confessionFeed = document.querySelector(".confession-feed");

export function getTimeAgo(createdAt) {
  const now = new Date();
  const postTime = new Date(createdAt);
  const diffInSeconds = Math.floor((now - postTime) / 1000);

  if (diffInSeconds < 5) {
    return "just now";
  } else if (diffInSeconds < 60) {
    return diffInSeconds + " sec ago";
  } else if (diffInSeconds < 3600) {
    return Math.floor(diffInSeconds / 60) + " min ago";
  } else if (diffInSeconds < 86400) {
    return Math.floor(diffInSeconds / 3600) + " hr ago";
  } else {
    return Math.floor(diffInSeconds / 86400) + " day ago";
  }
}

export function updateAllTimes() {
  const cards = document.querySelectorAll(".confession-card");

  cards.forEach(function(card) {
    const createdAt = card.dataset.createdAt;
    const timeElement = card.querySelector(".time-text");

    timeElement.textContent = getTimeAgo(createdAt);
  });
}

setInterval(updateAllTimes, 1000);