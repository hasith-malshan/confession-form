export function toggleComments(button) {
  const commentsWrapper = button.closest(".comments-wrapper");
  const commentsList = commentsWrapper.querySelector(".comments-list");

  if (commentsList.style.display === "none" || commentsList.style.display === "") {
    commentsList.style.display = "block";
    button.textContent = "Hide Comments";
  } else {
    commentsList.style.display = "none";
    button.textContent = "Show Comments";
  }
}