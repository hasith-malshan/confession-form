

// Profile icon
const profileIcon = document.getElementById("profileIcon");
if (profileIcon) {
  const name = getUsername();
  profileIcon.textContent = name ? name.charAt(0).toUpperCase() : "?";
}
